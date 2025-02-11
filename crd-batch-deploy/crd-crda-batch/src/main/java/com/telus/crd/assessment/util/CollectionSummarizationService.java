package com.telus.crd.assessment.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.crd.assessment.batch.dto.CollectionBillingAccountData;
import com.telus.crd.assessment.batch.dto.CustomerCollectionData;
import com.telus.credit.dto.AdditionalCollectionData;
import com.telus.credit.util.ThreadLocalUtil;

public class CollectionSummarizationService {

	private static final Log log = LogFactory.getLog(CollectionSummarizationService.class);

	private int m_aggregatedSoldExternalAmount = 100;
	private int m_externalAgencyAssignmentMonthPeriod = 24;
	private int m_externalAgencySoldAssignmentMonthPeriod = 36;
	private int m_involuntaryCancelledMonthPeriod = 24;
	private List<String> m_internalAgencyCodes = null;

	/*
	 * Summarize given collection data at customer level based upon following logic:
	 *
	 * Attribute Logic to aggregate account level data
	 * BDS Score - RTCA1.5 not used in RTCA1.6 (Tim)
	 *   If no active accounts exists for that customer - use latest closed account score, for
	 *   Monthly re-assessment batch process customers with no active accounts
	 *   will not be part of re-assessment. If at least one active account take
	 *   worst BDS score across all of the open accounts for that customer.
	 *
	 * # external agency assigned accounts, Total Current AR Balance owing on
	 * external agency assigned accounts,Date of most recent external agency
	 * assignment
     *   Count only accounts assigned or sold to external agency.
     *   Assigned to external agency accounts should be assigned within last 24 month with
	 *   Current AR balance greater than 0$ Sold to external agency accounts
	 *   should be sold within last 36 month and aggregated Agency Assignment
	 *   Amount across all these accounts should be greater than 100$.
	 *
	 *
	 * # involuntary cancelled accounts, Date of most recent involuntary
	 * cancelled account, Balance owing on involuntary cancelled accounts
	 *   Aggregate data on involuntary cancelled accounts within last 24 month.
	 *
	 * latest In collection indicator, latest collection indicator start date,
	 * latest collection indicator end date
	 *   In Collection treatment indicator has to be summarized for all customers
	 *   active accounts based on below logic: indicator set to yes if at least
	 *   one account is currently in Collection treatment otherwise it will be set
	 *   to no.
	 *
	 *-- RTCA1.6
	 * Batch (CVUD) BAN-to-Customer level aggregation logic:
	   - The system shall source all new WLN BA attributes 
		 "BA Score"; "Delinquency cycle"; "Collection Segment"; "Scorecard ID"
		 from a single BAN in the scenario where a customer has multiple BANs.  
	
	   - The following is the logic to determine the correct BAN:
		 1. select the BAN with the largest delinquency cycle that does NOT have any of the following:
			- "-1" score result after applying the score rules in CR856625 (???)
			- Collection Segment = "TNTS" or "YOUNG"

		 2. If there are no BANs that satisfy the above conditions, use the oldest account available based on "start service date" from CODS billing extract
		 3. If there are multiple BANs that have the same "start service date", use any BAN. 
		 4. All other existing aggregation logic shall remain as-is in the existing implementation.
	 *
	 * @see
	 * com.telus.collections.treatment.service.CollectionSummarizationService
	 * #summarizeCollectionDataByCustomer(java.util.List)
	 */
	public CustomerCollectionData summarizeCollectionDataByCustomer(List<CollectionBillingAccountData> customerBillingAccountDataList) 
	{
		/* RTCA1.6: use the new object defined in crda: com.telus.crd.assessment.batch.dto.CustomerCollectionData */
		CustomerCollectionData result = new CustomerCollectionData();

		SummarizationCriteria summarizationCriteria = new SummarizationCriteria(customerBillingAccountDataList);

		/*If no active accounts exists for that customer - use latest closed account score, for
		 *   Monthly re-assessment batch process customers with no active accounts
		 *   will not be part of re-assessment. If at least one active account take
		 *   worst BDS score across all of the open accounts for that customer.*/
		
		// RTCA1.6: no matter the account is closed or not, all use the m_baStore
		result.setCollectionScore(summarizationCriteria.m_baStore);
		/*
		if ( summarizationCriteria.m_isAllAccountsClosed ) {
			result.setCollectionScore(summarizationCriteria.m_latestClosedAccountScore); // TODO
		}
		
		else if (summarizationCriteria.m_hasAtleastOneAccountOpen ) {
			//result.setCollectionScore(summarizationCriteria.m_worstBDSOpenAccountScore);
			result.setCollectionScore(summarizationCriteria.m_baStore);
		}
		*/

		result.setNumberOfAccountsInAgency( summarizationCriteria.m_noOfExternalAgencyAssignedAccounts );
		result.setAccountsInAgencyBalance( BigDecimal.valueOf( summarizationCriteria.m_arBalanceOnExternalAgencyAccounts ) );
		result.setLatestAgencyAssignmentDate( summarizationCriteria.m_dateOfMostRecentAssignment );

		result.setInvoluntaryCancelledAccounts( summarizationCriteria.m_noOfInvoluntartCancelledAccounts );
		result.setInvoluntaryCancelledAccountsBalance( BigDecimal.valueOf( summarizationCriteria.m_balanceOnInvoluntaryCancelledAccounts ) );
		result.setLatestInvoluntaryCancelledDate( summarizationCriteria.m_mostRecentInvoluntaryCancelledDate );
		result.setCollectionInd( summarizationCriteria.m_inCollection );
				
		// RTCA1.6, CVUD
		AdditionalCollectionData additionalCollData = new AdditionalCollectionData();
		additionalCollData.setCollectionSegment(summarizationCriteria.m_collSegment);	
		additionalCollData.setDelinquencyCycle(summarizationCriteria.m_largestDelinquencyCycle);
		additionalCollData.setScorecardID(summarizationCriteria.m_scorecardID);
				
		//ThreadLocalUtil threadLocalUtil = new ThreadLocalUtil();
		//threadLocalUtil.setAdditionalCollectionData(additionalCollData);		
		ThreadLocalUtil.setAdditionalCollectionData(additionalCollData);
		
		result.setLatestCollectionStartDate( summarizationCriteria.m_latestCollectionStartDate );
		result.setLatestCollectionEndDate( summarizationCriteria.m_latestCollectionEndDate );

		result.setNumberOfNSFCheques( summarizationCriteria.m_noOfNSF );

		return result;
	}

	private int getAggregatedSoldExternalAmount() {
		return m_aggregatedSoldExternalAmount;
	}

	private int getExternalAgencyAssignmentMonthPeriod() {
		return m_externalAgencyAssignmentMonthPeriod;
	}

	public int getExternalAgencySoldAssignmentMonthPeriod() {
		return m_externalAgencySoldAssignmentMonthPeriod;
	}

	/**
	 * @param m_aggregatedSoldExternalAmount the m_aggregatedSoldExternalAmount to set
	 */
	public void setAggregatedSoldExternalAmount(
			int aggregatedSoldExternalAmount) {
		this.m_aggregatedSoldExternalAmount = aggregatedSoldExternalAmount;
	}

	/**
	 * @param m_externalAgencyAssignmentMonthPeriod the m_externalAgencyAssignmentMonthPeriod to set
	 */
	public void setExternalAgencyAssignmentMonthPeriod(
			int externalAgencyAssignmentMonthPeriod) {
		this.m_externalAgencyAssignmentMonthPeriod = externalAgencyAssignmentMonthPeriod;
	}

	/**
	 * @param m_externalAgencySoldAssignmentMonthPeriod the m_externalAgencySoldAssignmentMonthPeriod to set
	 */
	public void setExternalAgencySoldAssignmentMonthPeriod(
			int externalAgencySoldAssignmentMonthPeriod) {
		this.m_externalAgencySoldAssignmentMonthPeriod = externalAgencySoldAssignmentMonthPeriod;
	}

	/**
	 * @return the m_involuntaryCancelledMonthPeriod
	 */
	public int getInvoluntaryCancelledMonthPeriod() {
		return m_involuntaryCancelledMonthPeriod;
	}

	/**
	 * @param m_involuntaryCancelledMonthPeriod the m_involuntaryCancelledMonthPeriod to set
	 */
	public void setInvoluntaryCancelledMonthPeriod(
			int involuntaryCancelledMonthPeriod) {
		this.m_involuntaryCancelledMonthPeriod = involuntaryCancelledMonthPeriod;
	}

	/**
	 * Set internal agency codes
	 *
	 * @param internalAgencyCodes
	 */
	public void setInternalAgencyCodes(String internalAgencyCodes) 
	{
		m_internalAgencyCodes = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer( internalAgencyCodes,"," );
		
		while ( tokenizer.hasMoreTokens() ) {
			m_internalAgencyCodes.add( tokenizer.nextToken() );
		}
	}

	public class SummarizationCriteria {

		private static final String PURCHASED_AGENCY_CODE_STARTS_WITH = "PA";
		private static final String OPEN_ACCOUNT_STATUS = "O";
		private static final String CLOSED_ACCOUNT_STATUS = "C";

		private boolean m_isAllAccountsClosed = true;
		private boolean m_hasAtleastOneAccountOpen = false;
		private boolean m_hasAnyAccount = false;

		private String m_worstBDSOpenAccountScore = null;
		private String m_latestClosedAccountScore = null;
		private Date m_latestClosedAccountScoreDate = null;

		private int m_noOfExternalAgencyAssignedAccounts = 0;
		private double m_arBalanceOnExternalAgencyAccounts = 0;
		private Date m_dateOfMostRecentAssignment = null;

		private int m_noOfInvoluntartCancelledAccounts = 0;
		private double m_balanceOnInvoluntaryCancelledAccounts = 0;
		private Date m_mostRecentInvoluntaryCancelledDate = null;

		private boolean m_inCollection = false;
		private Date m_latestCollectionStartDate = null;
		private Date m_latestCollectionEndDate = null;
		private int m_noOfNSF = 0;

		// RTCA1.6 - CVUD
		private Integer m_largestDelinquencyCycle = 0;
		private String m_collSegment = null;
		private Integer m_scorecardID = 0;
		private String m_baStore = null;
		//private boolean m_foundBanOfLargestDelinquencyCycle = false;

		// RTCA1.6
		public SummarizationCriteria( List<CollectionBillingAccountData> customerBillingAccountDataList)
		{
			boolean foundBanOfLargestDelinquencyCycle = false;
			if ( customerBillingAccountDataList != null && customerBillingAccountDataList.size() > 0 ) {
				m_hasAnyAccount = true;
			}

			// Note: in normal cases, for one customer, this customerBillingAccountDataList at least contains one ban.
			
			///////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// RTCA1.6 - CVUD: sort the customerBillingAccountDataList with delinquencyCycle in descending order
			// Using this map to retrieve values of "BA Score", "Delinquency cycle", "Collection Segment", and "Scorecard ID"
			Map<Integer, CollectionBillingAccountData> sortedCollBillingAccountDataMap = sortCollBillingAcctDataWithDelinquencyCycle(customerBillingAccountDataList);
			
			Set sortedCollBillingAccountDataSet = sortedCollBillingAccountDataMap.entrySet();
		    Iterator it = sortedCollBillingAccountDataSet.iterator();		    
		    CollectionBillingAccountData collBillingAccountData = null;
		    
		    while(it.hasNext())
		    {
		    	Map.Entry collBillingAccountDataEntry = (Map.Entry)it.next();
		    	collBillingAccountData = (CollectionBillingAccountData)collBillingAccountDataEntry.getValue();
		    			
		    	//log.info("delinquencyCycle_key = " + collBillingAccountDataEntry.getKey());
		    	//log.info("billingAccountNum_ban = " + collBillingAccountData.getBillingAccountNum());
		    	
		    	/*
		    	 * 1. select the BAN with the largest delinquency cycle that does NOT have any of the following:
						- "-1" score result after applying the score rules in CR856625
						- Collection Segment = "TNTS" or "YOUNG"
		    	 */
		    	if (collBillingAccountData != null)
		    	{
		    		// 1. This is the largest value of delinquencyCycle
		    		// 2. Per Alan if delinquencyCycle == 0, select the oldest account available based on "start service date".
		    		
		    		//if (collBillingAccountData.getDelinquencyCycle() == 0) {
		    		if (collBillingAccountDataEntry.getKey().toString().equals("0")) {
		    			foundBanOfLargestDelinquencyCycle = false;
		    			break;
		    		}
		    		
		    		if (collBillingAccountData.getCollectionScore() != null && 
		    			collBillingAccountData.getCollectionScore().equals("-1")) {
		    			continue;
		    		}
		    		
		    		if (collBillingAccountData.getCollectionSegment() != null) {
		    			if (collBillingAccountData.getCollectionSegment().equalsIgnoreCase("TNTS") || 
				    	    collBillingAccountData.getCollectionSegment().equalsIgnoreCase("YOUNG")) {
			    			continue;
			    		}
		    		}
		    		
		    		m_baStore = collBillingAccountData.getCollectionScore();
		    		m_largestDelinquencyCycle = collBillingAccountData.getDelinquencyCycle();
		    		m_collSegment = collBillingAccountData.getCollectionSegment();
		    		m_scorecardID = collBillingAccountData.getScorecardID();
		    		foundBanOfLargestDelinquencyCycle = true;
		    		
		    		// RTCA1.6-CVUD, TODO testing only, remove them after ATs
		    		//log.info("m_largestDelinquencyCycle = " + m_largestDelinquencyCycle);
		    		//log.info("m_baStore = " + m_baStore);
		    		//log.info("m_collSegment = " + m_collSegment);
		    		//log.info("m_scorecardID = " + m_scorecardID);
		    		
		    		break;
		    	}
		    }
		    
		    // 2. If there are no BANs that satisfy the above conditions, select the oldest account available based on "start service date" from CODS billing extract
		    if (!foundBanOfLargestDelinquencyCycle) 
		    {
		    	CollectionBillingAccountData oldestCollBillingAccountData = selectOldestCollBillingAcctData(customerBillingAccountDataList);
		    	
		    	m_baStore = oldestCollBillingAccountData.getCollectionScore();
	    		m_largestDelinquencyCycle = oldestCollBillingAccountData.getDelinquencyCycle();
	    		m_collSegment = oldestCollBillingAccountData.getCollectionSegment();
	    		m_scorecardID = oldestCollBillingAccountData.getScorecardID();

	    		// RTCA1.6-CVUD, TODO testing only, remove them after ATs
	    		//log.info("oldestCollBillingAccountData.m_largestDelinquencyCycle = " + m_largestDelinquencyCycle);
	    		//log.info("oldestCollBillingAccountData.m_baStore = " + m_baStore);
	    		//log.info("oldestCollBillingAccountData.m_collSegment = " + m_collSegment);
	    		//log.info("oldestCollBillingAccountData.m_scorecardID = " + m_scorecardID);
		    }
		    
		    // 3. If there are multiple BANs that have the same "start service date", use any BAN. 
		    // As 2 (no change)
		    
		    // End of RTCA1.6 - CVUD changes
		    /////////////////////////////////////////////////////////////////////////////////////////////////
		    
			for ( CollectionBillingAccountData collectionBillingAccountData: customerBillingAccountDataList )
			{				
				// In RTCA1.6, the result of m_worstBDSOpenAccountScore from summarizeBDSScore is actually not used.
				//summarizeBDSScore(collectionBillingAccountData);
				
				summarizeAgencyData(customerBillingAccountDataList, collectionBillingAccountData);
				summarizeInvoluntaryClosedData( collectionBillingAccountData );

				summarizeCollectionInvolvementData( collectionBillingAccountData );

				summarizeNumberOfNSF( collectionBillingAccountData );
			}
			
			// if there is at least one account in collection then
			// latest collection end date should not be populated as there is one account still in collection
			if ( m_inCollection ) {
				m_latestCollectionEndDate = null;
			}
		}

		private void summarizeNumberOfNSF(
				CollectionBillingAccountData collectionBillingAccountData) {
				if ( collectionBillingAccountData.getNumberOfNSFCheques() != null ) {
					m_noOfNSF += collectionBillingAccountData.getNumberOfNSFCheques().intValue();
				}
		}

		private void summarizeCollectionInvolvementData(
				CollectionBillingAccountData collectionBillingAccountData) {
			/* latest In collection indicator
			 latest collection indicator start date
			 latest collection indicator end date


			In Collection treatment indicator has to be summarized for all customers active accounts based on below logic:
			indicator set to yes if at least one account is currently in Collection treatment
			otherwise it will be set to no  */
			// if account status is not open then collection indicator,start,end date should not be set and
			if ( collectionBillingAccountData.getAccountStatus() == null
				 || !collectionBillingAccountData.getAccountStatus().equals(OPEN_ACCOUNT_STATUS) ) {
				return;
			}

			if ( collectionBillingAccountData.isCollectionIndicator() != null && collectionBillingAccountData.isCollectionIndicator().booleanValue()  ) {
				m_inCollection = true;
			}
			
			if ( m_latestCollectionStartDate == null
			     || ( collectionBillingAccountData.getCollectionStartDate() != null
				  && (m_latestCollectionStartDate.compareTo( collectionBillingAccountData.getCollectionStartDate() ) < 0) ) ) {
				m_latestCollectionStartDate = collectionBillingAccountData.getCollectionStartDate();
			}
			
			if ( m_latestCollectionEndDate == null
			     || ( collectionBillingAccountData.getCollectionEndDate() != null
				  && (m_latestCollectionEndDate.compareTo( collectionBillingAccountData.getCollectionEndDate() ) < 0 ) ) ) {
			    m_latestCollectionEndDate = collectionBillingAccountData.getCollectionEndDate();
			}
		}

		private void summarizeInvoluntaryClosedData(
				CollectionBillingAccountData collectionBillingAccountData) {
			/*# involuntary cancelled accounts
			Date of most recent involuntary cancelled account
			Balance owing on involuntary cancelled accounts

			Aggregate data on involuntary cancelled accounts within last 24 month */
			if ( collectionBillingAccountData.isInvoluntaryCeasedIndicator() != null && collectionBillingAccountData.isInvoluntaryCeasedIndicator().booleanValue()
				 && collectionBillingAccountData.getAccountStatus() != null
				 && collectionBillingAccountData.getAccountStatus().equals( CLOSED_ACCOUNT_STATUS)
				 && collectionBillingAccountData.getAccountStatusDate() != null
				 && collectionBillingAccountData.getAccountStatusDate().compareTo(getSysDateMinusMonths(m_involuntaryCancelledMonthPeriod)) >= 0  ) 
			{
				m_noOfInvoluntartCancelledAccounts++;
				m_balanceOnInvoluntaryCancelledAccounts += (collectionBillingAccountData.getAccountBalance() == null ? 0 : collectionBillingAccountData.getAccountBalance().doubleValue());
				
				if ( m_mostRecentInvoluntaryCancelledDate == null
					|| (m_mostRecentInvoluntaryCancelledDate.compareTo( collectionBillingAccountData.getAccountStatusDate() ) < 0) ) {
					m_mostRecentInvoluntaryCancelledDate = collectionBillingAccountData.getAccountStatusDate();
				}
			}
		}

		private void summarizeAgencyData(
				List<CollectionBillingAccountData> customerBillingAccountDataList,
				CollectionBillingAccountData collectionBillingAccountData) 
		{
			if ( collectionBillingAccountData.getAgencyAssignmentCode() != null ) 
			{
				if ( isExternalNonSoldAgency(collectionBillingAccountData.getAgencyAssignmentCode()) ) 
				{
					// Assigned to external agency accounts should be assigned within last 24 month with Current AR balance greater than 0$
					if ( collectionBillingAccountData.getAccountBalance() != null
						 && collectionBillingAccountData.getAccountBalance().doubleValue() > 0
						 && collectionBillingAccountData.getAgencyAssignmentDate() != null
						 && ( collectionBillingAccountData.getAgencyAssignmentDate().compareTo(getSysDateMinusMonths(getExternalAgencyAssignmentMonthPeriod()) )  >= 0 ) ) 
					{
						m_noOfExternalAgencyAssignedAccounts++;
						m_arBalanceOnExternalAgencyAccounts += (collectionBillingAccountData.getAccountBalance() == null ? 0 : collectionBillingAccountData.getAccountBalance().doubleValue());
						
						if ( m_dateOfMostRecentAssignment == null ) {
							m_dateOfMostRecentAssignment = collectionBillingAccountData.getAgencyAssignmentDate();
						}
						else if (m_dateOfMostRecentAssignment.compareTo(collectionBillingAccountData.getAgencyAssignmentDate()) < 0 ){
							m_dateOfMostRecentAssignment = collectionBillingAccountData.getAgencyAssignmentDate();
						}
					}
				} 
				else if ( isExternalSoldAgency(collectionBillingAccountData.getAgencyAssignmentCode()) ) 
				{
					// Sold to external agency accounts should be sold within last 36 month and aggregated Agency Assignment Amount across all these accounts should be greater than 100$.
					if ( collectionBillingAccountData.getAgencyAssignmentDate() != null
						 && ( collectionBillingAccountData.getAgencyAssignmentDate().compareTo(getSysDateMinusMonths(getExternalAgencySoldAssignmentMonthPeriod()) )  >= 0 )
						 && getAggregatedSoldExternalAmount(customerBillingAccountDataList) > CollectionSummarizationService.this.getAggregatedSoldExternalAmount() ) 
					{
						m_noOfExternalAgencyAssignedAccounts++;
						if ( collectionBillingAccountData.getAccountBalance() != null ) {
							m_arBalanceOnExternalAgencyAccounts += collectionBillingAccountData.getAccountBalance().doubleValue();
						}
						
						if ( m_dateOfMostRecentAssignment == null ) {
							m_dateOfMostRecentAssignment = collectionBillingAccountData.getAgencyAssignmentDate();
						}
						else if (m_dateOfMostRecentAssignment.compareTo(collectionBillingAccountData.getAgencyAssignmentDate()) < 0 ){
							m_dateOfMostRecentAssignment = collectionBillingAccountData.getAgencyAssignmentDate();
						}
					}
				}
			}
		}
				

		private void summarizeBDSScore(CollectionBillingAccountData collectionBillingAccountData)
		{
			if ( !collectionBillingAccountData.getAccountStatus().equals( CLOSED_ACCOUNT_STATUS )) {
				m_isAllAccountsClosed = false;
			}

			if ( collectionBillingAccountData.getAccountStatus().equals( OPEN_ACCOUNT_STATUS ) )
			{
				m_hasAtleastOneAccountOpen = true;

				if ( m_worstBDSOpenAccountScore == null ) {
					m_worstBDSOpenAccountScore = collectionBillingAccountData.getCollectionScore();
				}
				else if ( collectionBillingAccountData.getCollectionScore() != null )
				{
					try {
						// Higher score is better then lower score
						// if ( 600 (good) < 250 (bad) ) then take 250 (bad)
						if ( Integer.parseInt(collectionBillingAccountData.getCollectionScore() ) < Integer.parseInt(m_worstBDSOpenAccountScore) )  {
							m_worstBDSOpenAccountScore = collectionBillingAccountData.getCollectionScore();
						}
					}
					catch ( NumberFormatException numberFormatException ) {
						log.error("Error parsing collection score: m_worstBDSOpenAccountScore" + m_worstBDSOpenAccountScore
								+ "billing account data score: " + collectionBillingAccountData.getCollectionScore() );
					}
				}
			}
			else if ( collectionBillingAccountData.getAccountStatus().equals( CLOSED_ACCOUNT_STATUS ) )
			{
				// use latest closed account score
				if ( m_latestClosedAccountScoreDate == null )
				{
					m_latestClosedAccountScoreDate = collectionBillingAccountData.getAccountStatusDate();
					m_latestClosedAccountScore = collectionBillingAccountData.getCollectionScore();
				}
				else if ( collectionBillingAccountData.getAccountStatusDate() != null )
				{
					if ( collectionBillingAccountData.getAccountStatusDate().compareTo( m_latestClosedAccountScoreDate ) > 0 ) {
						m_latestClosedAccountScoreDate = collectionBillingAccountData.getCollectionScoreDate();
						m_latestClosedAccountScore = collectionBillingAccountData.getCollectionScore();
					}
				}
			}
		}

		private double getAggregatedSoldExternalAmount(List<CollectionBillingAccountData> customerBillingAccountDataList)
		{
			double result = 0;
			for ( CollectionBillingAccountData collectionBillingAccountData: customerBillingAccountDataList ) 
			{
				if ( collectionBillingAccountData.getAgencyAssignmentCode() != null
					 && isExternalSoldAgency( collectionBillingAccountData.getAgencyAssignmentCode() )
					 && collectionBillingAccountData.getAgencyAssignmentAmount() != null ) {
					result += collectionBillingAccountData.getAgencyAssignmentAmount().doubleValue();
				}
			}
			return result;
		}

		private Date getSysDateMinusMonths(int monthsToSubtract) 
		{
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.MONTH, -monthsToSubtract);
			return c.getTime();
		}

		private boolean isExternalNonSoldAgency(String agencyAssignmentCode) {
			return isExternalAgencyCode(agencyAssignmentCode) && (agencyAssignmentCode!=null && (!agencyAssignmentCode.startsWith(PURCHASED_AGENCY_CODE_STARTS_WITH)));
		}

		public boolean isExternalSoldAgency(String agencyAssignmentCode) {
			return isExternalAgencyCode(agencyAssignmentCode) && (agencyAssignmentCode!=null && agencyAssignmentCode.startsWith(PURCHASED_AGENCY_CODE_STARTS_WITH));
		}

		private boolean isExternalAgencyCode(String agencyAssignmentCode) {	
			return agencyAssignmentCode != null && (!m_internalAgencyCodes.contains(agencyAssignmentCode));
		}

		private int scoreLevel(String collectionScore) 
		{
			try {
				return Integer.parseInt(collectionScore);
			}
			catch ( NumberFormatException numberFormatException ) {
				log.error("Error parsing collection score: " + collectionScore );
			}
			return 0;
		}
		
		// Sort by key (delinquencyCycle), reverse order / descending order of keys
		private Map<Integer, CollectionBillingAccountData> sortCollBillingAcctDataWithDelinquencyCycle(List<CollectionBillingAccountData> customerBillingAccountDataList)
		{
			Map<Integer, CollectionBillingAccountData> sortedCollBillingAcctDataMap = new TreeMap<Integer, CollectionBillingAccountData>(Collections.reverseOrder());

			if (customerBillingAccountDataList != null && customerBillingAccountDataList.size() >0) {				
				for ( CollectionBillingAccountData collBillingAccountData: customerBillingAccountDataList )
				{
					Integer delinquencyCycle = collBillingAccountData.getDelinquencyCycle();										
					if (delinquencyCycle == null) {
						log.info("ban = " + collBillingAccountData.getBillingAccountNum() + ", delinquencyCycle is null");

						// in this case, set the delinquencyCycle = 0 (as default), otherwise, if the customer has only ban, 
						// and the ban's delinquencyCycle = null, this customer will not be sent to FICO for credit assessment
						delinquencyCycle = Integer.valueOf(0);
					}
									
					if (delinquencyCycle >=0)
						sortedCollBillingAcctDataMap.put(Integer.valueOf(delinquencyCycle), collBillingAccountData);
					else
						log.info("delinquencyCycle is less than 0: " + delinquencyCycle);					
				}
			}
			
			//log.info("SortedCollBillingAccountDataMap = " + sortedCollBillingAcctDataMap.toString());			
			return sortedCollBillingAcctDataMap;
		}		
		
		// RTCA1.6: select oldest CollBillingAcctData with start service date
		private CollectionBillingAccountData selectOldestCollBillingAcctData(List<CollectionBillingAccountData> customerBillingAccountDataList)
		{
			CollectionBillingAccountData oldestCollBillingAcctData = null;			
			if (customerBillingAccountDataList != null && customerBillingAccountDataList.size() >0) 
			{
				oldestCollBillingAcctData = (CollectionBillingAccountData)customerBillingAccountDataList.get(0);
				if (customerBillingAccountDataList.size() == 1)
					return oldestCollBillingAcctData;
				
				for ( CollectionBillingAccountData collBillingAccountData: customerBillingAccountDataList )
				{
					if (oldestCollBillingAcctData.getStartServiceDate() != null && collBillingAccountData.getStartServiceDate() != null) 
					{
						if (oldestCollBillingAcctData.getStartServiceDate().compareTo(collBillingAccountData.getStartServiceDate()) >0) {
							oldestCollBillingAcctData = collBillingAccountData;
						}
					}
				}
			}
			
			// RTCA1.6-CVUD, TODO testing only, remove them after ATs
			//log.info("Ban = " + oldestCollBillingAcctData.getBillingAccountNum() + ", oldestStartSeviceDate = " + oldestCollBillingAcctData.getStartServiceDate());			
			return oldestCollBillingAcctData;
		}
	}
}
