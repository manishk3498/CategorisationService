package com.manish.categorization.db;



public class MeerkatTxnClassifier {
	private long classifierId;
	private String classifierName;
	private long txnTypeId;
	private String txnTypeName;
	private long txnSubTypeId;
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
