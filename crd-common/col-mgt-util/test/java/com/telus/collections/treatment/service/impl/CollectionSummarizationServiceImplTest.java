package com.telus.collections.treatment.service.impl;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.collections.treatment.service.dto.CollectionBillingAccountData;
import com.telus.collections.treatment.service.dto.CustomerCollectionData;
import com.telus.credit.framework.test.TelusJUnitClassRunner;

@RunWith(TelusJUnitClassRunner.class)
@ContextConfiguration("classpath:collectionMgtUtil-spring.xml")
public class CollectionSummarizationServiceImplTest {
	
	@Autowired
	private CollectionSummarizationServiceImpl m_collectionSummarizationService;
	
	@Test
	public void test_OneAccountWithMostEmptyData() throws ParseException {
		List<CollectionBillingAccountData> customerBillingAccountDataList = new ArrayList<CollectionBillingAccountData>();
		CollectionBillingAccountData cb = new CollectionBillingAccountData();
		cb.setAccountStatus("O");
		cb.setAccountStatusDate( convertStringToDate( "12/06/1990" ) );
		CustomerCollectionData summarizedResult = m_collectionSummarizationService.summarizeCollectionDataByCustomer(customerBillingAccountDataList);
		printCustomerCollectionData(summarizedResult);
		assertTrue( "Collection Score must be null",  ( summarizedResult.getCollectionScore() == null ) );
	}
	
	@Test
	public void test_NoAccountInList() {
		List<CollectionBillingAccountData> customerBillingAccountDataList = new ArrayList<CollectionBillingAccountData>();
		CustomerCollectionData summarizedResult = m_collectionSummarizationService.summarizeCollectionDataByCustomer(customerBillingAccountDataList);
		printCustomerCollectionData(summarizedResult);
		assertTrue( "Collection Score must be null",  ( summarizedResult.getCollectionScore() == null ) );
	}
	
	/**
	 * 	Requirement: BDS Score   
	 *		If no active accounts exists for that customer  -  use latest closed account score, for Monthly re-assessment batch process customers with no active accounts will not be part of re-assessment.
	 * @throws ParseException 
	 *
	 */
	@Test
	public void test_BDSSCoreNoActiveAccountOneClosed() throws ParseException {
		List<CollectionBillingAccountData> customerBillingAccountDataList = new ArrayList<CollectionBillingAccountData>();
		
		CollectionBillingAccountData cb = new CollectionBillingAccountData();
		cb.setAccountStatus("C");
		cb.setAccountStatusDate( convertStringToDate( "12/06/2012" ) );
		cb.setCollectionScore("600");
		cb.setCollectionScoreDate( convertStringToDate( "12/06/2012" ) );
		customerBillingAccountDataList.add( cb );
		CustomerCollectionData summarizedResult = m_collectionSummarizationService.summarizeCollectionDataByCustomer(customerBillingAccountDataList);
		printCustomerCollectionData(summarizedResult);
		assertTrue( "Collection Score should be 600 for test_BDSSCoreNoActiveAccountOneClosed", ( summarizedResult.getCollectionScore().equals("600") ) );
	}
	
	/**
	 * 	Requirement: BDS Score   
	 *		If no active accounts exists for that customer  -  use latest closed account score, for Monthly re-assessment batch process customers with no active accounts will not be part of re-assessment.
	 * @throws ParseException 
	 *
	 */
	@Test
	public void test_BDSSCoreNoActiveAccountTwoClosed() throws ParseException {
		List<CollectionBillingAccountData> customerBillingAccountDataList = new ArrayList<CollectionBillingAccountData>();
		
		CollectionBillingAccountData cb = new CollectionBillingAccountData();
		cb.setAccountStatus("C");
		cb.setAccountStatusDate( convertStringToDate( "12/06/2012" ) );
		cb.setCollectionScore("600");
		//cb.setCollectionScoreDate( convertStringToDate( "12/06/2012" ) );
		customerBillingAccountDataList.add( cb );
		
		CollectionBillingAccountData cb2= new CollectionBillingAccountData();
		cb2.setAccountStatus("C");
		cb2.setAccountStatusDate( convertStringToDate( "12/06/2013" ) );
		cb2.setCollectionScore("300");
		//cb.setCollectionScoreDate( convertStringToDate( "12/06/2013" ) );
		customerBillingAccountDataList.add( cb2 );
		
		CustomerCollectionData summarizedResult = m_collectionSummarizationService.summarizeCollectionDataByCustomer(customerBillingAccountDataList);
		printCustomerCollectionData(summarizedResult);
		assertTrue( "Collection Score should be 300 for test_BDSSCoreNoActiveAccountTwoClosed", ( summarizedResult.getCollectionScore().equals("300") ) );
	}
	
	/**
	 * If at least one active account take worst BDS score across all of the open accounts for that customer.
	 * 
	 * @throws ParseException
	 */
	@Test
	public void test_BDSScoreWithOneActiveTwoClosed() throws ParseException {
		List<CollectionBillingAccountData> customerBillingAccountDataList = new ArrayList<CollectionBillingAccountData>();
		
		CollectionBillingAccountData cb = new CollectionBillingAccountData();
		cb.setAccountStatus("C");
		cb.setAccountStatusDate( convertStringToDate( "12/06/2012" ) );
		cb.setCollectionScore("600");
		//cb.setCollectionScoreDate( convertStringToDate( "12/06/2012" ) );
		customerBillingAccountDataList.add( cb );
		
		CollectionBillingAccountData cb2= new CollectionBillingAccountData();
		cb2.setAccountStatus("C");
		cb2.setAccountStatusDate( convertStringToDate( "12/06/2013" ) );
		cb2.setCollectionScore("300");
		//cb.setCollectionScoreDate( convertStringToDate( "12/06/2013" ) );
		customerBillingAccountDataList.add( cb2 );
		
		CollectionBillingAccountData cb3= new CollectionBillingAccountData();
		cb3.setAccountStatus("O");
		cb3.setAccountStatusDate( convertStringToDate( "12/06/2011" ) );
		cb3.setCollectionScore("400");
		//cb.setCollectionScoreDate( convertStringToDate( "12/06/2013" ) );
		customerBillingAccountDataList.add( cb3 );
		
		CustomerCollectionData summarizedResult = m_collectionSummarizationService.summarizeCollectionDataByCustomer(customerBillingAccountDataList);
		printCustomerCollectionData(summarizedResult);
		assertTrue( "Collection Score should be 400 for test_BDSScoreWithOneActiveTwoClosed", ( summarizedResult.getCollectionScore().equals("400") ) );
	}
	
	/**
	 * If at least one active account take worst BDS score across all of the open accounts for that customer.
	 * 
	 * @throws ParseException
	 */
	@Test
	public void test_BDSScoreWithTwoActiveTwoClosed() throws ParseException {
		List<CollectionBillingAccountData> customerBillingAccountDataList = new ArrayList<CollectionBillingAccountData>();
		
		CollectionBillingAccountData cb = new CollectionBillingAccountData();
		cb.setAccountStatus("C");
		cb.setAccountStatusDate( convertStringToDate( "12/06/2012" ) );
		cb.setCollectionScore("600");
		//cb.setCollectionScoreDate( convertStringToDate( "12/06/2012" ) );
		customerBillingAccountDataList.add( cb );
		
		CollectionBillingAccountData cb2= new CollectionBillingAccountData();
		cb2.setAccountStatus("C");
		cb2.setAccountStatusDate( convertStringToDate( "12/06/2013" ) );
		cb2.setCollectionScore("300");
		//cb.setCollectionScoreDate( convertStringToDate( "12/06/2013" ) );
		customerBillingAccountDataList.add( cb2 );
		
		CollectionBillingAccountData cb3= new CollectionBillingAccountData();
		cb3.setAccountStatus("O");
		cb3.setAccountStatusDate( convertStringToDate( "12/06/2011" ) );
		cb3.setCollectionScore("400");
		//cb.setCollectionScoreDate( convertStringToDate( "12/06/2013" ) );
		customerBillingAccountDataList.add( cb3 );
		
		CollectionBillingAccountData cb4= new CollectionBillingAccountData();
		cb4.setAccountStatus("O");
		cb4.setAccountStatusDate( convertStringToDate( "12/06/2012" ) );
		cb4.setCollectionScore("200");
		//cb.setCollectionScoreDate( convertStringToDate( "12/06/2013" ) );
		customerBillingAccountDataList.add( cb4 );
		
		CustomerCollectionData summarizedResult = m_collectionSummarizationService.summarizeCollectionDataByCustomer(customerBillingAccountDataList);
		printCustomerCollectionData(summarizedResult);
		assertTrue( "Collection Score should be 200 for test_BDSScoreWithTwoActiveTwoClosed", ( summarizedResult.getCollectionScore().equals("200") ) );
	}
	
	@Test
	public void testAgencyAssignedTo1ExternalWithin12Months() throws ParseException {
		/*
		# external agency assigned accounts
		Total Current AR Balance owing on external agency assigned accounts
		Date of most recent external agency assignment
		  
		1. Count only accounts assigned or sold to external agency.
		2. Assigned to external agency accounts should be assigned within last 24 month 
		with Current AR balance greater than 0$ 
		3. Sold to external agency accounts should be sold within last 36 month 
		and aggregated Agency Assignment Amount across all these accounts should be greater than 100$.
		 */
		List<CollectionBillingAccountData> customerBillingAccountDataList = new ArrayList<CollectionBillingAccountData>();
		
		CollectionBillingAccountData cb = new CollectionBillingAccountData();
		cb.setAccountStatus("C");
		cb.setAccountStatusDate( convertStringToDate( "12/06/2013" ) );
		cb.setAgencyAssignmentAmount(BigDecimal.valueOf(120));
		cb.setAccountBalance(BigDecimal.valueOf(120));
		cb.setAgencyAssignmentCode("CBV");
		cb.setAgencyAssignmentDate(convertStringToDate( "12/06/2012") );
		customerBillingAccountDataList.add( cb );
		
		CustomerCollectionData summarizedResult = m_collectionSummarizationService.summarizeCollectionDataByCustomer(customerBillingAccountDataList);
		printCustomerCollectionData(summarizedResult);
		assertTrue( "No of external agency accounts should be 1", (summarizedResult.getNumberOfAccountsInAgency() == 1) );
		assertTrue( "Accounts in agency balance should be 120", (summarizedResult.getAccountsInAgencyBalance().intValue() == 120));
		assertTrue( "Agency Assignment Date should be 12/06/2012", (convertDateToString(summarizedResult.getLatestAgencyAssignmentDate()).equals("12/06/2012")));
	}
	
	@Test
	public void testAgencyAssignedTo2ExternalWithin12Months() throws ParseException {
		/*
		# external agency assigned accounts
		Total Current AR Balance owing on external agency assigned accounts
		Date of most recent external agency assignment
		  
		1. Count only accounts assigned or sold to external agency.
		2. Assigned to external agency accounts should be assigned within last 24 month 
		with Current AR balance greater than 0$ 
		3. Sold to external agency accounts should be sold within last 36 month 
		and aggregated Agency Assignment Amount across all these accounts should be greater than 100$.
		 */
		List<CollectionBillingAccountData> customerBillingAccountDataList = new ArrayList<CollectionBillingAccountData>();
		
		CollectionBillingAccountData cb = new CollectionBillingAccountData();
		cb.setAccountStatus("C");
		cb.setAccountStatusDate( convertStringToDate( "12/06/2013" ) );
		cb.setAgencyAssignmentAmount(BigDecimal.valueOf(120));
		cb.setAccountBalance(BigDecimal.valueOf(120));
		cb.setAgencyAssignmentCode("CBV");
		cb.setAgencyAssignmentDate(convertStringToDate( "12/06/2012") );
		customerBillingAccountDataList.add( cb );
		
		cb = new CollectionBillingAccountData();
		cb.setAccountStatus("C");
		cb.setAccountStatusDate( convertStringToDate( "12/06/2013" ) );
		cb.setAgencyAssignmentAmount(BigDecimal.valueOf(120));
		cb.setAccountBalance(BigDecimal.valueOf(220));
		cb.setAgencyAssignmentCode("CBV");
		cb.setAgencyAssignmentDate(convertStringToDate( "12/15/2012") );
		customerBillingAccountDataList.add( cb );
		
		CustomerCollectionData summarizedResult = m_collectionSummarizationService.summarizeCollectionDataByCustomer(customerBillingAccountDataList);
		printCustomerCollectionData(summarizedResult);
		assertTrue( "No of external agency accounts should be 2", (summarizedResult.getNumberOfAccountsInAgency() == 2) );
		assertTrue( "Accounts in agency balance should be 340", (summarizedResult.getAccountsInAgencyBalance().intValue() == 340));
		assertTrue( "Agency Assignment Date should be 12/15/2012", (convertDateToString(summarizedResult.getLatestAgencyAssignmentDate()).equals("12/15/2012")));
	}
	
	@Test
	public void testAgencyAssignedTo1External1SoldAgencyButWithUnder100() throws ParseException {
		/*
		# external agency assigned accounts
		Total Current AR Balance owing on external agency assigned accounts
		Date of most recent external agency assignment
		  
		1. Count only accounts assigned or sold to external agency.
		2. Assigned to external agency accounts should be assigned within last 24 month 
		with Current AR balance greater than 0$ 
		3. Sold to external agency accounts should be sold within last 36 month 
		and aggregated Agency Assignment Amount across all these accounts should be greater than 100$.
		 */
		List<CollectionBillingAccountData> customerBillingAccountDataList = new ArrayList<CollectionBillingAccountData>();
		
		CollectionBillingAccountData cb = new CollectionBillingAccountData();
		cb.setAccountStatus("C");
		cb.setAccountStatusDate( convertStringToDate( "12/06/2013" ) );
		cb.setAgencyAssignmentAmount(BigDecimal.valueOf(20));
		cb.setAccountBalance(BigDecimal.valueOf(120));
		cb.setAgencyAssignmentCode("PACBV");
		cb.setAgencyAssignmentDate(convertStringToDate( "12/06/2012") );
		customerBillingAccountDataList.add( cb );
		
		cb = new CollectionBillingAccountData();
		cb.setAccountStatus("C");
		cb.setAccountStatusDate( convertStringToDate( "12/06/2013" ) );
		cb.setAgencyAssignmentAmount(BigDecimal.valueOf(120));
		cb.setAccountBalance(BigDecimal.valueOf(220));
		cb.setAgencyAssignmentCode("CBV");
		cb.setAgencyAssignmentDate(convertStringToDate( "12/15/2012") );
		customerBillingAccountDataList.add( cb );
		
		CustomerCollectionData summarizedResult = m_collectionSummarizationService.summarizeCollectionDataByCustomer(customerBillingAccountDataList);
		printCustomerCollectionData(summarizedResult);
		assertTrue( "No of external agency accounts should be 1", (summarizedResult.getNumberOfAccountsInAgency() == 1 ) );
		assertTrue( "Accounts in agency balance should be 220", (summarizedResult.getAccountsInAgencyBalance().intValue() == 220));
		assertTrue( "Agency Assignment Date should be 12/15/2012", (convertDateToString(summarizedResult.getLatestAgencyAssignmentDate()).equals("12/15/2012")));
	}
	
	@Test
	public void testAgencyAssignedTo1External1SoldAgencyButWithOVer100() throws ParseException {
		/*
		# external agency assigned accounts
		Total Current AR Balance owing on external agency assigned accounts
		Date of most recent external agency assignment
		  
		1. Count only accounts assigned or sold to external agency.
		2. Assigned to external agency accounts should be assigned within last 24 month 
		with Current AR balance greater than 0$ 
		3. Sold to external agency accounts should be sold within last 36 month 
		and aggregated Agency Assignment Amount across all these accounts should be greater than 100$.
		 */
		List<CollectionBillingAccountData> customerBillingAccountDataList = new ArrayList<CollectionBillingAccountData>();
		
		CollectionBillingAccountData cb = new CollectionBillingAccountData();
		cb.setAccountStatus("C");
		cb.setAccountStatusDate( convertStringToDate( "12/06/2013" ) );
		cb.setAgencyAssignmentAmount(BigDecimal.valueOf(120));
		cb.setAccountBalance(BigDecimal.valueOf(120));
		cb.setAgencyAssignmentCode("PACBV");
		cb.setAgencyAssignmentDate(convertStringToDate( "12/06/2012") );
		customerBillingAccountDataList.add( cb );
		
		cb = new CollectionBillingAccountData();
		cb.setAccountStatus("C");
		cb.setAccountStatusDate( convertStringToDate( "12/06/2013" ) );
		cb.setAgencyAssignmentAmount(BigDecimal.valueOf(120));
		cb.setAccountBalance(BigDecimal.valueOf(220));
		cb.setAgencyAssignmentCode("CBV");
		cb.setAgencyAssignmentDate(convertStringToDate( "12/15/2012") );
		customerBillingAccountDataList.add( cb );
		
		CustomerCollectionData summarizedResult = m_collectionSummarizationService.summarizeCollectionDataByCustomer(customerBillingAccountDataList);
		printCustomerCollectionData(summarizedResult);
		assertTrue( "No of external agency accounts should be 2", (summarizedResult.getNumberOfAccountsInAgency() == 2) );
		assertTrue( "Accounts in agency balance should be 340", (summarizedResult.getAccountsInAgencyBalance().intValue() == 340));
		assertTrue( "Agency Assignment Date should be 12/15/2012", (convertDateToString(summarizedResult.getLatestAgencyAssignmentDate()).equals("12/15/2012")));
	}
	
	@Test
	public void testInvoluntaryCancelledAccounts() throws ParseException {
		/*# involuntary cancelled accounts
		Date of most recent involuntary cancelled account
		Balance owing on involuntary cancelled accounts 
		
		Aggregate data on involuntary cancelled accounts within last 24 month.*/
		List<CollectionBillingAccountData> customerBillingAccountDataList = new ArrayList<CollectionBillingAccountData>();
		
		CollectionBillingAccountData cb = new CollectionBillingAccountData();
		cb.setAccountStatus("C");
		cb.setAccountStatusDate( convertStringToDate( "12/06/2012" ) );
		cb.setInvoluntaryCeasedIndicator(true);
		cb.setAccountBalance(BigDecimal.valueOf(120));
		customerBillingAccountDataList.add( cb );
		
		cb = new CollectionBillingAccountData();
		cb.setAccountStatus("C");
		cb.setAccountStatusDate( convertStringToDate( "12/06/2010" ) );
		cb.setInvoluntaryCeasedIndicator(true);
		cb.setAccountBalance(BigDecimal.valueOf(100));
		customerBillingAccountDataList.add( cb );
		
		cb = new CollectionBillingAccountData();
		cb.setAccountStatus("C");
		cb.setAccountStatusDate( convertStringToDate( "12/07/2012" ) );
		cb.setInvoluntaryCeasedIndicator(true);
		cb.setAccountBalance(BigDecimal.valueOf(130));
		customerBillingAccountDataList.add( cb );
		
		CustomerCollectionData summarizedResult = m_collectionSummarizationService.summarizeCollectionDataByCustomer(customerBillingAccountDataList);
		printCustomerCollectionData(summarizedResult);
		assertTrue("# of involuntary cancelled accounts should be 2", (summarizedResult.getInvoluntaryCancelledAccounts().intValue() == 2) );
		assertTrue("Balance on involuntary cancelled accounts should be 250", (summarizedResult.getInvoluntaryCancelledAccountsBalance().intValue()==250));
		assertTrue("Date of most recent involuntary cancelled account should be 12/07/2012", 
				(convertDateToString(summarizedResult.getLatestInvoluntaryCancelledDate()).equals("12/07/2012")));
	}
	
	@Test
	public void test_CollectionIndicatorForClosedAccount() throws ParseException {
		/*latest In collection indicator
		 latest collection indicator start date
		 latest collection indicator end date

		  
		In Collection treatment indicator has to be summarized for all customers active accounts based on below logic:
		indicator set to yes if at least one account is currently in Collection treatment
		otherwise it will be set to no*/
		List<CollectionBillingAccountData> customerBillingAccountDataList = new ArrayList<CollectionBillingAccountData>();
		
		CollectionBillingAccountData cb = new CollectionBillingAccountData();
		cb.setAccountStatus("C");
		cb.setAccountStatusDate( convertStringToDate( "12/06/2012" ) );
		cb.setCollectionIndicator(true);
		cb.setCollectionStartDate(convertStringToDate( "12/06/2011" ) );
		customerBillingAccountDataList.add( cb );
		CustomerCollectionData summarizedResult = m_collectionSummarizationService.summarizeCollectionDataByCustomer(customerBillingAccountDataList);
		printCustomerCollectionData(summarizedResult);
		assertTrue( "latest in collection indicator should be false", !summarizedResult.isCollectionInd()  );
		assertTrue( "Latest collection start date should be null", summarizedResult.getLatestCollectionStartDate() == null );
		assertTrue( "Latest collection end date should be null", summarizedResult.getLatestCollectionEndDate() == null );
	}
	
	@Test
	public void test_CollectionStartEndDateFor1Open1ClosedAccount() throws ParseException {
		
		List<CollectionBillingAccountData> customerBillingAccountDataList = new ArrayList<CollectionBillingAccountData>();
		
		CollectionBillingAccountData cb = new CollectionBillingAccountData();
		cb.setAccountStatus("O");
		cb.setAccountStatusDate( convertStringToDate( "12/06/2012" ) );
		cb.setCollectionIndicator(true);
		cb.setCollectionStartDate(convertStringToDate( "12/06/2011" ) );
		customerBillingAccountDataList.add( cb );
		
		cb = new CollectionBillingAccountData();
		cb.setAccountStatus("O");
		cb.setAccountStatusDate( convertStringToDate( "12/06/2012" ) );
		cb.setCollectionIndicator(false);
		cb.setCollectionStartDate(convertStringToDate( "10/06/2011" ) );
		cb.setCollectionEndDate(convertStringToDate( "11/08/2011" ) );
		customerBillingAccountDataList.add( cb );
		
		CustomerCollectionData summarizedResult = m_collectionSummarizationService.summarizeCollectionDataByCustomer(customerBillingAccountDataList);
		printCustomerCollectionData(summarizedResult);
		assertTrue( "latest in collection indicator should be true", summarizedResult.isCollectionInd()  );
		assertTrue( "Latest collection start date should be 12/06/2011", convertDateToString(summarizedResult.getLatestCollectionStartDate()).equals("12/06/2011") );
		assertTrue( "Latest collection end date should be null", summarizedResult.getLatestCollectionEndDate() == null );
	}
	
	@Test
	public void test_CollectionIndicatorForOpenAccounts() throws ParseException {
		/*latest In collection indicator
		 latest collection indicator start date
		 latest collection indicator end date

		  
		In Collection treatment indicator has to be summarized for all customers active accounts based on below logic:
		indicator set to yes if at least one account is currently in Collection treatment
		otherwise it will be set to no*/
		List<CollectionBillingAccountData> customerBillingAccountDataList = new ArrayList<CollectionBillingAccountData>();
		
		CollectionBillingAccountData cb = new CollectionBillingAccountData();
		cb.setAccountStatus("O");
		cb.setAccountStatusDate( convertStringToDate( "12/06/2012" ) );
		cb.setCollectionIndicator(true);
		cb.setCollectionStartDate(convertStringToDate( "12/06/2011" ) );
		customerBillingAccountDataList.add( cb );
		
		cb = new CollectionBillingAccountData();
		cb.setAccountStatus("O");
		cb.setAccountStatusDate( convertStringToDate( "12/06/2012" ) );
		cb.setCollectionIndicator(true);
		cb.setCollectionStartDate(convertStringToDate( "12/06/2012" ) );
		customerBillingAccountDataList.add( cb );
		
		CustomerCollectionData summarizedResult = m_collectionSummarizationService.summarizeCollectionDataByCustomer(customerBillingAccountDataList);
		printCustomerCollectionData(summarizedResult);
		assertTrue( "latest in collection indicator should be true", summarizedResult.isCollectionInd()  );
		assertTrue( "Latest collection start date should be 12/06/2012", convertDateToString(summarizedResult.getLatestCollectionStartDate()).equals("12/06/2012") );
		assertTrue( "Latest collection end date should be null", summarizedResult.getLatestCollectionEndDate() == null );
		
	}
	
	@Test
	public void test_NSF() throws ParseException {
		List<CollectionBillingAccountData> customerBillingAccountDataList = new ArrayList<CollectionBillingAccountData>();
		
		CollectionBillingAccountData cb = new CollectionBillingAccountData();
		cb.setAccountStatus("O");
		cb.setAccountStatusDate( convertStringToDate( "12/06/2012" ) );	
		cb.setNumberOfNSFCheques(1);
		customerBillingAccountDataList.add( cb );
		
		cb = new CollectionBillingAccountData();
		cb.setAccountStatus("C");
		cb.setAccountStatusDate( convertStringToDate( "12/06/2012" ) );	
		cb.setNumberOfNSFCheques(2);
		customerBillingAccountDataList.add( cb );
		
		CustomerCollectionData summarizedResult = m_collectionSummarizationService.summarizeCollectionDataByCustomer(customerBillingAccountDataList);
		printCustomerCollectionData(summarizedResult);
		
		assertTrue( "No of NSF should be 3", summarizedResult.getNumberOfNSFCheques() == 3 );
	}
	@Test
	public void test_SysDateMinusMonths() {
		System.out.println("test_SysDateMinusMonths()");
		System.out.println("Sysdate minus 24 months: " + getSysDateMinusMonths(24) );
	}

	private void printCustomerCollectionData(
			CustomerCollectionData summarizedResult) {
		System.out.println( "\tCollection Score: " + summarizedResult.getCollectionScore()
							+ "\n\t InvoluntaryCancelledAccounts:" + summarizedResult.getInvoluntaryCancelledAccounts()
							+ "\n\t NumberOfAccountsInAgency: " + summarizedResult.getNumberOfAccountsInAgency()
							+ "\n\t NumberOfNSFCheques: " + summarizedResult.getNumberOfNSFCheques()
							+ "\n\t AccountsInAgencyBalance: " + summarizedResult.getAccountsInAgencyBalance()
							+ "\n\t InvoluntaryCancelledAccountsBalance: " + summarizedResult.getInvoluntaryCancelledAccountsBalance()
							+ "\n\t LatestAgencyAssignmentDate: " + convertDateToString(summarizedResult.getLatestAgencyAssignmentDate())
							+ "\n\t LatestCollectionEndDate: " + convertDateToString(summarizedResult.getLatestCollectionEndDate())
							+ "\n\t LatestCollectionStartDate: " + convertDateToString(summarizedResult.getLatestCollectionStartDate())
							+ "\n\t LatestInvoluntaryCancelledDate: " + convertDateToString(summarizedResult.getLatestInvoluntaryCancelledDate() )
							+ "\n\t collection indicator: " + summarizedResult.isCollectionInd() );
	}
	private String convertDateToString( Date dateObj ) {
		return ( dateObj == null ? null : (new SimpleDateFormat("mm/dd/yyyy")).format(dateObj) );
	}

	private Date convertStringToDate( String dateStr ) throws ParseException {
		DateFormat df = new SimpleDateFormat("mm/dd/yyyy");
		return df.parse(dateStr);
	}
	
	private Date getSysDateMinusMonths(
			int monthsToSubtract) {
		Calendar c = Calendar.getInstance(); 
		c.setTime(new Date()); 
		c.add(Calendar.MONTH, -monthsToSubtract);
		return c.getTime();
	}
	/*Attribute
	 Logic to aggregate account level data 
	 
	BDS Score  
	  
	If no active accounts exists for that customer  -  use latest closed account score, for Monthly re-assessment batch process customers with no active accounts will not be part of re-assessment.
	If at least one active account take worst BDS score across all of the open accounts for that customer.
	 
	 
	# external agency assigned accounts
	Total Current AR Balance owing on external agency assigned accounts
	Date of most recent external agency assignment
	  
	Count only accounts assigned or sold to external agency.
	Assigned to external agency accounts should be assigned within last 24 month with Current AR balance greater than 0$ 
	Sold to external agency accounts should be sold within last 36 month and aggregated Agency Assignment Amount across all these accounts should be greater than 100$.
	 
	 
	# involuntary cancelled accounts
	Date of most recent involuntary cancelled account
	Balance owing on involuntary cancelled accounts 
	  
	Aggregate data on involuntary cancelled accounts within last 24 month.
	 
	 latest In collection indicator
	 latest collection indicator start date
	 latest collection indicator end date

	  
	In Collection treatment indicator has to be summarized for all customers active accounts based on below logic:
	indicator set to yes if at least one account is currently in Collection treatment
	otherwise it will be set to no */
	 

}
