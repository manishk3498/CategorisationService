package com.manish.categorization.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.manish.categorization.repository.TxnMerchantTypeRepo;
import com.manish.categorization.repository.entity.TxnMerchantTypeEntity;

/**
 * 
 * @author smohanty
 *
 */

@Component
public class TxnMerchantTypeCache {
	
private static final Logger logger = LogManager.getLogger(TxnMerchantTypeCache.class);
	
	@Autowired
	TxnMerchantTypeRepo txnMerchantTypeRepo;
	
	private Map<String,Long> txnMerchantTypeNameToIdMap = new HashMap<String,Long>();
	
	@PostConstruct
	public void init(){
		
		List<TxnMerchantTypeEntity> TxnMerchantTypeList = txnMerchantTypeRepo.getTxnMerchantTypeEntity();
		if(TxnMerchantTypeList != null && TxnMerchantTypeList.size() > 0){
			for (TxnMerchantTypeEntity txnMerchantTypeEntity : TxnMerchantTypeList) {
				txnMerchantTypeNameToIdMap.put(txnMerchantTypeEntity.getTxnMerchantTypeName().toLowerCase(), txnMerchantTypeEntity.getTxnMerchantTypeId());
			}
		}
	}


	public Map<String, Long> getTxnMerchantTypeNameToIdMap() {
		return txnMerchantTypeNameToIdMap;
	}

	public void setTxnMerchantTypeNameToIdMap(Map<String, Long> txnMerchantTypeNameToIdMap) {
		this.txnMerchantTypeNameToIdMap = txnMerchantTypeNameToIdMap;
	}
	
	

}
