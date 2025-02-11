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

import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telus.credit.documentum.domain.CommercialCreditReportDocument;
import com.telus.credit.documentum.domain.ConsumerCreditReportDocument;
import com.telus.credit.documentum.domain.CorporateCreditReportDocument;
import com.telus.credit.documentum.domain.UserAttachmentDocument;
import com.telus.credit.documentum.exceptions.RetrieveDocumentException;
import com.telus.credit.documentum.exceptions.SaveDocumentException;
import com.telus.framework.content.document.Docpath;
import com.telus.framework.content.document.DocumentContent;
import com.telus.framework.content.document.DocumentException;
import com.telus.framework.content.document.DocumentMetadata;
import com.telus.framework.content.document.DocumentObject;
import com.telus.framework.content.document.LoginInfo;
import com.telus.framework.content.document.dctm.DocumentManagerImpl;
import com.telus.framework.exception.ObjectNotFoundException;

public class DocumentDaoImplTest {
	private static final String REPOSITORY = "d2archive";
	private static final String USER = "crda";
	private static final String PASS = "crda";
	private static final String CABINET = "/crda";
	private static final String DOC_PATH = "/crda/UnitTest.doc";
	private static final String DOC_FILE_PATH ="UnitTest.doc";
	private static DocumentDaoImpl m_docDao;
	private static DocumentDaoImpl.TestBackdoor backDoor;
	
	@BeforeClass
	public static void setUpOnce() throws DocumentException {
		// login info for documentum
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setRepository(REPOSITORY);
		loginInfo.setUser(USER);
		loginInfo.setPassword(PASS);
		// Setting  document manager
		DocumentManagerImpl docManager = new DocumentManagerImpl();
		docManager.setLoginInfo(loginInfo);
		// creating dao
		m_docDao = new DocumentDaoImpl(docManager);
		m_docDao.setCabinet(CABINET);
		
		backDoor = m_docDao.new TestBackdoor();
		
	}
	
	@Test
	public void testSaveDocumentumDoc() throws SaveDocumentException {
		DocumentObject doc = createTestDocument();
		m_docDao.saveDocument(doc);
	}
	
	@Test
	public void testRetrieveDocumentObject() throws ObjectNotFoundException, RetrieveDocumentException {
		DocumentObject doc = m_docDao.retrieveDocumentObject(DOC_PATH);
		Assert.assertNotNull(doc);
	}
	
	
	@Test
	public void testSaveUserAttachmentDocument() throws IOException, SaveDocumentException, ObjectNotFoundException, RetrieveDocumentException{
		final String docName = "UnitTest2.doc";
		UserAttachmentDocument document = new UserAttachmentDocument(docName);
		document.setContentType("doc");
		document.setDocumentContents(getDocContents());
		String docPath = m_docDao.saveDocument(document);
		Assert.assertTrue(docPath.startsWith("/crda/USER_ATTACHMENTS/2010/"));
		Assert.assertTrue(docPath.contains(docName));
		UserAttachmentDocument saved = m_docDao.retrieveUserAttachment(docPath);
		Assert.assertNotNull(saved);
		Assert.assertEquals(docName, saved.getFileName());
		Assert.assertEquals(docPath, saved.getFullPath());
		Assert.assertEquals(document.getDocumentType().code(), saved.getDocumentType().code());
	}
	
	@Test
	public void testSaveCorporateReport() throws Exception{
		CorporateCreditReportDocument doc = new CorporateCreditReportDocument("UnitTest.doc");
		doc.setContentType("doc");
		doc.setDocumentContents(getDocContents());
		doc.setJurisdictionCode("ON");
		doc.setCarId(123);
		doc.setCity("Vancouver");
		String docpath = m_docDao.saveDocument(doc);
		
		// test retrieval
		CorporateCreditReportDocument corpReport = m_docDao.retrieveCorporateReportDocument(docpath);
		Assert.assertNotNull(corpReport);
		Assert.assertEquals("doc", corpReport.getContentType());
		Assert.assertEquals("ON", corpReport.getJurisdictionCode());
		Assert.assertEquals(123, corpReport.getCarId());
		Assert.assertEquals("Vancouver", corpReport.getCity());
	}
	
	
	@Test
	public void testBuildPathForDoc() {
		UserAttachmentDocument doc = new UserAttachmentDocument("file.ext");
		Docpath docpath = backDoor.$buildPathForDocument(doc);
		Assert.assertNotNull(docpath);
		Assert.assertTrue(docpath.getDocpath().startsWith("/crda/USER_ATTACHMENTS/2010/"));
		Assert.assertTrue(docpath.getDocpath().contains("file.ext"));
		
		// *** Corporate report NO jurisdiction
		doc = new CorporateCreditReportDocument("file.ext");
		docpath = backDoor.$buildPathForDocument(doc);
		Assert.assertNotNull(docpath);
		Assert.assertTrue(docpath.getDocpath().startsWith("/crda/CORP_REPORTS/2010/"));
		Assert.assertTrue(docpath.getDocpath().contains("file.ext"));
		
		// *** Corporate report with jurisdiction
		CorporateCreditReportDocument corpdoc = new CorporateCreditReportDocument("file.ext");
		corpdoc.setJurisdictionCode("BC");
		docpath = backDoor.$buildPathForDocument(corpdoc);
		Assert.assertNotNull(docpath);
		Assert.assertTrue(docpath.getDocpath().startsWith("/crda/CORP_REPORTS/BC/2010/"));
		Assert.assertTrue(docpath.getDocpath().contains("file.ext"));
		
		// *** consumer report
		doc = new ConsumerCreditReportDocument("file.ext");
		docpath = backDoor.$buildPathForDocument(doc);
		Assert.assertNotNull(docpath);
		Assert.assertTrue(docpath.getDocpath().startsWith("/crda/CONS_REPORTS/2010/"));
		Assert.assertTrue(docpath.getDocpath().contains("file.ext"));
		
		// *** commercial report
		doc = new CommercialCreditReportDocument("file.ext");
		docpath = backDoor.$buildPathForDocument(doc);
		Assert.assertNotNull(docpath);
		Assert.assertTrue(docpath.getDocpath().startsWith("/crda/COMM_REPORTS/2010/"));
		Assert.assertTrue(docpath.getDocpath().contains("file.ext"));
	}
	
	@Test
	public void testYearFolder() {
		Assert.assertEquals("/2010", backDoor.$yearFolder());
	}
	
	@Test 
	public void testGetHumanReadableFilename() {
		Assert.assertEquals("My_file.ext", backDoor.$getHumanReadableFilename("114124124_My_file.ext"));
	}
	
	@Test
	public void testDocSpecificFolder() {
		UserAttachmentDocument userAttach = new UserAttachmentDocument("file.ext");
		Assert.assertEquals("", backDoor.$docSpecificFolder(userAttach));
		
		CorporateCreditReportDocument corpDocNoJurisdiction = new CorporateCreditReportDocument("file.ext");
		Assert.assertEquals("", backDoor.$docSpecificFolder(corpDocNoJurisdiction));
		
		CorporateCreditReportDocument corpDocJurisdiction = new CorporateCreditReportDocument("file.ext");
		corpDocJurisdiction.setJurisdictionCode("BC");
		Assert.assertEquals("/BC", backDoor.$docSpecificFolder(corpDocJurisdiction));
		
		ConsumerCreditReportDocument consDoc= new ConsumerCreditReportDocument("file.ext");
		Assert.assertEquals("", backDoor.$docSpecificFolder(consDoc));
	}
	
	@Test
	public void testGetUID(){
		Assert.assertNotNull(backDoor.$getUID());
	}
	
	
//	@Test
	public void loadTestCommercialReports() throws IOException, SaveDocumentException{
		// First commercial report.
		String fileName = "DSResponse-EQX-Comm full.xml" ;
		CommercialCreditReportDocument comReport = new CommercialCreditReportDocument(fileName);
		comReport.setDocumentContents(IOUtils.toByteArray(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("cg_docs/"+ fileName)));
		comReport.setContentType("xml");
		String docpath = m_docDao.saveDocument(comReport);
		Assert.assertTrue(docpath.startsWith("/crda/COMM_REPORTS/2010/"));
		Assert.assertTrue(docpath.contains("DSResponse-EQX-Comm full.xml"));
		
		// Second file
		fileName = "DSResponse-EQX-Comm full.html" ;
		comReport = new CommercialCreditReportDocument(fileName);
		comReport.setDocumentContents(IOUtils.toByteArray(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("cg_docs/"+ fileName)));
		comReport.setContentType("html");
		docpath = m_docDao.saveDocument(comReport);
		Assert.assertTrue(docpath.startsWith("/crda/COMM_REPORTS/2010/"));
		Assert.assertTrue(docpath.contains("DSResponse-EQX-Comm full.html"));
		
		// Third commercial report. 
		fileName = "DSResponse-EQX Comm LOS.xml";
		comReport = new CommercialCreditReportDocument(fileName);
		comReport.setDocumentContents(IOUtils.toByteArray(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("cg_docs/"+ fileName)));
		comReport.setContentType("xml");
		docpath = m_docDao.saveDocument(comReport);
		Assert.assertTrue(docpath.startsWith("/crda/COMM_REPORTS/2010/"));
		Assert.assertTrue(docpath.contains("DSResponse-EQX Comm LOS.xml"));
		
	}
	
//	@Test
	public void loadTestCorporateReports() throws IOException, SaveDocumentException{
		// First corporate report.
		String fileName = "DSResponse-Corp-Quick Search.xml" ;
		CorporateCreditReportDocument corpReport = new CorporateCreditReportDocument(fileName);
		corpReport.setDocumentContents(IOUtils.toByteArray(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("cg_docs/"+ fileName)));
		corpReport.setContentType("xml");
		corpReport.setJurisdictionCode("BC");
		String docpath = m_docDao.saveDocument(corpReport);
		Assert.assertTrue(docpath.startsWith("/crda/CORP_REPORTS/BC/2010/"));
		Assert.assertTrue(docpath.contains("DSResponse-Corp-Quick Search.xml"));
		
		// Second file
		fileName = "DSResponse-Corp-Quick Search.pdf" ;
		corpReport = new CorporateCreditReportDocument(fileName);
		corpReport.setDocumentContents(IOUtils.toByteArray(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("cg_docs/"+ fileName)));
		corpReport.setContentType("pdf");
		corpReport.setJurisdictionCode("BC");
		docpath = m_docDao.saveDocument(corpReport);
		Assert.assertTrue(docpath.startsWith("/crda/CORP_REPORTS/BC/2010/"));
		Assert.assertTrue(docpath.contains("DSResponse-Corp-Quick Search.pdf"));
	}
	
//	@Test
	public void loadTestConsumerReports() throws IOException, SaveDocumentException{
		// First consumer report.
		String fileName = "DSResponse-EQX consumer.xml" ;
		ConsumerCreditReportDocument consReport = new ConsumerCreditReportDocument(fileName);
		consReport.setDocumentContents(IOUtils.toByteArray(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("cg_docs/"+ fileName)));
		consReport.setContentType("xml");
		String docpath = m_docDao.saveDocument(consReport);
		Assert.assertTrue(docpath.startsWith("/crda/CONS_REPORTS/2010/"));
		Assert.assertTrue(docpath.contains("DSResponse-EQX consumer.xml"));
		
		// second consumer report.
		fileName = "DSResponse-EQX consumer.html" ;
		consReport = new ConsumerCreditReportDocument(fileName);
		consReport.setDocumentContents(IOUtils.toByteArray(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("cg_docs/"+ fileName)));
		consReport.setContentType("html");
		docpath = m_docDao.saveDocument(consReport);
		Assert.assertTrue(docpath.startsWith("/crda/CONS_REPORTS/2010/"));
		Assert.assertTrue(docpath.contains("DSResponse-EQX consumer.html"));
		
		// First consumer report.
		fileName = "DSResponse-EQX consumer.pdf" ;
		consReport = new ConsumerCreditReportDocument(fileName);
		consReport.setDocumentContents(IOUtils.toByteArray(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("cg_docs/"+ fileName)));
		consReport.setContentType("pdf");
		docpath = m_docDao.saveDocument(consReport);
		Assert.assertTrue(docpath.startsWith("/crda/CONS_REPORTS/2010/"));
		Assert.assertTrue(docpath.contains("DSResponse-EQX consumer.pdf"));
	}
	

	private DocumentObject createTestDocument() {
		Docpath docPath = new Docpath(DOC_PATH);
		DocumentMetadata docMetadata = new DocumentMetadata(docPath);
		docMetadata.setType("telus_document");
		docMetadata.setContentType("doc");
		
		DocumentContent docContent;
		try {
			docContent = new DocumentContent(getDocContents());
		} catch (IOException e) {
			throw new RuntimeException("Unable to read file", e);
		}
		DocumentObject doc = new DocumentObject(docMetadata, docContent);
		return doc;
	}
	
	private byte[] getDocContents() throws IOException {
		InputStream  is = Thread.currentThread().getContextClassLoader().getResourceAsStream(DOC_FILE_PATH);
		assert is !=null: "Ops... couldn't find the file";
		return IOUtils.toByteArray(is);
	}
}
