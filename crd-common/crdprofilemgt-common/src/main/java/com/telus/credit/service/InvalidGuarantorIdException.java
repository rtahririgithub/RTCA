/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.service;

import com.telus.framework.exception.BaseException;

/**
 * Exception thrown if the Guarantor Customer Id is not found
 * in the Credit Management PDS.
 * 
 * @author ypollock
 */
public class InvalidGuarantorIdException extends BaseException
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -304296270493330984L;


	/**
     * Constructs a InvalidGuarantorIdException with no detail message, no error code and
     * no cause exception.
     */
    public InvalidGuarantorIdException()
    {
        super();
    }


    /**
     * Constructs a InvalidGuarantorIdException with a detail message 
     * @param message the detail message
     */
    public InvalidGuarantorIdException(String message)
    {
        super( message );
    }


    /**
     * Constructs a InvalidGuarantorIdException with a cause exception.
     * @param cause a Thowable object representing the casue exception.
     */
    public InvalidGuarantorIdException(Throwable cause)
    {
        super( cause );
    }


    /**
     * Constructs a InvalidGuarantorIdException with a detail message and 
     * errorCode.
     * @param message the detail message.
     * @param errorCode  the error code.
     */
    public InvalidGuarantorIdException(String message, String errorCode)
    {
        super( message, errorCode );
    }


    /**
     * Constructs a InvalidGuarantorIdException with a detail message and 
     * cause exception.
     * @param message the detail message.
     * @param cause a Thowable object representing the casue exception.
     */
    public InvalidGuarantorIdException(String message, Throwable cause)
    {
        super( message, cause );
    }


    /**
     * Constructs a InvalidGuarantorIdException with a detail message, 
     * an error code and a cause exception. 
     * @param message the detail message.
     * @param errorCode  the error code.
     * @param cause a Thowable object representing the casue exception.
     */
    public InvalidGuarantorIdException(String message, String errorCode,
            Throwable cause)
    {
        super( message, errorCode, cause );
    }
}