package com.telus.credit.crda.util;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import com.fico.telus.blaze.creditCommon.DepositData;
import com.telus.credit.domain.crda.ExistingCustomerCreditAssessmentRequest;

import com.telus.credit.domain.crda.PerformCreditAssessment;
import com.telus.credit.framework.test.TelusConfig;
import com.telus.credit.framework.test.TelusJUnitClassRunner;

import com.telus.credit.domain.deposit.DepositItemList;
import com.telus.credit.domain.deposit.DepositItem;;

@RunWith(TelusJUnitClassRunner.class)
@TelusConfig(configAppCtxFile="crda-test-appCtx.properties")
@ContextConfiguration("classpath:test/test-telus-crd-crda-impl-spring.xml")
public class SummarizeARDepositDetailTest {
	ExistingCustomerCreditAssessmentRequest aCreditAssessmentRequest_Src=null;
	@Before
    public void setFicoInputOutputTestData() throws Throwable {
     String filename=TestFiles.FULL_ASSESSMENT_MANUAL_ASSESSMENT_WITHDEPOSIT;
	 PerformCreditAssessment performCreditAssessment = XMLUtility.createPerformCreditAssessmentRequest(filename);
	  aCreditAssessmentRequest_Src=(ExistingCustomerCreditAssessmentRequest)performCreditAssessment.getCreditAssessmentRequest();
    }
	@Test
	public void testSummarizeARDepositDetail() throws Exception {
		//depositItem1 has only requestAmount=100 and ns11:requestDate=2013-12-02
		//depositItem2 has  
			//requestAmount=100 and ns11:requestDate=2013-12-02 and 
			//paidAmount=100 and  paidDate=null
			//cancelledAmount = 100.00 , cancelDate 2013-12-02

		//SummarizeARDepositDetail
		/*		DepositPaid = 0.00
				MostRecentDepositPaidDate = null
				DepositPending = 100.00
				MostRecentDepositPendingDate = Tue Nov 07 16:00:00 PST 2113
				DepositReleased = 0.00
				MostRecentDepositReleaseDate = null*/

		DepositItemList aAccountReceivableDepositDataList = aCreditAssessmentRequest_Src.getDepositItemList();
		List<DepositItem>  accountReceivableDepositDetailList=  aAccountReceivableDepositDataList.getDepositItem();	    	
		DepositData aDepositData=SummarizeARDepositDetail.summarizeARDepositDetail(accountReceivableDepositDetailList);
		
		System.out.println("SummarizeARDepositDetail result :" );
		System.out.println("*******************************************" );
		System.out.println("DepositPaid = " + aDepositData.getDepositPaid() );
		System.out.println("MostRecentDepositPaidDate = " + CrdaUtility.asDate(aDepositData.getMostRecentDepositPaidDate()) );
		
		System.out.println("DepositPending = " + aDepositData.getDepositPending() );
		System.out.println("MostRecentDepositPendingDate = " + CrdaUtility.asDate(aDepositData.getMostRecentDepositPendingDate()) ); 
		
		System.out.println("DepositReleased = " + aDepositData.getDepositReleased() );
		System.out.println("MostRecentDepositReleaseDate = " + CrdaUtility.asDate(aDepositData.getMostRecentDepositReleaseDate()) );
		
		
		

	}

}
