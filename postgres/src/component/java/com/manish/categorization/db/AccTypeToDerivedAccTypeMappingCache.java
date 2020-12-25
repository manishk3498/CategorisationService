package com.manish.categorization.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @author mgarg
 *
 */
@Component
public class AccTypeToDerivedAccTypeMappingCache {

	private static final Logger logger = LogManager.getLogger(AccTypeToDerivedAccTypeMappingCache.class);
	
	private static Map<String,String> acctTypeToDerivedAccTypeMap = new HashMap<String,String>();
	
	@PostConstruct
	public void init() {
		
		if(logger.isInfoEnabled())
			logger.info("Initializing Acct Type to Derived Account type cache");
		cacheData();
		if(logger.isInfoEnabled())
			logger.info("Finished Initializing acct type to derived acc type cache");
	}
	/**
	 * This function reads the account type to derived account type mappings from the JS file and stores those mappings inmemory in HashMap
	 */
	private void cacheData(){
		
		Type listType = new TypeToken<List<AccTypeToDerivedAccTypeMapping>>() {
		}.getType();
		InputStream in = this.getClass().getResourceAsStream("/AccountTypeToDerivedAccountTypeMappings.js");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.disableHtmlEscaping();
		gsonBuilder.setPrettyPrinting();
		List<AccTypeToDerivedAccTypeMapping> list = gsonBuilder.create().fromJson(reader, listType);
		if(list != null && list.size() > 0){
			for (AccTypeToDerivedAccTypeMapping accTypeToDerivedAccTypeMapping : list) {
				acctTypeToDerivedAccTypeMap.put(accTypeToDerivedAccTypeMapping.getAccountType(), accTypeToDerivedAccTypeMapping.getDerivedAccountType());
			}
		}
	}
	public static Map<String, String> getAcctTypeToDerivedAccTypeMap() {
		return acctTypeToDerivedAccTypeMap;
	}
	
}
