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
package com.telus.credit.crda.exception;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import com.fico.telus.rtca.blaze.RuleServicesException;
import com.telus.credit.documentum.exceptions.DocumentEncryptionException;
import com.telus.credit.documentum.exceptions.RetrieveDocumentException;
import com.telus.framework.exception.BaseException;
import com.telus.framework.exception.BaseRuntimeException;
import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.framework.exception.PersistenceException;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditassessmentservice_2.PolicyException;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditassessmentservice_2.ServiceException;

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
 * @version 1.0
 * @stereotype Exception Factory
 */
public class CreditAssessmentExceptionFactory {

    /*
     * Log object
     */
    private static final Log log = LogFactory.getLog(CreditAssessmentExceptionFactory.class);


    /**
     * Create PolicyException from the given exception
     *
     * @param operationName
     * @param errorMessage
     * @param err
     * @return PolicyException
     */
    public static PolicyException createPolicyException(String operationName,
                                                        String errorMessage, Throwable err) {
        String errorCode = getErrorCode(err);
        String fullErrorMessage = getErrorMessage("PolicyException", err,
                errorMessage, operationName);
        if (err instanceof EnterpriseCreditAssessmentPolicyException
                && err.getCause() != null) {
            err = err.getCause();
        }
        log.error("Error Code: " + errorCode + ", Error Message: "
                + fullErrorMessage, err);

        com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException faultInfo = new com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException();
        faultInfo.setErrorCode(errorCode);
        faultInfo.setErrorMessage(fullErrorMessage);

        return new PolicyException(errorMessage, faultInfo, err);
    }


    /**
     * Create ServiceException from the given exception
     *
     * @param operationName
     * @param errorMessage
     * @param err
     * @return ServiceException
     */
    public static ServiceException createServiceException(String operationName,
                                                          String errorMessage, Throwable err) {
        String errorCode = getErrorCode(err);
        String fullErrorMessage = getErrorMessage("ServiceException", err, errorMessage, operationName);
        if (err instanceof EnterpriseCreditAssessmentServiceException
                && err.getCause() != null) {
            err = err.getCause();
        }
        log.error("Error Code: " + errorCode + ", Error Message: "
                + fullErrorMessage, err);

        com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException faultInfo = new com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException();
        faultInfo.setErrorCode(errorCode);
        faultInfo.setErrorMessage(fullErrorMessage);
        return new ServiceException(errorMessage, faultInfo, err);
    }


    /**
     * Extract the error code from the exception
     *
     * @param A {@code Exception}
     * @return errorCode
     */
    private static String getErrorCode(Throwable e) {
        String result = EnterpriseCreditAssessmentExceptionCodes.UNKNOWN_EXCEPTION;
        if (e instanceof EnterpriseCreditAssessmentPolicyException) {
            result = ((EnterpriseCreditAssessmentPolicyException) e).getErrorCode();
        } else if (e instanceof EnterpriseCreditAssessmentServiceException) {
            result = ((EnterpriseCreditAssessmentServiceException) e).getErrorCode();
        } else if (e instanceof ObjectNotFoundException) {
            result = EnterpriseCreditAssessmentExceptionCodes.OBJECT_NOT_FOUND_EXCEPTION;
        } else if (e instanceof DuplicateKeyException) {
            result = EnterpriseCreditAssessmentExceptionCodes.DUPLICATE_KEY_EXCEPTION;
        } else if (e instanceof PersistenceException) {
            result = EnterpriseCreditAssessmentExceptionCodes.PERSISTENT_EXCEPTION;
        } else if (e instanceof BaseException) {
            result = ((BaseException) e).getErrorCode();
        } else if (e instanceof BaseRuntimeException) {
            result = ((BaseRuntimeException) e).getErrorCode();
        } else if (e instanceof org.springframework.beans.factory.BeanCreationException) {
            Throwable t = ((org.springframework.beans.factory.BeanCreationException) e).getRootCause();
/*        	if ( t instanceof com.fico.telus.rtca.blaze.RuleServicesException) {
                if(t!= null)
        			result =((com.fico.telus.rtca.blaze.RuleServicesException)t).getErrorCode();
        	}	*/
            //result = ((org.springframework.beans.factory.BeanCreationException)e).getRootCause();
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
     * @return fullErrorMessage
     */
    private static String getErrorMessage(String typeOfException, Throwable err,
                                          String errorMessage, String operationName) {
        return "Type of Exception: " + typeOfException
                + ", Operation Name: " + operationName
                + ", Error Message: " + errorMessage
                + ", Exception Error Message: " + err;
    }


    public static void throwEnterpriseCreditAssessmentException(String operationAndClassName,
                                                                String objectType, Object objectId, Throwable e) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        String msgStr =
                e.getClass().getName() + " in "
                        + operationAndClassName
                        + " for " + objectType + " " //
                        + objectId;
        if (e instanceof EnterpriseCreditAssessmentPolicyException) {
            throw (EnterpriseCreditAssessmentPolicyException) e;
        } else if (e instanceof EnterpriseCreditAssessmentServiceException) {
            throw (EnterpriseCreditAssessmentServiceException) e;
/*        } else if (e instanceof RuleServicesException) {
            throw new EnterpriseCreditAssessmentServiceException(msgStr,
                    EnterpriseCreditAssessmentExceptionCodes.RuleServicesException_EXCEPTION,
                    e);*/
        } else if (e instanceof DuplicateKeyException) {
        	DuplicateKeyException  eDuplicateKeyException = (DuplicateKeyException)e;
        	if(null != eDuplicateKeyException.getCause() && null != eDuplicateKeyException.getCause().getCause()){
	        	com.ibatis.common.jdbc.exception.NestedSQLException eNestedSQLException = (com.ibatis.common.jdbc.exception.NestedSQLException)eDuplicateKeyException.getCause().getCause();
	        	eNestedSQLException.getErrorCode();
	        	if (eNestedSQLException.getErrorCode() ==1400){
	        		msgStr=msgStr + " : Possible root cause:  Customer does not exist in credit mgmt db. No credit_profile_id found in cprofl_customer_map table . ";
	         	}
        	}
            throw new EnterpriseCreditAssessmentServiceException(msgStr,
                    EnterpriseCreditAssessmentExceptionCodes.DUPLICATE_KEY_EXCEPTION,
                    e);
        } else if (e instanceof RetrieveDocumentException) {
            throw new EnterpriseCreditAssessmentServiceException(msgStr,
                    EnterpriseCreditAssessmentExceptionCodes.DOCUMENT_COULD_NOT_BE_RETRIEVED,
                    e);
        } else if (e instanceof DocumentEncryptionException) {
            throw new EnterpriseCreditAssessmentServiceException(msgStr,
                    EnterpriseCreditAssessmentExceptionCodes.DOCUMENT_ENCRYPTION_ERROR,
                    e);
        } else if (e instanceof ConcurrencyConflictException) {
            throw new EnterpriseCreditAssessmentServiceException(msgStr,
                    EnterpriseCreditAssessmentExceptionCodes.CONCURRENCY_EXCEPTION,
                    e);
        } else if (e instanceof java.sql.SQLException) {
        	java.sql.SQLException sqle = (java.sql.SQLException)e;
        	//sqle.getErrorCode()
        	//java.sql.SQLException: ORA-01400: cannot insert NULL into ("PDSCR_WK05"."CAR_CREDIT_PROFILE"."CREDIT_PROFILE_ID")
            throw new EnterpriseCreditAssessmentServiceException(msgStr,
                    EnterpriseCreditAssessmentExceptionCodes.SQLException_EXCEPTION,
                    e);
        } else if (e instanceof com.telus.framework.exception.InternalErrorException) {
        	com.telus.framework.exception.InternalErrorException  eInternalErrorException = (com.telus.framework.exception.InternalErrorException)e;
        	if(null != eInternalErrorException.getCause() && null != eInternalErrorException.getCause().getCause()){
	        		msgStr=msgStr + " : Possible root cause:  " + eInternalErrorException.getCause().getCause().getMessage();
        	}        	
            throw new EnterpriseCreditAssessmentServiceException(msgStr,
                    EnterpriseCreditAssessmentExceptionCodes.InternalErrorException_EXCEPTION,
                    e);
        } else if (e instanceof DatatypeConfigurationException) {
            throw new EnterpriseCreditAssessmentServiceException(msgStr,
                    EnterpriseCreditAssessmentExceptionCodes.DatatypeConfigurationException_EXCEPTION,
                    e);
        } else if (e instanceof ObjectNotFoundException) {
            throw new EnterpriseCreditAssessmentPolicyException(msgStr,
                    EnterpriseCreditAssessmentExceptionCodes.OBJECT_NOT_FOUND_EXCEPTION,
                    e);
        }
         else {
            throw new EnterpriseCreditAssessmentServiceException(msgStr,
                    EnterpriseCreditAssessmentExceptionCodes.UNKNOWN_EXCEPTION,
                    e);
        }
        
    }
}
