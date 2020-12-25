package com.manish.categorization.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.manish.categorization.BaseType;
import com.manish.categorization.repository.GranularCategoryRepo;
import com.manish.categorization.repository.entity.GranularCategoryEntity;
import com.manish.categorization.sdp.config.CaasConfigBean;

@Component
public class GranularCategoryCache {

	private static final Logger logger = LogManager.getLogger(GranularCategoryCache.class);
	
	Map<String,Map<String,GranularCategoryMapping>> granularCategoryCache = new HashMap<String,Map<String,GranularCategoryMapping>>();
	Map<String, GranularCategoryMapping> debitGranularCategoryCache = new CaseInsensitiveMap<String, GranularCategoryMapping>();
	Map<String, GranularCategoryMapping> creditGranularCategoryCache = new CaseInsensitiveMap<String, GranularCategoryMapping>();
	//Use this map for requests where category merger is disabled
	Map<String,String> catNameTocatchAllGranularCatNameMap = new HashMap<>();
	//Use this map for requests where category merger is enabled
	Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = new HashMap<>();
	
	private Map<String,Long> granularCategoryNameToIdMap = new HashMap<String,Long>();
	
	@Autowired
	TransactionCategoryCache transactionCategoryCache;
	
	@Inject
	GranularCategoryRepo granularCategoryRepo;
	
	@Autowired
	CaasConfigBean caasConfigBean;
	
	@PostConstruct
	public void init() {
		
		if(logger.isInfoEnabled())
			logger.info("Initializing Granular Category Cache.");
		granularCategoryCache.put(BaseType.CREDIT.toString(), debitGranularCategoryCache);
		granularCategoryCache.put(BaseType.DEBIT.toString(), creditGranularCategoryCache);
		cacheData();
		if(logger.isInfoEnabled())
			logger.info("Finished Initializing Granular Category Cache.");
	}


	@PreDestroy
	public void cleanup() {
		granularCategoryCache.clear();
	}

	public void cacheIncrementalGranularCategory() {
		if(logger.isInfoEnabled())
			logger.info("Started loading incremental Granular Category Cache.");
		List<GranularCategoryEntity> granularCategoryEntityIncrementalList = null;
		try {
			granularCategoryEntityIncrementalList = granularCategoryRepo.getIncrementalGranularCategoryEntity();
			populateCatchAllMaps(granularCategoryEntityIncrementalList);
			populateGranularCategoryNameToIdMap(granularCategoryEntityIncrementalList);
			if(granularCategoryEntityIncrementalList != null && granularCategoryEntityIncrementalList.size() > 0){		
				for (GranularCategoryEntity granularCategoryEntity : granularCategoryEntityIncrementalList) {
					GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
					granularCategoryMapping.setGranularCategoryId(granularCategoryEntity.getGranularCategoryId());
					granularCategoryMapping.setGranularCategoryName(granularCategoryEntity.getGranularCategoryName());
					granularCategoryMapping.setMasterCategoryName(granularCategoryEntity.getTransactionCategoryName());
					if(granularCategoryEntity.getTransactionBaseTypeId() == null){
						granularCategoryMapping.setBaseType(BaseType.CREDIT.toString());
						creditGranularCategoryCache.put(granularCategoryEntity.getGranularCategoryName(), granularCategoryMapping);
						granularCategoryMapping.setBaseType(BaseType.DEBIT.toString());
						debitGranularCategoryCache.put(granularCategoryEntity.getGranularCategoryName(), granularCategoryMapping);
					}else{
						if(granularCategoryEntity.getTransactionBaseTypeId().longValue() == 1L){
							granularCategoryMapping.setBaseType(BaseType.CREDIT.toString());
							creditGranularCategoryCache.put(granularCategoryEntity.getGranularCategoryName(), granularCategoryMapping);
						}else if(granularCategoryEntity.getTransactionBaseTypeId().longValue() == 2L){
							granularCategoryMapping.setBaseType(BaseType.DEBIT.toString());
							debitGranularCategoryCache.put(granularCategoryEntity.getGranularCategoryName(), granularCategoryMapping);
						}
					}
				}
				
			}	
		} catch (Exception exception) {
			if(logger.isInfoEnabled()){
				logger.info("Exception while loading incremental granular category to master category mappings from db",exception);
			}	
		}
		
	}
	
	
	private void cacheData() {
		
		if(StringUtils.isNotEmpty(caasConfigBean.getCaas().getUseJsonForLoadingGranularCategories()) && "true".equalsIgnoreCase(caasConfigBean.getCaas().
				getUseJsonForLoadingGranularCategories())){
			Type listType = new TypeToken<List<GranularCategoryMapping>>() {
			}.getType();
			InputStream in = this.getClass().getResourceAsStream("/granularCategoryMappings.js");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.disableHtmlEscaping();
			gsonBuilder.setPrettyPrinting();
			List<GranularCategoryMapping> jsonCategories = gsonBuilder.create().fromJson(reader, listType);
			for (GranularCategoryMapping granularCategory : jsonCategories) {
				String granularCategoryName = granularCategory.getGranularCategoryName();
				String baseType = granularCategory.getBaseType();
				if(BaseType.CREDIT.toString().equals(baseType)){
					creditGranularCategoryCache.put(granularCategoryName, granularCategory);
				}else if(BaseType.DEBIT.toString().equals(baseType)){
					debitGranularCategoryCache.put(granularCategoryName, granularCategory);
				}
			}
		}else{
			List<GranularCategoryEntity> granularCategoryEntityList = null;
			try {
				granularCategoryEntityList = granularCategoryRepo.getGranularCategoryEntity();
			} catch (Exception exception) {
				if(logger.isInfoEnabled()){
					logger.info("Exception while loading granular category to master category mappings from db",exception);
				}	
			}
			//Populate catch all categories for all the categories and merged categories
			populateCatchAllMaps(granularCategoryEntityList);
			//loadOldAndNewGranularCategories(debitGranularCategoryCache,creditGranularCategoryCache);
			loadOldAndNewGranularCategoriesForMar(debitGranularCategoryCache,creditGranularCategoryCache);
			populateGranularCategoryNameToIdMap(granularCategoryEntityList);
			if(granularCategoryEntityList != null && granularCategoryEntityList.size() > 0){		
				for (GranularCategoryEntity granularCategoryEntity : granularCategoryEntityList) {
					GranularCategoryMapping granularCategoryMapping = new GranularCategoryMapping();
					granularCategoryMapping.setGranularCategoryId(granularCategoryEntity.getGranularCategoryId());
					granularCategoryMapping.setGranularCategoryName(granularCategoryEntity.getGranularCategoryName());
					granularCategoryMapping.setMasterCategoryName(granularCategoryEntity.getTransactionCategoryName());
					if(granularCategoryEntity.getTransactionBaseTypeId() == null){
						granularCategoryMapping.setBaseType(BaseType.CREDIT.toString());
						creditGranularCategoryCache.put(granularCategoryEntity.getGranularCategoryName(), granularCategoryMapping);
						granularCategoryMapping.setBaseType(BaseType.DEBIT.toString());
						debitGranularCategoryCache.put(granularCategoryEntity.getGranularCategoryName(), granularCategoryMapping);
					}else{
						if(granularCategoryEntity.getTransactionBaseTypeId().longValue() == 1L){
							granularCategoryMapping.setBaseType(BaseType.CREDIT.toString());
							creditGranularCategoryCache.put(granularCategoryEntity.getGranularCategoryName(), granularCategoryMapping);
						}else if(granularCategoryEntity.getTransactionBaseTypeId().longValue() == 2L){
							granularCategoryMapping.setBaseType(BaseType.DEBIT.toString());
							debitGranularCategoryCache.put(granularCategoryEntity.getGranularCategoryName(), granularCategoryMapping);
						}
					}
				}
				
			}			
			
		}
		
	}
	
	/**
	 * This method is used to load all the new and renamed granular categories to be in sync wrt TDE and DB filer.
	 * @param debitGranularCategoryCache
	 * @param creditGranularCategoryCache
	 */
	public void loadOldAndNewGranularCategories(Map<String, GranularCategoryMapping>debitGranularCategoryCache, 
			Map <String, GranularCategoryMapping>creditGranularCategoryCache) {
		
		Type listType = new TypeToken<List<GranularCategoryMapping>>() {
		}.getType();
		InputStream in = this.getClass().getResourceAsStream("/granularCategory.js");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.disableHtmlEscaping();
		gsonBuilder.setPrettyPrinting();
		List<GranularCategoryMapping> jsonCategories = gsonBuilder.create().fromJson(reader, listType);
		for (GranularCategoryMapping granularCategory : jsonCategories) {
			String granularCategoryName = granularCategory.getGranularCategoryName();
			String baseType = granularCategory.getBaseType();
			granularCategoryNameToIdMap.put(granularCategory.getGranularCategoryName().toLowerCase(), granularCategory.getGranularCategoryId());
			if(BaseType.CREDIT.toString().equals(baseType)){
				creditGranularCategoryCache.put(granularCategoryName, granularCategory);
			}else if(BaseType.DEBIT.toString().equals(baseType)){
				debitGranularCategoryCache.put(granularCategoryName, granularCategory);
			}
		}

	}
	/**
	 * This method is used to load all the new and renamed granular categories to be in sync wrt TDE and DB filer.
	 * @param debitGranularCategoryCache
	 * @param creditGranularCategoryCache
	 */
	private void loadOldAndNewGranularCategoriesForMar(Map<String, GranularCategoryMapping>debitGranularCategoryCache, 
			Map <String, GranularCategoryMapping>creditGranularCategoryCache) {
		
		Type listType = new TypeToken<List<GranularCategoryMapping>>() {
		}.getType();
		InputStream in = this.getClass().getResourceAsStream("/granularCategoryMar2020.js");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.disableHtmlEscaping();
		gsonBuilder.setPrettyPrinting();
		List<GranularCategoryMapping> jsonCategories = gsonBuilder.create().fromJson(reader, listType);
		for (GranularCategoryMapping granularCategory : jsonCategories) {
			String granularCategoryName = granularCategory.getGranularCategoryName();
			String baseType = granularCategory.getBaseType();
			granularCategoryNameToIdMap.put(granularCategory.getGranularCategoryName().toLowerCase(), granularCategory.getGranularCategoryId());
			if(BaseType.CREDIT.toString().equals(baseType)){
				creditGranularCategoryCache.put(granularCategoryName, granularCategory);
			}else if(BaseType.DEBIT.toString().equals(baseType)){
				debitGranularCategoryCache.put(granularCategoryName, granularCategory);
			}
		}

	}

	public GranularCategoryMapping getGranularCategory(String granularCategoryName, boolean merged, String baseType) {
		GranularCategoryMapping granularCategory = null;
		if (granularCategoryName != null && !merged) {
			if (granularCategoryName.equalsIgnoreCase("Wages Paid")
					&& BaseType.CREDIT.toString().equalsIgnoreCase(baseType)) {
				granularCategory = getGranularCategoryMappingForWagesPaidForCredit();
			} else if (granularCategoryName.equalsIgnoreCase("Wages Paid")
					&& BaseType.DEBIT.toString().equalsIgnoreCase(baseType)) {
				granularCategory = getGranularCategoryMappingForWagesPaidForDebit();
			} else if (granularCategoryName.equalsIgnoreCase("Child Support")
					&& BaseType.CREDIT.toString().equalsIgnoreCase(baseType)) {
				granularCategory = creditGranularCategoryCache.get(granularCategoryName);
			} else if (granularCategoryName.equalsIgnoreCase("Child Support")
					&& BaseType.DEBIT.toString().equalsIgnoreCase(baseType)) {
				granularCategory = debitGranularCategoryCache.get(granularCategoryName);
			} else if (granularCategoryName.equalsIgnoreCase("Line of Credit Advance")
					&& BaseType.CREDIT.toString().equalsIgnoreCase(baseType)) {
				granularCategory = creditGranularCategoryCache.get(granularCategoryName);
			} else if (granularCategoryName.equalsIgnoreCase("Line of Credit Advance")
					&& BaseType.DEBIT.toString().equalsIgnoreCase(baseType)) {
				granularCategory = debitGranularCategoryCache.get(granularCategoryName);
			} else {
				granularCategory = creditGranularCategoryCache.get(granularCategoryName);
				if (granularCategory == null) {
					granularCategory = debitGranularCategoryCache.get(granularCategoryName);
				}

			}

		}
		return granularCategory;
	}
	
	private GranularCategoryMapping getGranularCategoryMappingForWagesPaidForCredit(){
		GranularCategoryMapping wagesPaidMapping = new  GranularCategoryMapping();
		wagesPaidMapping.setGranularCategoryId(1160l);
		wagesPaidMapping.setBaseType("credit");
		wagesPaidMapping.setGranularCategoryName("Wages Paid");
		wagesPaidMapping.setMasterCategoryName("Paychecks/Salary");
		return wagesPaidMapping;
	}
	
	private GranularCategoryMapping getGranularCategoryMappingForWagesPaidForDebit(){
		GranularCategoryMapping wagesPaidMapping = new  GranularCategoryMapping();
		wagesPaidMapping.setGranularCategoryId(1160l);
		wagesPaidMapping.setBaseType("debit");
		wagesPaidMapping.setGranularCategoryName("Wages Paid");
		wagesPaidMapping.setMasterCategoryName("Wages Paid");
		return wagesPaidMapping;
	}
	
	/**
	 * This method populates the default/catch all granular category for the transaction categories 
	 * both for merged categories as well as normal transaction categories
	 * @param granularCategoryEntityList
	 */
	private void populateCatchAllMaps(List<GranularCategoryEntity> granularCategoryEntityList){
		
		Map<String,String> childCatNameToMergedCatNameCache = transactionCategoryCache.getTxnCatNameToMergedTxnCatName();
		Map<String,String> childParentCache = transactionCategoryCache.getChildParentCache();
		if(granularCategoryEntityList != null && granularCategoryEntityList.size() > 0){
			for (GranularCategoryEntity granularCategoryEntity : granularCategoryEntityList) {
				Long isCatchAllCat = granularCategoryEntity.getIsCatchAllCat();
				Long isMergedCatchAll = granularCategoryEntity.getIsMergedCatchAllCat();
				String txnCatName = granularCategoryEntity.getTransactionCategoryName();
				String mergedTxnCatName = childCatNameToMergedCatNameCache.get(txnCatName);
				if(mergedTxnCatName == null){
					//Please note that parentCategoryName contains the legacy name and not the merged name like - Automotive Expenses will be present instead of Automotive/Fuel
					String parentCategoryName = childParentCache.get(txnCatName);
					mergedTxnCatName = childCatNameToMergedCatNameCache.get(parentCategoryName);
				}
				String granularCategory = granularCategoryEntity.getGranularCategoryName();
				if(isCatchAllCat != null && isCatchAllCat.longValue() == 1L){
					catNameTocatchAllGranularCatNameMap.put(txnCatName,granularCategory);
				}
				if(isMergedCatchAll != null && isMergedCatchAll.longValue() == 1L){
					mergedCatNameTocatchAllGranularCatNameMap.put(mergedTxnCatName, granularCategory);
				}
			}
		}
		
	}
	private void populateGranularCategoryNameToIdMap(List<GranularCategoryEntity> granularCategoryEntityList){
		if(granularCategoryEntityList != null && granularCategoryEntityList.size() > 0){
			for (GranularCategoryEntity granularCategoryEntity : granularCategoryEntityList) {
				granularCategoryNameToIdMap.put(granularCategoryEntity.getGranularCategoryName().toLowerCase(), granularCategoryEntity.getGranularCategoryId());
			}
		}
	}


	public Map<String, String> getCatNameTocatchAllGranularCatNameMap() {
		return catNameTocatchAllGranularCatNameMap;
	}


	public void setCatNameTocatchAllGranularCatNameMap(
			Map<String, String> catNameTocatchAllGranularCatNameMap) {
		this.catNameTocatchAllGranularCatNameMap = catNameTocatchAllGranularCatNameMap;
	}


	public Map<String, String> getMergedCatNameTocatchAllGranularCatNameMap() {
		return mergedCatNameTocatchAllGranularCatNameMap;
	}


	public void setMergedCatNameTocatchAllGranularCatNameMap(
			Map<String, String> mergedCatNameTocatchAllGranularCatNameMap) {
		this.mergedCatNameTocatchAllGranularCatNameMap = mergedCatNameTocatchAllGranularCatNameMap;
	}


	public Map<String, Long> getGranularCategoryNameToIdMap() {
		return granularCategoryNameToIdMap;
	}


	public void setGranularCategoryNameToIdMap(
			Map<String, Long> granularCategoryNameToIdMap) {
		this.granularCategoryNameToIdMap = granularCategoryNameToIdMap;
	}
	
}
