package com.manish.categorization.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.manish.categorization.algo.tde.dto.MeerkatResponse;
import com.manish.categorization.db.GranularCategoryCache;
import com.manish.categorization.db.HlMasterCategoryMappingCache;
import com.manish.categorization.db.TransactionCategoryCache;
import com.manish.categorization.db.TransactionClassificationCache;
import com.manish.categorization.rest.dto.EnrichedTransactionResponse;
import com.manish.categorization.rest.dto.TransactionResponse;
/**
 * 
 * @author mgarg
 *
 */
public class CommonHelper {

	private static final Logger logger = LogManager.getLogger(CommonHelper.class);
	/**
	 * This method returns the default/catch all cateogory to a transaction based on whether TDE is enabled or not
	 * Also granular category is applied only to Bank and Card container.
	 * @param mergerEnabled
	 * @param tdeEnabled
	 * @param txnResponse
	 */
	public static String getDefaultGranularCategory(GranularCategoryCache granularCategoryCache,boolean mergerEnabled,boolean tdeEnabled,EnrichedTransactionResponse txnResponse) {
		
		Map<String,String> catNameTocatchAllGranularCatNameMap = granularCategoryCache.getCatNameTocatchAllGranularCatNameMap();
		Map<String,String> mergedCatNameTocatchAllGranularCatNameMap = granularCategoryCache.getMergedCatNameTocatchAllGranularCatNameMap();
		String granularCategory = null;
		if(tdeEnabled){
			if(granularCategoryCache != null){
				if(mergerEnabled){
					granularCategory = mergedCatNameTocatchAllGranularCatNameMap.get(txnResponse.getCategory());
				}else{
					granularCategory = catNameTocatchAllGranularCatNameMap.get(txnResponse.getCategory());
				}
			}
		}
		if(StringUtils.isNotEmpty(granularCategory)){
			granularCategory = granularCategory.toLowerCase();
		}else {
			if(txnResponse!=null) {
				logger.info("Default Granular Category is coming as null for Category: " + txnResponse.getCategory());
			}
		}
		return granularCategory;
	}
	/**
	 * This function is used to get the TDE version from the Meerkat Response
	 * @param meerkatResponseList
	 * @return
	 */
	public static boolean isTde2(List<MeerkatResponse> meerkatResponseList){
		boolean isTde2Version = Boolean.FALSE;
		if(meerkatResponseList != null && meerkatResponseList.size() > 0){
			MeerkatResponse meerkatResponse = meerkatResponseList.get(0);
			if(meerkatResponse.getData() != null){
				String version = meerkatResponse.getData().getVersion();
				if(version != null && version.contains(Constants.TDE2_VERSION)){
					isTde2Version = Boolean.TRUE;
				}
			}
		}
		return isTde2Version;
	}
	
	/**
	 * This method is used to nullify all the meerkat merchant fields if vendor name  is present in the request and the value for the 
	 * vendor name variation is set to 3
	 * @param txnResponse
	 */
	 public static void nullifyMeerkatMerchantFields(EnrichedTransactionResponse txnResponse) {
		 	txnResponse.setMerchant(null);
			txnResponse.setStreet(null);
			txnResponse.setCity(null);
			txnResponse.setState(null);
			txnResponse.setZip(null);
			txnResponse.setCountry(null);
			txnResponse.setLongitude(null);
			txnResponse.setLatitude(null);
			txnResponse.setFaxNumber(null);
			txnResponse.setChainName(null);
			txnResponse.setNeighbourhood(null);
			txnResponse.setPhoneNumber(null);
			txnResponse.setSourceMerchantId(null);
			txnResponse.setStoreId(null);
			txnResponse.setWebsite(null);
			txnResponse.setMerchantSource(null);
			txnResponse.setMeerkatMerchant(null);
			txnResponse.setPostalCode(null);
			txnResponse.setEmail(null);
			txnResponse.setLogoEndpoint(null);
		 
	 }
	 /**
	  * This is to get comma separated category labels
	  * @param categoryLabelsList
	  * @return
	  */
	 public static String getCategoryLabelsAsString(List<String> categoryLabelsList){
		 String categoryLabels = "";
		 if(categoryLabelsList != null && categoryLabelsList.size() > 0){
			 for (String categoryLabel : categoryLabelsList) {
				categoryLabels += (categoryLabel + Constants.COMMA);
			}
		 }
		 if(StringUtils.isNotEmpty(categoryLabels)){
			 categoryLabels = categoryLabels.substring(0, categoryLabels.length()-1);
		 }
		 return categoryLabels;
	 }
	 /**
	  * This method is used to populate the corresponding ids for transaction category, hl transaction category and granular category
	  * @param transactionCategoryCache
	  * @param hlMasterCategoryMappingCache
	  * @param granularCategoryCache
	  * @param transactionResponseList
	  */
	 public static void populateIds(TransactionCategoryCache transactionCategoryCache,HlMasterCategoryMappingCache hlMasterCategoryMappingCache,
			 GranularCategoryCache granularCategoryCache, TransactionClassificationCache transactionClassificationCache,List<TransactionResponse> transactionResponseList,String uniqueTrackingId){
		 Map<String,Long> granularCategoryNameToIdMap = granularCategoryCache.getGranularCategoryNameToIdMap();
		 Map<String,Long> transactionCategoryNameToIdMap = transactionCategoryCache.getCategoryNameToIdMap();
		 Map<Long,String> hlTransactionCategoryNameToIdMap = hlMasterCategoryMappingCache.getHighLevelCategoryIdToNameMap();
		 Map<Long,Long> txnCatIdToHlCatIdMap = hlMasterCategoryMappingCache.getTxnCatIdToHlCatIdMap();
		 Map<String,Long> meerkatTxnTypeToIdMap = transactionClassificationCache.getMeerkatTxnTypeToIdMap();
		 Map<String,Long> meerkatTxnSubTypeToIdMap =transactionClassificationCache.getMeerkatTxnSubTypeToIdMap();
		 if(transactionResponseList != null && transactionResponseList.size() > 0){
			 int emptyMeerkatTxnTypeIdCount=0;
			 int emptyMeerkatTxnSubTypeIdCount=0;
			 int emptyGranularCategoryIdCount=0;
			 int emptyCategoryIdCount=0;
			 int emptyHighlevelCatgeroyIdCount=0;
			 for (TransactionResponse response : transactionResponseList) {
				 if(meerkatTxnTypeToIdMap !=null && meerkatTxnSubTypeToIdMap !=null && response instanceof EnrichedTransactionResponse ) {
					 EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) response;					 
					 String meerkatType=enrichedResponse.getMeerkatType();
					 String meerkatSubType=enrichedResponse.getMeerkatSubType();
					 if(StringUtils.isNotEmpty(meerkatType)){
							Long meerkatTxnTypeId = meerkatTxnTypeToIdMap.get(meerkatType);
							if(meerkatTxnTypeId != null){
								enrichedResponse.setMeerkatTxnTypeId(meerkatTxnTypeId);
							}
							else {
								emptyMeerkatTxnTypeIdCount++;
							}
						}
						if(StringUtils.isNotEmpty(meerkatSubType)){
							Long meerkatTxnSubTypeId = meerkatTxnSubTypeToIdMap.get(meerkatSubType);
							if(meerkatTxnSubTypeId != null){
								enrichedResponse.setMeerkatTxnSubTypeId(meerkatTxnSubTypeId);
							}else {
								emptyMeerkatTxnSubTypeIdCount++;
							}
						}
				 }
				if(granularCategoryNameToIdMap != null && response instanceof EnrichedTransactionResponse){
					EnrichedTransactionResponse enrichedResponse = (EnrichedTransactionResponse) response;
					String granularCategory = enrichedResponse.getGranularCategory();
					
					if(StringUtils.isNotEmpty(granularCategory)){
						Long granularCategoryId = granularCategoryNameToIdMap.get(granularCategory.toLowerCase());
						if(granularCategoryId != null){
							enrichedResponse.setGranularCategoryId(granularCategoryId);
						}else {
							emptyGranularCategoryIdCount++;
						}
					}
					
				}
				if(transactionCategoryCache != null){
					String category = response.getCategory();
					if(StringUtils.isNotEmpty(category)){
						Long categoryId = transactionCategoryNameToIdMap.get(category);
						if(categoryId != null){
							response.setCategoryId(categoryId);
						}else {
							emptyCategoryIdCount++;
						}
					}
				}
				
				if(txnCatIdToHlCatIdMap != null){
					Long categoryId = response.getCategoryId();
					if(categoryId != null){
						Long hlCategoryId  = txnCatIdToHlCatIdMap.get(categoryId);
						if(hlCategoryId != null){
							response.setHlCategoryId(hlCategoryId);
							if(hlTransactionCategoryNameToIdMap !=null) {
								String hlCategory=hlTransactionCategoryNameToIdMap.get(hlCategoryId);
								if(StringUtils.isNotEmpty(hlCategory)) {
									response.setHlCategory(hlCategory);
								}
							}
						}else {
							emptyHighlevelCatgeroyIdCount++;
							
						}
					}
				}	
			}
			 if(emptyHighlevelCatgeroyIdCount>0) {
				 logger.info("The Number of High Level Category Id null for Unique Txn Id:: " + uniqueTrackingId + " are "+ emptyHighlevelCatgeroyIdCount );
			 }
			 if(emptyCategoryIdCount>0) {
				 logger.info("The Number of  Category Id null for Unique Txn Id:: " + uniqueTrackingId + " are "+ emptyCategoryIdCount );
			 }
			 if(emptyGranularCategoryIdCount>0) {
				 logger.info("The Number of Granular Category Id null for Unique Txn Id:: " + uniqueTrackingId + " are "+ emptyGranularCategoryIdCount );
			 }
			 if(emptyMeerkatTxnSubTypeIdCount>0) {
				 logger.info("The Number of MeerkatTxnSubType Id null for Unique Txn Id:: " + uniqueTrackingId + " are "+ emptyMeerkatTxnSubTypeIdCount );
			 }
			 if(emptyMeerkatTxnTypeIdCount>0) {
				 logger.info("The Number of MeerkatTxnType Id null for Unique Txn Id:: " + uniqueTrackingId + " are "+ emptyMeerkatTxnSubTypeIdCount );
			 }
		 }	 
	 } 
}
 