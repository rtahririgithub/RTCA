package com.telus.credit.util;



import java.util.Iterator;
import java.util.Vector;

import org.apache.xmlbeans.XmlObject;
import org.ehcache.Cache;
import org.ehcache.Cache.Entry;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

public class ESBCacheHelper {
	 
    private static CacheManager cacheManager;
    private static Cache<String, Object> refPdsCache=null;
    private static String cacheName = "ESBCache";
 
	private static void createCache() {
		if(cacheManager==null){
	        cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
	        cacheManager.init();
	        CacheConfigurationBuilder<String, Object> aCacheConfigurationBuilder = 
	        		CacheConfigurationBuilder.newCacheConfigurationBuilder
	        		(String.class, Object.class,ResourcePoolsBuilder.heap(100)); 
	        refPdsCache = cacheManager.createCache(cacheName, aCacheConfigurationBuilder);
	        
    	}
	}
    public static void addXmlObjectToCache(String key, XmlObject value) {
    	createCache(); 
    	if(refPdsCache!=null && key!=null && value!=null){
    		refPdsCache.put(key,value);    	 
    	}else{
    		LogUtil.logError("[Failed to add key="+key+",value="+value +"]");
    	}          	
    }
    public static XmlObject getXmlObjectFromCache(String key) {
    	XmlObject value=null;
    	if(refPdsCache!=null && key!=null){
    		value=(XmlObject) refPdsCache.get(key);
    	}
        return  value;
    } 
    
    public static void addObjectToCache(String key, Object value) {
    	createCache(); 
    	if( refPdsCache!=null && key!=null&& value!=null){
    		refPdsCache.put(key,value);    	 
    	}else{
    		LogUtil.logError("[Failed to add key="+key+",value="+value +"]");
    	}  
        
    }
    public static Object getObjectFromCache(String key) {
    	Object value=null;
    	if(refPdsCache!=null && key!=null){
    		value=(Object) refPdsCache.get(key);
    	}
        return  value;
    } 
    public static String getOperationalDataFromCache() {
    	LogUtil.logInfo("*****************start_getOperationalDataFromCache*****************");
        	StringBuffer cacheContent=new StringBuffer();;
    		if(refPdsCache!=null){
    			Iterator<Entry<String, Object>> refPdsCacheIterator = refPdsCache.iterator();			
    			if(refPdsCacheIterator!=null ){
    			   	while (refPdsCacheIterator.hasNext()) {
    						Entry<String, Object> aEntry = refPdsCacheIterator.next();
    						if(aEntry!=null){
    							String key = aEntry.getKey();
    							Object val = aEntry.getValue();
    							if(
    											CONSTANTS.UCDORMANTFLAG_CACHE_KEY.equalsIgnoreCase(key) 
    										||	CONSTANTS.UCTOPMATCHSIMFLAG_CACHE_KEY.equalsIgnoreCase(key) 
    										||	CONSTANTS.WLN_WCDAP_LOGGING_ENABLED_CACHE_KEY.equalsIgnoreCase(key)    										
    										||	CONSTANTS.UC_ENVID_CACHE_KEY.equalsIgnoreCase(key) 
    										||	CONSTANTS.UC_CACHELOADTIMESTAMP_CACHE_KEY.equalsIgnoreCase(key)
    										){
    								cacheContent.append( "\n"+ key + "=" + val);
    							}	
    						}
    				}	
    			}    
    		}
    		if(cacheContent.length()==0){
    			cacheContent.append("Empty");
    		}
    		LogUtil.logInfo("\n[cacheContent:" + cacheContent.toString() +"\n]\n");
    		LogUtil.logInfo("*****************end_getOperationalDataFromCache*****************");
    		return  cacheContent.toString();
        } 
    
    public static String getAllObjectFromCache() {
    	LogUtil.logInfo("*****************start_getAllObjectFromCache*****************");
    	StringBuffer cacheContent=new StringBuffer();;
		if(refPdsCache!=null){
			Iterator<Entry<String, Object>> refPdsCacheIterator = refPdsCache.iterator();			
			if(refPdsCacheIterator!=null ){
			   	while (refPdsCacheIterator.hasNext()) {
						Entry<String, Object> aEntry = refPdsCacheIterator.next();
						if(aEntry!=null){
							String key = aEntry.getKey();
							Object val = aEntry.getValue();
							if(val instanceof Vector){
								Vector valVector=(Vector)val;
								String valVectorStr="";
								for (Object valVectorStrObj : valVector) {
									valVectorStr= valVectorStr  + valVectorStrObj;
									valVectorStr= valVectorStr +",";
								}
								cacheContent.append( "\n"+ key + ":" + valVectorStr);
							}else{
								if(val instanceof String[]){
									String[] valArray=(String[])val;
									String valArrayStr="";
									for (int i = 0; i < valArray.length; i++) {
										valArrayStr= valArrayStr  + valArray[i];	
										valArrayStr= (i != (valArray.length-1))?valArrayStr +",":valArrayStr;
									}
									cacheContent.append( "\n"+ key + ":" + valArrayStr);
								}else{
									if(val instanceof String [][] ){
										String [][]  valArray=(String [][])val;
										String valArrayStr="";
										for (int i = 0; i < valArray.length; i++) {
											valArrayStr= valArrayStr +"[";
											for (int j = 0; j < valArray[i].length; j++) {
												valArrayStr= valArrayStr  + valArray[i][j];
												valArrayStr= (j != (valArray[i].length-1))?valArrayStr +",":valArrayStr;
											}
											valArrayStr= valArrayStr +"]";
										}
										cacheContent.append( "\n"+ key + ":" + valArrayStr);
									}else{										
										if(CONSTANTS.UC_REFPDS_APPID_CACHE_KEY.equalsIgnoreCase(key) ){
											cacheContent.append( "\n"+ key + ":" + "All refpds tables.");
										}else{
											cacheContent.append( "\n"+ key + ":" + val);
										}										
									}
								}
							}
						}
				}	
			}    
		}
//CONSTANTS.UC_REFPDS_APPID_CACHE_KEY
		if(cacheContent.length()==0){
			cacheContent.append("Empty");
		}
		LogUtil.logInfo("\n[cacheContent:" + cacheContent.toString() +"\n]");
		LogUtil.logInfo("*****************end_getAllObjectFromCache*****************");
		return  cacheContent.toString();
}      
    
    public static void clearCache() {
    	LogUtil.logInfo("*****************start_clearCache*****************");
    	try {
			if(refPdsCache!=null){
				refPdsCache.clear();
			}
		} catch (Throwable e) {
			LogUtil.logException(e);
		}
    	LogUtil.logInfo("*****************end_clearCache*****************");
    }           

} 