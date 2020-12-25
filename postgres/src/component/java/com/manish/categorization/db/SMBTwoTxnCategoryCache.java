package com.manish.categorization.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.manish.categorization.repository.SMBTwoTxnCategoryRepo;
import com.manish.categorization.repository.TxnMerchantTypeRepo;
import com.manish.categorization.repository.entity.SMBTwoTxnCategoryEntity;
import com.manish.categorization.repository.entity.TxnMerchantTypeEntity;

/**
 * 
 * @author smohanty
 *
 */

@Component
public class SMBTwoTxnCategoryCache {
	
private static final Logger logger = LogManager.getLogger(SMBTwoTxnCategoryCache.class);
	
	@Autowired
	SMBTwoTxnCategoryRepo smbTwoTxnCategoryRepo;
	
	private Map<String,Long> smbTwoTxnCategoryNameToIdMap = new HashMap<String,Long>();
	
	@PostConstruct
	public void init(){
		
		List<SMBTwoTxnCategoryEntity> SMBTwoTxnCategoryList = smbTwoTxnCategoryRepo.getSMBTwoTxnCategoryEntity();
		if(SMBTwoTxnCategoryList != null && SMBTwoTxnCategoryList.size() > 0){
			for (SMBTwoTxnCategoryEntity smbTwoTxnCategoryEntity : SMBTwoTxnCategoryList) {
				smbTwoTxnCategoryNameToIdMap.put(smbTwoTxnCategoryEntity.getSmbTwoTxnCategoryName().toLowerCase(), smbTwoTxnCategoryEntity.getSmbTwoTxnCategroyId());
			}
		}
	}

	public Map<String, Long> getSmbTwoTxnCategoryNameToIdMap() {
		return smbTwoTxnCategoryNameToIdMap;
	}

	public void setSmbTwoTxnCategoryNameToIdMap(Map<String, Long> smbTwoTxnCategoryNameToIdMap) {
		this.smbTwoTxnCategoryNameToIdMap = smbTwoTxnCategoryNameToIdMap;
	}

	

}
