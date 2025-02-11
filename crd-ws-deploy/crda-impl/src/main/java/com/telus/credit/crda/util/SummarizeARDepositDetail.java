package com.telus.credit.crda.util;

//import com.fico.telus.blaze.creditCommon.DepositData;
import com.telus.framework.math.Money;
import com.telus.credit.domain.deposit.DepositItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SummarizeARDepositDetail {
	 /*public static DepositData summarizeARDepositDetail(List<DepositItem> accountReceivableDepositDetailList) {
		 // convert to summarizeARDepositDetail input
		 List<com.telus.credit.domain.deposit.DepositItem> aARDepositItemList = new ArrayList<com.telus.credit.domain.deposit.DepositItem>();
		 for (DepositItem inputARDepositItem : accountReceivableDepositDetailList) {
			 com.telus.credit.domain.deposit.DepositItem aARDepositItem = new com.telus.credit.domain.deposit.DepositItem();
			 
				aARDepositItem.setDepositID(inputARDepositItem.getDepositID());
				aARDepositItem.setRequestAmount(inputARDepositItem.getRequestAmount());
				
				aARDepositItem.setDueDate(inputARDepositItem.getDueDate());;
				aARDepositItem.setRequestDate(inputARDepositItem.getRequestDate());;	
				
				aARDepositItem.setPaidAmount(inputARDepositItem.getPaidAmount() );
				aARDepositItem.setPaidDate(inputARDepositItem.getPaidDate());
				
				aARDepositItem.setCancelDate(inputARDepositItem.getCancelDate());
				aARDepositItem.setCancelledAmount(inputARDepositItem.getCancelledAmount());		
				
				aARDepositItem.setReleasedAmount((inputARDepositItem.getReleasedAmount()) );
				aARDepositItem.setReleaseDate(inputARDepositItem.getReleaseDate());
				
			 

				//extra data 
				aARDepositItem.setAccountID(inputARDepositItem.getAccountID());
				aARDepositItem.setCancelReasonTxt(inputARDepositItem.getCancelReasonTxt());	
				aARDepositItem.setReleaseMethodCd(inputARDepositItem.getReleaseMethodCd());
				aARDepositItem.setReleaseMethodTxt(inputARDepositItem.getReleaseMethodTxt());
				aARDepositItem.setReleaseReasonCd(inputARDepositItem.getReleaseReasonCd());
				aARDepositItem.setReleaseReasonTxt(inputARDepositItem.getReleaseReasonTxt());
				aARDepositItem.setRequestReasonCd(inputARDepositItem.getRequestReasonCd());
				aARDepositItem.setRequestReasonTxt(inputARDepositItem.getRequestReasonTxt());
				
				
				
				aARDepositItemList.add(aARDepositItem);
			 
		 }
		 //call summarizeARDepositDetail
		 com.telus.credit.domain.deposit.DepositData summeriazidedDepositData = com.telus.credit.util.SummarizeARDepositItemList.summarizeARDepositDetail(aARDepositItemList);
		
		 //convert summarizeARDepositDetail output
		 DepositData aDepositItem= new DepositData();
			
			aDepositItem.setDepositPaid(summeriazidedDepositData.getDepositPaid() );
			aDepositItem.setMostRecentDepositPaidDate(summeriazidedDepositData.getMostRecentDepositPaidDate());
			
			aDepositItem.setDepositPending(summeriazidedDepositData.getDepositPending());
			aDepositItem.setMostRecentDepositPendingDate(summeriazidedDepositData.getMostRecentDepositPendingDate());		
			
			aDepositItem.setDepositReleased((summeriazidedDepositData.getDepositReleased()) );
			aDepositItem.setMostRecentDepositReleaseDate(summeriazidedDepositData.getMostRecentDepositReleaseDate());
		 
		 return aDepositItem;
	 }*/
	/* 
	 public static DepositData summarizeARDepositDetailOLD(List<DepositItem> accountReceivableDepositDetailList) {

        BigDecimal totalDepositPaid = new Money("0");
        Date mostRecentDepositPaidDate = null;

        BigDecimal totalPendingDepositAmt = new Money("0");
        Date mostRecentDepositPendingDate = null;
 
        BigDecimal totalReleasedDepositAmt = new Money("0");
        Date mostRecentDepositReleasedDate = null;

        for (DepositItem accountReceivableDepositDetail : accountReceivableDepositDetailList) {
            
        	//accountReceivableDepositDetail.getAccountID();

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

            //***************** getTotalDepositPendingAmount and most RecentDepositPendingDate *****************
            if (
            		//accountReceivableDepositDetail.getCancelDate() == null && 
                    accountReceivableDepositDetail.getReleaseDate() == null
                    && accountReceivableDepositDetail.getPaidDate() == null) {
                    BigDecimal pendingDepositAmount = new BigDecimal(accountReceivableDepositDetail.getRequestAmount());
                    pendingDepositAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
                    totalPendingDepositAmt = totalPendingDepositAmt.add(pendingDepositAmount);
                    //get the latest date
                    mostRecentDepositPendingDate = getLastestDate(mostRecentDepositPendingDate,accountReceivableDepositDetail.getDueDate());
                   
            }


            //*****************getTotalDepositReleasedAmount and mostRecentDepositReleaseDate*****************
            if (accountReceivableDepositDetail.getReleaseDate() != null) {
                //checking for ReleaseDate() != null in order to prevent an ReleaseDate not belonging to the same ReleaseAmt
                BigDecimal releasedDepositAmount = new BigDecimal(accountReceivableDepositDetail.getReleasedAmount());
                releasedDepositAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
                totalReleasedDepositAmt = totalReleasedDepositAmt.add(releasedDepositAmount);
                mostRecentDepositReleasedDate = getLastestDate(mostRecentDepositReleasedDate,accountReceivableDepositDetail.getReleaseDate());

            }


        }//end of loop

        //populate fico deposit data
        DepositData aDepositData = new DepositData();
        aDepositData.setDepositPaid(totalDepositPaid.setScale(2, BigDecimal.ROUND_HALF_UP));
        aDepositData.setMostRecentDepositPaidDate(CrdaUtility.asXMLGregorianCalendar(mostRecentDepositPaidDate));

        aDepositData.setDepositPending(totalPendingDepositAmt.setScale(2, BigDecimal.ROUND_HALF_UP));
        aDepositData.setMostRecentDepositPendingDate(CrdaUtility.asXMLGregorianCalendar(mostRecentDepositPendingDate));


        aDepositData.setDepositReleased(totalReleasedDepositAmt.setScale(2, BigDecimal.ROUND_HALF_UP));
        aDepositData.setMostRecentDepositReleaseDate(CrdaUtility.asXMLGregorianCalendar(mostRecentDepositReleasedDate));

        return aDepositData;

    }

*/	private static Date getLastestDate(
			Date mostRecentDate,Date thisDate) {
		if(mostRecentDate== null || mostRecentDate.before(thisDate) ){
			mostRecentDate = thisDate;
		}
		return mostRecentDate;
	}
}

