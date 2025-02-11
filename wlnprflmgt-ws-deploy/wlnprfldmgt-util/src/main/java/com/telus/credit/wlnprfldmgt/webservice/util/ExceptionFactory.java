/*
 *  Copyright (c) 2012 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 * $Id$
 */
package com.telus.credit.wlnprfldmgt.webservice.util;

import com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.PolicyException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.ServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.framework.exception.BaseException;
import com.telus.framework.exception.BaseRuntimeException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.framework.exception.PersistenceException;

/**
 * <p><b>Description :</b><class>ExceptionFactory</class> creates the policy and service exceptions from different exceptions
 * thrown by Credit Assessment Svc.</p>
 * <p><b>Design Observations : </b></p>
 * <ul>
 * <li>None</li>
 * </ul>
 * <p><br><b>Revision History : </b></p>
 * <table border="1" width="100%">
 * <tr>
 * <th width="15%">Date</th>
 * <th width="15%">Revised By</th>
 * <th width="55%">Description</th>
 * <th width="15%">Reviewed By</th>
 * </tr>
 * <tr>
 * <td width="15%">20-Sep-2012</td>
 * <td width="15%">Gurbirinder Sidhu</td>
 * <td width="55%">New Class</td>
 * <td width="15%">&nbsp;</td>
 * </tr>
 * </table>
 * 
 * @author Gurbirinder Sidhu
 * 
 * @stereotype Exception Factory
 * @version 1.0
 */
public class ExceptionFactory
{

    /*
     * Log object
     */
    private static final Log log = LogFactory.getLog( ExceptionFactory.class );


    /**
     * Create PolicyException from the given exception
     *  
     * @param operationName
     * @param errorMessage
     * @param err
     * @return PolicyException
     */
    public static PolicyException createPolicyException(String operationName,
            String errorMessage, String errorCode )
    {
        log.error( "Error Code: " + errorCode + ", Error Message: "
                + errorMessage );

        com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException faultInfo = new com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException();
        faultInfo.setErrorCode( errorCode );
        faultInfo.setErrorMessage( errorMessage );

        return new PolicyException( errorMessage, faultInfo, null);
    }

    /**
     * Create PolicyException from the given exception
     *  
     * @param operationName
     * @param errorMessage
     * @param err
     * @return PolicyException
     */
    public static PolicyException createPolicyException(String operationName,
            String errorMessage, Throwable err)
    {
        String errorCode = getErrorCode( err );
        String fullErrorMessage = getErrorMessage( "PolicyException", err,
                errorMessage, operationName );

        log.error( "Error Code: " + errorCode + ", Error Message: "
                + fullErrorMessage, err );

        com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException faultInfo = new com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException();
        faultInfo.setErrorCode( errorCode );
        faultInfo.setErrorMessage( fullErrorMessage );

        return new PolicyException( errorMessage, faultInfo, err );
    }


    /**
     * Create ServiceException from the given exception
     *  
     * @param operationName
     * @param errorMessage
     * @param err
     * @return  ServiceException 
     */
    public static ServiceException createServiceException(String operationName,
            String errorMessage, Throwable err)
    {
        String errorCode = getErrorCode( err );
        String fullErrorMessage = getErrorMessage( "ServiceException", err,
                errorMessage, operationName );

        log.error( "Error Code: " + errorCode + ", Error Message: "
                + fullErrorMessage, err );

        com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException faultInfo = new com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException();
        faultInfo.setErrorCode( errorCode );
        faultInfo.setErrorMessage( fullErrorMessage );
        return new ServiceException( errorMessage, faultInfo, err );
    }


    /**
     * Extract the error code from the exception
     *  
     * @param A {@code Exception}
     * @return errorCode   
     *  
     */
    private static String getErrorCode(Throwable e)
    {
        String result = WLNCreditProfileDataManagementExceptionCodes.UNKNOWN_EXCEPTION;
        if ( e instanceof WLNCreditProfileDataManagementPolicyException ) {
            result = ((WLNCreditProfileDataManagementPolicyException)e).getErrorCode();
        }
        else if ( e instanceof WLNCreditProfileDataManagementServiceException ) {
            result = ((WLNCreditProfileDataManagementServiceException)e).getErrorCode();
        }
        else if ( e instanceof ObjectNotFoundException ) {
            result = WLNCreditProfileDataManagementExceptionCodes.OBJECT_NOT_FOUND_EXCEPTION;
        }
        else if ( e instanceof DuplicateKeyException ) {
            result = WLNCreditProfileDataManagementExceptionCodes.DUPLICATE_KEY_EXCEPTION;
        }
        else if ( e instanceof PersistenceException ) {
            result = WLNCreditProfileDataManagementExceptionCodes.PERSISTENT_EXCEPTION;
        }
        else if ( e instanceof BaseException ) {
            result = ((BaseException)e).getErrorCode();
        }
        else if ( e instanceof BaseRuntimeException ) {
            result = ((BaseRuntimeException)e).getErrorCode();
        }
        
        return result;
    }


    /**
     * Extract the error message from the exception
     *  
     * @param string
     * @param err
     * @param errorMessage
     * @param operationName
     * @return  fullErrorMessage
     */
    private static String getErrorMessage(String typeOfException, Throwable err,
            String errorMessage, String operationName)
    {
        return "Type of Exception: " + typeOfException
                + ", Operation Name: " + operationName
                + ", Error Message: " + errorMessage
                + ", Exception Error Message: " + err;
    }
}
