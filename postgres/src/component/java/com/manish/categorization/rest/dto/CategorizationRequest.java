package com.manish.categorization.rest.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class CategorizationRequest implements Cloneable {

	@JsonProperty(value = "cobrandId", required = true)
	private Long cobrandId;

	@JsonProperty(value = "memId", required = true)
	private Long memId;

	@JsonProperty(value = "country", required = false, defaultValue = "")
	private String country;

	@JsonProperty(value = "state", required = false, defaultValue = "")
	private String state;

	@JsonProperty(value = "city", required = false, defaultValue = "")
	private String city;

	@JsonProperty(value = "sumInfoId", required = true)
	private Long sumInfoId;

	@JsonProperty(value = "container", required = true)
	private String container;

	@JsonProperty(value = "region", required = true)
	private Long region;

	@JsonProperty(value = "configurations", required = true)
	Configurations configurations;

	@JsonProperty(value = "accountType", required = false, defaultValue = "")
	private String accountType;
	
	@JsonProperty(value = "txns", required = true)
	private List<TransactionRequest> txns;

	@JsonProperty(value = "uniqueTrackingId", required = false, defaultValue = "")
	private String uniqueTrackingId;

	@JsonProperty(value = "isTdeV2", required = false)
	private boolean isTdeV2;
	
	@JsonProperty(value = "accountClassification", required = false, defaultValue = "")
	private String accountClassification;
	
	@JsonProperty(value = "smallBusiness", required = false, defaultValue = "false")
	private boolean smallBusiness;
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getCobrandId() {
		return cobrandId;
	}

	public void setCobrandId(Long cobrandId) {
		this.cobrandId = cobrandId;
	}

	public Long getMemId() {
		return memId;
	}

	public void setMemId(Long memId) {
		this.memId = memId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Long getSumInfoId() {
		return sumInfoId;
	}

	public void setSumInfoId(Long sumInfoId) {
		this.sumInfoId = sumInfoId;
	}

	public String getContainer() {
		return container;
	}

	public void setContainer(String container) {
		this.container = container;
	}

	public Long getRegion() {
		return region;
	}

	public void setRegion(Long region) {
		this.region = region;
	}

	public List<TransactionRequest> getTxns() {
		return txns;
	}

	public void setTxns(List<TransactionRequest> txns) {
		this.txns = txns;
	}

	public Configurations getConfigurations() {
		return configurations;
	}

	public void setConfigurations(Configurations configurations) {
		this.configurations = configurations;
	}
	
	
	public String getUniqueTrackingId() {
		return uniqueTrackingId;
	}

	public void setUniqueTrackingId(String uniqueTrackingId) {
		this.uniqueTrackingId = uniqueTrackingId;
	}
	
	public boolean isTdeV2() {
		return isTdeV2;
	}

	public void setTdeV2(boolean isTdeV2) {
		this.isTdeV2 = isTdeV2;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String getAccountClassification() {
		return accountClassification;
	}

	public void setAccountClassification(String accountClassification) {
		this.accountClassification = accountClassification;
	}

	public boolean isSmallBusiness() {
		return smallBusiness;
	}

	public void setSmallBusiness(boolean smallBusiness) {
		this.smallBusiness = smallBusiness;
	}
	
	
}
