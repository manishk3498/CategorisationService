package com.manish.categorization.rest.dto;

import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This object is used to send the configurations like whether meerkat is enabled for a cobrand
 * in the request 
 * @author mgarg
 *
 */
public class Configurations {

	@JsonProperty(value = "meerkat", required = false, defaultValue = "true")
	private boolean meerkat;

	@JsonProperty(value = "meerkatUrl")
	private String meerkatUrl;
	
	@JsonProperty(value = "legacy", required = false, defaultValue = "true")
	private boolean legacy;

	@JsonProperty(value = "mergerEnabled", required = false, defaultValue = "true")
	private boolean mergerEnabled;

	//This key is primarily used for deciding whether the Simple Description should be derived by TDE Algorithm
	@JsonProperty(value = "simpleDescEnabled", required = false, defaultValue = "true")
	private boolean simpleDescEnabled;
	
	//This key is used for deciding Simple Description version
	@JsonProperty(value = "simpleDescVersion", required = false, defaultValue = "1.0")
	private String simpleDescVersion;
	
	@JsonProperty(value = "geoLocationEnabledInSD", required = false, defaultValue = "false")
	private boolean geoLocationEnabledInSD;
	
	@JsonProperty(value = "debug", required = false, defaultValue = "false")
	private boolean debug;
	
	@JsonProperty("services")
	private List<String> services;
	
	@JsonProperty(value="mccRule", required = false,defaultValue="2")
	private Long mccRule;
	
	@JsonProperty(value = "granularCategoryEnabled", required = false, defaultValue = "false")
	private boolean granularCategoryEnabled;
	
	@JsonProperty(value = "localeStr", required = false, defaultValue = "")
	private String localeStr;
	
	@JsonProperty(value = "locale", required = false)
	private Locale locale;
	
	@JsonProperty(value = "derivedAccountTypeEnabled", required = false, defaultValue = "false")
	private boolean derivedAccountTypeEnabled;
	
	//This key is having the comma separated account types which are configured at cobrand level for TDE to be applied
	@JsonProperty(value = "configuredAccountTypes", required = false, defaultValue = "")
	private String configuredAccountTypes;
	
	//This contains the derived account type.
	@JsonProperty(value = "derivedAccountType", required = false, defaultValue = "")
	private String derivedAccountType;
	
	public boolean isMeerkat() {
		return meerkat;
	}

	public void setMeerkat(boolean meerkat) {
		this.meerkat = meerkat;
	}

	public boolean isLegacy() {
		return legacy;
	}

	public void setLegacy(boolean legacy) {
		this.legacy = legacy;
	}

	public boolean isMergerEnabled() {
		return mergerEnabled;
	}

	public void setMergerEnabled(boolean mergerEnabled) {
		this.mergerEnabled = mergerEnabled;
	}

	public Long getMccRule() {
		return mccRule;
	}

	public void setMccRule(Long mccRule) {
		this.mccRule = mccRule;
	}

	public boolean isGeoLocationEnabledInSD() {
		return geoLocationEnabledInSD;
	}

	public void setGeoLocationEnabledInSD(boolean isGeoLocationEnabledInSD) {
		this.geoLocationEnabledInSD = isGeoLocationEnabledInSD;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public List<String> getServices() {
		return services;
	}

	public void setServices(List<String> services) {
		this.services = services;
	}

	public boolean isSimpleDescEnabled() {
		return simpleDescEnabled;
	}

	public void setSimpleDescEnabled(boolean isSimpleDescEnabled) {
		this.simpleDescEnabled = isSimpleDescEnabled;
	}

	public String getMeerkatUrl() {
		return meerkatUrl;
	}

	public void setMeerkatUrl(String meerkatUrl) {
		this.meerkatUrl = meerkatUrl;
	}

	public String getSimpleDescVersion() {
		return simpleDescVersion;
	}

	public void setSimpleDescVersion(String simpleDescVersion) {
		this.simpleDescVersion = simpleDescVersion;
	}

	public boolean isGranularCategoryEnabled() {
		return granularCategoryEnabled;
	}

	public void setGranularCategoryEnabled(boolean granularCategoryEnabled) {
		this.granularCategoryEnabled = granularCategoryEnabled;
	}

	public String getLocaleStr() {
		return localeStr;
	}

	public void setLocaleStr(String localeStr) {
		this.localeStr = localeStr;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public boolean isDerivedAccountTypeEnabled() {
		return derivedAccountTypeEnabled;
	}

	public void setDerivedAccountTypeEnabled(boolean derivedAccountTypeEnabled) {
		this.derivedAccountTypeEnabled = derivedAccountTypeEnabled;
	}

	public String getConfiguredAccountTypes() {
		return configuredAccountTypes;
	}

	public void setConfiguredAccountTypes(String configuredAccountTypes) {
		this.configuredAccountTypes = configuredAccountTypes;
	}

	public String getDerivedAccountType() {
		return derivedAccountType;
	}

	public void setDerivedAccountType(String derivedAccountType) {
		this.derivedAccountType = derivedAccountType;
	}
	
}
