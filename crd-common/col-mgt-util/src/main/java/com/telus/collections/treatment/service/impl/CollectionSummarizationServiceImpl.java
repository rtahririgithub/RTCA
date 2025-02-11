/*
 *  Copyright (c) 2013 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 * $Id$
 */
package com.telus.collections.treatment.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.collections.treatment.service.CollectionSummarizationService;
import com.telus.collections.treatment.service.dto.CollectionBillingAccountData;
import com.telus.collections.treatment.service.dto.CustomerCollectionData;

/**
 * <p><b>Description :</b><class>CollectionSummarizationServiceImpl</class> implements the service interface <interface>CollectionSummarizationService</interface>.</p>
 * <p><b>Design Observations : </b></p>
 * <ul>
 * <li>All the Summarization parameters can be configured through a config file attributes.</li>
 * </ul>
 * <p><br><b>Revision History : </b></p>
 * <table border="1" width="100%">
 * <tr>
 * <th width="15%">Date</th>
 * 
 * <th width="15%">Revised By</th>
 * <th width="55%">Description</th>
 * <th width="15%">Reviewed By</th>
 * </tr>
 * <tr>
 * <td width="15%">30-Jul-2013</td>
 * <td width="15%">Gurbirinder Sidhu</td>
 * <td width="55%">Initial Creation</td>
 * <td width="15%">&nbsp;</td>
 * </tr>
 * </table>
 * 
 * @author Gurbirinder Sidhu
 * 
 * @stereotype Service Implementation
 * @version 1.0
 */
public class CollectionSummarizationServiceImpl implements
		CollectionSummarizationService {

	private static final Log log = LogFactory
			.getLog(CollectionSummarizationServiceImpl.class);
	
	private int m_aggregatedSoldExternalAmount = 100;
	private int m_externalAgencyAssignmentMonthPeriod = 24;
	private int m_externalAgencySoldAssignmentMonthPeriod = 36;
	private int m_involuntaryCancelledMonthPeriod = 24;
	private List<String> m_internalAgencyCodes = null;


	
	/*
	 * Summarize given collection data at customer level based upon following logic:
	 * 
	 * Attribute Logic to aggregate account level data 
	 * BDS Score 
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
	 * 
	 * @see
	 * com.telus.collections.treatment.service.CollectionSummarizationService
	 * #summarizeCollectionDataByCustomer(java.util.List)
	 */
	@Override
	public CustomerCollectionData summarizeCollectionDataByCustomer(
			List<CollectionBillingAccountData> customerBillingAccountDataList) {
		
		CustomerCollectionData result = new CustomerCollectionData();
		SummarizationCriteria summarizationCriteria = new SummarizationCriteria(customerBillingAccountDataList);
		 /*If no active accounts exists for that customer - use latest closed account score, for
		 *   Monthly re-assessment batch process customers with no active accounts
		 *   will not be part of re-assessment. If at least one active account take
		 *   worst BDS score across all of the open accounts for that customer.*/
		if ( summarizationCriteria.m_isAllAccountsClosed ) {
			result.setCollectionScore(summarizationCriteria.m_latestClosedAccountScore);
		}
		else if (summarizationCriteria.m_hasAtleastOneAccountOpen ) {
			result.setCollectionScore(summarizationCriteria.m_worstBDSOpenAccountScore);
		}
		
		result.setNumberOfAccountsInAgency( summarizationCriteria.m_noOfExternalAgencyAssignedAccounts );
		result.setAccountsInAgencyBalance( BigDecimal.valueOf( summarizationCriteria.m_arBalanceOnExternalAgencyAccounts ) );
		result.setLatestAgencyAssignmentDate( summarizationCriteria.m_dateOfMostRecentAssignment );
		
		result.setInvoluntaryCancelledAccounts( summarizationCriteria.m_noOfInvoluntartCancelledAccounts );
		result.setInvoluntaryCancelledAccountsBalance( BigDecimal.valueOf( summarizationCriteria.m_balanceOnInvoluntaryCancelledAccounts ) );
		result.setLatestInvoluntaryCancelledDate( summarizationCriteria.m_mostRecentInvoluntaryCancelledDate );
		
		result.setCollectionInd( summarizationCriteria.m_inCollection );
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
	public void setInternalAgencyCodes(String internalAgencyCodes) {
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
		
		public SummarizationCriteria( List<CollectionBillingAccountData> customerBillingAccountDataList)  {
			if ( customerBillingAccountDataList != null && customerBillingAccountDataList.size() > 0 ) {
				m_hasAnyAccount = true;
			}
			for ( CollectionBillingAccountData collectionBillingAccountData: customerBillingAccountDataList ) {
				summarizeBDSScore(collectionBillingAccountData);
				
				summarizeAgencyData(customerBillingAccountDataList,
						collectionBillingAccountData);
				summarizeInvoluntaryClosedData( collectionBillingAccountData );
				
				summarizeCollectionInvolvementData( collectionBillingAccountData );
				
				summarizeNumberOfNSF( collectionBillingAccountData );
			}
			// if there is atleast one account in collection then
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
				 && collectionBillingAccountData.getAccountStatusDate().compareTo(getSysDateMinusMonths(m_involuntaryCancelledMonthPeriod)) >= 0  ) {
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
				CollectionBillingAccountData collectionBillingAccountData) {
			if ( collectionBillingAccountData.getAgencyAssignmentCode() != null ) {
				if ( isExternalNonSoldAgency(collectionBillingAccountData.getAgencyAssignmentCode()) ) {
					// Assigned to external agency accounts should be assigned within last 24 month with Current AR balance greater than 0$ 
					if ( collectionBillingAccountData.getAccountBalance() != null
						 && collectionBillingAccountData.getAccountBalance().doubleValue() > 0 
						 && collectionBillingAccountData.getAgencyAssignmentDate() != null
						 && ( collectionBillingAccountData.getAgencyAssignmentDate().compareTo(getSysDateMinusMonths(getExternalAgencyAssignmentMonthPeriod()) )  >= 0 ) ) {
						m_noOfExternalAgencyAssignedAccounts++;
						m_arBalanceOnExternalAgencyAccounts += (collectionBillingAccountData.getAccountBalance() == null ? 0 : collectionBillingAccountData.getAccountBalance().doubleValue());
						if ( m_dateOfMostRecentAssignment == null ) {
							m_dateOfMostRecentAssignment = collectionBillingAccountData.getAgencyAssignmentDate();
						}
						else if (m_dateOfMostRecentAssignment.compareTo(collectionBillingAccountData.getAgencyAssignmentDate()) < 0 ){
							m_dateOfMostRecentAssignment = collectionBillingAccountData.getAgencyAssignmentDate();
						}
					}
				} else if ( isExternalSoldAgency(collectionBillingAccountData.getAgencyAssignmentCode()) ) {
					// Sold to external agency accounts should be sold within last 36 month and aggregated Agency Assignment Amount across all these accounts should be greater than 100$.
					if ( collectionBillingAccountData.getAgencyAssignmentDate() != null
						 && ( collectionBillingAccountData.getAgencyAssignmentDate().compareTo(getSysDateMinusMonths(getExternalAgencySoldAssignmentMonthPeriod()) )  >= 0 )
						 && getAggregatedSoldExternalAmount(customerBillingAccountDataList) > CollectionSummarizationServiceImpl.this.getAggregatedSoldExternalAmount() ) {
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

		private void summarizeBDSScore(
				CollectionBillingAccountData collectionBillingAccountData) {
			if ( !collectionBillingAccountData.getAccountStatus().equals( CLOSED_ACCOUNT_STATUS )) {
				m_isAllAccountsClosed = false;
			}
			if ( collectionBillingAccountData.getAccountStatus().equals( OPEN_ACCOUNT_STATUS ) ) {
				m_hasAtleastOneAccountOpen = true;
				if ( m_worstBDSOpenAccountScore == null ) {
					m_worstBDSOpenAccountScore = collectionBillingAccountData.getCollectionScore();
				}
				else if ( collectionBillingAccountData.getCollectionScore() != null ) {
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
			else if ( collectionBillingAccountData.getAccountStatus().equals( CLOSED_ACCOUNT_STATUS ) ) {
				// use latest closed account score
				if ( m_latestClosedAccountScoreDate == null ) {
					m_latestClosedAccountScoreDate = collectionBillingAccountData.getAccountStatusDate();
					m_latestClosedAccountScore = collectionBillingAccountData.getCollectionScore();
				}
				else if ( collectionBillingAccountData.getAccountStatusDate() != null ) {
					if ( collectionBillingAccountData.getAccountStatusDate().compareTo( m_latestClosedAccountScoreDate ) > 0 ) {
						m_latestClosedAccountScoreDate = collectionBillingAccountData.getCollectionScoreDate();
						m_latestClosedAccountScore = collectionBillingAccountData.getCollectionScore();
					}
				}
			}
		}

		

		private double getAggregatedSoldExternalAmount(
				List<CollectionBillingAccountData> customerBillingAccountDataList) {
			double result = 0;
			for ( CollectionBillingAccountData collectionBillingAccountData: customerBillingAccountDataList ) {
				if ( collectionBillingAccountData.getAgencyAssignmentCode() != null 
					 && isExternalSoldAgency( collectionBillingAccountData.getAgencyAssignmentCode() )
					 && collectionBillingAccountData.getAgencyAssignmentAmount() != null ) {
					result += collectionBillingAccountData.getAgencyAssignmentAmount().doubleValue();
				}
			}
			return result;
		}

		private Date getSysDateMinusMonths(
				int monthsToSubtract) {
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

		private int scoreLevel(String collectionScore) {
			
			try {
				return Integer.parseInt(collectionScore);
			}
			catch ( NumberFormatException numberFormatException ) {
				log.error("Error parsing collection score: " + collectionScore );
			}
			return 0;
		}
		
	}



	

}
