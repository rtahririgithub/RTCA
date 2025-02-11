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

import com.telus.framework.crypto.impl.jce.JceCryptoImpl;
import com.telus.framework.crypto.EncryptionUtil;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;

public class TestEncryption {
    private final static String  SPRING_CONFIG_FILE = "document-encryptor-spring.xml";
    
    private JceCryptoImpl m_jceCrypto = null;

    public TestEncryption( ) {
	ClassPathXmlApplicationContext factory = 
		new ClassPathXmlApplicationContext(new String[] {SPRING_CONFIG_FILE}); 
	   m_jceCrypto = (JceCryptoImpl) factory.getBean("jceCryptoImpl");
    }
 
    public String encryptWeak(String input) {
	return EncryptionUtil.encrypt( input );
    }

    public String decryptWeak(String input) {
	return EncryptionUtil.decrypt( input );
    }

    public String encryptStrong(String input) throws Exception {
        byte[] encryptedBytes = m_jceCrypto.encrypt( input.getBytes() );
        return new String(encryptedBytes, 0, encryptedBytes.length );
    }
    
    public String decryptStrong(String input) throws Exception {
        byte[] decryptedBytes = m_jceCrypto.decrypt( input.getBytes() );
        return new String(decryptedBytes, 0, decryptedBytes.length );
    }

    /**
     *	Main method. triggers the Example runner's runExample method
     */
    public static void main(String[] args)
    {
	try {
	    
	    TestEncryption testEncryption = new TestEncryption( );
	    if ( args.length < 2 ) {
	        System.out.println( "Invalid No. of Args.\n Usage: TestEncryption <type> <value> \n e.g. TestEncryption encryptWeak 1234" );
	        System.exit( 1 );
	    }
	    String encryptForChangeIt = testEncryption.encryptWeak("changeit");
	    System.out.println("Encrypt for changeit: '" + encryptForChangeIt + "'");
	    System.out.println("Decrypt for changeit: " + testEncryption.decryptWeak(encryptForChangeIt) );
	    
	    String encryptRTCA = testEncryption.encryptWeak("rtca");
        System.out.println("Encrypt for changeit: '" + encryptRTCA + "'");
        System.out.println("Decrypt for changeit: " + testEncryption.decryptWeak(encryptRTCA) );
	    if ( args[0].equalsIgnoreCase( "encryptWeak" ) ) {
	        System.out.println( "Result: " + testEncryption.encryptWeak( args[1] ) );
	    } else if ( args[0].equalsIgnoreCase( "decryptWeak" ) ) {
            System.out.println( "Result: " + testEncryption.decryptWeak( args[1] ) );
        } else if ( args[0].equalsIgnoreCase( "encryptStrong" ) ) {
            System.out.println( "Result: " + testEncryption.encryptStrong( args[1] ) );
        } else if ( args[0].equalsIgnoreCase( "decryptStrong" ) ) {
            System.out.println( "Result: " + testEncryption.decryptStrong( args[1] ) );
        }
	}
	catch ( Exception e ) {
	    e.printStackTrace();
	}
    }
}
