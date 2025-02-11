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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.ibatis.sqlmap.engine.execution.BatchException;

import com.telus.framework.batch.BatchContext;
import com.telus.framework.batch.exception.ModuleException;

import com.telus.credit.batch.exception.BaseModuleException;
import com.telus.credit.batch.util.BatchErrorConstants;
import com.telus.credit.batch.util.BatchErrorHandler;
import com.telus.credit.batch.util.WildcardFilenameFilter;

/**
 * Base module that handles the processing of input files. Can support
 * single files or multiple files in the input directory.
 * 
 * <p>
 * <b>For Spring Injection:</p>
 * <dl>
 * <dt>inputFilePattern</dt><dd>The wildcard filename pattern to match filenames of files in the inputDirectory.</dd>
 * <dt>inputFileDirectory</dt><dd>The path where to look for input files.</dd>
 * <dt>singleFileMode</dt><dd><code>true</code> if expecting to process only one file in inputDirectory. Default: <code>false</code></dd>
 * </dl>
 * </p>
 */
public abstract class BaseFileModule extends BaseModule
{
    protected static final String RESTART_KEY_INPUT_FILE = "INPUT_FILE";
    protected static final String RESTART_KEY_INPUT_FILE_RECORD_NUM = "INPUT_FILE_RECORD_NUM";
    protected static final String RESTART_KEY_PROCESSED_FILE_COUNT = "PROCESSED_FILE_COUNT";
    protected static final String RESTART_KEY_PROCESSED_FILE_PREFIX = "PROCESSED_FILE_#";
    protected static final String RESTART_KEY_PROCESSED_REQUEST_COUNT = "PROCESSED_REQUEST_COUNT";

    protected static final String SUMMARY_KEY_PROCESSED_FILE_COUNT = "Files processed: ";
    protected static final String SUMMARY_KEY_PROCESSED_FILE_PREFIX = "File processed #";
    protected static final String SUMMARY_KEY_PROCESSED_REQUEST_COUNT = "Requests processed: ";

    private File m_currentFile;
    private String m_currentRecord;

    private String m_inputFilePattern;
    private String m_inputDirectory;
    private boolean m_singleFileMode;

    private List m_inputFileList;      // list of files that can be processed
    private List m_processedFileList;  // list of absolute file paths to files already processed
    private int m_processedRequestCount;


    /**
     * Obtains a list of files in the input directory.
     * 
     * @param  batchContext  the batch context.
     * 
     * @throws  ModuleException  if an error occurred.
     */
    public void launch(BatchContext batchContext) throws ModuleException
    {
        m_inputFileList = initInputFileList();
        m_processedFileList = new ArrayList();

        try
        {
            if( m_inputFileList.isEmpty() )
            {
                throw new BaseModuleException(
                        BatchErrorConstants.Type.FILE,
                        BatchErrorConstants.Subtype.FL_NOT_FOUND,
                        "No files exist in input directory: " + m_inputDirectory,
                        m_inputDirectory);
            }
            else if( m_singleFileMode && m_inputFileList.size() > 1 )
            {
                throw new BaseModuleException(
                        BatchErrorConstants.Type.FILE,
                        BatchErrorConstants.Subtype.FL_DUPLICATE_FOUND,
                        "More than one file matched pattern. Expecting only one file for pattern: " + m_inputFilePattern,
                        m_inputDirectory);
            }
        }
        catch( BaseModuleException e )
        {
            BatchErrorHandler.logError(m_log, e);
            throw new ModuleException(e);
        }

        super.launch(batchContext);
    }


    /**
     * Initializes the reader for the {@link #getCurrentFile()}.
     * 
     * @throws  ModuleException  if an error occurred.
     */
    protected void initNormalMode() throws ModuleException
    {
        m_currentFile = (File )m_inputFileList.remove(0);

        initReader(m_currentFile, 0);
    }


    /**
     * Initializes the reader for the {@link #getCurrentFile()}. 
     * The current file was partially processed by the previous job run.
     * 
     * @param  restoreState  the previous job run's saved state.
     * 
     * @throws  ModuleException  if an error occurred.
     */
    protected void initRestartMode(Properties restoreState) throws ModuleException
    {
        // obtain saved state
        // input file info
        m_currentFile = new File(restoreState.getProperty(RESTART_KEY_INPUT_FILE));
        int recordNum = Integer.parseInt(restoreState.getProperty(RESTART_KEY_INPUT_FILE_RECORD_NUM));

        m_processedRequestCount = Integer.parseInt(restoreState.getProperty(RESTART_KEY_PROCESSED_REQUEST_COUNT));

        // obtain list of files processed
        int processedFileCount = Integer.parseInt(restoreState.getProperty(RESTART_KEY_PROCESSED_FILE_COUNT));
        for( int i = 0; i < processedFileCount; i++ )
        {
            String file = restoreState.getProperty(RESTART_KEY_PROCESSED_FILE_PREFIX + i); 

            m_processedFileList.add(file);

            // remove already processed files from input file list
            int index = m_inputFileList.indexOf(new File(file));
            if( index != -1 )
            {
                m_inputFileList.remove(index);
            }
        }

        // remove current file from list and init reader on it.
        m_inputFileList.remove(m_currentFile);
        initReader(m_currentFile, recordNum);
    }


    /**
     * Iterates through all the files in the input directory and the
     * the records in those files.
     * 
     * @return  <code>true</code> if there is another record to read.
     * 
     * @throws  ModuleException  if an error occurred.
     */
    protected boolean next() throws ModuleException
    {
        m_currentRecord = readRecord();
        if( m_currentRecord != null )
        {
            return true;
        }

        // finish processing current file
        m_processedFileList.add(m_currentFile.getAbsolutePath());

        if( m_singleFileMode || m_inputFileList.isEmpty() )
        {
            return false;
        }

        // obtain another file
        closeReader();

        m_currentFile = (File )m_inputFileList.remove(0);

        initReader(m_currentFile, 0);
        return next();
    }


    /**
     * @return  the current job run saved state.
     * 
     * @throws  ModuleException  if an error occurred.
     */
    public Properties getStateForRestart() throws ModuleException
    {
        Properties state = super.getStateForRestart();

        state.setProperty(RESTART_KEY_INPUT_FILE, m_currentFile.getAbsolutePath());
        state.setProperty(RESTART_KEY_INPUT_FILE_RECORD_NUM, String.valueOf(getCurrentRecordNumber()));

        state.setProperty(RESTART_KEY_PROCESSED_REQUEST_COUNT, String.valueOf(m_processedRequestCount));
        
        state.setProperty(RESTART_KEY_PROCESSED_FILE_COUNT, String.valueOf(m_processedFileList.size()));
        for( int i = 0; i < m_processedFileList.size(); i++ )
        {
            String path = (String )m_processedFileList.get(i);

            state.setProperty(RESTART_KEY_PROCESSED_FILE_PREFIX + i, path);
        }
                
        return state;
    }


    /**
     * @return  audit summary info.
     * 
     * @throws  ModuleException  if an error occurred.
     */
    public Properties getSummary() throws ModuleException
    {
        Properties summary = super.getSummary();

        summary.put(SUMMARY_KEY_PROCESSED_FILE_COUNT, String.valueOf(m_processedFileList.size()));
        for( int i = 0; i < m_processedFileList.size(); i++ )
        {
            summary.put(SUMMARY_KEY_PROCESSED_FILE_PREFIX + i, m_processedFileList.get(i));
        }

        summary.put(SUMMARY_KEY_PROCESSED_REQUEST_COUNT, String.valueOf(m_processedRequestCount));
        return summary;
    }


    /**
     * Closes the reader at the end of the batch job run.
     * 
     * @param  success  if the batch job completed successfully.
     * 
     * @return  The appropriate return code based on <code>success</code>.
     * 
     * @throws  ModuleException  if any error occurred.
     */
    public int onExit(boolean success) throws ModuleException
    {
        closeReader();
        return super.onExit(success);
    }


    /**
     * Override this method to initialize the reader.
     * 
     * @param  currentFile  the current input file to read.
     * @param  recordNum  if a restart then reposition the reader to this record;
     *                    otherwise 0 for the first record.
     * 
     * @throws  ModuleException  if any error occurred.
     */
    protected abstract void initReader(File currentFile, int recordNum) throws ModuleException;


    /**
     * Override this method to close the reader.
     * 
     * @throws  ModuleException  if any error occurred.
     */
    protected abstract void closeReader() throws ModuleException;


    /**
     * @return  the record read from the file, or
     *          <code>null</code> if there are no more records to read.
     * 
     * @throws  ModuleException  if any error occurred.
     */
    protected abstract String readRecord() throws ModuleException;


    /**
     * @return  The current file being processed.
     */
    public File getCurrentFile()
    {
        return m_currentFile;
    }


    /**
     * @return  The current record being processed.
     */
    public String getCurrentRecord()
    {
        return m_currentRecord;
    }


    /**
     * @return  The record number for the current record being processed.
     */
    protected abstract int getCurrentRecordNumber();


    /**
     * Returns the number of requests processed. This number is not necessarily
     * the same as the number of records processed, because a record may not be
     * a request.
     * 
     * @return  The # of requests processed.
     */
    public int getProcessedRequestCount()
    {
        return m_processedRequestCount;
    }


    /**
     * Call to increment the processed request counter after a request was successfully processed.
     * 
     * @param  amount  the amount to increment the counter.
     */
    protected void incProcessedRequestCount(int amount)
    {
        m_processedRequestCount += amount;
    }


    /**
     * @return  The wildcard filename pattern that finds files in the input directory.
     */
    public String getInputFilePattern()
    {
        return m_inputFilePattern;
    }


    /**
     * param  inputFilePattern  The wildcard filename pattern that finds files in the input directory.
     */
    public void setInputFilePattern(String inputFilePattern)
    {
        m_inputFilePattern = inputFilePattern;
    }


    /**
     * @return  The input directory where the files are.
     */
    public String getInputDirectory()
    {
        return m_inputDirectory;
    }


    /**
     * @param  inputDirectory  The input directory to set.
     */
    public void setInputDirectory(String inputDirectory)
    {
        m_inputDirectory = inputDirectory;
    }


    /**
     * @return  <code>true</code> if module handles only one input file.
     */
    public boolean getSingleFileMode()
    {
        return m_singleFileMode;
    }


    /**
     * @param  singleFileMode  <code>true</code> if module handles only one input file.
     */
    public void setSingleFileMode(boolean singleFileMode)
    {
        m_singleFileMode = singleFileMode;
    }


    /**
     * @return  The list of files in the input directory.
     */
    private List initInputFileList()
    {
        File inputDir = new File(m_inputDirectory);
        File[] inputFiles = inputDir.listFiles(new WildcardFilenameFilter(m_inputFilePattern));

        int length = (inputFiles == null)? 0 : inputFiles.length;

        ArrayList fileList = new ArrayList(length);

        for( int i = 0; i < length; i++ )
        {
            fileList.add(inputFiles[i]);
        }

        return fileList;
    }


    /**
     * @return Returns the inputFileList.
     */
    public List getInputFileList()
    {
        return m_inputFileList;
    }


    /**
     * @return Returns the processedFileList.
     */
    public List getProcessedFileList()
    {
        return m_processedFileList;
    }
}
