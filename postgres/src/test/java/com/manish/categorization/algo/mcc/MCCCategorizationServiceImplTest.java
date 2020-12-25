package com.manish.categorization.algo.mcc;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.manish.categorization.algo.mcc.MCCCategorizationServiceImpl;
import com.manish.categorization.db.GranularCategoryCache;
import com.manish.categorization.db.MccTxnCatMapCache;
import com.manish.categorization.db.TransactionCategory;
import com.manish.categorization.db.TransactionCategoryCache;
import com.manish.categorization.rest.AbstractTest;
import com.manish.categorization.rest.dto.CategorizationRequest;
import com.manish.categorization.rest.dto.EnrichedTransactionResponse;
import com.manish.categorization.rest.dto.TransactionRequest;
import com.manish.categorization.rest.dto.TransactionResponse;
import com.manish.categorization.util.CommonUtil;
import com.yodlee.simpledescservice.rest.dto.SimpleDescriptionRequest;
import com.yodlee.simpledescservice.service.SimpleDescriptionService;

import junit.framework.Assert;

public class MCCCategorizationServiceImplTest extends AbstractTest{
	
	@Mock
	MccTxnCatMapCache mccTxnCatMapCache;
	
	@Mock
	TransactionCategoryCache categoryCache;	
	
	@Mock
	GranularCategoryCache granularCategoryCache;
	
	@InjectMocks
	MCCCategorizationServiceImpl mCCCategorizationServiceImpl;
	
	@Mock
	SimpleDescriptionService simpleDescriptionService;
	

	@Override
	@Before
	public void setUp() {
      super.setUp();
      MockitoAnnotations.initMocks(this);   
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testApplyMCCCategorization() {
		Long mccCode=6012L;
		HashMap<Long,String> mccTxnCatMap = new HashMap<Long,String>();
		mccTxnCatMap.put(6012L, "Other Expenses");
		when(mccTxnCatMapCache.getMccTxnCatMap()).thenReturn(mccTxnCatMap);
		String txnCategory = mCCCategorizationServiceImpl.applyMCCCategorization(mccCode);
		Assert.assertEquals(txnCategory, "Other Expenses");
		
	}	
	
	@SuppressWarnings("deprecation")
	@Test
	public void testMCCForBankWithCategoryMerger() {		
		String transactionCategoryName="Other Expenses";
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq = CommonUtil.getCateRequest(true, true, true);
		HashMap<Long,String> mccTxnCatMap = new HashMap<Long,String>();
		mccTxnCatMap.put(6012L, "Other Expenses");
		when(mccTxnCatMapCache.getMccTxnCatMap()).thenReturn(mccTxnCatMap);
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		when (categoryCache.getTransactionCategory(transactionCategoryName,
				true)).thenReturn(cat);
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		TransactionResponse response = mCCCategorizationServiceImpl.categorise(req, catReq);
		txnResponse = (EnrichedTransactionResponse) response;
		Assert.assertEquals(txnResponse.getGranularCategory(), "other expenses");
		Assert.assertEquals(txnResponse.getCategory(), "Other Expenses");
		Assert.assertEquals(txnResponse.getCategorisationSource(), "MCC");
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testMCCForBankWithCategoryMergerWithAccountType() {		
		String transactionCategoryName="Other Expenses";
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq = CommonUtil.getCateRequest(true, true, true);
		catReq.getTxns().get(0).setAccountType("savings");
		HashMap<Long,String> mccTxnCatMap = new HashMap<Long,String>();
		mccTxnCatMap.put(6012L, "Other Expenses");
		when(mccTxnCatMapCache.getMccTxnCatMap()).thenReturn(mccTxnCatMap);
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		when (categoryCache.getTransactionCategory(transactionCategoryName,
				true)).thenReturn(cat);
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		TransactionResponse response = mCCCategorizationServiceImpl.categorise(req, catReq);
		txnResponse = (EnrichedTransactionResponse) response;
		Assert.assertEquals(txnResponse.getGranularCategory(), "other expenses");
		Assert.assertEquals(txnResponse.getCategory(), "Other Expenses");
		Assert.assertEquals(txnResponse.getCategorisationSource(), "MCC");
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testMCCForBankWithCategoryMergerTdeDisabled() {		
		String transactionCategoryName="Other Expenses";
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq = CommonUtil.getCateRequest(true, false, true);
		HashMap<Long,String> mccTxnCatMap = new HashMap<Long,String>();
		mccTxnCatMap.put(6012L, "Other Expenses");
		when(mccTxnCatMapCache.getMccTxnCatMap()).thenReturn(mccTxnCatMap);
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		when (categoryCache.getTransactionCategory(transactionCategoryName,
				true)).thenReturn(cat);
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		TransactionResponse response = mCCCategorizationServiceImpl.categorise(req, catReq);
		txnResponse = (EnrichedTransactionResponse) response;
		Assert.assertEquals(txnResponse.getGranularCategory(), null);
		Assert.assertEquals(txnResponse.getCategory(), "Other Expenses");
		Assert.assertEquals(txnResponse.getCategorisationSource(), "MCC");
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testMCCForBankWithoutCategoryMerger() {		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq = CommonUtil.getCateRequest(false, true, true);
		HashMap<Long,String> mccTxnCatMap = new HashMap<Long,String>();
		mccTxnCatMap.put(6012L, "Other Expenses");
		when(mccTxnCatMapCache.getMccTxnCatMap()).thenReturn(mccTxnCatMap);
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		String transactionCategoryName="Other Expenses";
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		when (categoryCache.getTransactionCategory(transactionCategoryName,
				false)).thenReturn(cat);
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		TransactionResponse response = mCCCategorizationServiceImpl.categorise(req, catReq);
		txnResponse = (EnrichedTransactionResponse) response;
		Assert.assertEquals(txnResponse.getGranularCategory(), "other expenses");
		Assert.assertEquals(txnResponse.getCategory(), "Other Expenses");
		Assert.assertEquals(txnResponse.getCategorisationSource(), "MCC");
		
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testMCCForBankWithoutCategoryMergerTdeDisabled() {		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq = CommonUtil.getCateRequest(false, false, true);
		HashMap<Long,String> mccTxnCatMap = new HashMap<Long,String>();
		mccTxnCatMap.put(6012L, "Other Expenses");
		when(mccTxnCatMapCache.getMccTxnCatMap()).thenReturn(mccTxnCatMap);
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		String transactionCategoryName="Other Expenses";
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		when (categoryCache.getTransactionCategory(transactionCategoryName,
				false)).thenReturn(cat);
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		TransactionResponse response = mCCCategorizationServiceImpl.categorise(req, catReq);
		txnResponse = (EnrichedTransactionResponse) response;
		//Granular category will not come when tde is disabled.
		Assert.assertEquals(txnResponse.getGranularCategory(), null);
		Assert.assertEquals(txnResponse.getCategory(), "Other Expenses");
		Assert.assertEquals(txnResponse.getCategorisationSource(), "MCC");
		
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testMCCforInvestmentContainer() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq = CommonUtil.getCategorizationRequest();
		catReq.setContainer("stocks");
		HashMap<Long,String> mccTxnCatMap = new HashMap<Long,String>();
		mccTxnCatMap.put(6012L, "Other Expenses");
		when(mccTxnCatMapCache.getMccTxnCatMap()).thenReturn(mccTxnCatMap);
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		String transactionCategoryName="Other Expenses";
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		when (categoryCache.getTransactionCategory(transactionCategoryName,
				true)).thenReturn(cat);
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		SimpleDescriptionRequest simpleDescRequest = new SimpleDescriptionRequest();
		doReturn(transactionCategoryName).when(simpleDescriptionService).deriveSimpleDescription(simpleDescRequest);
		//when (simpleDescriptionService.deriveSimpleDescription(simpleDescRequest)).thenReturn(transactionCategoryName);
		TransactionResponse response = mCCCategorizationServiceImpl.categorise(req, catReq);
		txnResponse = (EnrichedTransactionResponse) response;
		Assert.assertEquals(txnResponse.getCategorisationSource(),"MCC");
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testMCCforInvestmentContainerTdeDisabled() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq = CommonUtil.getCategorizationRequest();
		catReq.setContainer("stocks");
		HashMap<Long,String> mccTxnCatMap = new HashMap<Long,String>();
		mccTxnCatMap.put(6012L, "Other Expenses");
		when(mccTxnCatMapCache.getMccTxnCatMap()).thenReturn(mccTxnCatMap);
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		String transactionCategoryName="Other Expenses";
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		when (categoryCache.getTransactionCategory(transactionCategoryName,
				true)).thenReturn(cat);
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		SimpleDescriptionRequest simpleDescRequest = new SimpleDescriptionRequest();
		doReturn(transactionCategoryName).when(simpleDescriptionService).deriveSimpleDescription(simpleDescRequest);
		//when (simpleDescriptionService.deriveSimpleDescription(simpleDescRequest)).thenReturn(transactionCategoryName);
		TransactionResponse response = mCCCategorizationServiceImpl.categorise(req, catReq);
		txnResponse = (EnrichedTransactionResponse) response;
		Assert.assertEquals(txnResponse.getCategorisationSource(),"MCC");
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testMCCforInvestmentContainerwithMergerDisabled() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq = CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("stocks");
		HashMap<Long,String> mccTxnCatMap = new HashMap<Long,String>();
		mccTxnCatMap.put(6012L, "Other Expenses");
		when(mccTxnCatMapCache.getMccTxnCatMap()).thenReturn(mccTxnCatMap);
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		String transactionCategoryName="Other Expenses";
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		when (categoryCache.getTransactionCategory(transactionCategoryName,
				false)).thenReturn(cat);
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		SimpleDescriptionRequest simpleDescRequest = new SimpleDescriptionRequest();
		doReturn(transactionCategoryName).when(simpleDescriptionService).deriveSimpleDescription(simpleDescRequest);
		//when (simpleDescriptionService.deriveSimpleDescription(simpleDescRequest)).thenReturn(transactionCategoryName);
		TransactionResponse response = mCCCategorizationServiceImpl.categorise(req, catReq);
		txnResponse = (EnrichedTransactionResponse) response;
		Assert.assertEquals(txnResponse.getCategorisationSource(),"MCC");
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testMCCforCardContainer() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq = CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("credits");
		req.setMccCode(7296L);
		HashMap<Long,String> mccTxnCatMap = new HashMap<Long,String>();
		mccTxnCatMap.put(7296L, "Clothing/Shoes");
		when(mccTxnCatMapCache.getMccTxnCatMap()).thenReturn(mccTxnCatMap);
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		cat.setName("Clothing/Shoes");
		String transactionCategoryName="Clothing/Shoes";
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		when (categoryCache.getTransactionCategory(transactionCategoryName,
				catReq.getConfigurations().isMergerEnabled())).thenReturn(cat);
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Clothing/Shoes","Jewelry/Accessories");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		TransactionResponse response = mCCCategorizationServiceImpl.categorise(req, catReq);
		txnResponse = (EnrichedTransactionResponse) response;
		Assert.assertEquals(txnResponse.getGranularCategory(),"jewelry/accessories"); 
		Assert.assertEquals(txnResponse.getCategory(),"Clothing/Shoes");
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testMCCforCardContainerWithTdeDisabled() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq = CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.getConfigurations().setMeerkat(false);
		catReq.setContainer("credits");
		req.setMccCode(7296L);
		HashMap<Long,String> mccTxnCatMap = new HashMap<Long,String>();
		mccTxnCatMap.put(7296L, "Clothing/Shoes");
		when(mccTxnCatMapCache.getMccTxnCatMap()).thenReturn(mccTxnCatMap);
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		cat.setName("Clothing/Shoes");
		String transactionCategoryName="Clothing/Shoes";
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		when (categoryCache.getTransactionCategory(transactionCategoryName,
				catReq.getConfigurations().isMergerEnabled())).thenReturn(cat);
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Clothing/Shoes","Jewelry/Accessories");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		TransactionResponse response = mCCCategorizationServiceImpl.categorise(req, catReq);
		txnResponse = (EnrichedTransactionResponse) response;
		Assert.assertEquals(txnResponse.getGranularCategory(),null); 
		Assert.assertEquals(txnResponse.getCategory(),"Clothing/Shoes");
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testMCCforLoanContainerWithoutAccountType() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq = CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(true);
		catReq.setContainer("loans");
		req.setMccCode(7296L);
		HashMap<Long,String> mccTxnCatMap = new HashMap<Long,String>();
		mccTxnCatMap.put(7296L, "Clothing/Shoes");
		when(mccTxnCatMapCache.getMccTxnCatMap()).thenReturn(mccTxnCatMap);
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		cat.setName("Clothing/Shoes");
		cat.setMergedTxnCatName("Personal/Family");
		String transactionCategoryName="Clothing/Shoes";
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		when (categoryCache.getTransactionCategory(transactionCategoryName,
				catReq.getConfigurations().isMergerEnabled())).thenReturn(cat);
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Clothing/Shoes","Jewelry/Accessories");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		TransactionResponse response = mCCCategorizationServiceImpl.categorise(req, catReq);
		txnResponse = (EnrichedTransactionResponse) response;
		Assert.assertEquals(txnResponse.getGranularCategory(),null); 
		Assert.assertEquals(txnResponse.getCategory(),"Personal/Family");
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testMCCforLoanContainerTdeDisabledWithoutAccountType() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq = CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(true);
		catReq.getConfigurations().setMeerkat(false);
		catReq.setContainer("loans");
		req.setMccCode(7296L);
		HashMap<Long,String> mccTxnCatMap = new HashMap<Long,String>();
		mccTxnCatMap.put(7296L, "Clothing/Shoes");
		when(mccTxnCatMapCache.getMccTxnCatMap()).thenReturn(mccTxnCatMap);
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		cat.setName("Clothing/Shoes");
		cat.setMergedTxnCatName("Personal/Family");
		String transactionCategoryName="Clothing/Shoes";
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		when (categoryCache.getTransactionCategory(transactionCategoryName,
				catReq.getConfigurations().isMergerEnabled())).thenReturn(cat);
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Clothing/Shoes","Jewelry/Accessories");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		TransactionResponse response = mCCCategorizationServiceImpl.categorise(req, catReq);
		txnResponse = (EnrichedTransactionResponse) response;
		Assert.assertEquals(txnResponse.getGranularCategory(),null); 
		Assert.assertEquals(txnResponse.getCategory(),"Personal/Family");
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testMCCforLoanContainerWithMergerDisabledTdeEnabledWithoutAccountType() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq = CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.getTxns().get(0).setAccountType("savings");
		catReq.setContainer("loans");
		req.setMccCode(7296L);
		HashMap<Long,String> mccTxnCatMap = new HashMap<Long,String>();
		mccTxnCatMap.put(7296L, "Clothing/Shoes");
		when(mccTxnCatMapCache.getMccTxnCatMap()).thenReturn(mccTxnCatMap);
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		cat.setName("Clothing/Shoes");
		String transactionCategoryName="Clothing/Shoes";
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		when (categoryCache.getTransactionCategory(transactionCategoryName,
				catReq.getConfigurations().isMergerEnabled())).thenReturn(cat);
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Clothing/Shoes","Jewelry/Accessories");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		TransactionResponse response = mCCCategorizationServiceImpl.categorise(req, catReq);
		txnResponse = (EnrichedTransactionResponse) response;
		Assert.assertEquals(txnResponse.getGranularCategory(),"jewelry/accessories"); 
		Assert.assertEquals(txnResponse.getCategory(),"Clothing/Shoes");
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testMCCforInsuranceContainerTdeEnabledCategoryMergerEnabled() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq = CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(true);
		catReq.setContainer("insurance");
		req.setMccCode(7296L);
		HashMap<Long,String> mccTxnCatMap = new HashMap<Long,String>();
		mccTxnCatMap.put(7296L, "Clothing/Shoes");
		when(mccTxnCatMapCache.getMccTxnCatMap()).thenReturn(mccTxnCatMap);
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		cat.setName("Clothing/Shoes");
		cat.setMergedTxnCatName("Personal/Family");
		String transactionCategoryName="Clothing/Shoes";
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		when (categoryCache.getTransactionCategory(transactionCategoryName,
				catReq.getConfigurations().isMergerEnabled())).thenReturn(cat);
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Clothing/Shoes","Jewelry/Accessories");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		TransactionResponse response = mCCCategorizationServiceImpl.categorise(req, catReq);
		txnResponse = (EnrichedTransactionResponse) response;
		Assert.assertEquals(txnResponse.getGranularCategory(),null); 
		Assert.assertEquals(txnResponse.getCategory(),"Personal/Family");
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testMCCforInsuranceContainerWithTdeAcctTypeAndCategoryMergerEnabled() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq = CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(true);
		catReq.setContainer("insurance");
		req.setMccCode(7296L);
		HashMap<Long,String> mccTxnCatMap = new HashMap<Long,String>();
		mccTxnCatMap.put(7296L, "Clothing/Shoes");
		when(mccTxnCatMapCache.getMccTxnCatMap()).thenReturn(mccTxnCatMap);
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		cat.setName("Clothing/Shoes");
		cat.setMergedTxnCatName("Personal/Family");
		String transactionCategoryName="Clothing/Shoes";
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		when (categoryCache.getTransactionCategory(transactionCategoryName,
				catReq.getConfigurations().isMergerEnabled())).thenReturn(cat);
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Clothing/Shoes","Jewelry/Accessories");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		TransactionResponse response = mCCCategorizationServiceImpl.categorise(req, catReq);
		txnResponse = (EnrichedTransactionResponse) response;
		Assert.assertEquals(txnResponse.getGranularCategory(),null); 
		Assert.assertEquals(txnResponse.getCategory(),"Personal/Family");
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testMCCforInsuranceContainerTdeDisabledCategoryMergerEnabled() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq = CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(true);
		catReq.getConfigurations().setMeerkat(false);
		catReq.setContainer("insurance");
		req.setMccCode(7296L);
		HashMap<Long,String> mccTxnCatMap = new HashMap<Long,String>();
		mccTxnCatMap.put(7296L, "Clothing/Shoes");
		when(mccTxnCatMapCache.getMccTxnCatMap()).thenReturn(mccTxnCatMap);
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		cat.setName("Clothing/Shoes");
		cat.setMergedTxnCatName("Personal/Family");
		String transactionCategoryName="Clothing/Shoes";
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		when (categoryCache.getTransactionCategory(transactionCategoryName,
				catReq.getConfigurations().isMergerEnabled())).thenReturn(cat);
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Clothing/Shoes","Jewelry/Accessories");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		TransactionResponse response = mCCCategorizationServiceImpl.categorise(req, catReq);
		txnResponse = (EnrichedTransactionResponse) response;
		Assert.assertEquals(txnResponse.getGranularCategory(),null); 
		Assert.assertEquals(txnResponse.getCategory(),"Personal/Family");
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testMCCforInsuranceContainerWithMergerDisabledTdeEnabled() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq = CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.getConfigurations().setMeerkat(true);
		catReq.setContainer("insurance");
		req.setMccCode(7296L);
		HashMap<Long,String> mccTxnCatMap = new HashMap<Long,String>();
		mccTxnCatMap.put(7296L, "Clothing/Shoes");
		when(mccTxnCatMapCache.getMccTxnCatMap()).thenReturn(mccTxnCatMap);
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		cat.setName("Clothing/Shoes");
		String transactionCategoryName="Clothing/Shoes";
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		when (categoryCache.getTransactionCategory(transactionCategoryName,
				catReq.getConfigurations().isMergerEnabled())).thenReturn(cat);
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Clothing/Shoes","Jewelry/Accessories");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		TransactionResponse response = mCCCategorizationServiceImpl.categorise(req, catReq);
		txnResponse = (EnrichedTransactionResponse) response;
		Assert.assertEquals(txnResponse.getGranularCategory(),null); 
		Assert.assertEquals(txnResponse.getCategory(),"Clothing/Shoes");
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testMCCforInsuranceContainerWithMergerDisabledTdeDisabled() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq = CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("insurance");
		req.setMccCode(7296L);
		HashMap<Long,String> mccTxnCatMap = new HashMap<Long,String>();
		mccTxnCatMap.put(7296L, "Clothing/Shoes");
		when(mccTxnCatMapCache.getMccTxnCatMap()).thenReturn(mccTxnCatMap);
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		cat.setName("Clothing/Shoes");
		String transactionCategoryName="Clothing/Shoes";
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		when (categoryCache.getTransactionCategory(transactionCategoryName,
				catReq.getConfigurations().isMergerEnabled())).thenReturn(cat);
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Clothing/Shoes","Jewelry/Accessories");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		TransactionResponse response = mCCCategorizationServiceImpl.categorise(req, catReq);
		txnResponse = (EnrichedTransactionResponse) response;
		Assert.assertEquals(txnResponse.getGranularCategory(),null); 
		Assert.assertEquals(txnResponse.getCategory(),"Clothing/Shoes");
	}
}
