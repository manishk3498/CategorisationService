package com.manish.categorization.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="HL_MASTER_CATEGORY_MAPPING")
public class HLMasterMappingEntity implements Serializable{

	@Id
	@Column(name = "HL_MASTER_CATEGORY_MAPPING_ID")
	private Long hlMasterCategoryMappingId;
	
	@Column(name = "HL_TRANSACTION_CATEGORY_ID")
	private Long hlTransactionCategoryId;

	@Column(name = "TRANSACTION_CATEGORY_ID")
	private Long transactionCategoryId;
	
	@Column(name = "HL_TXN_CATEGORY_NAME")
	private String hlTransactionCategoryName;

	public Long getHlMasterCategoryMappingId() {
		return hlMasterCategoryMappingId;
	}

	public void setHlMasterCategoryMappingId(Long hlMasterCategoryMappingId) {
		this.hlMasterCategoryMappingId = hlMasterCategoryMappingId;
	}

	public Long getHlTransactionCategoryId() {
		return hlTransactionCategoryId;
	}

	public void setHlTransactionCategoryId(Long hlTransactionCategoryId) {
		this.hlTransactionCategoryId = hlTransactionCategoryId;
	}

	public Long getTransactionCategoryId() {
		return transactionCategoryId;
	}

	public void setTransactionCategoryId(Long transactionCategoryId) {
		this.transactionCategoryId = transactionCategoryId;
	}

	public String getHlTransactionCategoryName() {
		return hlTransactionCategoryName;
	}

	public void setHlTransactionCategoryName(String hlTransactionCategoryName) {
		this.hlTransactionCategoryName = hlTransactionCategoryName;
	}
	
	

}
