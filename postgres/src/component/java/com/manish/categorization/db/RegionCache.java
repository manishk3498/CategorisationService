package com.manish.categorization.db;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.manish.categorization.repository.RegionRepo;
import com.manish.categorization.repository.entity.RegionEntity;
import com.manish.categorization.util.Constants;

/**
 * 
 * @author mgarg
 *
 */
@Component
public class RegionCache {

	private static final Logger logger = LogManager.getLogger(RegionCache.class);
	
	@Autowired
	RegionRepo regionRepo;
	
	Map<Long,List<String>> regionIdToServicesListMap = new HashMap<>();
	
	@PostConstruct
	public void init(){
		
		if(logger.isInfoEnabled())
			logger.info("Initialising Region Cache");
		List<RegionEntity> regionEntities = regionRepo.getRegionEntity();
		if(regionEntities != null && regionEntities.size() > 0){
			for (RegionEntity regionEntity : regionEntities) {
				Long regionId = regionEntity.getRegionId();
				String tdeServices = regionEntity.getTdeServices();
				if(StringUtils.isNotEmpty(tdeServices)){
					String servicesArr[] = tdeServices.split(Constants.COMMA);
					regionIdToServicesListMap.put(regionId, Arrays.asList(servicesArr));
				}
			}
		}
		if(logger.isInfoEnabled())
			logger.info("Finished initialising Region Cache");
	}

	public Map<Long, List<String>> getRegionIdToServicesListMap() {
		return regionIdToServicesListMap;
	}

	public void setRegionIdToServicesListMap(Map<Long, List<String>> regionIdToServicesListMap) {
		this.regionIdToServicesListMap = regionIdToServicesListMap;
	}
	
}
