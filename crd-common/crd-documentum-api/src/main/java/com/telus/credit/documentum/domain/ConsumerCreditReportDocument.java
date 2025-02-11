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

package com.telus.credit.documentum.domain;


public class ConsumerCreditReportDocument extends CreditReportDocument{
	private final DocumentType m_documentType = DocumentType.CONS_REPORT;

	public ConsumerCreditReportDocument(String fileName) {
		super(fileName);
	}

	
	public DocumentType getDocumentType() {
		return m_documentType;
	}
	

}
