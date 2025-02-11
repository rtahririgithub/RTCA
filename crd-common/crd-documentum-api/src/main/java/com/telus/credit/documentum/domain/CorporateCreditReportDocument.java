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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CorporateCreditReportDocument extends CreditReportDocument {
	private final DocumentType m_documentType = DocumentType.CORP_REPORT;
	private final static String JUR_CD_ATTR = "crd_jurcode";
	private final static String JUR_COUNTRY_CD_ATTR = "crd_jurcountrycode";
	private final static String INC_NUM_ATTR = "crd_incnum";
	
	private static final String[] localAttrNames = new String[]{JUR_CD_ATTR, JUR_COUNTRY_CD_ATTR, INC_NUM_ATTR};
	
	private String m_incorporationNum;
	private String m_jurisdictionCode;
	private String m_jurisdictionCountryCode;

	public CorporateCreditReportDocument(String fileName) {
		super(fileName);
	}

	public String getIncorporationNum() {
		return m_incorporationNum;
	}

	public void setIncorporationNum(String incorporationNum) {
		this.m_incorporationNum = incorporationNum;
	}

	public String getJurisdictionCode() {
		return m_jurisdictionCode;
	}

	public void setJurisdictionCode(String jurisdictionCode) {
		this.m_jurisdictionCode = jurisdictionCode;
	}

	public String getJurisdictionCountryCode() {
		return m_jurisdictionCountryCode;
	}

	public void setJurisdictionCountryCode(String jurisdictionCountryCode) {
		this.m_jurisdictionCountryCode = jurisdictionCountryCode;
	}
	
	public DocumentType getDocumentType() {
		return m_documentType;
	}
	
	public List<String> getMetadataAttrNames() {
		List<String> attrNames = new ArrayList<String>();
		attrNames.addAll(Arrays.asList(localAttrNames));
		// add parent attributes
		attrNames.addAll(super.getMetadataAttrNames());
		return attrNames;
	}
	
	public Object getValueForAttribute(String attrName) {
		if (attrName.equalsIgnoreCase(INC_NUM_ATTR)){
			return getIncorporationNum();
		} else if (attrName.equalsIgnoreCase(JUR_CD_ATTR)) {
			return getJurisdictionCode();
		} else if(attrName.equalsIgnoreCase(JUR_COUNTRY_CD_ATTR)) {
			return getJurisdictionCountryCode();
		} else {
			return super.getValueForAttribute(attrName);
		}
	}
	
	public void setValueForAttribute(String attrName, Object value) {
		if (attrName.equalsIgnoreCase(INC_NUM_ATTR)){
			setIncorporationNum((String) value);
		} else if (attrName.equalsIgnoreCase(JUR_CD_ATTR)) {
			setJurisdictionCode((String)value);
		} else if(attrName.equalsIgnoreCase(JUR_COUNTRY_CD_ATTR)) {
			setJurisdictionCountryCode((String)value);
		} else {
			super.setValueForAttribute(attrName, value);
		}
		
	}
	
	

}
