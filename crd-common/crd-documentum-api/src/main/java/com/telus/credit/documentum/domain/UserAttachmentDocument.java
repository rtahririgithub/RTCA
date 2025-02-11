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
import java.util.List;

public class UserAttachmentDocument {
	private final DocumentType m_documentType = DocumentType.USER_ATTACHMENT;
	
	private byte[] m_documentContents;
	private String m_fileName;
	private String m_fullPath;
	private String m_contentType;
	
	public UserAttachmentDocument(String fileName) {
		if (fileName == null) {
			throw new IllegalArgumentException("fileName cannot be null");
		}
		this.m_fileName = fileName;
	}
	public String getFullPath() {
		return m_fullPath;
	}
	public void setFullPath(String path) {
		m_fullPath = path;
	}
	public String getContentType() {
		return this.m_contentType;
	}
	public void setContentType(String contentType) {
		this.m_contentType = contentType != null ? contentType.toLowerCase() : contentType;
	}
	public byte[] getDocumentContents() {
		return m_documentContents;
	}
	public void setDocumentContents(byte[] data) {
		m_documentContents = data;
	}
	public String getFileName() {
		return this.m_fileName;
	}
	
	public DocumentType getDocumentType() {
		return m_documentType;
	}
	public List<String> getMetadataAttrNames() {
		//empty no meta-data attributes in user attachment object
		return new ArrayList<String>();
	}
	
	public Object getValueForAttribute(String attrName) {
		// as there are not attributes we return null.
		return null;
	}
	
	public void setValueForAttribute(String attrName, Object value) {
		// no implementation in this class.
	}
	
}
