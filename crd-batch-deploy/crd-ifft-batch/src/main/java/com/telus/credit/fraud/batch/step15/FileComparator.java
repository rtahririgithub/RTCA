package com.telus.credit.fraud.batch.step15;

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

import java.util.*;
import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.framework.batch.BatchContext;
import com.telus.framework.batch.Module;
import com.telus.framework.batch.exception.ModuleException;

/**
 * Designed to generate the final output file intended for consumption
 * by the Telus Fraud Management System (TFMS).
 *
 * Created by x107469
 */
public class FileComparator implements Module {

   private static final Log log = LogFactory.getLog(FileComparator.class);

   // encapsulates the input file
   private BufferedReader m_input = null;

   // encapsulates the output file
   private PrintStream m_output = null;

   // holds the path to the input file
   private String m_inputFilename = null; /*"INTERSECTION.DAT";*/

   // holds the path to the output file
   private String m_outputFilename = null; /*"CHANGE_RECORDS.DAT";*/

   // the start point of the customer id
   private int m_width = 0; /*460;*/

   // used to ensure that the batch execution framework executes this module exactly once
   private int m_executionCounter = 1;

   /*
    * (non-Javadoc)
    *
    * @see com.telus.framework.batch.DataExtractModule#execute()
    */
   public void execute() throws ModuleException
   {
      String master;
      String target;
      String line;
      try {
         for(;;) {
            line = m_input.readLine();
            if(line == null) break;
            master = line.substring(0, m_width);
            target = line.substring(m_width, m_width * 2);
            if( ! master.equals(target)) {
               this.write("C" + target.substring(1));
            }
         }
         // increment the execution counter to ensure that this module is executed exactly once by the batch execution framework
         m_executionCounter ++;
      }
      catch (Exception e) {
         throw new ModuleException(e);
      }
   }

   /*
    * (non-Javadoc)
    *
    * @see com.telus.framework.batch.DataExtractModule#launch(com.telus.framework.batch.BatchContext)
    */
   public void launch(BatchContext batchContext) throws ModuleException
   {
      try {
         m_input = new BufferedReader(new java.io.FileReader(m_inputFilename));
         m_output = new PrintStream(new FileOutputStream(m_outputFilename));
      }
      catch (Exception e) {
         throw new ModuleException(e);
      }
   }

   /*
    * (non-Javadoc)
    *
    * @see com.telus.framework.batch.DataExtractModule#hasNext()
    */
   public boolean hasNext() throws ModuleException {
      return m_executionCounter <= 1;
   }

   /*
    * (non-Javadoc)
    *
    * @see com.telus.framework.batch.DataExtractModule#onExit(boolean)
    */
   public int onExit(boolean success) throws ModuleException
   {
      try {
         m_input.close();
         m_output.close();
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
    * @see com.telus.framework.batch.DataExtractModule#restoreState(java.util.Properties)
    */
   public void restoreState(Properties state) throws ModuleException {
   }


   /*
    * (non-Javadoc)
    *
    * @see com.telus.framework.batch.DataExtractModule#getStateForRestart()
    */
   public Properties getStateForRestart() throws ModuleException {
      return null;
   }


   /*
    * (non-Javadoc)
    *
    * @see com.telus.framework.batch.DataExtractModule#getSummary()
    */
   public Properties getSummary() throws ModuleException {
        return null;
   }

   /**
    * Writes a line to the output file.
    */
   private void write(String line) {
      m_output.println(line);
   }

   /**
    * Sets the input filename.
    */
   public void setInputFilename(String inputFilename) {
      m_inputFilename = inputFilename;
   }

   /**
    * Sets the output filename.
    */
   public void setOutputFilename(String outputFilename) {
      m_outputFilename = outputFilename;
   }

   /**
    * Sets the record width.
    */
   public void setWidth(String width) {
      m_width = Integer.parseInt(width);
   }
}
