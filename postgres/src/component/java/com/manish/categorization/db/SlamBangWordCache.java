package com.manish.categorization.db;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.manish.categorization.BaseType;
import com.manish.categorization.Container;
import com.manish.categorization.repository.SlamBangWordRepo;
import com.manish.categorization.repository.entity.SlamBangWordEntity;
import com.manish.categorization.util.Constants;
/**
 * 
 * @author mgarg
 *
 */
@Component
public class SlamBangWordCache {

	private static final Logger logger = LogManager.getLogger(SlamBangWordCache.class);
	
	@Autowired
	TransactionCategoryCache categoryCache;

	@Inject
	SlamBangWordRepo slamBangWordRepo;
	
	Map<String,List<SlamBangWord>> slamBangWordToSlamBangWordObjListMap = new ConcurrentHashMap<String,List<SlamBangWord>>();
	
	//Key  - container.baseType Value - Map -> Key - slamBangWord, Value - SlamBangWordObject. This map should be used for merger disabled requests
	Map<String,Map<String,SlamBangWord>> slamBangWordMap = new ConcurrentHashMap<String, Map<String,SlamBangWord>>();
	
	//Loaded during incremental load
	Map<String,Map<String,SlamBangWord>> clonedSlamBangWordMap = new ConcurrentHashMap<String, Map<String,SlamBangWord>>();
	
	@PostConstruct
	public void init() {
		if(logger.isInfoEnabled())
			logger.info("Initializing Slam Bang Word Cache");
		initSlamBangWordMap(slamBangWordMap);
		cacheData(slamBangWordMap);
		if(logger.isInfoEnabled())
			logger.info("Finished Slam Bang Word Cache");
	}
	/**
	 * This is to initialise the map for every container and baseType combination
	 * @param slamBangWordMap
	 */
	private void initSlamBangWordMap(Map<String,Map<String,SlamBangWord>> slamBangWordMap){
		if(slamBangWordMap == null){
			slamBangWordMap = new ConcurrentHashMap<String, Map<String,SlamBangWord>>();
		}
		for (Container container : Container.values()) {
			String debitContainerKey = container+Constants.HYPHEN+BaseType.DEBIT.toString();
			String creditContainerKey = container+Constants.HYPHEN+BaseType.CREDIT.toString();
			slamBangWordMap.put(debitContainerKey, new ConcurrentHashMap<String, SlamBangWord>());		
			slamBangWordMap.put(creditContainerKey, new ConcurrentHashMap<String, SlamBangWord>());
		}
	}
	/**
	 * This method add the keyword for all the containers for a specific baseType
	 * @param slamBangWordMap
	 * @param baseType
	 * @param slamBangWord
	 */
	private void putKeyForAllContainers(Map<String,Map<String,SlamBangWord>> slamBangWordMap,String baseType,SlamBangWord slamBangWord){
		for (Container container : Container.values()) {
			String key = container+Constants.HYPHEN+baseType;
			Map<String,SlamBangWord> map = slamBangWordMap.get(key);
			map.put(slamBangWord.getSlamBangWord().toLowerCase(), slamBangWord);		
		}
	}
	/**
	 * This methods ass the keywords to the map for a specific container and baseType
	 * @param slamBangWordMap
	 * @param baseType
	 * @param container
	 * @param slamBangWord
	 */
	private void putKeyForContainer(Map<String,Map<String,SlamBangWord>> slamBangWordMap,String baseType,String container,SlamBangWord slamBangWord){
			String key = container+Constants.HYPHEN+baseType;
			Map<String,SlamBangWord> map = slamBangWordMap.get(key);
			map.put(slamBangWord.getSlamBangWord().toLowerCase(), slamBangWord);		
	}
	private void cacheData(Map<String,Map<String,SlamBangWord>> slamBangWordMap){	
		try {
			System.out.println("Started loading slam bang words");
			if(logger.isInfoEnabled()){
				logger.info("Started loading slam bang words");
			}
			long startTime = System.currentTimeMillis();
			List<SlamBangWordEntity> slamBangWordsEntityList =slamBangWordRepo.getSlamBangWordEntity();
			System.out.println("Completed Query Execution : Time taken "+ (System.currentTimeMillis()-startTime));
			if(logger.isInfoEnabled()){
				logger.info("Completed Query Execution : Time taken "+ (System.currentTimeMillis()-startTime));
			}
			if(slamBangWordsEntityList != null && slamBangWordsEntityList.size() > 0){
				for (SlamBangWordEntity slamBangWordEntity : slamBangWordsEntityList) {
					SlamBangWord sbw = new SlamBangWord();
					sbw.setSlamBangWordId(slamBangWordEntity.getSlamBangWordId());
					sbw.setSlamBangWord(slamBangWordEntity.getSlamBangWord().toLowerCase());
					if(slamBangWordEntity.getIsMerchantDeleted() != null && slamBangWordEntity.getIsMerchantDeleted().longValue() == 0){
						sbw.setMerchantId(slamBangWordEntity.getMerchantId() != null ? slamBangWordEntity
								.getMerchantId() : null);
						sbw.setMerchantName(slamBangWordEntity.getMerchantName() != null ? slamBangWordEntity
								.getMerchantName() : "");
						sbw.setIsDummyMerchant(slamBangWordEntity.getIsDummyMerchant());
					}else{
						sbw.setMerchantId(null);
						sbw.setMerchantName(null);
						sbw.setIsDummyMerchant(null);
					}					
					sbw.setSumInfoId(slamBangWordEntity.getSumInfoId() != null ? slamBangWordEntity
							.getSumInfoId() : null);
					sbw.setTransactionCategoryName(slamBangWordEntity.getTransactionCategoryName());
					sbw.setTransactionType(slamBangWordEntity.getTransactionType());
					sbw.setRegionId(slamBangWordEntity.getRegionId() != null ? slamBangWordEntity
							.getRegionId() : null);
					sbw.setPriority(slamBangWordEntity.getPriority());					
					if(slamBangWordEntity.getIsDeleted() != null && slamBangWordEntity.getIsDeleted().longValue() == 1){
						sbw.setDeleted(Boolean.TRUE);
					}else{
						sbw.setDeleted(Boolean.FALSE);
					}
					sbw.setContainers(slamBangWordEntity.getContainers());
					String containers = slamBangWordEntity.getContainers();
					if(containers == null){
						addSlamBangWordToMaps(slamBangWordMap,null, sbw, Boolean.FALSE);
					}else{
						String containerArr[] = containers.split(Constants.COMMA);
						if(containerArr != null && containerArr.length > 0){
							List<String> containerList = Arrays.asList(containerArr);
							addSlamBangWordToMaps(slamBangWordMap,containerList, sbw, Boolean.FALSE);
						}
					}
				}
			}
			System.out.println("Completed Loading Slam Bang Words");
			if(logger.isInfoEnabled()){
				logger.info("Completed Loading Slam Bang Words");
			}
		} catch (Exception exception) {
			System.out.println("Exception while loading Slam Bang Words");
			if(logger.isFatalEnabled()){
				logger.info("Exception while loading Slam Bang Words",exception);
			}
		}
	}
	/**
	 * This is a method for adding the keywords in the Slam Bang Word honouring the base type and container of the transaction
	 * @param slamBangWordMap
	 * @param containers
	 * @param slamBangWord
	 * @param mergerEnabled
	 */
	private void addSlamBangWordToMaps(Map<String,Map<String,SlamBangWord>> slamBangWordMap,List<String> containers,SlamBangWord slamBangWord,boolean mergerEnabled){
		
		TransactionCategory category = categoryCache.getTransactionCategory(slamBangWord.getTransactionCategoryName(),mergerEnabled);
		Set<ContainerBaseTypeRule> rules = category.getRules();
		if(containers == null || containers.size() == 0){
			if (rules != null && rules.size()>0) {
				for (ContainerBaseTypeRule rule : rules) {
					putKeyForContainer(slamBangWordMap, rule.getBaseType(), rule.getContainer(), slamBangWord);
				}
			}else{
				putKeyForAllContainers(slamBangWordMap, BaseType.DEBIT.toString(), slamBangWord);
				putKeyForAllContainers(slamBangWordMap, BaseType.CREDIT.toString(), slamBangWord);
			}
		}else{
			if (rules != null && rules.size()>0) {
				for (ContainerBaseTypeRule rule : rules) {
					if(containers.contains(rule.getContainer())){
						putKeyForContainer(slamBangWordMap, rule.getBaseType(), rule.getContainer(), slamBangWord);
					}
				}
			}else{
				for (String container  : containers) {
					putKeyForContainer(slamBangWordMap, BaseType.CREDIT.toString(), container, slamBangWord);
					putKeyForContainer(slamBangWordMap,BaseType.DEBIT.toString(), container, slamBangWord);
				}
			}
		}
	}
	/**
	 * This method is used to incrementally load the Slam Bang Words
	 *  Slam Bang Word Cache Map is deep cloned and operations are performed on that map for incremental keyword loading
	 *  and finally the original map reference is replaced with the cloned map
	 */
	public void cacheIncrementalData(){		
		try {
			initSlamBangWordMap(clonedSlamBangWordMap);
			cacheData(clonedSlamBangWordMap);
			slamBangWordMap.clear();
			slamBangWordMap.putAll(clonedSlamBangWordMap);
			clonedSlamBangWordMap.clear();
		} catch (Exception exception) {
			if(logger.isErrorEnabled()){
				logger.error("Exception while Slam Bang Word Cache Incremental Load ",exception);
			}
		}
		
	}

	public Map<String,List<SlamBangWord>> getSlamBangWordToSlamBangWordObjListMap(){
		return slamBangWordToSlamBangWordObjListMap;
	}

	public Map<String, Map<String, SlamBangWord>> getSlamBangWordMap() {
		return slamBangWordMap;
	}

	public void setSlamBangWordMap(Map<String, Map<String, SlamBangWord>> slamBangWordMap) {
		this.slamBangWordMap = slamBangWordMap;
	}	
}
