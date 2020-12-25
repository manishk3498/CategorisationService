package com.manish.categorization.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class SiteInfoEntity implements Serializable{
	
	@Id
	@Column(name = "sum_info_id")
	private Long sumInfoId;
	
	@Column(name = "site_id")
	private Long siteId;
	
	@Column(name = "name")
	private String siteName;
	
	public Long getSumInfoId() {
		return sumInfoId;
	}
	public void setSumInfoId(Long sumInfoId) {
		this.sumInfoId = sumInfoId;
	}
	public Long getSiteId() {
		return siteId;
	}
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	
	
	
}
