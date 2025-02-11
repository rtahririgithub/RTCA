/*
 *  Copyright (c) 2012 TELUS Communications Inc.,
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

import com.telus.framework.content.document.DocumentObject;
import com.telus.framework.crypto.impl.jce.JceCryptoImpl;
import com.telus.framework.crypto.EncryptionUtil;
import com.telus.framework.exception.ObjectNotFoundException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import com.telus.credit.documentum.DocumentDaoImpl;
import com.telus.credit.documentum.DocumentDao;
import com.telus.credit.documentum.domain.ConsumerCreditReportDocument;
import com.telus.credit.documentum.exceptions.DocumentEncryptionException;
import com.telus.credit.documentum.exceptions.RetrieveDocumentException;
import com.telus.credit.documentum.exceptions.SaveDocumentException;

public class TestDocumentum {
    private final static String  SPRING_CONFIG_FILE = "CrdDocumentumApi-spring.xml";
    
    
    private DocumentDao m_documentumDao = null;
    
    public TestDocumentum( ) {
        ClassPathXmlApplicationContext factory = 
            new ClassPathXmlApplicationContext(new String[] {SPRING_CONFIG_FILE}); 
        m_documentumDao = (DocumentDaoImpl) factory.getBean("DocumentumDao");
    }
    
    

    public String saveConsumerCreditReport(String fileName, String contentType, String filePath) throws IOException, SaveDocumentException, DocumentEncryptionException {
	ConsumerCreditReportDocument doc = new ConsumerCreditReportDocument(fileName);
	doc.setContentType(contentType);
	doc.setDocumentContents(getDocContents(filePath));
	doc.setCustomerId( 1234  );
	doc.setCarId( 1235 );
	doc.setCountry( "CA" );
	doc.setLegalName( "Gurbir" );
	doc.setAddressLine( "14972 - 69 A Ave" );
	doc.setCity( "Surrey" );
	
	String docPath = m_documentumDao.saveDocument( doc );
	return docPath;
    }
    
    public ConsumerCreditReportDocument retrieveConsumerCreditReport(String docPath) throws ObjectNotFoundException, RetrieveDocumentException, DocumentEncryptionException {
        ConsumerCreditReportDocument doc = m_documentumDao.retrieveConsumerReportDocument( docPath );
        System.out.println( "Address: " + doc.getAddressLine()
                            + "\n CAR ID: " + doc.getCarId()
                            + "\n City: " + doc.getCity()
                            + "\n Content Type: " + doc.getContentType()
                            + "\n Country: " +  doc.getCountry()
                            + "\n Customer id: " + doc.getCustomerId()
                            + "\n File name: " + doc.getFileName()
                            + "\n Full Path: " + doc.getFullPath()
                            + "\n Contents: " + new String(doc.getDocumentContents(), 0, doc.getDocumentContents().length ) );
        return doc;
    }
    
    private byte[] getDocContents(String filePath) throws IOException {
	InputStream  is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
	assert is !=null: "Ops... couldn't find the file";
	return IOUtils.toByteArray(is);
    }
 

    /**
     *	Main method. triggers the Example runner's runExample method
     */
    public static void main(String[] args)
    {
	try {
	    
	    TestDocumentum testDocumentum = new TestDocumentum( );
	    if ( args.length < 3 ) {
	        System.out.println( "Invalid No. of Args.\n Usage: TestDocumentum <file-name> <content-type> \n e.g. TestEncryption encryptWeak 1234" );
	        System.exit( 1 );
	    }
	    System.out.println("args[0]: " + args[0] );
	    System.out.println("args[1]: " + args[1] );
	    System.out.println("args[2]: " + args[2] );
	    
	    String docPath = testDocumentum.saveConsumerCreditReport( args[0], args[1], args[2] );
	    testDocumentum.retrieveConsumerCreditReport(docPath);
	    
	}
	catch ( Exception e ) {
	    e.printStackTrace();
	}
    }
}
