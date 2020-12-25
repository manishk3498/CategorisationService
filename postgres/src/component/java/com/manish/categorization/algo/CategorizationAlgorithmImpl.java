package com.manish.categorization.algo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manish.categorization.BaseType;
import com.manish.categorization.Container;
import com.manish.categorization.algo.legacy.LegacyCategoisationService;
import com.manish.categorization.algo.mcc.MCCCategorizationService;
import com.manish.categorization.algo.tde.MeerkatService;
import com.manish.categorization.db.AccTypeToDerivedAccTypeMappingCache;
import com.manish.categorization.db.GranularCategoryCache;
import com.manish.categorization.db.HlMasterCategoryMappingCache;
import com.manish.categorization.db.TransactionCategory;
import com.manish.categorization.db.TransactionCategoryCache;
import com.manish.categorization.db.TransactionClassificationCache;
import com.manish.categorization.rest.dto.CategorizationRequest;
import com.manish.categorization.rest.dto.CategorizationResponse;
import com.manish.categorization.rest.dto.Configurations;
import com.manish.categorization.rest.dto.EnrichedTransactionResponse;
import com.manish.categorization.rest.dto.TransactionRequest;
import com.manish.categorization.rest.dto.TransactionResponse;
import com.manish.categorization.sdp.config.CaasConfigBean;
import com.manish.categorization.util.CommonHelper;
import com.manish.categorization.util.Constants;
import com.manish.categorization.util.LocaleBuilder;
import com.manish.categorization.util.YCategorizationStats;
import com.yodlee.simpledescservice.rest.dto.SimpleDescriptionRequest;
import com.yodlee.simpledescservice.service.SimpleDescriptionService;
import com.yodlee.simpledescservice.service.SimpleDescriptionServiceImpl;

@Service
public class CategorizationAlgorithmImpl implements CategorizationAlgorithm {

	private static final Logger logger = LogManager.getLogger(CategorizationAlgorithmImpl.class);

	@Autowired
	MeerkatService meerkatService;
	
	SimpleDescriptionService simpleDescriptionService = null;
	
	@Autowired
	LegacyCategoisationService legacyService;

	@Autowired
	TransactionCategoryCache categoryCache;
	
	@Autowired
	GranularCategoryCache granularCategoryCache;
	
	@Autowired
	HlMasterCategoryMappingCache hlMasterCategoryMappingCache;
	
	@Autowired
	TransactionClassificationCache transactionClassificationCache;
	
	@Autowired
	MCCCategorizationService mccCategorizationService;
	
	@Autowired
	CaasConfigBean caasConfigBean;
	
	private List<String> loanTypesList;
	
	private List<String> investmentAccountTypesList;
	
	private List<String> investmentTransactionTypesList;
	
	private List<Long> supportedRegionsForLoanAndInvestmentList;

	@PostConstruct
	public void init(){
		if(StringUtils.isNotEmpty(caasConfigBean.getCaas().getLoanTypes())){
			String[] loanTypesArr = caasConfigBean.getCaas().getLoanTypes().split(Constants.COMMA);
			loanTypesList = Arrays.asList(loanTypesArr);
		}
		if(StringUtils.isNotEmpty(caasConfigBean.getCaas().getInvestmentAccountTypes())){
			String[] investmentAccountTypesArr = caasConfigBean.getCaas().getInvestmentAccountTypes().split(Constants.COMMA);
			investmentAccountTypesList = Arrays.asList(investmentAccountTypesArr);
		}
		if(StringUtils.isNotEmpty(caasConfigBean.getCaas().getInvestmentTransactionTypes())){
			String[] investmentTransactionTypesArr = caasConfigBean.getCaas().getInvestmentTransactionTypes().split(Constants.COMMA);
			investmentTransactionTypesList = Arrays.asList(investmentTransactionTypesArr);
		}
		if(StringUtils.isNotEmpty(caasConfigBean.getCaas().getSupportedRegionsForLoanAndInvestment())){
			String[] supportedRegionsForLoanAndInvestmentArr = caasConfigBean.getCaas().getSupportedRegionsForLoanAndInvestment().split(Constants.COMMA);
			supportedRegionsForLoanAndInvestmentList = new ArrayList<Long>();
			if(supportedRegionsForLoanAndInvestmentArr != null && supportedRegionsForLoanAndInvestmentArr.length > 0){
				for (String regionStr : supportedRegionsForLoanAndInvestmentArr) {
					Long region = Long.parseLong(regionStr);
					supportedRegionsForLoanAndInvestmentList.add(region);
				}
			}
		}
	}
	
	@Override
	public CategorizationResponse categorise(CategorizationRequest request,YCategorizationStats categorizationStats) {
		
			return applyCategorisation(request, categorizationStats);
	}
	/**
	 * This method categorises the transactions in the request and does the following tasks in order.</br>
	 *  1) Sends the request to Meerkat Service which categorizes the transactions parallely or sequentially.  </br>
	 *  2) The categorisation algorithm checks for which all transactions we need to apply Legacy.</br>
	 *  3) The request is made for those transactions and are being sent to Legacy Service. </br>
	 *  4) Response is accumulated post processing and returned. </br>
	 * @param request	CategorizationRequest
	 * @param categorizationStats YCategorizationStats
	 * @return	CategorizationResponse
	 */
	public CategorizationResponse applyCategorisation(CategorizationRequest request, YCategorizationStats categorizationStats) {
		long txnCount = request.getTxns().size();
		long startTime = System.currentTimeMillis();
		Configurations configurations = request.getConfigurations();
		if (request.isSmallBusiness()) {
			if (request.getRegion() != null) {
				if (request.getRegion().longValue() == 1) {
					configurations.setLegacy(false);
				} else {
					configurations.setLegacy(true);
					configurations.setMeerkat(false);
				}
			}
		}
		configurations.setLocale(LocaleBuilder.getLocale(configurations.getLocaleStr()));
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(request.getMemId());
		response.setCobrandId(request.getCobrandId());
		String finalAccountType = setAccountTypeAtTransactionLevel(request);
		List<TransactionRequest> txnRequestList = request.getTxns();
		boolean applyMccRules = Boolean.FALSE;
		if(configurations.getMccRule() != null && configurations.getMccRule().longValue() == 1){
			applyMccRules = Boolean.TRUE;
		}
		boolean meerkat = configurations.isMeerkat() && isMeerkatApplicableForContainer(configurations, request.getContainer(), request.getRegion(), finalAccountType);
		//This flag is used in various places - for example to assign default granular category.
		//Say suppose if account type is not supported for TDE, default granular should not be assigned for that account type 
		//If account type is supported by TDE, then default granular category should be assigned for the transactions of that account type
		configurations.setMeerkat(meerkat);
		//Apply MCC Categorisation
		List<TransactionResponse> mccTxnResList = new ArrayList<TransactionResponse>();
		Map<BigInteger, EnrichedTransactionResponse> mccTxnIdToResponseMap = new HashMap<BigInteger,EnrichedTransactionResponse>();
		if(applyMccRules){
			Iterator<TransactionRequest> txnRequestListItr = txnRequestList.iterator();
			while(txnRequestListItr.hasNext()){
				TransactionRequest transactionRequest = txnRequestListItr.next();
				if(transactionRequest.getMccCode() != null){	
					TransactionResponse mccResponse = mccCategorizationService.categorise(transactionRequest, request);
					if(mccResponse != null && mccResponse instanceof EnrichedTransactionResponse){
						EnrichedTransactionResponse enrichedMccTxnResponse = (EnrichedTransactionResponse) mccResponse;
						mccTxnResList.add(enrichedMccTxnResponse);
						mccTxnIdToResponseMap.put(mccResponse.getTransactionId(), enrichedMccTxnResponse);
					}else {
							logger.error("Transaction response is null for mcc categorization for mcc code : "+transactionRequest.getMccCode()+" Unique Txn Id::"+ request.getUniqueTrackingId());
					}
				}
			}
		}
		List<TransactionRequest> applyLegacyTxnRequestList = new ArrayList<TransactionRequest>();
		//List stores request for Applying Legacy Algorithm for getting Legacy Merchant because TDE Merchant is not available
		List<TransactionRequest> applyLegacyForLegacyMerchant = new ArrayList<TransactionRequest>();
		List<TransactionResponse> meerkatResponseList = new ArrayList<TransactionResponse>();
		List<TransactionResponse> legacyResponseList = new ArrayList<TransactionResponse>();
		Map<BigInteger, TransactionResponse> meerkatResponsesMap = new HashMap<BigInteger, TransactionResponse>();
		Map<BigInteger, TransactionResponse> legacyResponseMap = new HashMap<BigInteger, TransactionResponse>();
		request.setContainer(request.getContainer().toLowerCase());
		
		//Meerkat required transactionId,Date,Desc,Amount,BaseType as the mandatory fields for processing transactions
		//If a txn does not contain any of these fields only Legacy Categorisation Algorithm can be applied
		
		Iterator<TransactionRequest> txnRequestListItr = txnRequestList.iterator();
		while(txnRequestListItr.hasNext()){
			TransactionRequest txnRequest = txnRequestListItr.next();
			if(txnRequest.getTransactionId() != null && StringUtils.isNotEmpty(txnRequest.getTransactionId().toString()) && StringUtils.isNotEmpty(txnRequest.getDate())
					&& StringUtils.isNotEmpty(txnRequest.getDescription()) && isBaseTypeApplicableForMeerkatTxnrequest(txnRequest.getBaseType())
					&& txnRequest.getAmount() != null){
				continue;
			}else if(mccTxnIdToResponseMap.get(txnRequest.getTransactionId()) == null){//Check if categorised using MCC, then dont add in applyLegacyTxnRequestList
				applyLegacyTxnRequestList.add(txnRequest);
				txnRequestListItr.remove();
			}
		}
		
		//Only for configured transaction types TDE should be applied....
		if(Container.INVESTMENT.toString().equals(request.getContainer())){
			txnRequestListItr = txnRequestList.iterator();
			while(txnRequestListItr.hasNext()){
				TransactionRequest txnRequest = txnRequestListItr.next();
				if(investmentTransactionTypesList != null && StringUtils.isEmpty(txnRequest.getTransactionType())){
						applyLegacyTxnRequestList.add(txnRequest);
						//transaction should not be considered for tde at all whether mcc is applied or not
						txnRequestListItr.remove();
				}
				if(investmentTransactionTypesList != null && StringUtils.isNotEmpty(txnRequest.getTransactionType())){
					if(!investmentTransactionTypesList.contains(txnRequest.getTransactionType())){
						if(mccTxnIdToResponseMap.get(txnRequest.getTransactionId()) == null){//Check if categorised using MCC, then dont add in applyLegacyTxnRequestList
							applyLegacyTxnRequestList.add(txnRequest);
						}
						//transaction should not be considered for tde at all whether mcc is applied or not
						txnRequestListItr.remove();
					}
				}
			}
		}
		if (meerkat && txnRequestList.size() > 0) {
			meerkatResponseList = meerkatService.categorise(request,response, mccTxnIdToResponseMap,categorizationStats);
		}
		if(meerkatResponseList != null && meerkatResponseList.size() > 0){
			for (TransactionResponse txnResponse : meerkatResponseList) {
				meerkatResponsesMap.put(txnResponse.getTransactionId(), txnResponse);
			}
		}
		//If meerkat is disabled then we need to add the mcc response in the final response which is being set in categorisation response
		if(!meerkat && applyMccRules && mccTxnResList != null && mccTxnResList.size() > 0){
			response.addTxns(mccTxnResList);
		}
		if(meerkat){
			for (TransactionResponse mccTxnResponse : mccTxnResList) {
				TransactionResponse meerkatResponse = meerkatResponsesMap.get(mccTxnResponse.getTransactionId()); 
				if(meerkatResponse != null){
					response.addTxn(meerkatResponse);
					meerkatResponsesMap.remove(mccTxnResponse.getTransactionId());
				}else{
					response.addTxn(mccTxnResponse);
				}
			}
		}
		//Add the transactions which are categorised using Meerkat into the response and collect other txns for which legacy categorisation needs to be applied.
		for (TransactionRequest txn : txnRequestList) {
			TransactionResponse categorizedTxn = null;
			TransactionResponse meerkatResponse = meerkatResponsesMap.get(txn.getTransactionId());
			if (configurations.isLegacy()) {
				if (meerkatResponse != null && considerMeerkatCategory(request, txn, meerkatResponse)) {
					categorizedTxn = meerkatResponse;
					EnrichedTransactionResponse enrichedMeerkatResponse = (EnrichedTransactionResponse) meerkatResponse;
					//Need to check whether we can use legacy merchant and the merchant is not a dummy merchant
					if(enrichedMeerkatResponse.isUseLegacyMerchant() && StringUtils.isEmpty(meerkatResponse.getMerchant())){
						applyLegacyForLegacyMerchant.add(txn);
						continue;
					}
				} else if(mccTxnIdToResponseMap.get(txn.getTransactionId()) == null){
					applyLegacyTxnRequestList.add(txn);
					continue;
				}
			} else {
				categorizedTxn = meerkatResponse;
			}
			if(categorizedTxn != null)
				response.addTxn(categorizedTxn);
		}
		List<TransactionRequest> mergedInputList = new ArrayList<TransactionRequest>();
		if(configurations.isLegacy()){
			if(applyLegacyTxnRequestList != null && applyLegacyTxnRequestList.size() > 0){
				mergedInputList.addAll(applyLegacyTxnRequestList);
			}
			if(applyLegacyForLegacyMerchant != null && applyLegacyForLegacyMerchant.size() > 0){
				mergedInputList.addAll(applyLegacyForLegacyMerchant);
			}
			request.setTxns(mergedInputList);
			legacyResponseList = legacyService.categorise(request); 
		}
		if(legacyResponseList != null && legacyResponseList.size() > 0){
			for (TransactionResponse txnResponse : legacyResponseList) {
				legacyResponseMap.put(txnResponse.getTransactionId(), txnResponse);
			}
		}
		for (TransactionRequest legacyRequest : mergedInputList) {
			TransactionResponse categorizedTxn = null;
			TransactionResponse legacyResponse = legacyResponseMap.get(legacyRequest.getTransactionId());
			TransactionResponse meerkatResponse = meerkatResponsesMap.get(legacyRequest.getTransactionId());
			if (legacyResponse == null && meerkat) {
				categorizedTxn = meerkatResponse;
			} else {
				SimpleDescriptionRequest simpleDescriptionRequest= null;
				String simpleDescVersion=null;
				if(StringUtils.isNotEmpty(configurations.getSimpleDescVersion())){
					simpleDescVersion = configurations.getSimpleDescVersion();
				}else{
					simpleDescVersion = caasConfigBean.getCaas().getSimpleDescriptionVersion();
				}
				if(applyLegacyForLegacyMerchant.contains(legacyRequest)){
					categorizedTxn = meerkatResponse;
					String merchant = meerkatResponse.getMerchant();
					
					
					if(StringUtils.isEmpty(merchant)){
						simpleDescriptionService = new SimpleDescriptionServiceImpl();
						categorizedTxn.setMerchant(legacyResponse.getMerchant());
						categorizedTxn.setMerchantId(legacyResponse.getMerchantId());
						EnrichedTransactionResponse enrichedCategorizedTxn = (EnrichedTransactionResponse) categorizedTxn;
						simpleDescriptionRequest = new SimpleDescriptionRequest();
						
						BeanUtils.copyProperties(categorizedTxn, simpleDescriptionRequest);
						simpleDescriptionRequest.setGeoLocationEnabledInSD(configurations.isGeoLocationEnabledInSD());
						simpleDescriptionRequest.setPlainTextDesc(legacyRequest.getDescription());
						simpleDescriptionRequest.setVersion(simpleDescVersion);
						simpleDescriptionRequest.setTxnBaseType(legacyRequest.getBaseType());
						simpleDescriptionRequest.setLocale(configurations.getLocale());
						simpleDescriptionRequest.setContainer(request.getContainer());
						if(configurations.isSimpleDescEnabled())
							enrichedCategorizedTxn.setSimpleDescription(simpleDescriptionService.deriveSimpleDescription(simpleDescriptionRequest));
						
					}
				}else{
					categorizedTxn = legacyResponse;
					if (meerkatResponse != null) {
						EnrichedTransactionResponse enrichedLegacyRes = (EnrichedTransactionResponse) legacyResponse;
						EnrichedTransactionResponse enrichedMeerkatRes = (EnrichedTransactionResponse) meerkatResponse;
						enrichedLegacyRes.setMeerkatType(enrichedMeerkatRes.getMeerkatType());
						enrichedLegacyRes.setMeerkatSubType(enrichedMeerkatRes.getMeerkatSubType());
						enrichedLegacyRes.setLabels(enrichedMeerkatRes.getLabels());
						enrichedLegacyRes.setMeerkatMerchant(enrichedMeerkatRes.getMeerkatMerchant());
						enrichedLegacyRes.setCnn(enrichedMeerkatRes.getCnn());
						enrichedLegacyRes.setFactualCategory(enrichedMeerkatRes.getFactualCategory());
						enrichedLegacyRes.setSmbTwoTxnCategoryId(enrichedMeerkatRes.getSmbTwoTxnCategoryId());							
						enrichedLegacyRes.setIsBusinessRelated(enrichedMeerkatRes.getIsBusinessRelated());

						if (caasConfigBean.getCaas().isPopulateIsPhysical()){
							enrichedLegacyRes.setIsPhysical(enrichedMeerkatRes.getIsPhysical());
						}
						if(caasConfigBean.getCaas().isPopulateIntermediary()){
							enrichedLegacyRes.setIntermediary(enrichedMeerkatRes.getIntermediary());
						}
						if(caasConfigBean.getCaas().isPopulateTxnMerchantType()){
							enrichedLegacyRes.setTxnMerchantTypeId(enrichedMeerkatRes.getTxnMerchantTypeId());
							enrichedLegacyRes.setTxnMerchantType(enrichedMeerkatRes.getTxnMerchantType());
						}					
						if(caasConfigBean.getCaas().isPopulateLogoEndpoint()){
							enrichedLegacyRes.setLogoEndpoint(enrichedMeerkatRes.getLogoEndpoint());
						}                      
						if (caasConfigBean.getCaas().isPopulateAdditionalFields()) {
							enrichedLegacyRes.setIsRecurring(enrichedMeerkatRes.getIsRecurring());
							enrichedLegacyRes.setPeriodicity(enrichedMeerkatRes.getPeriodicity());
							enrichedLegacyRes.setModeOfPayment(enrichedMeerkatRes.getModeOfPayment());
							enrichedLegacyRes.setTdePiiInfo(enrichedMeerkatRes.getTdePiiInfo());
						}
						boolean isSimpleDescEnabled = configurations.isSimpleDescEnabled();
						if(isSimpleDescEnabled && simpleDescVersion.equals("1.1")){
							simpleDescriptionService = new SimpleDescriptionServiceImpl();
							simpleDescriptionRequest = new SimpleDescriptionRequest();
							BeanUtils.copyProperties(categorizedTxn, simpleDescriptionRequest);
							simpleDescriptionRequest.setGeoLocationEnabledInSD(configurations.isGeoLocationEnabledInSD());
							simpleDescriptionRequest.setPlainTextDesc(legacyRequest.getDescription());
							simpleDescriptionRequest.setVersion(simpleDescVersion);
							simpleDescriptionRequest.setTxnBaseType(legacyRequest.getBaseType());
							simpleDescriptionRequest.setLocale(configurations.getLocale());
							simpleDescriptionRequest.setContainer(request.getContainer());
							enrichedLegacyRes.setSimpleDescription(simpleDescriptionService.deriveSimpleDescription(simpleDescriptionRequest));
						}
						
					}
				}
				
			}
			if (categorizedTxn == null) {
				if(logger.isInfoEnabled())
					logger.info(String.format("Finally uncategorized transaction:", legacyRequest.getTransactionId()));
				categorizedTxn = new EnrichedTransactionResponse();
				categorizedTxn.setTransactionId(legacyRequest.getTransactionId());
			} else {
				String name = categorizedTxn.getCategory();
				TransactionCategory category = categoryCache.getTransactionCategory(name, configurations.isMergerEnabled());
				if(category != null){
					if(configurations.isMergerEnabled()){
						categorizedTxn.setCategory(category.getMergedTxnCatName());
					}else{
						categorizedTxn.setCategory(category.getName());
					}
				}
			}
			response.addTxn(categorizedTxn);
		}
		
		List<TransactionResponse> transactionResponseList = response.getTxns();
		CommonHelper.populateIds(categoryCache, hlMasterCategoryMappingCache, granularCategoryCache, transactionClassificationCache,transactionResponseList,request.getUniqueTrackingId());
		
		long endTime = System.currentTimeMillis();
		categorizationStats.setTotalTimeTaken(endTime-startTime);
		if(logger.isInfoEnabled()){
			logger.info("Finished categorisation algorithm...TDE enabled Flag: "
					+ request.getConfigurations().isMeerkat()
					+ " ,Cobrand Id: "
					+ request.getCobrandId() + " ,Mem Id: "+ request.getMemId()+" ,Region Id: "+request.getRegion()
					+" Unique Txn Id:: " + request.getUniqueTrackingId()
					+ " ,requestSentTime : "
					+ startTime
					+ " ,responseReceivedTime :"
					+ endTime
					+ " ,totalTime in ms:"
					+ (endTime - startTime) + " for txnCount: " + txnCount);
		}
			
		return response;
	}
	
	private boolean considerMeerkatCategory(CategorizationRequest request, TransactionRequest txn,
			TransactionResponse response) {
		Configurations configurations = request.getConfigurations();
		String categoryName = response.getCategory();
		TransactionCategory category = categoryCache.getTransactionCategory(categoryName,
				configurations.isMergerEnabled(),configurations.isMeerkat());
		boolean consider = false;
		if (category == null) {
			if(logger.isErrorEnabled())
				logger.error(String.format("Skiping txnId:%s as the category:%s is null",
					response.getTransactionId(), response.getCategory()));
		} else {
			consider = category.isSpecificCategory();
		}
		return consider;
	}
	private boolean isBaseTypeApplicableForMeerkatTxnrequest(String basetype){
		boolean result = Boolean.FALSE;
		if(StringUtils.isNotEmpty(basetype)){
			if(basetype.equals(BaseType.DEBIT.toString()) || basetype.equals(BaseType.CREDIT.toString())){
				result = Boolean.TRUE;
			}
		}
		return result;
	}
	/**
	 * This function is used to check whether the TDE is applicable for Loan and Investment container
	 * 1) Only US region accounts are supported for Loan and Investment container. These are configured in application.properties
	 * 2) TDE is supported only for certain account types - configured in application.properties
	 * 3) TDE will be applied only for those account types which are configured for the cobrand - ACL - TDE_SUPPORTED_ACCOUNT_TYPES
	 * @param container
	 * @param regionId
	 * @return
	 */
	private boolean isMeerkatApplicableForContainer(Configurations configurations,String container,Long regionId,String accountType){
		
		boolean meerkatEnabled = Boolean.FALSE;
		if(StringUtils.isNotEmpty(container)){
			if(Container.BANK.toString().equals(container) || Container.CARD.toString().equals(container)){
				meerkatEnabled = Boolean.TRUE;
			}
			//Check for investment and loan container
			if(StringUtils.isNotEmpty(accountType)){
				if(Container.INVESTMENT.toString().equals(container) || Container.LOAN.toString().equals(container)){
					
					if(regionId != null && supportedRegionsForLoanAndInvestmentList.contains(regionId)){
						String cobrandSupportedAccountTypes = configurations.getConfiguredAccountTypes();
						String[] cobrandSupportedAccountTypesArr = null;
						if(StringUtils.isNotEmpty(cobrandSupportedAccountTypes)){
							cobrandSupportedAccountTypesArr = cobrandSupportedAccountTypes.split(Constants.COMMA);
						}
						List<String> cobrandSupportedAccountTypesList = new ArrayList<>();
						if(cobrandSupportedAccountTypesArr != null && cobrandSupportedAccountTypesArr.length > 0){
							cobrandSupportedAccountTypesList = Arrays.asList(cobrandSupportedAccountTypesArr);
						}
						
						if(Container.INVESTMENT.toString().equals(container)){
							if(investmentAccountTypesList != null && investmentAccountTypesList.size() > 0){
									if(investmentAccountTypesList.contains(accountType)){
										if(cobrandSupportedAccountTypesList.contains(accountType)){
											meerkatEnabled = Boolean.TRUE;
										}
									}
							}
						}
						if(Container.LOAN.toString().equals(container)){
							if(loanTypesList != null && loanTypesList.size() > 0){
									if(loanTypesList.contains(accountType)){
										if(cobrandSupportedAccountTypesList.contains(accountType)){
											meerkatEnabled = Boolean.TRUE;
										}
									}
							}
						}
					}
				}
			}
		}
		return meerkatEnabled;
	}
	
	/**
	 * This method is used to set account type to send to meerkat
	 * 1) If derived account type is enabled and request is for investment container
	 * then derived account type will be sent if it is available
	 * 2) If derived account type is not available account type will be mapped to derived account type
	 * 3) If mapping is not successsful then account type will be sent to TDE
	 * @param categorizationrequest
	 */
	private String setAccountTypeAtTransactionLevel(CategorizationRequest categorizationrequest){
		
		String accountType = categorizationrequest.getAccountType();
		Configurations configurations = categorizationrequest.getConfigurations();
		String derivedAccountType = configurations != null ? configurations.getDerivedAccountType() : null;
		boolean derivedAccountTypeEnabled = configurations != null ? configurations.isDerivedAccountTypeEnabled() : Boolean.FALSE;
		List<TransactionRequest> transactionRequestList = categorizationrequest.getTxns();
		String finalAccountType = null;
		if(derivedAccountTypeEnabled && Container.INVESTMENT.toString().equalsIgnoreCase(categorizationrequest.getContainer())){
			Map<String,String> acctTypeToDerivedAcctypeMap = AccTypeToDerivedAccTypeMappingCache.getAcctTypeToDerivedAccTypeMap();
			if(StringUtils.isNotEmpty(derivedAccountType)){
				finalAccountType = derivedAccountType;
			}else if(StringUtils.isNotEmpty(accountType)){
				String mappedDerivedAccountType = null;
				if(acctTypeToDerivedAcctypeMap != null){
					mappedDerivedAccountType = acctTypeToDerivedAcctypeMap.get(accountType);	
				}
				if(StringUtils.isNotEmpty(mappedDerivedAccountType)){
					finalAccountType = mappedDerivedAccountType;
				}else{
					finalAccountType = accountType;
				}
			}
		}else{
			if(StringUtils.isNotEmpty(accountType)){
				finalAccountType = accountType;
			}
		}
		if(transactionRequestList != null && transactionRequestList.size() > 0 && StringUtils.isNotEmpty(finalAccountType)){
			for (TransactionRequest transactionRequest : transactionRequestList) {
				transactionRequest.setAccountType(finalAccountType);
			}
		}
		return finalAccountType;
	}
	
	
}
