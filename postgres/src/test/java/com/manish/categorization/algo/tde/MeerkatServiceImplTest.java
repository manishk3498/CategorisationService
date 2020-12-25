package com.manish.categorization.algo.tde;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorCompletionService;

import org.apache.http.client.ClientProtocolException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.manish.categorization.URLConfig;
import com.manish.categorization.algo.tde.MeerkatServiceImpl;
import com.manish.categorization.algo.tde.dto.MeerkatResponse;
import com.manish.categorization.algo.tde.dto.MeerkatResponse.CNNInfo;
import com.manish.categorization.algo.tde.dto.MeerkatResponse.MeerkatData;
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
import com.manish.categorization.rest.AbstractTest;
import com.manish.categorization.rest.dto.CategorizationRequest;
import com.manish.categorization.rest.dto.CategorizationResponse;
import com.manish.categorization.rest.dto.EnrichedTransactionResponse;
import com.manish.categorization.rest.dto.TransactionResponse;
import com.manish.categorization.sdp.config.Caas;
import com.manish.categorization.sdp.config.CaasConfigBean;
import com.manish.categorization.util.CommonUtil;
import com.manish.categorization.util.Constants;
import com.manish.categorization.util.TestConstants;
import com.manish.categorization.util.YCategorizationStats;
import com.yodlee.simpledescservice.service.SimpleDescriptionService;

import junit.framework.Assert;

public class MeerkatServiceImplTest extends AbstractTest {
	
	@Mock
	RestTemplate restTemplate;
	
	@Mock
	FluentExecutor fluentExecutor;
	
	@Mock
	TransactionCategoryCache categoryCache;
	
	@Mock
	GranularCategoryCache granularCategoryCache;
	
	@Mock
	TransactionClassificationCache transactionClassificationCache;
	
	@Mock
	SiteInfoCache siteInfoCache;
	
	@Mock
	ExecutorCompletionService<List<TransactionResponse>> meerkatExecutorCompletionService;
	
	SimpleDescriptionService simpleDescriptionService = null;

	@Mock
	RegionCache regionCache;
	
	@Mock
	TxnMerchantTypeCache txnMerchantTypeCache;
	
	@Mock
	SMBTwoTxnCategoryCache smbTwoTxnCategoryCache;
	
	@Mock
	URLConfig urlConfig;
	
	@InjectMocks@Spy
	MeerkatServiceImpl meerkatServiceImpl;
	
	@Mock
	CaasConfigBean caasConfigBean;
	
	@Override
	@Before
	public void setUp() {
      super.setUp();
      MockitoAnnotations.initMocks(this);
	}	
	
	public Caas getCaas() {
		Caas caasObj = new Caas();
		caasObj.setPopulateAdditionalFields(true);
		caasObj.setPopulateIntermediary(true);
		caasObj.setPopulateIsPhysical(true);
		caasObj.setPopulateTxnMerchantType(true);
		caasObj.setPopulateLogoEndpoint(true);
		caasObj.setPopulateDigitalFieldForLoanAndInvestment(true);
		caasObj.setMeerkatUrl("https://10.20.36.200/meerkat/");
		caasObj.setParallelEnabled(false);
		caasObj.setBatchSize("32");
		caasObj.setMeerkatThreadTimeout(2000);
		caasObj.setConfidenceScoreThreshold(98);
		caasObj.setExtraThreadDelay(10);
		caasObj.setSimpleDescriptionVersion("1.1");
		 return caasObj;
	}
	
	@Test
	public void testMeerkatCategorizationWithMCCResponseForAutomtotiveExpenses() throws ClientProtocolException, IOException {
		meerkatCategorizationWithMCCResponse(TestConstants.AUTOMOTIVE_FUEL);
	}
	@Test
	public void testMeerkatCategorizationWithMCCResponseForCableSatelliteTelecom() throws ClientProtocolException, IOException {
		meerkatCategorizationWithMCCResponse(TestConstants.CABLE_SATELLITE_TELECOM);
	}
	@Test
	public void testMeerkatCategorizationWithMCCResponseForHealthcareMedical() throws ClientProtocolException, IOException {
		meerkatCategorizationWithMCCResponse(TestConstants.HEALTHCARE_MEDICAL);
	}
	private void meerkatCategorizationWithMCCResponse(String categoryName)
			throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCategorizationRequest();
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		EnrichedTransactionResponse txnResponse = CommonUtil.getEnrichedTransactionResponse(categoryName);
		mccTxnCatIdToResponseMap.put(new BigInteger("2"),txnResponse);
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, CommonUtil.getServicesForUSRegion());
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(CommonUtil.getServicesForUSRegion());
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction(categoryName);  
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		//when(meerkatServiceImpl.processMeerkatRequest(request, headers, categorizationResponse)).thenReturn(meerkatResponses);
		TransactionCategory cat = CommonUtil.getTransactionCategory(categoryName,categoryName);
		when (categoryCache.getTransactionCategory(meerkatTransaction.getLabels(),true)).thenReturn(cat);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		//when (granularCategoryCache.getGranularCategory(meerkatTransaction.getGranularCategory(), false, txnIdToBaseTypeMap.get(meerkatTransaction.getTransactionId().toString()))).thenReturn(CommonUtil.getGranualarCategoryMapping());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);
		for(TransactionResponse txnRe : responses ) {
			Assert.assertEquals(txnRe.getCategory(), categoryName);
		}
	}
	@Test
	public void testMeerkatCategorizationWithMCCResponseForIndiaRegionForAutomotiveExpenses() throws ClientProtocolException, IOException {
		meerkatCategorizationWithMCCResponseForIndiaRegion(TestConstants.AUTOMOTIVE_FUEL);
	}
	@Test
	public void testMeerkatCategorizationWithMCCResponseForIndiaRegionForCableSatellite() throws ClientProtocolException, IOException {
		meerkatCategorizationWithMCCResponseForIndiaRegion(TestConstants.RENT);
	}
	@Test
	public void testMeerkatCategorizationWithMCCResponseForIndiaRegionForHealthcareMedical() throws ClientProtocolException, IOException {
		meerkatCategorizationWithMCCResponseForIndiaRegion(TestConstants.TRAVEL);
	}
	private void meerkatCategorizationWithMCCResponseForIndiaRegion(String categoryName)
			throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCategorizationRequest();
		request.setRegion(2L);
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		EnrichedTransactionResponse txnResponse = CommonUtil.getEnrichedTransactionResponse(categoryName);
		mccTxnCatIdToResponseMap.put(new BigInteger("2"),txnResponse);
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(2L, CommonUtil.getServicesForIndiaRegion());
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(CommonUtil.getServicesForIndiaRegion());
		data.setRegion(2L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction(categoryName);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		TransactionCategory cat = CommonUtil.getTransactionCategory(categoryName,categoryName);
		when (categoryCache.getTransactionCategory(meerkatTransaction.getLabels(),true)).thenReturn(cat);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);
		for(TransactionResponse txnRe : responses ) {
			Assert.assertEquals(txnRe.getCategory(), categoryName);
		}
	}
	@Test
	public void testMeerkatCategorization() throws ClientProtocolException, IOException {
		CategorizationRequest request = CommonUtil.getCategorizationRequest();
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction();
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		TransactionCategory cat = CommonUtil.getTransactionCategory(TestConstants.AUTOMOTIVE_FUEL,TestConstants.AUTOMOTIVE_FUEL);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		when (categoryCache.getTransactionCategory(meerkatTransaction.getLabels(),true)).thenReturn(cat);
		//when (granularCategoryCache.getGranularCategory(meerkatTransaction.getGranularCategory(), false, txnIdToBaseTypeMap.get(meerkatTransaction.getTransactionId().toString()))).thenReturn(CommonUtil.getGranualarCategoryMapping());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);
		for(TransactionResponse txnRe : responses ) {
			Assert.assertEquals(txnRe.getCategory(), TestConstants.AUTOMOTIVE_FUEL);
		}
	}
	@Test
	public void testMeerkatCategorizationWithMergerDisabledForAutomotive() throws ClientProtocolException, IOException {
		meerkatCategorizationWithMergerDisabled(TestConstants.AUTOMOTIVE_EXPENSES,TestConstants.AUTOMOTIVE);
	}
	
	@Test
	public void testMeerkatCategorizationWithMergerDisabledForRestaurants() throws ClientProtocolException, IOException {
		meerkatCategorizationWithMergerDisabled(TestConstants.RESTAURANTS,TestConstants.FOOD_DINING);
	}
	
	
	private void meerkatCategorizationWithMergerDisabled(String categoryName,String granularCategoryName)
			throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
		request.getTxns().get(0).setMccCode(null);
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		EnrichedTransactionResponse txnResponse = CommonUtil.getEnrichedTransactionResponse(categoryName,granularCategoryName);
		mccTxnCatIdToResponseMap.put(new BigInteger("2"),txnResponse);
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction(categoryName,granularCategoryName);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);	
		//when(meerkatServiceImpl.processMeerkatRequest(request, headers, categorizationResponse)).thenReturn(meerkatResponses);
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

		for(TransactionResponse txnRe : responses ) {
			Assert.assertEquals(txnRe.getCategory(), categoryName);
		}
	}
	
	@Test
	public void testMeerkatCategorizationWithoutRecurringFlag() throws ClientProtocolException, IOException {
		List<TransactionResponse> responses = getNullifiedAdditionalFlags();
		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedTransactionResponse = (EnrichedTransactionResponse) txnRe;
			//Assert.assertEquals(enrichedTransactionResponse.getIsRecurring(), "1");
		}
	}
	@Test
	public void testMeerkatCategorizationWithoutIntermediary() throws ClientProtocolException, IOException {
		List<TransactionResponse> responses = getNullifiedAdditionalFlags();
		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedTransactionResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedTransactionResponse.getIntermediary(), null);
		}
	}
	@Test
	public void testMeerkatCategorizationWithoutModeOfPayment() throws ClientProtocolException, IOException {
		List<TransactionResponse> responses = getNullifiedAdditionalFlags();
		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedTransactionResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedTransactionResponse.getModeOfPayment(), null);
		}
	}
	@Test
	public void testMeerkatCategorizationWithoutPeriodicity() throws ClientProtocolException, IOException {
		List<TransactionResponse> responses = getNullifiedAdditionalFlags();
		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedTransactionResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedTransactionResponse.getPeriodicity(), null);
		}
	}
	private List<TransactionResponse> getNullifiedAdditionalFlags()
			throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCategorizationRequest();
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		EnrichedTransactionResponse txnResponse = CommonUtil.getEnrichedTransactionResponse();
		mccTxnCatIdToResponseMap.put(new BigInteger("2"),txnResponse);
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction();
		meerkatTransaction.setRecurring(true);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		//when(meerkatServiceImpl.processMeerkatRequest(request, headers, categorizationResponse)).thenReturn(meerkatResponses);
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);
		return responses;
	}
	
	@Test
	public void testMeerkatCategorizationWithRecurringFlag() throws ClientProtocolException, IOException {
		List<TransactionResponse> responses = getRecurringFields();
		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedTransactionResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedTransactionResponse.getIsRecurring(), (Long)1L);
		}
	}
	@Test
	public void testMeerkatCategorizationWithBillers() throws ClientProtocolException, IOException {
		List<TransactionResponse> responses = getTxnMerchantType("billers");
		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedTransactionResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedTransactionResponse.getTxnMerchantTypeId(), (Long)1L);
		}
	}
	
	@Test
	public void testMeerkatCategorizationWithSubscription() throws ClientProtocolException, IOException {
		List<TransactionResponse> responses = getTxnMerchantType("subscription");
		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedTransactionResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedTransactionResponse.getTxnMerchantTypeId(), (Long)2L);
		}
	}
	
	@Test
	public void testMeerkatCategorizationWithOthers() throws ClientProtocolException, IOException {
		List<TransactionResponse> responses = getTxnMerchantType("others");
		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedTransactionResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedTransactionResponse.getTxnMerchantTypeId(), (Long)3L);
		}
	}
	
	
	@Test
	public void testMeerkatCategorizationWithIntermediary() throws ClientProtocolException, IOException {
		List<TransactionResponse> responses = getRecurringFields();
		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedTransactionResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedTransactionResponse.getIntermediary(), "dummyintermediary");
		}
	}
	@Test
	public void testMeerkatCategorizationWithModeOfPayment() throws ClientProtocolException, IOException {
		List<TransactionResponse> responses = getRecurringFields();
		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedTransactionResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedTransactionResponse.getModeOfPayment(), "Paypal");
		}
	}
	private List<TransactionResponse> getRecurringFields()
			throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCategorizationRequest();
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		EnrichedTransactionResponse txnResponse = CommonUtil.getEnrichedTransactionResponse();
		mccTxnCatIdToResponseMap.put(new BigInteger("2"),txnResponse);
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction();
		meerkatTransaction.setRecurring(true);
		meerkatTransaction.setPeriodicity("Monthly");
		meerkatTransaction.setIntermediary("dummyintermediary");
		meerkatTransaction.setModeOfPayment("Paypal");
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		//when(meerkatServiceImpl.processMeerkatRequest(request, headers, categorizationResponse)).thenReturn(meerkatResponses);
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);
		return responses;
	}
	
	private List<TransactionResponse> getTxnMerchantType(String txnMerchantType)
			throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCategorizationRequest();
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		EnrichedTransactionResponse txnResponse = CommonUtil.getEnrichedTransactionResponse();
		mccTxnCatIdToResponseMap.put(new BigInteger("2"),txnResponse);
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		Map<String,Long> txnMerchantTypeNameToIdMap = new HashMap<String,Long>();
		txnMerchantTypeNameToIdMap.put("billers", 1l);
		txnMerchantTypeNameToIdMap.put("subscription", 2l);
		txnMerchantTypeNameToIdMap.put("others", 3l);
		when(txnMerchantTypeCache.getTxnMerchantTypeNameToIdMap()).thenReturn(txnMerchantTypeNameToIdMap);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction();
		meerkatTransaction.setTxnMerchantType(txnMerchantType);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		//when(meerkatServiceImpl.processMeerkatRequest(request, headers, categorizationResponse)).thenReturn(meerkatResponses);
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);
		return responses;
	}
	
	@Test
	public void testCategoriseWithPopulateAdditionalFieldTrueWithoutIsRecurring() throws ClientProtocolException, IOException {

		CategorizationRequest request = CommonUtil.getCategorizationRequest();
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		EnrichedTransactionResponse txnResponse = CommonUtil.getEnrichedTransactionResponse();
		mccTxnCatIdToResponseMap.put(new BigInteger("2"),txnResponse);
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction();
		meerkatTransaction.setRecurring(false);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		//when(meerkatServiceImpl.processMeerkatRequest(request, headers, categorizationResponse)).thenReturn(meerkatResponses);
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);
		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedTransactionResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedTransactionResponse.getIsRecurring(), (Long)0L);
		}
	
	}
	
	@Test
	public void testSimpleDescription() throws ClientProtocolException, IOException {
		CategorizationRequest request = CommonUtil.getCategorizationRequest();
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		mccTxnCatIdToResponseMap.put(new BigInteger("2"),null);
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = new ArrayList<String>();
		services.add(Constants.CNN_MERCHANT.toString());
		services.add(Constants.CNN_SUBTYPE.toString());
		services.add(Constants.BLOOM_FILTER.toString());
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(10000004L);
		data.setMemId(10222903L);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = new MeerkatTransaction();
		meerkatTransaction.setType("Other Withdrawals");
		meerkatTransaction.setSubType("Debit");
		meerkatTransaction.setMerchant("53 automotive");
		meerkatTransaction.setCountry("US");
		meerkatTransaction.setConfidenceScore("64.81");
		meerkatTransaction.setTransactionId(new BigInteger("2"));
		meerkatTransaction.setSourceMerchantId("53automotiveus");
		List<String> labels = new ArrayList<String>();
		labels.add("Automotive/Fuel");
		meerkatTransaction.setLabels(labels);
		meerkatTransaction.setSource("OTHER");
		CNNInfo debugInfo = new CNNInfo();
		meerkatTransaction.setDebugInfo(debugInfo);
		String [] s = {"0","1"};
		meerkatTransaction.setVendorNameVariation(s);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate");
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, headers, categorizationResponse);
		TransactionCategory category = CommonUtil.getTransactionCategory();
		when(categoryCache.getTransactionCategory(meerkatTransaction.getLabels(),false)).thenReturn(category);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);
		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse txnResponse=(EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(txnResponse.getSimpleDescription(), "53 automotive");
		}
	}
	

	@Test
	public void testMeerkatCategorizationForBankContainer() throws ClientProtocolException, IOException {
		CategorizationRequest request = CommonUtil.getCategorizationRequest();
		request.getTxns().get(0).setTransactionId(new BigInteger("5"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("Vodafone Mobile");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		EnrichedTransactionResponse txnResponse = CommonUtil.getEnrichedTransactionResponse();
		txnResponse.setGranularCategory("telephone/cellular services");
		txnResponse.setCategory("Telephone Services");
		mccTxnCatIdToResponseMap.put(new BigInteger("5"),txnResponse);
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = new ArrayList<String>();
		services.add(Constants.CNN_MERCHANT.toString());
		services.add(Constants.CNN_SUBTYPE.toString());
		services.add(Constants.BLOOM_FILTER.toString());
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(10000004L);
		data.setMemId(10222903L);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = new MeerkatTransaction();
		meerkatTransaction.setType("Payment");
		meerkatTransaction.setSubType("Recurring/Subscription Payment");
		meerkatTransaction.setMerchant("");
		meerkatTransaction.setCountry("US");
		meerkatTransaction.setConfidenceScore("100.00");
		meerkatTransaction.setTransactionId(new BigInteger("5"));
		meerkatTransaction.setSourceMerchantId("");
		List<String> labels = new ArrayList<String>();
		labels.add("Cable/Satellite/Telecom");
		meerkatTransaction.setLabels(labels);
		meerkatTransaction.setSource("OTHER");
		CNNInfo debugInfo = new CNNInfo();
		meerkatTransaction.setDebugInfo(debugInfo);
		String [] s = {"0","1"};
		meerkatTransaction.setVendorNameVariation(s);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate");
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, headers, categorizationResponse);
		//when(meerkatServiceImpl.processMeerkatRequest(request, headers, categorizationResponse)).thenReturn(meerkatResponses);
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);
		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getCategory(), "Telephone Services");
			Assert.assertEquals(enrichedResponse.getGranularCategory(), "telephone/cellular services");
		}
	}
	
	@Test
	public void testMeerkatCategorizationForOnlineServices() throws ClientProtocolException, IOException {
		CategorizationRequest request = CommonUtil.getCategorizationRequest();
		request.getTxns().get(0).setTransactionId(new BigInteger("3"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("real estate services");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		EnrichedTransactionResponse txnResponse = CommonUtil.getEnrichedTransactionResponse();
		txnResponse.setGranularCategory("real estate services");
		txnResponse.setCategory("Online Services");
		mccTxnCatIdToResponseMap.put(new BigInteger("3"),txnResponse);
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = new ArrayList<String>();
		services.add(Constants.CNN_MERCHANT.toString());
		services.add(Constants.CNN_SUBTYPE.toString());
		services.add(Constants.BLOOM_FILTER.toString());
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(10000004L);
		data.setMemId(10222903L);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = new MeerkatTransaction();
		meerkatTransaction.setType("Purchase");
		meerkatTransaction.setSubType("Purchase");
		meerkatTransaction.setMerchant("Real Estate Services");
		meerkatTransaction.setCountry("US");
		meerkatTransaction.setConfidenceScore("100.00");
		meerkatTransaction.setTransactionId(new BigInteger("3"));
		meerkatTransaction.setSourceMerchantId("");
		List<String> labels = new ArrayList<String>();
		labels.add("Services/Supplies");
		meerkatTransaction.setLabels(labels);
		meerkatTransaction.setSource("OTHER");
		CNNInfo debugInfo = new CNNInfo();
		meerkatTransaction.setDebugInfo(debugInfo);
		String [] s = {"0","1"};
		meerkatTransaction.setVendorNameVariation(s);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate");
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, headers, categorizationResponse);
		//when(meerkatServiceImpl.processMeerkatRequest(request, headers, categorizationResponse)).thenReturn(meerkatResponses);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);
		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getCategory(), "Online Services");
			Assert.assertEquals(enrichedResponse.getGranularCategory(), "real estate services");
			Assert.assertEquals(enrichedResponse.getSimpleDescription(), "Real Estate Services");
		}
	}
	
	@Test
	public void testMeerkatCategorizationForCategoryHobbies() throws ClientProtocolException, IOException {
		CategorizationRequest request = CommonUtil.getCategorizationRequest();
		request.getTxns().get(0).setTransactionId(new BigInteger("2"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("book store");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		EnrichedTransactionResponse txnResponse = CommonUtil.getEnrichedTransactionResponse();
		txnResponse.setGranularCategory("bookstores");
		txnResponse.setCategory("Hobbies");
		mccTxnCatIdToResponseMap.put(new BigInteger("2"),txnResponse);
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = new ArrayList<String>();
		services.add(Constants.CNN_MERCHANT.toString());
		services.add(Constants.CNN_SUBTYPE.toString());
		services.add(Constants.BLOOM_FILTER.toString());
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(10000004L);
		data.setMemId(10222903L);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = new MeerkatTransaction();
		meerkatTransaction.setType("Purchase");
		meerkatTransaction.setSubType("Purchase");
		meerkatTransaction.setMerchant("Book Store");
		meerkatTransaction.setCountry("US");
		meerkatTransaction.setConfidenceScore("100.00");
		meerkatTransaction.setTransactionId(new BigInteger("2"));
		meerkatTransaction.setSourceMerchantId("");
		List<String> labels = new ArrayList<String>();
		labels.add("Entertainment/Recreation");
		meerkatTransaction.setLabels(labels);
		meerkatTransaction.setSource("OTHER");
		CNNInfo debugInfo = new CNNInfo();
		meerkatTransaction.setDebugInfo(debugInfo);
		String [] s = {"0","1"};
		meerkatTransaction.setVendorNameVariation(s);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate");
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, headers, categorizationResponse);
		//when(meerkatServiceImpl.processMeerkatRequest(request, headers, categorizationResponse)).thenReturn(meerkatResponses);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);
		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getCategory(), "Hobbies");
			Assert.assertEquals(enrichedResponse.getGranularCategory(), "bookstores");
			Assert.assertEquals(enrichedResponse.getSimpleDescription(), "Book Store");
		}
	}
	
	@Test
	public void testMeerkatCategorizationForCategoryPayroll() throws ClientProtocolException, IOException {
		CategorizationRequest request = CommonUtil.getCategorizationRequest();
		request.getTxns().get(0).setTransactionId(new BigInteger("6"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("Payroll FE0X/10/201X WAGES HILTON HAULA");
		request.getTxns().get(0).setBaseType("credit");
		request.getTxns().get(0).setAccountType("SecuritiesBackedLineOfCredit");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		EnrichedTransactionResponse txnResponse = CommonUtil.getEnrichedTransactionResponse();
		txnResponse.setGranularCategory("regular payroll");
		txnResponse.setCategory("Paychecks/Salary");
		mccTxnCatIdToResponseMap.put(new BigInteger("6"),txnResponse);
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = new ArrayList<String>();
		services.add(Constants.CNN_MERCHANT.toString());
		services.add(Constants.CNN_SUBTYPE.toString());
		services.add(Constants.BLOOM_FILTER.toString());
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(10000004L);
		data.setMemId(10222903L);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = new MeerkatTransaction();
		meerkatTransaction.setType("Deposits/Credits");
		meerkatTransaction.setSubType("Direct Deposit/Salary");
		meerkatTransaction.setMerchant("");
		meerkatTransaction.setCountry("US");
		meerkatTransaction.setConfidenceScore("100.00");
		meerkatTransaction.setTransactionId(new BigInteger("6"));
		meerkatTransaction.setSourceMerchantId("");
		List<String> labels = new ArrayList<String>();
		labels.add("Salary/Regular Income");
		meerkatTransaction.setLabels(labels);
		meerkatTransaction.setSource("OTHER");
		CNNInfo debugInfo = new CNNInfo();
		meerkatTransaction.setDebugInfo(debugInfo);
		String [] s = {"0","1"};
		meerkatTransaction.setVendorNameVariation(s);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate");
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, headers, categorizationResponse);
		//when(meerkatServiceImpl.processMeerkatRequest(request, headers, categorizationResponse)).thenReturn(meerkatResponses);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);
		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getCategory(), "Paychecks/Salary");
			Assert.assertEquals(enrichedResponse.getGranularCategory(), "regular payroll");
		}
	}

	
	@Test
	public void testMeerkatCategorizationForCategoryAtmCashWidrwal() throws ClientProtocolException, IOException {
		CategorizationRequest request = CommonUtil.getCategorizationRequest();
		request.getTxns().get(0).setTransactionId(new BigInteger("9"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("0263 ATM WITHDRAWAL 408 6TH STREET WELLS NV--10/22/2014");
		request.getTxns().get(0).setBaseType("debit");
		request.getTxns().get(0).setAccountType("SecuritiesBackedLineOfCredit");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		EnrichedTransactionResponse txnResponse = CommonUtil.getEnrichedTransactionResponse();
		txnResponse.setGranularCategory("atm/cash withdrawals");
		txnResponse.setCategory("ATM/Cash Withdrawals");
		mccTxnCatIdToResponseMap.put(new BigInteger("9"),txnResponse);
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = new ArrayList<String>();
		services.add(Constants.CNN_MERCHANT.toString());
		services.add(Constants.CNN_SUBTYPE.toString());
		services.add(Constants.BLOOM_FILTER.toString());
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(10000004L);
		data.setMemId(10222903L);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = new MeerkatTransaction();
		meerkatTransaction.setType("Withdrawal");
		meerkatTransaction.setSubType("ATM/Cash Withdrawal");
		meerkatTransaction.setMerchant("");
		meerkatTransaction.setCountry("US");
		meerkatTransaction.setConfidenceScore("100.00");
		meerkatTransaction.setTransactionId(new BigInteger("9"));
		meerkatTransaction.setSourceMerchantId("");
		List<String> labels = new ArrayList<String>();
		labels.add("ATM/Cash Withdrawals");
		meerkatTransaction.setLabels(labels);
		meerkatTransaction.setSource("OTHER");
		CNNInfo debugInfo = new CNNInfo();
		meerkatTransaction.setDebugInfo(debugInfo);
		String [] s = {"0","1"};
		meerkatTransaction.setVendorNameVariation(s);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate");
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, headers, categorizationResponse);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		//when(meerkatServiceImpl.processMeerkatRequest(request, headers, categorizationResponse)).thenReturn(meerkatResponses);
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);
		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getCategory(), "ATM/Cash Withdrawals");
			Assert.assertEquals(enrichedResponse.getGranularCategory(), "atm/cash withdrawals");
		}
	}
	
	
	private void meerkatCategorizationWithCategoryChecks(String categoryName,String granularCategoryName)
			throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
		request.getTxns().get(0).setMccCode(null);
		request.getTxns().get(0).setTransactionId(new BigInteger("10"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("View Cheque CHQ#00240-050003XXX5");
		request.getTxns().get(0).setBaseType("debit");
		request.getTxns().get(0).setAccountType("SecuritiesBackedLineOfCredit");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		EnrichedTransactionResponse txnResponse = CommonUtil.getEnrichedTransactionResponse(categoryName,granularCategoryName);
		mccTxnCatIdToResponseMap.put(new BigInteger("10"),txnResponse);
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction(categoryName,granularCategoryName);
		meerkatTransaction.setType("Payment");
		meerkatTransaction.setSubType("Payment by Check");
		meerkatTransaction.setTransactionId(new BigInteger("10"));
		List<String> labels = new ArrayList<String>();
		labels.add("Check Payment");
		meerkatTransaction.setLabels(labels);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		//when(meerkatServiceImpl.processMeerkatRequest(request, headers, categorizationResponse)).thenReturn(meerkatResponses);
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getCategory(), categoryName);
			Assert.assertEquals(enrichedResponse.getGranularCategory(), granularCategoryName);
		}
	}
	
	@Test
	public void testMeerkatCategorizationWithCategoryChecks() throws ClientProtocolException, IOException {
		meerkatCategorizationWithCategoryChecks(TestConstants.CHECK_PAYMENT,TestConstants.CHECK_PAYMENT.toLowerCase());
	}
	
	@Test
	public void testMeerkatCategorizationWithGeneralMerchandise() throws ClientProtocolException, IOException {
		meerkatCategorizationWithCategoryGeneralMerchandise(TestConstants.GENERAL_MERCHANDISE,TestConstants.FURNITURE_DECOR);
	}

	private void meerkatCategorizationWithCategoryGeneralMerchandise(String categoryName, String granularCategoryName) throws JsonProcessingException {
			CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
			request.getTxns().get(0).setMccCode(null);
			request.getTxns().get(0).setTransactionId(new BigInteger("14"));
			request.getTxns().get(0).setAmount(new Double("1308.75001"));
			request.getTxns().get(0).setDescription("rent");
			request.getTxns().get(0).setBaseType("debit");
			request.getTxns().get(0).setAccountType("SecuritiesBackedLineOfCredit");
			Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
			EnrichedTransactionResponse txnResponse = CommonUtil.getEnrichedTransactionResponse(categoryName,granularCategoryName);
			mccTxnCatIdToResponseMap.put(new BigInteger("14"),txnResponse);
			CategorizationResponse categorizationResponse = new CategorizationResponse();
			YCategorizationStats categorizationStats= new YCategorizationStats();
			List<String> services = CommonUtil.getServicesForUSRegion();
			Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
			regionIdToServicesListMap.put(1L, services);
			when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
			List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
			MeerkatResponse meResponse = new MeerkatResponse();
			MeerkatData data = new MeerkatData();
			data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
			data.setMemId(TestConstants.DUMMY_MEM_ID);
			data.setServices(services);
			data.setRegion(1L);
			data.setVersion("1.0");
			List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
			MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction(categoryName,granularCategoryName);
			meerkatTransaction.setType("Payment");
			meerkatTransaction.setSubType("Payment");
			meerkatTransaction.setTransactionId(new BigInteger("14"));
			meerkatTransaction.setMerchant("Rent");
			List<String> labels = new ArrayList<String>();
			labels.add("Electronics/General Merchandise");
			meerkatTransaction.setLabels(labels);
			txns.add(meerkatTransaction);
			data.setTxns(txns);
			meResponse.setData(data);
			meerkatResponses.add(meResponse);
			doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
			when(caasConfigBean.getCaas()).thenReturn(getCaas());
			//when(meerkatServiceImpl.processMeerkatRequest(request, headers, categorizationResponse)).thenReturn(meerkatResponses);
			List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

			for(TransactionResponse txnRe : responses ) {
				EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
				Assert.assertEquals(enrichedResponse.getCategory(), categoryName);
				Assert.assertEquals(enrichedResponse.getGranularCategory(), granularCategoryName);
				Assert.assertEquals(enrichedResponse.getSimpleDescription(), "Rent");
			}
		
	}
	
	@Test
	public void testMeerkatCategorizationWithVendorNameVariation() throws ClientProtocolException, IOException {
		meerkatCategorizationWithVendorNameVariation(TestConstants.GENERAL_MERCHANDISE,TestConstants.FURNITURE_DECOR);
	}

	private void meerkatCategorizationWithVendorNameVariation(String categoryName, String granularCategoryName) throws JsonProcessingException {
			CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
			request.getTxns().get(0).setMccCode(null);
			request.getTxns().get(0).setTransactionId(new BigInteger("14"));
			request.getTxns().get(0).setAmount(new Double("1308.75001"));
			request.getTxns().get(0).setDescription("rent");
			request.getTxns().get(0).setBaseType("debit");
			request.getTxns().get(0).setAccountType("SecuritiesBackedLineOfCredit");
			Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
			EnrichedTransactionResponse txnResponse = CommonUtil.getEnrichedTransactionResponse(categoryName,granularCategoryName);
			mccTxnCatIdToResponseMap.put(new BigInteger("14"),txnResponse);
			CategorizationResponse categorizationResponse = new CategorizationResponse();
			YCategorizationStats categorizationStats= new YCategorizationStats();
			List<String> services = CommonUtil.getServicesForUSRegion();
			Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
			regionIdToServicesListMap.put(1L, services);
			when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
			List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
			MeerkatResponse meResponse = new MeerkatResponse();
			MeerkatData data = new MeerkatData();
			data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
			data.setMemId(TestConstants.DUMMY_MEM_ID);
			data.setServices(services);
			data.setRegion(1L);
			data.setVersion("1.0");
			List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
			MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction(categoryName,granularCategoryName);
			meerkatTransaction.setType("Payment");
			meerkatTransaction.setSubType("Payment");
			meerkatTransaction.setTransactionId(new BigInteger("14"));
			meerkatTransaction.setMerchant("Rent");
			List<String> labels = new ArrayList<String>();
			labels.add("Electronics/General Merchandise");
			meerkatTransaction.setLabels(labels);
			String [] s = {"Diamond Services","3","97"};
			meerkatTransaction.setVendorNameVariation(s);
			txns.add(meerkatTransaction);
			data.setTxns(txns);
			meResponse.setData(data);
			meerkatResponses.add(meResponse);
			when(caasConfigBean.getCaas()).thenReturn(getCaas());
			doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
			//when(meerkatServiceImpl.processMeerkatRequest(request, headers, categorizationResponse)).thenReturn(meerkatResponses);
			List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

			for(TransactionResponse txnRe : responses ) {
				EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
				Assert.assertEquals(enrichedResponse.getSimpleDescription(), "Diamond Services");
			}
		
	}
	
	@Test
	public void testMeerkatCategorizationWithVendorNameVariationTypeOne() throws ClientProtocolException, IOException {
		meerkatCategorizationWithVendorNameVariation(TestConstants.GENERAL_MERCHANDISE,TestConstants.FURNITURE_DECOR);
	}

	private void meerkatCategorizationWithVendorNameVariationTypeOne(String categoryName, String granularCategoryName) throws JsonProcessingException {
			CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
			request.getTxns().get(0).setMccCode(null);
			request.getTxns().get(0).setTransactionId(new BigInteger("14"));
			request.getTxns().get(0).setAmount(new Double("1308.75001"));
			request.getTxns().get(0).setDescription("rent");
			request.getTxns().get(0).setBaseType("debit");
			request.getTxns().get(0).setAccountType("SecuritiesBackedLineOfCredit");
			Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
			EnrichedTransactionResponse txnResponse = CommonUtil.getEnrichedTransactionResponse(categoryName,granularCategoryName);
			mccTxnCatIdToResponseMap.put(new BigInteger("14"),txnResponse);
			CategorizationResponse categorizationResponse = new CategorizationResponse();
			YCategorizationStats categorizationStats= new YCategorizationStats();
			List<String> services = CommonUtil.getServicesForUSRegion();
			Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
			regionIdToServicesListMap.put(1L, services);
			when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
			List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
			MeerkatResponse meResponse = new MeerkatResponse();
			MeerkatData data = new MeerkatData();
			data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
			data.setMemId(TestConstants.DUMMY_MEM_ID);
			data.setServices(services);
			data.setRegion(1L);
			data.setVersion("1.0");
			List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
			MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction(categoryName,granularCategoryName);
			meerkatTransaction.setType("Payment");
			meerkatTransaction.setSubType("Payment");
			meerkatTransaction.setTransactionId(new BigInteger("14"));
			meerkatTransaction.setMerchant("Rent");
			List<String> labels = new ArrayList<String>();
			labels.add("Electronics/General Merchandise");
			meerkatTransaction.setLabels(labels);
			String [] s = {"Diamond Services","1"};
			meerkatTransaction.setVendorNameVariation(s);
			txns.add(meerkatTransaction);
			data.setTxns(txns);
			meResponse.setData(data);
			meerkatResponses.add(meResponse);
			doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
			//when(meerkatServiceImpl.processMeerkatRequest(request, headers, categorizationResponse)).thenReturn(meerkatResponses);
			List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

			for(TransactionResponse txnRe : responses ) {
				EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
				Assert.assertEquals(enrichedResponse.getSimpleDescription(), "Rent");
				Assert.assertEquals(enrichedResponse.getVendorName(), null);
			}
		
	}
	
	@Test
	public void testMeerkatCategorizationWithSimpleDescriptionDisbled() throws ClientProtocolException, IOException {
		meerkatCategorizationWithSimpleDescriptionDisabled(TestConstants.GENERAL_MERCHANDISE,TestConstants.FURNITURE_DECOR);
	}

	private void meerkatCategorizationWithSimpleDescriptionDisabled(String categoryName, String granularCategoryName) throws JsonProcessingException {
			CategorizationRequest request = CommonUtil.getCateRequest(false, true, false);
			request.getTxns().get(0).setMccCode(null);
			request.getTxns().get(0).setTransactionId(new BigInteger("14"));
			request.getTxns().get(0).setAmount(new Double("1308.75001"));
			request.getTxns().get(0).setDescription("rent");
			request.getTxns().get(0).setBaseType("debit");
			request.getTxns().get(0).setAccountType("SecuritiesBackedLineOfCredit");
			Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
			EnrichedTransactionResponse txnResponse = CommonUtil.getEnrichedTransactionResponse(categoryName,granularCategoryName);
			mccTxnCatIdToResponseMap.put(new BigInteger("14"),txnResponse);
			CategorizationResponse categorizationResponse = new CategorizationResponse();
			YCategorizationStats categorizationStats= new YCategorizationStats();
			List<String> services = CommonUtil.getServicesForUSRegion();
			Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
			regionIdToServicesListMap.put(1L, services);
			when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
			List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
			MeerkatResponse meResponse = new MeerkatResponse();
			MeerkatData data = new MeerkatData();
			data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
			data.setMemId(TestConstants.DUMMY_MEM_ID);
			data.setServices(services);
			data.setRegion(1L);
			data.setVersion("1.0");
			List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
			MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction(categoryName,granularCategoryName);
			meerkatTransaction.setType("Payment");
			meerkatTransaction.setSubType("Payment");
			meerkatTransaction.setTransactionId(new BigInteger("14"));
			meerkatTransaction.setMerchant("Rent");
			List<String> labels = new ArrayList<String>();
			labels.add("Electronics/General Merchandise");
			meerkatTransaction.setLabels(labels);
			String [] s = {"Diamond Services","1"};
			meerkatTransaction.setVendorNameVariation(s);
			txns.add(meerkatTransaction);
			data.setTxns(txns);
			meResponse.setData(data);
			meerkatResponses.add(meResponse);
			when(caasConfigBean.getCaas()).thenReturn(getCaas());
			doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
			//when(meerkatServiceImpl.processMeerkatRequest(request, headers, categorizationResponse)).thenReturn(meerkatResponses);
			List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

			for(TransactionResponse txnRe : responses ) {
				EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
				Assert.assertEquals(enrichedResponse.getSimpleDescription(), null);
				Assert.assertEquals(enrichedResponse.getVendorName(), null);
			}
		
	}
	
	@Test
	public void testMeerkatCategorizationWithoutMappingCategoryWithTxnResponse() throws ClientProtocolException, IOException {
		meerkatCategorizationWithoutMappingCategoryWithTxnResponse(TestConstants.GENERAL_MERCHANDISE,TestConstants.FURNITURE_DECOR);
	}

	private void meerkatCategorizationWithoutMappingCategoryWithTxnResponse(String categoryName, String granularCategoryName) throws JsonProcessingException {
			CategorizationRequest request = CommonUtil.getCateRequest(true, true, true);
			request.getTxns().get(0).setMccCode(null);
			request.getTxns().get(0).setTransactionId(new BigInteger("14"));
			request.getTxns().get(0).setAmount(new Double("1308.75001"));
			request.getTxns().get(0).setDescription("rent");
			request.getTxns().get(0).setBaseType("debit");
			request.getTxns().get(0).setAccountType("SecuritiesBackedLineOfCredit");
			Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
			CategorizationResponse categorizationResponse = new CategorizationResponse();
			YCategorizationStats categorizationStats= new YCategorizationStats();
			List<String> services = CommonUtil.getServicesForUSRegion();
			Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
			regionIdToServicesListMap.put(1L, services);
			when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
			List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
			MeerkatResponse meResponse = new MeerkatResponse();
			MeerkatData data = new MeerkatData();
			data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
			data.setMemId(TestConstants.DUMMY_MEM_ID);
			data.setServices(services);
			data.setRegion(1L);
			data.setVersion("1.0");
			List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
			MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction(categoryName,granularCategoryName);
			meerkatTransaction.setType("Payment");
			meerkatTransaction.setSubType("Payment");
			meerkatTransaction.setTransactionId(new BigInteger("14"));
			meerkatTransaction.setMerchant("Rent");
			List<String> labels = new ArrayList<String>();
			labels.add("Electronics/General Merchandise");
			meerkatTransaction.setLabels(labels);
			String [] s = {"Diamond Services","1"};
			meerkatTransaction.setVendorNameVariation(s);
			txns.add(meerkatTransaction);
			data.setTxns(txns);
			meResponse.setData(data);
			meerkatResponses.add(meResponse);
			doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
			TransactionCategory txnCat= CommonUtil.getTransactionCategory(categoryName, categoryName);
			when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
			when(caasConfigBean.getCaas()).thenReturn(getCaas());
			List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

			for(TransactionResponse txnRe : responses ) {
				EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
				Assert.assertEquals(enrichedResponse.getSimpleDescription(), "Rent");
				Assert.assertEquals(enrichedResponse.getVendorName(), null);
			}
		
	}
	
	@Test
	public void testMeerkatCategorizationForGrannularCategory() throws ClientProtocolException, IOException {
		meerkatCategorizationForGrannularCategory(TestConstants.GENERAL_MERCHANDISE,TestConstants.FURNITURE_DECOR);
	}

	private void meerkatCategorizationForGrannularCategory(String categoryName, String granularCategoryName) throws JsonProcessingException {
			CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
			request.getTxns().get(0).setMccCode(null);
			request.getTxns().get(0).setTransactionId(new BigInteger("14"));
			request.getTxns().get(0).setAmount(new Double("1308.75001"));
			request.getTxns().get(0).setDescription("rent");
			request.getTxns().get(0).setBaseType("debit");
			request.getTxns().get(0).setAccountType("SecuritiesBackedLineOfCredit");
			Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
			CategorizationResponse categorizationResponse = new CategorizationResponse();
			YCategorizationStats categorizationStats= new YCategorizationStats();
			List<String> services = CommonUtil.getServicesForUSRegion();
			Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
			regionIdToServicesListMap.put(1L, services);
			when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
			List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
			MeerkatResponse meResponse = new MeerkatResponse();
			MeerkatData data = new MeerkatData();
			data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
			data.setMemId(TestConstants.DUMMY_MEM_ID);
			data.setServices(services);
			data.setRegion(1L);
			data.setVersion("1.0");
			List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
			MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction(categoryName,granularCategoryName);
			meerkatTransaction.setType("Payment");
			meerkatTransaction.setSubType("Payment");
			meerkatTransaction.setTransactionId(new BigInteger("14"));
			meerkatTransaction.setMerchant("Rent");
			List<String> labels = new ArrayList<String>();
			labels.add("Electronics/General Merchandise");
			meerkatTransaction.setLabels(labels);
			String [] s = {"Diamond Services","1"};
			meerkatTransaction.setVendorNameVariation(s);
			txns.add(meerkatTransaction);
			data.setTxns(txns);
			meResponse.setData(data);
			meerkatResponses.add(meResponse);
			doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
			TransactionCategory txnCat= CommonUtil.getTransactionCategory(categoryName, categoryName);
			when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
			GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
			granularCategoryMapping.setMasterCategoryName(categoryName);
			when(granularCategoryCache.getGranularCategory(Mockito.anyString(), 
					Mockito.anyBoolean(),Mockito.anyString())).thenReturn(granularCategoryMapping);
			when(caasConfigBean.getCaas()).thenReturn(getCaas());
			List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

			for(TransactionResponse txnRe : responses ) {
				EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
				Assert.assertEquals(enrichedResponse.getCategory(),categoryName);
			}
		
	}
	
	@Test
	public void testMeerkatCategorizationForLoanContainer() throws ClientProtocolException, IOException {
		meerkatCategorizationForLoanContainer(TestConstants.GENERAL_MERCHANDISE,TestConstants.FURNITURE_DECOR);
	}

	private void meerkatCategorizationForLoanContainer(String categoryName, String granularCategoryName) throws JsonProcessingException {
			CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
			request.setContainer("loans");
			request.getTxns().get(0).setMccCode(null);
			request.getTxns().get(0).setTransactionId(new BigInteger("14"));
			request.getTxns().get(0).setAmount(new Double("1308.75001"));
			request.getTxns().get(0).setDescription("rent");
			request.getTxns().get(0).setBaseType("debit");
			request.getTxns().get(0).setAccountType("SecuritiesBackedLineOfCredit");
			Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
			CategorizationResponse categorizationResponse = new CategorizationResponse();
			YCategorizationStats categorizationStats= new YCategorizationStats();
			List<String> services = CommonUtil.getServicesForUSRegion();
			Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
			regionIdToServicesListMap.put(1L, services);
			when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
			List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
			MeerkatResponse meResponse = new MeerkatResponse();
			MeerkatData data = new MeerkatData();
			data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
			data.setMemId(TestConstants.DUMMY_MEM_ID);
			data.setServices(services);
			data.setRegion(1L);
			data.setVersion("1.0");
			List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
			MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction(categoryName,granularCategoryName);
			meerkatTransaction.setType("Payment");
			meerkatTransaction.setSubType("Payment");
			meerkatTransaction.setTransactionId(new BigInteger("14"));
			meerkatTransaction.setMerchant("Rent");
			List<String> labels = new ArrayList<String>();
			labels.add("Electronics/General Merchandise");
			meerkatTransaction.setLabels(labels);
			String [] s = {"Diamond Services","1"};
			meerkatTransaction.setVendorNameVariation(s);
			txns.add(meerkatTransaction);
			data.setTxns(txns);
			meResponse.setData(data);
			meerkatResponses.add(meResponse);
			doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
			TransactionCategory txnCat= CommonUtil.getTransactionCategory(categoryName, categoryName);
			when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
			GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
			granularCategoryMapping.setMasterCategoryName(categoryName);
			when(granularCategoryCache.getGranularCategory(Mockito.anyString(), 
					Mockito.anyBoolean(),Mockito.anyString())).thenReturn(granularCategoryMapping);
			when(caasConfigBean.getCaas()).thenReturn(getCaas());
			List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

			for(TransactionResponse txnRe : responses ) {
				EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
				Assert.assertEquals(enrichedResponse.getCategory(),categoryName);
			}
		
	}
	
	@Test
	public void testMeerkatCategorizationForInvetsmentContainer() throws ClientProtocolException, IOException {
		meerkatCategorizationForInvestmentContainer(TestConstants.GENERAL_MERCHANDISE,TestConstants.FURNITURE_DECOR);
	}

	private void meerkatCategorizationForInvestmentContainer(String categoryName, String granularCategoryName) throws JsonProcessingException {
			CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
			request.setContainer("stocks");
			request.getTxns().get(0).setMccCode(null);
			request.getTxns().get(0).setTransactionId(new BigInteger("14"));
			request.getTxns().get(0).setAmount(new Double("1308.75001"));
			request.getTxns().get(0).setDescription("rent");
			request.getTxns().get(0).setBaseType("debit");
			request.getTxns().get(0).setAccountType("SecuritiesBackedLineOfCredit");
			Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
			CategorizationResponse categorizationResponse = new CategorizationResponse();
			YCategorizationStats categorizationStats= new YCategorizationStats();
			List<String> services = CommonUtil.getServicesForUSRegion();
			Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
			regionIdToServicesListMap.put(1L, services);
			when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
			List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
			MeerkatResponse meResponse = new MeerkatResponse();
			MeerkatData data = new MeerkatData();
			data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
			data.setMemId(TestConstants.DUMMY_MEM_ID);
			data.setServices(services);
			data.setRegion(1L);
			data.setVersion("1.0");
			List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
			MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction(categoryName,granularCategoryName);
			meerkatTransaction.setType("Payment");
			meerkatTransaction.setSubType("Payment");
			meerkatTransaction.setTransactionId(new BigInteger("14"));
			meerkatTransaction.setMerchant("Rent");
			List<String> labels = new ArrayList<String>();
			labels.add("Electronics/General Merchandise");
			meerkatTransaction.setLabels(labels);
			String [] s = {"Diamond Services","1"};
			meerkatTransaction.setVendorNameVariation(s);
			txns.add(meerkatTransaction);
			data.setTxns(txns);
			meResponse.setData(data);
			meerkatResponses.add(meResponse);
			doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
			TransactionCategory txnCat= CommonUtil.getTransactionCategory(categoryName, categoryName);
			when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
			GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
			granularCategoryMapping.setMasterCategoryName(categoryName);
			when(granularCategoryCache.getGranularCategory(Mockito.anyString(), 
					Mockito.anyBoolean(),Mockito.anyString())).thenReturn(granularCategoryMapping);
			when(caasConfigBean.getCaas()).thenReturn(getCaas());
			List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

			for(TransactionResponse txnRe : responses ) {
				EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
				Assert.assertEquals(enrichedResponse.getCategory(),categoryName);
			}
		
	}
	
	@Test
	public void meerkatCategorizationSmbForBank() throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
		request.setAccountClassification("smallBusiness");
		request.setSmallBusiness(true);
		request.getTxns().get(0).setMccCode(null);
		request.getTxns().get(0).setTransactionId(new BigInteger("14"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("Allegro Coffee refund");
		request.getTxns().get(0).setBaseType("credit");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction("Restaurants/Dining","Cafes/Coffee/Tea Houses");
		meerkatTransaction.setType("Refund");
		meerkatTransaction.setSubType("Refund");
		meerkatTransaction.setTransactionId(new BigInteger("14"));
		meerkatTransaction.setMerchant("Allegro Coffee");
		meerkatTransaction.setSmbCategory("Personal Expenses");
		meerkatTransaction.setIsBusinessRelated("No");
		List<String> labels = new ArrayList<String>();
		labels.add("Restaurants");
		meerkatTransaction.setLabels(labels);
		String [] s = {"allegro","c8","91"};
		meerkatTransaction.setVendorNameVariation(s);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		TransactionCategory txnCat= CommonUtil.getTransactionCategory("Restaurants/Dining", "Restaurants");
		when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
		GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
		granularCategoryMapping.setMasterCategoryName("Restaurants/Dining");
		when(granularCategoryCache.getGranularCategory(Mockito.anyString(), 
				Mockito.anyBoolean(),Mockito.anyString())).thenReturn(granularCategoryMapping);
		Map<String,Long> smbTwoTxnCategoryNameToIdMap = new HashMap<String,Long>();
		smbTwoTxnCategoryNameToIdMap.put("personal expenses", 6l);
		when(smbTwoTxnCategoryCache.getSmbTwoTxnCategoryNameToIdMap()).thenReturn(smbTwoTxnCategoryNameToIdMap);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getSmbTwoTxnCategoryId(),(Long)6L);
			Assert.assertEquals(enrichedResponse.getIsBusinessRelated(),(Long)0L);
		}
	
}
	
	@Test
	public void meerkatCategorizationSmbForCard() throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
		request.setContainer("credits");
		request.setAccountClassification("smallBusiness");
		request.setSmallBusiness(true);
		request.getTxns().get(0).setMccCode(null);
		request.getTxns().get(0).setTransactionId(new BigInteger("14"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("Allegro Coffee refund");
		request.getTxns().get(0).setBaseType("credit");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction("Restaurants/Dining","Cafes/Coffee/Tea Houses");
		meerkatTransaction.setType("Refund");
		meerkatTransaction.setSubType("Refund");
		meerkatTransaction.setTransactionId(new BigInteger("14"));
		meerkatTransaction.setMerchant("Allegro Coffee");
		meerkatTransaction.setSmbCategory("Personal Expenses");
		meerkatTransaction.setIsBusinessRelated("No");
		List<String> labels = new ArrayList<String>();
		labels.add("Restaurants");
		meerkatTransaction.setLabels(labels);
		String [] s = {"allegro","c8","91"};
		meerkatTransaction.setVendorNameVariation(s);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		TransactionCategory txnCat= CommonUtil.getTransactionCategory("Restaurants/Dining", "Restaurants");
		when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
		GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
		granularCategoryMapping.setMasterCategoryName("Restaurants/Dining");
		when(granularCategoryCache.getGranularCategory(Mockito.anyString(), 
				Mockito.anyBoolean(),Mockito.anyString())).thenReturn(granularCategoryMapping);
		Map<String,Long> smbTwoTxnCategoryNameToIdMap = new HashMap<String,Long>();
		smbTwoTxnCategoryNameToIdMap.put("personal expenses", 6l);
		when(smbTwoTxnCategoryCache.getSmbTwoTxnCategoryNameToIdMap()).thenReturn(smbTwoTxnCategoryNameToIdMap);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getSmbTwoTxnCategoryId(),(Long)6L);
			Assert.assertEquals(enrichedResponse.getIsBusinessRelated(),(Long)0L);
		}
	
}
	
	@Test
	public void meerkatCategorizationSmbTransfer() throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
		request.setAccountClassification("smallBusiness");
		request.setSmallBusiness(true);
		request.getTxns().get(0).setMccCode(null);
		request.getTxns().get(0).setTransactionId(new BigInteger("14"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("BankSMBTrans1");
		request.getTxns().get(0).setBaseType("credit");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction("Transfers","Money Transfer");
		meerkatTransaction.setType("Transfer");
		meerkatTransaction.setSubType("Transfer");
		meerkatTransaction.setTransactionId(new BigInteger("14"));
		meerkatTransaction.setSmbCategory("Transfers");
		meerkatTransaction.setIsBusinessRelated("Yes");
		List<String> labels = new ArrayList<String>();
		labels.add("Transfers");
		meerkatTransaction.setLabels(labels);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		TransactionCategory txnCat= CommonUtil.getTransactionCategory("Transfers", "");
		when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
		GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
		granularCategoryMapping.setMasterCategoryName("Transfers");
		when(granularCategoryCache.getGranularCategory(Mockito.anyString(), 
				Mockito.anyBoolean(),Mockito.anyString())).thenReturn(granularCategoryMapping);
		Map<String,Long> smbTwoTxnCategoryNameToIdMap = new HashMap<String,Long>();
		smbTwoTxnCategoryNameToIdMap.put("transfers", 9l);
		when(smbTwoTxnCategoryCache.getSmbTwoTxnCategoryNameToIdMap()).thenReturn(smbTwoTxnCategoryNameToIdMap);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getSmbTwoTxnCategoryId(),(Long)9L);
			Assert.assertEquals(enrichedResponse.getIsBusinessRelated(),(Long)1L);
		}
	
}
	
	@Test
	public void meerkatCategorizationSmbTransferForCard() throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
		request.setContainer("credits");
		request.setAccountClassification("smallBusiness");
		request.setSmallBusiness(true);
		request.getTxns().get(0).setMccCode(null);
		request.getTxns().get(0).setTransactionId(new BigInteger("14"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("BankSMBTrans1");
		request.getTxns().get(0).setBaseType("credit");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction("Transfers","Money Transfer");
		meerkatTransaction.setType("Transfer");
		meerkatTransaction.setSubType("Transfer");
		meerkatTransaction.setTransactionId(new BigInteger("14"));
		meerkatTransaction.setSmbCategory("Transfers");
		meerkatTransaction.setIsBusinessRelated("Yes");
		List<String> labels = new ArrayList<String>();
		labels.add("Transfers");
		meerkatTransaction.setLabels(labels);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		TransactionCategory txnCat= CommonUtil.getTransactionCategory("Transfers", "");
		when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
		GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
		granularCategoryMapping.setMasterCategoryName("Transfers");
		when(granularCategoryCache.getGranularCategory(Mockito.anyString(), 
				Mockito.anyBoolean(),Mockito.anyString())).thenReturn(granularCategoryMapping);
		Map<String,Long> smbTwoTxnCategoryNameToIdMap = new HashMap<String,Long>();
		smbTwoTxnCategoryNameToIdMap.put("transfers", 9l);
		when(smbTwoTxnCategoryCache.getSmbTwoTxnCategoryNameToIdMap()).thenReturn(smbTwoTxnCategoryNameToIdMap);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getSmbTwoTxnCategoryId(),(Long)9L);
			Assert.assertEquals(enrichedResponse.getIsBusinessRelated(),(Long)1L);
		}
	
}
	@Test
	public void testSmbTransferForGeneralExpenses() throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
		request.setContainer("credits");
		request.setAccountClassification("smallBusiness");
		request.setSmallBusiness(true);
		request.getTxns().get(0).setMccCode(null);
		request.getTxns().get(0).setTransactionId(new BigInteger("14"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("Debit Card Purchase XX/XX XX:XXp #XXXXUW HUSKYTEAMBKSTORE XXX-XXX-XXXX WA XXXXX");
		request.getTxns().get(0).setBaseType("debit");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction("Groceries","Supermarkets and Groceries");
		meerkatTransaction.setType("Purchase");
		meerkatTransaction.setSubType("Purchase");
		meerkatTransaction.setTransactionId(new BigInteger("14"));
		meerkatTransaction.setSmbCategory("General Expenses");
		meerkatTransaction.setIsBusinessRelated("Yes");
		List<String> labels = new ArrayList<String>();
		labels.add("Groceries");
		meerkatTransaction.setLabels(labels);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		TransactionCategory txnCat= CommonUtil.getTransactionCategory("Groceries", "");
		when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
		GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
		granularCategoryMapping.setMasterCategoryName("Groceries");
		when(granularCategoryCache.getGranularCategory(Mockito.anyString(), 
				Mockito.anyBoolean(),Mockito.anyString())).thenReturn(granularCategoryMapping);
		Map<String,Long> smbTwoTxnCategoryNameToIdMap = new HashMap<String,Long>();
		smbTwoTxnCategoryNameToIdMap.put("general expenses", 2l);
		when(smbTwoTxnCategoryCache.getSmbTwoTxnCategoryNameToIdMap()).thenReturn(smbTwoTxnCategoryNameToIdMap);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getSmbTwoTxnCategoryId(),(Long)2L);
			Assert.assertEquals(enrichedResponse.getIsBusinessRelated(),(Long)1L);
		}
	
}
	
	@Test
	public void testSmbTransferForOtherDeposits() throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
		request.setContainer("credits");
		request.setAccountClassification("smallBusiness");
		request.setSmallBusiness(true);
		request.getTxns().get(0).setMccCode(null);
		request.getTxns().get(0).setTransactionId(new BigInteger("14"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("ACH Electronic CreditPERSONAL CAPITAL DEPOSIT");
		request.getTxns().get(0).setBaseType("credit");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction("Deposits","Credits/Deposits");
		meerkatTransaction.setType("Purchase");
		meerkatTransaction.setSubType("Purchase");
		meerkatTransaction.setTransactionId(new BigInteger("14"));
		meerkatTransaction.setSmbCategory("Other Deposits");
		meerkatTransaction.setIsBusinessRelated("Yes");
		List<String> labels = new ArrayList<String>();
		labels.add("Deposits");
		meerkatTransaction.setLabels(labels);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		TransactionCategory txnCat= CommonUtil.getTransactionCategory("Deposits", "");
		when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
		GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
		granularCategoryMapping.setMasterCategoryName("Deposits");
		when(granularCategoryCache.getGranularCategory(Mockito.anyString(), 
				Mockito.anyBoolean(),Mockito.anyString())).thenReturn(granularCategoryMapping);
		Map<String,Long> smbTwoTxnCategoryNameToIdMap = new HashMap<String,Long>();
		smbTwoTxnCategoryNameToIdMap.put("other deposits", 5l);
		when(smbTwoTxnCategoryCache.getSmbTwoTxnCategoryNameToIdMap()).thenReturn(smbTwoTxnCategoryNameToIdMap);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getSmbTwoTxnCategoryId(),(Long)5L);
			Assert.assertEquals(enrichedResponse.getIsBusinessRelated(),(Long)1L);
		}
	
}
	
	@Test
	public void testSmbForSalesRevenue() throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
		request.setContainer("credits");
		request.setAccountClassification("smallBusiness");
		request.setSmallBusiness(true);
		request.getTxns().get(0).setMccCode(null);
		request.getTxns().get(0).setTransactionId(new BigInteger("14"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("salse revenue desc");
		request.getTxns().get(0).setBaseType("credit");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction("Other Income","Other Income");
		meerkatTransaction.setType("Deposits/Credits");
		meerkatTransaction.setSubType("Credit");
		meerkatTransaction.setTransactionId(new BigInteger("14"));
		meerkatTransaction.setSmbCategory("Sales Revenue");
		meerkatTransaction.setIsBusinessRelated("Yes");
		List<String> labels = new ArrayList<String>();
		labels.add("Other Income");
		meerkatTransaction.setLabels(labels);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		TransactionCategory txnCat= CommonUtil.getTransactionCategory("Other Income", "");
		when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
		GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
		granularCategoryMapping.setMasterCategoryName("Other Income");
		when(granularCategoryCache.getGranularCategory(Mockito.anyString(), 
				Mockito.anyBoolean(),Mockito.anyString())).thenReturn(granularCategoryMapping);
		Map<String,Long> smbTwoTxnCategoryNameToIdMap = new HashMap<String,Long>();
		smbTwoTxnCategoryNameToIdMap.put("sales revenue", 8l);
		when(smbTwoTxnCategoryCache.getSmbTwoTxnCategoryNameToIdMap()).thenReturn(smbTwoTxnCategoryNameToIdMap);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getSmbTwoTxnCategoryId(),(Long)8L);
			Assert.assertEquals(enrichedResponse.getIsBusinessRelated(),(Long)1L);
		}
	
}
	
	@Test
	public void testSmbForMarketingExpenses() throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
		request.setContainer("credits");
		request.setAccountClassification("smallBusiness");
		request.setSmallBusiness(true);
		request.getTxns().get(0).setMccCode(null);
		request.getTxns().get(0).setTransactionId(new BigInteger("14"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("salse revenue desc");
		request.getTxns().get(0).setBaseType("credit");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction("Other Income","Other Income");
		meerkatTransaction.setType("Deposits/Credits");
		meerkatTransaction.setSubType("Credit");
		meerkatTransaction.setTransactionId(new BigInteger("14"));
		meerkatTransaction.setSmbCategory("Marketing Expenses");
		meerkatTransaction.setIsBusinessRelated("Yes");
		List<String> labels = new ArrayList<String>();
		labels.add("Other Income");
		meerkatTransaction.setLabels(labels);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		TransactionCategory txnCat= CommonUtil.getTransactionCategory("Other Income", "");
		when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
		GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
		granularCategoryMapping.setMasterCategoryName("Other Income");
		when(granularCategoryCache.getGranularCategory(Mockito.anyString(), 
				Mockito.anyBoolean(),Mockito.anyString())).thenReturn(granularCategoryMapping);
		Map<String,Long> smbTwoTxnCategoryNameToIdMap = new HashMap<String,Long>();
		smbTwoTxnCategoryNameToIdMap.put("marketing expenses", 3l);
		when(smbTwoTxnCategoryCache.getSmbTwoTxnCategoryNameToIdMap()).thenReturn(smbTwoTxnCategoryNameToIdMap);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getSmbTwoTxnCategoryId(),(Long)3L);
			Assert.assertEquals(enrichedResponse.getIsBusinessRelated(),(Long)1L);
		}
	
}
	
	@Test
	public void testWagesPaidGranularCategory() throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
		request.setContainer("credits");
		request.setAccountClassification("smallBusiness");
		request.setSmallBusiness(true);
		request.getTxns().get(0).setMccCode(null);
		request.getTxns().get(0).setTransactionId(new BigInteger("14"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("Wages paid amount");
		request.getTxns().get(0).setBaseType("credit");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction("Paychecks/Salary","Wages Paid");
		meerkatTransaction.setType("Deposits/Credits");
		meerkatTransaction.setSubType("Direct Deposit/Salary");
		meerkatTransaction.setTransactionId(new BigInteger("14"));
		meerkatTransaction.setSmbCategory("Personal Income");
		meerkatTransaction.setIsBusinessRelated("No");
		List<String> labels = new ArrayList<String>();
		labels.add("Paychecks/Salary");
		meerkatTransaction.setLabels(labels);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		TransactionCategory txnCat= CommonUtil.getTransactionCategory("Paychecks/Salary", "");
		when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
		GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
		granularCategoryMapping.setMasterCategoryName("Paychecks/Salary");
		when(granularCategoryCache.getGranularCategory(Mockito.anyString(), 
				Mockito.anyBoolean(),Mockito.anyString())).thenReturn(granularCategoryMapping);
		Map<String,Long> smbTwoTxnCategoryNameToIdMap = new HashMap<String,Long>();
		smbTwoTxnCategoryNameToIdMap.put("personal income", 7l);
		when(smbTwoTxnCategoryCache.getSmbTwoTxnCategoryNameToIdMap()).thenReturn(smbTwoTxnCategoryNameToIdMap);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getSmbTwoTxnCategoryId(),(Long)7L);
			Assert.assertEquals(enrichedResponse.getIsBusinessRelated(),(Long)0L);
			Assert.assertEquals(enrichedResponse.getGranularCategory(),"Wages Paid");
			Assert.assertEquals(enrichedResponse.getCategory(),"Paychecks/Salary");
		}
	
}
	
	@Test
	public void testWagesExpensesGranularCategory() throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
		request.setContainer("credits");
		request.setAccountClassification("smallBusiness");
		request.setSmallBusiness(true);
		request.getTxns().get(0).setMccCode(null);
		request.getTxns().get(0).setTransactionId(new BigInteger("14"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("Wages paid amount");
		request.getTxns().get(0).setBaseType("debit");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction("Wages Paid","Wage Expenses");
		meerkatTransaction.setType("Payment");
		meerkatTransaction.setSubType("Payment");
		meerkatTransaction.setTransactionId(new BigInteger("14"));
		meerkatTransaction.setSmbCategory("General Expenses");
		meerkatTransaction.setIsBusinessRelated("Yes");
		List<String> labels = new ArrayList<String>();
		labels.add("Other Expenses");
		meerkatTransaction.setLabels(labels);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		TransactionCategory txnCat= CommonUtil.getTransactionCategory("Wages Paid", "");
		when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
		GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
		granularCategoryMapping.setMasterCategoryName("Wages Paid");
		when(granularCategoryCache.getGranularCategory(Mockito.anyString(), 
				Mockito.anyBoolean(),Mockito.anyString())).thenReturn(granularCategoryMapping);
		Map<String,Long> smbTwoTxnCategoryNameToIdMap = new HashMap<String,Long>();
		smbTwoTxnCategoryNameToIdMap.put("general expenses", 2l);
		when(smbTwoTxnCategoryCache.getSmbTwoTxnCategoryNameToIdMap()).thenReturn(smbTwoTxnCategoryNameToIdMap);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getSmbTwoTxnCategoryId(),(Long)2L);
			Assert.assertEquals(enrichedResponse.getIsBusinessRelated(),(Long)1L);
			Assert.assertEquals(enrichedResponse.getGranularCategory(),"Wage Expenses");
			Assert.assertEquals(enrichedResponse.getCategory(),"Wages Paid");
		}
	
}
	@Test
	public void testChildSupportPaymentGranularCategory() throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
		request.setContainer("credits");
		request.setAccountClassification("smallBusiness");
		request.setSmallBusiness(true);
		request.getTxns().get(0).setMccCode(null);
		request.getTxns().get(0).setTransactionId(new BigInteger("14"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("Child support amount");
		request.getTxns().get(0).setBaseType("debit");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction("Child/Dependent Expenses","Child Support Payments");
		meerkatTransaction.setType("Payment");
		meerkatTransaction.setSubType("Child Support");
		meerkatTransaction.setTransactionId(new BigInteger("14"));
		meerkatTransaction.setSmbCategory("Personal Expenses");
		meerkatTransaction.setIsBusinessRelated("No");
		List<String> labels = new ArrayList<String>();
		labels.add("Personal/Family");
		meerkatTransaction.setLabels(labels);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		TransactionCategory txnCat= CommonUtil.getTransactionCategory("Child/Dependent Expenses", "");
		when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
		GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
		granularCategoryMapping.setMasterCategoryName("Child/Dependent Expenses");
		when(granularCategoryCache.getGranularCategory(Mockito.anyString(), 
				Mockito.anyBoolean(),Mockito.anyString())).thenReturn(granularCategoryMapping);
		Map<String,Long> smbTwoTxnCategoryNameToIdMap = new HashMap<String,Long>();
		smbTwoTxnCategoryNameToIdMap.put("personal expenses", 6l);
		when(smbTwoTxnCategoryCache.getSmbTwoTxnCategoryNameToIdMap()).thenReturn(smbTwoTxnCategoryNameToIdMap);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getSmbTwoTxnCategoryId(),(Long)6L);
			Assert.assertEquals(enrichedResponse.getIsBusinessRelated(),(Long)0L);
			Assert.assertEquals(enrichedResponse.getGranularCategory(),"Child Support Payments");
			Assert.assertEquals(enrichedResponse.getCategory(),"Child/Dependent Expenses");
		}
	
}
	
	@Test
	public void testChildSupportGranularCategory() throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
		request.setContainer("credits");
		request.setAccountClassification("smallBusiness");
		request.setSmallBusiness(true);
		request.getTxns().get(0).setMccCode(null);
		request.getTxns().get(0).setTransactionId(new BigInteger("14"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("Child support amount");
		request.getTxns().get(0).setBaseType("credit");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction("Child/Dependent Expenses","Child Support");
		meerkatTransaction.setType("Payment");
		meerkatTransaction.setSubType("Child Support");
		meerkatTransaction.setTransactionId(new BigInteger("14"));
		meerkatTransaction.setSmbCategory("Personal Income");
		meerkatTransaction.setIsBusinessRelated("No");
		List<String> labels = new ArrayList<String>();
		labels.add("Personal/Family");
		meerkatTransaction.setLabels(labels);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		TransactionCategory txnCat= CommonUtil.getTransactionCategory("Child/Dependent Expenses", "");
		when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
		GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
		granularCategoryMapping.setMasterCategoryName("Child/Dependent Expenses");
		when(granularCategoryCache.getGranularCategory(Mockito.anyString(), 
				Mockito.anyBoolean(),Mockito.anyString())).thenReturn(granularCategoryMapping);
		Map<String,Long> smbTwoTxnCategoryNameToIdMap = new HashMap<String,Long>();
		smbTwoTxnCategoryNameToIdMap.put("personal income", 7l);
		when(smbTwoTxnCategoryCache.getSmbTwoTxnCategoryNameToIdMap()).thenReturn(smbTwoTxnCategoryNameToIdMap);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getSmbTwoTxnCategoryId(),(Long)7L);
			Assert.assertEquals(enrichedResponse.getIsBusinessRelated(),(Long)0L);
			Assert.assertEquals(enrichedResponse.getGranularCategory(),"Child Support");
			Assert.assertEquals(enrichedResponse.getCategory(),"Child/Dependent Expenses");
		}
	
}
	
	@Test
	public void meerkatCategorizationSmbTransferForCorporateAccount() throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
		request.setContainer("credits");
		request.setAccountClassification("corporate");
		request.setSmallBusiness(true);
		request.getTxns().get(0).setMccCode(null);
		request.getTxns().get(0).setTransactionId(new BigInteger("14"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("BankSMBTrans1");
		request.getTxns().get(0).setBaseType("credit");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction("Transfers","Money Transfer");
		meerkatTransaction.setType("Transfer");
		meerkatTransaction.setSubType("Transfer");
		meerkatTransaction.setTransactionId(new BigInteger("14"));
		meerkatTransaction.setSmbCategory("Transfers");
		meerkatTransaction.setIsBusinessRelated("Yes");
		List<String> labels = new ArrayList<String>();
		labels.add("Transfers");
		meerkatTransaction.setLabels(labels);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		TransactionCategory txnCat= CommonUtil.getTransactionCategory("Transfers", "");
		when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
		GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
		granularCategoryMapping.setMasterCategoryName("Transfers");
		when(granularCategoryCache.getGranularCategory(Mockito.anyString(), 
				Mockito.anyBoolean(),Mockito.anyString())).thenReturn(granularCategoryMapping);
		Map<String,Long> smbTwoTxnCategoryNameToIdMap = new HashMap<String,Long>();
		smbTwoTxnCategoryNameToIdMap.put("transfers", 9l);
		when(smbTwoTxnCategoryCache.getSmbTwoTxnCategoryNameToIdMap()).thenReturn(smbTwoTxnCategoryNameToIdMap);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getSmbTwoTxnCategoryId(),(Long)9L);
			Assert.assertEquals(enrichedResponse.getIsBusinessRelated(),(Long)1L);
		}
	
}
	@Test
	public void testSmbForCorporateAccountWithSmallBusinessFlagFalse() throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
		request.setContainer("credits");
		request.setAccountClassification("corporate");
		request.setSmallBusiness(false);
		request.getTxns().get(0).setMccCode(null);
		request.getTxns().get(0).setTransactionId(new BigInteger("14"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("BankSMBTrans1");
		request.getTxns().get(0).setBaseType("credit");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction("Transfers","Money Transfer");
		meerkatTransaction.setType("Transfer");
		meerkatTransaction.setSubType("Transfer");
		meerkatTransaction.setTransactionId(new BigInteger("14"));
		List<String> labels = new ArrayList<String>();
		labels.add("Transfers");
		meerkatTransaction.setLabels(labels);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		TransactionCategory txnCat= CommonUtil.getTransactionCategory("Transfers", "");
		when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
		GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
		granularCategoryMapping.setMasterCategoryName("Transfers");
		when(granularCategoryCache.getGranularCategory(Mockito.anyString(), 
				Mockito.anyBoolean(),Mockito.anyString())).thenReturn(granularCategoryMapping);
		Map<String,Long> smbTwoTxnCategoryNameToIdMap = new HashMap<String,Long>();
		smbTwoTxnCategoryNameToIdMap.put("transfers", 9l);
		when(smbTwoTxnCategoryCache.getSmbTwoTxnCategoryNameToIdMap()).thenReturn(smbTwoTxnCategoryNameToIdMap);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getSmbTwoTxnCategoryId(),null);
			Assert.assertEquals(enrichedResponse.getIsBusinessRelated(),null);
		}
	
}
	@Test
	public void testSmbForOtherAccount() throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
		request.setContainer("credits");
		request.setAccountClassification("other");
		request.setSmallBusiness(false);
		request.getTxns().get(0).setMccCode(null);
		request.getTxns().get(0).setTransactionId(new BigInteger("14"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("BankSMBTrans1");
		request.getTxns().get(0).setBaseType("credit");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction("Transfers","Money Transfer");
		meerkatTransaction.setType("Transfer");
		meerkatTransaction.setSubType("Transfer");
		meerkatTransaction.setTransactionId(new BigInteger("14"));
		List<String> labels = new ArrayList<String>();
		labels.add("Transfers");
		meerkatTransaction.setLabels(labels);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		TransactionCategory txnCat= CommonUtil.getTransactionCategory("Transfers", "");
		when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
		GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
		granularCategoryMapping.setMasterCategoryName("Transfers");
		when(granularCategoryCache.getGranularCategory(Mockito.anyString(), 
				Mockito.anyBoolean(),Mockito.anyString())).thenReturn(granularCategoryMapping);
		Map<String,Long> smbTwoTxnCategoryNameToIdMap = new HashMap<String,Long>();
		smbTwoTxnCategoryNameToIdMap.put("transfers", 9l);
		when(smbTwoTxnCategoryCache.getSmbTwoTxnCategoryNameToIdMap()).thenReturn(smbTwoTxnCategoryNameToIdMap);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getSmbTwoTxnCategoryId(),null);
			Assert.assertEquals(enrichedResponse.getIsBusinessRelated(),null);
		}
	
}
	
	@Test
	public void testSmbForAddOnCardAccount() throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
		request.setContainer("credits");
		request.setAccountClassification("addOnCard");
		request.setSmallBusiness(false);
		request.getTxns().get(0).setMccCode(null);
		request.getTxns().get(0).setTransactionId(new BigInteger("14"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("BankSMBTrans1");
		request.getTxns().get(0).setBaseType("credit");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction("Transfers","Money Transfer");
		meerkatTransaction.setType("Transfer");
		meerkatTransaction.setSubType("Transfer");
		meerkatTransaction.setTransactionId(new BigInteger("14"));
		List<String> labels = new ArrayList<String>();
		labels.add("Transfers");
		meerkatTransaction.setLabels(labels);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		TransactionCategory txnCat= CommonUtil.getTransactionCategory("Transfers", "");
		when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
		GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
		granularCategoryMapping.setMasterCategoryName("Transfers");
		when(granularCategoryCache.getGranularCategory(Mockito.anyString(), 
				Mockito.anyBoolean(),Mockito.anyString())).thenReturn(granularCategoryMapping);
		Map<String,Long> smbTwoTxnCategoryNameToIdMap = new HashMap<String,Long>();
		smbTwoTxnCategoryNameToIdMap.put("transfers", 9l);
		when(smbTwoTxnCategoryCache.getSmbTwoTxnCategoryNameToIdMap()).thenReturn(smbTwoTxnCategoryNameToIdMap);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getSmbTwoTxnCategoryId(),null);
			Assert.assertEquals(enrichedResponse.getIsBusinessRelated(),null);
		}
	
}
	@Test
	public void testSmbForTrustAccount() throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
		request.setContainer("credits");
		request.setAccountClassification("trust");
		request.setSmallBusiness(false);
		request.getTxns().get(0).setMccCode(null);
		request.getTxns().get(0).setTransactionId(new BigInteger("14"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("BankSMBTrans1");
		request.getTxns().get(0).setBaseType("credit");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction("Transfers","Money Transfer");
		meerkatTransaction.setType("Transfer");
		meerkatTransaction.setSubType("Transfer");
		meerkatTransaction.setTransactionId(new BigInteger("14"));
		List<String> labels = new ArrayList<String>();
		labels.add("Transfers");
		meerkatTransaction.setLabels(labels);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		TransactionCategory txnCat= CommonUtil.getTransactionCategory("Transfers", "");
		when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
		GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
		granularCategoryMapping.setMasterCategoryName("Transfers");
		when(granularCategoryCache.getGranularCategory(Mockito.anyString(), 
				Mockito.anyBoolean(),Mockito.anyString())).thenReturn(granularCategoryMapping);
		Map<String,Long> smbTwoTxnCategoryNameToIdMap = new HashMap<String,Long>();
		smbTwoTxnCategoryNameToIdMap.put("transfers", 9l);
		when(smbTwoTxnCategoryCache.getSmbTwoTxnCategoryNameToIdMap()).thenReturn(smbTwoTxnCategoryNameToIdMap);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getSmbTwoTxnCategoryId(),null);
			Assert.assertEquals(enrichedResponse.getIsBusinessRelated(),null);
		}
	
}
	@Test
	public void testSmbForOtherRegion() throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
		request.setContainer("credits");
		request.setAccountClassification("smallBusiness");
		request.setSmallBusiness(false);
		request.getTxns().get(0).setMccCode(null);
		request.getTxns().get(0).setTransactionId(new BigInteger("14"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("BankSMBTrans1");
		request.getTxns().get(0).setBaseType("credit");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(2L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction("Transfers","Money Transfer");
		meerkatTransaction.setType("Transfer");
		meerkatTransaction.setSubType("Transfer");
		meerkatTransaction.setTransactionId(new BigInteger("14"));
		List<String> labels = new ArrayList<String>();
		labels.add("Transfers");
		meerkatTransaction.setLabels(labels);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		TransactionCategory txnCat= CommonUtil.getTransactionCategory("Transfers", "");
		when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
		GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
		granularCategoryMapping.setMasterCategoryName("Transfers");
		when(granularCategoryCache.getGranularCategory(Mockito.anyString(), 
				Mockito.anyBoolean(),Mockito.anyString())).thenReturn(granularCategoryMapping);
		Map<String,Long> smbTwoTxnCategoryNameToIdMap = new HashMap<String,Long>();
		smbTwoTxnCategoryNameToIdMap.put("transfers", 9l);
		when(smbTwoTxnCategoryCache.getSmbTwoTxnCategoryNameToIdMap()).thenReturn(smbTwoTxnCategoryNameToIdMap);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getSmbTwoTxnCategoryId(),null);
			Assert.assertEquals(enrichedResponse.getIsBusinessRelated(),null);
		}
	
}
	
	@Test
	public void meerkatCategorizationSmbTransferWithPersonalClassification() throws JsonProcessingException {
		CategorizationRequest request = CommonUtil.getCateRequest(false, true, true);
		request.setContainer("credits");
		request.setAccountClassification("personal");
		request.setSmallBusiness(false);
		request.getTxns().get(0).setMccCode(null);
		request.getTxns().get(0).setTransactionId(new BigInteger("14"));
		request.getTxns().get(0).setAmount(new Double("1308.75001"));
		request.getTxns().get(0).setDescription("BankSMBTrans1");
		request.getTxns().get(0).setBaseType("credit");
		Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap = new HashMap<BigInteger, EnrichedTransactionResponse>();
		CategorizationResponse categorizationResponse = new CategorizationResponse();
		YCategorizationStats categorizationStats= new YCategorizationStats();
		List<String> services = CommonUtil.getServicesForUSRegion();
		Map<Long, List<String>> regionIdToServicesListMap = new HashMap <Long, List<String>> ();
		regionIdToServicesListMap.put(1L, services);
		when(regionCache.getRegionIdToServicesListMap()).thenReturn(regionIdToServicesListMap);	
		List<MeerkatResponse> meerkatResponses = new ArrayList<MeerkatResponse>();
		MeerkatResponse meResponse = new MeerkatResponse();
		MeerkatData data = new MeerkatData();
		data.setCobrandId(TestConstants.DUMMY_COBRAND_ID);
		data.setMemId(TestConstants.DUMMY_MEM_ID);
		data.setServices(services);
		data.setRegion(1L);
		data.setVersion("1.0");
		List<MeerkatTransaction> txns = new ArrayList<MeerkatTransaction>();
		MeerkatTransaction meerkatTransaction = CommonUtil.getMeerkatTransaction("Transfers","Money Transfer");
		meerkatTransaction.setType("Transfer");
		meerkatTransaction.setSubType("Transfer");
		meerkatTransaction.setTransactionId(new BigInteger("14"));
		List<String> labels = new ArrayList<String>();
		labels.add("Transfers");
		meerkatTransaction.setLabels(labels);
		txns.add(meerkatTransaction);
		data.setTxns(txns);
		meResponse.setData(data);
		meerkatResponses.add(meResponse);
		doReturn(meerkatResponses).when(meerkatServiceImpl).processMeerkatRequest(request, CommonUtil.gethttpHeaders(), categorizationResponse);
		TransactionCategory txnCat= CommonUtil.getTransactionCategory("Transfers", "");
		when(categoryCache.getTransactionCategory(Mockito.anyList(),Mockito.anyBoolean())).thenReturn(txnCat);
		GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
		granularCategoryMapping.setMasterCategoryName("Transfers");
		when(granularCategoryCache.getGranularCategory(Mockito.anyString(), 
				Mockito.anyBoolean(),Mockito.anyString())).thenReturn(granularCategoryMapping);
		Map<String,Long> smbTwoTxnCategoryNameToIdMap = new HashMap<String,Long>();
		smbTwoTxnCategoryNameToIdMap.put("transfers", 9l);
		when(smbTwoTxnCategoryCache.getSmbTwoTxnCategoryNameToIdMap()).thenReturn(smbTwoTxnCategoryNameToIdMap);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		List<TransactionResponse> responses = meerkatServiceImpl.categorise(request, categorizationResponse, mccTxnCatIdToResponseMap, categorizationStats);

		for(TransactionResponse txnRe : responses ) {
			EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) txnRe;
			Assert.assertEquals(enrichedResponse.getSmbTwoTxnCategoryId(),null);
			Assert.assertEquals(enrichedResponse.getIsBusinessRelated(),null);
		}
	
}
	
	
 
}
