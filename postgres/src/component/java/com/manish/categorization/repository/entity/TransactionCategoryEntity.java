package com.manish.categorization.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TransactionCategoryEntity implements Serializable{ 
	
	@Id
	@Column(name = "transaction_category_id")
	private Long txnCategoryId;
	
	@Column(name = "transaction_category_name")
	private String txnCategoryName;
	
	@Column(name = "transaction_category_type")
	private String txnCategoryType;
	
	@Column(name = "region_id")
	private Long regionId;
	
	@Column(name = "merged_txn_cat_id")
	private Long mergedTxnCategoryId;
	
	@Column(name = "merged_txn_cat_name")
	private String mergedTxnCatName;
	
	@Column(name = "is_small_business")
	private Long smallBusiness;
	
	@Column(name = "categorisation_rule")
	private String categorizationRule;

	public Long getTxnCategoryId() {
		return txnCategoryId;
	}

	public void setTxnCategoryId(Long txnCategoryId) {
		this.txnCategoryId = txnCategoryId;
	}

	public String getTxnCategoryName() {
		return txnCategoryName;
	}

	public void setTxnCategoryName(String txnCategoryName) {
		this.txnCategoryName = txnCategoryName;
	}

	public String getTxnCategoryType() {
		return txnCategoryType;
	}

	public void setTxnCategoryType(String txnCategoryType) {
		this.txnCategoryType = txnCategoryType;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public Long getMergedTxnCategoryId() {
		return mergedTxnCategoryId;
	}

	public void setMergedTxnCategoryId(Long mergedTxnCategoryId) {
		this.mergedTxnCategoryId = mergedTxnCategoryId;
	}

	public Long getSmallBusiness() {
		return smallBusiness;
	}

	public void setSmallBusiness(Long isSmallBusiness) {
		this.smallBusiness = isSmallBusiness;
	}

	public String getCategorizationRule() {
		return categorizationRule;
	}

	public void setCategorizationRule(String categorizationRule) {
		this.categorizationRule = categorizationRule;
	}

	public String getMergedTxnCatName() {
		return mergedTxnCatName;
	}

	public void setMergedTxnCatName(String mergedTxnCatName) {
		this.mergedTxnCatName = mergedTxnCatName;
	}
	
		
}
