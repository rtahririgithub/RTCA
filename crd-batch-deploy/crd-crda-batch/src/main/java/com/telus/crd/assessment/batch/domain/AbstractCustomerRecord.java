package com.telus.crd.assessment.batch.domain;

public abstract class AbstractCustomerRecord
{
    private long customerId;
    private String recordTime;

    public String getRecordTime() {
		return recordTime;
	}


	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}


	public long getCustomerId()
    {
        return customerId;
    }


    public void setCustomerId(long customerId)
    {
        this.customerId = customerId;
    }
    
}
