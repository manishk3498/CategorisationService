package com.manish.categorization.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author smohanty
 *
 */
@Entity
@Table(name="TXN_MERCHANT_TYPE")
public class TxnMerchantTypeEntity implements Serializable{ 
	
	@Id
	@Column(name = "TXN_MERCHANT_TYPE_ID")
	private Long txnMerchantTypeId;
	
	@Column(name = "TXN_MERCHANT_TYPE_NAME")
	private String txnMerchantTypeName;

	public Long getTxnMerchantTypeId() {
		return txnMerchantTypeId;
	}

	public void setTxnMerchantTypeId(Long txnMerchantTypeId) {
		this.txnMerchantTypeId = txnMerchantTypeId;
	}

	public String getTxnMerchantTypeName() {
		return txnMerchantTypeName;
	}

	public void setTxnMerchantTypeName(String txnMerchantTypeName) {
		this.txnMerchantTypeName = txnMerchantTypeName;
	}
	
	

}
