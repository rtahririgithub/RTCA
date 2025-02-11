package com.telus.credit.util;

import com.telus.credit.dto.AdditionalCollectionData;

public class ThreadLocalUtil {
	
	// These thread locals will be set during batch process init, to store the partition key
	// - which later will be used to set the partition key in multiple domain objects
	// in order to make sure that the partition keys are the same during the batch process when persisting data
	// into database
	private static ThreadLocal<Integer> s_riskLevelNum = new ThreadLocal<Integer>();
    private static ThreadLocal<AdditionalCollectionData> s_additionalCollectionData = new ThreadLocal<AdditionalCollectionData>();
    
    public static AdditionalCollectionData getAdditionalCollectionData()
    {
    		return s_additionalCollectionData.get();
    }
    
    public static void setAdditionalCollectionData( AdditionalCollectionData additionalCollectionData)
    {
    	s_additionalCollectionData.set(additionalCollectionData);
    }

    public static Integer getRiskLevelNum()
    {
    	return s_riskLevelNum.get();
    }
    
    public static void setRiskLevelNum(Integer riskLevelNum)
    {
    	s_riskLevelNum.set(riskLevelNum);
    }
}
