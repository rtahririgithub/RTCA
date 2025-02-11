package com.telus.crd.assessment.batch.dto;

import java.math.BigDecimal;
import java.util.Date;

// RTCA1.6 - CVUD
// copy from com.telus.collections.treatment.service.dto
public class CustomerCollectionData 
{
	// PRCA1.6
	private Long billingAccountNum;	
	private Integer scorecardID;
	private Integer delinquencyCycle;
	private String collectionSegment;
		
	// those fields are defined in com.telus.collections.treatment.service.dto.CustomerCollectionData
	private Boolean collectionInd;    
	private Date latestCollectionStartDate;    
	private Date latestCollectionEndDate;
	private String collectionScore;		// RTCA1.6: baScore (int)
	private Integer numberOfAccountsInAgency;
	private BigDecimal accountsInAgencyBalance;

	private Date latestAgencyAssignmentDate;
	private Integer involuntaryCancelledAccounts;
	private BigDecimal involuntaryCancelledAccountsBalance;

	private Date latestInvoluntaryCancelledDate;
	private Integer numberOfNSFCheques;
	
	public Boolean isCollectionInd() {
		return collectionInd;
	}
	
	public void setCollectionInd(Boolean collectionInd) {
		this.collectionInd = collectionInd;
	}
	
	public Date getLatestCollectionStartDate() {
		return latestCollectionStartDate;
	}
	
	public void setLatestCollectionStartDate(Date latestCollectionStartDate) {
		this.latestCollectionStartDate = latestCollectionStartDate;
	}
	
	public Date getLatestCollectionEndDate() {
		return latestCollectionEndDate;
	}
	
	public void setLatestCollectionEndDate(Date latestCollectionEndDate) {
		this.latestCollectionEndDate = latestCollectionEndDate;
	}
	
	public String getCollectionScore() {
		return collectionScore;
	}
	
	public void setCollectionScore(String collectionScore) {
		this.collectionScore = collectionScore;
	}
	
	public Integer getNumberOfAccountsInAgency() {
		return numberOfAccountsInAgency;
	}
	
	public void setNumberOfAccountsInAgency(Integer numberOfAccountsInAgency) {
		this.numberOfAccountsInAgency = numberOfAccountsInAgency;
	}
	
	public BigDecimal getAccountsInAgencyBalance() {
		return accountsInAgencyBalance;
	}
	
	public void setAccountsInAgencyBalance(BigDecimal accountsInAgencyBalance) {
		this.accountsInAgencyBalance = accountsInAgencyBalance;
	}
	
	public Date getLatestAgencyAssignmentDate() {
		return latestAgencyAssignmentDate;
	}
	
	public void setLatestAgencyAssignmentDate(Date latestAgencyAssignmentDate) {
		this.latestAgencyAssignmentDate = latestAgencyAssignmentDate;
	}
	
	public Integer getInvoluntaryCancelledAccounts() {
		return involuntaryCancelledAccounts;
	}
	
	public void setInvoluntaryCancelledAccounts(Integer involuntaryCancelledAccounts) {
		this.involuntaryCancelledAccounts = involuntaryCancelledAccounts;
	}
	
	public BigDecimal getInvoluntaryCancelledAccountsBalance() {
		return involuntaryCancelledAccountsBalance;
	}
	
	public void setInvoluntaryCancelledAccountsBalance(
			BigDecimal involuntaryCancelledAccountsBalance) {
		this.involuntaryCancelledAccountsBalance = involuntaryCancelledAccountsBalance;
	}
	
	public Date getLatestInvoluntaryCancelledDate() {
		return latestInvoluntaryCancelledDate;
	}
	
	public void setLatestInvoluntaryCancelledDate(
			Date latestInvoluntaryCancelledDate) {
		this.latestInvoluntaryCancelledDate = latestInvoluntaryCancelledDate;
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
