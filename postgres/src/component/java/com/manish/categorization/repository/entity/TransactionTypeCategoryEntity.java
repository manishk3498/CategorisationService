package com.manish.categorization.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 
 * @author mgarg
 *
 */
@Entity
public class TransactionTypeCategoryEntity implements Serializable{ 
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "inv_trans_type_cat_map_id")
	private String invTransTypeCatMapId;
	
	@Column(name = "transaction_category_id")
	private String txnCategoryId;
	
	@Column(name = "transaction_type")
	private String transactionType;

	@Column(name = "transaction_category_name")
	private String txnCategoryName;
	
	public String getTxnCategoryId() {
		return txnCategoryId;
	}

	public void setTxnCategoryId(String txnCategoryId) {
		this.txnCategoryId = txnCategoryId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getTxnCategoryName() {
		return txnCategoryName;
	}

	public void setTxnCategoryName(String txnCategoryName) {
		this.txnCategoryName = txnCategoryName;
	}
}
