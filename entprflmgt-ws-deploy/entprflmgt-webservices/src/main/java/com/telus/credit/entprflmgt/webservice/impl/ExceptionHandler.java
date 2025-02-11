package com.telus.credit.entprflmgt.webservice.impl;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.io.StringWriter;

import com.telus.credit.entprflmgt.client.CreditProfileUpdateException;
import com.telus.credit.entprflmgt.client.GetFullCustomerInfoException;
import com.telus.credit.entprflmgt.client.WSClientInvokeException;
import com.telus.framework.exception.BaseException;
import com.telus.framework.exception.BaseRuntimeException;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditprofilemanagementsvc_2.ServiceException;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.exceptions_v3.FaultExceptionDetailsType;

public class ExceptionHandler {
	private static final String CUSTOMER_INFO_RETRIEVAL_EXCEPTION = "ECRD-CUS-001";
	private static final String UPDATING_CREDIT_PROFILE_EXCEPTION = "ECRD-UPT-001";
	public static final String GUARANTOR_VALIDATION_EXCEPTION = "ECRD-UPT-002";
    public static final String GUARANTOR_CUSTOMER_ID_VALIDATION_EXCEPTION = "ECRD-UPT-003";
	
	private static final String UNKNOWN_EXCEPTION = "ECRD-UNK-001";
	
	/*	public static PolicyException createPolicyException(Throwable th) {
		PolicyException pe = new PolicyException();
		pe.setErrorCode(getErrorCode(th));
		pe.setErrorMessage(getErrorMessage(th));
		pe.setMessageId("");
		return pe;
		
	}*/
	public static FaultExceptionDetailsType createFaultExceptionDetailsType(Throwable th) {
		FaultExceptionDetailsType pp = new FaultExceptionDetailsType(); 
		String id = "";
		String code = getErrorCode(th);
		String message = getErrorMessage(th);

		pp.setErrorCode(code);
		pp.setErrorMessage(message);
		pp.setMessageId(id);
		pp.getVariables().add("StackTrace:\n" + getStackTrace(th));
		return pp;		
	}
	
	public static String getStackTrace(Throwable aThrowable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
	}
	
	public static FaultExceptionDetailsType createFaultExceptionDetailsType(String errorMessage) {
		FaultExceptionDetailsType pe = new FaultExceptionDetailsType();
		pe.setErrorCode(UPDATING_CREDIT_PROFILE_EXCEPTION);
		pe.setErrorMessage(errorMessage);
		pe.setMessageId("");
		return pe;
	}
	
	public static String getErrorMessage(Throwable th) {
		StringBuffer result = new StringBuffer(th.getMessage());
		if (th instanceof WSClientInvokeException) {
			WSClientInvokeException clientException = (WSClientInvokeException)th;
			result.append( ", Down stream service error code: " + clientException.getParentErrorCode()
						   + ", error message: " + clientException.getParentErrorMessage() );
		}
		else if ( th instanceof BaseException ) {
			result.append( ", Base Exception Error Code: " + ((BaseException)th).getErrorCode() );
		}
		else if ( th instanceof BaseRuntimeException ) {
			result.append( ", Base Runtime Exception Error Code: " + ((BaseRuntimeException)th).getErrorCode() );
		}
		return result.toString();
	}
	
	public static String getErrorCode(Throwable th) {
		String errorCode;
		if (th instanceof GetFullCustomerInfoException){
			errorCode = CUSTOMER_INFO_RETRIEVAL_EXCEPTION;
		}
		else if (th instanceof CreditProfileUpdateException) {
			errorCode = UPDATING_CREDIT_PROFILE_EXCEPTION;
			// RTCA 1.5, add specific error codes for gurantor validation exception
			CreditProfileUpdateException e = (CreditProfileUpdateException) th;
			if ( e.getParentErrorCode() != null ) {
				if ( e.getParentErrorCode().equals("WLND-GEN-007") ) {
					errorCode = GUARANTOR_VALIDATION_EXCEPTION;
				} else if ( e.getParentErrorCode().equals("WLND-GEN-008") ) {
					errorCode = GUARANTOR_CUSTOMER_ID_VALIDATION_EXCEPTION;
				}
			}
		}
		else {
			errorCode = UNKNOWN_EXCEPTION;
		}
		return errorCode;
		
	}
	

}
