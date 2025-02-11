package com.telus.credit.entprflmgt.asyncprocessing;

// This enum represents common JMS Message properties supported by TELUS ESB components.
// We better move this to a separate jar.
public enum ARFN_HEADER_VALUES {
	ARFM_APPLICATION_ID, 
	ARFM_CORRELATION_ID, 
	ARFM_CUSTOMER_ID, 
	ARFM_ORDER_NUMBER, 
	ARFM_RESOURCE_ID, 
	ARFM_SERVICE_NAME, 
	ARFM_SERVICE_TEL_NUMBER;
}
