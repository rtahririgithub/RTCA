/*
 * Copyright (c) 2004 TELUS Communications Inc., All Rights Reserved.
 * 
 * This document contains proprietary information that shall be distributed or
 * routed only within TELUS, and its authorized clients, except with written
 * permission of TELUS.
 * 
 * $Id$
 */

package com.telus.credit.batch.merge;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.framework.batch.BatchContext;
import com.telus.framework.batch.Module;
import com.telus.framework.batch.exception.ModuleException;
import com.telus.framework.batch.io.LineNumberAwareReader;
import com.telus.framework.batch.io.PositionAwareWriter;
import com.telus.framework.crypto.EncryptionUtil;
import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.framework.security.SecurityContext;

import com.telus.credit.batch.dao.CreditProfileCustomerMapDao;
import com.telus.credit.batch.util.CreditProfileCustomerMap;
import com.telus.credit.batch.util.DateUtil;
import com.telus.credit.entprflmgt.domain.ConsumerCreditIdentification;
import com.telus.credit.entprflmgt.domain.ConsumerCreditProfileInfo;
import com.telus.credit.entprflmgt.domain.Identification;
import com.telus.credit.wlnprfldmgt.domain.DriverLicense;
import com.telus.credit.wlnprfldmgt.domain.PersonalInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v6.AuditInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.exceptions_v3.FaultExceptionDetailsType;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditprofilemanagementsvc_2.EnterpriseCreditProfileManagementServicePortType;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditprofilemanagementsvc_2.ServiceException;

/**
 * @author 
 */
public class CreditIDUpdateBatch  implements Module
{
    private static final Log log = LogFactory.getLog( CreditIDUpdateBatch.class );

    private String m_inputFile;
    private String m_outputFile;
    private int m_failurePoint;
    private LineNumberAwareReader m_reader;
    private PositionAwareWriter m_writer;
    private PrintWriter m_pWriter;
    private String m_inputStr;
    private int m_counter;

    private AuditInfo m_aud;
    
    private EnterpriseCreditProfileManagementServicePortType m_enterpriseCreditProfileManagementService;

    private String m_userId;
    private String m_sourceId;
    
    private CreditProfileCustomerMapDao m_profileCustomerMapDao;
    
    public static final String PRIMARY_KEY="PRI";
    
    private static final String S_CRDB_BATCH_USER ="CRDB-BATCH";
    private static final String S_CRDB_BATCH_SOURCE ="CRDB-BATCH-MERGE";
    private static final String INPUT_FILE_LINE_NO = "INPUT_FILE_LINE_NO";
    private static final String OUTPUT_FILE_POSITION = "OUPUT_FILE_POSITION";
   
    private static final int S_PID_START = 0;
    private static final int S_PID_END = 20;
    private static final int S_DL_START = 20;
    private static final int S_DL_END = 70;
    private static final int S_PV_START = 70;
    private static final int S_PV_END = 80;
    private static final int S_SIN_START = 80;
    private static final int S_SIN_END = 130;
    private static final int S_DOB_START = 130;

    /*
     * (non-Javadoc)
     *
     * @see com.telus.framework.batch.Module#execute()
     */
    public void execute() throws ModuleException
    {
        ConsumerCreditProfileInfo ccpInfo = new  ConsumerCreditProfileInfo();
        ConsumerCreditIdentification wsIdCard = new ConsumerCreditIdentification();;
        PersonalInfo pInfo = new PersonalInfo();        
        DriverLicense dr =null;
        
        String dl = "";
        String provcd = "";
        String sin = "";
        String dob= "";
        String inputStr = m_inputStr;
      
        boolean creditChg =false;
        long profileId = 0;
        if(inputStr != null && inputStr.length()>129)
        {
            
            String pid = inputStr.substring(S_PID_START,S_PID_END);
            profileId =Long.parseLong(pid.trim());
           
            String udl = inputStr.substring(S_DL_START,S_DL_END).trim();           
            dl = decrypt(udl);
            
            String upv= inputStr.substring(S_PV_START,S_PV_END);          
            provcd = upv.trim();

            String usin = inputStr.substring(S_SIN_START,S_SIN_END).trim();
            sin = decrypt(usin);

            if(inputStr.length()>130)
            {
                dob =inputStr.substring(S_DOB_START).trim();
                
            }
                
            if(dl != null && !dl.trim().equalsIgnoreCase(""))
            {
                dr = new DriverLicense();
                dr.setDriverLicenseNum(dl);               
                dr.setProvinceCd(provcd);               
                wsIdCard.setDriverLicense( dr );
                creditChg = true;

            }
            if(sin !=null && !sin.trim().equalsIgnoreCase(""))
            {
                wsIdCard.setSin(sin);
                creditChg = true;
               
            }
            if(creditChg)
            {
                ccpInfo.setCreditIdentification(wsIdCard);
            }

            if(dob !=null && !dob.trim().equalsIgnoreCase(""))
            {
                Date date = DateUtil.convertStringToDate(dob,DateUtil.SIMPLE_DATE_FORMAT_YYYY_MM_DD);
               // pInfo.setBirthDate(DateUtil.asXMLGregorianCalendar(date));
                ccpInfo.setPersonalInfo(pInfo);
            } 
                        
            try
            {
                log.debug("Processing Customer profile"+profileId+" ......");
                    
                List customerList=new ArrayList();
                customerList=m_profileCustomerMapDao.getCustomerListByProfileId(profileId+"",PRIMARY_KEY);  
       
                CreditProfileCustomerMap resultTempMap = new CreditProfileCustomerMap();
                
            
                Identification idt = new Identification();
                for (Iterator iterator = customerList.iterator(); iterator.hasNext();)
                {
                     resultTempMap = (CreditProfileCustomerMap)iterator.next();
                     if(resultTempMap !=null)
                     {      
                         Long cust = new Long( resultTempMap.getCustomerId());
                         
                         if(cust != null)
                         {
                             idt.setCustomerId(cust.longValue());
                             ccpInfo.setIdentification(idt);
                             m_enterpriseCreditProfileManagementService.updateCreditProfile(ccpInfo, m_aud, null, null, null, null, null, null);
                         }
                     }
                 }

                    log.debug("Updating Credit Profile "+profileId+" succcessfully");
             }catch (ObjectNotFoundException e)
             {                 
                    log.error("Updating EnterPrise remote profile id problem "+profileId+",profileId failed, enterprise web service ObjectNotFoundException. Erreo code:"+e.getErrorCode() + ". Error Msg:" +e.getMessage());
                    m_pWriter.println("** Profile "+profileId+",[Credit Profile "+profileId+"] enterprise web service ObjectNotFoundException. Erreo code:"+e.getErrorCode() + ". Error Msg:" +e.getMessage());
                  
                   //  throw new ModuleException( e );
              }
              catch (ServiceException e)
              {
            	  FaultExceptionDetailsType faultInfo = e.getFaultInfo();
                    log.error("Updating Credit Profile  id problem "+profileId+",[Credit Profile "+profileId+"] failed, enterprise web service ServiceException. Error code:"+
                            (faultInfo != null ? faultInfo.getErrorCode() : "") + ". Error Msg:" +(faultInfo != null ? faultInfo.getErrorMessage() :""));
                    m_pWriter.println("** Profile "+profileId+",[Credit Profile "+profileId+"]  credit ID could NOT be updated because of  enterprise web service ServiceException. Error code:"+
                            (faultInfo != null ? faultInfo.getErrorCode() : "") + ". Error Msg:" +(faultInfo != null ? faultInfo.getErrorMessage() :""));
                 
                     throw new ModuleException( e );
              }
               catch (Exception e)
               {
                   log.error("Updating Credit Profile  id problem "+profileId+",[Credit Profile "+profileId+"] failed." + e, e );
                   m_pWriter.println("** Profile "+profileId+",[Credit Profile "+profileId+"]  credit ID could NOT be updated because of  enterprise web service ServiceException.");
                throw new ModuleException( e );
             }
          }

          m_counter++;
          //simulate failure...
          if ( m_failurePoint != 0 && m_counter >= m_failurePoint ) {
              throw new ModuleException(
                    "Simulated Exception. Failure point reached" );
          }
    }


    /*
     * (non-Javadoc)
     *
     * @see com.telus.framework.batch.Module#hasNext()
     */
    public boolean hasNext() throws ModuleException
    {
        try {
            m_inputStr = m_reader.readLine();
            
        }
        catch ( IOException ioe ) {
            throw new ModuleException( ioe );
        }
        //no more records to process...
        if ( m_inputStr == null ) {
            //execute method will not be called
            return false;
        }

        //call execute method
        return true;

    }


    /*
     * (non-Javadoc)
     *
     * @see com.telus.framework.batch.Module#launch(com.telus.framework.batch.BatchContext)
     */

    public void launch(BatchContext batchContext) throws ModuleException
    {
        try {
            log.debug( "Job instance ID is " + batchContext.getJobId() );

            //open writer in append mode on 'RESTART'
            boolean append = MODE_RESTART.equals( batchContext.getStartMode() );
            m_reader = new LineNumberAwareReader( m_inputFile );
            
            m_writer = new PositionAwareWriter( m_outputFile, append );
            m_pWriter = new PrintWriter( m_writer );

            m_userId=(SecurityContext.getPrincipal()==null)? S_CRDB_BATCH_USER: SecurityContext.getPrincipal().getName();
              // web service,
            Integer source = SecurityContext.getSystemSourceId();
             //m_userId= SecurityContext.getPrincipal().getName(); 
             //m_userId= SecurityContext.getPrincipal().getName();
            if(source != null)
               m_sourceId =  source.toString();                          
             m_aud = new AuditInfo();
             
             m_aud.setUserId(m_userId);
             m_aud.setOriginatorApplicationId(m_sourceId);
             

             
             // ((EnterpriseCreditProfileManagementServicePortType_Stub)m_wsPort)._setProperty(javax.xml.rpc.Stub.USERNAME_PROPERTY, m_username);
             // ((EnterpriseCreditProfileManagementServicePortType_Stub)m_wsPort)._setProperty(javax.xml.rpc.Stub.PASSWORD_PROPERTY, m_password);
            //  System.out.println("by pass security----------");
            
              
             // System.setProperty("weblogic.webservice.verbose", "true" );
             // System.setProperty("weblogic.wsee.verbose", "true" );
              
            // System.out.println("ping----------"); //TODO, will be removed before PT testing
            // m_wsPort.ping();  //TODO, will be removed with all the system.out before PT testing
             //System.out.println("ping return----------"); //TODO, will be removed before PT testing

        }
        catch ( FileNotFoundException fnfe ) {
            throw new ModuleException( fnfe );
        }catch ( Exception e)
        {
            log.error(" Error in initializing web service client! ", e);
               throw new ModuleException(e);
         }
    }


    /*
     * (non-Javadoc)
     *
     * @see com.telus.framework.batch.Module#onExit(boolean)
     */
    public int onExit(boolean success) throws ModuleException
    {
        try {
            //close writer & reader.
            m_writer.close();
            m_reader.close();
        }
        catch ( IOException e ) {
            //ignore...
        }
        if ( success ) {
            return RETURN_CODE_SUCCESS;
        }

        return RETURN_CODE_FAILURE;

    }


    /*
     * (non-Javadoc)
     *
     * @see com.telus.framework.batch.Module#restoreState(java.util.Properties)
     */
    public void restoreState(Properties state) throws ModuleException
    {
        String str = state.getProperty( INPUT_FILE_LINE_NO );
        int inLineNo = (str != null) ? Integer.parseInt( str ) : 0;
        str = state.getProperty( OUTPUT_FILE_POSITION );
        long outPosition = (str != null) ? Long.parseLong( str ) : 0;
        try {
            m_reader.reposition( inLineNo );
            m_writer.reposition( outPosition );
        }
        catch ( IOException ioe ) {
            throw new ModuleException( ioe );
        }

    }


    /*
     * (non-Javadoc)
     *
     * @see com.telus.framework.batch.Module#getStateForRestart()
     */
    public Properties getStateForRestart() throws ModuleException
    {
        Properties state = new Properties();
        try {
              state.setProperty( INPUT_FILE_LINE_NO, m_reader.getLineNumber()
                    + "" );
              state.setProperty( OUTPUT_FILE_POSITION, m_writer.getPosition()
                + "" );
        }
        catch ( IOException ioe ) {
            throw new ModuleException( ioe );
        }
        return state;
    }


    /*
     * (non-Javadoc)
     *
     * @see com.telus.framework.batch.Module#getSummary()
     */
    public Properties getSummary() throws ModuleException
    {
        Properties summary = new Properties();
        summary.put( "INPUT_RECORD_COUNT", m_counter + "" );
        summary.put( "OUTPUT_RECORD_COUNT", m_counter + "" );
        return summary;
    }


    /**
     * @param failurePoint
     *            The failurePoint to set.
     */
    public void setFailurePoint(int failurePoint)
    {
        m_failurePoint = failurePoint;
    }


    /**
     * @param inputFile
     *            The inputFile to set.
     */
    public void setInputFile(String inputFile)
    {
        m_inputFile = inputFile;
    }

    /**
     * @param outputFile
     *            The outputFile to set.
     */
    public void setOutputFile(String outputFile)
    {
        m_outputFile = outputFile;
    }


    /**
     * @param profileCustomerMapDao The profileCustomerMapDao to set.
     */
    public void setProfileCustomerMapDao(
            CreditProfileCustomerMapDao profileCustomerMapDao) {
        m_profileCustomerMapDao = profileCustomerMapDao;
    }

    /**
     * Decrypts the specifed string using the telus encryption framework.
     */
    private String decrypt(String string) 
    {
       
       if(string == null || string.length() <= 0) return string;
      
       return EncryptionUtil.decrypt(string);
    }

    
    public CreditIDUpdateBatch(EnterpriseCreditProfileManagementServicePortType clientServiceInterface) {
        this.m_enterpriseCreditProfileManagementService = clientServiceInterface;
    }

/*  public void setEnterpriseCreditProfileManagementService(
            EnterpriseCreditProfileManagementServiceV11PortType enterpriseCreditProfileManagementService) {
        m_enterpriseCreditProfileManagementService = enterpriseCreditProfileManagementService;
    }
    */
        
}

