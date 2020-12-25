package com.manish.categorization.db;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class TransactionCategory implements Serializable {

	private String name;
	private String displayName;
	private String label;
	private String categoryType;
	private Set<Long> regions = new TreeSet<Long>();;
	private String parentCategoryName;
	private String mergedTxnCatName;
	private boolean global;
	private boolean smallBusinessCategory;
	private boolean personnelCategory;
	private boolean specificCategory;
	private Set<String> tdeLabels = new TreeSet<String>();
	private Set<ContainerBaseTypeRule> rules = new HashSet<ContainerBaseTypeRule>();
	private Set<String> transactionTypes = new TreeSet<String>();

	public TransactionCategory() {
		super();
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
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

	public Set<Long> getRegions() {
		return regions;
	}

	public void setRegions(Set<Long> regions) {
		this.regions = regions;
	}

	public String getParentCategoryName() {
		return parentCategoryName;
	}

	public void setParentCategoryName(String parentCategoryName) {
		this.parentCategoryName = parentCategoryName;
	}

	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	public boolean isSmallBusinessCategory() {
		return smallBusinessCategory;
	}

	public void setSmallBusinessCategory(boolean smallBusinessCategory) {
		this.smallBusinessCategory = smallBusinessCategory;
	}

	public boolean isPersonnelCategory() {
		return personnelCategory;
	}

	public void setPersonnelCategory(boolean personnelCategory) {
		this.personnelCategory = personnelCategory;
	}

	public boolean isSpecificCategory() {
		return specificCategory;
	}

	public void setSpecificCategory(boolean specificCategory) {
		this.specificCategory = specificCategory;
	}

	public Set<String> getTdeLabels() {
		return tdeLabels;
	}

	public void setTdeLabels(Set<String> tdeLabels) {
		this.tdeLabels = tdeLabels;
	}

	public Set<ContainerBaseTypeRule> getRules() {
		return rules;
	}

	public void setRules(Set<ContainerBaseTypeRule> rules) {
		this.rules = rules;
	}

	public Set<String> getTransactionTypes() {
		return transactionTypes;
	}

	public void setTransactionTypes(Set<String> transactionTypes) {
		this.transactionTypes = transactionTypes;
	}

	public String getMergedTxnCatName() {
		return mergedTxnCatName;
	}

	public void setMergedTxnCatName(String mergedTxnCatName) {
		this.mergedTxnCatName = mergedTxnCatName;
	}
	
}
