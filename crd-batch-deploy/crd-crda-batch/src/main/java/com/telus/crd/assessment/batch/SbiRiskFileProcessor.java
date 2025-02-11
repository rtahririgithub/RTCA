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

package com.telus.crd.assessment.batch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.framework.batch.BatchContext;
import com.telus.framework.batch.exception.ModuleException;
import com.telus.framework.batch.io.LineNumberAwareReader;
import com.telus.framework.batch.io.PositionAwareWriter;

public class SbiRiskFileProcessor extends SbiFileProcessor
{
    private static final Log log = LogFactory.getLog( SbiFileProcessor.class ); 
    private String m_inputFile; 
    private String m_outputFile; 
    private int m_failurePoint; 
    private LineNumberAwareReader m_reader; 
    private PositionAwareWriter m_writer; 
    private PrintWriter m_pWriter; 
    private String m_inputStr; 
    private int m_counter; 
    private static final String INPUT_FILE_LINE_NO = "INPUT_FILE_LINE_NO"; 
    private static final String OUTPUT_FILE_POSITION = "OUPUT_FILE_POSITION"; 
    private int m_maxRecordCount;
    private int m_fieldCount;
    private int m_startFieldIndex;
 

    public void setMaxRecordCount(int maxRecordCount)
    {
        m_maxRecordCount = maxRecordCount;
    }


    public void setFieldCount(int fieldCount)
    {
        m_fieldCount = fieldCount;
    }


    public void setStartFieldIndex(int startFieldIndex)
    {
        m_startFieldIndex = startFieldIndex;
    }


    public void setInputFile(String inputFile) 
    {   m_inputFile = inputFile;     } 
 

    public void setOutputFile(String outputFile) 
    {   m_outputFile = outputFile;   } 
 
 
    public void setFailurePoint(int failurePoint) 
    {   m_failurePoint = failurePoint;     } 
 

    public void launch(BatchContext batchContext) throws ModuleException 
    {   try 
        {   
            log.debug( "Job instance ID is " + batchContext.getJobId() );             
 
            boolean append = MODE_RESTART.equals(batchContext.getStartMode()); 
            m_reader = new LineNumberAwareReader( m_inputFile );
            m_writer = new PositionAwareWriter( m_outputFile, append ); 
            m_pWriter = new PrintWriter( m_writer ); 
            
        } 
        catch ( FileNotFoundException fnfe )  {   throw new ModuleException(fnfe);    } 
    } 
 
 
    public void restoreState(Properties state) throws ModuleException 
    {   String str = state.getProperty( INPUT_FILE_LINE_NO ); 
        int inLineNo = (str != null) ? Integer.parseInt( str ) : 0; 
        str = state.getProperty( OUTPUT_FILE_POSITION ); 
        long outPosition = (str != null) ? Long.parseLong( str ) : 0; 
 
        try 
        {   m_reader.reposition( inLineNo ); 
            m_writer.reposition( outPosition ); 
        } 
        catch ( IOException ioe )   {   throw new ModuleException(ioe);      } 
    } 
 

    public boolean hasNext() throws ModuleException 
    {   
        try    {  
            m_inputStr = m_reader.readLine();      
            } 
        catch ( IOException ioe ) {  throw new ModuleException(ioe);        } 
 
        if ( m_inputStr == null )   {   
            return false;       } 
        else                               {   
            return true;        
            } 
    } 
 

    public void execute() throws ModuleException 
    {   
        m_pWriter.println( transform( m_inputStr,"|" ) ); 
        m_counter++; 
 
        if ( m_failurePoint != 0 && m_counter >= m_failurePoint ) 
        {   throw new ModuleException("Simulated Exception. Failure point reached");  } 
    } 
  
 
    public Properties getStateForRestart() throws ModuleException 
    {   Properties state = new Properties(); 
        try 
        {   state.setProperty( INPUT_FILE_LINE_NO, m_reader.getLineNumber() + "" ); 
            state.setProperty( OUTPUT_FILE_POSITION, m_writer.getPosition() + "" ); 
        } 
        catch ( IOException ioe ) 
        {   throw new ModuleException(ioe);     } 
        return state; 
    } 
 

    public Properties getSummary() throws ModuleException 
    { 
        Properties summary = new Properties(); 
        summary.put( "INPUT_RECORD_COUNT", m_counter + "" ); 
        summary.put( "OUTPUT_RECORD_COUNT", m_counter + "" ); 
        return summary; 
    } 
 

    public int onExit(boolean success) throws ModuleException 
    {   try 
        {   m_writer.close(); 
            m_reader.close(); 
        } 
        catch ( IOException e ) 
        {    } 
 
        return success ? RETURN_CODE_SUCCESS : RETURN_CODE_FAILURE; 
    } 
    
    private String transform(String strLine, String delimiter) {
        StringBuffer str = new StringBuffer(); 
        String[] splitStr = strLine.split( "\\"+delimiter, -1 );
        HashMap<Integer, String> mapVal = new HashMap<Integer, String>(  );
        
        for (int i = 1 ; i < splitStr.length ; i++) {
            mapVal.put( new Integer(splitStr[i]), splitStr[++i] );
        }
        
        str.append(splitStr[0]);

        for (int i = 1 ; i <= m_maxRecordCount ; i++ ){
            
            String value = (String)mapVal.get( i );
            
            if (value == null) {
                str.append( delimiter );
                str.append( delimiter );
            } else {
                str.append( delimiter );
                str.append( i );
                str.append( delimiter );
                str.append( value);
            }
                
        }
                
        return str.toString();
    }
}
