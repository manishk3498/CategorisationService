package com.manish.categorization.db;



public class GranularCategoryMapping {
	private long granularCategoryId;
	private String granularCategoryName;
	private String masterCategoryName;
	private String baseType;
	
	/**
	 * @return the granularCategoryId
	 */
	public long getGranularCategoryId() {
		return granularCategoryId;
	}
	/**
	 * @param granularCategoryId the granularCategoryId to set
	 */
	public void setGranularCategoryId(long granularCategoryId) {
		this.granularCategoryId = granularCategoryId;
	}
	/**
	 * @return the granularCategoryName
	 */
	public String getGranularCategoryName() {
		return granularCategoryName;
	}
	/**
	 * @param granularCategoryName the granularCategoryName to set
	 */
	public void setGranularCategoryName(String granularCategoryName) {
		this.granularCategoryName = granularCategoryName;
	}
	/**
	 * @return the masterCategoryName
	 */
	public String getMasterCategoryName() {
		return masterCategoryName;
	}
	/**
	 * @param masterCategoryName the masterCategoryName to set
	 */
	public void setMasterCategoryName(String masterCategoryName) {
		this.masterCategoryName = masterCategoryName;
	}
	public String getBaseType() {
		return baseType;
	}
	public void setBaseType(String baseType) {
		this.baseType = baseType;
	}
	
}
