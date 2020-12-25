package com.manish.categorization.repository.entity;

import java.io.Serializable;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.lucene.document.Document;

import com.manish.categorization.db.SlamBangWord;
import com.manish.categorization.util.Constants;

@Entity
@Table(name="SLAM_BANG_WORD")
public class SlamBangWordEntity implements Serializable{ //Comparable<SlamBangWordEntity> {
	
	@Id
	@Column(name = "SLAM_BANG_WORD_ID")
	private Long slamBangWordId;
	
	@Column(name = "SLAM_BANG_WORD")
	private String slamBangWord;
	
	@Column(name = "MERCHANT_ID")
	private Long merchantId;
	
	@Column(name = "MERCHANT_NAME")
	private String merchantName;
	
	@Column(name = "SUM_INFO_ID")
	private Long sumInfoId;
	
	@Column(name = "TRANSACTION_CATEGORY_NAME")
	private String transactionCategoryName;
	
	@Column(name = "TRANSACTION_TYPE")
	private String transactionType;
	
	@Column(name = "REGION_ID")
	private Long regionId;
	
	@Column(name = "PRIORITY")
	private Long priority;
	
	@Column(name="IS_DUMMY")
	private Long isDummyMerchant;
	
	@Column(name="IS_DELETED")
	private Long isDeleted;
	
	@Column(name="CONTAINERS")
	private String containers;
	
	@Column(name="IS_MERCHANT_DELETED")
	private Long isMerchantDeleted;
	
	public Long getSlamBangWordId() {
		return slamBangWordId;
	}
	
	public void setSlamBangWordId(Long slamBangWordId) {
		this.slamBangWordId = slamBangWordId;
	}
	public String getSlamBangWord() {
		return slamBangWord;
	}
	public void setSlamBangWord(String slamBangWord) {
		this.slamBangWord = slamBangWord;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public Long getSumInfoId() {
		return sumInfoId;
	}
	public void setSumInfoId(Long sumInfoId) {
		this.sumInfoId = sumInfoId;
	}
	public String getTransactionCategoryName() {
		return transactionCategoryName;
	}
	public void setTransactionCategoryName(String transactionCategoryName) {
		this.transactionCategoryName = transactionCategoryName;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public Long getRegionId() {
		return regionId;
	}
	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
	public Long getPriority() {
		return priority;
	}
	public void setPriority(Long priority) {
		this.priority = priority;
	}
	public Long getIsDummyMerchant() {
		return isDummyMerchant;
	}

	public void setIsDummyMerchant(Long isDummyMerchant) {
		this.isDummyMerchant = isDummyMerchant;
	}
	public Long getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Long isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getContainers() {
		return containers;
	}
	public void setContainers(String containers) {
		this.containers = containers;
	}

	public Long getIsMerchantDeleted() {
		return isMerchantDeleted;
	}

	public void setIsMerchantDeleted(Long isMerchantDeleted) {
		this.isMerchantDeleted = isMerchantDeleted;
	}

	
	
	
	
}
