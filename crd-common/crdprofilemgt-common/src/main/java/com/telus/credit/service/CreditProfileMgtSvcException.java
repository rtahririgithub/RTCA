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

import com.telus.framework.exception.BaseRuntimeException;


/**
 * 
 * <p><b>Description: </b> CreditProfileMgtRuntimeException is the runtime exception 
 * class associated with the Credit Profile Management service.
 * Any operations within this service can potentially throw this exception. </p>
 * <p><b>Design Observations: </b></p>
 * 	<ul>
 * 		<li>This is unchecked (runtime) exception.</li>
 * 		<li>This exception could contain nested exceptions.</li>		
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

public class CreditProfileMgtSvcException extends BaseRuntimeException
        implements
            Serializable
{

	




    /**
	 * 
	 */
	private static final long serialVersionUID = -6530460080557081729L;


	/**
     * Constructs a CreditProfileMgtSvcException with no detail message, no error code and
     * no cause exception.
     */
    public CreditProfileMgtSvcException()
    {
        super();
    }


    /**
     * Constructs a CreditProfileMgtSvcException with a detail message 
     * @param message the detail message
     */
    public CreditProfileMgtSvcException(String message)
    {
        super( message );
    }


    /**
     * Constructs a CreditProfileMgtSvcException with a cause exception.
     * @param cause a Thowable object representing the casue exception.
     */
    public CreditProfileMgtSvcException(Throwable cause)
    {
        super( cause );
    }


    /**
     * Constructs a CreditProfileMgtSvcException with a detail message and 
     * errorCode.
     * @param message the detail message.
     * @param errorCode  the error code.
     */
    public CreditProfileMgtSvcException(String message, String errorCode)
    {
        super( message, errorCode );
    }


    /**
     * Constructs a CreditProfileMgtSvcException with a detail message and 
     * cause exception.
     * @param message the detail message.
     * @param cause a Thowable object representing the casue exception.
     */
    public CreditProfileMgtSvcException(String message, Throwable cause)
    {
        super( message, cause );
    }


    /**
     * Constructs a CreditProfileMgtSvcException with a detail message, 
     * an error code and a cause exception. 
     * @param message the detail message.
     * @param errorCode  the error code.
     * @param cause a Thowable object representing the casue exception.
     */
    public CreditProfileMgtSvcException(String message, String errorCode,
            Throwable cause)
    {
        super( message, errorCode, cause );
    }
}