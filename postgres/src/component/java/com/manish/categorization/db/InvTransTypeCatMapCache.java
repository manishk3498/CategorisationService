package com.manish.categorization.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.manish.categorization.repository.TransactionTypeCategoryRepo;
import com.manish.categorization.repository.entity.TransactionTypeCategoryEntity;

/**
 * 
 * @author mgarg
 *
 */
@Component
public class InvTransTypeCatMapCache {

	private static final Logger logger = LogManager.getLogger(InvTransTypeCatMapCache.class);
	
	@Inject
	private TransactionTypeCategoryRepo transactionTypeCategoryRepo;

	Map<String,String> txnTypeToTxnCatMap = new HashMap<>();
	
	@PostConstruct
	public void init(){
		if(logger.isInfoEnabled())
			logger.info("Initialising InvTransTypeCatMapCache");
		cacheData();
		if(logger.isInfoEnabled())
			logger.info("Finished intialising InvTransTypeCatMapCache");
	}

	private void cacheData(){
		List<TransactionTypeCategoryEntity> txnTypeCatEntityList = transactionTypeCategoryRepo.getTransactionTypeCategoryEntity();
		if(txnTypeCatEntityList != null && txnTypeCatEntityList.size() > 0){
			for (TransactionTypeCategoryEntity txnTypeCatMapEntity : txnTypeCatEntityList) {
				txnTypeToTxnCatMap.put(txnTypeCatMapEntity.getTransactionType().toLowerCase(), txnTypeCatMapEntity.getTxnCategoryName());
			}
		}
	}

	public Map<String, String> getTxnTypeToTxnCatMap() {
		return txnTypeToTxnCatMap;
	}

	public void setTxnTypeToTxnCatMap(Map<String, String> txnTypeToTxnCatMap) {
		this.txnTypeToTxnCatMap = txnTypeToTxnCatMap;
	}
	

}
