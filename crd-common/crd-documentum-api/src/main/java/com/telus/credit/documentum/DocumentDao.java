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

import com.telus.credit.documentum.domain.CommercialCreditReportDocument;
import com.telus.credit.documentum.domain.ConsumerCreditReportDocument;
import com.telus.credit.documentum.domain.CorporateCreditReportDocument;
import com.telus.credit.documentum.domain.UserAttachmentDocument;
import com.telus.credit.documentum.exceptions.RetrieveDocumentException;
import com.telus.credit.documentum.exceptions.SaveDocumentException;
import com.telus.credit.documentum.exceptions.DocumentEncryptionException;
import com.telus.framework.content.document.DocumentObject;
import com.telus.framework.exception.ObjectNotFoundException;

/**
 * 
 * Interface for Documentum DAO, contains functions to retrieve and save documents.
 * 
 * @author x136263
 */
public interface DocumentDao {
	/**
	 * Retrieves document specified by docPath from Documentum.
	 *  
	 * @param docPath full absolute path to the document to be retrieved (e.g. "/cabinet/folder/my_file.xml")
	 * @return DocumentObject representing requested document.
	 * @throws RetrieveDocumentException if operation can not be performed.
	 * @throws ObjectNotFoundException  if document not found at the docpath specified.
	 */
	public DocumentObject retrieveDocumentObject(String docPath) throws RetrieveDocumentException, ObjectNotFoundException, DocumentEncryptionException;
	
	/**
	 * Saves Documentum-specific object to Documentum
	 *  
	 * @param document document to save.
	 * @throws SaveDocumentException  if operation can not be performed. 
	 */
	public String saveDocument(DocumentObject document) throws SaveDocumentException, DocumentEncryptionException;
	
	/**
	 * Retrieves user attachment document from Documentum.
	 *  
	 * @param docpath full path to the document.
	 * @return object of UserAttachmentDocument type.
	 * @throws RetrieveDocumentException if operation cannot be done.
	 * @throws ObjectNotFoundException  if document is not found in Documentum. 
	 */
	public UserAttachmentDocument retrieveUserAttachment(String docpath) 
	throws RetrieveDocumentException, ObjectNotFoundException, DocumentEncryptionException;
	
	/**
	 * Retrieves Corporate credit report from Documentum on the path specified.
	 *  
	 * @param docpath full path to the document.
	 * @return object of CorporateCreditReportDocument type.
	 * @throws RetrieveDocumentException if operation cannot be done.
	 * @throws ObjectNotFoundException  if document is not found in Documentum. 
	 */
	public CorporateCreditReportDocument retrieveCorporateReportDocument(String docpath) 
	throws RetrieveDocumentException, ObjectNotFoundException, DocumentEncryptionException;
	
	/**
	 * Retrieves Consumer credit report from Documentum.
	 *  
	 * @param docpath full path to the document.
	 * @return object of ConsumerCreditReportDocument type.
	 * @throws RetrieveDocumentException if operation cannot be done.
	 * @throws ObjectNotFoundException  if document is not found in Documentum. 
	 */
	public ConsumerCreditReportDocument retrieveConsumerReportDocument(String docpath) 
	throws RetrieveDocumentException, ObjectNotFoundException, DocumentEncryptionException;
	
	/**
	 * Retrieves Commercial credit report from Documentum.
	 *  
	 * @param docpath full path to the document.
	 * @return object of CommercialCreditReportDocument type.
	 * @throws RetrieveDocumentException if operation cannot be done.
	 * @throws ObjectNotFoundException  if document is not found in Documentum. 
	 */
	public CommercialCreditReportDocument retrieveCommercialReportDocument(String docpath) 
	throws RetrieveDocumentException, ObjectNotFoundException, DocumentEncryptionException;
	
	/**
	 * Saves given User attachment document to Documentum.
	 *  
	 * @param document document to save.
	 * @return full path to the document saved.
	 * @throws SaveDocumentException   if operation cannot be done.
	 */
	public String saveDocument(UserAttachmentDocument document) throws SaveDocumentException, DocumentEncryptionException;
	
	/**
	 * Saves given Corporate credit report to Documentum.
	 *  
	 * @param document document to save.
	 * @return full path to the document saved.
	 * @throws SaveDocumentException   if operation cannot be done.
	 */
	public String saveDocument(CorporateCreditReportDocument document) throws SaveDocumentException, DocumentEncryptionException;
	
	/**
	 * Saves given Consumer credit report to Documentum.
	 *  
	 * @param document document to save.
	 * @return full path to the document saved.
	 * @throws SaveDocumentException   if operation cannot be done.
	 */
	public String saveDocument(ConsumerCreditReportDocument document) throws SaveDocumentException, DocumentEncryptionException;
	
	/**
	 * Saves given Commercial credit report to Documentum.
	 *  
	 * @param document document to save.
	 * @return full path to the document saved.
	 * @throws SaveDocumentException   if operation cannot be done.
	 */
	public String saveDocument(CommercialCreditReportDocument document) throws SaveDocumentException, DocumentEncryptionException;
	
}
