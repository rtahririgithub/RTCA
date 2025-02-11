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

/*
 * Created on 20-Sep-2005
 *
 * Class is created to facilitate multiple files processing in
 * a single job module.
 * Class must be extended to implement methods
 * readBatchHeader(), readRequest(), readBatchTrailer()
 */

package com.telus.credit.batch.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.telus.framework.batch.exception.ModuleException;
import com.telus.framework.batch.io.LineNumberAwareReader;

import com.telus.credit.batch.util.BatchErrorConstants;
import com.telus.credit.batch.util.BatchErrorHandler;

/**
 * Base module that handles the processing of input files with line reader.
 */
public abstract class BaseLineReaderModule extends BaseFileModule
{
    private LineNumberAwareReader m_lineReader;



    /**
     * Read/Parse the line record.
     * @param record - String line record read from file.
     * 
     * @throws  ModuleException  if an error occurred.
     */
    protected abstract void processLineRecord(String record)
            throws ModuleException;

    /* (non-Javadoc)
     * @see com.telus.formletters.batch.BaseFileModule#initReader(java.io.File, int)
     */
    protected void initReader(File currentFile,int lineNum) throws ModuleException
    {
        String file = currentFile.getAbsolutePath();
        try
        {
            m_lineReader = new LineNumberAwareReader( file );
            if ( lineNum != 0 )
            {
                try
                {
                    m_lineReader.reposition( lineNum );
                }
                catch ( IOException e )
                {
                    BatchErrorHandler.logError( m_log, e,
                            "Error repositioning line in:"
                                    + currentFile.getAbsolutePath() );
                    throw new ModuleException( "Error repositioning line in: "
                            + currentFile.getAbsolutePath() );
                }
            }
        }
        catch ( FileNotFoundException e )
        {
            BatchErrorHandler.logError( m_log, BatchErrorConstants.Type.FILE,
                    BatchErrorConstants.Subtype.FL_NOT_FOUND, "file:" + file );
            throw new ModuleException( e );
        }
    }

 
    /* (non-Javadoc)
     * @see com.telus.formletters.batch.BaseFileModule#closeReader()
     */
    protected void closeReader() throws ModuleException
    {

        try
        {
            if ( m_lineReader != null ) m_lineReader.close();
        }
        catch ( IOException e )
        {
            BatchErrorHandler.logError( m_log, BatchErrorConstants.Type.FILE,
                    BatchErrorConstants.Subtype.FL_READWRITE,
                    "Error closing LineReader for: "
                            + getCurrentFile().getAbsolutePath(),
                    getCurrentFile().getAbsolutePath() );
            throw new ModuleException( "Error closing LineReader for: "
                    + getCurrentFile().getAbsolutePath(), e );
        }

    }


    /* (non-Javadoc)
     * @see com.telus.formletters.batch.BaseFileModule#readRecord()
     */
    protected String readRecord() throws ModuleException
    {
        try
        {
            return m_lineReader.readLine();

        }
        catch ( IOException e )
        {
            BatchErrorHandler.logError( m_log, e, "Error reading line from:"
                    + getCurrentFile().getAbsolutePath() );
            throw new ModuleException( "Error reading line from: "
                    + getCurrentFile().getAbsolutePath() );
        }
    }


    /* (non-Javadoc)
     * @see com.telus.formletters.batch.BaseFileModule#getCurrentRecordNumber()
     */
    protected int getCurrentRecordNumber()
    {
        return m_lineReader.getLineNumber();
    }


    /**
     * Delegates the string record.
     * 
     * @throws  ModuleException  if an error occurred.
     * 
     * @see  #processLineRecord(String)
     */
    public void execute() throws ModuleException
    {
        processLineRecord( getCurrentRecord() );
    }

}
