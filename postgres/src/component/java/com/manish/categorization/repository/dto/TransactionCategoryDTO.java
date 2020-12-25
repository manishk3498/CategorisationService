package com.manish.categorization.repository.dto;


import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.manish.categorization.db.ContainerBaseTypeRule;

public class TransactionCategoryDTO {
	private long categoryId;
	private String categoryName;
	private String categoryLabel;
	private String mergedTxnCatName;
	private String transactionCategoryType;
	private Long regionId;
	private Long parentId;
	private boolean isSmallBusiness;
	private boolean specific;
	private String rule;
	private List<String> labels;
	private List<ContainerBaseTypeRule> rules;
	private Set<String> transactionTypes = new TreeSet<String>();

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public String getCategoryLabel() {
		return categoryLabel;
	}

	public void setCategoryLabel(String categoryLabel) {
		this.categoryLabel = categoryLabel;
	}

	public List<ContainerBaseTypeRule> getRules() {
		return rules;
	}

	public Set<String> getTransactionTypes() {
		return transactionTypes;
	}


	public void setTransactionTypes(Set<String> transactionTypes) {
		this.transactionTypes = transactionTypes;
	}

	public void setRules(List<ContainerBaseTypeRule> rules) {
		this.rules = rules;
	}

	public boolean isSpecific() {
		return specific;
	}

	public void setSpecific(boolean specific) {
		this.specific = specific;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getTransactionCategoryType() {
		return transactionCategoryType;
	}

	public void setTransactionCategoryType(String transactionCategoryType) {
		this.transactionCategoryType = transactionCategoryType;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public boolean isSmallBusiness() {
		return isSmallBusiness;
	}

	public void setSmallBusiness(boolean isSmallBusiness) {
		this.isSmallBusiness = isSmallBusiness;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getMergedTxnCatName() {
		return mergedTxnCatName;
	}

	public void setMergedTxnCatName(String mergedTxnCatName) {
		this.mergedTxnCatName = mergedTxnCatName;
	}
	
}

