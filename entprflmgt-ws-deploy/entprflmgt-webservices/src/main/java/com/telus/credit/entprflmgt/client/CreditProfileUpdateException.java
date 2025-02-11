package com.telus.credit.entprflmgt.client;

/**
 * Indicates errors updating Credit Profile using external web-services.
 * @author Danil Glinenko
 *
 */
public class CreditProfileUpdateException extends WSClientInvokeException {
	private static final long serialVersionUID = -6041302450448654215L;

	public CreditProfileUpdateException() {
		super();
	}

	public CreditProfileUpdateException(String message, Throwable th) {
		super(message, th);
		String parentErrorCode = null;
		String parentErrorMessage = null;
		Origin org = null;
		ExceptionType et = null;
		// get parent error code if possible.
		if (th instanceof com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.PolicyException) {
			org = Origin.WIRELINE;
			et = ExceptionType.POLICY;
			parentErrorCode = ((com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.PolicyException)th)
			.getFaultInfo().getErrorCode();
			parentErrorMessage = ((com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.PolicyException)th)
			.getFaultInfo().getErrorMessage();
		} else if(th instanceof com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.ServiceException) {
			org = Origin.WIRELINE;
			et = ExceptionType.SERVICE;
			parentErrorCode = ((com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.ServiceException)th)
			.getFaultInfo().getErrorCode();
			parentErrorMessage = ((com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.ServiceException)th)
			.getFaultInfo().getErrorMessage();
		}
		else if (th instanceof com.telus.wsdl.cmo.ordermgmt.wlscreditprofiledatamanagementsvc_2.ServiceException) {
			org = Origin.WIRELESS;
			et = ExceptionType.SERVICE;
			parentErrorCode = ((com.telus.wsdl.cmo.ordermgmt.wlscreditprofiledatamanagementsvc_2.ServiceException)th)
			.getFaultInfo().getErrorCode();
			parentErrorMessage = ((com.telus.wsdl.cmo.ordermgmt.wlscreditprofiledatamanagementsvc_2.ServiceException)th)
			.getFaultInfo().getErrorMessage();
		}
		
		this.setOrigin(org);
		this.setParentErrorCode(parentErrorCode);
		this.setType(et);
		this.setParentErrorMessage(parentErrorMessage);
	}

	public CreditProfileUpdateException(String arg0) {
		super(arg0);
	}

	public CreditProfileUpdateException(Throwable arg0) {
		this("", arg0);
	}

}
