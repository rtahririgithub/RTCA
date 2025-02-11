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

package com.telus.credit.batch.util;

import java.io.IOException;

import org.apache.commons.logging.Log;

import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;

import com.telus.credit.batch.exception.BaseModuleException;

/**
 * Batch Error Handler - to log the batch application related errors.
 *
 */
public class BatchErrorHandler
{

    public static void logError(Log logger, Throwable e)
    {
        logger.error( formatMessage( BatchErrorConstants.Type.THRW, null, e
                .getMessage() ) );
    }


    public static void logError(Log logger, BaseModuleException e)
    {
        logger.error( formatMessage( ((BaseModuleException) e).getErrorType(),
                ((BaseModuleException) e).getErrorSubType(), e.getMessage() ) );
    }


    public static void logError(Log logger, IOException e)
    {
        logError( logger, e, null );
    }


    public static void logError(Log logger, IOException e, String message)
    {
        if ( message == null ) message = "";
        message = message + " " + e.getMessage();
        logger.error( formatMessage( BatchErrorConstants.Type.FILE, null, message ) );
    }


    public static void logError(Log logger, DuplicateKeyException e)
    {
        logError( logger, e, null );
    }


    public static void logError(Log logger, DuplicateKeyException e,
            String message)
    {
        if ( message == null ) message = "";
        message = message + " " + e.getMessage();
        logger.error( formatMessage( BatchErrorConstants.Type.DB,
                BatchErrorConstants.Subtype.DB_DUP_KEY, message ) );
    }


    public static void logError(Log logger, ObjectNotFoundException e)
    {
        logError( logger, e, null );
    }


    public static void logError(Log logger, ObjectNotFoundException e,
            String message)
    {
        if ( message == null ) message = "";
        message = message + " " + e.getMessage();
        logger.error( formatMessage( BatchErrorConstants.Type.DB,
                BatchErrorConstants.Subtype.DB_OBJ_NOT_FOUND, message ) );
    }


    public static void logError(Log logger, ConcurrencyConflictException e)
    {
        logError( logger, e, null );
    }


    public static void logError(Log logger, ConcurrencyConflictException e,
            String message)
    {
        if ( message == null ) message = "";
        message = message + " " + e.getMessage();
        logger.error( formatMessage( BatchErrorConstants.Type.DB,
                BatchErrorConstants.Subtype.DB_CONCR_CONF, message ) );
    }


    public static void logError(Log logger, Exception e)
    {
        logger.error( formatMessage( null, null, e.getMessage() ) );
    }


    public static void logError(Log logger, String errorType,
            String errorSubtype, String errorMessage)
    {
        logger.error( formatMessage( errorType, errorSubtype, errorMessage ) );
    }


    public static void logError(Log logger, String errorType,
            String errorSubtype, String errorMessage, String fileName)
    {
        logger.error( formatMessage( errorType, errorSubtype, errorMessage,
                fileName ) );
    }


    /**
     * method will format the record to print each param on designated position
     * by adding extra spaces to the error type and subtype
     * method implemenmtation is not finished
     * @param errorType
     * @param errorSubtype
     * @param errorMessage
     * @return
     */
    private static String formatMessage(String errorType, String errorSubtype,
            String errorMessage)
    {
        return errorType + ": " + errorSubtype + ": " + errorMessage;
    }


    /**
     * method will format the record to print each param on designated position
     * by adding extra spaces to the error type and subtype
     * method implemenmtation is not finished
     * @param errorType
     * @param errorSubtype
     * @param errorMessage
     * @param fileName
     * @return
     */
    private static String formatMessage(String errorType, String errorSubtype,
            String errorMessage, String fileName)
    {
        String message = "";
        if ( fileName == null )
        {
            message = errorType + ": " + errorSubtype + ": " + errorMessage;
        }
        else
        {
            message = errorType + ": " + errorSubtype + ":File:" + fileName
                    + errorMessage;
        }
        return message;
    }
}
