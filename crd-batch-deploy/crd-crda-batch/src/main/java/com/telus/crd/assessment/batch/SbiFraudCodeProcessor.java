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
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.framework.batch.BatchContext;
import com.telus.framework.batch.Module;
import com.telus.framework.batch.exception.ModuleException;
import com.telus.framework.batch.io.LineNumberAwareReader;
import com.telus.framework.batch.io.PositionAwareWriter;

import com.telus.crd.assessment.batch.domain.CreditBureauTranDetail;

/**
 * Transform SBI Detail tables to have same field count based on maxFieldCount 
 * 
 * @author x136675
 */
public class SbiFraudCodeProcessor implements Module
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

    private final String DELIM = "|";

    private String currentID = "";

    public void setInputFile(String inputFile)
    {
        m_inputFile = inputFile;
    }


    public void setOutputFile(String outputFile)
    {
        m_outputFile = outputFile;
    }


    public void setFailurePoint(int failurePoint)
    {
        m_failurePoint = failurePoint;
    }


    public void launch(BatchContext batchContext) throws ModuleException
    {
        try {
            log.debug( "Job instance ID is " + batchContext.getJobId() );

            boolean append = MODE_RESTART.equals( batchContext.getStartMode() );
            m_reader = new LineNumberAwareReader( m_inputFile );
            m_writer = new PositionAwareWriter( m_outputFile, append );
            m_pWriter = new PrintWriter( m_writer );

        }
        catch ( FileNotFoundException fnfe ) {
            throw new ModuleException( fnfe );
        }
    }


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


    public boolean hasNext() throws ModuleException
    {
        try {
            m_inputStr = m_reader.readLine();
        }
        catch ( IOException ioe ) {
            throw new ModuleException( ioe );
        }

        if ( m_inputStr == null ) {
            return false;
        }
        else {
            return true;
        }
    }


    public void execute() throws ModuleException
    {
        CreditBureauTranDetail cbtd = transformDetail( m_inputStr, DELIM );
        if ( cbtd.getCarId().equalsIgnoreCase( currentID ) ) {
            m_pWriter.print( DELIM + cbtd.getDetailCd() + DELIM + cbtd.getCode() + DELIM + cbtd.getVal() );
        }
        else {
            if ( !currentID.equalsIgnoreCase( "" ) ) m_pWriter.println();

            currentID = cbtd.getCarId();
            m_pWriter.print( cbtd.getCarId() + DELIM + cbtd.getDetailCd() + DELIM + cbtd.getCode() + DELIM
                    + cbtd.getVal() );
        }

        m_counter++;

        if ( m_failurePoint != 0 && m_counter >= m_failurePoint ) {
            throw new ModuleException(
                    "Simulated Exception. Failure point reached" );
        }
    }


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


    public Properties getSummary() throws ModuleException
    {
        Properties summary = new Properties();
        summary.put( "INPUT_RECORD_COUNT", m_counter + "" );
        summary.put( "OUTPUT_RECORD_COUNT", m_counter + "" );
        return summary;
    }


    public int onExit(boolean success) throws ModuleException
    {
        try {
            m_writer.close();
            m_reader.close();
        }
        catch ( IOException e ) {
        }

        return success ? RETURN_CODE_SUCCESS : RETURN_CODE_FAILURE;
    }


    private CreditBureauTranDetail transformDetail(String strLine,
            String delimiter)
    {
        StringBuffer str = new StringBuffer();
        str.append( StringUtils.stripEnd( strLine, null ) );
        String[] splitStr = strLine.split( "\\"+delimiter, -1 );

        CreditBureauTranDetail cbtd = new CreditBureauTranDetail();
        for (int i = 0 ; i < splitStr.length ; i++) {
            String value = splitStr[i];
            if ( i == 0 ) {
                cbtd.setCarId( value );
            }
            else if ( i == 1 ) {
                cbtd.setDetailCd( value );
            }
            else if ( i == 2 ) {
                cbtd.setCode( value );
            }
            else if ( i == 3 ) {
                cbtd.setVal( value );
            } else
                break;
        }

        return cbtd;
    }

}
