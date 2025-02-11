package com.telus.crd.assessment.batch.domain;

//import java.util.Date;

//import com.telus.crd.assessment.util.DateUtil;

public class CustomerCreditProfileFraudRecord extends AbstractCustomerRecord
{
	private long creditProfileId;
	private String fraudCd;
	
   // private final static String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
   // private final static String blank = "";
	
    public long getCreditProfileId() {
		return creditProfileId;
	}
	public void setCreditProfileId(long creditProfileId) {
		this.creditProfileId = creditProfileId;
	}
	public String getFraudCd() {
		return fraudCd;
	}
	public void setFraudCd(String fraudCd) {
		this.fraudCd = fraudCd;
	}

    




}
