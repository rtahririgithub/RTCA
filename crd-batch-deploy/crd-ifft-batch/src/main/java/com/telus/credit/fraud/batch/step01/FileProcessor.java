package com.telus.credit.fraud.batch.step01;

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

import java.util.Iterator;
import java.util.Set;
import java.util.Properties;
import java.io.RandomAccessFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.framework.batch.BatchContext;
import com.telus.framework.batch.Module;
import com.telus.framework.batch.exception.ModuleException;

/**
 * Designed to assign the WTN of TN related fields to the BTN
 * of SRTN related fields.
 *
 * Complexity Analysis
 *
 *       Let n be defined as one I/O operation
 *
 *       Then,
 *             Scan input file                n   reads
 *             Replace STRN BTN's             n   reades
 *                                         ----
 *                                           2n   i/o operations
 *       And,
 *             2n -> O(n)
 *
 * Therefore I/O operations are linearly bound.
 *
 * Created by x107469
 */
public class FileProcessor implements Module {

   private static final Log log = LogFactory.getLog(FileProcessor.class);

   // holds the path to the input and output file
   private String m_inputFilename = null;

   // encapsulates the master file
   private RandomAccessFile m_masterFile = null;

   // used to iterate the master file
   private FileReader m_fileReader = null;

   // the width of each record in the file (in bytes)
   private int m_lineWidth = 0;

   // the width of the key of each record in the file (in bytes)
   private int m_keyWidth = 0;

   // the offset of the WTN field from the beginning of a record (in bytes)
   private int m_wtnOffset = 0;

   // used to ensure that the batch execution framework executes this module exactly once
   private int m_executionCounter = 1;

   /*
   * (non-Javadoc)
   *
   * @see com.telus.framework.batch.Module#execute()
   */
   public void execute() throws ModuleException
   {
      Iterator iter = m_fileReader.getSet().iterator();
      while(iter.hasNext()) {
         String key = (String) iter.next();
         long lineNumberTn = m_fileReader.getLineNumberTn(key);
         String wtn = m_fileReader.getWtn(lineNumberTn);
         if(wtn != null) {
            long lineNumberSrtn = m_fileReader.getLineNumberSrtn(key);
            if(lineNumberSrtn > 0) {
               long pos = m_fileReader.convertToPosition(lineNumberSrtn) + m_wtnOffset;
               try {
                  m_masterFile.seek(pos);
                  m_masterFile.writeBytes(wtn);
               }
               catch (Exception e) {
                  throw new ModuleException(e);
               }
            }
         }
      }
      // increment the execution counter to ensure that this module is executed exactly once by the batch execution framework
      m_executionCounter ++;
   }

   /*
   * (non-Javadoc)
   *
   * @see com.telus.framework.batch.Module#launch(com.telus.framework.batch.BatchContext)
   */
   public void launch(BatchContext batchContext) throws ModuleException {
      try {
          m_fileReader = new FileReader(m_inputFilename, m_lineWidth, m_keyWidth);
          m_masterFile = new RandomAccessFile(m_inputFilename, "rw");
      }
      catch (Exception e) {
          throw new ModuleException(e);
      }
   }

   /*
    * (non-Javadoc)
    *
    * @see com.telus.framework.batch.Module#hasNext()
    */
   public boolean hasNext() throws ModuleException {
      return m_executionCounter <= 1;
   }

   /*
    * (non-Javadoc)
    *
    * @see com.telus.framework.batch.Module#onExit(boolean)
    */
   public int onExit(boolean success) throws ModuleException {
      try {
         m_fileReader.closeResources();
         m_masterFile.close();
      }
      catch (Exception e) {
         throw new ModuleException(e);
      }
      if(success) {
         return RETURN_CODE_SUCCESS;
      }
      return RETURN_CODE_FAILURE;
   }

   /*
    * (non-Javadoc)
    *
    * @see com.telus.framework.batch.Module#restoreState(java.util.Properties)
    */
   public void restoreState(Properties state) throws ModuleException {
   }


   /*
    * (non-Javadoc)
    *
    * @see com.telus.framework.batch.Module#getStateForRestart()
    */
   public Properties getStateForRestart() throws ModuleException {
      return null;
   }


   /*
    * (non-Javadoc)
    *
    * @see com.telus.framework.batch.Module#getSummary()
    */
   public Properties getSummary() throws ModuleException {
      return null;
   }


   /**
    * Displays the contents of the specified set to the console.
    */
   private void display(Set set) {
      Iterator iter = set.iterator();
      while(iter.hasNext()) {
         String key = (String) iter.next();
         log.debug("  key [" + key + "]");
      }
   }

   /**
    * Sets the input filename.
    */
   public void setInputFilename(String inputFilename) {
      m_inputFilename = inputFilename;
   }

   /**
    * Sets the line width.
    */
   public void setLineWidth(String lineWidth) {
      m_lineWidth = Integer.parseInt(lineWidth);
   }

   /**
    * Sets the key width.
    */
   public void setKeyWidth(String keyWidth) {
      m_keyWidth = Integer.parseInt(keyWidth);
   }

   /**
    * Sets the wtn offset.
    */
   public void setWtnOffset(String wtnOffset) {
      m_wtnOffset = Integer.parseInt(wtnOffset);
   }
}
