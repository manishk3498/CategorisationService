package com.manish.categorization;

import java.util.HashMap;
import java.util.Map;

/**
 * This class used to load the region based URL from properties file
 * @author mdey
 *
 */
public class URLConfig
{
	public Map<Integer, String> regionMap = new HashMap<>();
	public URLConfig(){
		
	}
	public URLConfig(Map<Integer,String> regionMap){
		this.regionMap = regionMap;
		doURLHealthCheck(regionMap);
	}
    private void doURLHealthCheck(Map<Integer, String> regionMap) {
    	MeerkatHealthCheck meerkatHealthCheck = new MeerkatHealthCheck();
    	meerkatHealthCheck.healthCheck(regionMap);
    	
    }
    public Map<Integer, String> getRegion(){
    	return regionMap;
    }
    
}