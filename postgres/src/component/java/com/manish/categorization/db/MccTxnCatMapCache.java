package com.manish.categorization.db;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.manish.categorization.repository.MCCTxnCatMapRepo;
import com.manish.categorization.repository.entity.MCCTxnCategoryEntity;
/**
 * 
 * @author mgarg
 *
 */
@Component
public class MccTxnCatMapCache {

	private static final Logger logger = LogManager.getLogger(MccTxnCatMapCache.class);
	
	@Inject
	MCCTxnCatMapRepo mccTxnCatMapRepo;
	
	Map<Long,String> mccCodeToTxnCatMap = new HashMap<>();
	
	
	@PostConstruct
	public void init(){
		if(logger.isInfoEnabled())
			logger.info("Initialising MccTxnCatMapCache");
		
		cacheData();
		
		if(logger.isInfoEnabled())
			logger.info("Finished intialising MccTxnCatMapCache");
	}
	
	private void cacheData(){
		List<MCCTxnCategoryEntity> mccTxnCategoryEntityList = mccTxnCatMapRepo.getMCCTxnCategoryEntity();
		if(mccTxnCategoryEntityList != null && mccTxnCategoryEntityList.size() > 0){
			for (MCCTxnCategoryEntity mccTxnCategoryEntity : mccTxnCategoryEntityList) {
				mccCodeToTxnCatMap.put(mccTxnCategoryEntity.getMccCode(), mccTxnCategoryEntity.getTransactionCategoryName());
			}
		}
	}
	public Map<Long,String> getMccTxnCatMap(){
		return mccCodeToTxnCatMap;
	}
	
	public void cacheIncrementalData(){
			try {
				List<MCCTxnCategoryEntity> mccTxnCategoryEntityList = mccTxnCatMapRepo.getIncrementalMCCTxnCategoryEntity();
				if(mccTxnCategoryEntityList != null && mccTxnCategoryEntityList.size() > 0){
					for (MCCTxnCategoryEntity mccTxnCategoryEntity : mccTxnCategoryEntityList) {
						mccCodeToTxnCatMap.put(mccTxnCategoryEntity.getMccCode(), mccTxnCategoryEntity.getTransactionCategoryName());
					}
				}
			} catch (Exception exception) {
				if(logger.isErrorEnabled())
					logger.error("Exception while incremental load of MccTxnCatMapCache", exception);
			}
	}
}
