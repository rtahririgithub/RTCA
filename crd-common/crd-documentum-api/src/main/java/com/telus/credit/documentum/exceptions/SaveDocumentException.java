/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 * $Id$
 */

package com.telus.credit.documentum.exceptions;

public class SaveDocumentException extends Exception {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 2377607037138584757L;

	public SaveDocumentException() {
		super();
	}

	public SaveDocumentException(String message, Throwable cause) {
		super(message, cause);
	}

	public SaveDocumentException(String message) {
		super(message);
	}

	public SaveDocumentException(Throwable cause) {
		super(cause);
	}
	

}
