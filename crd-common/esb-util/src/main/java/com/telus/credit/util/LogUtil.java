package com.telus.credit.util;

public class LogUtil {

	 static void logInfo(Object obj){
		String m_loggingEnabled=(String) ESBCacheHelper.getObjectFromCache(CONSTANTS.WLN_WCDAP_LOGGING_ENABLED_CACHE_KEY);
		m_loggingEnabled=(m_loggingEnabled!=null?m_loggingEnabled.trim():m_loggingEnabled);
		if("Y".equalsIgnoreCase(m_loggingEnabled)){
			System.err.println(obj);
		}else{
			System.err.println(CONSTANTS.WLN_WCDAP_LOGGING_ENABLED_CACHE_KEY+ "=" + m_loggingEnabled);			
		}
	}
	 static void logError(Object obj){
		System.err.println("[WLN_WCDAP_JAVA_CALLOUT_ERROR:\n");
		System.err.println(obj);
		System.err.println("\n]");
	}
	  static void logException(Throwable e){
		System.err.println("[WLN_WCDAP_JAVA_CALLOUT_EXCEPTION:\n");
		e.printStackTrace();
		System.err.println("\n]");
	}	
}
