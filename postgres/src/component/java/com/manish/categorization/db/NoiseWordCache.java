package com.manish.categorization.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.manish.categorization.repository.NoiseWordRepo;
import com.manish.categorization.repository.entity.NoiseWordEntity;
/**
 * 
 * @author mgarg
 *
 */
@Component
public class NoiseWordCache {

	@Inject
	NoiseWordRepo noiseWordRepo;
	
	private static final Logger logger = LogManager.getLogger(NoiseWordCache.class);
	Map<String, Boolean> simpleDescNoiseWordMap = new HashMap<>();
	Map<Long, Boolean> simpleDescNoiseWordLengthMap = new HashMap<>();
	
	@PostConstruct
	public void init() {
		if(logger.isInfoEnabled())
			logger.info("Initializing Noise Word Cache.");
		
		noiseWordCache();
		
		if(logger.isInfoEnabled())
			logger.info("Finished Noise Word Cache.");
	}


	@PreDestroy
	public void cleanup() {
		simpleDescNoiseWordMap.clear();
		simpleDescNoiseWordLengthMap.clear();
	}

	private void noiseWordCache() {
		List<NoiseWordEntity> noiseWordEntityList = noiseWordRepo.getNoiseEntity();
		if (noiseWordEntityList != null && noiseWordEntityList.size() > 0)
			for (NoiseWordEntity noiseWordEntity : noiseWordEntityList) {
				String noiseWord = noiseWordEntity.getNoiseWord();
				if (StringUtils.isNotEmpty(noiseWord)) {
					simpleDescNoiseWordMap.put(noiseWord.toLowerCase(), Boolean.TRUE);
					String words[] = noiseWord.split(" ");
					if (words != null && words.length > 0)
						simpleDescNoiseWordLengthMap.put(new Long(words.length), Boolean.TRUE);
				}
			}
	}
	/**
	 * This method loads the the site info changed in the last one day
	 * The incremental load should happen if read from db is enabled
	 */
	public void cacheIncrementalNoiseWords(){
		try {
			// Code from db
			List<NoiseWordEntity> noiseWordEntityList = noiseWordRepo.getIncrementalNoiseWords();
			if (noiseWordEntityList != null && noiseWordEntityList.size() > 0)
				for (NoiseWordEntity noiseWordEntity : noiseWordEntityList) {
					String noiseWord = noiseWordEntity.getNoiseWord();
					if (StringUtils.isNotEmpty(noiseWord)) {
						simpleDescNoiseWordMap.put(noiseWord.toLowerCase(), Boolean.TRUE);
						String words[] = noiseWord.split(" ");
						if (words != null && words.length > 0)
							simpleDescNoiseWordLengthMap.put(new Long(words.length), Boolean.TRUE);
					}
				}
		} catch (Exception exception) {
			if(logger.isErrorEnabled())
				logger.error("Exception while incrementally loading NoiseWord Load ",exception);
		}
	}


	public Map<String, Boolean> getSimpleDescNoiseWordMap() {
		return simpleDescNoiseWordMap;
	}


	public void setSimpleDescNoiseWordMap(Map<String, Boolean> simpleDescNoiseWordMap) {
		this.simpleDescNoiseWordMap = simpleDescNoiseWordMap;
	}


	public Map<Long, Boolean> getSimpleDescNoiseWordLengthMap() {
		return simpleDescNoiseWordLengthMap;
	}


	public void setSimpleDescNoiseWordLengthMap(Map<Long, Boolean> simpleDescNoiseWordLengthMap) {
		this.simpleDescNoiseWordLengthMap = simpleDescNoiseWordLengthMap;
	}
	
	
}
