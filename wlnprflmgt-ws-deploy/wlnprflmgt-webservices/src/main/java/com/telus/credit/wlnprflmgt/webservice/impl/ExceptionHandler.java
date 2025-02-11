package com.telus.credit.wlnprflmgt.webservice.impl;


import com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.*;

public class ExceptionHandler {
	
	public static ServiceException createServiceException(String errorMessage,String errorCode) {
		ServiceException se = new ServiceException();
		se.setErrorCode(errorCode);
		se.setErrorMessage(errorMessage);
		se.setMessageId("");
		return se;
		
	}
	
	public static PolicyException createPolicyException(String errorMessage,String errorCode) {
		PolicyException pe = new PolicyException();
		pe.setErrorCode(errorCode);
		pe.setErrorMessage(errorMessage);
		pe.setMessageId("");
		return pe;
	}
	
	


}
