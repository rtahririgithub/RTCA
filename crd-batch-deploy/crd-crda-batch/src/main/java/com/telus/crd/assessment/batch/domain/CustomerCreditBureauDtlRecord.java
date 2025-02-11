package com.telus.crd.assessment.batch.domain;

//import java.util.Date;
//import java.math.BigDecimal;

public class CustomerCreditBureauDtlRecord extends AbstractCustomerBureauRecord
{
	private long bureauTranDtlId;
    private String bureauTranDtlCd;
    private String scoreName;
    private String scoreText;
    private Integer riskIndType;
    private String riskIndValue;
    private String fraudType;
	private String fraudCd;
	private String fraudMsg;
	private String bureauDataDocName;
	private String extSrcSysCd; 
	//private String extDocPathStr; //according to Reza, this one does not needed.
    private String extDocFormatType;
    
    public long getBureauTranDtlId() {
		return bureauTranDtlId;
	}
	public void setBureauTranDtlId(long bureauTranDtlId) {
		this.bureauTranDtlId = bureauTranDtlId;
	}
	public String getBureauTranDtlCd() {
		return bureauTranDtlCd;
	}
	public void setBureauTranDtlCd(String bureauTranDtlCd) {
		this.bureauTranDtlCd = bureauTranDtlCd;
	}
	public String getScoreName() {
		return scoreName;
	}
	public void setScoreName(String scoreName) {
		this.scoreName = scoreName;
	}
	public String getScoreText() {
		return scoreText;
	}
	public void setScoreText(String scoreText) {
		this.scoreText = scoreText;
	}
	public Integer getRiskIndType() {
		return riskIndType;
	}
	public void setRiskIndType(Integer riskIndType) {
		if(riskIndType != null)
		   this.riskIndType = riskIndType;
	}
	public String getRiskIndValue() {
		return riskIndValue;
	}
	public void setRiskIndValue(String riskIndValue) {
		this.riskIndValue = riskIndValue;
	}
	public String getFraudType() {
		return fraudType;
	}
	public void setFraudType(String fraudType) {
		this.fraudType = fraudType;
	}
	public String getFraudCd() {
		return fraudCd;
	}
	public void setFraudCd(String fraudCd) {
		this.fraudCd = fraudCd;
	}
	public String getFraudMsg() {
		return fraudMsg;
	}
	public void setFraudMsg(String fraudMsg) {
		this.fraudMsg = fraudMsg;
	}
	public String getBureauDataDocName() {
		return bureauDataDocName;
	}
	public void setBureauDataDocName(String bureauDataDocName) {
		this.bureauDataDocName = bureauDataDocName;
	}
	public String getExtSrcSysCd() {
		return extSrcSysCd;
	}
	public void setExtSrcSysCd(String extSrcSysCd) {
		this.extSrcSysCd = extSrcSysCd;
	} 
	/*public String getExtDocPathStr() {
		return extDocPathStr;
	}
	public void setExtDocPathStr(String extDocPathStr) {
		this.extDocPathStr = extDocPathStr;
	} */
	public String getExtDocFormatType() {
		return extDocFormatType;
	}
	public void setExtDocFormatType(String extDocFormatType) {
		this.extDocFormatType = extDocFormatType;
	}
	



}
