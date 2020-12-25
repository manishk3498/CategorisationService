package com.manish.categorization.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.manish.categorization.BaseType;
import com.manish.categorization.Container;
import com.manish.categorization.algo.tde.dto.MeerkatResponse.CNNInfo;
import com.manish.categorization.algo.tde.dto.MeerkatResponse.MeerkatTransaction;
import com.manish.categorization.db.GranularCategoryMapping;
import com.manish.categorization.db.SlamBangWord;
import com.manish.categorization.db.TransactionCategory;
import com.manish.categorization.rest.dto.CategorizationRequest;
import com.manish.categorization.rest.dto.Configurations;
import com.manish.categorization.rest.dto.EnrichedTransactionResponse;
import com.manish.categorization.rest.dto.TransactionRequest;
import com.manish.categorization.util.Constants;
import com.manish.categorization.util.LocaleBuilder;
/**
 * 
 * @author smohanty
 *
 */
public class CommonUtil {
	
	
	public static TransactionRequest getTransactionRequest() {
		TransactionRequest request = new TransactionRequest();
		request.setMccCode(6012L);
		request.setBaseType("debit");
		request.setTransactionId(new BigInteger("19"));
		request.setDescription("Other Expenses");	
		request.setTransactionType("loanPayment");
		return request;
	}
	
	public static TransactionRequest getTransactionRequest(String baseType) {
		TransactionRequest request = new TransactionRequest();
		request.setMccCode(6012L);
		request.setBaseType(baseType);
		request.setTransactionId(new BigInteger("19"));
		request.setDescription("Other Expenses");	
		request.setTransactionType("loanPayment");
		return request;
	}
	
	public static CategorizationRequest getCategorizationRequest() {
		CategorizationRequest request = new CategorizationRequest();
		Configurations cnfg = new Configurations();
		cnfg.setMergerEnabled(true);
		cnfg.setMeerkat(true);
		cnfg.setSimpleDescEnabled(true);
		cnfg.setSimpleDescVersion("1.0");
		cnfg.setGeoLocationEnabledInSD(false);
		cnfg.setLocaleStr("en_US");
		cnfg.setLocale(LocaleBuilder.getLocale(cnfg.getLocaleStr()));
		cnfg.setMeerkatUrl("https://10.20.36.200/meerkat/");
		List<String> services = new ArrayList<String>();
		services.add(Constants.CNN_MERCHANT.toString());
		services.add(Constants.CNN_SUBTYPE.toString());
		services.add(Constants.BLOOM_FILTER.toString());
		cnfg.setServices(services);
		request.setContainer(Container.BANK.toString());
		request.setConfigurations(cnfg);
		request.setTdeV2(true);
		request.setRegion(1L);
		request.setCobrandId(10000004L);
		request.setMemId(1000000001L);
		request.setContainer(Container.BANK.toString());
		request.setSumInfoId(11195L);
		List<TransactionRequest> txns = new ArrayList<TransactionRequest>();
		TransactionRequest txnReq = new TransactionRequest();
		txnReq.setAmount(new Double(12.0));
		txnReq.setBaseType(BaseType.DEBIT.toString());
		txnReq.setDate("12/01/2018");
		txnReq.setTransactionId(new BigInteger("1"));
		txnReq.setDescription("WINCOSIN CHILDREN'S HOSPITAL");
		txnReq.setMccCode(6012L);
		txns.add(txnReq);
		request.setTxns(txns);
		return request;
	}
	public static CategorizationRequest getCateRequest(boolean mergerEnabled,boolean meerkat,boolean simpleDescEnabled) {
		CategorizationRequest request = new CategorizationRequest();
		Configurations cnfg = new Configurations();
		cnfg.setMergerEnabled(mergerEnabled);
		cnfg.setMeerkat(meerkat);
		cnfg.setSimpleDescEnabled(simpleDescEnabled);
		cnfg.setSimpleDescVersion("1.0");
		cnfg.setGeoLocationEnabledInSD(false);
		cnfg.setLocaleStr("en_US");
		cnfg.setLocale(LocaleBuilder.getLocale(cnfg.getLocaleStr()));
		cnfg.setMeerkatUrl("https://10.79.8.159/meerkat/");
		List<String> services = new ArrayList<String>();
		services.add(Constants.CNN_MERCHANT.toString());
		services.add(Constants.CNN_SUBTYPE.toString());
		services.add(Constants.BLOOM_FILTER.toString());
		cnfg.setServices(services);
		request.setContainer(Container.BANK.toString());
		request.setConfigurations(cnfg);
		request.setTdeV2(true);
		request.setRegion(1L);
		request.setCobrandId(10000004L);
		request.setMemId(1000000001L);
		request.setContainer(Container.BANK.toString());
		request.setSumInfoId(11195L);
		List<TransactionRequest> txns = new ArrayList<TransactionRequest>();
		TransactionRequest txnReq = new TransactionRequest();
		txnReq.setAmount(new Double(12.0));
		txnReq.setBaseType(BaseType.DEBIT.toString());
		txnReq.setDate("12/01/2018");
		txnReq.setTransactionId(new BigInteger("1"));
		txnReq.setDescription("WINCOSIN CHILDREN'S HOSPITAL");
		txnReq.setMccCode(6012L);
		txns.add(txnReq);
		request.setTxns(txns);
		return request;
	}
	
	public static TransactionCategory getTransactionCategory() {
		TransactionCategory transCategory = new TransactionCategory();
		transCategory.setMergedTxnCatName("Other Expenses");
		transCategory.setName("Other Expenses");
		transCategory.setDisplayName("Other Expenses");
		return transCategory;
		
	}
	public static TransactionCategory getTransactionCategory(String categoryName,String mergedTxnCatName) {
		TransactionCategory transCategory = new TransactionCategory();
		transCategory.setMergedTxnCatName(mergedTxnCatName);
		transCategory.setName(categoryName);
		return transCategory;
	}
	public static GranularCategoryMapping getGranualarCategoryMapping(){
		GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
		granularCategoryMapping.setGranularCategoryId(1161L);
		granularCategoryMapping.setBaseType("debit");
		granularCategoryMapping.setGranularCategoryName("Other Expenses");
		granularCategoryMapping.setMasterCategoryName("Other Expenses");
		return granularCategoryMapping;
	}
	
	public static EnrichedTransactionResponse getEnrichedTransactionResponse() {
		
		EnrichedTransactionResponse enrichedTransactionResponse = new EnrichedTransactionResponse();
		enrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		enrichedTransactionResponse.setMeerkatSubType("Debit");
		enrichedTransactionResponse.setCategorisationSource("MEERKAT");
		enrichedTransactionResponse.setCategory("Automotive/Fuel");
		enrichedTransactionResponse.setCategorisationSourceId("24");
		enrichedTransactionResponse.setGranularCategory("Automotive/Fuel");
		return enrichedTransactionResponse;
		
	}
	public static EnrichedTransactionResponse getEnrichedTransactionResponse(String categoryName) {
		
		EnrichedTransactionResponse enrichedTransactionResponse = new EnrichedTransactionResponse();
		enrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		enrichedTransactionResponse.setMeerkatSubType("Debit");
		enrichedTransactionResponse.setCategorisationSource("MEERKAT");
		enrichedTransactionResponse.setCategory(categoryName);
		enrichedTransactionResponse.setCategorisationSourceId("24");
		enrichedTransactionResponse.setGranularCategory(categoryName);
		return enrichedTransactionResponse;
		
	}
	public static EnrichedTransactionResponse getEnrichedTransactionResponse(String categoryName,String granularCategory) {
		
		EnrichedTransactionResponse enrichedTransactionResponse = new EnrichedTransactionResponse();
		enrichedTransactionResponse.setMeerkatType("Other Withdrawals");
		enrichedTransactionResponse.setMeerkatSubType("Debit");
		enrichedTransactionResponse.setCategorisationSource("MEERKAT");
		enrichedTransactionResponse.setCategory(categoryName);
		enrichedTransactionResponse.setCategorisationSourceId("24");
		enrichedTransactionResponse.setGranularCategory(granularCategory);
		return enrichedTransactionResponse;
		
	}
	public static Map<String,Map<String,SlamBangWord>> getSlamBangWordMockedMap(){
		
		Map<String,Map<String,SlamBangWord>> slamBangWordMap = new ConcurrentHashMap<String, Map<String,SlamBangWord>>();
		for (Container container : Container.values()) {
			String debitContainerKey = container+Constants.HYPHEN+BaseType.DEBIT.toString();
			String creditContainerKey = container+Constants.HYPHEN+BaseType.CREDIT.toString();
			slamBangWordMap.put(debitContainerKey, new ConcurrentHashMap<String, SlamBangWord>());		
			slamBangWordMap.put(creditContainerKey, new ConcurrentHashMap<String, SlamBangWord>());
		}
		String key = "bank-debit";
		slamBangWordMap.put(key, getSlamBangWordToObjMockedMap());
		return slamBangWordMap;
	}
	
	public static Map<String,SlamBangWord> getSlamBangWordToObjMockedMap(){
		Map<String,SlamBangWord> slamBangWordMap = new ConcurrentHashMap<String,SlamBangWord>();
		SlamBangWord slamBangWord = getSlamBangWordMockedObjFor7Eleven();
		slamBangWordMap.put(slamBangWord.getSlamBangWord().toLowerCase(), slamBangWord);
//		24hr fitness
//7-eleven,asda superstor
		return slamBangWordMap;
	}
	
	public static SlamBangWord getSlamBangWordMockedObjFor7Eleven() {
		SlamBangWord slamBangWord = new SlamBangWord();
		slamBangWord.setTransactionType("purchase");
		slamBangWord.setTransactionCategoryName("Groceries");
		slamBangWord.setSumInfoId(null);
		//slamBangWord.setSource(source);
		slamBangWord.setSlamBangWordId(195L);
		slamBangWord.setSlamBangWord("7-eleven");
		slamBangWord.setRegionId(0L);
		slamBangWord.setPriority(1L);
		slamBangWord.setMerchantName("7-Eleven");
		slamBangWord.setMerchantId(177L);
		slamBangWord.setIsDummyMerchant(0L);
		slamBangWord.setDeleted(false);
		slamBangWord.setContainers("bank");
		List<String> containersList = new ArrayList<String>();
		containersList.add("bank");
		slamBangWord.setContainerList(containersList);
		return slamBangWord;
	}
	public static SlamBangWord getSlamBangWordMockedObjFor7Elevenn() {
		SlamBangWord slamBangWord = new SlamBangWord();
		slamBangWord.setTransactionType("purchase");
		slamBangWord.setTransactionCategoryName("Groceries");
		slamBangWord.setSumInfoId(null);
		//slamBangWord.setSource(source);
		slamBangWord.setSlamBangWordId(195L);
		slamBangWord.setSlamBangWord("7-eleven");
		slamBangWord.setRegionId(0L);
		slamBangWord.setPriority(1L);
		slamBangWord.setMerchantName("7-Eleven");
		slamBangWord.setMerchantId(177L);
		slamBangWord.setIsDummyMerchant(0L);
		slamBangWord.setDeleted(false);
		slamBangWord.setContainers("bank");
		List<String> containersList = new ArrayList<String>();
		containersList.add("bank");
		slamBangWord.setContainerList(containersList);
		return slamBangWord;
	}
	public static List<String> getServicesForIndiaRegion(){
		List<String> services = new ArrayList<String>();
		services.add(Constants.CNN_MERCHANT.toString());
		services.add(Constants.CNN_SUBTYPE.toString());
		return services;
	}
	public static List<String> getServicesForUSRegion(){
		List<String> services = new ArrayList<String>();
		services.add(Constants.CNN_MERCHANT.toString());
		services.add(Constants.CNN_SUBTYPE.toString());
		services.add(Constants.BLOOM_FILTER.toString());
		return services;
	}
	public static HttpHeaders gethttpHeaders(){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate");
		return headers;
	}
	public static MeerkatTransaction getMeerkatTransaction(){
		MeerkatTransaction meerkatTransaction = new MeerkatTransaction();
		meerkatTransaction.setType(TestConstants.OTHER_WITHDRAWALS);
		meerkatTransaction.setSubType(TestConstants.DEBIT);
		meerkatTransaction.setMerchant(TestConstants.MERCHANT_53_AUTOMOTIVE);
		meerkatTransaction.setCountry(TestConstants.US);
		meerkatTransaction.setConfidenceScore("64.81");
		meerkatTransaction.setTransactionId(new BigInteger("2"));
		meerkatTransaction.setSourceMerchantId("53automotiveus");
		meerkatTransaction.setGranularCategory(TestConstants.OTHER_EXPENSES);
		List<String> labels = new ArrayList<String>();
		labels.add(TestConstants.AUTOMOTIVE_FUEL);
		meerkatTransaction.setLabels(labels);
		meerkatTransaction.setSource(TestConstants.OTHER);
		CNNInfo debugInfo = new CNNInfo();
		meerkatTransaction.setDebugInfo(debugInfo);
		String [] s = {"0","1"};
		meerkatTransaction.setVendorNameVariation(s);
		return meerkatTransaction;
	}
	public static MeerkatTransaction getMeerkatTransaction(String category,String granularCategory){
		MeerkatTransaction meerkatTransaction = new MeerkatTransaction();
		meerkatTransaction.setType(TestConstants.OTHER_WITHDRAWALS);
		meerkatTransaction.setSubType(TestConstants.DEBIT);
		meerkatTransaction.setMerchant(TestConstants.MERCHANT_53_AUTOMOTIVE);
		meerkatTransaction.setCountry(TestConstants.US);
		meerkatTransaction.setConfidenceScore("64.81");
		meerkatTransaction.setTransactionId(new BigInteger("2"));
		meerkatTransaction.setSourceMerchantId("53automotiveus");
		meerkatTransaction.setGranularCategory(granularCategory);
		List<String> labels = new ArrayList<String>();
		labels.add(category);
		meerkatTransaction.setLabels(labels);
		meerkatTransaction.setSource(TestConstants.OTHER);
		CNNInfo debugInfo = new CNNInfo();
		meerkatTransaction.setDebugInfo(debugInfo);
		String [] s = {"0","1"};
		meerkatTransaction.setVendorNameVariation(s);
		return meerkatTransaction;
	}
	public static MeerkatTransaction getMeerkatTransaction(String categoryName){
		MeerkatTransaction meerkatTransaction = new MeerkatTransaction();
		meerkatTransaction.setType(TestConstants.OTHER_WITHDRAWALS);
		meerkatTransaction.setSubType(TestConstants.DEBIT);
		meerkatTransaction.setMerchant(TestConstants.MERCHANT_53_AUTOMOTIVE);
		meerkatTransaction.setCountry(TestConstants.US);
		meerkatTransaction.setConfidenceScore("64.81");
		meerkatTransaction.setTransactionId(new BigInteger("2"));
		meerkatTransaction.setSourceMerchantId("53automotiveus");
		meerkatTransaction.setGranularCategory(TestConstants.OTHER_EXPENSES);
		List<String> labels = new ArrayList<String>();
		labels.add(categoryName);
		meerkatTransaction.setLabels(labels);
		meerkatTransaction.setSource(TestConstants.OTHER);
		CNNInfo debugInfo = new CNNInfo();
		meerkatTransaction.setDebugInfo(debugInfo);
		String [] s = {"0","1"};
		meerkatTransaction.setVendorNameVariation(s);
		return meerkatTransaction;
	}

}
