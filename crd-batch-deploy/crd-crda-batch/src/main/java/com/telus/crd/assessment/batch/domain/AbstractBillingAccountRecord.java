package com.telus.crd.assessment.batch.domain;

public abstract class AbstractBillingAccountRecord extends AbstractCustomerRecord
{
    private long billingAccountNum;


    public long getBillingAccountNum()
    {
        return billingAccountNum;
    }


    public void setBillingAccountNum(long billingAccountNum)
    {
        this.billingAccountNum = billingAccountNum;
    }
}
