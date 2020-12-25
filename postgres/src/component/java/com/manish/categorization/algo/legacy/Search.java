package com.manish.categorization.algo.legacy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.manish.categorization.BaseType;
import com.manish.categorization.db.SlamBangWord;
import com.manish.categorization.db.SlamBangWordCache;
import com.manish.categorization.util.Constants;

@Component
public class Search {

	private static Logger logger = LogManager.getLogger(Search.class);

	private static Search _instance = null;
	
	public static Search getInstance() {
		if (_instance == null) {
			_instance = new Search();
		}
		return _instance;
	}
	
	@Autowired
	Directory luceneDirectory;
	
	@Autowired
	SlamBangWordIndexer indexer;
	
	@Autowired
	SlamBangWordCache slamBangWordCache;

	IndexSearcher searcher = null;
	
	Map<String,Map<String,SlamBangWord>> slamBangWordMap;
	
	@PostConstruct
	public void init() {
		if(logger.isDebugEnabled())
			logger.debug("Initiaizing the LuceneSearch.");
		slamBangWordMap = slamBangWordCache.getSlamBangWordMap();
		if(logger.isDebugEnabled())
			logger.debug("Completed the MerchantIndexer.");
	}
	public List<SlamBangWord> search(String token,String baseType,String container,boolean mergerEnabled, boolean containerBaseCheck){	
		
		List<SlamBangWord> slamBangWordList = null;
		slamBangWordList = getSlamBangWordList(token, baseType, container, slamBangWordMap, containerBaseCheck);
		return slamBangWordList;
	}
	/**
	 * 1) If container base check is disabled then both the debit and credit category keywords should be applied
	 * If base type is debit/credit then appropriate keyword is picked according to the base type
	 * else if base type is null or other/unknown then both the debit and credit category keywords will be picked
	 * @param token
	 * @param baseType
	 * @param container
	 * @param slamBangWordMap
	 * @param containerBaseCheck TODO
	 * @return
	 */
	private List<SlamBangWord> getSlamBangWordList(String token, String baseType, String container,
			Map<String,Map<String,SlamBangWord>> slamBangWordMap, boolean containerBaseCheck) {
		List<SlamBangWord> slamBangWordList = new ArrayList<SlamBangWord>();
		//BaseType may be unkonwn/other or may not be present. In this case both credit and debit keywords should be picked up
		if(!containerBaseCheck || (StringUtils.isNotEmpty(baseType) && !baseType.equals(BaseType.DEBIT.toString()) && !baseType.equals(BaseType.CREDIT.toString()))){
			Map<String,SlamBangWord> map = slamBangWordMap.get(container+Constants.HYPHEN+BaseType.DEBIT.toString()); 
			SlamBangWord slamBangWord = map != null ? map.get(token) : null;
			if(slamBangWord != null){
				slamBangWordList.add(slamBangWord);
			}
			map = slamBangWordMap.get(container+Constants.HYPHEN+BaseType.CREDIT.toString());
			slamBangWord = map != null ? map.get(token) : null;
			if(slamBangWord != null){
				slamBangWordList.add(slamBangWord);
			}
		}else{
			Map<String,SlamBangWord> map = slamBangWordMap.get(container+Constants.HYPHEN+baseType);
			SlamBangWord slamBangWord = map != null ? map.get(token) : null;
			if(slamBangWord != null && !slamBangWord.isDeleted()){
				slamBangWordList.add(slamBangWord);
			}
		}
		return slamBangWordList;
	}
}
