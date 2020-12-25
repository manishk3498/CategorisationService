package com.manish.categorization.rest.dto;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(Include.NON_NULL)
public class TransactionResponse {
	@JsonProperty("transactionId")
	private BigInteger transactionId;

	@JsonProperty("merchant")
	private String merchant;

	@JsonProperty("categorisationSource")
	private String categorisationSource;
	
	@JsonProperty("categorisationSourceId")
	private String categorisationSourceId;

	@JsonProperty("category")
	private String category;

	@JsonProperty("categoryId")
	private Long categoryId;

	@JsonProperty("hlCategoryId")
	private Long hlCategoryId;
	
	@JsonProperty("hlCategory")
	private String hlCategory;
	
	@JsonProperty("keyword")
	private String keyword;

	@JsonProperty("merchantId")
	private Long merchantId;
	
	public BigInteger getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(BigInteger transactionId) {
		this.transactionId = transactionId;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public String getCategorisationSource() {
		return categorisationSource;
	}

	public void setCategorisationSource(String categorisationSource) {
		this.categorisationSource = categorisationSource;
	}

	public String getCategorisationSourceId() {
		return categorisationSourceId;
	}

	public void setCategorisationSourceId(String categorisationSourceId) {
		this.categorisationSourceId = categorisationSourceId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getHlCategoryId() {
		return hlCategoryId;
	}

	public void setHlCategoryId(Long hlCategoryId) {
		this.hlCategoryId = hlCategoryId;
	}

	public String getHlCategory() {
		return hlCategory;
	}

	public void setHlCategory(String hlCategory) {
		this.hlCategory = hlCategory;
	}
	
	
	
}
