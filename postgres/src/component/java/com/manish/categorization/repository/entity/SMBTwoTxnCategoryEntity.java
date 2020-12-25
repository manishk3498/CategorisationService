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
@Table(name="SMB2_TXN_CATEGORY")
public class SMBTwoTxnCategoryEntity implements Serializable{

	@Id
	@Column(name = "SMB2_TXN_CATEGORY_ID")
	private Long smbTwoTxnCategroyId;
	
	@Column(name = "SMB2_TXN_CATEGORY_NAME")
	private String smbTwoTxnCategoryName;

	public Long getSmbTwoTxnCategroyId() {
		return smbTwoTxnCategroyId;
	}

	public void setSmbTwoTxnCategroyId(Long smbTwoTxnCategroyId) {
		this.smbTwoTxnCategroyId = smbTwoTxnCategroyId;
	}

	public String getSmbTwoTxnCategoryName() {
		return smbTwoTxnCategoryName;
	}

	public void setSmbTwoTxnCategoryName(String smbTwoTxnCategoryName) {
		this.smbTwoTxnCategoryName = smbTwoTxnCategoryName;
	}
	
	
}
