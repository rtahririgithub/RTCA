/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.service;

import java.io.Serializable;

import com.telus.framework.exception.BaseException;


/**
 * 
 * <p><b>Description: </b> This exception is thrown if validation 
 * of the Credit Profile domain object fails.</p>
 * <p><b>Design Observations: </b></p>
 * 	<ul>
 * 		<li>This is checked exception.</li>		
 * 	</ul>
 *
 * <p><br><b>Issues: </b></p>
 * 	<ul>
 * 		<li>N/A</li>		
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

public class CreditProfileValidationException extends BaseException
        implements
            Serializable
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 8136166960126333137L;


	/**
     * Constructs a CreditProfileValidationException with no detail message, no error code and
     * no cause exception.
     */
    public CreditProfileValidationException()
    {
        super();
    }


    /**
     * Constructs a CreditProfileValidationException with a detail message 
     * @param message the detail message
     */
    public CreditProfileValidationException(String message)
    {
        super( message );
    }


    /**
     * Constructs a CreditProfileValidationException with a cause exception.
     * @param cause a Thowable object representing the casue exception.
     */
    public CreditProfileValidationException(Throwable cause)
    {
        super( cause );
    }


    /**
     * Constructs a CreditProfileValidationException with a detail message and 
     * errorCode.
     * @param message the detail message.
     * @param errorCode  the error code.
     */
    public CreditProfileValidationException(String message, String errorCode)
    {
        super( message, errorCode );
    }


    /**
     * Constructs a CreditProfileValidationException with a detail message and 
     * cause exception.
     * @param message the detail message.
     * @param cause a Thowable object representing the casue exception.
     */
    public CreditProfileValidationException(String message, Throwable cause)
    {
        super( message, cause );
    }


    /**
     * Constructs a CreditProfileValidationException with a detail message, 
     * an error code and a cause exception. 
     * @param message the detail message.
     * @param errorCode  the error code.
     * @param cause a Thowable object representing the casue exception.
     */
    public CreditProfileValidationException(String message, String errorCode,
            Throwable cause)
    {
        super( message, errorCode, cause );
    }
}