/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 * $Id$
 */

package com.telus.credit.batch.exception;

import com.telus.framework.exception.BaseException;


/**
 * Batch Expception - used for Batch errors.
 * @author x105587
 *
 */
public class BaseModuleException extends BaseException
{

    private String m_errorType;
    private String m_errorSubType;



    public BaseModuleException(String message)
    {
        super( message );
    }


    public BaseModuleException(String type, String subtype, String message)
    {
        super( message );
        m_errorType = type;
        m_errorSubType = subtype;
    }


    public BaseModuleException(String type, String subtype, String message,
            String errorCode)
    {
        super( message, errorCode );
        m_errorType = type;
        m_errorSubType = subtype;
    }


    public BaseModuleException(String type, String subtype, String message,
            String errorCode, Throwable throwableExp)
    {
        super( message, errorCode, throwableExp );
        m_errorType = type;
        m_errorSubType = subtype;
    }


    public BaseModuleException(String type, String subtype, String message,
            Throwable throwableExp)
    {
        super( message, throwableExp );
        m_errorType = type;
        m_errorSubType = subtype;
    }


    /**
     * @return Returns the errorSubType.
     */
    public String getErrorSubType()
    {
        return m_errorSubType;
    }


    /**
     * @return Returns the errorType.
     */
    public String getErrorType()
    {
        return m_errorType;
    }
}
