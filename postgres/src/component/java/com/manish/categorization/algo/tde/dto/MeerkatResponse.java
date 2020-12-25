package com.manish.categorization.algo.tde.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class MeerkatResponse {
	@JsonProperty("status")
	private String status;
	@JsonProperty("data")
	private MeerkatData data;

	public String getStatus() {
		return status;

	}

	public void setStatus(String status) {
		this.status = status;
	}

	public MeerkatData getData() {
		return data;
	}

	public void setData(MeerkatData data) {
		this.data = data;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class MeerkatData implements Serializable {
		@JsonProperty("cobrand_id")
		@SerializedName("cobrand_id")
		private long cobrandId;

		@JsonProperty("user_id")
		@SerializedName("user_id")
		private long memId;

		@JsonProperty("container")
		@SerializedName("container")
		private String container;

		@JsonProperty("version")
		@SerializedName("version")
		private String version;
		
		@JsonProperty("debug")
		private boolean debug;

		@JsonProperty("transaction_list")
		@SerializedName("transaction_list")
		private List<MeerkatTransaction> txns;

		@JsonProperty("cobrand_region")
		@SerializedName("cobrand_region")
		private long region;

		@JsonProperty("services_list")
		@SerializedName("services_list")
		private List<String> services;
		
		public boolean isDebug() {
			return debug;
		}

		public void setDebug(boolean debug) {
			this.debug = debug;
		}

		public long getRegion() {
			return region;
		}

		public void setRegion(long region) {
			this.region = region;
		}

		public List<String> getServices() {
			return services;
		}

		public void setServices(List<String> services) {
			this.services = services;
		}

		public String getContainer() {
			return container;
		}

		public void setContainer(String container) {
			this.container = container;
		}

		
		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public long getCobrandId() {
			return cobrandId;
		}

		public void setCobrandId(long cobrandId) {
			this.cobrandId = cobrandId;
		}

		public long getMemId() {
			return memId;
		}

		public void setMemId(long memId) {
			this.memId = memId;
		}

		public List<MeerkatTransaction> getTxns() {
			return txns;
		}

		public void setTxns(List<MeerkatTransaction> txns) {
			this.txns = txns;
		}

	}

	@JsonInclude(Include.NON_NULL)
	public static class CNNInfo implements Serializable {

		@JsonProperty("category_score")
		@SerializedName("category_score")
		private String categoryScore;

		@JsonProperty("subtype_score")
		@SerializedName("subtype_score")
		private String subTypeScore;

		@JsonProperty("merchant_score")
		@SerializedName("merchant_score")
		private String merchantScore;

		public String getCategoryScore() {
			return categoryScore;
		}

		public void setCategoryScore(String categoryScore) {
			this.categoryScore = categoryScore;
		}

		public String getSubTypeScore() {
			return subTypeScore;
		}

		public void setSubTypeScore(String subTypeScore) {
			this.subTypeScore = subTypeScore;
		}

		public String getMerchantScore() {
			return merchantScore;
		}

		public void setMerchantScore(String merchantScore) {
			this.merchantScore = merchantScore;
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class MeerkatTransaction implements Serializable {

		@JsonProperty("transaction_id")
		@SerializedName("transaction_id")
		private BigInteger transactionId;

		@JsonProperty("category_labels")
		@SerializedName("category_labels")
		private List<String> labels;
		
		@JsonProperty("granular_category")
		@SerializedName("granular_category")
		private String granularCategory;

		@JsonProperty("txn_sub_type")
		@SerializedName("txn_sub_type")
		private String subType;

		@JsonProperty("txn_type")
		@SerializedName("txn_type")
		private String type;

		@JsonProperty("cnn")
		@SerializedName("cnn")
		private CNNInfo debugInfo;

		@JsonProperty("neighbourhood")
		@SerializedName("neighbourhood")
		private String neighbourhood;

		@JsonProperty("is_physical_merchant")
		@SerializedName("is_physical_merchant")
		private boolean physicalMerchant;

		@JsonProperty("merchant_name")
		@SerializedName("merchant_name")
		private String merchant;

		@JsonProperty("store_id")
		@SerializedName("store_id")
		private String storeId;

		@JsonProperty("street")
		@SerializedName("street")
		private String street;

		@JsonProperty("city")
		@SerializedName("city")
		private String city;

		@JsonProperty("state")
		@SerializedName("state")
		private String state;

		@JsonProperty("country")
		@SerializedName("country")
		private String country;

		@JsonProperty("postal_code")
		@SerializedName("postal_code")
		private String postalCode;

		@JsonProperty("phone_number")
		@SerializedName("phone_number")
		private String phone;

		@JsonProperty("fax_number")
		@SerializedName("fax_number")
		private String fax;

		@JsonProperty("chain_name")
		@SerializedName("chain_name")
		private String chainName;

		@JsonProperty("website")
		@SerializedName("website")
		private String website;

		@JsonProperty("email")
		@SerializedName("email")
		private String email;

		@JsonProperty("confidence_score")
		@SerializedName("confidence_score")
		private String confidenceScore;

		@JsonProperty("longitude")
		@SerializedName("longitude")
		private String longitude;

		@JsonProperty("latitude")
		@SerializedName("latitude")
		private String latitude;

		@JsonProperty("source")
		@SerializedName("source")
		private String source;

		@JsonProperty("source_merchant_id")
		@SerializedName("source_merchant_id")
		private String sourceMerchantId;

		@JsonProperty("use_legacy_merchant")
		@SerializedName("use_legacy_merchant")
		private boolean use_legacy_merchant;

		@JsonProperty("is_recurring")
		@SerializedName("is_recurring")
		private boolean isRecurring;
		
		@JsonProperty("periodicity")
		@SerializedName("periodicity")
		private String periodicity;
		
		@JsonProperty("intermediary")
		@SerializedName("intermediary")
		private String intermediary;
		
		@JsonProperty("mode_of_payment")
		@SerializedName("mode_of_payment")
		private String modeOfPayment;
		
		@JsonProperty("pii_info")
		@SerializedName("pii_info")
		private String tdePiiInfo;
 
		@JsonProperty("vendor_name_variation")
		@SerializedName("vendor_name_variation")
		private String[] vendorNameVariation; 
		
		@JsonProperty("txn_merchant_type")
		@SerializedName("txn_merchant_type")
		private String txnMerchantType;
		
		@JsonProperty("logo_endpoint")
		@SerializedName("logo_endpoint")
		private String logoEndpoint;

		@JsonProperty("hierarchy_category")
		@SerializedName("hierarchy_category")
		private String factualCategory;
		
		@JsonProperty("smb_category")
		@SerializedName("smb_category")
		private String smbCategory;
		
		@JsonProperty("is_business_related")
		@SerializedName("is_business_related")
		private String isBusinessRelated;
		
		public CNNInfo getDebugInfo() {
			return debugInfo;
		}

		public void setDebugInfo(CNNInfo debugInfo) {
			this.debugInfo = debugInfo;
		}

		public BigInteger getTransactionId() {
			return transactionId;
		}

		public void setTransactionId(BigInteger transactionId) {
			this.transactionId = transactionId;
		}

		public List<String> getLabels() {
			return labels;
		}

		public void setLabels(List<String> labels) {
			this.labels = labels;
		}
		
		public String getGranularCategory() {
			return granularCategory;
		}

		public void setGranularCategory(String granularCategory) {
			this.granularCategory = granularCategory;
		}

		public String getSubType() {
			return subType;
		}

		public void setSubType(String subType) {
			this.subType = subType;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getNeighbourhood() {
			return neighbourhood;
		}

		public void setNeighbourhood(String neighbourhood) {
			this.neighbourhood = neighbourhood;
		}

		public boolean isPhysicalMerchant() {
			return physicalMerchant;
		}

		public void setPhysicalMerchant(boolean physicalMerchant) {
			this.physicalMerchant = physicalMerchant;
		}

		public String getMerchant() {
			return merchant;
		}

		public void setMerchant(String merchant) {
			this.merchant = merchant;
		}

		public String getStoreId() {
			return storeId;
		}

		public void setStoreId(String storeId) {
			this.storeId = storeId;
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

		public String getPostalCode() {
			return postalCode;
		}

		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getFax() {
			return fax;
		}

		public void setFax(String fax) {
			this.fax = fax;
		}

		public String getWebsite() {
			return website;
		}

		public void setWebsite(String website) {
			this.website = website;
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

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public String getSourceMerchantId() {
			return sourceMerchantId;
		}

		public void setSourceMerchantId(String sourceMerchantId) {
			this.sourceMerchantId = sourceMerchantId;
		}

		public String getChainName() {
			return chainName;
		}

		public void setChainName(String chainName) {
			this.chainName = chainName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getConfidenceScore() {
			return confidenceScore;
		}

		public void setConfidenceScore(String confidenceScore) {
			this.confidenceScore = confidenceScore;
		}

		public boolean getUse_legacy_merchant() {
			return use_legacy_merchant;
		}

		public void setUse_legacy_merchant(boolean use_legacy_merchant) {
			this.use_legacy_merchant = use_legacy_merchant;
		}

		public boolean isRecurring() {
			return isRecurring;
		}

		public void setRecurring(boolean isRecurring) {
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

		public String[] getVendorNameVariation() {
			return vendorNameVariation;
		}

		public void setVendorNameVariation(String[] vendorNameVariation) {
			this.vendorNameVariation = vendorNameVariation;
		}

		public String getTxnMerchantType() {
			return txnMerchantType;
		}

		public void setTxnMerchantType(String txnMerchantType) {
			this.txnMerchantType = txnMerchantType;
		}

		public String getLogoEndpoint() {
			return logoEndpoint;
		}

		public void setLogoEndpoint(String logoEndpoint) {
			this.logoEndpoint = logoEndpoint;
		}

		public String getFactualCategory() {
			return factualCategory;
		}

		public void setFactualCategory(String factualCategory) {
			this.factualCategory = factualCategory;
		}

		public String getSmbCategory() {
			return smbCategory;
		}

		public void setSmbCategory(String smbCategory) {
			this.smbCategory = smbCategory;
		}

		public String getIsBusinessRelated() {
			return isBusinessRelated;
		}

		public void setIsBusinessRelated(String isBusinessRelated) {
			this.isBusinessRelated = isBusinessRelated;
		}

		
		
		
		
	}

}
