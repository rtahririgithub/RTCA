package com.telus.credit.crda.util.mapping;

import com.telus.credit.crda.util.ReferencePDSUtils;
import org.dozer.CustomConverter;
import org.dozer.MappingException;

import com.telus.credit.domain.deposit.DepositItemList;;

public class CanadianProvinceCustomConverter implements CustomConverter {

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        if (source == null) {
            return null; 
        }
        String dest = null;
        if (source instanceof String) {
            String aCreditIdentification = ((String) source);

            if (aCreditIdentification != null
                    && (!ReferencePDSUtils.instanceOf().isValidCode(aCreditIdentification, "PROVINCE"))
                //&& aCreditIdentification.equalsIgnoreCase("12c3")
                    ) {
                dest = null;
            } else {
                dest = aCreditIdentification;
            }

            return dest;
        } else if (source instanceof String) {
            return new DepositItemList();
        } else {
            throw new MappingException("Converter CanadianProvinceCustomConverter "
                    + "used incorrectly. Arguments passed in were:"
                    + destination + " and " + source);
        }

    }

}
