package com.telus.crd.assessment.batch.domain;

import java.math.BigDecimal;
import java.util.Date;

public class BillingAccountRecord extends AbstractBillingAccountRecord
{
    private String billingAccountStatus;
    private Date billingAccountStatusDate;
    private Long billingAccountMasterSourceId;
    private BigDecimal totalAmount;
    
    // RTCA1.6: start service date of a ban
    private Date startServiceDate;

    public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getBillingAccountStatus()
    {
        return billingAccountStatus;
    }

    public void setBillingAccountStatus(String billingAccountStatus)
    {
        this.billingAccountStatus = billingAccountStatus;
    }

    public Date getBillingAccountStatusDate()
    {
        return billingAccountStatusDate;
    }

    public void setBillingAccountStatusDate(Date billingAccountStatusDate)
    {
        this.billingAccountStatusDate = billingAccountStatusDate;
    }

    public Long getBillingAccountMasterSourceId()
    {
        return billingAccountMasterSourceId;
    }

    public void setBillingAccountMasterSourceId(Long billingAccountMasterSourceId)
    {
        this.billingAccountMasterSourceId = billingAccountMasterSourceId;
    }

	public Date getStartServiceDate() {
		return startServiceDate;
	}

	public void setStartServiceDate(Date startServiceDate) {
		this.startServiceDate = startServiceDate;
	}
}
