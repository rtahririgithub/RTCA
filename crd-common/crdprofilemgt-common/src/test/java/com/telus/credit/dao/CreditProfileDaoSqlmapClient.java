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

package com.telus.credit.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

//import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.telus.credit.domain.CreditIDCard;
import com.telus.credit.service.dto.CreditProfileDto;



import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.framework.exception.PersistenceException;


public class CreditProfileDaoSqlmapClient {

    private CreditProfileDao m_creditProfileDao;
    
    public static void main(String[] args)
    {
        System.out.println("\nStart .....");
        
        /*// initialize the Spring Framework
        ClassPathXmlApplicationContext factory = 
            new ClassPathXmlApplicationContext(new String[] {"test-crd-dao-sqlmap-spring.xml"}); 
        
        // get the service bean
        CreditProfileDaoSqlmapClient client = (CreditProfileDaoSqlmapClient)factory.getBean("creditProfileDaoSqlmapClient");
           
        client.testGetCreditProfileDtoByCreditProfileAndCustomerIds();	// Passed.
*/        
    }

    private void testGetCreditProfileDtoByCreditProfileAndCustomerIds() 
    {

           //Long customerId1=new Long(10001728);
            //Long customerId2=new Long(10001729);
            
            List<Long> customerIds=new ArrayList<Long>();
            //customerIds.add(customerId1);
            //customerIds.add(customerId2);
            
            Date birthDt=new Date();
            birthDt.setYear(58);
            birthDt.setMonth(9);
            birthDt.setDate(4);
            
           /*XMLGregorianCalendar cal=CreditProfileDaoSqlmapClient.asXMLGregorianCalendar(birthDt);
            XMLGregorianCalendar cal=CreditProfileDaoSqlmapClient.getXMLGregorianCalendarNow();
            System.out.println("year is"+cal.getYear());
            System.out.println("month is"+cal.getMonth());
            System.out.println("day is"+cal.getDay());
            */
            
            CreditIDCard sinIdCard=null;//new CreditIDCard();
            CreditIDCard dlIdCard=new CreditIDCard();
            dlIdCard.setIdNumber("DPoig>oS");
            dlIdCard.setIdTypeCode("DL");
            dlIdCard.setProvinceCode("BC");
            CreditIDCard hcIdCard=null;//new CreditIDCard();
            CreditIDCard pspIdCard=null;//new CreditIDCard();
            CreditIDCard prvIdCard=null;//new CreditIDCard();
            
            Integer numOfIds=new Integer(1);
    	    System.out.println("test Starting ....\n");
            
    	  CreditProfileDto[] creditProfileDtos=
    	    	m_creditProfileDao.getCreditProfileDtoByCreditProfileAndCustomerIds
           (customerIds, birthDt, numOfIds,sinIdCard, dlIdCard, hcIdCard, pspIdCard, prvIdCard);
            
    	   System.out.println("\nNumber="+ creditProfileDtos.length);
    	   for (int i = 0; i < creditProfileDtos.length; i++) 
   	      {
    		   System.out.println("\n"+creditProfileDtos[i].getCustomerId());
    		   System.out.println("\n"+creditProfileDtos[i].getCreditProfile().getBirthdate());
   	       }
               
            System.out.println("\nEnd!");
       

    }
    private static DatatypeFactory df = null;
    static {
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException dce) {
            throw new IllegalStateException(
                "Exception while obtaining DatatypeFactory instance", dce);
        }
    }  

    /**
     * Converts a java.util.Date into an instance of XMLGregorianCalendar
     *
     * @param date Instance of java.util.Date or a null reference
     * @return XMLGregorianCalendar instance whose value is based upon the
     *  value in the date parameter. If the date parameter is null then
     *  this method will simply return null.
     */
    public static XMLGregorianCalendar asXMLGregorianCalendar(java.util.Date date) {
        if (date == null) {
            return null;
        } else {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(date.getTime());
            return df.newXMLGregorianCalendar(gc);
        }
    }
    
    public static XMLGregorianCalendar getXMLGregorianCalendarNow() 

{
GregorianCalendar gregorianCalendar = new GregorianCalendar();

XMLGregorianCalendar now = 
   df.newXMLGregorianCalendar(gregorianCalendar);
return now;
}



    

	/**
	 * @return the m_creditProfileDao
	 */
	public CreditProfileDao getCreditProfileDao() {
		return m_creditProfileDao;
	}

	/**
	 * @param m_creditProfileDao the m_creditProfileDao to set
	 */
	public void setCreditProfileDao(CreditProfileDao creditProfileDao) {
		 m_creditProfileDao = creditProfileDao;
	}

}
