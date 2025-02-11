package com.telus.crd.assessment.batch.domain;

import java.util.Date;
import java.math.BigDecimal;

public class CustomerCreditBureauRecord extends AbstractCustomerBureauRecord
{
    //private String bureauTrnId;
    private String firstName;
    private String middleName;
    private String lastName;

    private String errorCode;
    private String reportSourceCode;
    private String reportType;
    private String bureauReportStatusCd;
    private Date bureauReportStatusDate;
    private String adjudicationScoreTxt;
    private String adjudicationScoreType;
    private String adjudicationClass;
    private BigDecimal adjudicationLimitAmount;
    private String adjudicationDecisionCd;
    private String adjudicationDecisionMsg;
    private BigDecimal adjudicationDepositAmt;

    private Date createDate;


   /* public String getPersonName()
    {
        return personName;
    }


    public void setPersonName(String personName)
    {
        this.personName = personName;
    } */


    public String getErrorCode()
    {
        return errorCode;
    }


  /*  public String getBureauTrnId() {
		return bureauTrnId;
	}


	public void setBureauTrnId(String bureauTrnId) {
		this.bureauTrnId = bureauTrnId;
	} */


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getMiddleName() {
		return middleName;
	}


	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getBureauReportStatusCd() {
		return bureauReportStatusCd;
	}


	public void setBureauReportStatusCd(String bureauReportStatusCd) {
		this.bureauReportStatusCd = bureauReportStatusCd;
	}


	public Date getBureauReportStatusDate() {
		return bureauReportStatusDate;
	}


	public void setBureauReportStatusDate(Date bureauReportStatusDate) {
		this.bureauReportStatusDate = bureauReportStatusDate;
	}


	public String getAdjudicationScoreTxt() {
		return adjudicationScoreTxt;
	}


	public void setAdjudicationScoreTxt(String adjudicationScoreTxt) {
		this.adjudicationScoreTxt = adjudicationScoreTxt;
	}


	public String getAdjudicationScoreType() {
		return adjudicationScoreType;
	}


	public void setAdjudicationScoreType(String adjudicationScoreType) {
		this.adjudicationScoreType = adjudicationScoreType;
	}


	public String getAdjudicationClass() {
		return adjudicationClass;
	}


	public void setAdjudicationClass(String adjudicationClass) {
		this.adjudicationClass = adjudicationClass;
	}


	public BigDecimal getAdjudicationLimitAmount() {
		return adjudicationLimitAmount;
	}


	public void setAdjudicationLimitAmount(BigDecimal adjudicationLimitAmount) {
		this.adjudicationLimitAmount = adjudicationLimitAmount;
	}


	public String getAdjudicationDecisionCd() {
		return adjudicationDecisionCd;
	}


	public void setAdjudicationDecisionCd(String adjudicationDecisionCd) {
		this.adjudicationDecisionCd = adjudicationDecisionCd;
	}


	public String getAdjudicationDecisionMsg() {
		return adjudicationDecisionMsg;
	}


	public void setAdjudicationDecisionMsg(String adjudicationDecisionMsg) {
		this.adjudicationDecisionMsg = adjudicationDecisionMsg;
	}


	public BigDecimal getAdjudicationDepositAmt() {
		return adjudicationDepositAmt;
	}


	public void setAdjudicationDepositAmt(BigDecimal adjudicationDepositAmt) {
		this.adjudicationDepositAmt = adjudicationDepositAmt;
	}


	public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }


    public String getReportSourceCode()
    {
        return reportSourceCode;
    }


    public void setReportSourceCode(String reportSourceCode)
    {
        this.reportSourceCode = reportSourceCode;
    }


    public String getReportType()
    {
        return reportType;
    }


    public void setReportType(String reportType)
    {
        this.reportType = reportType;
    }


 /*   public String getAdjudicationResult()
    {
        return adjudicationResult;
    }


    public void setAdjudicationResult(String adjudicationResult)
    {
        this.adjudicationResult = adjudicationResult;
    }

*/
  /*  public String getRiskIndicator()
    {
        return riskIndicator;
    }


    public void setRiskIndicator(String riskIndicator)
    {
        this.riskIndicator = riskIndicator;
    }


    public String getFraudWarningList()
    {
        return fraudWarningList;
    }


    public void setFraudWarningList(String fraudWarningList)
    {
        this.fraudWarningList = fraudWarningList;
    }
*/

    public Date getCreateDate()
    {
        return createDate;
    }


    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate;
    }

}
