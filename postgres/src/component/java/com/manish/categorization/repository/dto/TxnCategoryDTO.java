package com.manish.categorization.repository.dto;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.manish.categorization.db.ContainerBaseTypeRule;

public class TxnCategoryDTO {
	private long categoryId;
	private String name;
	private String mergedTxnCatName;
	private String categoryType;
	private Long regionId;
	private Long parentId;
	private boolean smallBusiness;
	private boolean specific;
	private List<String> labels = new ArrayList<String>();
	private List<ContainerBaseTypeRule> rules = new ArrayList<>();
	private Set<String> transactionTypes = new TreeSet<String>();

	public TxnCategoryDTO(TransactionCategoryDTO category) {
		super();
		this.categoryId = category.getCategoryId();
		this.name = category.getCategoryName();
		this.mergedTxnCatName = category.getMergedTxnCatName();
		this.categoryType = category.getTransactionCategoryType();
		this.regionId = category.getRegionId();
		this.parentId = category.getParentId();
		this.smallBusiness = category.isSmallBusiness();
		this.specific = category.isSpecific();
		this.labels = category.getLabels();
		this.rules = category.getRules();
		this.transactionTypes = category.getTransactionTypes();
	}

	public TxnCategoryDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getCategoryId() {
		return categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
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
		return smallBusiness;
	}

	public void setSmallBusiness(boolean smallBusiness) {
		this.smallBusiness = smallBusiness;
	}

	public boolean isSpecific() {
		return specific;
	}

	public void setSpecific(boolean specific) {
		this.specific = specific;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public List<ContainerBaseTypeRule> getRules() {
		return rules;
	}

	public void setRules(List<ContainerBaseTypeRule> rules) {
		this.rules = rules;
	}

	public Set<String> getTransactionTypes() {
		return transactionTypes;
	}

	public void setTransactionTypes(Set<String> transactionTypes) {
		this.transactionTypes = transactionTypes;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public String getMergedTxnCatName() {
		return mergedTxnCatName;
	}

	public void setMergedTxnCatName(String mergedTxnCatName) {
		this.mergedTxnCatName = mergedTxnCatName;
	}
	
}

