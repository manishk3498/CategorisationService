package com.manish.categorization.db;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.reflect.TypeToken;
import com.manish.categorization.repository.TransactionCategoryLabelRepo;
import com.manish.categorization.repository.TransactionCategoryRepo;
import com.manish.categorization.repository.TransactionTypeCategoryRepo;
import com.manish.categorization.repository.dto.CategoryLabelDTO;
import com.manish.categorization.repository.dto.TransactionCategoryDTO;
import com.manish.categorization.repository.dto.TxnCategoryDTO;
import com.manish.categorization.repository.dto.TxnTypeCategoryDTO;
import com.manish.categorization.repository.entity.TransactionCategoryEntity;
import com.manish.categorization.repository.entity.TransactionCategoryLabelEntity;
import com.manish.categorization.repository.entity.TransactionTypeCategoryEntity;
import com.manish.categorization.util.CategorizationRuleParser;
import com.manish.categorization.util.CategorizationRules;

@Component
public class TransactionCategoryCache {

	@Inject
	private TransactionCategoryRepo transactionCategoryRepo;
	
	@Inject
	private TransactionCategoryLabelRepo transactionCategoryLabelRepo;
	
	@Inject
	private TransactionTypeCategoryRepo transactionTypeCategoryRepo;
	
	private static final Logger logger = LogManager.getLogger(TransactionCategoryCache.class);

	Map<String, TransactionCategory> categoryCache = new HashMap<>();
	Map<String, TransactionCategory> labelCache = new HashMap<>();
	Map<String, String> childParentCache = new HashMap<>();
	Map<String,String> txnCatNameToMergedTxnCatName = new HashMap<>();
	Map<String,Long> categoryNameToIdMap = new HashMap<>();
	
	
	@PostConstruct
	public void init() {
		if(logger.isInfoEnabled())
			logger.info("Initializing Transaction Category Cache.");
		cacheData();
		if(logger.isInfoEnabled())
			logger.info("Finished Initializing Transaction Category Cache.");
	}

	public TransactionCategory getTransactionCategory(String categoryName, boolean merged) {
		TransactionCategory category = categoryCache.get(categoryName);
		if (category != null) {
			String parentCategoryName = category.getParentCategoryName();
			if (merged && parentCategoryName != null && !parentCategoryName.trim().isEmpty()) {
				category = categoryCache.get(parentCategoryName);
			}
		}
		return category;
	}
	/**
	 * This method returns the appropriate category name. Logic - 
	 * a) If TDE or Category Merger is enabled then category obj is taken from Label Cache (New Txn Cat Name -> Txn Cat Obj)
	 * else it is taken from category cache (Old Txn Cat Name -> Txn Cat Obj)
	 * @param categoryName
	 * @param merged
	 * @return
	 */
	public TransactionCategory getTransactionCategory(String categoryName, boolean merged,boolean meerkatEnabled) {
		
		TransactionCategory category = null;
		if(merged || meerkatEnabled){
			List<String> labelsList = new ArrayList<String>();
			if(StringUtils.isNotEmpty(categoryName)){
				labelsList.add(categoryName);
			}
			category = getTransactionCategory(labelsList, merged);
		}else{
			category = categoryCache.get(categoryName);
			if (category != null) {
				String parentCategoryName = category.getParentCategoryName();
				if (merged && parentCategoryName != null && !parentCategoryName.trim().isEmpty()) {
					category = categoryCache.get(parentCategoryName);
				}
			}
		}
		
		return category;
	}
	
	public TransactionCategory getTransactionCategory(List<String> labels, boolean merged) {
		Stack<String> labelStack = new Stack<String>();
		String labelName = "";
		for (String label : labels) {
			if (!labelName.isEmpty()) {
				labelName = labelName + ",";
			}
			labelName = labelName + label;
			labelStack.push(labelName);
		}
		TransactionCategory category = null;
		for (String label : labelStack) {
			category = labelCache.get(label.toLowerCase());
			if (category == null) {
				category = categoryCache.get(getCegoryNameForLabel(label));
				if(category == null){
					category = categoryCache.get(label);
				}
			}
			if (category != null && category.isGlobal() && category.isPersonnelCategory()) {
				break;
			}
		}
		if (category != null && merged) {
			String parentCategoryName = category.getParentCategoryName();
			if (parentCategoryName != null && !parentCategoryName.trim().isEmpty()) {
				category = categoryCache.get(parentCategoryName);
			}
		}
		if (category == null) {
			if(logger.isErrorEnabled())
				logger.error(String.format("No Category found for CategoryLabels:%s ", labels.toString()));
		}
		return category;
	}

	@PreDestroy
	public void cleanup() {
		categoryCache.clear();
		childParentCache.clear();
		labelCache.clear();
	}

	private void cacheData() {
		Type listType = new TypeToken<List<TransactionCategory>>() {
		}.getType();
		
		List<TransactionCategory> mergedCategories = new ArrayList<TransactionCategory>();
		List<TxnCategoryDTO> formatedCategories = new ArrayList<TxnCategoryDTO>();
			List<TransactionCategoryDTO> categories = getTxnCategoryList();
			Map<Long, CategoryLabelDTO> labels = getCategoryLabels();
			Map<Long, TxnTypeCategoryDTO> txnTypeCategoryMap = getTxnTypeCategories();
			
			for (TransactionCategoryDTO category : categories) {
				String categoryName = category.getCategoryName();
				CategoryLabelDTO labelsO = labels.get(category.getCategoryId());
				if (labelsO != null) {
					category.setLabels(labelsO.getLabels());
				}
				TxnTypeCategoryDTO txnTypeCategory = txnTypeCategoryMap.get(category.getCategoryId());
				if (txnTypeCategory != null) {
					category.setTransactionTypes(txnTypeCategory.getTxnTypes());
				}
				formatedCategories.add(new TxnCategoryDTO(category));
			}

			//Map contains txnCatName -> List of TxnCategoryDTO (Objects with same name)
			Map<String, List<TxnCategoryDTO>> categoryGroups = new HashMap<>();
			Map<Long, String> categoryIdToNameMap = new HashMap<>();
			for (TxnCategoryDTO category : formatedCategories) {
				List<TxnCategoryDTO> categoriesL = categoryGroups.get(category.getName());
				if (categoriesL == null) {
					categoriesL = new ArrayList<>();
					categoryGroups.put(category.getName(), categoriesL);
				}
				categoryIdToNameMap.put(category.getCategoryId(), category.getName());
				categoriesL.add(category);
			}
			
			for (Map.Entry<String, List<TxnCategoryDTO>> categoryEntry : categoryGroups.entrySet()) {
				String name = categoryEntry.getKey();
				List<TxnCategoryDTO> childs = categoryEntry.getValue();
				TransactionCategory category = new TransactionCategory();
				category.setName(name);
				category.setCategoryType(childs.get(0).getCategoryType());
				Long parentId = childs.get(0).getParentId();
				if (parentId != null && parentId.longValue() != 0) {
					String parentName = categoryIdToNameMap.get(parentId);
					category.setParentCategoryName(parentName);
				}
				if(parentId == null){
					if(txnCatNameToMergedTxnCatName.get(name) != null){
						category.setMergedTxnCatName(txnCatNameToMergedTxnCatName.get(name));
					}
				}
				for (TxnCategoryDTO child : childs) {
					category.setCategoryType(child.getCategoryType());
					Long parentIdT = childs.get(0).getParentId();
					if (parentIdT != null && parentIdT.longValue() != 0) {
						String parentName = categoryIdToNameMap.get(parentIdT);
						category.setParentCategoryName(parentName);
					}
					category.getRegions().add(child.getRegionId());
					if (child.getLabels() != null) {
						category.getTdeLabels().addAll(child.getLabels());
					}
					if (child.getRules() != null) {
						category.getRules().addAll(child.getRules());
					}
					if (child.getTransactionTypes() != null) {
						category.getTransactionTypes().addAll(child.getTransactionTypes());
					}
					if (!category.isSpecificCategory() && child.isSpecific()) {
						category.setSpecificCategory(true);
					}
					if (!category.isSmallBusinessCategory() && child.isSmallBusiness()) {
						category.setSmallBusinessCategory(true);
					}
					if (!category.isPersonnelCategory() && !child.isSmallBusiness()) {
						category.setPersonnelCategory(true);
					}
					
				}
				mergedCategories.add(category);
			}
		
		for (TransactionCategory category : mergedCategories) {
			String parentCategoryName = category.getParentCategoryName();
			categoryCache.put(category.getName(), category);
			Set<String> labelss = category.getTdeLabels();
			if (labelss != null && !labelss.isEmpty()) {
				for (String label : labelss) {
					labelCache.put(label, category);
					labelCache.put(label.toLowerCase(), category);
				}
			}
			if (parentCategoryName != null && !parentCategoryName.trim().isEmpty()) {
				childParentCache.put(category.getName(), parentCategoryName);
			}
		}
	}
	
	private String getCegoryNameForLabel(String label) {
		return label.toLowerCase().replaceAll(" and ", "_").replaceAll(" - ", "_").replaceAll(" / ", "_")
				.replaceAll(" & ", "_").replaceAll("/", "_").replaceAll(" ", "_");
	}

	public static String getCategoryLabel(String categoryName) {
		return categoryName.toLowerCase().replaceAll(" and ", "_").replaceAll(" - ", "_")
				.replaceAll(" / ", "_").replaceAll(" & ", "_").replaceAll("/", "_").replaceAll(" ", "_");
	}
	

	private Map<Long, TxnTypeCategoryDTO> getTxnTypeCategories() {
		Map<Long, TxnTypeCategoryDTO> txnTypeCategories = new HashMap<Long, TxnTypeCategoryDTO>();
		List<TransactionTypeCategoryEntity> transactionTypeCategoryList = transactionTypeCategoryRepo.getTransactionTypeCategoryEntity();
		if(transactionTypeCategoryList != null && transactionTypeCategoryList.size() > 0){
			for (TransactionTypeCategoryEntity transactionTypeCategoryEntity : transactionTypeCategoryList) {

				if(transactionTypeCategoryEntity != null){
					Long categoryId = Long.valueOf(transactionTypeCategoryEntity.getTxnCategoryId());
					
					TxnTypeCategoryDTO txnTypeCategory = txnTypeCategories.get(categoryId);
					String transactionType = transactionTypeCategoryEntity.getTransactionType();
					
					if (txnTypeCategory == null) {
						txnTypeCategory = new TxnTypeCategoryDTO();
						txnTypeCategory.setCategoryId(categoryId);
						txnTypeCategories.put(categoryId, txnTypeCategory);
					}
					txnTypeCategory.addTxnType(transactionType);
				}	
			}
		}
		return txnTypeCategories;
	}

	private Map<Long, CategoryLabelDTO> getCategoryLabels() {
		Map<Long, CategoryLabelDTO> categoryLabels = new HashMap<Long, CategoryLabelDTO>();
		List<TransactionCategoryLabelEntity> transactionCategoryLabelList = transactionCategoryLabelRepo.getTransactionCategoryLabelEntity();
		
		if(transactionCategoryLabelList != null && transactionCategoryLabelList.size() > 0){
			for (TransactionCategoryLabelEntity transactionCategoryLabelEntity : transactionCategoryLabelList) {
				if(transactionCategoryLabelEntity !=null){
					Long categoryId = transactionCategoryLabelEntity.getTxnCategoryId()!=null ? transactionCategoryLabelEntity.getTxnCategoryId():0;
					String label = transactionCategoryLabelEntity.getLabel()!=null?transactionCategoryLabelEntity.getLabel():"";
					CategoryLabelDTO categoryLabel = categoryLabels.get(categoryId);
					if (categoryLabel == null) {
						categoryLabel = new CategoryLabelDTO();
						categoryLabel.setCategoryId(categoryId);
						categoryLabels.put(categoryId, categoryLabel);
					}
					categoryLabel.addLabel(label);
				}
			}
		} 
		return categoryLabels;
	}

	private List<TransactionCategoryDTO> getTxnCategoryList() {
		List<TransactionCategoryDTO> categories = new ArrayList<>();
		List<TransactionCategoryEntity> categoryEntityList = transactionCategoryRepo.getTransactionCategoryEntity();
		
		if(categoryEntityList != null && categoryEntityList.size() > 0){
			for (TransactionCategoryEntity transactionCategoryEntity : categoryEntityList) {
				Long categoryId = transactionCategoryEntity.getTxnCategoryId();
				String categoryName = transactionCategoryEntity.getTxnCategoryName();
				String categoryType = transactionCategoryEntity.getTxnCategoryType();
				Long regionId = transactionCategoryEntity.getRegionId()!=null ? transactionCategoryEntity.getRegionId():1L;
				Long parentId = transactionCategoryEntity.getMergedTxnCategoryId();
				String mergedTxnCatName = transactionCategoryEntity.getMergedTxnCatName();
				Long smallBusiness = transactionCategoryEntity.getSmallBusiness();
				String rule = transactionCategoryEntity.getCategorizationRule();
				
				TransactionCategoryDTO category = new TransactionCategoryDTO();
				category.setCategoryId(categoryId);
				category.setCategoryName(categoryName);
				category.setParentId(parentId);
				category.setMergedTxnCatName(mergedTxnCatName);
				CategorizationRuleParser rulesParser = new CategorizationRuleParser();
				category.setRegionId(regionId);
				category.setRule(rule);
				category.setTransactionCategoryType(categoryType);
				if (rule != null) {
					CategorizationRules rules = rulesParser.unmarshal(rule);
					category.setSpecific("Specific".equals(rules.getPrecision()));
					category.setRules(rules.getRules());
				}else{
					//If no rule is mentioned then the category can be applied
					category.setSpecific(Boolean.TRUE);
				}
				category.setSmallBusiness(smallBusiness != null && smallBusiness.intValue() == 1);
				categories.add(category);
				txnCatNameToMergedTxnCatName.put(categoryName, mergedTxnCatName);
				if(regionId.longValue() == 1L) {
					categoryNameToIdMap.put(transactionCategoryEntity.getTxnCategoryName(), transactionCategoryEntity.getTxnCategoryId());
				}
			}
		}
		return categories;
	}

	public Map<String, String> getTxnCatNameToMergedTxnCatName() {
		return txnCatNameToMergedTxnCatName;
	}

	public Map<String, String> getChildParentCache() {
		return childParentCache;
	}

	public Map<String, Long> getCategoryNameToIdMap() {
		return categoryNameToIdMap;
	}

	public void setCategoryNameToIdMap(Map<String, Long> categoryNameToIdMap) {
		this.categoryNameToIdMap = categoryNameToIdMap;
	}
	
}
