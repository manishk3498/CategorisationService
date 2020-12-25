package com.manish.categorization.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.manish.categorization.algo.tde.dto.MeerkatResponse.CNNInfo;

@JsonInclude(Include.NON_NULL)
public class EnrichedTransactionResponse extends TransactionResponse {

	private String meerkatType;
	private String categoryDisplayName;
	private String meerkatSubType;
	private String meerkatMerchant;
	private String storeId;
	private String street;
	private String city;
	private String zip;
	private String state;
	private String country;
	private String longitude;
	private String latitude;
	private String chainName;
	private String faxNumber;
	private String neighbourhood;
	private String phoneNumber;
	private String website;
	private String postalCode;
	private String confidenceScore;
	private String email;
	private String sourceMerchantId;
	private String merchantSource;
	private String labels;
	private CNNInfo cnn = new CNNInfo();
	private String simpleDescription;
	private String granularCategory;
	private Long granularCategoryId;
	private Long meerkatTxnTypeId;
	private Long meerkatTxnSubTypeId;
	@JsonIgnore
	private boolean useLegacyMerchant;
	private Long isRecurring;
	private String periodicity;
	private String intermediary;
	private String modeOfPayment;
	private String tdePiiInfo;
	private String vendorName;
	private Long isPhysical;
	private Long txnMerchantTypeId;
	private String logoEndpoint;
	private Long txnMerchantType;
	private String factualCategory;
	private Long smbTwoTxnCategoryId;
	private Long isBusinessRelated;
	private String merchantType;

	public String getCategoryDisplayName() {
		return categoryDisplayName;
	}

	public CNNInfo getCnn() {
		return cnn;
	}

	public void setCnn(CNNInfo cnn) {
		this.cnn = cnn;
	}

	public void setCategoryDisplayName(String categoryDisplayName) {
		this.categoryDisplayName = categoryDisplayName;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getSourceMerchantId() {
		return sourceMerchantId;
	}

	public void setSourceMerchantId(String sourceMerchantId) {
		this.sourceMerchantId = sourceMerchantId;
	}

	public String getMeerkatType() {
		return meerkatType;
	}

	public void setMeerkatType(String meerkatType) {
		this.meerkatType = meerkatType;
	}

	public String getMeerkatSubType() {
		return meerkatSubType;
	}

	public void setMeerkatSubType(String meerkatSubType) {
		this.meerkatSubType = meerkatSubType;
	}

	public String getMeerkatMerchant() {
		return meerkatMerchant;
	}

	public void setMeerkatMerchant(String meerkatMerchant) {
		this.meerkatMerchant = meerkatMerchant;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getChainName() {
		return chainName;
	}

	public void setChainName(String chainName) {
		this.chainName = chainName;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getNeighbourhood() {
		return neighbourhood;
	}

	public void setNeighbourhood(String neighbourhood) {
		this.neighbourhood = neighbourhood;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getConfidenceScore() {
		return confidenceScore;
	}

	public void setConfidenceScore(String confidenceScore) {
		this.confidenceScore = confidenceScore;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public String getMerchantSource() {
		return merchantSource;
	}

	public void setMerchantSource(String merchantSource) {
		this.merchantSource = merchantSource;
	}

	public String getSimpleDescription() {
		return simpleDescription;
	}

	public void setSimpleDescription(String simpleDescription) {
		this.simpleDescription = simpleDescription;
	}
	public boolean isUseLegacyMerchant() {
		return useLegacyMerchant;
	}

	public void setUseLegacyMerchant(boolean useLegacyMerchant) {
		this.useLegacyMerchant = useLegacyMerchant;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGranularCategory() {
		return granularCategory;
	}

	public void setGranularCategory(String granularCategory) {
		this.granularCategory = granularCategory;
	}
	
	public Long getIsRecurring() {
		return isRecurring;
	}

	public void setIsRecurring(Long isRecurring) {
		this.isRecurring = isRecurring;
	}

	public String getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}

	public String getIntermediary() {
		return intermediary;
	}

	public void setIntermediary(String intermediary) {
		this.intermediary = intermediary;
	}

	public String getModeOfPayment() {
		return modeOfPayment;
	}

	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}

	public String getTdePiiInfo() {
		return tdePiiInfo;
	}

	public void setTdePiiInfo(String tdePiiInfo) {
		this.tdePiiInfo = tdePiiInfo;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Long getIsPhysical() {
		return isPhysical;
	}

	public void setIsPhysical(Long isPhysical) {
		this.isPhysical = isPhysical;
	}

	public Long getGranularCategoryId() {
		return granularCategoryId;
	}

	public void setGranularCategoryId(Long granularCategoryId) {
		this.granularCategoryId = granularCategoryId;
	}

	public Long getMeerkatTxnTypeId() {
		return meerkatTxnTypeId;
	}

	public void setMeerkatTxnTypeId(Long meerkatTxnTypeId) {
		this.meerkatTxnTypeId = meerkatTxnTypeId;
	}

	public Long getMeerkatTxnSubTypeId() {
		return meerkatTxnSubTypeId;
	}

	public void setMeerkatTxnSubTypeId(Long meerkatTxnSubTypeId) {
		this.meerkatTxnSubTypeId = meerkatTxnSubTypeId;
	}

	public Long getTxnMerchantTypeId() {
		return txnMerchantTypeId;
	}

	public void setTxnMerchantTypeId(Long txnMerchantTypeId) {
		this.txnMerchantTypeId = txnMerchantTypeId;
	}

	public String getLogoEndpoint() {
		return logoEndpoint;
	}

	public void setLogoEndpoint(String logoEndpoint) {
		this.logoEndpoint = logoEndpoint;
	}

	public Long getTxnMerchantType() {
		return txnMerchantType;
	}

	public void setTxnMerchantType(Long txnMerchantType) {
		this.txnMerchantType = txnMerchantType;
	}	
	
	public String getFactualCategory() {
		return factualCategory;
	}

	public void setFactualCategory(String factualCategory) {
		this.factualCategory = factualCategory;
	}
	

	public Long getSmbTwoTxnCategoryId() {
		return smbTwoTxnCategoryId;
	}

	public void setSmbTwoTxnCategoryId(Long smbTwoTxnCategoryId) {
		this.smbTwoTxnCategoryId = smbTwoTxnCategoryId;
	}

	public Long getIsBusinessRelated() {
		return isBusinessRelated;
	}

	public void setIsBusinessRelated(Long isBusinessRelated) {
		this.isBusinessRelated = isBusinessRelated;
	}

	public String getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(String merchantType) {
		this.merchantType = merchantType;
	}
	
	
}
