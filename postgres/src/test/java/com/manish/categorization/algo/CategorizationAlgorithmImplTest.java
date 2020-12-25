package com.manish.categorization.algo;

import static org.mockito.Mockito.when;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.manish.categorization.algo.CategorizationAlgorithmImpl;
import com.manish.categorization.algo.legacy.LegacyCategoisationService;
import com.manish.categorization.algo.mcc.MCCCategorizationService;
import com.manish.categorization.algo.tde.MeerkatService;
import com.manish.categorization.db.GranularCategoryCache;
import com.manish.categorization.db.HlMasterCategoryMappingCache;
import com.manish.categorization.db.TransactionCategory;
import com.manish.categorization.db.TransactionCategoryCache;
import com.manish.categorization.db.TransactionClassificationCache;
import com.manish.categorization.rest.AbstractTest;
import com.manish.categorization.rest.dto.CategorizationRequest;
import com.manish.categorization.rest.dto.CategorizationResponse;
import com.manish.categorization.rest.dto.EnrichedTransactionResponse;
import com.manish.categorization.rest.dto.TransactionRequest;
import com.manish.categorization.rest.dto.TransactionResponse;
import com.manish.categorization.sdp.config.Caas;
import com.manish.categorization.sdp.config.CaasConfigBean;
import com.manish.categorization.util.CommonUtil;
import com.manish.categorization.util.YCategorizationStats;
import com.yodlee.simpledescservice.service.SimpleDescriptionService;

import junit.framework.Assert;

public class CategorizationAlgorithmImplTest  extends AbstractTest {	
	
	
	
	@Mock
	MeerkatService meerkatService;
	
	SimpleDescriptionService simpleDescriptionService = null;
	
	@Mock
	LegacyCategoisationService legacyService;

	@Mock
	TransactionCategoryCache categoryCache;
	
	@Mock
	MCCCategorizationService mccCategorizationService;
	
	@InjectMocks
	CategorizationAlgorithmImpl categorizationAlgorithmImpl;
	
	@Mock
	GranularCategoryCache granularCategoryCache;
	
	@Mock
	HlMasterCategoryMappingCache hlMasterCategoryMappingCache;
	
	@Mock
	TransactionClassificationCache transactionClassificationCache;
	
	@Mock
	CaasConfigBean caasConfigBean;
	
	@Override
	@Before
	public void setUp() {
      super.setUp();
      MockitoAnnotations.initMocks(this);
      /*ClassLoader classLoader = getClass().getClassLoader();
      File file = new File(classLoader.getResource("sdpConfig.json").getFile());
      System.setProperty("com.yodlee.sdp.configJsonPath", file.getAbsolutePath());*/
	}	
	
	@Test
	public void testApplyCategorisationWithNullConfig() {
		
		CategorizationRequest catRequest = CommonUtil.getCategorizationRequest();
		YCategorizationStats categorizationStats = new YCategorizationStats();
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catRequest, categorizationStats);
		Assert.assertEquals(categorizationResponse.getTxns(), new ArrayList<TransactionResponse>());
	}
	
	@Test
	public void testApplyCategorisation() {
		
		CategorizationRequest catRequest = CommonUtil.getCategorizationRequest();
		catRequest.setAccountType("Cash Management Account");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		List<TransactionResponse> meerkatResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse txnResponse= new TransactionResponse();
		EnrichedTransactionResponse enrichedTransactionResponse = new EnrichedTransactionResponse();
		enrichedTransactionResponse.setSimpleDescription("Citibank Transfer");
		txnResponse.setTransactionId(new BigInteger("1"));
		txnResponse.setCategorisationSource("MEERKAT");
		txnResponse.setCategorisationSourceId("24");
		txnResponse.setCategory("Transfers");
		txnResponse.setMerchant("Citibank");
		txnResponse = enrichedTransactionResponse;
		meerkatResponseList.add(txnResponse);
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catRequest.getMemId());
		response.setCobrandId(catRequest.getCobrandId());
		when(meerkatService.categorise(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyMap(), Mockito.anyObject())).thenReturn(meerkatResponseList);
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catRequest, categorizationStats);
	
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getSimpleDescription(), "Citibank Transfer");
		}
	}
	@Test
	public void testCategorisationForCard() {
		
		CategorizationRequest catRequest = CommonUtil.getCategorizationRequest();
		catRequest.setContainer("credits");
		catRequest.setAccountType("Cash Management Account");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		List<TransactionResponse> meerkatResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse txnResponse= new TransactionResponse();
		EnrichedTransactionResponse enrichedTransactionResponse = new EnrichedTransactionResponse();
		enrichedTransactionResponse.setSimpleDescription("Citibank Transfer");
		txnResponse.setTransactionId(new BigInteger("1"));
		txnResponse.setCategorisationSource("MEERKAT");
		txnResponse.setCategorisationSourceId("24");
		txnResponse.setCategory("Transfers");
		txnResponse.setMerchant("Citibank");
		txnResponse = enrichedTransactionResponse;
		meerkatResponseList.add(txnResponse);
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catRequest.getMemId());
		response.setCobrandId(catRequest.getCobrandId());
		when(meerkatService.categorise(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyMap(), Mockito.anyObject())).thenReturn(meerkatResponseList);
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catRequest, categorizationStats);
	
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getSimpleDescription(), "Citibank Transfer");
		}
	}
	//CategorizationAlgorithmImplTest.testCategorisationForLoan:156 » NullPointer
	public void testCategorisationForLoan() {
		
		CategorizationRequest catRequest = CommonUtil.getCategorizationRequest();
		catRequest.setContainer("loans");
		catRequest.setAccountType("HomeEquityLineOfCredit");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		List<TransactionResponse> meerkatResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse txnResponse= new TransactionResponse();
		EnrichedTransactionResponse enrichedTransactionResponse = new EnrichedTransactionResponse();
		enrichedTransactionResponse.setSimpleDescription("Citibank Transfer");
		txnResponse.setTransactionId(new BigInteger("1"));
		txnResponse.setCategorisationSource("MEERKAT");
		txnResponse.setCategorisationSourceId("24");
		txnResponse.setCategory("Transfers");
		txnResponse.setMerchant("Citibank");
		txnResponse = enrichedTransactionResponse;
		meerkatResponseList.add(txnResponse);
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catRequest.getMemId());
		response.setCobrandId(catRequest.getCobrandId());
		when(meerkatService.categorise(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyMap(), Mockito.anyObject())).thenReturn(meerkatResponseList);
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catRequest, categorizationStats);
	
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getSimpleDescription(), "Citibank Transfer");
		}
	}
	//  CategorizationAlgorithmImplTest.testCategorisationForInvestment:185 » NullPointer
	public void testCategorisationForInvestment() {
		
		CategorizationRequest catRequest = CommonUtil.getCategorizationRequest();
		catRequest.setContainer("stocks");
		catRequest.setAccountType("investmentClub");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		List<TransactionResponse> meerkatResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse txnResponse= new TransactionResponse();
		EnrichedTransactionResponse enrichedTransactionResponse = new EnrichedTransactionResponse();
		enrichedTransactionResponse.setSimpleDescription("Citibank Transfer");
		txnResponse.setTransactionId(new BigInteger("1"));
		txnResponse.setCategorisationSource("MEERKAT");
		txnResponse.setCategorisationSourceId("24");
		txnResponse.setCategory("Transfers");
		txnResponse.setMerchant("Citibank");
		txnResponse = enrichedTransactionResponse;
		meerkatResponseList.add(txnResponse);
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catRequest.getMemId());
		response.setCobrandId(catRequest.getCobrandId());
		when(meerkatService.categorise(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyMap(), Mockito.anyObject())).thenReturn(meerkatResponseList);
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catRequest, categorizationStats);
	
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getSimpleDescription(), "Citibank Transfer");
		}
	}
	
	@Test
	public void testApplyCategorisationForPaymentCategory() {
		
		CategorizationRequest catRequest = CommonUtil.getCategorizationRequest();
		catRequest.setAccountType("Cash Management Account");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		List<TransactionResponse> meerkatResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse txnResponse= new TransactionResponse();
		EnrichedTransactionResponse enrichedTransactionResponse = new EnrichedTransactionResponse();
		enrichedTransactionResponse.setSimpleDescription("Citibank Payment");
		txnResponse.setTransactionId(new BigInteger("1"));
		txnResponse.setCategorisationSource("MEERKAT");
		txnResponse.setCategorisationSourceId("24");
		txnResponse.setCategory("Payment");
		txnResponse.setMerchant("Citibank");
		txnResponse = enrichedTransactionResponse;
		meerkatResponseList.add(txnResponse);
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catRequest.getMemId());
		response.setCobrandId(catRequest.getCobrandId());
		when(meerkatService.categorise(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyMap(), Mockito.anyObject())).thenReturn(meerkatResponseList);
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catRequest, categorizationStats);
	
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getSimpleDescription(), "Citibank Payment");
		}
	}
	
	@Test
	public void testApplyCategorisationWithoutMerchant() {
		
		CategorizationRequest catRequest = CommonUtil.getCategorizationRequest();
		for(TransactionRequest txnRequest : catRequest.getTxns() ) {
			txnRequest.setTransactionId(new BigInteger("1"));
			txnRequest.setDescription("BUSINESS TO BUSINESS ACH ONLINE PAYROLL PAYROLL XXXXX1 XXXXX87 LAXA AND KRAUSE *ENTER");
		}
		catRequest.setAccountType("Cash Management Account");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		List<TransactionResponse> meerkatResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse txnResponse= new TransactionResponse();
		EnrichedTransactionResponse enrichedTransactionResponse = new EnrichedTransactionResponse();
		enrichedTransactionResponse.setMeerkatSubType("Debit");
		enrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		enrichedTransactionResponse.setSimpleDescription("BUSINESS TO BUSINESS ACH ONLINE PAYROLL PAYROLL XX1 XX87 LAXA AND KRAUSE *ENTER");
		txnResponse.setTransactionId(new BigInteger("1"));
		txnResponse.setCategorisationSource("MEERKAT");
		txnResponse.setCategorisationSourceId("24");
		txnResponse.setCategory("Services/Supplies");
		txnResponse.setMerchant("");
		txnResponse = enrichedTransactionResponse;
		meerkatResponseList.add(txnResponse);
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catRequest.getMemId());
		response.setCobrandId(catRequest.getCobrandId());
		when(meerkatService.categorise(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyMap(), Mockito.anyObject())).thenReturn(meerkatResponseList);
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catRequest, categorizationStats);
	
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getSimpleDescription(), "BUSINESS TO BUSINESS ACH ONLINE PAYROLL PAYROLL XX1 XX87 LAXA AND KRAUSE *ENTER");
		Assert.assertEquals(etr.getSimpleDescription(),catRequest.getTxns().get(0).getDescription());
		}
	}
	
	@Test
	public void testApplyCategorisationWithMerchant() {
		
		CategorizationRequest catRequest = CommonUtil.getCategorizationRequest();
		for(TransactionRequest txnRequest : catRequest.getTxns() ) {
			txnRequest.setTransactionId(new BigInteger("2"));
			txnRequest.setDescription("Automotive");
		}
		catRequest.setAccountType("Cash Management Account");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		List<TransactionResponse> meerkatResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse txnResponse= new TransactionResponse();
		EnrichedTransactionResponse enrichedTransactionResponse = new EnrichedTransactionResponse();
		enrichedTransactionResponse.setMeerkatSubType("Debit");
		enrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		enrichedTransactionResponse.setSimpleDescription("53 automotive");
		txnResponse.setTransactionId(new BigInteger("2"));
		txnResponse.setCategorisationSource("MEERKAT");
		txnResponse.setCategorisationSourceId("24");
		txnResponse.setCategory("Automotive/Fuel");
		txnResponse.setMerchant("53 automotive");
		txnResponse = enrichedTransactionResponse;
		meerkatResponseList.add(txnResponse);
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catRequest.getMemId());
		response.setCobrandId(catRequest.getCobrandId());
		when(meerkatService.categorise(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyMap(), Mockito.anyObject())).thenReturn(meerkatResponseList);
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catRequest, categorizationStats);
	
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getSimpleDescription(), "53 automotive");
		}
	}
	
	@Test
	public void testApplyCategorisationForLegacyForBankContainer() {
		
		CategorizationRequest catRequest = CommonUtil.getCategorizationRequest();
		catRequest.getConfigurations().setMeerkat(false);
		catRequest.getConfigurations().setLegacy(true);
		for(TransactionRequest txnRequest : catRequest.getTxns() ) {
			txnRequest.setTransactionId(new BigInteger("2"));
			txnRequest.setDescription("Automotive");
			txnRequest.setDate("");
		}
		catRequest.setAccountType("Cash Management Account");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		List<TransactionResponse> legacyResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse txnResponse= new TransactionResponse();
		EnrichedTransactionResponse enrichedTransactionResponse = new EnrichedTransactionResponse();
		enrichedTransactionResponse.setMeerkatSubType("Debit");
		enrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		enrichedTransactionResponse.setSimpleDescription("53 automotive");
		enrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		txnResponse.setCategorisationSource("LEGACY");
		txnResponse.setCategorisationSourceId("23");
		txnResponse.setCategory("Automotive/Fuel");
		txnResponse.setMerchant("53 automotive");
		txnResponse = enrichedTransactionResponse;
		txnResponse.setTransactionId(new BigInteger("2"));
		legacyResponseList.add(txnResponse);
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catRequest.getMemId());
		response.setCobrandId(catRequest.getCobrandId());
		when(legacyService.categorise(Mockito.anyObject())).thenReturn(legacyResponseList);
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catRequest, categorizationStats);
	
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getSimpleDescription(), "53 automotive");
		}
	}
	@Test
	public void testApplyCategorisationForLegacyForCreditsContainer() {
		
		CategorizationRequest catRequest = CommonUtil.getCategorizationRequest();
		catRequest.getConfigurations().setMeerkat(false);
		catRequest.getConfigurations().setLegacy(true);
		catRequest.setContainer("credits");
		for(TransactionRequest txnRequest : catRequest.getTxns() ) {
			txnRequest.setTransactionId(new BigInteger("2"));
			txnRequest.setDescription("Automotive");
			txnRequest.setDate("");
		}
		catRequest.setAccountType("Cash Management Account");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		List<TransactionResponse> legacyResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse txnResponse= new TransactionResponse();
		EnrichedTransactionResponse enrichedTransactionResponse = new EnrichedTransactionResponse();
		enrichedTransactionResponse.setMeerkatSubType("Debit");
		enrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		enrichedTransactionResponse.setSimpleDescription("53 automotive");
		enrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		txnResponse.setCategorisationSource("LEGACY");
		txnResponse.setCategorisationSourceId("23");
		txnResponse.setCategory("Automotive/Fuel");
		txnResponse.setMerchant("53 automotive");
		txnResponse = enrichedTransactionResponse;
		txnResponse.setTransactionId(new BigInteger("2"));
		legacyResponseList.add(txnResponse);
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catRequest.getMemId());
		response.setCobrandId(catRequest.getCobrandId());
		when(legacyService.categorise(Mockito.anyObject())).thenReturn(legacyResponseList);
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catRequest, categorizationStats);
	
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getSimpleDescription(), "53 automotive");
		}
	}
	@Test
	public void testApplyCategorisationForLegacyForInvestmentContainer() {
		
		CategorizationRequest catRequest = CommonUtil.getCategorizationRequest();
		catRequest.getConfigurations().setMeerkat(false);
		catRequest.getConfigurations().setLegacy(true);
		catRequest.setContainer("stocks");
		for(TransactionRequest txnRequest : catRequest.getTxns() ) {
			txnRequest.setTransactionId(new BigInteger("2"));
			txnRequest.setDescription("Automotive");
			txnRequest.setDate("");
		}
		catRequest.setAccountType("Cash Management Account");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		List<TransactionResponse> legacyResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse txnResponse= new TransactionResponse();
		EnrichedTransactionResponse enrichedTransactionResponse = new EnrichedTransactionResponse();
		enrichedTransactionResponse.setMeerkatSubType("Debit");
		enrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		enrichedTransactionResponse.setSimpleDescription("53 automotive");
		enrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		txnResponse.setCategorisationSource("LEGACY");
		txnResponse.setCategorisationSourceId("23");
		txnResponse.setCategory("Automotive/Fuel");
		txnResponse.setMerchant("53 automotive");
		txnResponse = enrichedTransactionResponse;
		txnResponse.setTransactionId(new BigInteger("2"));
		legacyResponseList.add(txnResponse);
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catRequest.getMemId());
		response.setCobrandId(catRequest.getCobrandId());
		when(legacyService.categorise(Mockito.anyObject())).thenReturn(legacyResponseList);
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catRequest, categorizationStats);
	
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getSimpleDescription(), "53 automotive");
		}
	}
	@Test
	public void testCategorisationWithLegacyForLoanContainer() {
		
		CategorizationRequest catRequest = CommonUtil.getCategorizationRequest();
		catRequest.getConfigurations().setMeerkat(false);
		catRequest.getConfigurations().setLegacy(true);
		catRequest.setContainer("loans");
		for(TransactionRequest txnRequest : catRequest.getTxns() ) {
			txnRequest.setTransactionId(new BigInteger("2"));
			txnRequest.setDescription("Automotive");
			txnRequest.setDate("");
		}
		catRequest.setAccountType("HomeEquityLineOfCredit");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		List<TransactionResponse> legacyResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse txnResponse= new TransactionResponse();
		EnrichedTransactionResponse enrichedTransactionResponse = new EnrichedTransactionResponse();
		enrichedTransactionResponse.setMeerkatSubType("Debit");
		enrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		enrichedTransactionResponse.setSimpleDescription("53 automotive");
		enrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		txnResponse.setCategorisationSource("LEGACY");
		txnResponse.setCategorisationSourceId("23");
		txnResponse.setCategory("Automotive/Fuel");
		txnResponse.setMerchant("53 automotive");
		txnResponse = enrichedTransactionResponse;
		txnResponse.setTransactionId(new BigInteger("2"));
		legacyResponseList.add(txnResponse);
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catRequest.getMemId());
		response.setCobrandId(catRequest.getCobrandId());
		when(legacyService.categorise(Mockito.anyObject())).thenReturn(legacyResponseList);
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catRequest, categorizationStats);
	
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getSimpleDescription(), "53 automotive");
		}
	}
	@Test
	public void testCategorisationWithLegacyForInsuranceContainer() {
		
		CategorizationRequest catRequest = CommonUtil.getCategorizationRequest();
		catRequest.getConfigurations().setMeerkat(false);
		catRequest.getConfigurations().setLegacy(true);
		catRequest.setContainer("insurance");
		for(TransactionRequest txnRequest : catRequest.getTxns() ) {
			txnRequest.setTransactionId(new BigInteger("2"));
			txnRequest.setDescription("Automotive");
			txnRequest.setDate("");
		}
		catRequest.setAccountType("rewardPoints");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		List<TransactionResponse> legacyResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse txnResponse= new TransactionResponse();
		EnrichedTransactionResponse enrichedTransactionResponse = new EnrichedTransactionResponse();
		enrichedTransactionResponse.setMeerkatSubType("Debit");
		enrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		enrichedTransactionResponse.setSimpleDescription("53 automotive");
		enrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		txnResponse.setCategorisationSource("LEGACY");
		txnResponse.setCategorisationSourceId("23");
		txnResponse.setCategory("Automotive/Fuel");
		txnResponse.setMerchant("53 automotive");
		txnResponse = enrichedTransactionResponse;
		txnResponse.setTransactionId(new BigInteger("2"));
		legacyResponseList.add(txnResponse);
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catRequest.getMemId());
		response.setCobrandId(catRequest.getCobrandId());
		when(legacyService.categorise(Mockito.anyObject())).thenReturn(legacyResponseList);
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catRequest, categorizationStats);
	
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getSimpleDescription(), "53 automotive");
		}
	}
	
	
	@Test
	public void testApplyCategorisationWithMCCEnable() {
		
		CategorizationRequest catRequest = CommonUtil.getCategorizationRequest();
		catRequest.getConfigurations().setMeerkat(true);
		catRequest.getConfigurations().setLegacy(false);
		catRequest.getConfigurations().setMccRule(1L);
		for(TransactionRequest txnRequest : catRequest.getTxns() ) {
			txnRequest.setTransactionId(new BigInteger("2"));
			txnRequest.setDescription("Automotive");
		}
		catRequest.setAccountType("Cash Management Account");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		List<TransactionResponse> meerkatResponseList = new ArrayList<TransactionResponse>();
		EnrichedTransactionResponse mccResponse = new EnrichedTransactionResponse();
		mccResponse.setTransactionId(new BigInteger("2"));
		mccResponse.setCategorisationSource("INVESTMENT_TRANSACTION_TYPE_MAPPING");
		mccResponse.setCategorisationSourceId("15");
		mccResponse.setCategory("Automotive/Fuel");
		
		EnrichedTransactionResponse meerkatResponse = new EnrichedTransactionResponse();
		meerkatResponse.setMeerkatSubType("Debit");
		meerkatResponse.setMeerkatType("Other Withdrawals");
		meerkatResponse.setSimpleDescription("53 automotive");
		meerkatResponse.setTransactionId(new BigInteger("2"));
		meerkatResponse.setCategorisationSource("MEERKAT");
		meerkatResponse.setCategorisationSourceId("24");
		meerkatResponse.setCategory("Groceries");
		meerkatResponse.setMerchant("53 automotive");
		
		meerkatResponseList.add(meerkatResponse);
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catRequest.getMemId());
		response.setCobrandId(catRequest.getCobrandId());
		when(mccCategorizationService.categorise(Mockito.anyObject(), Mockito.anyObject())).thenReturn(mccResponse);
		when(meerkatService.categorise(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyMap(), Mockito.anyObject())).thenReturn(meerkatResponseList);
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catRequest, categorizationStats);
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getSimpleDescription(), "53 automotive");
	//	System.out.println("Manish::: "+etr.getCategorisationSource()+ " "+etr.getCategory()+" "+etr.getMerchant()+" "+etr.getMeerkatType());
		}
	}
	
	@Test
	public void testApplyCategorisationWithMeerkatAndLegacyEnabled() {
		
		CategorizationRequest catRequest = CommonUtil.getCategorizationRequest();
		catRequest.getConfigurations().setMeerkat(true);
		catRequest.getConfigurations().setLegacy(true);
		for(TransactionRequest txnRequest : catRequest.getTxns() ) {
			txnRequest.setTransactionId(new BigInteger("2"));
			txnRequest.setDescription("Automotive");
		}
		catRequest.setAccountType("Cash Management Account");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		List<TransactionResponse> meerkatResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse txnResponse= new TransactionResponse();
		EnrichedTransactionResponse enrichedTransactionResponse = new EnrichedTransactionResponse();
		enrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		enrichedTransactionResponse.setMeerkatSubType("Debit");
		enrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		enrichedTransactionResponse.setSimpleDescription("53 automotive");	
		enrichedTransactionResponse.setUseLegacyMerchant(true);
		txnResponse.setCategorisationSource("MEERKAT");
		txnResponse.setCategorisationSourceId("24");
		txnResponse.setCategory("Automotive/Fuel");
		txnResponse = enrichedTransactionResponse;
		txnResponse.setTransactionId(new BigInteger("2"));
		meerkatResponseList.add(txnResponse);
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catRequest.getMemId());
		response.setCobrandId(catRequest.getCobrandId());
		when(mccCategorizationService.categorise(Mockito.anyObject(), Mockito.anyObject())).thenReturn(txnResponse);
		when(meerkatService.categorise(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyMap(), Mockito.anyObject())).thenReturn(meerkatResponseList);
		List<TransactionResponse> legacyResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse legacyTxnResponse= new TransactionResponse();
		EnrichedTransactionResponse legacyEnrichedTransactionResponse = new EnrichedTransactionResponse();
		legacyEnrichedTransactionResponse.setMeerkatSubType("Debit");
		legacyEnrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		legacyEnrichedTransactionResponse.setSimpleDescription("53 automotive");
		legacyEnrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		legacyTxnResponse.setCategorisationSource("MEERKAT");
		legacyTxnResponse.setCategorisationSourceId("24");
		legacyTxnResponse.setCategory("Automotive/Fuel");
		legacyTxnResponse.setMerchant("53 automotive");
		legacyTxnResponse = legacyEnrichedTransactionResponse;
		legacyResponseList.add(legacyTxnResponse);
		legacyTxnResponse.setTransactionId(new BigInteger("2"));
		when(legacyService.categorise(Mockito.anyObject())).thenReturn(legacyResponseList);
		TransactionCategory category = new TransactionCategory();
		category.setName("Automotive/Fuel");
		category.setSpecificCategory(true);
		when(categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean(),Mockito.anyBoolean())).thenReturn(category);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catRequest, categorizationStats);
	
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getSimpleDescription(), "53 automotive");
		}
	}
	
	@Test
	public void testApplyCategorisationWithMeerkatAndLegacyMerchant() {
		
		CategorizationRequest catRequest = CommonUtil.getCategorizationRequest();
		catRequest.getConfigurations().setMeerkat(true);
		catRequest.getConfigurations().setLegacy(true);
		for(TransactionRequest txnRequest : catRequest.getTxns() ) {
			txnRequest.setTransactionId(new BigInteger("2"));
			txnRequest.setDescription("Automotive");
		}
		catRequest.setAccountType("Cash Management Account");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		List<TransactionResponse> meerkatResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse txnResponse= new TransactionResponse();
		EnrichedTransactionResponse enrichedTransactionResponse = new EnrichedTransactionResponse();
		enrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		enrichedTransactionResponse.setMeerkatSubType("Debit");
		enrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		enrichedTransactionResponse.setSimpleDescription("53 automotive");	
		enrichedTransactionResponse.setUseLegacyMerchant(true);
		txnResponse.setCategorisationSource("MEERKAT");
		txnResponse.setCategorisationSourceId("24");
		txnResponse.setCategory("Automotive/Fuel");
		txnResponse = enrichedTransactionResponse;
		txnResponse.setTransactionId(new BigInteger("2"));
		meerkatResponseList.add(txnResponse);
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catRequest.getMemId());
		response.setCobrandId(catRequest.getCobrandId());
		when(mccCategorizationService.categorise(Mockito.anyObject(), Mockito.anyObject())).thenReturn(txnResponse);
		when(meerkatService.categorise(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyMap(), Mockito.anyObject())).thenReturn(meerkatResponseList);
		List<TransactionResponse> legacyResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse legacyTxnResponse= new TransactionResponse();
		EnrichedTransactionResponse legacyEnrichedTransactionResponse = new EnrichedTransactionResponse();
		legacyEnrichedTransactionResponse.setMeerkatSubType("Debit");
		legacyEnrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		legacyEnrichedTransactionResponse.setSimpleDescription("53 automotive");
		legacyEnrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		legacyEnrichedTransactionResponse.setMerchant("53 automotive");
		legacyTxnResponse.setCategorisationSource("MEERKAT");
		legacyTxnResponse.setCategorisationSourceId("24");
		legacyTxnResponse.setCategory("Automotive/Fuel");
		legacyTxnResponse.setMerchant("53 automotive");
		legacyTxnResponse = legacyEnrichedTransactionResponse;
		legacyResponseList.add(legacyTxnResponse);
		legacyTxnResponse.setTransactionId(new BigInteger("2"));
		when(legacyService.categorise(Mockito.anyObject())).thenReturn(legacyResponseList);
		TransactionCategory category = new TransactionCategory();
		category.setName("Automotive/Fuel");
		category.setSpecificCategory(true);
		when(categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean(),Mockito.anyBoolean())).thenReturn(category);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catRequest, categorizationStats);
	
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getSimpleDescription(), "53 automotive");
		}
	}
	
	@Test
	public void testApplyCategorisationMeerkatMerchantWithSimpleDescVersion() {
		
		CategorizationRequest catRequest = CommonUtil.getCategorizationRequest();
		catRequest.getConfigurations().setMeerkat(true);
		catRequest.getConfigurations().setLegacy(true);
		catRequest.getConfigurations().setSimpleDescVersion("1.1");
		for(TransactionRequest txnRequest : catRequest.getTxns() ) {
			txnRequest.setTransactionId(new BigInteger("2"));
			txnRequest.setDescription("Automotive");
		}
		catRequest.setAccountType("Cash Management Account");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		List<TransactionResponse> meerkatResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse txnResponse= new TransactionResponse();
		EnrichedTransactionResponse enrichedTransactionResponse = new EnrichedTransactionResponse();
		enrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		enrichedTransactionResponse.setMeerkatSubType("Debit");
		enrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		enrichedTransactionResponse.setSimpleDescription("53 automotive");	
		enrichedTransactionResponse.setUseLegacyMerchant(true);
		txnResponse.setCategorisationSource("MEERKAT");
		txnResponse.setCategorisationSourceId("24");
		txnResponse.setCategory("Automotive/Fuel");
		txnResponse = enrichedTransactionResponse;
		txnResponse.setTransactionId(new BigInteger("2"));
		meerkatResponseList.add(txnResponse);
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catRequest.getMemId());
		response.setCobrandId(catRequest.getCobrandId());
		when(mccCategorizationService.categorise(Mockito.anyObject(), Mockito.anyObject())).thenReturn(txnResponse);
		when(meerkatService.categorise(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyMap(), Mockito.anyObject())).thenReturn(meerkatResponseList);
		List<TransactionResponse> legacyResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse legacyTxnResponse= new TransactionResponse();
		EnrichedTransactionResponse legacyEnrichedTransactionResponse = new EnrichedTransactionResponse();
		legacyEnrichedTransactionResponse.setMeerkatSubType("Debit");
		legacyEnrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		legacyEnrichedTransactionResponse.setSimpleDescription("53 automotive");
		legacyEnrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		legacyEnrichedTransactionResponse.setMerchant("53 automotive");
		legacyTxnResponse.setCategorisationSource("MEERKAT");
		legacyTxnResponse.setCategorisationSourceId("24");
		legacyTxnResponse.setCategory("Automotive/Fuel");
		legacyTxnResponse.setMerchant("53 automotive");
		legacyTxnResponse = legacyEnrichedTransactionResponse;
		legacyResponseList.add(legacyTxnResponse);
		legacyTxnResponse.setTransactionId(new BigInteger("2"));
		when(legacyService.categorise(Mockito.anyObject())).thenReturn(legacyResponseList);
		TransactionCategory category = new TransactionCategory();
		category.setName("Automotive/Fuel");
		category.setSpecificCategory(false);
		when(categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean(),Mockito.anyBoolean())).thenReturn(category);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catRequest, categorizationStats);	
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getMerchant(), "53 automotive");
		}
	}
	
	
	@Test
	public void testApplyCategorisationLegacyMerchant() {
		
		CategorizationRequest catRequest = CommonUtil.getCategorizationRequest();
		catRequest.getConfigurations().setMeerkat(true);
		catRequest.getConfigurations().setLegacy(true);
		catRequest.getConfigurations().setSimpleDescVersion("1.1");
		for(TransactionRequest txnRequest : catRequest.getTxns() ) {
			txnRequest.setTransactionId(new BigInteger("2"));
			txnRequest.setDescription("Automotive");
		}
		catRequest.setAccountType("Cash Management Account");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		List<TransactionResponse> meerkatResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse txnResponse= new TransactionResponse();
		EnrichedTransactionResponse enrichedTransactionResponse = new EnrichedTransactionResponse();
		enrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		enrichedTransactionResponse.setMeerkatSubType("Debit");
		enrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		enrichedTransactionResponse.setSimpleDescription("53 automotive");	
		enrichedTransactionResponse.setUseLegacyMerchant(true);
		txnResponse.setCategorisationSource("MEERKAT");
		txnResponse.setCategorisationSourceId("24");
		txnResponse.setCategory("Automotive/Fuel");
		txnResponse = enrichedTransactionResponse;
		txnResponse.setTransactionId(new BigInteger("2"));
		meerkatResponseList.add(txnResponse);
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catRequest.getMemId());
		response.setCobrandId(catRequest.getCobrandId());
		when(mccCategorizationService.categorise(Mockito.anyObject(), Mockito.anyObject())).thenReturn(txnResponse);
		when(meerkatService.categorise(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyMap(), Mockito.anyObject())).thenReturn(meerkatResponseList);
		List<TransactionResponse> legacyResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse legacyTxnResponse= new TransactionResponse();
		EnrichedTransactionResponse legacyEnrichedTransactionResponse = new EnrichedTransactionResponse();
		legacyEnrichedTransactionResponse.setMeerkatSubType("Debit");
		legacyEnrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		legacyEnrichedTransactionResponse.setSimpleDescription("53 automotive");
		legacyEnrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		legacyEnrichedTransactionResponse.setMerchant("53 automotive");
		legacyTxnResponse.setCategorisationSource("LEGACY");
		legacyTxnResponse.setCategorisationSourceId("23");
		legacyTxnResponse.setCategory("Automotive/Fuel");
		legacyTxnResponse.setMerchant("53 automotive");
		legacyTxnResponse = legacyEnrichedTransactionResponse;
		legacyResponseList.add(legacyTxnResponse);
		legacyTxnResponse.setTransactionId(new BigInteger("2"));
		when(legacyService.categorise(Mockito.anyObject())).thenReturn(legacyResponseList);
		TransactionCategory category = new TransactionCategory();
		category.setName("Automotive/Fuel");
		category.setSpecificCategory(false);
		when(categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean(),Mockito.anyBoolean())).thenReturn(category);
		when(caasConfigBean.getCaas()).thenReturn(getCaas());
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catRequest, categorizationStats);
		
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getMerchant(), "53 automotive");
		}
	}
	public Caas getCaas() {
		Caas caasObj = new Caas();
		caasObj.setPopulateAdditionalFields(true);
		caasObj.setPopulateIntermediary(true);
		caasObj.setPopulateIsPhysical(true);
		caasObj.setPopulateTxnMerchantType(true);
		caasObj.setPopulateLogoEndpoint(true);
		 return caasObj;
	}
	
	@Test
	public void testCategorizationWithTdeDisabled() {
		CategorizationRequest catRequest = CommonUtil.getCategorizationRequest();
		catRequest.getConfigurations().setMeerkat(false);
		catRequest.getConfigurations().setLegacy(true);
		catRequest.getConfigurations().setSimpleDescVersion("1.1");
		for(TransactionRequest txnRequest : catRequest.getTxns() ) {
			txnRequest.setTransactionId(new BigInteger("2"));
			txnRequest.setDescription("Automotive");
		}
		catRequest.setAccountType("Cash Management Account");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		List<TransactionResponse> meerkatResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse txnResponse= new TransactionResponse();
		EnrichedTransactionResponse enrichedTransactionResponse = new EnrichedTransactionResponse();
		enrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		enrichedTransactionResponse.setMeerkatSubType("Debit");
		enrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		enrichedTransactionResponse.setSimpleDescription("53 automotive");	
		enrichedTransactionResponse.setUseLegacyMerchant(true);
		txnResponse.setCategorisationSource("MEERKAT");
		txnResponse.setCategorisationSourceId("24");
		txnResponse.setCategory("Automotive/Fuel");
		txnResponse = enrichedTransactionResponse;
		txnResponse.setTransactionId(new BigInteger("2"));
		meerkatResponseList.add(txnResponse);
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catRequest.getMemId());
		response.setCobrandId(catRequest.getCobrandId());
		when(mccCategorizationService.categorise(Mockito.anyObject(), Mockito.anyObject())).thenReturn(txnResponse);
		when(meerkatService.categorise(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyMap(), Mockito.anyObject())).thenReturn(meerkatResponseList);
		List<TransactionResponse> legacyResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse legacyTxnResponse= new TransactionResponse();
		EnrichedTransactionResponse legacyEnrichedTransactionResponse = new EnrichedTransactionResponse();
		legacyEnrichedTransactionResponse.setMeerkatSubType("Debit");
		legacyEnrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		legacyEnrichedTransactionResponse.setSimpleDescription("53 automotive");
		legacyEnrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		legacyEnrichedTransactionResponse.setMerchant("53 automotive");
		legacyTxnResponse.setCategorisationSource("LEGACY");
		legacyTxnResponse.setCategorisationSourceId("23");
		legacyTxnResponse.setCategory("Automotive/Fuel");
		legacyTxnResponse.setMerchant("53 automotive");
		legacyTxnResponse = legacyEnrichedTransactionResponse;
		legacyResponseList.add(legacyTxnResponse);
		legacyTxnResponse.setTransactionId(new BigInteger("2"));
		when(legacyService.categorise(Mockito.anyObject())).thenReturn(legacyResponseList);
		TransactionCategory category = new TransactionCategory();
		category.setName("Automotive/Fuel");
		category.setSpecificCategory(false);
		when(categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean(),Mockito.anyBoolean())).thenReturn(category);
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catRequest, categorizationStats);
	
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getMerchant(), "53 automotive");
		}
	}
	
	@Test
	public void testCategorizationForInvestmentWithTdeDisabled() {
		
		CategorizationRequest catRequest = CommonUtil.getCategorizationRequest();
		catRequest.setContainer("stocks");
		catRequest.getConfigurations().setMeerkat(false);
		catRequest.getConfigurations().setLegacy(true);
		catRequest.getConfigurations().setSimpleDescVersion("1.1");
		for(TransactionRequest txnRequest : catRequest.getTxns() ) {
			txnRequest.setTransactionId(new BigInteger("2"));
			txnRequest.setDescription("Automotive");
		}
		catRequest.setAccountType("Cash Management Account");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		List<TransactionResponse> meerkatResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse txnResponse= new TransactionResponse();
		EnrichedTransactionResponse enrichedTransactionResponse = new EnrichedTransactionResponse();
		enrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		enrichedTransactionResponse.setMeerkatSubType("Debit");
		enrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		enrichedTransactionResponse.setSimpleDescription("53 automotive");	
		enrichedTransactionResponse.setUseLegacyMerchant(true);
		txnResponse.setCategorisationSource("MEERKAT");
		txnResponse.setCategorisationSourceId("24");
		txnResponse.setCategory("Automotive/Fuel");
		txnResponse = enrichedTransactionResponse;
		txnResponse.setTransactionId(new BigInteger("2"));
		meerkatResponseList.add(txnResponse);
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catRequest.getMemId());
		response.setCobrandId(catRequest.getCobrandId());
		when(mccCategorizationService.categorise(Mockito.anyObject(), Mockito.anyObject())).thenReturn(txnResponse);
		when(meerkatService.categorise(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyMap(), Mockito.anyObject())).thenReturn(meerkatResponseList);
		List<TransactionResponse> legacyResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse legacyTxnResponse= new TransactionResponse();
		EnrichedTransactionResponse legacyEnrichedTransactionResponse = new EnrichedTransactionResponse();
		legacyEnrichedTransactionResponse.setMeerkatSubType("Debit");
		legacyEnrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		legacyEnrichedTransactionResponse.setSimpleDescription("53 automotive");
		legacyEnrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		legacyEnrichedTransactionResponse.setMerchant("53 automotive");
		legacyTxnResponse.setCategorisationSource("LEGACY");
		legacyTxnResponse.setCategorisationSourceId("23");
		legacyTxnResponse.setCategory("Automotive/Fuel");
		legacyTxnResponse.setMerchant("53 automotive");
		legacyTxnResponse = legacyEnrichedTransactionResponse;
		legacyResponseList.add(legacyTxnResponse);
		legacyTxnResponse.setTransactionId(new BigInteger("2"));
		when(legacyService.categorise(Mockito.anyObject())).thenReturn(legacyResponseList);
		TransactionCategory category = new TransactionCategory();
		category.setName("Automotive/Fuel");
		category.setSpecificCategory(false);
		when(categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean(),Mockito.anyBoolean())).thenReturn(category);
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catRequest, categorizationStats);
	
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getMerchant(), "53 automotive");
		}
	}
	
	@Test
	public void testCategorizationForCardWithTdeDisabled() {
		
		CategorizationRequest catRequest = CommonUtil.getCategorizationRequest();
		catRequest.setContainer("credits");
		catRequest.getConfigurations().setMeerkat(false);
		catRequest.getConfigurations().setLegacy(true);
		catRequest.getConfigurations().setSimpleDescVersion("1.1");
		for(TransactionRequest txnRequest : catRequest.getTxns() ) {
			txnRequest.setTransactionId(new BigInteger("2"));
			txnRequest.setDescription("Automotive");
		}
		catRequest.setAccountType("Cash Management Account");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		List<TransactionResponse> meerkatResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse txnResponse= new TransactionResponse();
		EnrichedTransactionResponse enrichedTransactionResponse = new EnrichedTransactionResponse();
		enrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		enrichedTransactionResponse.setMeerkatSubType("Debit");
		enrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		enrichedTransactionResponse.setSimpleDescription("53 automotive");	
		enrichedTransactionResponse.setUseLegacyMerchant(true);
		txnResponse.setCategorisationSource("MEERKAT");
		txnResponse.setCategorisationSourceId("24");
		txnResponse.setCategory("Automotive/Fuel");
		txnResponse = enrichedTransactionResponse;
		txnResponse.setTransactionId(new BigInteger("2"));
		meerkatResponseList.add(txnResponse);
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catRequest.getMemId());
		response.setCobrandId(catRequest.getCobrandId());
		when(mccCategorizationService.categorise(Mockito.anyObject(), Mockito.anyObject())).thenReturn(txnResponse);
		when(meerkatService.categorise(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyMap(), Mockito.anyObject())).thenReturn(meerkatResponseList);
		List<TransactionResponse> legacyResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse legacyTxnResponse= new TransactionResponse();
		EnrichedTransactionResponse legacyEnrichedTransactionResponse = new EnrichedTransactionResponse();
		legacyEnrichedTransactionResponse.setMeerkatSubType("Debit");
		legacyEnrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		legacyEnrichedTransactionResponse.setSimpleDescription("53 automotive");
		legacyEnrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		legacyEnrichedTransactionResponse.setMerchant("53 automotive");
		legacyTxnResponse.setCategorisationSource("LEGACY");
		legacyTxnResponse.setCategorisationSourceId("23");
		legacyTxnResponse.setCategory("Automotive/Fuel");
		legacyTxnResponse.setMerchant("53 automotive");
		legacyTxnResponse = legacyEnrichedTransactionResponse;
		legacyResponseList.add(legacyTxnResponse);
		legacyTxnResponse.setTransactionId(new BigInteger("2"));
		when(legacyService.categorise(Mockito.anyObject())).thenReturn(legacyResponseList);
		TransactionCategory category = new TransactionCategory();
		category.setName("Automotive/Fuel");
		category.setSpecificCategory(false);
		when(categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean(),Mockito.anyBoolean())).thenReturn(category);
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catRequest, categorizationStats);
	
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getMerchant(), "53 automotive");
		}
	}
	
	@Test
	public void testCategorizationForCardForUncategorized() {
		
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMeerkat(false);
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("credits");
		catReq.setAccountType("Cash Management Account");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catReq.getMemId());
		response.setCobrandId(catReq.getCobrandId());
		List<TransactionResponse> legacyResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse legacyTxnResponse= new TransactionResponse();
		EnrichedTransactionResponse legacyEnrichedTransactionResponse = new EnrichedTransactionResponse();
		legacyEnrichedTransactionResponse.setMeerkatSubType("Debit");
		legacyEnrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		legacyEnrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		legacyTxnResponse.setCategorisationSource("LEGACY");
		legacyTxnResponse.setCategorisationSourceId("23");
		legacyTxnResponse.setCategory("Uncategorized");
		legacyTxnResponse = legacyEnrichedTransactionResponse;
		legacyResponseList.add(legacyTxnResponse);
		legacyTxnResponse.setTransactionId(new BigInteger("2"));
		when(legacyService.categorise(Mockito.anyObject())).thenReturn(legacyResponseList);
		TransactionCategory category = new TransactionCategory();
		category.setName("Uncategorized");
		category.setSpecificCategory(false);
		when(categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean(),Mockito.anyBoolean())).thenReturn(category);
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catReq, categorizationStats);
	
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getCategory(), "Uncategorized");
		}
	}
	
	@Test
	public void testCategorizationForCreditCardContainer() {
		
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMeerkat(false);
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("credits");
		catReq.setAccountType("Cash Management Account");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catReq.getMemId());
		response.setCobrandId(catReq.getCobrandId());
		List<TransactionResponse> legacyResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse legacyTxnResponse= new TransactionResponse();
		EnrichedTransactionResponse legacyEnrichedTransactionResponse = new EnrichedTransactionResponse();
		legacyEnrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		legacyTxnResponse.setCategorisationSource("LEGACY");
		legacyTxnResponse.setCategorisationSourceId("23");
		legacyTxnResponse.setCategory("Other Income");
		legacyTxnResponse = legacyEnrichedTransactionResponse;
		legacyResponseList.add(legacyTxnResponse);
		legacyTxnResponse.setTransactionId(new BigInteger("2"));
		when(legacyService.categorise(Mockito.anyObject())).thenReturn(legacyResponseList);
		TransactionCategory category = new TransactionCategory();
		category.setName("Other Income");
		category.setSpecificCategory(false);
		when(categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean(),Mockito.anyBoolean())).thenReturn(category);
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catReq, categorizationStats);
	
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getCategory(), "Other Income");
		}
	}
	
	@Test
	public void testCategorizationForDebitCardContainer() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMeerkat(false);
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("credits");
		catReq.setAccountType("Cash Management Account");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catReq.getMemId());
		response.setCobrandId(catReq.getCobrandId());
		List<TransactionResponse> legacyResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse legacyTxnResponse= new TransactionResponse();
		EnrichedTransactionResponse legacyEnrichedTransactionResponse = new EnrichedTransactionResponse();
		legacyEnrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		legacyTxnResponse.setCategorisationSource("LEGACY");
		legacyTxnResponse.setCategorisationSourceId("23");
		legacyTxnResponse.setCategory("Other Expenses");
		legacyTxnResponse = legacyEnrichedTransactionResponse;
		legacyResponseList.add(legacyTxnResponse);
		legacyTxnResponse.setTransactionId(new BigInteger("2"));
		when(legacyService.categorise(Mockito.anyObject())).thenReturn(legacyResponseList);
		TransactionCategory category = new TransactionCategory();
		category.setName("Other Expenses");
		category.setSpecificCategory(false);
		when(categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean(),Mockito.anyBoolean())).thenReturn(category);
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catReq, categorizationStats);
	
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getCategory(), "Other Expenses");
		}
	}
	
	@Test
	public void testCategorizationForRefundAndAdjustment() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMeerkat(false);
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("credits");
		catReq.setAccountType("Cash Management Account");
		YCategorizationStats categorizationStats = new YCategorizationStats();
		CategorizationResponse response = new CategorizationResponse();
		response.setMemId(catReq.getMemId());
		response.setCobrandId(catReq.getCobrandId());
		List<TransactionResponse> legacyResponseList = new ArrayList<TransactionResponse>();
		TransactionResponse legacyTxnResponse= new TransactionResponse();
		EnrichedTransactionResponse legacyEnrichedTransactionResponse = new EnrichedTransactionResponse();
		legacyEnrichedTransactionResponse.setTransactionId(new BigInteger("2"));
		legacyTxnResponse.setCategorisationSource("LEGACY");
		legacyTxnResponse.setCategorisationSourceId("23");
		legacyTxnResponse.setCategory("Refunds/Adjustments");
		legacyTxnResponse = legacyEnrichedTransactionResponse;
		legacyResponseList.add(legacyTxnResponse);
		legacyTxnResponse.setTransactionId(new BigInteger("2"));
		when(legacyService.categorise(Mockito.anyObject())).thenReturn(legacyResponseList);
		TransactionCategory category = new TransactionCategory();
		category.setName("Refunds/Adjustments");
		category.setSpecificCategory(false);
		when(categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean(),Mockito.anyBoolean())).thenReturn(category);
		CategorizationResponse categorizationResponse=categorizationAlgorithmImpl.applyCategorisation(catReq, categorizationStats);
	
		for(TransactionResponse transactionResponse : categorizationResponse.getTxns()){
			EnrichedTransactionResponse etr = (EnrichedTransactionResponse) transactionResponse;
		Assert.assertEquals(etr.getCategory(), "Refunds/Adjustments");
		}
	}
	
	
	
}
