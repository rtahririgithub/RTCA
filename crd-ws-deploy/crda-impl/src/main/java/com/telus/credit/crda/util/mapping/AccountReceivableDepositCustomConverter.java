package com.telus.credit.crda.util.mapping;


//import com.fico.telus.blaze.creditCommon.DepositData;
import com.telus.credit.crda.util.SummarizeARDepositDetail;
 
import com.telus.credit.domain.deposit.DepositItem;
import com.telus.credit.domain.deposit.DepositItemList;
import org.dozer.CustomConverter;
import org.dozer.MappingException;

import java.util.List;

public class AccountReceivableDepositCustomConverter implements CustomConverter {

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        if (source == null) {
            return null;
        }
        /*DepositData dest = null;
        if (source instanceof DepositItemList) {
            if (destination == null) {
                dest = new DepositData();
            } else {
                dest = (DepositData) destination;
            }
            List<DepositItem> accountReceivableDepositDetailList = ((DepositItemList) source).getDepositItem();
            dest = SummarizeARDepositDetail.summarizeARDepositDetail(accountReceivableDepositDetailList);
            return dest;
        } else if (source instanceof DepositData) {
            //used only for Junit testing
            //do nothing
            return new DepositItemList();
        } else {
            throw new MappingException("Converter AccountReceivableDepositCustomConverter "
                    + "used incorrectly. Arguments passed in were:"
                    + destination + " and " + source);
        }*/
        return null;

    }

}
