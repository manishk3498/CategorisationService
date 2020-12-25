package com.manish.categorization.repository.dto;


import java.util.Set;
import java.util.TreeSet;

public class TxnTypeCategoryDTO {
	private Long categoryId;
	private Set<String> txnTypes = new TreeSet<String>();

	public Set<String> getTxnTypes() {
		return txnTypes;
	}
	public void addTxnType(String txnType) {
		this.txnTypes.add(txnType);
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

}
