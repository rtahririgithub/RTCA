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
 * <p><b>Description: </b> Indicates that the primary credit profile already exist in the Credit Management system</p>
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

public class DuplicateCreditProfileException extends BaseException
        implements
            Serializable
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1842530260310313115L;


	/**
     * Constructs a DuplicateCreditProfileException with no detail message, no error code and
     * no cause exception.
     */
    public DuplicateCreditProfileException()
    {
        super();
    }


    /**
     * Constructs a DuplicateCreditProfileException with a detail message 
     * @param message the detail message
     */
    public DuplicateCreditProfileException(String message)
    {
        super( message );
    }


    /**
     * Constructs a DuplicateCreditProfileException with a cause exception.
     * @param cause a Thowable object representing the casue exception.
     */
    public DuplicateCreditProfileException(Throwable cause)
    {
        super( cause );
    }


    /**
     * Constructs a DuplicateCreditProfileException with a detail message and 
     * errorCode.
     * @param message the detail message.
     * @param errorCode  the error code.
     */
    public DuplicateCreditProfileException(String message, String errorCode)
    {
        super( message, errorCode );
    }


    /**
     * Constructs a DuplicateCreditProfileException with a detail message and 
     * cause exception.
     * @param message the detail message.
     * @param cause a Thowable object representing the casue exception.
     */
    public DuplicateCreditProfileException(String message, Throwable cause)
    {
        super( message, cause );
    }


    /**
     * Constructs a DuplicateCreditProfileException with a detail message, 
     * an error code and a cause exception. 
     * @param message the detail message.
     * @param errorCode  the error code.
     * @param cause a Thowable object representing the casue exception.
     */
    public DuplicateCreditProfileException(String message, String errorCode,
            Throwable cause)
    {
        super( message, errorCode, cause );
    }
}