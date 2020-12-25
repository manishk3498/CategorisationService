package com.manish.categorization.db;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.reflect.TypeToken;
import com.manish.categorization.repository.SiteNameRepo;
import com.manish.categorization.repository.entity.SiteInfoEntity;

@Component
public class SiteInfoCache {

	@Inject
	SiteNameRepo siteNameRepo;
	
	private static final Logger logger = LogManager.getLogger(SiteInfoCache.class);
	Map<Long, SiteInfo> siteInfoCache = new HashMap<>();

	@PostConstruct
	public void init() {
		if(logger.isInfoEnabled())
			logger.info("Initializing Site Info cache.");
		
		siteInfoCache();
		
		if(logger.isInfoEnabled())
			logger.info("Finished Site Info Cache.");
	}


	@PreDestroy
	public void cleanup() {
		siteInfoCache.clear();
	}

	private void siteInfoCache() {
		Type listType = new TypeToken<List<SiteInfo>>() {
		}.getType();
		List<SiteInfo> siteInfoList = new ArrayList<>();
		// Code from db
		List<SiteInfoEntity> siteInfoEntityList = siteNameRepo.getSiteName();
		if (siteInfoEntityList != null && siteInfoEntityList.size() > 0)
			for (SiteInfoEntity siteInfoEntity : siteInfoEntityList) {
				SiteInfo siteInfo = new SiteInfo();
				siteInfo.setSumInfoId(siteInfoEntity.getSumInfoId());
				siteInfo.setSiteId(siteInfoEntity.getSiteId());
				siteInfo.setSiteName(siteInfoEntity.getSiteName());
				siteInfoList.add(siteInfo);
			}
		if (siteInfoList != null && siteInfoList.size() > 0)
			for (SiteInfo siteInfo : siteInfoList) {
				siteInfoCache.put(siteInfo.getSumInfoId(), siteInfo);
			}
	}
	/**
	 * This method loads the the site info changed in the last one day
	 * The incremental load should happen if read from db is enabled
	 */
	public void loadIncrementSiteInfos(){
		try {
			List<SiteInfo> siteInfoList = new ArrayList<>();
				List<SiteInfoEntity> siteInfoEntityList = siteNameRepo.getIncrementalSiteName();
				if(siteInfoEntityList!=null && siteInfoEntityList.size() > 0)
				for (SiteInfoEntity siteInfoEntity : siteInfoEntityList) {
					SiteInfo siteInfo = new SiteInfo();
					siteInfo.setSumInfoId(siteInfoEntity.getSumInfoId());
					siteInfo.setSiteId(siteInfoEntity.getSiteId());
					siteInfo.setSiteName(siteInfoEntity.getSiteName());
					siteInfoList.add(siteInfo);
				}
				if(siteInfoList != null && siteInfoList.size() > 0){
					for (SiteInfo siteInfo : siteInfoList) {
						siteInfoCache.put(siteInfo.getSumInfoId(), siteInfo);
					}
				}	
		} catch (Exception exception) {
			if(logger.isErrorEnabled())
				logger.error("Exception while incrementally loading SiteInfo Load ",exception);
		}
	}
	
	public Map<Long, SiteInfo> getSiteInfo(){
		return siteInfoCache;
	}
	
}
