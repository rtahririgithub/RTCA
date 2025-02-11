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

package com.telus.credit.documentum;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.credit.documentum.domain.CommercialCreditReportDocument;
import com.telus.credit.documentum.domain.ConsumerCreditReportDocument;
import com.telus.credit.documentum.domain.CorporateCreditReportDocument;
import com.telus.credit.documentum.domain.DocumentType;
import com.telus.credit.documentum.domain.UserAttachmentDocument;
import com.telus.framework.crypto.impl.jce.JceCryptoImpl;
import com.telus.credit.documentum.exceptions.RetrieveDocumentException;
import com.telus.credit.documentum.exceptions.SaveDocumentException;
import com.telus.credit.documentum.exceptions.DocumentEncryptionException;
import com.telus.framework.content.DocumentManager;
import com.telus.framework.content.document.CreateDocumentParameters;
import com.telus.framework.content.document.Docpath;
import com.telus.framework.content.document.DocumentCheckedOutException;
import com.telus.framework.content.document.DocumentContent;
import com.telus.framework.content.document.DocumentException;
import com.telus.framework.content.document.DocumentMetadata;
import com.telus.framework.content.document.DocumentObject;
import com.telus.framework.content.document.DuplicateDocumentException;
import com.telus.framework.content.document.RetrieveDocumentParameters;
import com.telus.framework.exception.ConfigurationException;
import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.framework.exception.ValidationException;

public class DocumentDaoImpl implements DocumentDao {
	private static Log log = LogFactory.getLog(DocumentDaoImpl.class);
	
	// static counter for UID generator
	private static int staticCounter = 0;
	private static final String USER_ATTACHMENT_FOLDER = "/USER_ATTACHMENTS";
	private static final String COMM_REPORT_FOLDER = "/COMM_REPORTS";
	private static final String CONS_REPORT_FOLDER = "/CONS_REPORTS";
	private static final String CORP_REPORT_FOLDER = "/CORP_REPORTS";
	private static final String SEPARATOR = "_";
	private DocumentManager m_documentMgr;
	private String m_cabinet;
    private JceCryptoImpl m_jceCrypto;
    private boolean m_enableDocumentEncryption = true;

	public DocumentDaoImpl(DocumentManager docManager){
		this.m_documentMgr = docManager;
	}
	
	/**
	 * {@inheritDoc}
	 */
    public String saveDocument(DocumentObject document) throws SaveDocumentException, DocumentEncryptionException {
	if (document == null){throw new IllegalArgumentException("document cannot be null");}
		encryptDocument(document);
		
		CreateDocumentParameters docParams = new CreateDocumentParameters(document);
		docParams.setFolderCreationAllowed(true);
		docParams.setDuplicateObjectNameAllowed(false);
		try {
			m_documentMgr.createDocument(docParams);
			return document.getDocpath().getDocpath();
		} catch (ConfigurationException e) {
			throw new SaveDocumentException(e.getMessage(), e);
		} catch (ObjectNotFoundException e) {
			throw new SaveDocumentException(e.getMessage(), e);
		} catch (DocumentException e) {
			throw new SaveDocumentException(e.getMessage(), e);
		} catch (DuplicateDocumentException e) {
			throw new SaveDocumentException(e.getMessage(), e);
		} catch (ValidationException e) {
			throw new SaveDocumentException(e.getMessage(), e);
		}
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DocumentObject retrieveDocumentObject(String docPath)
	    throws RetrieveDocumentException, ObjectNotFoundException,DocumentEncryptionException {
		if (docPath == null || docPath.isEmpty()){ throw new IllegalArgumentException("docPath param cannot be null or empty.");}
		RetrieveDocumentParameters params = new RetrieveDocumentParameters(new Docpath(docPath));
		params.setCheckout(false);
		
		try {
			DocumentObject document = m_documentMgr.retrieveDocument(params);
			decryptDocument( document );
			return document;
		} catch (ConfigurationException e) {
			throw new RetrieveDocumentException(e.getMessage(), e);
		} catch (DocumentException e) {
			throw new RetrieveDocumentException(e.getMessage(), e);
		} catch (DuplicateDocumentException e) {
			throw new RetrieveDocumentException(e.getMessage(), e);
		} catch (ValidationException e) {
			throw new RetrieveDocumentException(e.getMessage(), e);
		} catch (DocumentCheckedOutException e) {
			throw new RetrieveDocumentException(e.getMessage(), e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommercialCreditReportDocument retrieveCommercialReportDocument(
			String docpath) throws RetrieveDocumentException,
			ObjectNotFoundException, DocumentEncryptionException {
		DocumentObject docObject = retrieveDocumentObject(docpath);
		CommercialCreditReportDocument doc = new CommercialCreditReportDocument(docObject.getDocpath().getName());
		populateUserAttachmentWithMetadata(doc, docObject);
		
		log.info("Retrieved Commercial credit report from path: " + docpath + 
				" for carId: " + doc.getCarId() + 
				" and legal name: " + doc.getLegalName());
		return doc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConsumerCreditReportDocument retrieveConsumerReportDocument(String docpath)
			throws RetrieveDocumentException, ObjectNotFoundException, DocumentEncryptionException {
		DocumentObject docObject = retrieveDocumentObject(docpath);
		ConsumerCreditReportDocument doc = new ConsumerCreditReportDocument(docObject.getDocpath().getName());
		populateUserAttachmentWithMetadata(doc, docObject);
		
		log.info("Retrieved Consumer credit report from path: " + docpath + 
				" for carId: " + doc.getCarId() + 
				" and legal name: " + doc.getLegalName());
		return doc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CorporateCreditReportDocument retrieveCorporateReportDocument(
			String docpath) throws RetrieveDocumentException,
			ObjectNotFoundException, DocumentEncryptionException {
		DocumentObject docObject = retrieveDocumentObject(docpath);
		CorporateCreditReportDocument doc = new CorporateCreditReportDocument(docObject.getDocpath().getName());
		populateUserAttachmentWithMetadata(doc, docObject);
		
		log.info("Retrieved Corporate credit report from path: " + docpath + 
				" for carId: " + doc.getCarId() + 
				" and legal name: " + doc.getLegalName());
		return doc;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserAttachmentDocument retrieveUserAttachment(String docpath)
	throws RetrieveDocumentException, ObjectNotFoundException, DocumentEncryptionException {
		DocumentObject docObject = retrieveDocumentObject(docpath);
		String fileName = getHumanReadableFilename(docObject.getDocpath().getName());
		UserAttachmentDocument userAttachmentDoc = new UserAttachmentDocument(fileName);
		populateUserAttachmentWithMetadata(userAttachmentDoc, docObject);
		
		log.info("Retrieved User attachment document from path: " + docpath);
		return userAttachmentDoc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String saveDocument(UserAttachmentDocument document)
			throws SaveDocumentException, DocumentEncryptionException {
		log.info("Saving User attachment document : " + document.getFileName());
		DocumentObject docObject = buildDocumentObject(document);
		return saveDocument(docObject);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String saveDocument(CorporateCreditReportDocument document)
			throws SaveDocumentException, DocumentEncryptionException {
		log.info("Saving Corporate credit report: " + document.getFileName() + 
				" for carId: " + document.getCarId() +
				" and legal name: "+ document.getLegalName());
		return saveDocument((UserAttachmentDocument)document);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String saveDocument(ConsumerCreditReportDocument document)
			throws SaveDocumentException, DocumentEncryptionException {
		log.info("Saving Consumer credit report: " + document.getFileName() + 
				" for carId: " + document.getCarId() + 
				" and legal name: "+ document.getLegalName());
		return saveDocument((UserAttachmentDocument)document);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String saveDocument(CommercialCreditReportDocument document)
			throws SaveDocumentException, DocumentEncryptionException {
		log.info("Saving Commercial credit report: " + document.getFileName() + 
				" for carId: " + document.getCarId() + 
				" and legal name: "+ document.getLegalName());
		return saveDocument((UserAttachmentDocument)document);
	}
	
	private void  populateUserAttachmentWithMetadata(UserAttachmentDocument doc, DocumentObject docObject) {
		doc.setContentType(docObject.getMetadata().getContentType());
		doc.setDocumentContents(docObject.getContent().getBytes());
		doc.setFullPath(docObject.getDocpath().getDocpath());
		
		for (String attrName : doc.getMetadataAttrNames()) {
			Object value;
			try {
				value = docObject.getMetadata().getAttributes().getAttributeValue(attrName).getValue();
				doc.setValueForAttribute(attrName, value);
			} catch (ObjectNotFoundException e) {
				//ignore
			}
		}
	}
	
	private DocumentObject buildDocumentObject(UserAttachmentDocument doc) {
		Docpath docPath = buildPathForDocument(doc);
		DocumentMetadata docMetadata = buildDocMetadataForDocument(doc, docPath); 
		
		DocumentContent docContent = new DocumentContent(doc.getDocumentContents());
		DocumentObject document = new DocumentObject(docMetadata, docContent);
		return document;
	}
	
	private Docpath buildPathForDocument(UserAttachmentDocument doc) {
		String path = getCabinet();
		path += folderForDocType(doc.getDocumentType()) + docSpecificFolder(doc) + yearFolder();
		path += "/" + getUID() + SEPARATOR +doc.getFileName();
		
		return new Docpath(path);
	}
	
	private String getUID() {
		final int nBits = 4;
		// this allows generating about 16 unique ids per ms
		return Long.toString((System.currentTimeMillis() << nBits) | (staticCounter++ & 2^nBits -1)); 
	}
	
	private String getHumanReadableFilename(String fileName) {
		if (fileName.contains(SEPARATOR)) {
			return fileName.substring(fileName.indexOf(SEPARATOR) + 1);
		} else {
			return fileName;
		}
		
	}
	
	private String docSpecificFolder(UserAttachmentDocument doc) {
		String result ="";
		if (doc instanceof CorporateCreditReportDocument) {
			CorporateCreditReportDocument corpDoc = (CorporateCreditReportDocument) doc;
			if (corpDoc.getJurisdictionCode() != null && !corpDoc.getJurisdictionCode().isEmpty()) {
				result = "/" + corpDoc.getJurisdictionCode();
			}
		} 
		return result;
	}
	
	private String yearFolder(){
		return "/" + new GregorianCalendar().get(Calendar.YEAR);
	}
	
	private String folderForDocType(DocumentType type) {
		String result;
		switch (type) {
		case USER_ATTACHMENT:
			result = USER_ATTACHMENT_FOLDER;
			break;
		case COMM_REPORT:
			result = COMM_REPORT_FOLDER;
			break;
		case CONS_REPORT:
			result = CONS_REPORT_FOLDER;
			break;
		case CORP_REPORT:
			result = CORP_REPORT_FOLDER;
			break;
		default:
			result = "";
			break;
		}
		return result;
	}
	
	private DocumentMetadata buildDocMetadataForDocument(UserAttachmentDocument doc, Docpath docPath){
		DocumentMetadata docMetadata = new DocumentMetadata(docPath);
		docMetadata.setType(doc.getDocumentType().code());
		docMetadata.setContentType(doc.getContentType());
		
		// find document specific attributes
		for (String attrName : doc.getMetadataAttrNames()) {
			Object attrValue = doc.getValueForAttribute(attrName);
			if (attrValue != null) {
				if (attrValue instanceof Integer) {
					docMetadata.setAttribute(attrName, (Integer) attrValue);
				} 
				else if (attrValue instanceof Long) {
					Integer val = ((Long)attrValue).intValue();
					docMetadata.setAttribute(attrName, val);
				}
				else {
					docMetadata.setAttribute(attrName, (String) attrValue);
				}
			}
		}
		
		return docMetadata;
	}
	
    /**
     * Encrypt Document
     */
    private void encryptDocument(DocumentObject document) throws DocumentEncryptionException {
	try {
	    if ( m_enableDocumentEncryption ) {
		 document.getContent().setBytes( m_jceCrypto.encrypt(document.getContent().getBytes()) );
	    }
	}
	catch (Exception e) {
	    throw new DocumentEncryptionException( e.getMessage(), e );
	}
    }

private void decryptDocument(DocumentObject document) throws DocumentEncryptionException {
	try {
	    if ( m_enableDocumentEncryption ) {
		document.getContent().setBytes(m_jceCrypto.decrypt(document.getContent().getBytes()));
	    }
	}
	catch (Exception e) {
	    throw new DocumentEncryptionException( e.getMessage(), e );
	}
    }
	
	public void setCabinet(String cabinetName) {
		m_cabinet = cabinetName;
	}
	
	

    private String getCabinet() {
		if (m_cabinet != null && !m_cabinet.isEmpty()) {
			return m_cabinet.startsWith("/")? m_cabinet: "/" + m_cabinet;
		} else {
			return "/";
		}
	}
    
    /**
     * @return Returns the m_enableDocumentEncryption.
     */
    public boolean isEnableDocumentEncryption()
    {
        return m_enableDocumentEncryption;
    }

    /**
     * @param m_enableDocumentEncryption The m_enableDocumentEncryption to set.
     */
    public void setEnableDocumentEncryption(boolean m_enableDocumentEncryption)
    {
        this.m_enableDocumentEncryption = m_enableDocumentEncryption;
    }
    
	/**
     * @return Returns the m_jceCrypto.
     */
    public JceCryptoImpl getJceCrypto()
    {
        return m_jceCrypto;
    }

    /**
     * @param m_jceCrypto The m_jceCrypto to set.
     */
    public void setJceCrypto(JceCryptoImpl m_jceCrypto)
    {
        this.m_jceCrypto = m_jceCrypto;
    }

    /**
	 * For Testing purpose only.
	 */
	public class TestBackdoor {
		public String $yearFolder() {
			return yearFolder();
		}
		
		public String $docSpecificFolder(UserAttachmentDocument doc) {
			return docSpecificFolder(doc);
		}
		
		public Docpath $buildPathForDocument(UserAttachmentDocument doc) {
			return buildPathForDocument(doc);
		}
		
		public String $getUID(){
			return getUID();
		}
		public String $getHumanReadableFilename(String fileName) {
			return getHumanReadableFilename(fileName);
			
		}
		
		
	}

    

}
