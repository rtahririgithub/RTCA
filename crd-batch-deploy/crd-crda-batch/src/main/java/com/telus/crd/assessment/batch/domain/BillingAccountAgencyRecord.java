package com.telus.crd.assessment.batch.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.telus.crd.assessment.util.DateUtil;

public class BillingAccountAgencyRecord extends AbstractBillingAccountRecord
{
    private BigDecimal agencyAmount;
    private String agencyCode;
    private String agencyStatus;
    private String agencyDateStr;
    private Date agencyDate;
    private final static String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    private final static String blank = "";

    public BigDecimal getAgencyAmount()
    {
        return agencyAmount;
    }


    public void setAgencyAmount(BigDecimal agencyAmount)
    {
        this.agencyAmount = agencyAmount;
    }


    public String getAgencyCode()
    {
        return agencyCode;
    }


    public void setAgencyCode(String agencyCode)
    {
        this.agencyCode = agencyCode;
    }


    public String getAgencyStatus()
    {
        return agencyStatus;
    }


    public void setAgencyStatus(String agencyStatus)
    {
        this.agencyStatus = agencyStatus;
    }


    public Date getAgencyDate()
    {
    	String edate = agencyDateStr;
    	Date agencyDate = null;
    	try
    	{
    	    if(edate != null && !blank.equalsIgnoreCase(edate.trim()))
    	    {
    	    	agencyDate = DateUtil.formatStringToDate(edate.trim(), DATE_FORMAT_YYYY_MM_DD);
    	    }
    	}catch (Exception e){
    		
    	}
        return agencyDate;
    }


    public void setAgencyDate(Date agencyDate)
    {
        this.agencyDate = agencyDate;
    }


	public String getAgencyDateStr() {
		return agencyDateStr;
	}


	public void setAgencyDateStr(String agencyDateStr) {
		this.agencyDateStr = agencyDateStr;
	}

    
}
