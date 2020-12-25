package com.manish.categorization.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TransactionCategoryLabelEntity implements Serializable{ 
	
	@Id
	@Column(name = "txn_cate_label_map_id")
	private Long txnCateLabelMapId;
	
	@Column(name = "transaction_category_id")
	private Long txnCategoryId;
	
	@Column(name = "label")
	private String label;

	public Long getTxnCategoryId() {
		return txnCategoryId;
	}

	public void setTxnCategoryId(Long txnCategoryId) {
		this.txnCategoryId = txnCategoryId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
		
}
