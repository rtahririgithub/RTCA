/*
 * Copyright (c) 2005 TELUS Communications Inc., All Rights Reserved.
 *
 * This document contains proprietary information that shall be distributed or
 * routed only within TELUS, and its authorized clients, except with written
 * permission of TELUS.
 *
 */
package com.telus.crd.assessment.batch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.framework.batch.BatchContext;
import com.telus.framework.batch.Module;
import com.telus.framework.batch.exception.ModuleException;
import com.telus.framework.batch.io.LineNumberAwareReader;
import com.telus.framework.batch.io.PositionAwareWriter;
import com.telus.framework.security.SecurityContext;
import com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.ConsumerCustomerManagementServicePortType;
import com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.PolicyException;
import com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.ServiceException;
import com.telus.tmi.xmlschema.xsd.customer.customer.customersubdomain_v3.Customer;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v4.AuditInfo;



/**
 *
 * <p>
 * <b>Description: </b>  This function calls customer mgmt services to updates credit value according to review result from previous job.
 * </p>
 * <p>
 * <b>Design Observations: </b>
 * </p>
 * <ul>
 * <li>use existing service objects as local objects in order to reuse the business logic encapsulated therein</li>
 * </ul>
 *
 * <p>
 * <br>
 * <b>Issues: </b>
 * </p>
 * <ul>
 * <li>[Issues]</li>
 * </ul>
 *
 * <p>
 * <br>
 * <b>Revision History: </b>
 * </p>
 * <table border="1" width="100%">
 * <tr>
 * <th width="15%">Date</th>
 * <th width="15%">Revised By</th>
 * <th width="55%">Description</th>
 * <th width="15%">Reviewed By</th>
 * </tr>
 * <tr>
 * <td width="15%">&nbsp;</td>
 * <td width="15%">&nbsp;</td>
 * <td width="55%">&nbsp;</td>
 * <td width="15%">&nbsp;</td>
 * </tr>
 * </table>
 *
 * @author
 *
 */
public class UpdateCreditValueModule implements Module
{
	private static final Log log = LogFactory.getLog( UpdateCreditValueModule.class );

    private String m_inputFile;
    private String m_outputFile;
    private String m_outputFileSuc;
    private String m_outputFileLeftover;
    private int m_failurePoint;
    private LineNumberAwareReader m_reader;
    private PositionAwareWriter m_writer;
    private PositionAwareWriter m_writer_succ;
    private PositionAwareWriter m_writer_leftover;
    private PrintWriter m_pWriter;
    private PrintWriter m_pWriter_succ;
    private PrintWriter m_pWriter_leftover;
    private String m_inputStr;
    private int m_counter;


    private long m_customerId;

    private AuditInfo m_aud;

    private String m_userId;
    private Integer m_sourceId;
    
    private Customer m_customer;
    private String m_creditValue;

    private ConsumerCustomerManagementServicePortType m_consumerCustomerManagementService;

    private static final String S_CRDB_BATCH_USER ="CRDA-BATCH";
    private static final String INPUT_FILE_LINE_NO = "INPUT_FILE_LINE_NO";
    private static final String OUTPUT_FILE_POSITION = "OUPUT_FILE_POSITION";
    private static final String OUTPUT_FILE_POSITION_SUCC = "OUPUT_FILE_POSITION_SUCC";
    private static final String OUTPUT_FILE_POSITION_LEFT = "OUPUT_FILE_POSITION_LEFT";

    public UpdateCreditValueModule(ConsumerCustomerManagementServicePortType clientServiceInterface) {
		this.m_consumerCustomerManagementService = clientServiceInterface;
    }


    /*
     * (non-Javadoc)
     *
     * @see com.telus.framework.batch.Module#execute()
     */
    public void execute() throws ModuleException
    {
       	if(m_inputStr != null )
		{
       		String[] result = m_inputStr.split("[|]");
       		if(result.length>=3)
       		{
       		    String customer =result[0]; 
                String cv = result[1]; 
                String isUpdate = result[2];
            
       		
                if(customer !=null && cv != null)
                {
            	    //log.info("Processing string "+m_inputStr+" ......");
            	    //log.info("Processing customer "+customer+" ......");
            	    //log.info("Processing credit value "+cv+" ......");
                    m_customerId=Long.parseLong(customer.trim());
                    m_creditValue=cv.trim();
                    //log.info("Processing customer m_customerId:"+m_customerId+" ......");
            	    //log.info("Processing customer _creditValue:"+m_creditValue+" ......");
                    //web service to update credit profile
                    try
                    {
                        //log.debug("Processing Customer "+m_customerId+" ......");
                        Long customerid = new Long(m_customerId);
            
                        if(customerid != null && m_creditValue !=null && !m_creditValue.equalsIgnoreCase("")  )
                        {
               
                        	if( "Y".equalsIgnoreCase(isUpdate))
                            {
                                m_aud.setTimestamp(new Date());
                                
                                m_customer = m_consumerCustomerManagementService.getCustomer(customerid);
                                String oldCreditValue = m_customer.getCreditValueCode();
                                if(!m_creditValue.equalsIgnoreCase(oldCreditValue))
                                {
                	                m_customer.setCreditValueCode(m_creditValue);
                	                m_consumerCustomerManagementService.updateCustomer(m_customer, m_aud) ; 
                	                m_pWriter_succ.println(m_customerId+"|"+m_creditValue);
                	               // log.info("update request is sent to customer Management web service with customer id:"+m_customer.getCustomerId());
                   
                                } else
                                {
                        	        m_pWriter_succ.println(m_customerId+"|"+m_creditValue);
                 	                // log.info("update request is not needed to customer id:"+m_customer.getCustomerId());
                 	            
                                }
                            } else
                            {
                            	
                        	    m_pWriter_succ.println(m_customerId+"|"+m_creditValue);
                            }
                        }
                     
                    
                 }
                 catch (PolicyException e)
                 {
                	 m_pWriter_leftover.println(m_customerId+"|"+m_creditValue);
       	           
        	         com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException faultInfo = e.getFaultInfo();
                     log.error("Updating Credit Value remote customer id problem "+m_customerId+",[Credit Value updating failed, Customer Management web service PolicyException. Error code:"+
        	             (faultInfo != null ? faultInfo.getErrorCode() : "") + ". Error Msg:" +(faultInfo != null ? faultInfo.getErrorMessage() :"") );
                     m_pWriter.println("** Customer "+m_customerId+", whose credit value could NOT be updated because of  Customer Management Service PolicyException. Error code:"+
            		   (faultInfo != null ? faultInfo.getErrorCode() : "") + ". Error Msg:" + (faultInfo != null ? faultInfo.getErrorMessage() :""));
                     
                 } catch (ServiceException e)
                 {
                	 m_pWriter_leftover.println(m_customerId+"|"+m_creditValue);
         	           
        	         com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException faultInfo = e.getFaultInfo();
                     log.error("Updating Credit Value remote customer id problem"+m_customerId+", failed, customer management web service Exception. Error code:"+
            		     (faultInfo != null ? faultInfo.getErrorCode() : "") + ". Error Msg:" + (faultInfo != null ? faultInfo.getErrorMessage() :""));
                     m_pWriter.println("** Customer "+m_customerId + ", whose credit value could NOT be updated because of Customer Management Service ServiceException. Error code:"+
            		 (faultInfo != null ? faultInfo.getErrorCode() : "") + ". Error Msg:" + (faultInfo != null ? faultInfo.getErrorMessage() :"") );
                   
                     
                     throw new ModuleException( e );
                 }catch (Exception e)
                 {
                	 m_pWriter_leftover.println(m_customerId+"|"+m_creditValue);
         	           
                     log.error("Updating Credit value remote customer id problem"+m_customerId+", failed, customer management other Exception. Error Msg:" +e.getMessage());
                     m_pWriter.println("** Customer "+m_customerId + ", whose credit value could NOT be updated because of customer Management other Exception. Error Msg:" +e.getMessage());
                   
                     
                     throw new ModuleException( e );
                  }
              } 
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
            //log.debug( "Job instance ID is " + batchContext.getJobId() );

            //open writer in append mode on 'RESTART'
            boolean append = MODE_RESTART.equals( batchContext.getStartMode() );
            m_reader = new LineNumberAwareReader( m_inputFile );
            m_writer = new PositionAwareWriter( m_outputFile, append );
            m_pWriter = new PrintWriter( m_writer );

            m_writer_succ = new PositionAwareWriter( m_outputFileSuc, append );
            m_pWriter_succ = new PrintWriter( m_writer_succ );
            
            m_writer_leftover = new PositionAwareWriter( m_outputFileLeftover, append );
            m_pWriter_leftover = new PrintWriter( m_writer_leftover );
            
            m_userId=(SecurityContext.getPrincipal()==null)? S_CRDB_BATCH_USER: SecurityContext.getPrincipal().getName();
            m_sourceId = SecurityContext.getSystemSourceId();
            String sourceId = "";
            if(m_sourceId != null)
                sourceId =  m_sourceId.toString();
            m_aud = new AuditInfo();
            m_aud.setUserId(m_userId);
            m_aud.setOriginatorApplicationId(sourceId);

            System.setProperty("weblogic.webservice.verbose", "true" );
            System.setProperty("weblogic.wsee.verbose", "true" );
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
        	m_writer_succ.close();
        	m_writer_leftover.close();
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
        str = state.getProperty( OUTPUT_FILE_POSITION_SUCC );
        long outPosition_succ = (str != null) ? Long.parseLong( str ) : 0;
        str = state.getProperty( OUTPUT_FILE_POSITION_LEFT );
        long outPosition_left = (str != null) ? Long.parseLong( str ) : 0;
        
        try {
            m_reader.reposition( inLineNo );
            m_writer.reposition( outPosition );
            m_writer_succ.reposition( outPosition_succ );
            m_writer_leftover.reposition( outPosition_left);
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
              state.setProperty( OUTPUT_FILE_POSITION_SUCC, m_writer_succ.getPosition()
                      + "" );
              state.setProperty( OUTPUT_FILE_POSITION_LEFT, m_writer_leftover.getPosition()
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
     * @param outputFileSucc
     *            The outputFileSucc to set.
     */
    public void setOutputFileSuc(String outputFileSuc)
    {
        m_outputFileSuc = outputFileSuc;
    }

    /**
     * @param outputFileLeftover
     *            The outputFileLeftover to set.
     */
    public void setOutputFileLeftover(String outputFileLeftover)
    {
        m_outputFileLeftover = outputFileLeftover;
    }



}
