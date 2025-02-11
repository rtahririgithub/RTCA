/*
 * Copyright (c) 2005 TELUS Communications Inc., All Rights Reserved.
 * 
 * This document contains proprietary information that shall be distributed or
 * routed only within TELUS, and its authorized clients, except with written
 * permission of TELUS.
 * 
 * $Id$
 */

package com.telus.credit.batch.extract;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.framework.batch.BatchContext;
import com.telus.framework.batch.Module;
import com.telus.framework.batch.exception.ModuleException;
import com.telus.framework.batch.io.LineNumberAwareReader;
import com.telus.framework.batch.io.PositionAwareWriter;

/**
 * 
 * <p>
 * <b>Description: </b>  This function splits the input file into two files. 
 * One file contains lines that profile linked with single customer; the other file 
 * contains lines that profile linked with two or more customers.
 * </p>
 * <p>
 * <b>Design Observations: </b>
 * </p>
 * <ul>
 * <li></li>
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
 * @author Lei Fan(x089748)
 *  
 */
public class SplitFileBatch implements Module
{
    private static final Log log = LogFactory.getLog( SplitFileBatch.class );
    
    private String m_inputFile;
    private String m_singleCustomerOutputFile;
    private String m_multiCustomerOutputFile; 
    private int m_failurePoint;
    private LineNumberAwareReader m_reader;
    private PositionAwareWriter m_singleCustomerWriter;
    private PositionAwareWriter m_multiCustomerWriter;
    private PrintWriter m_pSingleCustomerWriter;
    private PrintWriter m_pMultiCustomerWriter;
    private String m_inputStr;
    private int m_counter;
    
    private String m_profileId;
 	private String m_tempInputStr=null;
 	private String m_previousProfileId=null;
    private boolean m_isNewId=false;
    

    private static final String INPUT_FILE_LINE_NO = "INPUT_FILE_LINE_NO";
    private static final String SINGLE_CUSTOMER_OUTPUT_FILE_POSITION = "SINGLE_CUSTOMER_OUPUT_FILE_POSITION";
    private static final String MULTI_CUSTOMER_OUTPUT_FILE_POSITION = "MULTI_CUSTOMER_OUPUT_FILE_POSITION";



    /*
     * (non-Javadoc)
     * 
     * @see com.telus.framework.batch.Module#execute()
     */
    public void execute() throws ModuleException
    {  
    	m_profileId=m_inputStr.substring(0,m_inputStr.indexOf(",")).trim();
    	if ((m_profileId.equals(m_previousProfileId)))
    	{
    		if (m_tempInputStr!=null) m_pMultiCustomerWriter.println(m_tempInputStr);
    		m_pMultiCustomerWriter.println(m_inputStr);
    		m_tempInputStr=null;
	    	m_isNewId=false;
	    		}
	    else
	    	{  
	    	 if (m_isNewId) m_pSingleCustomerWriter.println(m_tempInputStr.substring(m_tempInputStr.indexOf(",")+1));
	    	 m_tempInputStr=m_inputStr;
	    	 m_isNewId=true;
	    		}
	    	
    	m_previousProfileId=m_profileId;
    	
    	
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
        	if (m_isNewId) m_pSingleCustomerWriter.println(m_tempInputStr.substring(m_tempInputStr.indexOf(",")+1));
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
            m_singleCustomerWriter = new PositionAwareWriter( m_singleCustomerOutputFile, append );
            m_multiCustomerWriter = new PositionAwareWriter( m_multiCustomerOutputFile, append );
            m_pSingleCustomerWriter = new PrintWriter( m_singleCustomerWriter );
            m_pMultiCustomerWriter = new PrintWriter( m_multiCustomerWriter );
        }
        catch ( FileNotFoundException fnfe ) {
            throw new ModuleException( fnfe );
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
            m_singleCustomerWriter.close();
            m_multiCustomerWriter.close();
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
        str = state.getProperty( SINGLE_CUSTOMER_OUTPUT_FILE_POSITION );
        long singleCustomerOutPosition = (str != null) ? Long.parseLong( str ) : 0;
        str = state.getProperty( MULTI_CUSTOMER_OUTPUT_FILE_POSITION );
        long multiCustomerOutPosition = (str != null) ? Long.parseLong( str ) : 0;
        try {
            m_reader.reposition( inLineNo );
            m_singleCustomerWriter.reposition( singleCustomerOutPosition );
            m_multiCustomerWriter.reposition( multiCustomerOutPosition );      
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
            state.setProperty( SINGLE_CUSTOMER_OUTPUT_FILE_POSITION, m_singleCustomerWriter.getPosition()
                    + "" );
            state.setProperty( MULTI_CUSTOMER_OUTPUT_FILE_POSITION, m_multiCustomerWriter.getPosition()
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
	 * @param singleCustomerOutputFile The singleCustomerOutputFile to set.
	 */
	public void setSingleCustomerOutputFile(String singleCustomerOutputFile) {
		m_singleCustomerOutputFile = singleCustomerOutputFile;
	}
	
	/**
	 * @param multiCustomerOutputFile The multiCustomerOutputFile to set.
	 */
	public void setMultiCustomerOutputFile(String multiCustomerOutputFile) {
		m_multiCustomerOutputFile = multiCustomerOutputFile;
	}
}


