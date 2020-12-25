package com.manish.categorization.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.manish.categorization.repository.HlMasterCategoryMappingRepository;
import com.manish.categorization.repository.entity.HLMasterMappingEntity;

/**
 * 
 * @author mgarg
 *
 */
@Component
public class HlMasterCategoryMappingCache {

	@Autowired
	HlMasterCategoryMappingRepository hlMasterCategpryMappingRepository;
	
	private static final Logger logger = LogManager.getLogger(HlMasterCategoryMappingCache.class);
	
	private Map<Long,Long> txnCatIdToHlCatIdMap = new HashMap<>();
	private Map<Long,String> highLevelCategoryIdToNameMap = new HashMap<Long,String>();
	
	@PostConstruct
	public void init(){
		
		List<HLMasterMappingEntity> hlMasterCategoryMappingList = hlMasterCategpryMappingRepository.getHlMasterMappingEntity();
		if(hlMasterCategoryMappingList != null && hlMasterCategoryMappingList.size() > 0){
			for (HLMasterMappingEntity hlMasterMappingEntity : hlMasterCategoryMappingList) {
				txnCatIdToHlCatIdMap.put(hlMasterMappingEntity.getTransactionCategoryId(), hlMasterMappingEntity.getHlTransactionCategoryId());
				highLevelCategoryIdToNameMap.put(hlMasterMappingEntity.getHlTransactionCategoryId(),hlMasterMappingEntity.getHlTransactionCategoryName());
			}
		}
	}

	public Map<Long, Long> getTxnCatIdToHlCatIdMap() {
		return txnCatIdToHlCatIdMap;
	}

	public void setTxnCatIdToHlCatIdMap(Map<Long, Long> txnCatIdToHlCatIdMap) {
		this.txnCatIdToHlCatIdMap = txnCatIdToHlCatIdMap;
	}

	public Map<Long, String> getHighLevelCategoryIdToNameMap() {
		return highLevelCategoryIdToNameMap;
	}

	public void setHighLevelCategoryIdToNameMap(Map<Long, String> highLevelCategoryIdToNameMap) {
		this.highLevelCategoryIdToNameMap = highLevelCategoryIdToNameMap;
	}	
	
	
}

