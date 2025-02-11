/*package com.fico.telus.rtca.blaze;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.fico.telus.blaze.creditAsessment.CreditAssessmentRequest;
import com.fico.telus.blaze.creditAsessment.ExistingBaseCustomerCreditAssessmentRequest;
import com.fico.telus.blaze.creditCommon.AdjudicationResult;
import com.fico.telus.blaze.creditCommon.CollectionData;
import com.fico.telus.blaze.creditCommon.CreditBureauResult;
import com.fico.telus.blaze.creditCommon.CreditCustomerInfo;
import com.fico.telus.blaze.creditCommon.CreditDecision;
import com.fico.telus.blaze.creditCommon.CreditProfileData;
import com.fico.telus.blaze.creditCommon.CreditWorthinessData;
import com.fico.telus.blaze.creditCommon.DepositData;
import com.fico.telus.blaze.creditCommon.PersonName;
import com.fico.telus.blaze.creditCommon.RiskIndicator;
import com.telus.credit.framework.test.TelusConfig;
import com.telus.credit.framework.test.TelusJUnitClassRunner;

@SuppressWarnings("deprecation")
@RunWith(TelusJUnitClassRunner.class)
@TelusConfig(configAppCtxFile="appCtx-Decisioning.properties")
@ContextConfiguration("classpath:crda-decisionengine-spring.xml")
public class TestClient {
	
	@Autowired
	RuleServicesBean rules;
	
	@Test
    public void test_performCreditAssessment() throws Exception
    {
		long startTime = System.currentTimeMillis();
		System.out.println( "Elapsed time in milliseconds: Initialization    = " + (System.currentTimeMillis()- startTime));
		startTime = System.currentTimeMillis();
		try { 
			rules.performCreditAssessment(123456,TestClient.getTest_CA_Main_160_030a());
		}
		catch (Exception ex) {
			System.out.println("*** ERROR: " + ex.getMessage());
		}
		System.out.println( "Elapsed time in milliseconds: First invocation  = " + (System.currentTimeMillis()- startTime));
		rules.shutdown();
    }
	
	
		
	public static CreditAssessmentRequest getTest_CA_Main_160_030a() throws Exception {
		//*****************************************************
		//* CREATE CREDIT ASSESSMENT OBJECT
		//*****************************************************
		ExistingBaseCustomerCreditAssessmentRequest caRequest = new ExistingBaseCustomerCreditAssessmentRequest();

		//*****************************************************
		//* SET REQUEST TYPE AND SUBTYPE
		//*****************************************************
		caRequest.setCreditAssessmentTypeCd("FULL_ASSESSMENT");
		caRequest.setCreditAssessmentSubTypeCd("AUTO_ASSESSMENT");

		//*****************************************************
		//* ADD CUSTOMER DATA
		//*****************************************************
		caRequest.setCustomerData(new CreditCustomerInfo());
		caRequest.getCustomerData().setCustomerId(29584575);
				
		GregorianCalendar gCal = new GregorianCalendar();
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		
		gCal.setTime(new Timestamp(dateFormat.parse("12-Jun-2013").getTime()));
		caRequest.getCustomerData().setCustomerCreationDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gCal));
		if (caRequest.getCustomerData().getCustomerCreationDate().getYear() == 4444) 
		{
			caRequest.getCustomerData().setCustomerCreationDate(null);
		}
		caRequest.getCustomerData().setRevenueSegmentCd("1");
		caRequest.getCustomerData().setCustomerTypeCd("R");
		caRequest.getCustomerData().setEmployeeInd(false);

		//*****************************************************
		//* ADD PERSON NAME DATA
		//*****************************************************
		caRequest.getCustomerData().setPersonName( new PersonName() );
		caRequest.getCustomerData().getPersonName().setFirstName("John");
		caRequest.getCustomerData().getPersonName().setLastName("Doe");

		//*****************************************************
		//* ADD CREDIT PROFILE DATA
		//*****************************************************
		caRequest.setCreditProfileData( new CreditProfileData() );
		caRequest.getCreditProfileData().setCreditCheckConsentCd("Y");
		caRequest.getCreditProfileData().setCountryCd("CAN");
		caRequest.getCreditProfileData().setApplicationProvinceCd("AB");
		caRequest.getCreditProfileData().setBypassMatchIndicator(false);

		//* Treat date with 4444 year as null date

		gCal.setTime(new Timestamp(dateFormat.parse("12-Jun-2013").getTime()));
		caRequest.getCreditProfileData().setFirstCreditAssessmentDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gCal));
		caRequest.getCreditProfileData().setFirstCreditAssessmentDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gCal));
		if (caRequest.getCreditProfileData().getFirstCreditAssessmentDate().getYear() == 4444) 
		{
			caRequest.getCreditProfileData().setFirstCreditAssessmentDate(null);
		}

		gCal.setTime(new Timestamp(dateFormat.parse("12-Jun-2013").getTime()));
		caRequest.getCreditProfileData().setLatestCreditAssessmentDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gCal));
		caRequest.getCreditProfileData().setLatestCreditAssessmentDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gCal));
		if (caRequest.getCreditProfileData().getLatestCreditAssessmentDate().getYear() == 4444) 
		{
			caRequest.getCreditProfileData().setLatestCreditAssessmentDate(null);
		}


		gCal.setTime(new Timestamp(dateFormat.parse("12-Jun-2013").getTime()));
		caRequest.getCreditProfileData().setCreditValueEffectiveDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gCal));
		caRequest.getCreditProfileData().setCreditValueEffectiveDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gCal));
		if (caRequest.getCreditProfileData().getCreditValueEffectiveDate().getYear() == 4444) 
		{
			caRequest.getCreditProfileData().setCreditValueEffectiveDate(null);
		}
		

		//*****************************************************
		//* ADD CREDIT WORTHINESS DATA
		//*****************************************************
		caRequest.getCreditProfileData().setCreditWorthinessData(new CreditWorthinessData() );
		caRequest.getCreditProfileData().getCreditWorthinessData().setCreditValueCd("V");
		caRequest.getCreditProfileData().getCreditWorthinessData().setDecisionCd("");
		caRequest.getCreditProfileData().getCreditWorthinessData().setAssessmentMessageCd("");
		caRequest.getCreditProfileData().getCreditWorthinessData().setFraudIndicatorCd("");


		//*****************************************************
		//* ADD BUREAU RESULT DATA
		//*****************************************************
		caRequest.setBureauResultData( new CreditBureauResult() );
		caRequest.getBureauResultData().setErrorCd("");
		caRequest.getBureauResultData().setReportSourceCd("EQUIFAX");

		gCal.setTime(new Timestamp(dateFormat.parse("12-Jun-2013").getTime()));
		caRequest.getBureauResultData().setCreationDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gCal));
		if (caRequest.getBureauResultData().getCreationDate().getYear() == 4444) 
		{
			caRequest.getBureauResultData().setCreationDate(null);
		}


		caRequest.getBureauResultData().setAdjudicationResult( new AdjudicationResult() );
		caRequest.getBureauResultData().getAdjudicationResult().setCreditScore("");
		caRequest.getBureauResultData().getAdjudicationResult().setCreditClass("ANY");

		caRequest.getBureauResultData().getAdjudicationResult().setCreditDecision( new CreditDecision() );
		caRequest.getBureauResultData().getAdjudicationResult().getCreditDecision().setCreditDecisionCd("");

		caRequest.getBureauResultData().setRiskIndicator( new RiskIndicator() );
		caRequest.getBureauResultData().getRiskIndicator().setNoHitThinFileInd(false);
		caRequest.getBureauResultData().getRiskIndicator().setTrueThinFileInd(false);
		caRequest.getBureauResultData().getRiskIndicator().setHighRiskThinFileInd(false);
		caRequest.getBureauResultData().getRiskIndicator().setTempSINInd(false);
		caRequest.getBureauResultData().getRiskIndicator().setUnderAgeInd(false);
		caRequest.getBureauResultData().getRiskIndicator().setBankcrupcyInd(false);
		caRequest.getBureauResultData().getRiskIndicator().setWriteOffInd("None");
		caRequest.getBureauResultData().getRiskIndicator().setPrimaryRiskInd("DEFAULT");
		caRequest.getBureauResultData().getRiskIndicator().setSecondaryRiskInd("NON-FRAUD");

		//*****************************************************
		//* ADD DEPOSIT DATA
		//*****************************************************
		caRequest.setDepositData(new DepositData() );
		caRequest.getDepositData().setDepositPending(new BigDecimal(0));
		caRequest.getDepositData().setDepositPaid(new BigDecimal(0));
		
		gCal.setTime(new Timestamp(dateFormat.parse("19-Jun-4444").getTime()));
		caRequest.getDepositData().setMostRecentDepositPaidDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gCal));
		if (caRequest.getDepositData().getMostRecentDepositPaidDate().getYear() == 4444) 
		{
			caRequest.getDepositData().setMostRecentDepositPaidDate(null);
		}
		// Initialize the following dates to null - they are not currently used in the rule so no need to add to RMA
		caRequest.getDepositData().setMostRecentDepositPendingDate(null);
		caRequest.getDepositData().setMostRecentDepositReleaseDate(null);

		//*****************************************************
		//* ADD COLLECTION DATA
		//*****************************************************
		caRequest.setCollectionData( new CollectionData() );
		caRequest.getCollectionData().setBdsScore("0");
		caRequest.getCollectionData().setCollectionInd(false);
		caRequest.getCollectionData().setNoOfAccountsInAgency(0);
		caRequest.getCollectionData().setBalanceOwingOnAccountsInAgency(new BigDecimal(0));
		caRequest.getCollectionData().setInvoluntaryCancelledAccounts(0);
		caRequest.getCollectionData().setBalanceOwingOnICA(new BigDecimal(0));
		
		return caRequest;
	}

	
}

*/