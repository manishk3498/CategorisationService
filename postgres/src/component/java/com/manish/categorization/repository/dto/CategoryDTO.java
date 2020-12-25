package com.manish.categorization.repository.dto;


import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.manish.categorization.db.ContainerBaseTypeRule;

public class CategoryDTO {
	private String name;
	private String categoryType;
	private Set<Long> regions = new TreeSet<Long>();
	private String parentCategoryName;
	private boolean smallBusinessCategory;
	private boolean personnelCategory;
	private boolean specificCategory;
	private Set<String> tdeLabels = new TreeSet<String>();
	private Set<ContainerBaseTypeRule> rules = new HashSet<ContainerBaseTypeRule>();
	private Set<String> transactionTypes = new TreeSet<String>();
	
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
		if(this.categoryType!=null && !this.categoryType.equals(categoryType)) {
			throw new RuntimeException("Invalid category type");
		} else {
			this.categoryType = categoryType;
		}
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
		if(this.parentCategoryName!=null && !this.parentCategoryName.equals(parentCategoryName)) {
			throw new RuntimeException("Invalid parentCategoryName");
		} else {
			this.parentCategoryName = parentCategoryName;
		}
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
	

}

