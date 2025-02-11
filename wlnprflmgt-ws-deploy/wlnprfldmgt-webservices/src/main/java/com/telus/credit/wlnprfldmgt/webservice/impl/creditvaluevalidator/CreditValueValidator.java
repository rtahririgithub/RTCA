/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */
package com.telus.credit.wlnprfldmgt.webservice.impl.creditvaluevalidator;


import com.telus.credit.domain.CreditIDCard;
import com.telus.credit.service.CreditProfileValidationException;
import com.telus.credit.service.CreditValueValidationException;


/**
 * 
 * <p><b>Description: </b> Interface for credit value validations.
 * Credit Value is just another name for "Credit Value code". 
 * Example:
 * "D" - credit value code. "Deposit" - credit value for this customer.
 * D == Deposit.
 * </p>
 *
 * <p><br><b>Revision History: </b></p>
 * <table border="1" width="100%">
 * 	<tr>
 * 		<th width="15%">Date</th>
 * 		<th width="15%">Revised By</th>
 * 		<th width="55%">Description</th>
 * 		<th width="15%">Reviewed By</th>
 * 	</tr>
 * 	<tr>
 * 		<td width="15%">&nbsp;</td>
 * 		<td width="15%">&nbsp;</td>
 * 		<td width="55%">&nbsp;</td>
 * 		<td width="15%">&nbsp;</td>
 * 	</tr>
 * </table>
 * @author Roman Mikhailov
 * 
 */

public interface CreditValueValidator
{


    /**
     * <p><b>Description: </b> Validates credit value code supplied by the caller,
     * by examining both original and modified Credit Value codes.</p>
     * @param originalCreditValueCode 
     * @param modifiedCreditValueCode
     * @throws CreditValueValidationException if validation fails.
     */
    public void validate(String originalCreditValueCode,
            String modifiedCreditValueCode)
            throws CreditValueValidationException;
    
    /**
     * <p><b>Description: </b> Validates credit value code supplied by the caller,
     * by examining both original and modified Credit Value codes and modified Credit Profile's id card</p>
     * @param originalCreditValueCode 
     * @param modifiedCreditValueCode
     * @param cards ID cards that belong to "modified" Credit Profile  
     * @throws CreditValueValidationException if validation fails.
     * @throws CreditProfileValidationException if :
     * 	 - Credit Profile has id cards and modified Credit Value is "N"
     *   - Credit Profile has no id cards and modified Credit Value is not "N"
     */
    public void validate(String originalCreditValueCode,
            String modifiedCreditValueCode, CreditIDCard [] cards)
            throws CreditProfileValidationException, CreditValueValidationException;
    
    
    
    /**
     * <p><b>Description</b> Validates credit value code supplied by the caller.</p>
     * @param modifiedCreditValueCode
     * @throws CreditValueValidationException is the code is invalid.
     */
    public void validate(String modifiedCreditValueCode)
    		throws CreditValueValidationException;
}