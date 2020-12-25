package com.manish.categorization.algo.legacy;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorCompletionService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.manish.categorization.algo.legacy.LegacyCategorisationServiceImpl;
import com.manish.categorization.algo.mcc.MCCCategorizationService;
import com.manish.categorization.db.GranularCategoryCache;
import com.manish.categorization.db.InvTransTypeCatMapCache;
import com.manish.categorization.db.SlamBangWord;
import com.manish.categorization.db.SlamBangWordCache;
import com.manish.categorization.db.TransactionCategory;
import com.manish.categorization.db.TransactionCategoryCache;
import com.manish.categorization.rest.AbstractTest;
import com.manish.categorization.rest.dto.CategorizationRequest;
import com.manish.categorization.rest.dto.EnrichedTransactionResponse;
import com.manish.categorization.rest.dto.TransactionRequest;
import com.manish.categorization.rest.dto.TransactionResponse;
import com.manish.categorization.sdp.config.CaasConfigBean;
import com.manish.categorization.util.CommonUtil;

public class LegacyCategorisationServiceImplTest extends AbstractTest {
	
	@Mock
	TransactionCategoryCache categoryCache;
	
	@Mock
	InvTransTypeCatMapCache invTransTypeCatMapCache;
	
	@Mock
	GranularCategoryCache granularCategoryCache;
	
	@Mock
	ExecutorCompletionService<List<TransactionResponse>> legacyExecutorCompletionService;
	
	@Mock
	MCCCategorizationService mccCategorizationService;
	
	@InjectMocks@Spy
	LegacyCategorisationServiceImpl legacyCategorisationServiceImpl;
	
	@Mock
	SlamBangWordCache slamBangWordCache;
	
	@Mock
	Map<String,Map<String,SlamBangWord>> slamBangWordMap;
	
	@Mock
	CaasConfigBean caasConfigBean;
	
	private static final String METHOD = "prioritizationAlgorithm";
	
	@Override
	@Before
	public void setUp() {
      super.setUp();
      MockitoAnnotations.initMocks(this);
      
	}
	
	@Test
	public void testCategorisationForInvestmentWithoutCategoryMerger() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("stocks");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		cat.setName("Loans");
		cat.setMergedTxnCatName("Loans");
		String transactionCategoryName="Loans";
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(slamBangWordMap);
		Map<String,String> txnTypeToCatMap = new HashMap<String,String>();
		txnTypeToCatMap.put("loanpayment", "Loans");
		when(invTransTypeCatMapCache.getTxnTypeToTxnCatMap()).thenReturn(txnTypeToCatMap);
		when (categoryCache.getTransactionCategory(transactionCategoryName,
				false)).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategorisationSource(), "INVESTMENT_TRANSACTION_TYPE_MAPPING");
		junit.framework.Assert.assertEquals(txnResponse.getCategory(), "Loans");	
	}
	
	@Test
	public void testCategorisationForInvestmentWithTdeDisabled() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.getConfigurations().setMeerkat(false);
		catReq.setContainer("stocks");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		cat.setName("Loans");
		cat.setMergedTxnCatName("Loans");
		String transactionCategoryName="Loans";
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Loans","Other Loans");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Loans","Other Loans");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(slamBangWordMap);
		Map<String,String> txnTypeToCatMap = new HashMap<String,String>();
		txnTypeToCatMap.put("loanpayment", "Loans");
		when(invTransTypeCatMapCache.getTxnTypeToTxnCatMap()).thenReturn(txnTypeToCatMap);
		when (categoryCache.getTransactionCategory(transactionCategoryName,
				false)).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategorisationSource(), "INVESTMENT_TRANSACTION_TYPE_MAPPING");
		junit.framework.Assert.assertEquals(txnResponse.getCategory(), "Loans");
		junit.framework.Assert.assertEquals(txnResponse.getGranularCategory(), null);	
	}
	
	@Test
	public void testCategorisationForInvestmentWithTdeEnabled() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("stocks");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		cat.setName("Loans");
		cat.setMergedTxnCatName("Loans");
		String transactionCategoryName="Loans";
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Loans","Other Loans");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Loans","Other Loans");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(slamBangWordMap);
		Map<String,String> txnTypeToCatMap = new HashMap<String,String>();
		txnTypeToCatMap.put("loanpayment", "Loans");
		when(invTransTypeCatMapCache.getTxnTypeToTxnCatMap()).thenReturn(txnTypeToCatMap);
		when (categoryCache.getTransactionCategory(transactionCategoryName,
				false)).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategorisationSource(), "INVESTMENT_TRANSACTION_TYPE_MAPPING");
		junit.framework.Assert.assertEquals(txnResponse.getCategory(), "Loans");
		junit.framework.Assert.assertEquals(txnResponse.getGranularCategory(), "other loans");	
	}
	@Test
	public void testCategorisationForInvestment() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.setContainer("stocks");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		cat.setName("Loans");
		cat.setMergedTxnCatName("Loans");
		String transactionCategoryName="Loans";
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(slamBangWordMap);
		Map<String,String> txnTypeToCatMap = new HashMap<String,String>();
		txnTypeToCatMap.put("loanpayment", "Loans");
		when(invTransTypeCatMapCache.getTxnTypeToTxnCatMap()).thenReturn(txnTypeToCatMap);
		when (categoryCache.getTransactionCategory(transactionCategoryName,
				true)).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategorisationSource(), "INVESTMENT_TRANSACTION_TYPE_MAPPING");	
		junit.framework.Assert.assertEquals(txnResponse.getCategory(), "Loans");
	}
	
	@Test
	public void testCategorisationForBankWithMergerDisabled() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("bank");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategory(), "Groceries");
		junit.framework.Assert.assertEquals(txnResponse.getKeyword(), "7-eleven");
		
		
	}
	@Test
	public void testCategorisationForBankWithMergerEnabled() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.setContainer("bank");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		cat.setMergedTxnCatName("Groceries");
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategory(), "Groceries");
		junit.framework.Assert.assertEquals(txnResponse.getSimpleDescription(), "7-Eleven");
		
		
	}
	@Test
	public void testCategorisationForBank() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.setContainer("bank");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		Map<String,String> txnTypeToCatMap = new HashMap<String,String>();
		txnTypeToCatMap.put("loanpayment", "Loans");
		when(invTransTypeCatMapCache.getTxnTypeToTxnCatMap()).thenReturn(txnTypeToCatMap);
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategorisationSource(), "LEGACY");
		
		
	}
	@Test
	public void testCategorisationForCardWithMergerEnabled() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.setContainer("credits");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		cat.setMergedTxnCatName("Groceries");
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategory(), "Groceries");
		junit.framework.Assert.assertEquals(txnResponse.getSimpleDescription(), "7-Eleven");
		
		
	}
	
	
	@Test
	public void testCategorisationForCardWithMergerDisabled() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("credits");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategory(), "Groceries");
		junit.framework.Assert.assertEquals(txnResponse.getKeyword(), "7-eleven");
		
		
	}
	@Test
	public void testCategorisationForInvestmentWithMergerDisabled() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("stocks");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategory(), "Groceries");
		junit.framework.Assert.assertEquals(txnResponse.getKeyword(), "7-eleven");
		
		
	}
	
	@Test
	public void testCategorisationForInvestmentWithMergerEnabled() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.setContainer("loans");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		cat.setMergedTxnCatName("Groceries");
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategory(), "Groceries");
		junit.framework.Assert.assertEquals(txnResponse.getSimpleDescription(), "7-Eleven");
		
		
	}
	
	@Test
	public void testCategorisationForInsuranceWithMergerEnabled() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.setContainer("insurance");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		cat.setMergedTxnCatName("Groceries");
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategory(), "Groceries");
		junit.framework.Assert.assertEquals(txnResponse.getSimpleDescription(), "7-Eleven");
		
		
	}
	@Test
	public void testCategorisationForInsuranceWithMergerDisabled() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("insurance");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategory(), "Groceries");
		junit.framework.Assert.assertEquals(txnResponse.getKeyword(), "7-eleven");
		
		
	}
	
	@Test
	public void testCategorisationForInsuranceWithDummyMerchant() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("insurance");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		hit.setIsDummyMerchant(1L);
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getMerchant(), null);
		junit.framework.Assert.assertEquals(txnResponse.getMerchantId(), null);
		
		
	}
	
	@Test
	public void testSimpleDescForInsuranceWithoutDummyMerchant() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("insurance");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getSimpleDescription(), "7-Eleven");		
		
	}
	
	@Test
	public void testSimpleDescForInsuranceWithDummyMerchant() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("insurance");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		hit.setIsDummyMerchant(1L);
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getSimpleDescription(), null);		
		
	}
	
	@Test
	public void testUncategorized() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("insurance");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		hit.setIsDummyMerchant(1L);
		hit.setTransactionCategoryName("Uncategorized");
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategory(), "Uncategorized");		
		
	}
	
	@Test
	public void testUncategorizedForCreditCardContainer() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		req.setBaseType("credit");
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("credits");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		hit.setIsDummyMerchant(1L);
		hit.setTransactionCategoryName("Uncategorized");
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategory(), "Other Income");		
		
	}
	@Test
	public void testUncategorizedForBankContainerWithBaseTypeCredit() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		req.setBaseType("credit");
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("credits");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		hit.setIsDummyMerchant(1L);
		hit.setTransactionCategoryName("Uncategorized");
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategory(), "Other Income");		
		
	}
	
	@Test
	public void testUncategorizedForBankContainerWithTDEEnabled() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		req.setDate("12/01/2018");
		req.setBaseType("credit");
		req.setAmount(new Double(12.0));
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.getConfigurations().setMeerkat(true);
		catReq.setContainer("bank");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		hit.setIsDummyMerchant(1L);
		hit.setTransactionCategoryName("Uncategorized");
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse, null);		
		
	}
	
	@Test
	public void testUncategorizedWithTDEEnabledWithDiffBaseType() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		req.setDate("12/01/2018");
		req.setBaseType("edit");
		req.setAmount(new Double(12.0));
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.getConfigurations().setMeerkat(true);
		catReq.setContainer("bank");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		hit.setIsDummyMerchant(1L);
		hit.setTransactionCategoryName("Uncategorized");
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategory(), "Uncategorized");		
		
	}
	
	@Test
	public void testUncategorizedWithEmptySlamBangWord() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		req.setDate("12/01/2018");
		req.setBaseType("edit");
		req.setAmount(new Double(12.0));
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.getConfigurations().setMeerkat(true);
		catReq.setContainer("bank");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit =new SlamBangWord();
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategory(), "Uncategorized");		
		
	}
	@Test
	public void testUncategorizedForDebitCardContainer() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("credits");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		hit.setIsDummyMerchant(1L);
		hit.setTransactionCategoryName("Uncategorized");
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategory(), "Other Expenses");		
		
	}
	@Test
	public void testUncategorizedForBankContainerWithBaseTypeDebit() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("bank");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		hit.setIsDummyMerchant(1L);
		hit.setTransactionCategoryName("Uncategorized");
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategory(), "Other Expenses");		
		
	}
	
	@Test
	public void testCategorySourceForDebitCardContainer() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("credits");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		hit.setIsDummyMerchant(1L);
		hit.setTransactionCategoryName("Uncategorized");
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategorisationSource(), "NONE");	
		junit.framework.Assert.assertEquals(txnResponse.getCategorisationSourceId(), "0");	
		
	}
	
	@Test
	public void testCategoryForRefundAndAdjustment() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.setContainer("credits");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		hit.setIsDummyMerchant(1L);
		hit.setTransactionCategoryName("Refunds/Adjustments");
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategory(), "Refunds/Adjustments");	
		
	}
	
	@Test
	public void testCategoryForRefundAndAdjustmentWithMCC() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.getConfigurations().setMccRule(2L);
		catReq.setContainer("credits");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Other Expenses","Other Expenses");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		hit.setIsDummyMerchant(1L);
		hit.setTransactionCategoryName("Refunds/Adjustments");
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getCategory(), "Refunds/Adjustments");	
		
	}
	
	@Test
	public void testGranularCategoryForRefundAndAdjustment() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.getConfigurations().setMccRule(2L);
		catReq.setContainer("credits");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Refunds/Adjustments","Returned Purchase");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Refunds/Adjustments","Returned Purchase");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		hit.setIsDummyMerchant(1L);
		hit.setTransactionCategoryName("Refunds/Adjustments");
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getGranularCategory(), "returned purchase");	
		
	}
	
	@Test
	public void testGranularCategoryForInvalidContainer() {
		
		TransactionRequest req = CommonUtil.getTransactionRequest();
		CategorizationRequest catReq =CommonUtil.getCategorizationRequest();
		catReq.getConfigurations().setMergerEnabled(false);
		catReq.getConfigurations().setMccRule(2L);
		catReq.setContainer("test");
		TransactionCategory cat = CommonUtil.getTransactionCategory();
		EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();
		Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		catNameTocatchAllGranularCatNameMap.put("Refunds/Adjustments","Returned Purchase");
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<String,String>();
		mergedCatNameTocatchAllGranularCatNameMap.put("Refunds/Adjustments","Returned Purchase");
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap()).thenReturn(mergedCatNameTocatchAllGranularCatNameMap);
		when(granularCategoryCache.getCatNameTocatchAllGranularCatNameMap()).thenReturn(catNameTocatchAllGranularCatNameMap);
		when(slamBangWordCache.getSlamBangWordMap()).thenReturn(CommonUtil.getSlamBangWordMockedMap());
		SlamBangWord hit = CommonUtil.getSlamBangWordMockedObjFor7Eleven();
		hit.setIsDummyMerchant(1L);
		hit.setTransactionCategoryName("Refunds/Adjustments");
		doReturn(hit).when(legacyCategorisationServiceImpl).prioritizationAlgorithm(Mockito.anyObject(),Mockito.anyObject(),
				Mockito.anySet(),Mockito.anyObject(),Mockito.anyList(),Mockito.anyBoolean());
		when (categoryCache.getTransactionCategory(Mockito.anyString(),
				Mockito.anyBoolean())).thenReturn(cat);
		TransactionResponse response = legacyCategorisationServiceImpl.categorise(catReq, req);
		txnResponse = (EnrichedTransactionResponse) response;
		junit.framework.Assert.assertEquals(txnResponse.getGranularCategory(), null);	
		
	}
	

}
