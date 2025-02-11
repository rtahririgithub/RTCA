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

public enum DocumentType {
	USER_ATTACHMENT("telus_crd_usr_attach"), 
	COMM_REPORT("telus_crd_comm_report"), 
	CONS_REPORT("telus_crd_cons_report"),
	CORP_REPORT("telus_crd_corp_report");
	
	DocumentType(String code) {
		this.m_attrCode = code;
	}
	private final String m_attrCode;

	public String code() { return m_attrCode;}

}
