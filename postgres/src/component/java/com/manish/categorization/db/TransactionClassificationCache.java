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
import com.manish.categorization.repository.TransactionClassificationRepo;
import com.manish.categorization.repository.entity.TransactionClassificationEntity;

@Component
public class TransactionClassificationCache {

	@Inject
	TransactionClassificationRepo transactionClassificationRepo;
		
	List<MeerkatTxnClassifier> meerkatTxnClassifierList = new ArrayList<>();
	
	private Map<String,String> transactionClassificationCache = new HashMap<String,String>();
	
	private Map<String,Long> meerkatTxnTypeToIdMap = new HashMap<String,Long>();
	
	private Map<String,Long> meerkatTxnSubTypeToIdMap = new HashMap<String,Long>();
	
	private static final Logger logger = LogManager.getLogger(TransactionClassificationCache.class);
	
	@PostConstruct
	public void init() {
		if(logger.isInfoEnabled())
			logger.info("Initializing Meerkat Transaction classification cache.");
		
		classificationCache();
		
		if(logger.isInfoEnabled())
			logger.info("Finished Initializing Meerkat Transaction classification Cache.");
	}

	@PreDestroy
	public void cleanup() {
		meerkatTxnClassifierList.clear();
	}

	private void classificationCache() {
		Type listType = new TypeToken<List<MeerkatTxnClassifier>>() {
		}.getType();
		// Code from db
		List<TransactionClassificationEntity> classificationList = transactionClassificationRepo
				.getTransactionClassificationInfo();
		if (classificationList != null && classificationList.size() > 0) {
			for (TransactionClassificationEntity txnClassificationEntity : classificationList) {
				MeerkatTxnClassifier meerkatTxnClassifier = new MeerkatTxnClassifier();
				meerkatTxnClassifier.setClassifierId(txnClassificationEntity.getClassifierId());
				meerkatTxnClassifier.setClassifierName(txnClassificationEntity.getClassifierName());
				meerkatTxnClassifier.setTxnTypeId(txnClassificationEntity.getTxnTypeId());
				meerkatTxnClassifier.setTxnTypeName(txnClassificationEntity.getTxnTypeName());
				meerkatTxnClassifier.setTxnSubTypeId(txnClassificationEntity.getTxnSubTypeId());
				meerkatTxnClassifier.setTxnSubTypeName(txnClassificationEntity.getTxnSubTypeName());
				meerkatTxnTypeToIdMap.put(txnClassificationEntity.getTxnTypeName(),
						txnClassificationEntity.getTxnTypeId());
				meerkatTxnSubTypeToIdMap.put(txnClassificationEntity.getTxnSubTypeName(),
						txnClassificationEntity.getTxnSubTypeId());
				String classifierKey = String.valueOf(txnClassificationEntity.getTxnTypeId()) + "-"
						+ String.valueOf(txnClassificationEntity.getTxnSubTypeId());
				transactionClassificationCache.put(classifierKey,
						txnClassificationEntity.getClassifierName());
				meerkatTxnClassifierList.add(meerkatTxnClassifier);
			}
		}
	}
	public Map<String,String> getclassificationCache(){
		return transactionClassificationCache;
	}
	
	public Map<String, Long> getMeerkatTxnTypeToIdMap() {
		return meerkatTxnTypeToIdMap;
	}

	public void setMeerkatTxnTypeToIdMap(Map<String, Long> meerkatTxnTypeToIdMap) {
		this.meerkatTxnTypeToIdMap = meerkatTxnTypeToIdMap;
	}

	public Map<String, Long> getMeerkatTxnSubTypeToIdMap() {
		return meerkatTxnSubTypeToIdMap;
	}

	public void setMeerkatTxnSubTypeToIdMap(Map<String, Long> meerkatTxnSubTypeToIdMap) {
		this.meerkatTxnSubTypeToIdMap = meerkatTxnSubTypeToIdMap;
	}

	public void cacheIncrementalData() {
		try {
			List<TransactionClassificationEntity> classificationList = transactionClassificationRepo
					.getIncrementalTransactionClassificationInfo();
			if (classificationList != null && classificationList.size() > 0) {
				for (TransactionClassificationEntity txnClassificationEntity : classificationList) {
					MeerkatTxnClassifier meerkatTxnClassifier = new MeerkatTxnClassifier();
					meerkatTxnClassifier.setClassifierId(txnClassificationEntity.getClassifierId());
					meerkatTxnClassifier.setClassifierName(txnClassificationEntity
							.getClassifierName());
					meerkatTxnClassifier.setTxnTypeId(txnClassificationEntity.getTxnTypeId());
					meerkatTxnClassifier.setTxnTypeName(txnClassificationEntity.getTxnTypeName());
					meerkatTxnClassifier.setTxnSubTypeId(txnClassificationEntity.getTxnSubTypeId());
					meerkatTxnClassifier.setTxnSubTypeName(txnClassificationEntity
							.getTxnSubTypeName());
					meerkatTxnTypeToIdMap.put(txnClassificationEntity.getTxnTypeName(),
							txnClassificationEntity.getTxnTypeId());
					meerkatTxnSubTypeToIdMap.put(txnClassificationEntity.getTxnSubTypeName(),
							txnClassificationEntity.getTxnSubTypeId());
					String classifierKey = String.valueOf(txnClassificationEntity.getTxnTypeId())
							+ "-" + String.valueOf(txnClassificationEntity.getTxnSubTypeId());
					transactionClassificationCache.put(classifierKey,
							txnClassificationEntity.getClassifierName());
					meerkatTxnClassifierList.add(meerkatTxnClassifier);
				}
			}
		} catch (Exception exception) {
			if (logger.isErrorEnabled())
				logger.error(
						"Exception while incrementally loading Transaction Classification Cache",
						exception);
		}
	}
}
