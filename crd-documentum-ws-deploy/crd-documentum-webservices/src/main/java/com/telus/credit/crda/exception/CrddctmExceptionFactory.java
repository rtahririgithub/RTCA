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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.credit.crddctm.ws.ServiceException;
import com.telus.credit.documentum.exceptions.DocumentEncryptionException;
import com.telus.credit.documentum.exceptions.RetrieveDocumentException;
import com.telus.framework.exception.BaseException;
import com.telus.framework.exception.BaseRuntimeException;
import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.framework.exception.PersistenceException;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.Message;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.ResponseMessage;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.exceptions_v3.FaultExceptionDetailsType;

public class CrddctmExceptionFactory {

    /*
     * Log object
     */
    private static final Log log = LogFactory.getLog(CrddctmExceptionFactory.class);
	public static  boolean messageGetMessageIsNotEmptyInd(Message polictExceptionMessage) {
		String msg = (polictExceptionMessage!=null)?polictExceptionMessage.getMessage():null;
		msg = (msg!=null)?msg:"";
		msg = (!"null".equalsIgnoreCase(msg.trim()))?msg:"";
		if (	
				!"".equals(msg) 
				){
			return true;						
		}
		return false;
	}
	public static  void handlePolicyException(
    		ResponseMessage responseMessage_Response,
			Long aTransactionId,
			CrddctmPolicyException e) {

		//setErrorCode : this is the error code that consumer will use to lookup refpds
		responseMessage_Response.setErrorCode(e.getErrorCode()); 
		
		//the rest is to help for troubleshooting
		
		//setMessageList in ResponseMessage
		List<Message>  responseMessage_Response_messageList = new ArrayList<Message>();			
		
		//add policy exception Message to messageList
		Message  polictExceptionMessage= new Message(); 
		polictExceptionMessage.setMessage(e.getMessage());	
		if(messageGetMessageIsNotEmptyInd(polictExceptionMessage)){
			responseMessage_Response_messageList.add(polictExceptionMessage);
		}		
				
		
		//add policy exception errorArgslist to messageList
		Object[] policyException_ErrorArgslist = e.getErrorArgs();
		if(policyException_ErrorArgslist!= null){
			for (Object object : policyException_ErrorArgslist) {				
				if(object!=null && object instanceof Message){;
					Message polictException_ErrorArgslist_Of_MessageType_Message = (Message)object;
					if(messageGetMessageIsNotEmptyInd(polictException_ErrorArgslist_Of_MessageType_Message)){
						responseMessage_Response_messageList.add(polictException_ErrorArgslist_Of_MessageType_Message);
					}
				}else{
					if(object!=null && object instanceof String){;
						Message  polictException_ErrorArgslist_Of_STringType_Message= new Message(); 
						polictException_ErrorArgslist_Of_STringType_Message.setMessage((String)object);
						if(messageGetMessageIsNotEmptyInd(polictException_ErrorArgslist_Of_STringType_Message)){
							responseMessage_Response_messageList.add(polictException_ErrorArgslist_Of_STringType_Message);
						}						
					}else{
						String objectStr = String.valueOf(object);
						Message  polictException_ErrorArgslist_Of_ObjectType_Message= new Message(); 
						polictException_ErrorArgslist_Of_ObjectType_Message.setMessage(objectStr);			
						if(messageGetMessageIsNotEmptyInd(polictException_ErrorArgslist_Of_ObjectType_Message)){
							responseMessage_Response_messageList.add(polictException_ErrorArgslist_Of_ObjectType_Message);
						}
						
					}			
				}
			}
		}
		responseMessage_Response.setMessageList(responseMessage_Response_messageList);
		
		//response.setMessageType("MessageType");
		
		//setTransactionId 
		String  aTransactionIdStr =(aTransactionId!=null)? aTransactionId.toString():null;
		responseMessage_Response.setTransactionId(aTransactionIdStr);	
		//setDateTimeStamp
		responseMessage_Response.setDateTimeStamp(new Date());
		
	}

 
    public static ServiceException createCrddctmServiceException(String operationName,
                                                          String errorMessage, Throwable err) {
        String errorCode = getErrorCode(err);
        String fullErrorMessage = getErrorMessage("CrddctmServiceException", err, errorMessage, operationName);
        if (err instanceof CrddctmServiceException
                && err.getCause() != null) {
            err = err.getCause();
        }
        log.error("Error Code: " + errorCode + ", Error Message: "
                + fullErrorMessage, err);

        FaultExceptionDetailsType faultInfo = new FaultExceptionDetailsType();
        		//com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.CrddctmServiceException();
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
        String result = ExceptionCodes.UNKNOWN_EXCEPTION;
        if (e instanceof  CrddctmPolicyException) {
            result = ((CrddctmPolicyException) e).getErrorCode();
        } else if (e instanceof CrddctmServiceException) {
            result = ((CrddctmServiceException) e).getErrorCode();
        }else if (e instanceof BaseException) {
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
                                                                String objectType, Object objectId, Throwable e) throws CrddctmPolicyException, CrddctmServiceException {
        String msgStr =
                e.getClass().getName() + " in "
                        + operationAndClassName
                        + " for " + objectType + " " //
                        + objectId;
        if (e instanceof CrddctmPolicyException) {
            throw (CrddctmPolicyException) e;
        } else if (e instanceof CrddctmServiceException) {
            throw (CrddctmServiceException) e;
        }
        
    }
}
