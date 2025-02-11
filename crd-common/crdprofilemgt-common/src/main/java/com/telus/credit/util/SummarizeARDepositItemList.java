package com.telus.credit.util;

 
import com.telus.credit.domain.deposit.DepositData;
import com.telus.framework.math.Money;
import com.telus.credit.domain.deposit.DepositItem;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SummarizeARDepositItemList {
	
	public static final Log log = LogFactory.getLog(SummarizeARDepositItemList.class);
    public static DepositData summarizeARDepositDetail(List<DepositItem> accountReceivableDepositDetailList) {

    	
        BigDecimal totalDepositPaid = new Money("0");
        Date mostRecentDepositPaidDate = null;

        BigDecimal totalPendingDepositAmt = new Money("0");
        Date mostRecentDepositPendingDate = null;

        BigDecimal totalReleasedDepositAmt = new Money("0");
        Date mostRecentDepositReleasedDate = null;

        for (DepositItem accountReceivableDepositDetail : accountReceivableDepositDetailList) {
        	validateDepositItem(accountReceivableDepositDetail);
        	
	            //****************** getTotalDepositPaidAmount and mostRecentDepositPaidDate  ****************
	            if (accountReceivableDepositDetail.getCancelDate() == null
	                    && accountReceivableDepositDetail.getReleaseDate() == null
	                    && accountReceivableDepositDetail.getPaidDate() != null
	                    ) {
	                    //PaidAmt and RequestAmt are supposed to always be the same. Using PaidAmt
	                    BigDecimal paidDepositAmount = new BigDecimal(accountReceivableDepositDetail.getPaidAmount());
	                    paidDepositAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	                    totalDepositPaid = totalDepositPaid.add(paidDepositAmount);
	                    //get the latest date
	                    mostRecentDepositPaidDate = getLastestDate(mostRecentDepositPaidDate,accountReceivableDepositDetail.getPaidDate());
	                    
	            }
	            else {
		            //***************** getTotalDepositPendingAmount and most RecentDepositPendingDate *****************
		            if (
		            		accountReceivableDepositDetail.getCancelDate() == null && 
		                    accountReceivableDepositDetail.getReleaseDate() == null
		                    && accountReceivableDepositDetail.getPaidDate() == null) {
		                    BigDecimal pendingDepositAmount = new BigDecimal(accountReceivableDepositDetail.getRequestAmount());
		                    pendingDepositAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
		                    totalPendingDepositAmt = totalPendingDepositAmt.add(pendingDepositAmount);
		                    //get the latest date
		                    mostRecentDepositPendingDate = getLastestDate(mostRecentDepositPendingDate,accountReceivableDepositDetail.getDueDate());
		            }
		            else{
			            //*****************getTotalDepositReleasedAmount and mostRecentDepositReleaseDate*****************
			           //CancelDate=null, ReleaseDate<>null, PaidDate<>null"
			            if (
			            		accountReceivableDepositDetail.getCancelDate()  == null && 
			                    accountReceivableDepositDetail.getReleaseDate() != null
			                    && accountReceivableDepositDetail.getPaidDate() != null) {
			                BigDecimal releasedDepositAmount = new BigDecimal(accountReceivableDepositDetail.getReleasedAmount());
			                releasedDepositAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
			                totalReleasedDepositAmt = totalReleasedDepositAmt.add(releasedDepositAmount);
			                mostRecentDepositReleasedDate = getLastestDate(mostRecentDepositReleasedDate,accountReceivableDepositDetail.getReleaseDate());
			
			            }
		            }
	
	            }
	    }//end of loop

        //populate fico deposit data
        DepositData aDepositData = new DepositData();
        aDepositData.setDepositPaid(totalDepositPaid.setScale(2, BigDecimal.ROUND_HALF_UP));
        aDepositData.setMostRecentDepositPaidDate(asXMLGregorianCalendar(mostRecentDepositPaidDate));

        aDepositData.setDepositPending(totalPendingDepositAmt.setScale(2, BigDecimal.ROUND_HALF_UP));
        aDepositData.setMostRecentDepositPendingDate(asXMLGregorianCalendar(mostRecentDepositPendingDate));


        aDepositData.setDepositReleased(totalReleasedDepositAmt.setScale(2, BigDecimal.ROUND_HALF_UP));
        aDepositData.setMostRecentDepositReleaseDate(asXMLGregorianCalendar(mostRecentDepositReleasedDate));

        return aDepositData;

    }

	private static void  validateDepositItem(DepositItem accountReceivableDepositDetail) {
		//is invalid pending deposit item
		if( 
					accountReceivableDepositDetail.getCancelDate()  != null &&
					accountReceivableDepositDetail.getPaidDate()    == null &&
					accountReceivableDepositDetail.getReleaseDate() == null
			){
			System.out.println("invalid pending deposit item. \n " + toString(accountReceivableDepositDetail));
			log.error("invalid pending deposit item. \n " + toString(accountReceivableDepositDetail));
		}
		//is invalid released deposit item
		if( 
					accountReceivableDepositDetail.getCancelDate()  != null &&
					accountReceivableDepositDetail.getPaidDate()    != null &&
					accountReceivableDepositDetail.getReleaseDate() != null
			){
			//log
			System.out.println("invalid released deposit item. \n " + toString(accountReceivableDepositDetail));
			log.error("invalid released deposit item. \n " + toString(accountReceivableDepositDetail));
		}

	}

	private static Date getLastestDate(
			Date mostRecentDate,Date thisDate) {
		if(mostRecentDate== null || mostRecentDate.before(thisDate) ){
			mostRecentDate = thisDate;
		}
		return mostRecentDate;
	}
	
	private static String toString(DepositItem accountReceivableDepositDetail) {
		return 
		"DepositItem details :\n"  +
		"-----------------------\n"  +
		"DepositID = " +accountReceivableDepositDetail.getDepositID()  + "\n" +
		"AccountID = " +accountReceivableDepositDetail.getAccountID()  			+ "\n" +
		
		"DueDate = " +accountReceivableDepositDetail.getDueDate()  + "\n" + 
		"RequestAmount = " +accountReceivableDepositDetail.getRequestAmount()  + "\n" + 
		"RequestDate = " +accountReceivableDepositDetail.getRequestDate()  + "\n" + 
		
		"CancelDate = " + accountReceivableDepositDetail.getCancelDate() + "\n" +
		"CancelledAmount = " +accountReceivableDepositDetail.getCancelledAmount()  + "\n" +
		
		"PaidAmount = " +accountReceivableDepositDetail.getPaidAmount()  + "\n" +
		"PaidDate = " +accountReceivableDepositDetail.getPaidDate()  + "\n" +
		
		"ReleasedAmount = " +accountReceivableDepositDetail.getReleasedAmount()  +  
		"ReleaseDate = " +accountReceivableDepositDetail.getReleaseDate()  + "\n"  +  

		"CancelReasonTxt = " +accountReceivableDepositDetail.getCancelReasonTxt()  + "\n" +
		"ReleaseMethodCd = " +accountReceivableDepositDetail.getReleaseMethodCd()  + "\n" +
		"ReleaseMethodTxt = " +accountReceivableDepositDetail.getReleaseMethodTxt()  + "\n"+ 
		"ReleaseReasonCd = " +accountReceivableDepositDetail.getReleaseReasonCd()  + "\n" +
		"ReleaseReasonTxt = " +accountReceivableDepositDetail.getReleaseReasonTxt()  + "\n"+ 
		"RequestReasonCd = " +accountReceivableDepositDetail.getRequestReasonCd()  + "\n" +
		"RequestReasonTxt = " +accountReceivableDepositDetail.getRequestReasonTxt()  + "\n" 
		;

		
		
	}	
	
	
	
    private static DatatypeFactory df = null;

    static {
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException dce) {
            throw new IllegalStateException(
                    "Exception while obtaining DatatypeFactory instance", dce);
        }

    }
    private  static XMLGregorianCalendar asXMLGregorianCalendar(java.util.Date date) {
        if (date == null) {
            return null;
        } else {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(date.getTime());
            return df.newXMLGregorianCalendar(gc);
        }
    }
}

