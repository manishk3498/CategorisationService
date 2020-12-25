package com.manish.categorization.algo.tde;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.manish.categorization.BaseType;
import com.manish.categorization.CategorizationSource;
import com.manish.categorization.Category;
import com.manish.categorization.Container;
import com.manish.categorization.URLConfig;
import com.manish.categorization.algo.tde.dto.MeerkatRequest;
import com.manish.categorization.algo.tde.dto.MeerkatResponse;
import com.manish.categorization.algo.tde.dto.MeerkatResponse.CNNInfo;
import com.manish.categorization.algo.tde.dto.MeerkatResponse.MeerkatTransaction;
import com.manish.categorization.db.GranularCategoryCache;
import com.manish.categorization.db.GranularCategoryMapping;
import com.manish.categorization.db.RegionCache;
import com.manish.categorization.db.SMBTwoTxnCategoryCache;
import com.manish.categorization.db.SiteInfoCache;
import com.manish.categorization.db.TransactionCategory;
import com.manish.categorization.db.TransactionCategoryCache;
import com.manish.categorization.db.TransactionClassificationCache;
import com.manish.categorization.db.TxnMerchantTypeCache;
import com.manish.categorization.http.FluentExecutor;
import com.manish.categorization.rest.dto.CategorizationRequest;
import com.manish.categorization.rest.dto.CategorizationResponse;
import com.manish.categorization.rest.dto.Configurations;
import com.manish.categorization.rest.dto.EnrichedTransactionResponse;
import com.manish.categorization.rest.dto.TransactionRequest;
import com.manish.categorization.rest.dto.TransactionResponse;
import com.manish.categorization.sdp.config.CaasConfigBean;
import com.manish.categorization.util.CommonHelper;
import com.manish.categorization.util.Constants;
import com.manish.categorization.util.YCategorizationStats;
import com.yodlee.simpledescservice.rest.dto.SimpleDescriptionRequest;
import com.yodlee.simpledescservice.service.SimpleDescriptionService;
import com.yodlee.simpledescservice.service.SimpleDescriptionServiceImpl;

@Service
public class MeerkatServiceImpl implements MeerkatService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private FluentExecutor fluentExecutor;
	
	@Autowired
	CaasConfigBean caasConfigBean;
	
	@Autowired
	private HttpHost httpHost;
	
	@Autowired
	TransactionCategoryCache categoryCache;
	
	@Autowired
	GranularCategoryCache granularCategoryCache;
	
	@Autowired
	TransactionClassificationCache transactionClassificationCache;
	
	@Autowired 
	SiteInfoCache siteInfoCache;
	
	@Autowired
	ExecutorCompletionService<List<TransactionResponse>> meerkatExecutorCompletionService;
	
	SimpleDescriptionService simpleDescriptionService = null;

	@Autowired
	RegionCache regionCache;
	
	@Autowired
	TxnMerchantTypeCache txnMerchantTypeCache;
	
	@Autowired
	SMBTwoTxnCategoryCache smbTwoTxnCategoryCache;
	
	@Autowired
	URLConfig urlConfig;
	
	
	private static final Logger logger = LogManager.getLogger(MeerkatServiceImpl.class);
	
    private static final String ccPattern = "(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|"
    		+ "(?:2131|1800|35\\d{3})\\d{11}|(?:4\\d{3}|5[1-5]\\d{2}|6011|7\\d{3})-?\\d{4}-?\\d{4}-?\\d{4}|3[4,7]\\d{13})";
    
	@Override
	public List<TransactionResponse> categorise(CategorizationRequest request,
			CategorizationResponse categorizationResponse,
			Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap,YCategorizationStats categorizationStats) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate");
		if(caasConfigBean.getCaas().isParallelEnabled()){
			return applyParallelCategorization(request, headers, categorizationResponse);
		}else{
			return applyCategorization(request, headers, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);
		}
		
	}
	/**
	 * This method does the parallel categorization of the transactions by spawning thread for each Batch of transactions.
	 * Batch size is being configured in the application.properties.
	 * @param request
	 * @param headers
	 * @param categorizationResponse TODO
	 * @return
	 */
	private List<TransactionResponse> applyParallelCategorization(CategorizationRequest request, HttpHeaders headers, CategorizationResponse categorizationResponse) {
		
		List<TransactionRequest> txnReqList = new ArrayList<TransactionRequest>(request.getTxns());
		List<Future<List<TransactionResponse>>> futureList = new ArrayList<Future<List<TransactionResponse>>>();
		List<List<TransactionRequest>> subLists = Lists.partition(txnReqList, Integer.parseInt(caasConfigBean.getCaas().getBatchSize()));
		CountDownLatch countDownLatch = new CountDownLatch(subLists.size());
		request.setTxns(null);
		for (List<TransactionRequest> subList : subLists) {
			try {
				CategorizationRequest clonedRequest = (CategorizationRequest) request.clone();
				clonedRequest.setTxns(subList);
				Future<List<TransactionResponse>> future = submitCategorizeThread(clonedRequest, headers, countDownLatch);
				if(future != null)
					futureList.add(future);
			} catch (CloneNotSupportedException exception) {
				exception.printStackTrace();
			}
		}
		long startWaitingTime = System.currentTimeMillis();
		try {
			countDownLatch.await(caasConfigBean.getCaas().getMeerkatThreadTimeout(), TimeUnit.MILLISECONDS);
		} catch (InterruptedException exception) {
			exception.printStackTrace();
		}
		long endWaitingTime = System.currentTimeMillis();
		if(logger.isInfoEnabled())
			logger.info("Meerkat Categorisation CountdownLatch waited for "+(endWaitingTime-startWaitingTime)+" milliseconds");
		List<TransactionResponse> transactionResponseList = new ArrayList<TransactionResponse>();
		for (Future<List<TransactionResponse>> future : futureList) {		
				try {
						List<TransactionResponse> txnResList = future.get(caasConfigBean.getCaas().getExtraThreadDelay(), TimeUnit.MILLISECONDS); 
						if( txnResList != null)
							transactionResponseList.addAll(txnResList);
						else{
							if(logger.isErrorEnabled())
								logger.error("Transaction Result List by Meerkat Thread is null for memId "+request.getMemId());
						}
				}catch (TimeoutException exception) {
					future.cancel(Boolean.TRUE);
					exception.printStackTrace();
					if(logger.isErrorEnabled())
						logger.error("Timeout occured for Meerkat Thread and it is cancelled for memId "+request.getMemId(),exception);
				} 
				catch (InterruptedException exception) {
					if(logger.isErrorEnabled())
						logger.error("InterruptedException occured for Meerkat Thread and it is cancelled for memId "+request.getMemId(),exception);
				} catch (ExecutionException exception) {
					if(logger.isErrorEnabled())
						logger.error("ExecutionException occured for Meerkat Thread and it is cancelled for memId "+request.getMemId(),exception);
				}
		}
		return transactionResponseList;
	}
	/**
	 * This method does the normal categorization of the transactions by sending the transactions to the meerkat webservice in batches.
	 * @param request
	 * @param headers
	 * @param categorizationResponse
	 * @param mccTxnCatIdToResponseMap TODO
	 * @param categorizationStats TODO
	 * @return
	 */
	private List<TransactionResponse> applyCategorization(CategorizationRequest request,
			HttpHeaders headers, CategorizationResponse categorizationResponse,
			Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap,
			YCategorizationStats categorizationStats) {
		long startTime = System.currentTimeMillis();
		if(request.getUniqueTrackingId() == null){
			request.setUniqueTrackingId("");
		}
		int txnCount = request.getTxns().size();
		Configurations configurations = request.getConfigurations();
		List<TransactionResponse> txns = new ArrayList<TransactionResponse>();
		HashMap<String,String> txnIdToPlainTextDescMap = new HashMap<>();
		HashMap<String,String> txnIdToBaseTypeMap = new HashMap<>();
		try {
			List<TransactionRequest> txnRequestList = request.getTxns();
			if(txnRequestList != null && txnRequestList.size() > 0){
				for (TransactionRequest txnRequest : txnRequestList) {
					txnIdToPlainTextDescMap.put(txnRequest.getTransactionId().toString(), txnRequest.getDescription());
					txnIdToBaseTypeMap.put(txnRequest.getTransactionId().toString(), txnRequest.getBaseType());
				}
			}
			List<MeerkatResponse> responseList = processMeerkatRequest(request, headers, categorizationResponse);
			int emptyGranularCategoryCount=0;
			int emptyGranularCategoryMappingCount=0;
			request.setTdeV2(CommonHelper.isTde2(responseList));
			if(responseList != null && responseList.size() > 0){
				for (MeerkatResponse response : responseList) {
					List<MeerkatTransaction> meerkatTxns = response.getData().getTxns();
					for (MeerkatTransaction txn : meerkatTxns) {
						if(mccTxnCatIdToResponseMap != null){
							EnrichedTransactionResponse mccResponse = mccTxnCatIdToResponseMap.get(txn.getTransactionId());
							if(mccResponse != null){
								//enrich with TDE Fields
								enrichMCCResponseWithTDEFields(txn, mccResponse, configurations, txnIdToPlainTextDescMap, txnIdToBaseTypeMap, request);
								txns.add(mccResponse);
								continue;
							}
						}
						TransactionCategory category = categoryCache.getTransactionCategory(txn.getLabels(),
								configurations.isMergerEnabled());
						
						GranularCategoryMapping granularCategoryMapping = null;
						if(!configurations.isMergerEnabled()){
							if(txn.getGranularCategory() != null && txn.getGranularCategory().length() > 0){
								granularCategoryMapping = granularCategoryCache.getGranularCategory(txn.getGranularCategory(), 
									configurations.isMergerEnabled(), txnIdToBaseTypeMap.get(txn.getTransactionId().toString()));
							}else{
								emptyGranularCategoryCount++;
							}
						}
						
						boolean isSimpleDescEnabled=configurations.isSimpleDescEnabled();
						String simpleDescVersion=null;
						if(StringUtils.isNotEmpty(configurations.getSimpleDescVersion())){
							simpleDescVersion = configurations.getSimpleDescVersion();
						}else{
							simpleDescVersion = caasConfigBean.getCaas().getSimpleDescriptionVersion();
						}
						if(StringUtils.isNotEmpty(request.getContainer()) && (Container.LOAN.toString().equalsIgnoreCase(request.getContainer()) || Container.INVESTMENT.toString().equalsIgnoreCase(request.getContainer()))) {
							simpleDescVersion = Constants.SIMPLE_DESC_VERSION;
						}
						String sourceMerchantId = null, simpleDescription = null;
						sourceMerchantId = deriveSourceMerchantId(txn);
						
						/*
						 * Logging debug score if lower than threshold limit.
						 */
						if(configurations.isDebug())
							logDebugScore(request, txn);
						
						if(category != null){
							EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
							if(configurations.isMergerEnabled()){
								txnResponse.setCategory(category.getMergedTxnCatName());
							}else{
								if(granularCategoryMapping != null){
									txnResponse.setCategory(granularCategoryMapping.getMasterCategoryName());
								}
								else{
									emptyGranularCategoryMappingCount++;
									if(BaseType.CREDIT.toString().equals(txnIdToBaseTypeMap.get(txn.getTransactionId().toString()))){
										txnResponse.setCategory(Category.OTHER_INCOME.toString());
									}else if(BaseType.DEBIT.toString().equals(txnIdToBaseTypeMap.get(txn.getTransactionId().toString()))){
										txnResponse.setCategory(Category.OTHER_EXPENSES.toString());
									}
								}
							}
							txnResponse.setCategoryDisplayName(category.getDisplayName());
							txnResponse.setKeyword(null);
							txnResponse.setMerchant(txn.getMerchant());
							txnResponse.setTransactionId(txn.getTransactionId());
							txnResponse.setMeerkatMerchant(txn.getMerchant());
							txnResponse.setMeerkatType(txn.getType());
							txnResponse.setMeerkatSubType(txn.getSubType());
							txnResponse.setStreet(txn.getStreet());
							txnResponse.setCity(txn.getCity());
							txnResponse.setState(txn.getState());
							txnResponse.setZip(txn.getPostalCode());
							txnResponse.setCountry(txn.getCountry());
							txnResponse.setLongitude(txn.getLongitude());
							txnResponse.setLatitude(txn.getLatitude());
							txnResponse.setFaxNumber(txn.getFax());
							txnResponse.setChainName(txn.getChainName());
							txnResponse.setNeighbourhood(txn.getNeighbourhood());
							txnResponse.setPhoneNumber(txn.getPhone());
							txnResponse.setMerchantSource(txn.getSource());
							txnResponse.setSourceMerchantId(sourceMerchantId);
							txnResponse.setStoreId(txn.getStoreId());
							txnResponse.setWebsite(txn.getWebsite());
							txnResponse.setPostalCode(txn.getPostalCode());
							txnResponse.setEmail(txn.getEmail());
							txnResponse.setConfidenceScore(txn.getConfidenceScore());
							txnResponse.setFactualCategory(txn.getFactualCategory());
							txnResponse.setSmbTwoTxnCategoryId(getSMBTwoTxnCategoryId(txn.getSmbCategory()));
							if (txnResponse.getSmbTwoTxnCategoryId() != null
									&& txnResponse.getSmbTwoTxnCategoryId() != 0L) {
								if (StringUtils.isNotEmpty(txn.getIsBusinessRelated()) && txn.getIsBusinessRelated().equalsIgnoreCase("Yes")) {
									txnResponse.setIsBusinessRelated(1L);
								}
								else if (StringUtils.isNotEmpty(txn.getIsBusinessRelated()) && txn.getIsBusinessRelated().equalsIgnoreCase("No")) {
									txnResponse.setIsBusinessRelated(0L);
								}
							}
							CNNInfo debugInfo = txn.getDebugInfo();
							if (debugInfo != null) {
								txnResponse.setCnn(debugInfo);
							}
							txnResponse.setUseLegacyMerchant(txn.getUse_legacy_merchant());
							txnResponse.setLabels(CommonHelper.getCategoryLabelsAsString(txn.getLabels()));
							txnResponse.setCategorisationSource(CategorizationSource.MEERKAT.getSource());
							txnResponse.setCategorisationSourceId(CategorizationSource.MEERKAT.getSourceId());
							if (caasConfigBean.getCaas().isPopulateIsPhysical()){
								if(txn.isPhysicalMerchant())
									txnResponse.setIsPhysical(1L);
								else
									txnResponse.setIsPhysical(0L);
							}
							if (caasConfigBean.getCaas().isPopulateAdditionalFields()) {
								if (txn.isRecurring()) {
									txnResponse.setIsRecurring(1L);
								} else {
									txnResponse.setIsRecurring(0L);
								}
								txnResponse.setPeriodicity(txn.getPeriodicity());
								txnResponse.setModeOfPayment(txn.getModeOfPayment());
								txnResponse.setTdePiiInfo(txn.getTdePiiInfo());
							}
							if(caasConfigBean.getCaas().isPopulateIntermediary()){
								txnResponse.setIntermediary(txn.getIntermediary());
							}
							if (StringUtils.isNotEmpty(request.getContainer()) && (Container.LOAN.toString()
									.equalsIgnoreCase(request.getContainer())
									|| Container.INVESTMENT.toString().equalsIgnoreCase(request.getContainer()))) {
								if (caasConfigBean.getCaas().isPopulateDigitalFieldForLoanAndInvestment()) {
									if (caasConfigBean.getCaas().isPopulateTxnMerchantType()) {
										txnResponse.setTxnMerchantTypeId(
												getTransactionMerchantTypeId(txn.getTxnMerchantType()));
										txnResponse.setTxnMerchantType(
												getTransactionMerchantTypeId(txn.getTxnMerchantType()));
										if (StringUtils.isNotEmpty(txn.getTxnMerchantType())) {
											txnResponse.setMerchantType(txn.getTxnMerchantType().toUpperCase());
										}
									}
									txnResponse.setGranularCategory(txn.getGranularCategory());
								}
							} else if (StringUtils.isNotEmpty(request.getContainer())
									&& (Container.BANK.toString().equalsIgnoreCase(request.getContainer())
											|| Container.CARD.toString().equalsIgnoreCase(request.getContainer()))) {
								if (caasConfigBean.getCaas().isPopulateTxnMerchantType()) {
									txnResponse.setTxnMerchantTypeId(
											getTransactionMerchantTypeId(txn.getTxnMerchantType()));
									txnResponse
											.setTxnMerchantType(getTransactionMerchantTypeId(txn.getTxnMerchantType()));
									if (StringUtils.isNotEmpty(txn.getTxnMerchantType())) {
										txnResponse.setMerchantType(txn.getTxnMerchantType().toUpperCase());
									}
								}
								txnResponse.setGranularCategory(txn.getGranularCategory());
							}
							if(caasConfigBean.getCaas().isPopulateLogoEndpoint()){
								txnResponse.setLogoEndpoint(txn.getLogoEndpoint());
							}
							if (txn.getVendorNameVariation() != null && txn.getVendorNameVariation().length > 2) {
								if (StringUtils.isNotEmpty(txn.getVendorNameVariation()[2])
										&& Integer.parseInt(txn.getVendorNameVariation()[2]) > 0
										&& caasConfigBean.getCaas().getConfidenceScoreThreshold() != 0
										&& Integer.parseInt(txn.getVendorNameVariation()[2]) < caasConfigBean.getCaas()
												.getConfidenceScoreThreshold())
									if (StringUtils.isNotEmpty(txn.getVendorNameVariation()[1])
											&& Character.toString(txn.getVendorNameVariation()[1].charAt(0))
													.equals(Constants.VENDOR_NAME_VARIATION_TYPE)) {
										if (StringUtils.isNotEmpty(txn.getVendorNameVariation()[0])) {
											txnResponse.setVendorName(txn.getVendorNameVariation()[0]);
											configurations.setGeoLocationEnabledInSD(false);
											CommonHelper.nullifyMeerkatMerchantFields(txnResponse);
										}
									}
							}
							SimpleDescriptionRequest simpleDescRequest = null;
							if(isSimpleDescEnabled){
								simpleDescriptionService = new SimpleDescriptionServiceImpl();
								String plainTextDesc = txn.getTransactionId() != null ? txnIdToPlainTextDescMap.get(txn.getTransactionId().toString()) : null;
//								txnResp = populateSimpleDescriptionParam(txnResponse);
								simpleDescRequest = new SimpleDescriptionRequest();
								BeanUtils.copyProperties(txnResponse, simpleDescRequest);
								simpleDescRequest.setGeoLocationEnabledInSD(configurations.isGeoLocationEnabledInSD());
								simpleDescRequest.setPlainTextDesc(plainTextDesc);
								simpleDescRequest.setVersion(simpleDescVersion);
								simpleDescRequest.setTxnBaseType(txnIdToBaseTypeMap.get(txn.getTransactionId().toString()));
								simpleDescRequest.setLocale(request.getConfigurations().getLocale());
								simpleDescRequest.setContainer(request.getContainer());
								simpleDescription = simpleDescriptionService.deriveSimpleDescription(simpleDescRequest);
							}
							if(StringUtils.isNotEmpty(simpleDescription))
								txnResponse.setSimpleDescription(simpleDescription);
							txns.add(txnResponse);
						}else {
							if(logger.isErrorEnabled())
								logger.error("TransactionCategory is null for categoryLabel  : " + txn.getLabels() + " for  Unique Txn Id::"+ request.getUniqueTrackingId());
						}	
					}
				}
				if(emptyGranularCategoryCount>0) {
					logger.error("The number of Granular Categories empty from TDE response are "+ emptyGranularCategoryCount+" for Unique Txn Id:: "+request.getUniqueTrackingId());
				}
				if(emptyGranularCategoryMappingCount>0) {
					logger.error("The number of Granular Categories to Master Category Mapping Not Found from TDE response are "+ emptyGranularCategoryMappingCount+" for Unique Txn Id:: "+request.getUniqueTrackingId());
				}
			}
		} catch (RestClientException e) {
			if(logger.isErrorEnabled())
				logger.error("RestClientException while doing categorisation for  Unique Txn Id:: "+request.getUniqueTrackingId(),e);
		} catch (JsonProcessingException e) {
			if(logger.isErrorEnabled())
				logger.error("JsonProcessingException while doing categorisation for  Unique Txn Id:: "+request.getUniqueTrackingId(),e);
		}finally{
			txnIdToPlainTextDescMap.clear();
		}
		long timeTaken = System.currentTimeMillis() - startTime;
		categorizationStats.setMeerkatTimeTaken(timeTaken);
		if(logger.isInfoEnabled())
			logger.info("Time taken by meerkat for uniqueTrackingId in ms "+request.getUniqueTrackingId()+" for "+ txnCount +" txns" +" is " + timeTaken);
		return txns;
	}

	public List<MeerkatResponse> processMeerkatRequest(CategorizationRequest request,
			HttpHeaders headers, CategorizationResponse categorizationResponse) throws JsonProcessingException {
		UUID uuid = UUID.randomUUID();
		//These default services are being picked from region table which is being cached during startup
		List<String> defaultServicesList = getServicesList(request.getRegion());
		Configurations configurations = request.getConfigurations();
		String meerkatUrl = null;
		if(StringUtils.isNotEmpty(configurations.getMeerkatUrl())){
			meerkatUrl = configurations.getMeerkatUrl();
		}
		if(StringUtils.isEmpty(meerkatUrl) && null != request.getRegion()){
			meerkatUrl = urlConfig.getRegion().get(request.getRegion().intValue());			
		}
		if(StringUtils.isEmpty(meerkatUrl)){
			meerkatUrl = caasConfigBean.getCaas().getMeerkatUrl();
		}
		if(StringUtils.isNotEmpty(meerkatUrl)) {
			meerkatUrl= meerkatUrl.trim();
		}
		if(logger.isInfoEnabled())
		logger.info("TDE URL " + meerkatUrl);
		MeerkatRequest meerkatRequest = new MeerkatRequest(request,defaultServicesList);
		if(meerkatRequest.getServices() !=null && meerkatRequest.getServices().size() > Constants.ZERO_LONG.intValue()){
			meerkatRequest.getTxns().clear();
			List<TransactionRequest> clonedTxns = new ArrayList<>(request.getTxns());
			return recursiveProcess(meerkatRequest, clonedTxns, request.getAccountType(), headers, uuid, categorizationResponse, meerkatUrl, false, false,request.getUniqueTrackingId());
		}else{
			if(logger.isErrorEnabled())
				logger.error("Service List combination not valid....");
			return null;
		}
	}	
	
	private List<MeerkatResponse> recursiveProcess(MeerkatRequest meerkatRequest,List<TransactionRequest> txns,String acctType,HttpHeaders headers,UUID uuid, CategorizationResponse categorizationResponse, String meerkatUrl,
			boolean firstInternalServerError, boolean secondInternalServerError,String uniqueTrackingId){
		
		List<MeerkatResponse> responses = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		meerkatRequest.addAllTxns(txns);
		String meerkatRequestJson = null;
		String meerkatResponseJson = null;
		try {
			meerkatRequestJson = mapper.writeValueAsString(meerkatRequest);
		} catch (JsonProcessingException exception) {
			if(logger.isErrorEnabled())
				logger.error("JsonProcessingException in recursiveProcess for memId "+meerkatRequest.getMemId()+ "and cobrandId "+ meerkatRequest.getCobrandId() + " for tde url "+meerkatUrl +"with Unique Txn Id::" +uniqueTrackingId ,exception);
		}
		if(StringUtils.isEmpty(meerkatRequestJson)){
			return null;
		}
		MeerkatResponse meerkatResponse = null;
		Response response = null;
		boolean internalServerErrorOccured = Boolean.FALSE;
		boolean success = Boolean.FALSE;
		int txnBatchCount = getMeerkatTxnBatchSize();
		try {
			if(txns.size() < txnBatchCount){
				Executor executor = FluentExecutor.getExecutor();
				if (caasConfigBean.getCaas().isProxyEnabled()) {
					response = executor.execute(Request.Post(meerkatUrl).viaProxy(httpHost)
							.connectTimeout(caasConfigBean.getCaas().getConnectionTimeout()).socketTimeout(caasConfigBean.getCaas().getSocketTimeout())
							.bodyString(meerkatRequestJson, ContentType.APPLICATION_JSON));
				} else {
					response = executor.execute(Request.Post(meerkatUrl).connectTimeout(caasConfigBean.getCaas().getConnectionTimeout())
							.socketTimeout(caasConfigBean.getCaas().getSocketTimeout())
							.bodyString(meerkatRequestJson, ContentType.APPLICATION_JSON));
				}
				org.apache.http.HttpResponse httpResponse = response != null ? response.returnResponse() : null;
				int rc = (httpResponse != null && httpResponse.getStatusLine() != null) ? httpResponse.getStatusLine().getStatusCode() : 0;
				if (rc == org.apache.http.HttpStatus.SC_OK) {
					meerkatResponseJson = EntityUtils.toString(httpResponse.getEntity(), "UTF8");
					meerkatResponse = mapper.readValue(meerkatResponseJson, MeerkatResponse.class);
					success = Boolean.TRUE;
				}else if(rc == org.apache.http.HttpStatus.SC_NO_CONTENT){
					if(logger.isErrorEnabled())
						logger.error(String.format("No content from Meerkat service , Response Status Code:"+ rc + " tde url: "+meerkatUrl + " Unique Txn Id::"+ uniqueTrackingId));
				}else if(rc == org.apache.http.HttpStatus.SC_NOT_FOUND){
					if(logger.isErrorEnabled())
						logger.error(String.format("DAAS OPS Alert: URI mismatch from Meerkat service , Response Status Code:"+ rc+ " tde url: "+meerkatUrl + " Unique Txn Id::"+ uniqueTrackingId));
				}else if(rc == org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR){
					internalServerErrorOccured = Boolean.TRUE;
					if(logger.isErrorEnabled())
						logger.error(String.format("DAAS OPS Alert: Internal server error: Meerkat service , Response Status Code:"+ rc+ " tde url: "+meerkatUrl + " Unique Txn Id::"+ uniqueTrackingId));
				}else {
					if (logger.isErrorEnabled()) {
						logger.error("DAAS OPS Alert: Failed to get result from Categorisation Service, Response Status Code: "+ rc+ " tde url: "+meerkatUrl + " Unique Txn Id::"+ uniqueTrackingId);
					}
				}
			}
		} catch (ClientProtocolException exception) {
			if(logger.isErrorEnabled())
				logger.error("DAAS OPS Alert: ClientProtocolException in recursiveProcess for  tde url: "+meerkatUrl + " Unique Txn Id::"+ uniqueTrackingId,exception);
		} catch (IOException exception) {
			if(logger.isErrorEnabled())
				logger.error("DAAS OPS Alert: IOException in recursiveProcess for  tde url: "+meerkatUrl + " Unique Txn Id::"+ uniqueTrackingId,exception);
		}catch(Exception exception){
			if(logger.isErrorEnabled())
				logger.error("DAAS OPS Alert: Exception in recursiveProcess for  tde url: "+meerkatUrl + " Unique Txn Id::"+ uniqueTrackingId,exception);
		}finally{
			meerkatRequest.getTxns().clear();
			//If the request is not success and internal server error has not occured, mark the request for retry
			if(!success && !internalServerErrorOccured && txns.size() < txnBatchCount){
				if(categorizationResponse.isRetry() == false){
					categorizationResponse.setRetry(Boolean.TRUE);
				}
			} 
			boolean applySplitLogic = true;
			boolean tempFirstInternalServerError = false;
			boolean tempSecondInternalServerError = secondInternalServerError;
			if(firstInternalServerError && internalServerErrorOccured){
				applySplitLogic = false;
				secondInternalServerError = true;
				tempSecondInternalServerError = true;
			}
			if(internalServerErrorOccured){
				tempFirstInternalServerError = true;
			}else if(firstInternalServerError){
				tempFirstInternalServerError = true;
			}
			if(success && (txns.size() < txnBatchCount)){
				if(meerkatResponse != null){
					responses.add(meerkatResponse);
				}
			}else if(((txns.size() >= txnBatchCount) || (internalServerErrorOccured && txns.size() > 1)) && applySplitLogic) {
				int splitSize = txns.size() / 2;
				List<TransactionRequest> subTxnsOne = txns.subList(0, splitSize);
				responses.addAll(recursiveProcess(meerkatRequest, subTxnsOne, acctType, headers, uuid, categorizationResponse, meerkatUrl, tempFirstInternalServerError, tempSecondInternalServerError,uniqueTrackingId));
				List<TransactionRequest> subTxnsTwo = txns.subList(splitSize, txns.size());
				responses.addAll(recursiveProcess(meerkatRequest, subTxnsTwo, acctType, headers, uuid, categorizationResponse, meerkatUrl, tempFirstInternalServerError, tempSecondInternalServerError,uniqueTrackingId));
			}
		}
		return responses;
	}
	/**
	 * Returns the number of transactions to be sent in one HTTP call made to Meerkat web service (set in config db)
	 * By default, this value is set to 64 here
	 */
	public int getMeerkatTxnBatchSize()
	{
		int txnBatchCount = 64;
		try{
			String txnBatchCountStr = caasConfigBean.getCaas().getBatchSize(); 
			if(StringUtils.isNotEmpty(txnBatchCountStr)){
				txnBatchCount = Integer.parseInt(txnBatchCountStr);
			}
		}catch(Exception e){
			if(logger.isInfoEnabled())
				logger.info("Meerkat txn batch size is invalid ");
		}
		return txnBatchCount;
	}
	
	private String deriveSourceMerchantId(MeerkatTransaction output)
	{
		if(output == null)
			return null;
		
		String sourceMerchantId = "";
		
		if(StringUtils.isNotEmpty(output.getSourceMerchantId()))
			sourceMerchantId = output.getSourceMerchantId();
		else
		{
			if(StringUtils.isNotEmpty(output.getMerchant()))
				sourceMerchantId += "-M-"+ output.getMerchant().toLowerCase();
			if(StringUtils.isNotEmpty(output.getStreet()))
				sourceMerchantId += "-S-" +output.getStreet().toLowerCase();
			if(StringUtils.isNotEmpty(output.getCity()))
				sourceMerchantId += "-C-"+output.getCity().toLowerCase();
			if(StringUtils.isNotEmpty(output.getState()))
				sourceMerchantId += "-ST-"+output.getState().toLowerCase();
			if(StringUtils.isNotEmpty(output.getPostalCode()))
				sourceMerchantId += "-Z-"+output.getPostalCode().toLowerCase();
			if(StringUtils.isNotEmpty(output.getCountry()))
				sourceMerchantId += "-CY-"+output.getCountry().toLowerCase();
			
			sourceMerchantId = sourceMerchantId.replaceAll(" ", "");
		}
		
		if(StringUtils.isEmpty(sourceMerchantId))
			sourceMerchantId = null;
		
		return sourceMerchantId;
	}
	
	/*
	 * Log the debugging score below threshold limit for Category, Merchant and Subtype
	 */
	private void logDebugScore(CategorizationRequest request, MeerkatTransaction output)
	{
		if(output != null){
			double categoryScore;
			double subTypeScore;
			double merchantScore;
			
			if(StringUtils.isNotEmpty(output.getDebugInfo().getCategoryScore())){
				categoryScore = Double.valueOf(output.getDebugInfo().getCategoryScore());
				String requestDesc = null;
				String ledgerType = null;
				String container = null;
				if((((Double)categoryScore).compareTo(Double.valueOf(caasConfigBean.getCaas().getThresholdCategoryScore())))<0){
					for (TransactionRequest req : request.getTxns()) {
						if(req.getTransactionId().equals(output.getTransactionId())){
							requestDesc = maskDescription(req.getDescription());
							ledgerType = req.getTransactionType();
							container = request.getContainer();
						}
					}
					if(logger.isWarnEnabled())
						logger.warn("Category Score : "+categoryScore +" lesser than configured threshold value : "+ caasConfigBean.getCaas().getThresholdCategoryScore()  + " for Cobrand ID : "+request.getCobrandId() + 
							", Description : " +requestDesc +", Ledger : "+ledgerType +", Container : "+container);
				}
			}
			
			if(StringUtils.isNotEmpty(output.getDebugInfo().getSubTypeScore())){
				subTypeScore = Double.valueOf(output.getDebugInfo().getSubTypeScore());
				String requestDesc = null;
				String ledgerType = null;
				String container = null;
				if(((Double)subTypeScore).compareTo(Double.valueOf(caasConfigBean.getCaas().getThresholdSubtypeScore()))<0){
					for (TransactionRequest req : request.getTxns()) {
						if(req.getTransactionId().equals(output.getTransactionId())){
							requestDesc = maskDescription(req.getDescription());
							ledgerType = req.getTransactionType();
							container = request.getContainer();
						}
					}
					if(logger.isWarnEnabled())
						logger.warn("SubType Score : "+subTypeScore +" lesser than configured threshold value : "+ caasConfigBean.getCaas().getThresholdSubtypeScore()  + " for Cobrand ID : "+request.getCobrandId() + 
							", Description : " +requestDesc +", Ledger : "+ledgerType +", Container : "+container);
				}
			}
			
			if(StringUtils.isNotEmpty(output.getDebugInfo().getMerchantScore())){
				merchantScore = Double.valueOf(output.getDebugInfo().getMerchantScore());
				String requestDesc = null;
				String ledgerType = null;
				String container = null;
				if(((Double)merchantScore).compareTo(Double.valueOf(caasConfigBean.getCaas().getThresholdMerchantScore()))<0){
					for (TransactionRequest req : request.getTxns()) {
						if(req.getTransactionId().equals(output.getTransactionId())){
							requestDesc = maskDescription(req.getDescription());
							ledgerType = req.getTransactionType();
							container = request.getContainer();
						}
					}
					if(logger.isWarnEnabled())
						logger.warn("Merchant Score : "+merchantScore +" lesser than configured threshold value"+ caasConfigBean.getCaas().getThresholdMerchantScore() + " for Cobrand ID : "+request.getCobrandId() + 
							", Description : " +requestDesc +", Ledger : "+ledgerType +", Container : "+container);
				}
			}
		}
	}
	
	/*
	 * mask the input for
	 *	Any number greater than or equal to 8 digits to be masked
	 *	Any credit card/SSN number patterns to be masked.
	 */
	private static String maskDescription(String data){
		int[] a = new int[data.length()];
		char[] ch = data.toCharArray();
		int k = 0;
		int i = 0;
		for (i = 0; i < ch.length; i++) {
			int temp = i;
			if (isSpecialAndDigit(ch[i])) {
				int countNumber = 0;
				for (int j = i; j < ch.length; j++) {
					if (isDigit(ch[j])) {
						temp = j;
						countNumber = countNumber + 1;
						if (countNumber == 8) {
							a[k] = i;
							k++;
							a[k] = j;
							k++;
						}
					} else if (isSpecialChar(ch[j])) {
						temp = j;
					} else {
						temp = j;
						break;
					}
				}
			} else {
				i = temp;
			}
			i = temp;
		}

		String desc =  displayMaskValue(data, a, ch);
		if (desc.matches(".*"+ccPattern+".*")) {
	    	Pattern compile = Pattern.compile(ccPattern);
	        Matcher matcher = compile.matcher(desc);
	        matcher.find();
	        String masked = matcher.group().replaceAll("[0-9]", "X");
	        desc=desc.replaceAll(ccPattern, masked);
	    }
		
		return desc;
	}
	
	private static String displayMaskValue(String data, int[] a, char[] ch) {
		StringBuffer br = new StringBuffer();
		int pointer = 0;
		for (int m = 0; m < ch.length; m++) {
			if (isSpecialAndDigit(ch[m])) {
				for (int j = pointer; j < a.length; j++) {
					if (a[j] >= 0 && m == a[j]) {
						pointer = pointer + 2;
						if (a[j + 1] >= 0) {
							for (int j2 = a[j]; j2 <= a[j + 1]; j2++) {
								if (isDigit(data.charAt(j2))) {
									br.append('X');
								} else {
									br.append(data.charAt(j2));
								}
							}
							m = a[j + 1];
							break;
						}
					} else {
						br.append(ch[m]);
						break;
					}
				}

			} else {
				br.append(ch[m]);
			}
		}
		return br.toString();
	}

	private static boolean isDigit(char c) {
		boolean isDigitAndCharacter = (c >= '0' && c <= '9');
		return isDigitAndCharacter;
	}

	private static boolean isSpecialChar(char c) {
		boolean isSpecial = ((c == '!') || (c == '@') || (c == '#')
				|| (c == '$') || (c == '&') || (c == '(') || (c == ')')
				|| (c == '\\') || (c == '-') || (c == '`') || (c == '+')
				|| (c == ',') || (c == '/') || (c == '"') || (c == ' '));
		return isSpecial;
	}

	private static boolean isSpecialAndDigit(char ch) {
		boolean isDigitAndCharacter = ((ch >= '0' && ch <= '9') || (ch == '!')
				|| (ch == '@') || (ch == '#') || (ch == '$') || (ch == '&')
				|| (ch == '(') || (ch == ')') || (ch == '\\') || (ch == '-')
				|| (ch == '`') || (ch == '+') || (ch == ',') || (ch == '/') || (ch == '"') || (ch == ' '));
		return isDigitAndCharacter;
	}
	private Future<List<TransactionResponse>> submitCategorizeThread(CategorizationRequest categorizationRequest,HttpHeaders httpHeaders,CountDownLatch countDownLatch) {
		
			Callable<List<TransactionResponse>> categorizeThread = new Callable<List<TransactionResponse>>(){
				@Override
				public List<TransactionResponse> call() throws JsonProcessingException {
					List<TransactionResponse> transactionResponseList = applyCategorization(categorizationRequest, httpHeaders, null, null, new YCategorizationStats());
					countDownLatch.countDown();
					return transactionResponseList;
				}
			};
			return meerkatExecutorCompletionService.submit(categorizeThread); 
	}
	private List<String> getServicesList(Long regionId){
		
		Map<Long, List<String>> regionIdToServicesListMap = regionCache.getRegionIdToServicesListMap();
		if(regionIdToServicesListMap != null){
			return regionIdToServicesListMap.get(regionId);
		}else{
			return null;
		}		
	}
	
	private Long getTransactionMerchantTypeId(String txnMerchantTypeName) {
		
		Map<String,Long> txnMerchantTypeNametoIdMap = txnMerchantTypeCache.getTxnMerchantTypeNameToIdMap();
		if(txnMerchantTypeNametoIdMap != null){
			if(StringUtils.isNotEmpty(txnMerchantTypeName)) {
				return txnMerchantTypeNametoIdMap.get(txnMerchantTypeName.toLowerCase());
			}else {
				if(logger.isErrorEnabled())
				logger.error("TxnMerchantTypeId is null for txnMerchantTypeName : " + txnMerchantTypeName);
			}
		}else{
			return null;
		}			
		return null;
		
	}
	
	private Long getSMBTwoTxnCategoryId(String smbTwoTxnCategoryName) {
			
			Map<String,Long> smbTwoTxnCategoryNameToIdMap = smbTwoTxnCategoryCache.getSmbTwoTxnCategoryNameToIdMap();
			if(smbTwoTxnCategoryNameToIdMap != null){
				if(StringUtils.isNotEmpty(smbTwoTxnCategoryName)) {
					return smbTwoTxnCategoryNameToIdMap.get(smbTwoTxnCategoryName.toLowerCase());
				}
			}		
			return null;
			
		}

	/**
	 * This method is used to enrich the MCC Category Response with TDE Enriched Data like type, subtype, merchant etc
	 * @param txn
	 * @param txnResponse
	 * @param configurations
	 * @param txnIdToPlainTextDescMap
	 * @param request
	 */
	private void enrichMCCResponseWithTDEFields(MeerkatTransaction txn,
			EnrichedTransactionResponse txnResponse, Configurations configurations,
			HashMap<String, String> txnIdToPlainTextDescMap, HashMap<String,String> txnIdToBaseTypeMap, CategorizationRequest request) {

		boolean isSimpleDescEnabled = configurations.isSimpleDescEnabled();
		String simpleDescVersion=null;
		if(StringUtils.isNotEmpty(configurations.getSimpleDescVersion())){
			simpleDescVersion = configurations.getSimpleDescVersion();
		}else{
			simpleDescVersion = caasConfigBean.getCaas().getSimpleDescriptionVersion();
		}
		String sourceMerchantId = null, simpleDescription = null;
		sourceMerchantId = deriveSourceMerchantId(txn);
		txnResponse.setMerchant(txn.getMerchant());
		txnResponse.setMeerkatMerchant(txn.getMerchant());
		txnResponse.setMeerkatType(txn.getType());
		txnResponse.setMeerkatSubType(txn.getSubType());
		txnResponse.setStreet(txn.getStreet());
		txnResponse.setCity(txn.getCity());
		txnResponse.setState(txn.getState());
		txnResponse.setZip(txn.getPostalCode());
		txnResponse.setCountry(txn.getCountry());
		txnResponse.setLongitude(txn.getLongitude());
		txnResponse.setLatitude(txn.getLatitude());
		txnResponse.setFaxNumber(txn.getFax());
		txnResponse.setChainName(txn.getChainName());
		txnResponse.setNeighbourhood(txn.getNeighbourhood());
		txnResponse.setPhoneNumber(txn.getPhone());
		txnResponse.setMerchantSource(txn.getSource());
		txnResponse.setSourceMerchantId(sourceMerchantId);
		txnResponse.setStoreId(txn.getStoreId());
		txnResponse.setWebsite(txn.getWebsite());
		txnResponse.setPostalCode(txn.getPostalCode());
		txnResponse.setEmail(txn.getEmail());
		txnResponse.setConfidenceScore(txn.getConfidenceScore());
		txnResponse.setFactualCategory(txn.getFactualCategory());
		txnResponse.setSmbTwoTxnCategoryId(getSMBTwoTxnCategoryId(txn.getSmbCategory()));							
		if (txnResponse.getSmbTwoTxnCategoryId() != null
				&& txnResponse.getSmbTwoTxnCategoryId() != 0L) {
			if (StringUtils.isNotEmpty(txn.getIsBusinessRelated()) && txn.getIsBusinessRelated().equalsIgnoreCase("Yes")) {
				txnResponse.setIsBusinessRelated(1L);
			}
			else if (StringUtils.isNotEmpty(txn.getIsBusinessRelated()) && txn.getIsBusinessRelated().equalsIgnoreCase("No")) {
				txnResponse.setIsBusinessRelated(0L);
			}
		}
		if (txn.getVendorNameVariation() != null && txn.getVendorNameVariation().length > 2) {
			if (StringUtils.isNotEmpty(txn.getVendorNameVariation()[2])
					&& Integer.parseInt(txn.getVendorNameVariation()[2]) > 0
					&& caasConfigBean.getCaas().getConfidenceScoreThreshold() != 0
					&& Integer.parseInt(txn.getVendorNameVariation()[2]) < caasConfigBean.getCaas()
							.getConfidenceScoreThreshold())
				if (StringUtils.isNotEmpty(txn.getVendorNameVariation()[1])
						&& Character.toString(txn.getVendorNameVariation()[1].charAt(0))
								.equals(Constants.VENDOR_NAME_VARIATION_TYPE)) {
					if (StringUtils.isNotEmpty(txn.getVendorNameVariation()[0])) {
						txnResponse.setVendorName(txn.getVendorNameVariation()[0]);
						configurations.setGeoLocationEnabledInSD(false);
						CommonHelper.nullifyMeerkatMerchantFields(txnResponse);
					}
				}
		}
		CNNInfo debugInfo = txn.getDebugInfo();
		if (debugInfo != null) {
			txnResponse.setCnn(debugInfo);
		}
		txnResponse.setLabels(CommonHelper.getCategoryLabelsAsString(txn.getLabels()));
		if (caasConfigBean.getCaas().isPopulateAdditionalFields()) {
			if (txn.isRecurring()) {
				txnResponse.setIsRecurring(1L);
			} else {
				txnResponse.setIsRecurring(0L);
			}
			txnResponse.setPeriodicity(txn.getPeriodicity());
			txnResponse.setModeOfPayment(txn.getModeOfPayment());
			txnResponse.setTdePiiInfo(txn.getTdePiiInfo());
		}
		if(caasConfigBean.getCaas().isPopulateIntermediary()){
			txnResponse.setIntermediary(txn.getIntermediary());
		}
		if (caasConfigBean.getCaas().isPopulateIsPhysical()){
			if(txn.isPhysicalMerchant())
				txnResponse.setIsPhysical(1L);
			else
				txnResponse.setIsPhysical(0L);
		}
		if (StringUtils.isNotEmpty(request.getContainer()) && (Container.LOAN.toString()
				.equalsIgnoreCase(request.getContainer())
				|| Container.INVESTMENT.toString().equalsIgnoreCase(request.getContainer()))) {
			if (caasConfigBean.getCaas().isPopulateDigitalFieldForLoanAndInvestment()) {
				if (caasConfigBean.getCaas().isPopulateTxnMerchantType()) {
					txnResponse.setTxnMerchantTypeId(
							getTransactionMerchantTypeId(txn.getTxnMerchantType()));
					txnResponse.setTxnMerchantType(
							getTransactionMerchantTypeId(txn.getTxnMerchantType()));
					if (StringUtils.isNotEmpty(txn.getTxnMerchantType())) {
						txnResponse.setMerchantType(txn.getTxnMerchantType().toUpperCase());
					}
				}
			}
		} else if (StringUtils.isNotEmpty(request.getContainer())
				&& (Container.BANK.toString().equalsIgnoreCase(request.getContainer())
						|| Container.CARD.toString().equalsIgnoreCase(request.getContainer()))) {
			if (caasConfigBean.getCaas().isPopulateTxnMerchantType()) {
				txnResponse.setTxnMerchantTypeId(
						getTransactionMerchantTypeId(txn.getTxnMerchantType()));
				txnResponse
						.setTxnMerchantType(getTransactionMerchantTypeId(txn.getTxnMerchantType()));
				if (StringUtils.isNotEmpty(txn.getTxnMerchantType())) {
					txnResponse.setMerchantType(txn.getTxnMerchantType().toUpperCase());
				}
			}
		}
		if(caasConfigBean.getCaas().isPopulateLogoEndpoint()){
			txnResponse.setLogoEndpoint(txn.getLogoEndpoint());
		}
		SimpleDescriptionRequest simpleDescRequest= null;
		if(isSimpleDescEnabled){
			simpleDescriptionService = new SimpleDescriptionServiceImpl();
			simpleDescRequest = new SimpleDescriptionRequest();
			
			String plainTextDesc = txn.getTransactionId() != null ? txnIdToPlainTextDescMap.get(txn
					.getTransactionId().toString()) : null;
			
			BeanUtils.copyProperties(txnResponse, simpleDescRequest);
			simpleDescRequest.setGeoLocationEnabledInSD(configurations.isGeoLocationEnabledInSD());
			simpleDescRequest.setPlainTextDesc(plainTextDesc);
			simpleDescRequest.setVersion(simpleDescVersion);
			simpleDescRequest.setTxnBaseType(txnIdToBaseTypeMap.get(txn.getTransactionId().toString()));
			simpleDescRequest.setLocale(configurations.getLocale());
			simpleDescRequest.setContainer(request.getContainer());
			simpleDescription = simpleDescriptionService.deriveSimpleDescription(simpleDescRequest);
		}
		if (StringUtils.isNotEmpty(simpleDescription))
			txnResponse.setSimpleDescription(simpleDescription);

	}
}
