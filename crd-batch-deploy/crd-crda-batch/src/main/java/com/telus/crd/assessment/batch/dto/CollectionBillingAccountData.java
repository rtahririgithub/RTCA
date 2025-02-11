package com.telus.crd.assessment.batch.dto;

import java.math.BigDecimal;
import java.util.Date;

// New class in RTCA1.6 - CVUD
// copy from com.telus.collections.treatment.service.dto
public class CollectionBillingAccountData 
{
	private Boolean collectionIndicator;    
	private Date collectionStartDate;    
	private Date collectionEndDate;
	private String collectionStatus;
	private Boolean involuntaryCeasedIndicator;
	
	private String collectionScore;	// RTCA1.6: baScore (int)
	private Date collectionScoreDate;
	private String agencyAssignmentCode;
	private BigDecimal agencyAssignmentAmount;    
	private Date agencyAssignmentDate;
	
	private String accountStatus;    
	private Date accountStatusDate;
	private BigDecimal accountBalance;
	private Integer numberOfNSFCheques;
	
	// PRCA1.6
	private Long billingAccountNum;	
    private Integer scorecardID;
    private Integer delinquencyCycle;
    private String collectionSegment;
    private Date startServiceDate; // a ban Start Service Date
    
	public Boolean isCollectionIndicator() {
		return collectionIndicator;
	}
	
	public void setCollectionIndicator(Boolean collectionIndicator) {
		this.collectionIndicator = collectionIndicator;
	}
	
	public Date getCollectionStartDate() {
		return collectionStartDate;
	}
	
	public void setCollectionStartDate(Date collectionStartDate) {
		this.collectionStartDate = collectionStartDate;
	}
	
	public Date getCollectionEndDate() {
		return collectionEndDate;
	}
	
	public void setCollectionEndDate(Date collectionEndDate) {
		this.collectionEndDate = collectionEndDate;
	}
	
	public String getCollectionStatus() {
		return collectionStatus;
	}
	
	public void setCollectionStatus(String collectionStatus) {
		this.collectionStatus = collectionStatus;
	}
	
	public Boolean isInvoluntaryCeasedIndicator() {
		return involuntaryCeasedIndicator;
	}
	
	public void setInvoluntaryCeasedIndicator(Boolean involuntaryCeasedIndicator) {
		this.involuntaryCeasedIndicator = involuntaryCeasedIndicator;
	}
	
	public String getCollectionScore() {
		return collectionScore;
	}
	
	public void setCollectionScore(String collectionScore) {
		this.collectionScore = collectionScore;
	}
	
	public Date getCollectionScoreDate() {
		return collectionScoreDate;
	}
	
	public void setCollectionScoreDate(Date collectionScoreDate) {
		this.collectionScoreDate = collectionScoreDate;
	}
	
	public String getAgencyAssignmentCode() {
		return agencyAssignmentCode;
	}
	
	public void setAgencyAssignmentCode(String agencyAssignmentCode) {
		this.agencyAssignmentCode = agencyAssignmentCode;
	}
	
	public BigDecimal getAgencyAssignmentAmount() {
		return agencyAssignmentAmount;
	}
	
	public void setAgencyAssignmentAmount(BigDecimal agencyAssignmentAmount) {
		this.agencyAssignmentAmount = agencyAssignmentAmount;
	}
	
	public Date getAgencyAssignmentDate() {
		return agencyAssignmentDate;
	}
	
	public void setAgencyAssignmentDate(Date agencyAssignmentDate) {
		this.agencyAssignmentDate = agencyAssignmentDate;
	}
	
	public String getAccountStatus() {
		return accountStatus;
	}
	
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	
	public Date getAccountStatusDate() {
		return accountStatusDate;
	}
	
	public void setAccountStatusDate(Date accountStatusDate) {
		this.accountStatusDate = accountStatusDate;
	}
	
	public BigDecimal getAccountBalance() {
		return accountBalance;
	}
	
	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}
	
	public Integer getNumberOfNSFCheques() {
		return numberOfNSFCheques;
	}
	
	public void setNumberOfNSFCheques(Integer numberOfNSFCheques) {
		this.numberOfNSFCheques = numberOfNSFCheques;
	}
	
	public Long getBillingAccountNum() {
		return billingAccountNum;
	}
	
	public void setBillingAccountNum(Long billingAccountNum) {
		this.billingAccountNum = billingAccountNum;
	}
		
	public String getCollectionSegment() {
		return collectionSegment;
	}
	
	public void setCollectionSegment(String collectionSegment) {
		this.collectionSegment = collectionSegment;
	}

	public Date getStartServiceDate() {
		return startServiceDate;
	}

	public void setStartServiceDate(Date startServiceDate) {
		this.startServiceDate = startServiceDate;
	}

	public Integer getScorecardID() {
		return scorecardID;
	}

	public void setScorecardID(Integer scorecardID) {
		this.scorecardID = scorecardID;
	}

	public Integer getDelinquencyCycle() {
		return delinquencyCycle;
	}

	public void setDelinquencyCycle(Integer delinquencyCycle) {
		this.delinquencyCycle = delinquencyCycle;
	}
}
