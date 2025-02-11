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

package com.telus.credit.batch.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.framework.batch.exception.ModuleException;

public class EncryptionModule extends BaseLineReaderModule
{
    private static Log log = LogFactory.getLog(EncryptionModule.class);

    private Encryptor m_encryptor;
    
    private String m_outputFileName;

    public String getOutputFileName()
    {
        return m_outputFileName;
    }

    public void setOutputFileName(String outputFileName)
    {
        m_outputFileName = outputFileName;
    }

    public Encryptor getEncryptor()
    {
        return m_encryptor;
    }

    public void setEncryptor(Encryptor encryptor)
    {
        m_encryptor = encryptor;
    }

    HashMap m_outputFilesMap;
    protected void processLineRecord(String record) throws ModuleException
    {
        // Skip empty lines
        if (record == null || "".equals(record)) 
               return;
        try{
            String encryptedRecord = m_encryptor.encryptFields( record );
            PrintStream outputStream = null;
            if (m_outputFilesMap == null )
                m_outputFilesMap = new HashMap();
            String currentInputFileName = getCurrentFile().getName();
            if ( m_outputFilesMap.get(currentInputFileName)!=null)
                outputStream = (PrintStream) m_outputFilesMap.get(currentInputFileName);
            else{
                File newOutputFile = new File ( m_outputFileName );
                if (!newOutputFile.exists())
                    newOutputFile.createNewFile();
                PrintStream newOutputStream = new PrintStream( new FileOutputStream(newOutputFile));
                m_outputFilesMap.put( currentInputFileName, newOutputStream);
                outputStream = newOutputStream;
            }
            
            outputStream.println( encryptedRecord );
        }
        catch (IOException e)
        {
            m_log.error("error in writing encrypted record!" , e);
            throw new ModuleException ( "error in writing encrypted record!" , e);
        }
    }

}
