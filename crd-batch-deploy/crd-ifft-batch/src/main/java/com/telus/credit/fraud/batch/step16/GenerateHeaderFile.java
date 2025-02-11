package com.telus.credit.fraud.batch.step16;

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

import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import com.telus.framework.batch.BatchContext;
import com.telus.framework.batch.Module;
import com.telus.framework.batch.exception.ModuleException;
import com.telus.credit.fraud.batch.Utils;

/**
 * Generates the header file for later conacatenation with
 * the NDS feed file.
 * 
 * @author x107469
 */
public class GenerateHeaderFile implements Module {

   // encapsulates the output file
   private PrintStream m_output = null;

   // holds the path to the output file
   private String m_outputFilename = null;

   // defines the telco code for the header
   private String m_telcoCode = null;

   // used to ensure that the batch execution framework executes this module exactly once
   private int m_executionCounter = 1;

   /*
    * (non-Javadoc)
    *
    * @see com.telus.framework.batch.Module#execute()
    */
   public void execute() throws ModuleException
   {
      try {
         StringBuffer buffer = new StringBuffer();
         buffer.append("H");
         buffer.append(Utils.padright("", 5));
         buffer.append(Utils.padleft(m_telcoCode, 8));
         buffer.append(Utils.padright("", 5));
         buffer.append(Utils.getTodaysDate());
         buffer.append(Utils.padright("", 429));

         // write the contents of the buffer to the output file
         m_output.println(buffer.toString());

         // increment the execution counter to ensure that this module is executed exactly once by the batch execution framework
         m_executionCounter ++;
      }
      catch(Exception e) {
         throw new ModuleException(e);
      }
   }

   /*
    * (non-Javadoc)
    *
    * @see com.telus.framework.batch.Module#launch(com.telus.framework.batch.BatchContext)
    */
   public void launch(BatchContext batchContext) throws ModuleException
   {
      try {
         m_output = new PrintStream(new FileOutputStream(m_outputFilename));
      }
      catch (FileNotFoundException e) {
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
   public int onExit(boolean success) throws ModuleException
   {
      try {
         m_output.close();
      }
      catch(Exception e) {
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
    * Sets the output filename.
    */
   public void setOutputFilename(String outputFilename) {
      m_outputFilename = outputFilename;
   }

   /**
    * Sets the telco code.
    */
   public void setTelcoCode(String telcoCode) {
      m_telcoCode = telcoCode;
   }
}
