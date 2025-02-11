/*
 * Copyright (c) 2005 TELUS Communications Inc., All Rights Reserved.
 * 
 * This document contains proprietary information that shall be distributed or
 * routed only within TELUS, and its authorized clients, except with written
 * permission of TELUS.
 *  
 * $Id$
 */
package com.telus.credit.batch.exception;

import java.io.Serializable;

import com.telus.framework.exception.BaseException ;

/**
 * 
 * <p>
 * <b>Description: </b> Credit Management Batch Process Exception extends from framework BaseException.
 * </p>
 * <p>
 * <b>Design Observations: </b>
 * </p>
 * <ul>
 * <li>errorCode may be used for future</li>
 * </ul>
 * 
 * <p>
 * <br>
 * <b>Issues: </b>
 * </p>
 * <ul>
 * <li>[Issues]</li>
 * </ul>
 * 
 * <p>
 * <br>
 * <b>Revision History: </b>
 * </p>
 * <table border="1" width="100%">
 * <tr>
 * <th width="15%">Date</th>
 * <th width="15%">Revised By</th>
 * <th width="55%">Description</th>
 * <th width="15%">Reviewed By</th>
 * </tr>
 * <tr>
 * <td width="15%">&nbsp;</td>
 * <td width="15%">&nbsp;</td>
 * <td width="55%">&nbsp;</td>
 * <td width="15%">&nbsp;</td>
 * </tr>
 * </table>
 * 
 * @author Lei Fan(x089748)
 *  
 */
public class CreditMgmtBatchException extends BaseException implements Serializable
{
    /**
     * <p>
     * <b>Description: </b> Default constructor, take no parameters.
     * </p>
     */
	public CreditMgmtBatchException()
	{	
		super();
	  }
	
	 /**
     * <p>
     * <b>Description: </b> Constructs CreditMgmtBatchException with error message.
     * </p>
     * 
     * @param message
     */
	public CreditMgmtBatchException(String message)
	{
		super(message);
	  }
	
	 /**
     * <p>
     * <b>Description: </b> Constructs CreditMgmtBatchException with throwable cause.
     * </p>
     * 
     * @param cause
     */
	public CreditMgmtBatchException(Throwable cause)
	{
		super(cause);
	  }
	
	/**
     * <p>
     * <b>Description: </b> Constructs CreditMgmtBatchException with error message and error code.
     * </p>
     * 
     * @param message
     * @param errorCode
     */
    public CreditMgmtBatchException(String message, String errorCode)
    {
        super(message);
        setErrorCode(errorCode);
    }
	
	 /**
     * <p>
     * <b>Description: </b> Constructs CreditMgmtBatchException with error message and cause.
     * </p>
     * 
     * @param message
     * @param cause
     */
	public CreditMgmtBatchException(String message,Throwable cause)
	{
		super(message,cause);
	  }
	
	/**
     * <p>
     * <b>Description: </b> Constructs CreditMgmtBatchException with error message,errorCode and cause.
     * </p>
     * 
     * @param message
     * @param errorCode
     * @param cause
     */
    public CreditMgmtBatchException(String message, String errorCode,Throwable cause)
    {
        super( message, cause );
        setErrorCode( errorCode );
    }
}
