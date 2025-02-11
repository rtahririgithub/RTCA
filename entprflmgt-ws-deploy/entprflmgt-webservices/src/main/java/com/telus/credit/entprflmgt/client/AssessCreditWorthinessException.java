package com.telus.credit.entprflmgt.client;

public class AssessCreditWorthinessException extends WSClientInvokeException {
	private static final long serialVersionUID = 6995372213202021463L;

	public AssessCreditWorthinessException() {
		super();
	}

	public AssessCreditWorthinessException(String message, Throwable th) {
		super(message, th);
		String parentErrorCode = null;
		String parentErrorMessage = null;
		ExceptionType et = null;
		if (th instanceof com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementproxyservice_2.PolicyException) {
			et = ExceptionType.POLICY;
			parentErrorCode = ((com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementproxyservice_2.PolicyException)th)
			.getFaultInfo().getErrorCode();
			parentErrorMessage = ((com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementproxyservice_2.PolicyException)th)
			.getFaultInfo().getErrorMessage();
		}
		else if (th instanceof com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementproxyservice_2.ServiceException) {
			et = ExceptionType.SERVICE;
			parentErrorCode = ((com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementproxyservice_2.ServiceException)th)
			.getFaultInfo().getErrorCode();
			parentErrorMessage = ((com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementproxyservice_2.ServiceException)th)
			.getFaultInfo().getErrorMessage();
		}
		
		this.setParentErrorCode(parentErrorCode);
		this.setParentErrorMessage(parentErrorMessage);
		this.setType(et);
	}

	public AssessCreditWorthinessException(String arg0) {
		super(arg0);
	}

	public AssessCreditWorthinessException(Throwable arg0) {
		super(arg0);
	}

}
