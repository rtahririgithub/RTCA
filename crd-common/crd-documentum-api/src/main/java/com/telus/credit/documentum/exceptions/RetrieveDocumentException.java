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

public class RetrieveDocumentException extends Exception{

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -2885486362225772932L;

	public RetrieveDocumentException() {
		super();
	}

	public RetrieveDocumentException(String message, Throwable cause) {
		super(message, cause);
	}

	public RetrieveDocumentException(String message) {
		super(message);
	}

	public RetrieveDocumentException(Throwable cause) {
		super(cause);
	}

	
}
