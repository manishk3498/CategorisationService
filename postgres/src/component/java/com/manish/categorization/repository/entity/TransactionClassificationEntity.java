package com.manish.categorization.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class TransactionClassificationEntity implements Serializable{
	
	@Id
	@Column(name = "meerkat_txn_classifier_id")
	private long classifierId;
	
	@Column(name = "classification")
	private String classifierName;
	
	@Column(name = "meerkat_txn_type_id")
	private long txnTypeId;
	
	@Column(name = "meerkat_txn_type")
	private String txnTypeName;
	
	@Column(name = "meerkat_txn_sub_type_id")
	private long txnSubTypeId;
	
	@Column(name = "meerkat_txn_subtype")
	private String txnSubTypeName;
	
	public long getClassifierId() {
		return classifierId;
	}
	public void setClassifierId(long classifierId) {
		this.classifierId = classifierId;
	}
	public String getClassifierName() {
		return classifierName;
	}
	public void setClassifierName(String classifierName) {
		this.classifierName = classifierName;
	}
	public long getTxnTypeId() {
		return txnTypeId;
	}
	public void setTxnTypeId(long txnTypeId) {
		this.txnTypeId = txnTypeId;
	}
	public String getTxnTypeName() {
		return txnTypeName;
	}
	public void setTxnTypeName(String txnTypeName) {
		this.txnTypeName = txnTypeName;
	}
	public long getTxnSubTypeId() {
		return txnSubTypeId;
	}
	public void setTxnSubTypeId(long txnSubTypeId) {
		this.txnSubTypeId = txnSubTypeId;
	}
	public String getTxnSubTypeName() {
		return txnSubTypeName;
	}
	public void setTxnSubTypeName(String txnSubTypeName) {
		this.txnSubTypeName = txnSubTypeName;
	}


	
}
