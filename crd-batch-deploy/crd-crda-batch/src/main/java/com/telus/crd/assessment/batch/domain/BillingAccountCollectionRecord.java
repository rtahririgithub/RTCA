package com.telus.crd.assessment.batch.domain;

import java.util.Date;

import com.telus.crd.assessment.util.DateUtil;

public class BillingAccountCollectionRecord extends AbstractBillingAccountRecord
{
    private Boolean collectionsIndicator;
    private Date collectionsStartDate;
    private Date collectionsEndDate;
    private String collectionsStatus;
    private String collectionsScore; // RTCA1.6: baScore (int)
    private Integer numberOfNSFCheques;
    private String scoreDateStr;
    private Date scoreDate;
    private Boolean involuntaryCeasedIndicator;

    // RTCA1.6
    private Integer scorecardID;
    private Integer delinquencyCycle;
    private String collectionSegment;
    
    private final static String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    private final static String blank = "";

    public Boolean getCollectionsIndicator()
    {
        return collectionsIndicator;
    }

    public void setCollectionsIndicator(Boolean collectionsIndicator)
    {
        this.collectionsIndicator = collectionsIndicator;
    }

    public Date getCollectionsStartDate()
    {
        return collectionsStartDate;
    }

    public void setCollectionsStartDate(Date collectionsStartDate)
    {
        this.collectionsStartDate = collectionsStartDate;
    }

    public Date getCollectionsEndDate()
    {
        return collectionsEndDate;
    }

    public void setCollectionsEndDate(Date collectionsEndDate)
    {
        this.collectionsEndDate = collectionsEndDate;
    }

    public String getCollectionsStatus()
    {
        return collectionsStatus;
    }

    public void setCollectionsStatus(String collectionsStatus)
    {
        this.collectionsStatus = collectionsStatus;
    }

    public String getCollectionsScore()
    {
        return collectionsScore;
    }

    public void setCollectionsScore(String collectionsScore)
    {
        this.collectionsScore = collectionsScore;
    }

    public Integer getNumberOfNSFCheques()
    {
        return numberOfNSFCheques;
    }

    public void setNumberOfNSFCheques(Integer numberOfNSFCheques)
    {
        this.numberOfNSFCheques = numberOfNSFCheques;
    }

    public Date getScoreDate()
    {
    	String edate = scoreDateStr;
    	Date scoreDate = null;
    	try
    	{
    	    if(edate != null && !blank.equalsIgnoreCase(edate.trim()))
    	    {
    	    	scoreDate = DateUtil.formatStringToDate(edate.trim(), DATE_FORMAT_YYYY_MM_DD);
    	    }
    	}catch (Exception e){
    		
    	}
        return scoreDate;
    }

    public void setScoreDate(Date scoreDate)
    {
        this.scoreDate = scoreDate;
    }

    public Boolean getInvoluntaryCeasedIndicator()
    {
        return involuntaryCeasedIndicator;
    }

    public void setInvoluntaryCeasedIndicator(Boolean involuntaryCeasedIndicator)
    {
        this.involuntaryCeasedIndicator = involuntaryCeasedIndicator;
    }

	public String getScoreDateStr() {
		return scoreDateStr;
	}

	public void setScoreDateStr(String scoreDateStr) {
		this.scoreDateStr = scoreDateStr;
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
