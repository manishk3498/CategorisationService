package com.manish.categorization.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * @author mgarg
 *
 */
@Entity
@Table(name="REGION")
public class RegionEntity {

	@Id
	@Column(name = "REGION_ID")
	private Long regionId;
	
	@Column(name = "REGION_NAME")
	private String region;
	
	@Column(name = "TDE_SERVICES")
	private String tdeServices;

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getTdeServices() {
		return tdeServices;
	}

	public void setTdeServices(String tdeServices) {
		this.tdeServices = tdeServices;
	}
	
	
	
}
