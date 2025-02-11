package com.telus.credit.orderDepositCalculator.webservice.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fico.telus.rtca.blaze.RuleServicesException;
import com.telus.framework.exception.BaseException;
import com.telus.framework.exception.BaseRuntimeException;
import com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.*;

public class ExceptionHandler {
	public static final String FICO_CALL_EXCEPTION = "ODEP-FIC-001";
	public static final String VALIDATION_EXCEPTION = "ODEP-DAT-001";
	
	private static final String UNKNOWN_EXCEPTION = "ODEP-UNK-001";
	
	private static final Log log = LogFactory.getLog(ExceptionHandler.class);
	
	public static PolicyException createPolicyException(Throwable th) {
		log.error("Order Deposit Calculator Policy Exception: " + th, th );
		PolicyException pe = new PolicyException();
		pe.setErrorCode(getErrorCode(th));
		pe.setErrorMessage(getErrorMessage(th));
		pe.setMessageId("");
		return pe;
		
	}
	public static PolicyException createPolicyException(String errorCode,
				String errorMessage ) {
		log.error("Order Deposit Calculator Policy Exception: Errror code: " + errorCode +
				  " Error Message: " + errorMessage );
		PolicyException pe = new PolicyException();
		pe.setErrorCode(errorCode);
		pe.setErrorMessage(errorMessage);
		pe.setMessageId("");
		return pe;
	}
	
	
	public static ServiceException createServiceException(Throwable th) {
		log.error("Order Deposit Calculator Service Exception: " + th, th );
		ServiceException se = new ServiceException();
		se.setErrorCode(getErrorCode(th));
		se.setErrorMessage(getErrorMessage(th));
		se.setMessageId("");
		return se;
		
	}
	
	
	public static String getErrorMessage(Throwable th) {
		StringBuffer result = new StringBuffer(th.getMessage());
		if ( th instanceof RuleServicesException ) {
			result.append( " FICO Exception Error Code: " + ((RuleServicesException)th).getErrorCode() );
		}
		if ( th instanceof BaseException ) {
			result.append( ", Base Exception Error Code: " + ((BaseException)th).getErrorCode() );
		}
		else if ( th instanceof BaseRuntimeException ) {
			result.append( ", Base Runtime Exception Error Code: " + ((BaseRuntimeException)th).getErrorCode() );
		}
		return result.toString();
	}
	
	public static String getErrorCode(Throwable th) {
		String errorCode;
		if (th instanceof RuleServicesException){
			errorCode = FICO_CALL_EXCEPTION;
		}
		else {
			errorCode = UNKNOWN_EXCEPTION;
		}
		return errorCode;
		
	}
	

}
