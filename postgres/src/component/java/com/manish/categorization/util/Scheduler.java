package com.manish.categorization.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.manish.categorization.algo.legacy.SlamBangWordIndexer;
import com.manish.categorization.db.GranularCategoryCache;
import com.manish.categorization.db.MccTxnCatMapCache;
import com.manish.categorization.db.NoiseWordCache;
import com.manish.categorization.db.SiteInfoCache;
import com.manish.categorization.db.SlamBangWordCache;
import com.manish.categorization.db.TransactionClassificationCache;
/**
 * 
 * @author mgarg
 *
 */
@Component
public class Scheduler {

	private static final Logger logger = LogManager.getLogger(Scheduler.class);

	@Autowired
	SlamBangWordCache slamBangWordCache;
	
	@Autowired
	SiteInfoCache siteInfoCache;
	
	@Autowired
	TransactionClassificationCache transactionClassificationCache;
	
	@Autowired
	MccTxnCatMapCache mccTxnCatMapCache;
	
	@Autowired
	NoiseWordCache noiseWordCache;
	
	@Autowired
	GranularCategoryCache granularCategoryCache;
	
	@Scheduled(fixedDelayString = "86400000")
	private void incrementallyLoadSlamBangWords(){
		
		//Incremental Loading Data - Tables which are present in Content Cache in PLatform
		if(logger.isInfoEnabled())
			logger.info("Started Slam Bang Words Incremental Load");
		
		slamBangWordCache.cacheIncrementalData();
		
		if(logger.isInfoEnabled())
			logger.info("Completed Slam Bang Words Incremental Load");
		
		if(logger.isInfoEnabled())
			logger.info("Started SiteInfo Incremental Load");
		
		siteInfoCache.loadIncrementSiteInfos();
		
		if(logger.isInfoEnabled())
			logger.info("Completed SiteInfo Incremental Load");
		
		if(logger.isInfoEnabled())
			logger.info("Started MeerkatTxnClassifier Incremental Load");
		
		transactionClassificationCache.cacheIncrementalData();
		
		if(logger.isInfoEnabled())
			logger.info("Completed MeerkatTxnClassifer Incremental Load");
		
		if(logger.isInfoEnabled())
			logger.info("Started MccTxnCatMap Incremental Load");
		
		mccTxnCatMapCache.cacheIncrementalData();
		
		if(logger.isInfoEnabled())
			logger.info("Completed MccTxnCatMap Incremental Load");
		
		if(logger.isInfoEnabled())
			logger.info("Started NoiseWord Incremental Load");
		
		noiseWordCache.cacheIncrementalNoiseWords();
		
		if(logger.isInfoEnabled())
			logger.info("Completed NoiseWord Incremental Load");
		granularCategoryCache.cacheIncrementalGranularCategory();
		
	}
	
	
}