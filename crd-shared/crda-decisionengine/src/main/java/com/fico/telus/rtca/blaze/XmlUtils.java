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

package com.fico.telus.rtca.blaze;

import javax.xml.bind.JAXBContext;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fico.telus.blaze.creditAsessment.CancelDepositCreditAssessmentRequest;
import com.fico.telus.blaze.creditAsessment.ExistingCustomerCreditAssessmentRequest;
import com.fico.telus.blaze.creditAsessment.ManualInquiryCreditAssessmentRequest;
import com.fico.telus.blaze.creditAsessment.MonthlyUDCreditAssessmentRequest;
import com.fico.telus.blaze.creditAsessment.NewCustomerCreditAssessmentRequest;
import com.fico.telus.blaze.creditAsessment.OverrideCreditAssessmentRequest;
import com.fico.telus.blaze.creditCommon.CreditAssessmentResult;
import com.fico.telus.blaze.creditSimulator.SimulatorCreditBureauRequest;
import com.fico.telus.blaze.depositCalculator.DepositCalculatorRequest;
import com.fico.telus.blaze.depositCalculator.DepositCalculationResult;
import com.fico.telus.blaze.depositCalculator.DepositEquipmentRequest;
import com.fico.telus.blaze.depositCalculator.DepositEquipmentResult;

import java.io.InputStream;
import java.io.StringWriter;


/**
 * Utility class that helps to deal with converting JAXB-annotated POJOs to XML.
 */
public final class XmlUtils {
	
	private static final Log log = LogFactory.getLog(XmlUtils.class);
	
	private static final JAXBContext m_context = initContext();

    private static JAXBContext initContext() {
    	try {
    		return JAXBContext.newInstance
    				(
    				CancelDepositCreditAssessmentRequest.class,
    				ExistingCustomerCreditAssessmentRequest.class,
    				ManualInquiryCreditAssessmentRequest.class,
    				MonthlyUDCreditAssessmentRequest.class,
    				NewCustomerCreditAssessmentRequest.class,
    				OverrideCreditAssessmentRequest.class,
    				SimulatorCreditBureauRequest.class,
    				DepositCalculatorRequest.class,
    				DepositCalculationResult.class,
    				DepositEquipmentRequest.class,
    				DepositEquipmentResult.class,
    				CreditAssessmentResult.class
    				);
    										
    	}
    	catch (JAXBException e) {
    		log.error("Unable to initialize jaxb context:" + e, e );
    	}
    	return null;
    }
    
    private XmlUtils(){
        //private constructor to prevent instantiation.
    }
    
    

    /**
     * Converts given JAXB-annotated POJO to an XML represented as String. 
     * @param obj - JAXB annotated object to be converted to xml.
     * @return result - xml as String representing given java object.
     */
    public static String convertToXml(Object obj) {
        if (obj == null) { throw new IllegalArgumentException("obj cannot be null."); }

        final StringWriter sw= new StringWriter();
        try {
        	if ( m_context != null) {
        		Marshaller marshaller = m_context.createMarshaller();
        		marshaller.marshal(obj, sw);
        		return sw.toString();
        	}
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
    
	public static Object convertToObj(InputStream is) {
		try {
			Unmarshaller unmarshaller = m_context.createUnmarshaller();
			if (unmarshaller != null) {
				return unmarshaller.unmarshal(is);
			}
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}

		return null;
	}
}
