package com.telus.credit.dto;

public class AdditionalCollectionData 
{	
	private Long billingAccountNum;
	private Integer scorecardID;
	private Integer delinquencyCycle;
	private String collectionSegment;

	/**
	 * @return the billingAccountNum
	 */
	public Long getBillingAccountNum() {
		return billingAccountNum;
	}

	/**
	 * @param billingAccountNum the billingAccountNum to set
	 */
	public void setBillingAccountNum(Long billingAccountNum) {
		this.billingAccountNum = billingAccountNum;
	}	

	/**
	 * @return the collectionSegment
	 */
	public String getCollectionSegment() {
		return collectionSegment;
	}

	/**
	 * @param collectionSegment the collectionSegment to set
	 */
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
