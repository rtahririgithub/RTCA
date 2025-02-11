package com.telus.crd.assessment.batch;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.framework.batch.BatchContext;
import com.telus.framework.batch.exception.ModuleException;
import com.telus.framework.batch.module.AbstractNonLoopingModule;

/**
 * This modules reads the driver file and creates the control files for the
 * {@link MonthlyUpDownModule}.
 * 
 * @author Trevor Baker (x145637)
 */
public class MonthlyUpDownInitModule extends AbstractNonLoopingModule
{
    private static final Log s_log = LogFactory.getLog(MonthlyUpDownInitModule.class);

    // spring injected
    private File m_inputFile;
    private int m_minNumberOfRecordsPerFile;
    private int m_numberOfFiles;
    private int m_numberOfJobs;
    private File m_outputDirectory;
    private String m_outputFileFormat;


    @Override
    public void launch(BatchContext batchContext) throws ModuleException
    {
        if( m_minNumberOfRecordsPerFile < 1 )
        {
            throw new IllegalArgumentException("minNumberOfRecordsPerFile cannot be less than 1.");
        }

        if( m_numberOfJobs < 1 )
        {
            throw new IllegalArgumentException("numberOfJobs cannot be less than 1.");
        }

        if( m_numberOfFiles > m_numberOfJobs )
        {
            throw new IllegalArgumentException("numberOfFiles cannot be greater than numberOfJobs.");
        }

        try
        {
            List<Properties> props = getProperties();

            for( int i = 0; i < props.size(); i++ )
            {
                writeControlFile(i + 1, props.get(i));
            }
        }
        catch( Exception e )
        {
            throw new ModuleException(e);
        }
    }


    private List<Properties> getProperties() throws IOException
    {
        int recordCount = getRecordCount();
        int fileCount = Math.min(m_numberOfFiles, (recordCount / m_minNumberOfRecordsPerFile) + 1);

        int recordsPerFile = recordCount / fileCount;
        int recordsRemainder = recordCount % fileCount;

        List<Properties> props = new ArrayList<Properties>(m_numberOfJobs);

        int lineNum = 0;
        for( int i = 1; i <= fileCount; i++ )
        {
            int count = (i <= recordsRemainder)? recordsPerFile + 1 : recordsPerFile;

            props.add(newProperties(lineNum, count));

            lineNum += count;
        }

        for( int i = fileCount; i < m_numberOfJobs; i++ )
        {
            props.add(newProperties(0, 0));
        }
        
        return props;
    }


    private int getRecordCount() throws IOException
    {
        LineNumberReader reader = null;

        try
        {
            reader = new LineNumberReader(new FileReader(m_inputFile));
            reader.skip(Long.MAX_VALUE);
            return reader.getLineNumber();
        }
        finally
        {
            try
            {
                if( reader != null )
                {
                    reader.close();
                }
            }
            catch( IOException e )
            {
                s_log.error(null, e);
            }
        }
    }

    
    private Properties newProperties(int lineNum, int count)
    {
        Properties props = new Properties();
        props.setProperty(MonthlyUpDownConstants.CONTROL_PROP_KEY_COUNT, String.valueOf(count));
        props.setProperty(MonthlyUpDownConstants.CONTROL_PROP_KEY_LINE_NUM, String.valueOf(lineNum));
        return props;
    }
    

    private void writeControlFile(int fileNum, Properties props) throws IOException
    {
        if( !m_outputDirectory.exists() )
        {
            if( !m_outputDirectory.mkdirs() )
            {
                throw new IOException("Cannot create directory: " + m_outputDirectory);
            }
        }

        FileWriter writer = null;

        try
        {
            String fileName = m_outputFileFormat.replace("(NUM)", String.valueOf(fileNum));
            writer = new FileWriter(new File(m_outputDirectory, fileName));
            props.store(writer, null);
        }
        finally
        {
            try
            {
                if( writer != null )
                {
                    writer.close();
                }
            }
            catch( IOException e )
            {
                s_log.error(null, e);
            }
        }
    }


    //-------------------------------------------------------------------------
    // Spring Injected
    //-------------------------------------------------------------------------
    public void setInputFile(File inputFile)
    {
        m_inputFile = inputFile;
    }


    public void setMinNumberOfRecordsPerFile(int minNumberOfRecordsPerFile)
    {
        m_minNumberOfRecordsPerFile = minNumberOfRecordsPerFile;
    }


    public void setNumberOfFiles(int numberOfFiles)
    {
        m_numberOfFiles = numberOfFiles;
    }


    public void setNumberOfJobs(int numberOfJobs)
    {
        m_numberOfJobs = numberOfJobs;
    }


    public void setOutputDirectory(File outputDir)
    {
        m_outputDirectory = outputDir;
    }


    public void setOutputFileFormat(String outputFileFormat)
    {
        m_outputFileFormat = outputFileFormat;
    }
}
