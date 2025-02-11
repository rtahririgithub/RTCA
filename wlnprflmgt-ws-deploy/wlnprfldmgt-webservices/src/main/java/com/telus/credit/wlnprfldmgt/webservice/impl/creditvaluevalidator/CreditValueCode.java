/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.wlnprfldmgt.webservice.impl.creditvaluevalidator;

import java.util.Set;


import com.telus.credit.domain.CreditIDCard;
import com.telus.credit.service.CreditProfileValidationException;
import com.telus.credit.service.CreditValueValidationException;


/**
 * 
 * <p><b>Description: </b> Base class for all CreditValueCode classes. 
 * It represents the current (existing) credit value code.
 * The validity of newly assigned "credit value code" is 
 * based on the existing credit value code. 
 * For example: if the 
 * current credit value code is "D", 
 * the new credit value can be either "R" or "N" only. 
 * If CSR assigns "E" credit value code - CreditValueValidationException
 * will be thrown.</p>
 * <p><b>Design Observations: </b></p>
 * 	<ul>
 * 		<li>The instances of this class are configured with Spring.</li>		
 * 	</ul>
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

public class CreditValueCode implements CreditValueValidator
{

    /**
     * Credit Value codes.
     */
    private Set m_acceptableCreditValueCodes;


    public CreditValueCode()
    {
        super();
    }



    /* (non-Javadoc)
     * @see com.telus.credit.service.creditvalue.CreditValueValidator#validate(java.lang.String)
     */
    public void validate(String modifiedCreditValue)
            throws CreditValueValidationException {
        if ( !getAcceptableCreditValueCodes().contains(
                modifiedCreditValue ) ) {
            throw new CreditValueValidationException();
        }
    }


    /* (non-Javadoc)
     * @see com.telus.credit.service.impl.creditvalue.CreditValueValidator#validate(java.lang.String, java.lang.String)
     */
    public void validate(String originalCreditValue, String modifiedCreditValue)
            throws CreditValueValidationException
    {
        throw new CreditValueValidationException(
                "Default implemetation for CreditValueCode subclasses" );
    }
    
    /* (non-Javadoc)
     * @see com.telus.credit.service.impl.creditvalue.CreditValueValidator#validate(java.lang.String, java.lang.String)
     */
    public void validate(String originalCreditValue, String modifiedCreditValue, CreditIDCard [] cards)
            throws CreditProfileValidationException, CreditValueValidationException
    {
        throw new CreditValueValidationException(
                "Default implemetation for CreditValueCode subclasses" );
    }


    /**
     * <p><b>Description</b> Sets Credit Value codes.</p>
     * <p><b>High Level Design: </b></p>
     * <ul>
     * 		<li>To be invoked by Spring framework</li>
     * </ul>
     * @param set contains a set of acceptable credit value codes.
     */
    public void setAcceptableCreditValueCodes(Set set)
    {
        m_acceptableCreditValueCodes = set;
    }


    /**
     * <p><b>Description</b> Gets Credit Value codes.</p>
     * @return set of acceptable credit value codes.
     */
    protected Set getAcceptableCreditValueCodes()
    {
        return m_acceptableCreditValueCodes;
    }


}