/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.wlnprfldmgt.webservice.impl.creditvaluevalidator;

import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.telus.credit.domain.CreditIDCard;
import com.telus.credit.domain.CreditValue;
import com.telus.credit.service.CreditProfileMgtSvcExceptionCodes;
import com.telus.credit.service.CreditProfileValidationException;
import com.telus.credit.service.CreditValueValidationException;


/**
 * 
 * <p><b>Description: </b> Implementation of the CreditValueValidator interface.
 * Incapsulates a map of Credit Value codes to Credit Value code wrapper classes.
 * Each CreditValue code wrapper object holds a set of acceptable credit value codes
 * that can replace original credit value code during <code>com.telus.credit.service.CreditProfileMgtSvc#updateCreditProfile(CreditProfile)</code> operation.
 * Example: if the original Credit Value code is "D" and modified Credit Value code is "V"
 *  - validate method on DepositCreditValueCode object will be invoked. If "V" is not in the
 * set of acceptable Credit Value codes for DepositCreditValueCode - CreditValueValidationException
 * will be thrown.
 * 
 *  
 * </p>
 * <p><b>Design Observations: </b></p>
 * 	<ul>
 * 		<li>Map of Credit Value codes is injected by Spring.</li>	
 * 		<li>
 * 			Utilizes Singlton design pattern (GoF). This class in not a pure singleton because it has
 * 			public constructor. We need public constructor here because we want this class to be 
 * 			instantiable and configurable by Spring framework.
 * 		</li>	
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

public class CreditValueValidatorImpl implements CreditValueValidator
{

    /**
     * Map of Credit Value codes to Credit Value code wrappers.
     */
    private Map m_creditValueCodes;
    /**
     * Single instance of this class.
     */
    private static CreditValueValidatorImpl m_instance;



    /**
     * Default constructor. 
     */

    public CreditValueValidatorImpl()
    {
        super();
    }


    /**
     * <p><b>Description</b> Returns single instance of this class.</p>
     * <p><b>High Level Design: </b></p>
     * <ul>
     * 		<li>N/A</li>
     * </ul>
     * @return object of type CreditValueValidatorImpl.
     */
    public static CreditValueValidatorImpl getInstance()
    {
        if ( m_instance == null ) {
            m_instance = new CreditValueValidatorImpl();
        }
        return m_instance;
    }


    /* (non-Javadoc)
     * @see com.telus.credit.service.creditvalue.CreditValueValidator#validate(java.lang.String, 
     * java.lang.String, com.telus.credit.domain.CreditIDCard [])
     */
    public void validate(String originalCode, String modifiedCode,
            CreditIDCard[] iDcards)
            throws CreditProfileValidationException,
            CreditValueValidationException
    {
        if ( CreditValue.NO_CREDIT_INFO_CREDIT_VALUE_KEY
                .equalsIgnoreCase( modifiedCode ) ) {
            if ( !ArrayUtils.isEmpty( iDcards ) ) {
                throw new CreditProfileValidationException(
                        "Credit Value can only change to 'N' (and must change to 'N') if ID cards are removed from Credit Profile",
                        CreditProfileMgtSvcExceptionCodes.CREDIT_VALUE_N_ERROR_1 );
            }
        }

        if ( CreditValue.NO_CREDIT_INFO_CREDIT_VALUE_KEY
                .equalsIgnoreCase( originalCode )
                && !CreditValue.NO_CREDIT_INFO_CREDIT_VALUE_KEY
                        .equalsIgnoreCase( modifiedCode ) ) {
            if ( ArrayUtils.isEmpty( iDcards ) ) {
                throw new CreditProfileValidationException(
                        "Credit Value can only change from 'N' (and must change from 'N') if CreditProfile has ID cards",
                        CreditProfileMgtSvcExceptionCodes.CREDIT_VALUE_N_ERROR_2 );
            }
        }


        validate( originalCode, modifiedCode );
    }


    /* (non-Javadoc)
     * @see com.telus.credit.service.creditvalue.CreditValueValidator#validate(java.lang.String, 
     * java.lang.String)
     */
    public void validate(String originalCode, String modifiedCode)
            throws CreditValueValidationException
    {
        CreditValueCode originalCreditValueCode = (CreditValueCode) m_creditValueCodes
                .get( originalCode );
        originalCreditValueCode.validate( modifiedCode );
    }


    /**
     * <p><b>Description</b> Sets a map of Credit Value Codes to Credit Value code wrapper classes.</p>
     * <p><b>High Level Design: </b></p>
     * <ul>
     * 		<li>This method is invoked by Spring framework.</li>
     * </ul>
     * @param creditValueCodes map 
     */
    public void setCreditValueCodes(Map creditValueCodes)
    {
        m_creditValueCodes = creditValueCodes;
    }


    /**
     * <p><b>Description</b> Gets Credit Value codes map.</p>
     * <p><b>High Level Design: </b></p>
     * <ul>
     * 		<li>N/A</li>
     * </ul>
     * @return  map 
     */
    public Map getCreditValueCodes()
    {
        return m_creditValueCodes;
    }


    /* (non-Javadoc)
     * @see com.telus.credit.service.impl.creditvalue.CreditValueValidator#validate(java.lang.String)
     */
    public void validate(String modifiedCreditValue)
            throws CreditValueValidationException
    {
        throw new CreditValueValidationException(
                new UnsupportedOperationException( "Unimplemented method" ) );
    }

}