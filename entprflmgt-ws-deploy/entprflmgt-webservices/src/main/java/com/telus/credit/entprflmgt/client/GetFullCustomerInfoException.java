package com.telus.credit.entprflmgt.client;

public class GetFullCustomerInfoException extends WSClientInvokeException {
	private static final long serialVersionUID = 6995372213202021463L;

	public GetFullCustomerInfoException() {
		super();
	}

	public GetFullCustomerInfoException(String message, Throwable th) {
		super(message, th);
		String parentErrorCode = null;
		String parentErrorMessage = null;
		ExceptionType et = null;
		if (th instanceof com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.PolicyException) {
			et = ExceptionType.POLICY;
			parentErrorCode = ((com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.PolicyException)th)
			.getFaultInfo().getErrorCode();
			parentErrorMessage = ((com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.PolicyException)th)
			.getFaultInfo().getErrorMessage();
		}
		else if (th instanceof com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.ServiceException) {
			et = ExceptionType.SERVICE;
			parentErrorCode = ((com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.ServiceException)th)
			.getFaultInfo().getErrorCode();
			parentErrorMessage = ((com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.ServiceException)th)
			.getFaultInfo().getErrorMessage();
		}
		
		this.setParentErrorCode(parentErrorCode);
		this.setParentErrorMessage(parentErrorMessage);
		this.setType(et);
	}

	public GetFullCustomerInfoException(String arg0) {
		super(arg0);
	}

	public GetFullCustomerInfoException(Throwable arg0) {
		super(arg0);
	}

}
