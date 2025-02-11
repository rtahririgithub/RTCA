/*package com.telus.credit.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import com.telus.credit.domain.deposit.DepositData;
import com.telus.credit.domain.deposit.DepositItem;
 

@ContextConfiguration("classpath:test-spring.xml")
public class SummarizeARDepositDetailTest {

	public SummarizeARDepositDetailTest() {
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	protected void setUp() throws Exception {
		try {
			System.getProperties().setProperty("oracle.jdbc.J2EE13Compliant",
					"true");
			EnvUtil.setupTestEnv();
			ClassPathXmlApplicationContext m_ApplicationContext = new ClassPathXmlApplicationContext(
					EnvUtil.resourcesFolder);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void testSummarizeARDepositDetail() throws Throwable {

		DepositData aDepositData=SummarizeARDepositItemList.summarizeARDepositDetail(createTestDepositItems());
		printResult(aDepositData);

	}
	private void printResult(DepositData aDepositData) {
		
		
		
		System.out.println("DepositPaid = " + aDepositData.getDepositPaid() );
		System.out.println("MostRecentDepositPaidDate = " +  asDate(aDepositData.getMostRecentDepositPaidDate()) );
		
		System.out.println("DepositPending = " + aDepositData.getDepositPending() );
		System.out.println("MostRecentDepositPendingDate = " +  asDate(aDepositData.getMostRecentDepositPendingDate()) ); 
		
		System.out.println("DepositReleased = " + aDepositData.getDepositReleased() );
		System.out.println("MostRecentDepositReleaseDate = " +  asDate(aDepositData.getMostRecentDepositReleaseDate()) );
	}
	private List<DepositItem> createTestDepositItems() {
		
		
		List<DepositItem>  accountReceivableDepositDetailList=  new ArrayList<DepositItem>();
				
		//"CancelDate=null or any,  ReleaseDate=null  , PaidDate<>null"

		DepositItem aPaidDepositItem = new DepositItem();
			aPaidDepositItem.setDepositID(1);
			aPaidDepositItem.setRequestAmount(100);
			aPaidDepositItem.setDueDate(new Date());
			aPaidDepositItem.setRequestDate(new Date());	
			
			
			
			aPaidDepositItem.setPaidAmount(100);
			aPaidDepositItem.setPaidDate(new Date());
			
			//aPaidDepositItem.setCancelDate(new Date());
			//aPaidDepositItem.setCancelledAmount(100);		
			
			//aPaidDepositItem.setReleasedAmount(100);
			//aPaidDepositItem.setReleaseDate(new Date());
//******************			
			DepositItem anInvalid_PaidDepositItemWith_Cancel_Date = new DepositItem();
			anInvalid_PaidDepositItemWith_Cancel_Date.setDepositID(2);
			anInvalid_PaidDepositItemWith_Cancel_Date.setRequestAmount(200);
			anInvalid_PaidDepositItemWith_Cancel_Date.setDueDate(new Date());
			anInvalid_PaidDepositItemWith_Cancel_Date.setRequestDate(new Date());	
			
			anInvalid_PaidDepositItemWith_Cancel_Date.setPaidAmount(200);
			anInvalid_PaidDepositItemWith_Cancel_Date.setPaidDate(new Date());
			
			anInvalid_PaidDepositItemWith_Cancel_Date.setCancelDate(new Date());
			anInvalid_PaidDepositItemWith_Cancel_Date.setCancelledAmount(200);		
			
			anInvalid_PaidDepositItemWith_Cancel_Date.setReleasedAmount(100);
			anInvalid_PaidDepositItemWith_Cancel_Date.setReleaseDate(new Date());			
			
			

//*******************************			
			//"CancelDate=null , ReleaseDate=null , PaidDate=null"

			DepositItem aPendingDepositItem = new DepositItem();
			aPendingDepositItem.setDepositID(3);
			aPendingDepositItem.setRequestAmount(300);
			aPendingDepositItem.setDueDate(new Date());
			aPendingDepositItem.setRequestDate(new Date());		
			
			
			aPendingDepositItem.setPaidAmount(300);
			aPendingDepositItem.setPaidDate(new Date());
			
			aPendingDepositItem.setCancelDate(new Date());
			aPendingDepositItem.setCancelledAmount(300);		
			
			aPendingDepositItem.setReleasedAmount(300);
			aPendingDepositItem.setReleaseDate(new Date());

			
//******************			
			DepositItem anInvalid_PendingDepositItemWith_Cancel_Date = new DepositItem();
			anInvalid_PendingDepositItemWith_Cancel_Date.setDepositID(4);
			anInvalid_PendingDepositItemWith_Cancel_Date.setRequestAmount(400);
			anInvalid_PendingDepositItemWith_Cancel_Date.setDueDate(new Date());
			anInvalid_PendingDepositItemWith_Cancel_Date.setRequestDate(new Date());	
			
			anInvalid_PendingDepositItemWith_Cancel_Date.setPaidAmount(0);
			anInvalid_PendingDepositItemWith_Cancel_Date.setPaidDate(null);
			
			anInvalid_PendingDepositItemWith_Cancel_Date.setCancelDate(new Date());
			anInvalid_PendingDepositItemWith_Cancel_Date.setCancelledAmount(400);		
			
			anInvalid_PendingDepositItemWith_Cancel_Date.setReleasedAmount(100);
			anInvalid_PendingDepositItemWith_Cancel_Date.setReleaseDate(new Date());			

//*******************************			
			//"CancelDate=null , ReleaseDate<>null, PaidDate<>null"
			
			DepositItem aReleasedDepositItem = new DepositItem();
			aReleasedDepositItem.setDepositID(5);
			aReleasedDepositItem.setRequestAmount(500);
			aReleasedDepositItem.setDueDate(new Date());
			aReleasedDepositItem.setRequestDate(new Date());	
			
			
			aReleasedDepositItem.setPaidAmount(100);
			aReleasedDepositItem.setPaidDate(new Date());
			
			aReleasedDepositItem.setCancelDate(new Date());
			aReleasedDepositItem.setCancelledAmount(500);		
			
			aReleasedDepositItem.setReleasedAmount(500);
			aReleasedDepositItem.setReleaseDate(new Date());	
			
			
//******************			
			DepositItem anInvalid_ReleasedDepositItemWith_Cancel_Date = new DepositItem();
			anInvalid_ReleasedDepositItemWith_Cancel_Date.setDepositID(6);
			anInvalid_ReleasedDepositItemWith_Cancel_Date.setRequestAmount(600);
			anInvalid_ReleasedDepositItemWith_Cancel_Date.setDueDate(new Date());
			anInvalid_ReleasedDepositItemWith_Cancel_Date.setRequestDate(new Date());	
			
			anInvalid_ReleasedDepositItemWith_Cancel_Date.setPaidAmount(600);
			anInvalid_ReleasedDepositItemWith_Cancel_Date.setPaidDate(new Date());
			
			anInvalid_ReleasedDepositItemWith_Cancel_Date.setCancelDate(new Date());
			anInvalid_ReleasedDepositItemWith_Cancel_Date.setCancelledAmount(600);		
			
			anInvalid_ReleasedDepositItemWith_Cancel_Date.setReleasedAmount(100);
			anInvalid_ReleasedDepositItemWith_Cancel_Date.setReleaseDate(new Date());			
			
			
			
			//******************			
			accountReceivableDepositDetailList.add(aPaidDepositItem);
			accountReceivableDepositDetailList.add(anInvalid_PaidDepositItemWith_Cancel_Date);
			
			accountReceivableDepositDetailList.add(aPendingDepositItem);	
			accountReceivableDepositDetailList.add(anInvalid_PendingDepositItemWith_Cancel_Date);	
			
			accountReceivableDepositDetailList.add(aReleasedDepositItem);
			accountReceivableDepositDetailList.add(anInvalid_ReleasedDepositItemWith_Cancel_Date);	
		return accountReceivableDepositDetailList;
	}
    public static java.util.Date asDate(XMLGregorianCalendar xgc) {
        if (xgc == null) {
            return null;
        } else {
            return xgc.toGregorianCalendar().getTime();
        }
    }
    }
*/