package com.manish.categorization.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author mgarg
 *
 */
@Entity
@Table(name="GRANULAR_TXN_CAT_MAP")
public class GranularCategoryEntity implements Serializable{

	@Id
	@Column(name = "granular_txn_cat_map_id")
	private Long granularTxnCatMapId;
	
	@Column(name = "granular_category_id")
	private Long granularCategoryId;

	@Column(name = "granular_category_name")
	private String granularCategoryName;

	@Column(name = "transaction_category_id")
	private Long transactionCategoryId;
	
	@Column(name = "transaction_base_type_id")
	private Long transactionBaseTypeId;
	
	@Column(name = "transaction_category_name")
	private String transactionCategoryName;
		
	@Column(name = "is_merged_catch_all_cat")
	private Long isMergedCatchAllCat;
	
	@Column(name = "is_catch_all_cat")
	private Long isCatchAllCat;
	
	public Long getGranularCategoryId() {
		return granularCategoryId;
	}

	public void setGranularCategoryId(Long granularCategoryId) {
		this.granularCategoryId = granularCategoryId;
	}

	public String getGranularCategoryName() {
		return granularCategoryName;
	}

	public void setGranularCategoryName(String granularCategoryName) {
		this.granularCategoryName = granularCategoryName;
	}

	public Long getTransactionCategoryId() {
		return transactionCategoryId;
	}

	public void setTransactionCategoryId(Long transactionCategoryId) {
		this.transactionCategoryId = transactionCategoryId;
	}

	public Long getTransactionBaseTypeId() {
		return transactionBaseTypeId;
	}

	public void setTransactionBaseTypeId(Long transactionBaseTypeId) {
		this.transactionBaseTypeId = transactionBaseTypeId;
	}

	public String getTransactionCategoryName() {
		return transactionCategoryName;
	}

	public void setTransactionCategoryName(String transactionCategoryName) {
		this.transactionCategoryName = transactionCategoryName;
	}

	public Long getGranularTxnCatMapId() {
		return granularTxnCatMapId;
	}

	public void setGranularTxnCatMapId(Long granularTxnCatMapId) {
		this.granularTxnCatMapId = granularTxnCatMapId;
	}

	public Long getIsMergedCatchAllCat() {
		return isMergedCatchAllCat;
	}

	public void setIsMergedCatchAllCat(Long isMergedCatchAllCat) {
		this.isMergedCatchAllCat = isMergedCatchAllCat;
	}

	public Long getIsCatchAllCat() {
		return isCatchAllCat;
	}

	public void setIsCatchAllCat(Long isCatchAllCat) {
		this.isCatchAllCat = isCatchAllCat;
	}

}
