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
public class MCCTxnCategoryEntity implements Serializable{
	
	@Id
	@Column(name="mcc_txn_category_map_id")
	private Long mccTxnCategoryMapId;

	@Column(name="mc_code")
	private Long mccCode;
	
	@Column(name="transaction_category_name")
	private String transactionCategoryName;

	public Long getMccTxnCategoryMapId() {
		return mccTxnCategoryMapId;
	}

	public void setMccTxnCategoryMapId(Long mccTxnCategoryMapId) {
		this.mccTxnCategoryMapId = mccTxnCategoryMapId;
	}

	public Long getMccCode() {
		return mccCode;
	}

	public void setMccCode(Long mccCode) {
		this.mccCode = mccCode;
	}

	public String getTransactionCategoryName() {
		return transactionCategoryName;
	}

	public void setTransactionCategoryName(String transactionCategoryName) {
		this.transactionCategoryName = transactionCategoryName;
	}
	
}
