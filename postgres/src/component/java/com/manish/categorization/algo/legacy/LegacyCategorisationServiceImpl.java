package com.manish.categorization.algo.legacy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.manish.categorization.BaseType;
import com.manish.categorization.CategorizationSource;
import com.manish.categorization.Category;
import com.manish.categorization.Container;
import com.manish.categorization.algo.mcc.MCCCategorizationService;
import com.manish.categorization.db.GranularCategoryCache;
import com.manish.categorization.db.InvTransTypeCatMapCache;
import com.manish.categorization.db.SlamBangWord;
import com.manish.categorization.db.TransactionCategory;
import com.manish.categorization.db.TransactionCategoryCache;
import com.manish.categorization.rest.dto.CategorizationRequest;
import com.manish.categorization.rest.dto.Configurations;
import com.manish.categorization.rest.dto.EnrichedTransactionResponse;
import com.manish.categorization.rest.dto.TransactionRequest;
import com.manish.categorization.rest.dto.TransactionResponse;
import com.manish.categorization.sdp.config.CaasConfigBean;
import com.manish.categorization.util.CommonHelper;
import com.manish.categorization.util.Constants;
import com.yodlee.simpledescservice.rest.dto.SimpleDescriptionRequest;
import com.yodlee.simpledescservice.service.SimpleDescriptionService;
import com.yodlee.simpledescservice.service.SimpleDescriptionServiceImpl;

@Service
public class LegacyCategorisationServiceImpl implements LegacyCategoisationService {

	private static Logger logger = LogManager.getLogger(LegacyCategorisationServiceImpl.class);

	@Autowired
	TransactionCategoryCache categoryCache;
	
	@Autowired
	InvTransTypeCatMapCache invTransTypeCatMapCache;
	
	@Autowired
	GranularCategoryCache granularCategoryCache;

	@Autowired
	ExecutorCompletionService<List<TransactionResponse>> legacyExecutorCompletionService;
	
	@Autowired
	MCCCategorizationService mccCategorizationService;
	
	@Autowired
	CaasConfigBean caasConfigBean;
	
	SimpleDescriptionService simpleDescriptionService = null;
	
	@Override
	public List<TransactionResponse> categorise(CategorizationRequest request) {
		
		if(caasConfigBean.getCaas().isParallelEnabled())
			return applyParallelCategorization(request);
		else
			return applyCategorization(request);
		
	}
	/**
	 * This method does the parallel categorisation of the transactions by sending the transactions in batches of legacyBatchSize to the 
	 * new spawned threads for Legacy Categorisation.
	 * @param request
	 * @return
	 */
	private List<TransactionResponse> applyParallelCategorization(CategorizationRequest request) {
		
		List<TransactionRequest> txnRequestList = new ArrayList<TransactionRequest>(request.getTxns());
		List<List<TransactionRequest>> subLists = Lists.partition(txnRequestList, caasConfigBean.getCaas().getLegacyBatchSize());
		List<Future<List<TransactionResponse>>> futureList = new ArrayList<Future<List<TransactionResponse>>>();
		CountDownLatch countDownLatch = new CountDownLatch(subLists.size());
		request.setTxns(null);
		List<TransactionResponse> transactionResponse = new ArrayList<TransactionResponse>();
		for (List<TransactionRequest> subList : subLists) {
			try {
				CategorizationRequest categorizationRequest = (CategorizationRequest) request.clone();
				categorizationRequest.setTxns(subList);
				Future<List<TransactionResponse>> futureObj = submitCategorizeThread(categorizationRequest, countDownLatch);
				if(futureObj != null){
					futureList.add(futureObj);
				}	
			} catch (CloneNotSupportedException exception) {
				if(logger.isErrorEnabled())
					logger.error("Exception while cloning CategorizationRequest",exception);	
			}
		}
		long startWaitingTime = System.currentTimeMillis();	
		try {
			countDownLatch.await(caasConfigBean.getCaas().getLegacyThreadTimeout(),TimeUnit.MILLISECONDS);
		} catch (InterruptedException exception) {
			if(logger.isErrorEnabled())
				logger.error("Interruped Exception while awaiting for legacy thread execution",exception);
		}
		if(logger.isInfoEnabled())
			logger.info("Legacy Categorisation CountdownLatch waited for "+(System.currentTimeMillis()-startWaitingTime)+" milliseconds");
		for (Future<List<TransactionResponse>> future : futureList) {	
				try {
						List<TransactionResponse> txnResList = future.get(caasConfigBean.getCaas().getExtraThreadDelay(), TimeUnit.MILLISECONDS);
						if(txnResList != null)
							transactionResponse.addAll(txnResList);
						else{
							if(logger.isErrorEnabled())
								logger.error("Transaction Result List by Legacy Thread is null for memId "+request.getMemId());
						}
				}catch (TimeoutException exception) {
					future.cancel(Boolean.TRUE);
					if(logger.isErrorEnabled())
						logger.error("Timeout occured for Legacy Thread and it is cancelled for memId "+request.getMemId(),exception);
				}catch (InterruptedException exception) {
					if(logger.isErrorEnabled())
						logger.error("InterruptedException occured for Legacy Thread and it is cancelled for memId "+request.getMemId(),exception);
				} catch (ExecutionException exception) {
					if(logger.isErrorEnabled())
						logger.error("ExecutionException occured for Legacy Thread and it is cancelled for memId "+request.getMemId(),exception);
				}
		}
		return transactionResponse;
	}
	/**
	 * This categorizes the transactions by categorising the transaction one by one in the sequential order.
	 * @param request
	 * @return
	 */
	private List<TransactionResponse> applyCategorization(CategorizationRequest request) {
		
		List<TransactionResponse> txnResponseList = new ArrayList<TransactionResponse>();
		for (TransactionRequest txnRequest : request.getTxns()) {
			TransactionResponse txnResponse = categorise(request, txnRequest);
			if (txnResponse != null) {
				txnResponseList.add(txnResponse);
			}
		}
		return txnResponseList;
	}

	@SuppressWarnings("null")
	@Override
	public TransactionResponse categorise(CategorizationRequest request, TransactionRequest txn) {
		long startTime = System.currentTimeMillis();
		Configurations configurations = request.getConfigurations();
		if(Container.INVESTMENT.toString().equals(request.getContainer())){
			if(StringUtils.isNotEmpty(txn.getTransactionType())){
				if(invTransTypeCatMapCache != null){
					Map<String,String> txnTypeToCatMap = invTransTypeCatMapCache.getTxnTypeToTxnCatMap();
					if(txnTypeToCatMap != null){
						String txnCategoryName = txnTypeToCatMap.get(txn.getTransactionType().toLowerCase());
						if(StringUtils.isNotEmpty(txnCategoryName) && !Category.UNCATEGORIZED.toString().equalsIgnoreCase(txnCategoryName)){
							EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
							TransactionCategory category = categoryCache.getTransactionCategory(txnCategoryName,
									configurations.isMergerEnabled());
							if(category != null){
								if(configurations.isMergerEnabled()){
									txnResponse.setCategory(category.getMergedTxnCatName());
								}else{
									txnResponse.setCategory(category.getName());
								}	
							}else {
								if(logger.isErrorEnabled())
									logger.error("TransactionCategory is null for txnCategoryName  : " + txnCategoryName + " for  Unique Txn Id::"+ request.getUniqueTrackingId());
							}
							if (category != null) {
								txnResponse.setCategoryDisplayName(category.getDisplayName());
							}
							txnResponse.setTransactionId(txn.getTransactionId());
							txnResponse.setCategorisationSource(CategorizationSource.INVESTMENT_TRANSACTION_TYPE_MAPPING.getSource());
							txnResponse.setCategorisationSourceId(CategorizationSource.INVESTMENT_TRANSACTION_TYPE_MAPPING.getSourceId());
							String granularCategory = CommonHelper.getDefaultGranularCategory(granularCategoryCache, configurations.isMergerEnabled(),
									configurations.isMeerkat(), txnResponse);
							txnResponse.setGranularCategory(granularCategory);
							if(request.getConfigurations().isMeerkat()
									&& request.isTdeV2() && StringUtils.isEmpty(txnResponse.getSimpleDescription())){
								boolean isSimpleDescEnabled=configurations.isSimpleDescEnabled();
								SimpleDescriptionRequest simpleDescRequest = new SimpleDescriptionRequest();
								if(isSimpleDescEnabled){
									simpleDescriptionService = new SimpleDescriptionServiceImpl();
									BeanUtils.copyProperties(txnResponse, simpleDescRequest);
									simpleDescRequest.setVersion(configurations.getSimpleDescVersion());
									simpleDescRequest.setGeoLocationEnabledInSD(configurations.isGeoLocationEnabledInSD());
									simpleDescRequest.setPlainTextDesc(txn.getDescription());
									simpleDescRequest.setLocale(configurations.getLocale());
									simpleDescRequest.setContainer(request.getContainer());
									simpleDescRequest.setTxnId(txn.getTransactionId().toString());
									simpleDescRequest.setCategory(txnResponse.getCategory());
									simpleDescRequest.setTxnBaseType(txn.getBaseType());
									txnResponse.setSimpleDescription(simpleDescriptionService.deriveSimpleDescription(simpleDescRequest));
								}
							}
							long endTime = System.currentTimeMillis();
							if(logger.isInfoEnabled())
								logger.info("Processing end : categorisation through Legacy - (Type to Category Mapping)...requestSentTime :" + startTime + ",responseReceivedTime :" + endTime + ",totalTime in ms:"+ (endTime - startTime));
							return txnResponse;
						}
					}
				}
			}	
		}
		KeywordAnalyzer analyzer = new KeywordAnalyzer();
		Set<String> tokens = analyzer.tokens(txn.getDescription());
		analyzer.close();
		Search search = Search.getInstance();
		List<SlamBangWord> hits = new ArrayList<SlamBangWord>();
		SlamBangWord hit = prioritizationAlgorithm(request, txn, tokens, search, hits, true);
		if (hit != null && Category.REFUNDS_ADJUSTMENTS.toString().equals(hit.getTransactionCategoryName())) {
			hit = prioritizationAlgorithm(request, txn, tokens, search, hits, false);
		}
		EnrichedTransactionResponse txnResponse = null;
		if (hit != null) {
			txnResponse = new EnrichedTransactionResponse();
			TransactionCategory category = categoryCache.getTransactionCategory(hit.getTransactionCategoryName(),
					configurations.isMergerEnabled());
			
			if (category != null) {
				txnResponse.setCategoryDisplayName(category.getDisplayName());
			}else {
				if(logger.isErrorEnabled())
					logger.error("TransactionCategory is null for txnCategoryName  : " + hit.getTransactionCategoryName() + " for  Unique Txn Id::"+ request.getUniqueTrackingId());
			}
			if(configurations.isMergerEnabled()){
				txnResponse.setCategory(category.getMergedTxnCatName());
			}else{
				txnResponse.setCategory(hit.getTransactionCategoryName());
			}
			txnResponse.setKeyword(hit.getSlamBangWord());
			boolean isDummyMerchant = hit.getIsDummyMerchant() == null ? false : (hit.getIsDummyMerchant().longValue() == 1 ? true : false);
			if(!isDummyMerchant){
				txnResponse.setMerchant(hit.getMerchantName());
				txnResponse.setMerchantId(hit.getMerchantId());
			}
			txnResponse.setTransactionId(txn.getTransactionId());
			txnResponse.setCategorisationSource(CategorizationSource.LEGACY.getSource());
			txnResponse.setCategorisationSourceId(CategorizationSource.LEGACY.getSourceId());
			//In Legacy Merchant Name is being set as Simple Description
			if(!isDummyMerchant){
				txnResponse.setSimpleDescription(hit.getMerchantName());
			}
		}
		if(configurations.getMccRule() != null && configurations.getMccRule().longValue() == 2){
			if(isUncategorized(txnResponse)){
				txnResponse = (EnrichedTransactionResponse) mccCategorizationService.categorise(txn, request);
			}
		}
		if(isUncategorized(txnResponse)){
			txnResponse = populateCategory(request, txn);
		}
		if(txnResponse != null){
			if ((Container.BANK.toString().equalsIgnoreCase(request.getContainer())
					|| Container.CARD.toString().equalsIgnoreCase(request.getContainer()) 
					|| Container.LOAN.toString().equalsIgnoreCase(request.getContainer())
					|| Container.INVESTMENT.toString().equalsIgnoreCase(request.getContainer())
					&& request.isTdeV2())) {
				String granularCategory = CommonHelper.getDefaultGranularCategory(granularCategoryCache, request.getConfigurations()
										.isMergerEnabled(),request.getConfigurations().isMeerkat(),txnResponse);
				txnResponse.setGranularCategory(granularCategory);
			}
			
			if(Container.INVESTMENT.toString().equalsIgnoreCase(request.getContainer()) 
					&& request.getConfigurations().isMeerkat()
					&& request.isTdeV2() && StringUtils.isEmpty(txnResponse.getSimpleDescription())){
				boolean isSimpleDescEnabled=configurations.isSimpleDescEnabled();
				SimpleDescriptionRequest simpleDescRequest = new SimpleDescriptionRequest();
				if(isSimpleDescEnabled){
					simpleDescriptionService = new SimpleDescriptionServiceImpl();
					BeanUtils.copyProperties(txnResponse, simpleDescRequest);
					simpleDescRequest.setVersion(configurations.getSimpleDescVersion());
					simpleDescRequest.setGeoLocationEnabledInSD(configurations.isGeoLocationEnabledInSD());
					simpleDescRequest.setPlainTextDesc(txn.getDescription());
					simpleDescRequest.setLocale(configurations.getLocale());
					simpleDescRequest.setContainer(request.getContainer());
					simpleDescRequest.setTxnId(txn.getTransactionId().toString());
					simpleDescRequest.setCategory(txnResponse.getCategory());
					simpleDescRequest.setTxnBaseType(txn.getBaseType());
					txnResponse.setSimpleDescription(simpleDescriptionService.deriveSimpleDescription(simpleDescRequest));
				}
			}
		}
		return txnResponse;
	}
	/**
	 * This method applies the prioritization algorithm in legacy categorisation
	 * @param request
	 * @param txn
	 * @param tokens
	 * @param search
	 * @param hits
	 * @param baseTypeCheck
	 * @return
	 */
	public SlamBangWord prioritizationAlgorithm(CategorizationRequest request,
			TransactionRequest txn, Set<String> tokens, Search search, List<SlamBangWord> hits,
			boolean baseTypeCheck) {
		Configurations configurations = request.getConfigurations();
		for (String token : tokens) {
			List<SlamBangWord> slamBangWordList = search.search(token, txn.getBaseType(),
					request.getContainer(), configurations.isMergerEnabled(), true);
			
			if (slamBangWordList != null && slamBangWordList.size() > 0) {
				for (SlamBangWord slamBangWord : slamBangWordList) {
					CategorizationPattern pattern = CategorizationPattern.getPattern(slamBangWord,
							request.getSumInfoId(), request.getRegion());
					if (pattern != null) {
						for (CategorizationPattern lookupPattern : CategorizationPattern.patterns) {
							if (pattern.equals(lookupPattern)) {
								slamBangWord.setOrder(lookupPattern.getPriority());
								slamBangWord.setSource(lookupPattern.getCategorizationSource());
								hits.add(slamBangWord);
								break;
							}
						}
					}
				}
			}
		}
		SlamBangWord hit = null;
		if (!hits.isEmpty()) {
			Collections.sort(hits);
			for (SlamBangWord slamBangWord : hits) {
				if (slamBangWord.getPriority() > 0) {
					hit = slamBangWord;
					break;
				}
			}
		}
		return hit;
	}
	
	private Future<List<TransactionResponse>> submitCategorizeThread(CategorizationRequest categorizationRequest,CountDownLatch countDownLatch) {
		
			Callable<List<TransactionResponse>> categorizeThread = new Callable<List<TransactionResponse>>(){
				@Override
				public List<TransactionResponse> call() {
					List<TransactionResponse> transactionResponseList = applyCategorization(categorizationRequest);
					countDownLatch.countDown();
					return transactionResponseList;
				}
			}; 
			return legacyExecutorCompletionService.submit(categorizeThread);
	}
	/**
	 * This method checks whether the TransactionResponse is Uncategorised
	 * @param txnResponse
	 * @return
	 */
	private boolean isUncategorized(TransactionResponse txnResponse){
		boolean isUncategorized = Boolean.FALSE;
		if (txnResponse == null || StringUtils.isEmpty(txnResponse.getCategory())
				|| txnResponse.getCategory().equals(Category.UNCATEGORIZED.toString())) {
			isUncategorized = Boolean.TRUE;
		}
		return isUncategorized;
	}
	/**
	 * The Other Expenses and Other Income should be populated according to the base type if the below conditions are being met
	 * 1) TDE should be disabled for the request else TDE will take care of populating Other Income/Other Expense
	 * 2) The base type should be debit/credit else Uncategorized should be populated if base type is not available or basetype is unknown/other
	 * 3) This should be applicable Only for Bank and Card container. For example: For a investment transaction buying a stock may not be a real expense
	 */
	private EnrichedTransactionResponse populateCategory(CategorizationRequest request,TransactionRequest txn){

		Configurations configurations = request.getConfigurations();
		String baseType = txn.getBaseType();
		String container = request.getContainer();
		String uniqueTrackingId= request.getUniqueTrackingId();
		boolean meerkat = configurations.isMeerkat() && (Container.BANK.toString().equals(container) || Container.CARD.toString().equals(container)
				|| Container.INVESTMENT.toString().equals(container) || Container.LOAN.toString().equals(container));
		//If meerkat cannot be applied to a transaction then legacy should ultimately give the category - Other Expense/Other Income if base type is present
		//Uncategorized if base type is null. If Meerkat can be applied then meerkat will be giving the final category if legacy is not able to give.
		boolean isMeerkatApplicable = isMeerkatApplicable(meerkat, txn);
		if(!isMeerkatApplicable){
			if (baseType != null
					&& (baseType.equals(BaseType.DEBIT.toString()) || baseType
							.equals(BaseType.CREDIT.toString()))) {
				if(StringUtils.isNotEmpty(container) && (container.equals(Container.BANK.toString()) || container.equals(Container.CARD.toString()))){
					EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
					if(baseType.equals(BaseType.DEBIT.toString()))
						txnResponse.setCategory(Category.OTHER_EXPENSES.toString());
					else if(baseType.equals(BaseType.CREDIT.toString()))
						txnResponse.setCategory(Category.OTHER_INCOME.toString());
					TransactionCategory category = categoryCache.getTransactionCategory(txnResponse.getCategory(),configurations.isMergerEnabled());
					if (category != null) {
						txnResponse.setCategoryDisplayName(category.getDisplayName());
					}
					txnResponse.setTransactionId(txn.getTransactionId());
					txnResponse.setCategorisationSource(CategorizationSource.NONE.getSource());
					txnResponse.setCategorisationSourceId(CategorizationSource.NONE.getSourceId());
					return txnResponse;
				}
				//Mark the investment transaction uncategorized if TDE is not  applicable and the response is null.
				if( Container.INVESTMENT.toString().equals(container)) {
					return uncategorizedCategoryResponse(txn, configurations,uniqueTrackingId);
				}
			}
		}else if(isMeerkatApplicable){
			if( Container.INVESTMENT.toString().equals(container)) {
				List<String> investmentTransactionTypesList  = new ArrayList<String>();
				if(StringUtils.isNotEmpty(caasConfigBean.getCaas().getInvestmentTransactionTypes())){
					String[] investmentTransactionTypesArr = caasConfigBean.getCaas().getInvestmentTransactionTypes().split(Constants.COMMA);
					investmentTransactionTypesList = Arrays.asList(investmentTransactionTypesArr);
				}
				if (investmentTransactionTypesList != null) {
					return uncategorizedCategoryResponse(txn, configurations,uniqueTrackingId);
				}
				
			}
			return null;
		}
		return uncategorizedCategoryResponse(txn, configurations,uniqueTrackingId);
	}
	private EnrichedTransactionResponse uncategorizedCategoryResponse(TransactionRequest txn,
			Configurations configurations,String uniqueTrackingId) {
		//This is to handle the case for other containers like stocks, insurance and loans
		//Also cases where baseType is unknown/other for Bank/Card Transactions
		//Finally Uncategorised should be populated
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		txnResponse.setCategory(Category.UNCATEGORIZED.toString());
		TransactionCategory category = categoryCache.getTransactionCategory(txnResponse.getCategory(),configurations.isMergerEnabled());
		if (category != null) {
			txnResponse.setCategoryDisplayName(category.getDisplayName());
		}
		txnResponse.setTransactionId(txn.getTransactionId());
		txnResponse.setCategorisationSource(CategorizationSource.NONE.getSource());
		txnResponse.setCategorisationSourceId(CategorizationSource.NONE.getSourceId());
		return txnResponse;
	}
	/**
	 * This function returns true if and only if below two conditions are met for a transaction
	 * a) TDE is enabled
	 * b) txnId, date, desc, amount, basetype are present for a transaction
	 * @return
	 */
	private boolean isMeerkatApplicable(boolean meerkatEnabled,TransactionRequest txnRequest){
		
		boolean result = Boolean.FALSE;
		if(meerkatEnabled && txnRequest != null){
			if(txnRequest.getTransactionId() != null && StringUtils.isNotEmpty(txnRequest.getTransactionId().toString()) && StringUtils.isNotEmpty(txnRequest.getDate())
					&& StringUtils.isNotEmpty(txnRequest.getDescription()) && isBaseTypeApplicableForMeerkat(txnRequest.getBaseType())
					&& txnRequest.getAmount() != null){
				result = Boolean.TRUE;
			}	
		}
		return result;
	}
	/**
	 * Only debit/credit base type are being accepted by Meerkat Webservice.
	 * Internal Server Error will come for other base types
	 * @param baseType
	 * @return
	 */
	private boolean isBaseTypeApplicableForMeerkat(String baseType){
		boolean result = Boolean.FALSE;
		if(StringUtils.isNotEmpty(baseType)){
			if(baseType.equals(BaseType.DEBIT.toString()) || baseType.equals(BaseType.CREDIT.toString())){
				result = Boolean.TRUE;
			}
		}
		return result;
	}
}
