/**
 * 
 */
package com.telus.credit.wlnprflmgt.client;

/**
 * @author x122365
 *
 */
public class WSClientInvokeException extends Exception {
	
	private ExceptionType m_exceptionType;
	private String m_parentErrorCode;
	private String m_parentErrorMessage;
	
	public WSClientInvokeException(){
		super();
	}
	
	public WSClientInvokeException(String message, Throwable th) {
		super(message, th);
	}

	public WSClientInvokeException(String arg0) {
		super(arg0);
	}

	public WSClientInvokeException(Throwable arg0) {
		super(arg0);
	}
	
	public static enum ExceptionType {
		POLICY, 
		SERVICE;
	}
	
	public static enum Origin {
		WIRELINE,
		WIRELESS;
	}

	public ExceptionType getType() {
		return m_exceptionType;
	}

	public void setType(ExceptionType exceptionType) {
		m_exceptionType = exceptionType;
	}

	public void setParentErrorCode(String errorCode){
		m_parentErrorCode = errorCode;
	}
	
	public String getParentErrorCode(){
		return m_parentErrorCode;
	}

	public String getParentErrorMessage() {
		return m_parentErrorMessage;
	}

	public void setParentErrorMessage(String errorMessage) {
		m_parentErrorMessage = errorMessage;
	}

}
