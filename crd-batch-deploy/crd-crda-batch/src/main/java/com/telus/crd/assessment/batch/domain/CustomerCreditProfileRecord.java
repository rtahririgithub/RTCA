package com.telus.crd.assessment.batch.domain;

import java.util.Date;

import com.telus.crd.assessment.util.DateUtil;

public class CustomerCreditProfileRecord extends AbstractCustomerRecord
{
	private long creditProfileId;
	
	private String cproflStatusCd;
    private String cproflFormatCd;
    private Date busLastUpdateTs;
    private String populatemethodCd;
    private String sinNumber;
    private String dlNumber;
    private String dlProvinceCd;
    private String passportNumber;
    private String passportCountry;
    private String healthCardNumber;
    //private String hlprovinceCd;
    private String provinceIdNumer;
    private String provinceIdCd;
    private String cproflAddressTypeCd;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressLine4;
    private String addressLine5;
    private String municNm;
    private String provinceCd;
    private String postalZipCd;
    private String countryCode;
    private String birthDateStr;
    private Date birthDate;
    private String creditCheckConsentCode;
	private String employmentStatusCode;
    private String residencyCode;
    private String primCreditCardTypeCd;
    private String secondCreditCardTypeCd;    
    private String underLegalCareCode;    
    private String applicationProvinceCode;
    private String bypassMatchIndicatorStr;
    private Boolean bypassMatchIndicator;
    
    private String provinceOfCurrentResidence;
    private String credProfAttrValueReportInd;  //may not need this one
    private String creditValueEffectiveDateStr;
    private String firstCreditAssessmentDateStr;
    private String latestCreditAssessmentDateStr;
    
   
	//private Date creditValueEffectiveDate;
    //private Date firstCreditAssessmentDate;
    // private Date latestCreditAssessmentDate;
    private String creditValueCode;
    
    private String assessmentMessageCode;
    private String productCatBoltOnStr;
    private Boolean productCatBoltOn;
    private String decisionCode;
    private String fraudIndicatorCode;
    private String creditScoreNumber;    
    private String creditClassCd; 
    private String creditAssessReqIdStr;
    private Long creditAssessReqId;
    private String commentText;
    private final static String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    private final static String blank = "";
    private final static String TRUE_STR = "1";
    private final static String FALSE_STR = "0";
    public String getEmploymentStatusCode()
    {
    	return employmentStatusCode == null? "" : employmentStatusCode.trim();
         	
    }


    public void setEmploymentStatusCode(String employmentStatusCode)
    {
        this.employmentStatusCode = employmentStatusCode;
    }


    public String getResidencyCode()
    {
    	return residencyCode == null? "" : residencyCode.trim();
        
    }


    public void setResidencyCode(String residencyCode)
    {
        this.residencyCode = residencyCode;
    }


    public String getCreditCheckConsentCode()
    {
    	return creditCheckConsentCode == null? "" : creditCheckConsentCode.trim();
         
    }


    public void setCreditCheckConsentCode(String creditCheckConsentCode)
    {
        this.creditCheckConsentCode = creditCheckConsentCode;
    }


    public String getBirthDateStr()
    {
        return birthDateStr;
    }


    public void setBirthDateStr(String birthDateStr)
    {
        this.birthDateStr = birthDateStr;
    }
    
    public Date getBirthDate()
    {
    	String edate = birthDateStr;
    	Date birthDate = null;
    	try
    	{
    	    if(edate != null && !blank.equalsIgnoreCase(edate.trim()))
    	    {
    		    birthDate = DateUtil.formatStringToDate(edate.trim(), DATE_FORMAT_YYYY_MM_DD);
    	    }
    	}catch (Exception e){
    		
    	}
        
        return birthDate;
    }


    public String getBypassMatchIndicatorStr() {
		return bypassMatchIndicatorStr;
	}


	public void setBypassMatchIndicatorStr(String bypassMatchIndicatorStr) {
		this.bypassMatchIndicatorStr = bypassMatchIndicatorStr;
	}


	public String getProductCatBoltOnStr() {
		return productCatBoltOnStr;
	}


	public void setProductCatBoltOnStr(String productCatBoltOnStr) {
		this.productCatBoltOnStr = productCatBoltOnStr;
	}


	public String getCreditAssessReqIdStr() {
		return creditAssessReqIdStr;
	}


	public void setCreditAssessReqIdStr(String creditAssessReqIdStr) {
		this.creditAssessReqIdStr = creditAssessReqIdStr;
	}


	public void setBirthDate(Date birthDate)
    {
        this.birthDate = birthDate;
    }


    public String getUnderLegalCareCode()
    {
    	return underLegalCareCode == null? "" : underLegalCareCode.trim();
        
       
    }


    public void setUnderLegalCareCode(String underLegalCareCode)
    {
        this.underLegalCareCode = underLegalCareCode;
    }


    public String getProvinceOfCurrentResidence()
    {
    	return provinceOfCurrentResidence == null? "" : provinceOfCurrentResidence.trim();
        
    }


    public void setProvinceOfCurrentResidence(String provinceOfCurrentResidence)
    {
        this.provinceOfCurrentResidence = provinceOfCurrentResidence;
    }


    public String getApplicationProvinceCode()
    {
    	return applicationProvinceCode == null? "" : applicationProvinceCode.trim();

    }


    public void setApplicationProvinceCode(String applicationProvinceCode)
    {
        this.applicationProvinceCode = applicationProvinceCode;
    }


    public Boolean getBypassMatchIndicator()
    {
    	Boolean bys =null;
		if(bypassMatchIndicatorStr != null && !blank.equalsIgnoreCase(bypassMatchIndicatorStr.trim()))
		{
			if(TRUE_STR.equalsIgnoreCase(bypassMatchIndicatorStr))
			{
				bys = true;
			} else if (FALSE_STR.equalsIgnoreCase(bypassMatchIndicatorStr))
			{
				bys = false;
			}
			
		}
		return bys;
       
    }


    public void setBypassMatchIndicator(Boolean bypassMatchIndicator)
    {
        this.bypassMatchIndicator = bypassMatchIndicator;
    }


    public String getCountryCode()
    {
    	return countryCode == null? "" : countryCode.trim();

    }


    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
    }

    public String getCreditValueEffectiveDateStr() {
		return creditValueEffectiveDateStr;
	}


	public void setCreditValueEffectiveDateStr(String creditValueEffectiveDateStr) {
		this.creditValueEffectiveDateStr = creditValueEffectiveDateStr;
	}


	public String getFirstCreditAssessmentDateStr() {
		return firstCreditAssessmentDateStr;
	}


	public void setFirstCreditAssessmentDateStr(String firstCreditAssessmentDateStr) {
		this.firstCreditAssessmentDateStr = firstCreditAssessmentDateStr;
	}


	public String getLatestCreditAssessmentDateStr() {
		return latestCreditAssessmentDateStr;
	}


	public void setLatestCreditAssessmentDateStr(
			String latestCreditAssessmentDateStr) {
		this.latestCreditAssessmentDateStr = latestCreditAssessmentDateStr;
	}



    public Date getCreditValueEffectiveDate()
    {
    	String edate = creditValueEffectiveDateStr;
    	Date rdate = null;
    	try
    	{
    	    if(edate != null && !blank.equalsIgnoreCase(edate.trim()))
    	    {
    		    rdate = DateUtil.formatStringToDate(edate.trim(), DATE_FORMAT_YYYY_MM_DD);
    	    }
    	} catch (Exception e)
    	{
    		//skip
    	}
        return rdate;
    }


   /* public void setCreditValueEffectiveDate(Date creditValueEffectiveDate)
    {
        this.creditValueEffectiveDate = creditValueEffectiveDate;
    } */


    public Date getFirstCreditAssessmentDate()
    {
    	String edate = firstCreditAssessmentDateStr;
    	Date rdate = null;
    	try
    	{
    	    if(edate != null && !blank.equalsIgnoreCase(edate.trim()))
    	    {
    		    rdate = DateUtil.formatStringToDate(edate.trim(), DATE_FORMAT_YYYY_MM_DD);
    	    }
    	} catch (Exception e)
    	{
    		//skip
    	}
        return rdate;
        
    }


 /*   public void setFirstCreditAssessmentDate(Date firstCreditAssessmentDate)
    {
        this.firstCreditAssessmentDate = firstCreditAssessmentDate;
    } */


    public Date getLatestCreditAssessmentDate()
    {
    	String edate = latestCreditAssessmentDateStr;
    	Date rdate = null;
    	try
    	{
    	    if(edate != null && !blank.equalsIgnoreCase(edate.trim()))
    	    {
    		    rdate = DateUtil.formatStringToDate(edate.trim(), DATE_FORMAT_YYYY_MM_DD);
    	    }
    	} catch (Exception e)
    	{
    		//skip
    	}
        return rdate;
        
    }


    /*public void setLatestCreditAssessmentDate(Date latestCreditAssessmentDate)
    {
        this.latestCreditAssessmentDate = latestCreditAssessmentDate;
    } */


    public String getDecisionCode()
    {
    	return decisionCode == null? "" : decisionCode.trim();

    }


    public void setDecisionCode(String decisionCode)
    {
        this.decisionCode = decisionCode;
    }


    public String getAssessmentMessageCode()
    {
    	return assessmentMessageCode == null? "" : assessmentMessageCode.trim();

    }


    public void setAssessmentMessageCode(String assessmentMessageCode)
    {
        this.assessmentMessageCode = assessmentMessageCode;
    }


    public String getCreditValueCode()
    {
    	return creditValueCode == null? "" : creditValueCode.trim();


    }


    public void setCreditValueCode(String creditValueCode)
    {
        this.creditValueCode = creditValueCode;
    }


    public String getFraudIndicatorCode()
    {
    	return fraudIndicatorCode == null? "" : fraudIndicatorCode.trim();

    }


    public void setFraudIndicatorCode(String fraudIndicatorCode)
    {
        this.fraudIndicatorCode = fraudIndicatorCode;
    }


	public long getCreditProfileId() {
		
		return creditProfileId;
	}


	public void setCreditProfileId(long creditProfileId) {
		this.creditProfileId = creditProfileId;
	}

	public String getCproflStatusCd() {
	
		return cproflStatusCd == null? "" : cproflStatusCd.trim();

	}


	public void setCproflStatusCd(String cproflStatusCd) {
		this.cproflStatusCd = cproflStatusCd;
	}


	public String getCproflFormatCd() {
		
		return cproflFormatCd == null? "" : cproflFormatCd.trim();
	}


	public void setCproflFormatCd(String cproflFormatCd) {
		this.cproflFormatCd = cproflFormatCd;
	}


	public Date getBusLastUpdateTs() {
		return busLastUpdateTs;
	}


	public void setBusLastUpdateTs(Date busLastUpdateTs) {
		this.busLastUpdateTs = busLastUpdateTs;
	}


	public String getPopulatemethodCd() {
	
		return populatemethodCd == null? "" : populatemethodCd.trim();
	}


	public void setPopulatemethodCd(String populatemethodCd) {
		this.populatemethodCd = populatemethodCd;
	}


	public String getSinNumber() {
	
		return sinNumber == null? "" : sinNumber.trim();
	}


	public void setSinNumber(String sinNumber) {
		this.sinNumber = sinNumber;
	}


	public String getDlNumber() {
	
		return dlNumber == null? "" : dlNumber.trim();
	}


	public void setDlNumber(String dlNumber) {
		this.dlNumber = dlNumber;
	}


	public String getDlProvinceCd() {
		
		return dlProvinceCd == null? "" : dlProvinceCd.trim();
	}


	public void setDlProvinceCd(String dlProvinceCd) {
		this.dlProvinceCd = dlProvinceCd;
	}


	public String getPassportNumber() {
		
		return passportNumber == null? "" : passportNumber.trim();
	}


	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}


	public String getPassportCountry() {
		
		return passportCountry == null? "" : passportCountry.trim();
	}


	public void setPassportCountry(String passportCountry) {
		this.passportCountry = passportCountry;
	}


	public String getHealthCardNumber() {
		
		return healthCardNumber == null? "" : healthCardNumber.trim();
	}


	public void setHealthCardNumber(String healthCardNumber) {
		this.healthCardNumber = healthCardNumber;
	}


	public String getProvinceIdNumer() {
		
		return provinceIdNumer == null? "" : provinceIdNumer.trim();
	}


	public void setProvinceIdNumer(String provinceIdNumer) {
		this.provinceIdNumer = provinceIdNumer;
	}


	public String getProvinceIdCd() {
	
		return provinceIdCd == null? "" : provinceIdCd.trim();
	}


	public void setProvinceIdCd(String provinceIdCd) {
		this.provinceIdCd = provinceIdCd;
	
	}


	public String getCproflAddressTypeCd() {
		
		return cproflAddressTypeCd == null? "" : cproflAddressTypeCd.trim();
	}


	public void setCproflAddressTypeCd(String cproflAddressTypeCd) {
		this.cproflAddressTypeCd = cproflAddressTypeCd;
	}


	public String getAddressLine1() {
		
		return addressLine1 == null? "" : addressLine1.trim();
	}


	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}


	public String getAddressLine2() {
		
		return addressLine2 == null? "" : addressLine2.trim();
	}


	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}


	public String getAddressLine3() {
		
		return addressLine3 == null? "" : addressLine3.trim();
	}


	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}


	public String getAddressLine4() {
		
		return addressLine4 == null? "" : addressLine4.trim();
	}


	public void setAddressLine4(String addressLine4) {
		this.addressLine4 = addressLine4;
	}


	public String getAddressLine5() {
		
		return addressLine5 == null? "" : addressLine5.trim();
	}


	public void setAddressLine5(String addressLine5) {
		this.addressLine5 = addressLine5;
	}

	public String getMunicNm() {
		
		return municNm == null? "" : municNm.trim();
	}


	public void setMunicNm(String municNm) {
		this.municNm = municNm;
	}


	public String getProvinceCd() {
		
		return provinceCd == null? "" : provinceCd.trim();
	}


	public void setProvinceCd(String provinceCd) {
		this.provinceCd = provinceCd;
	}


	public String getPostalZipCd() {
		
		return postalZipCd == null? "" : postalZipCd.trim();
	}


	public void setPostalZipCd(String postalZipCd) {
		this.postalZipCd = postalZipCd;
	}


	public String getPrimCreditCardTypeCd() {
		
		return primCreditCardTypeCd == null? "" : primCreditCardTypeCd.trim();
	}


	public void setPrimCreditCardTypeCd(String primCreditCardTypeCd) {
		this.primCreditCardTypeCd = primCreditCardTypeCd;
	}


	public String getSecondCreditCardTypeCd() {
		
		return secondCreditCardTypeCd == null? "" : secondCreditCardTypeCd.trim();
	}


	public void setSecondCreditCardTypeCd(String secondCreditCardTypeCd) {
		this.secondCreditCardTypeCd = secondCreditCardTypeCd;
	}


	public String getCredProfAttrValueReportInd() {

		return credProfAttrValueReportInd == null? "" : credProfAttrValueReportInd.trim();
	}


	public void setCredProfAttrValueReportInd(String credProfAttrValueReportInd) {
		this.credProfAttrValueReportInd = credProfAttrValueReportInd;
	}


	public Boolean getProductCatBoltOn() {
		Boolean bo =null;
		if(productCatBoltOnStr != null && !blank.equalsIgnoreCase(productCatBoltOnStr.trim()))
		{
			if(TRUE_STR.equalsIgnoreCase(productCatBoltOnStr))
			{
				bo = true;
			} else if (FALSE_STR.equalsIgnoreCase(productCatBoltOnStr))
			{
				bo = false;
			}
			
		}
		return bo;
	}


	public void setProductCatBoltOn(Boolean productCatBoltOn) {
		this.productCatBoltOn = productCatBoltOn;
		
	}


	public String getCreditScoreNumber() {
		
		return creditScoreNumber == null? "" : creditScoreNumber.trim();
	}


	public void setCreditScoreNumber(String creditScoreNumber) {
		this.creditScoreNumber = creditScoreNumber;
	}


	public String getCreditClassCd() {
		
		return creditClassCd == null? "" : creditClassCd.trim();
	}


	public void setCreditClassCd(String creditClassCd) {
		this.creditClassCd = creditClassCd;
	}


	public Long getCreditAssessReqId() {
		Long reqId = null;
		try
		{
			Long rqi = Long.parseLong(creditAssessReqIdStr);
			if(rqi >=0)
				reqId = rqi;
		} catch (Exception e)
		{
			//skip
		}
		return reqId;
	}


	public void setCreditAssessReqId(Long creditAssessReqId) {
		this.creditAssessReqId = creditAssessReqId;
	}


	public String getCommentText() {
		
		return commentText == null? "" : commentText.trim();
	}


	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}


  

}
