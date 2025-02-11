package com.telus.crd.assessment.batch.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.telus.crd.assessment.util.DateUtil;

public class BillingAccountDepositRecord extends AbstractBillingAccountRecord
{
    private Long depositId;
    private String orderId;
    private String dueDateStr;
    private Date dueDate;
    private BigDecimal interestAmount;

    private BigDecimal cancelAmount;
    private String cancelDateStr;
    private Date cancelDate;
    private String cancelReasonCode;

    private BigDecimal paidAmount;
    private String paidDateStr;
    private Date paidDate;
    
    private BigDecimal releaseAmount;
    private String releaseDateStr;
    private Date releaseDate;
    private String releaseMethodCode;
    private String releaseMethodReason;
    
    private BigDecimal requestAmount;
    private String requestDateStr;
    private Date requestDate;
    private String requestReasonCode;
    
    private final static String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    private final static String blank = "";


    public Long getDepositId()
    {
        return depositId;
    }


    public void setDepositId(Long depositId)
    {
        this.depositId = depositId;
    }


    public String getOrderId()
    {
        return orderId;
    }


    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }


    public Date getDueDate()
    {
    	String edate = dueDateStr;
    	Date dueDate = null;
    	try
    	{
    	    if(edate != null && !blank.equalsIgnoreCase(edate.trim()))
    	    {
    	    	dueDate = DateUtil.formatStringToDate(edate.trim(), DATE_FORMAT_YYYY_MM_DD);
    	    }
    	}catch (Exception e){
    		
    	}
        return dueDate;
    }


    public void setDueDate(Date dueDate)
    {
        this.dueDate = dueDate;
    }


    public BigDecimal getInterestAmount()
    {
    	BigDecimal amt = new BigDecimal(0);
    	if(interestAmount != null)
    	{
    		amt = interestAmount;
    	} 
        return amt;
    }


    public void setInterestAmount(BigDecimal interestAmount)
    {
        this.interestAmount = interestAmount;
    }


    //-------------------------------------------------------------------------
    public BigDecimal getCancelAmount()
    {
    	BigDecimal amt = new BigDecimal(0);
    	if(cancelAmount != null)
    	{
    		amt = cancelAmount;
    	} 
        return amt;
        
    }


    public void setCancelAmount(BigDecimal cancelAmount)
    {
        this.cancelAmount = cancelAmount;
    }


    public Date getCancelDate()
    {
    	String edate = cancelDateStr;
    	Date cancelDate = null;
    	try
    	{
    	    if(edate != null && !blank.equalsIgnoreCase(edate.trim()))
    	    {
    	    	cancelDate = DateUtil.formatStringToDate(edate.trim(), DATE_FORMAT_YYYY_MM_DD);
    	    }
    	}catch (Exception e){
    		
    	}
        return cancelDate;
    }


    public void setCancelDate(Date cancelDate)
    {
    	
        this.cancelDate = cancelDate;
    }


    public String getCancelReasonCode()
    {
    	
        return cancelReasonCode;
    }


    public void setCancelReasonCode(String cancelReasonCode)
    {
        this.cancelReasonCode = cancelReasonCode;
    }


    //-------------------------------------------------------------------------
    public BigDecimal getPaidAmount()
    {
    	BigDecimal amt = new BigDecimal(0);
    	if(paidAmount != null)
    	{
    		amt = paidAmount;
    	} 
        return amt;
       
    }


    public void setPaidAmount(BigDecimal paidAmount)
    {
        this.paidAmount = paidAmount;
    }


    public Date getPaidDate()
    {
    	String edate = paidDateStr;
    	Date paidDate = null;
    	try
    	{
    	    if(edate != null && !blank.equalsIgnoreCase(edate.trim()))
    	    {
    	    	paidDate = DateUtil.formatStringToDate(edate.trim(), DATE_FORMAT_YYYY_MM_DD);
    	    }
    	}catch (Exception e){
    		
    	}
        return paidDate;
    }


    public void setPaidDate(Date paidDate)
    {
        this.paidDate = paidDate;
    }


    //-------------------------------------------------------------------------
    public BigDecimal getReleaseAmount()
    {
    	BigDecimal amt = new BigDecimal(0);
    	if(releaseAmount != null)
    	{
    		amt = releaseAmount;
    	} 
        return amt;

    }


    public void setReleaseAmount(BigDecimal releaseAmount)
    {
        this.releaseAmount = releaseAmount;
    }


    public Date getReleaseDate()
    {
    	String edate = releaseDateStr;
    	Date releaseDate = null;
    	try
    	{
    	    if(edate != null && !blank.equalsIgnoreCase(edate.trim()))
    	    {
    	    	releaseDate = DateUtil.formatStringToDate(edate.trim(), DATE_FORMAT_YYYY_MM_DD);
    	    }
    	}catch (Exception e){
    		
    	}
        return releaseDate;
    }


    public void setReleaseDate(Date releaseDate)
    {
        this.releaseDate = releaseDate;
    }


    public String getReleaseMethodCode()
    {
        return releaseMethodCode;
    }


    public void setReleaseMethodCode(String releaseMethodCode)
    {
        this.releaseMethodCode = releaseMethodCode;
    }

    public String getReleaseMethodReason()
    {
        return releaseMethodReason;
    }


    public void setReleaseMethodReason(String releaseMethodReason)
    {
        this.releaseMethodReason = releaseMethodReason;
    }


    //-------------------------------------------------------------------------
    public BigDecimal getRequestAmount()
    {
    	BigDecimal amt = new BigDecimal(0);
    	if(requestAmount != null)
    	{
    		amt = requestAmount;
    	} 
        return amt;

    }


    public void setRequestAmount(BigDecimal requestAmount)
    {
        this.requestAmount = requestAmount;
    }


    public Date getRequestDate()
    {
    	String edate = requestDateStr;
    	Date requestDate = null;
    	try
    	{
    	    if(edate != null && !blank.equalsIgnoreCase(edate.trim()))
    	    {
    	    	requestDate = DateUtil.formatStringToDate(edate.trim(), DATE_FORMAT_YYYY_MM_DD);
    	    }
    	}catch (Exception e){
    		
    	}
        return requestDate;
    }


    public void setRequestDate(Date requestDate)
    {
        this.requestDate = requestDate;
    }


    public String getRequestReasonCode()
    {
        return requestReasonCode;
    }


    public void setRequestReasonCode(String requestReasonCode)
    {
        this.requestReasonCode = requestReasonCode;
    }


	public String getDueDateStr() {
		return dueDateStr;
	}


	public void setDueDateStr(String dueDateStr) {
		this.dueDateStr = dueDateStr;
	}


	public String getCancelDateStr() {
		return cancelDateStr;
	}


	public void setCancelDateStr(String cancelDateStr) {
		this.cancelDateStr = cancelDateStr;
	}


	public String getPaidDateStr() {
		return paidDateStr;
	}


	public void setPaidDateStr(String paidDateStr) {
		this.paidDateStr = paidDateStr;
	}


	public String getReleaseDateStr() {
		return releaseDateStr;
	}


	public void setReleaseDateStr(String releaseDateStr) {
		this.releaseDateStr = releaseDateStr;
	}


	public String getRequestDateStr() {
		return requestDateStr;
	}


	public void setRequestDateStr(String requestDateStr) {
		this.requestDateStr = requestDateStr;
	}
    
    
}
