package com.telus.credit.fraud.batch.step05;

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

import java.util.Properties;
import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.framework.batch.BatchContext;
import com.telus.framework.batch.Module;
import com.telus.framework.batch.exception.ModuleException;
import com.telus.framework.crypto.EncryptionUtil;
import com.telus.credit.fraud.batch.Utils;

/**
 * Designed to perform the folllwing operations:
 *
 * 1. iterate the credit feed file (input file)
 *
 * 2. parse the Driver's License (DL) and Social Insurance Number (SIN)
 *    for each customer record
 *
 * 3. decrypt the DL and SIN using the telus encryption framework
 * 
 * 4. store customer id and the decrypted DL and SIN in the output file
 *
 * Complexity Analysis
 *
 *       Let n be defined as one I/O operation
 *
 *       Then,
 *             Scan input file                n   reads
 *             Write output file              n   writes
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

   // encapsulates the input file
   private BufferedReader m_input = null;

   // encapsulates the output file
   private PrintStream m_output = null;

   // holds the path to the input file
   private String m_inputFilename = null;

   // holds the path to the output file
   private String m_outputFilename = null;

   // the start point of the customer id
   private int m_customerIdStartPoint = 0;

   // the end point of the customer id
   private int m_customerIdEndPoint = 0;

   // the start point of the driver's license
   private int m_DLStartPoint = 0;

   // the end point of the driver's license
   private int m_DLEndPoint = 0;

   // the start point of the social insurance number
   private int m_SINStartPoint = 0;

   // the start point of the social insurance number
   private int m_SINEndPoint = 0;

   // dictates the exact size (padded if necessary) of the DL and SIN fields written to the output file (in bytes)
   private int m_fieldSize = 0;

   // used to ensure that the batch execution framework executes this module exactly once
   private int m_executionCounter = 1;

   /*
   * (non-Javadoc)
   *
   * @see com.telus.framework.batch.DataExtractModule#execute()
   */
   public void execute() throws ModuleException {

      try {
         for(;;) {
            String line = m_input.readLine();
            if(line == null) break;

            /* parse fields */
            String customerId = parseCustomerId(line);
            String driverLicense =  parseDL(line);
            String socialInsuranceNumber = parseSIN(line);

            /* decrypt */
            driverLicense = decrypt(driverLicense).trim();
            socialInsuranceNumber = decrypt(socialInsuranceNumber).trim();

            /* build the line */
           // if(driverLicense.length() > 0 || socialInsuranceNumber.length() > 0) {
               StringBuffer buffer = new StringBuffer();
               buffer.append(customerId);
               buffer.append(Utils.padleft(driverLicense, m_fieldSize));
               buffer.append(Utils.padleft(socialInsuranceNumber, m_fieldSize));

               /* write to output */
               this.write(buffer.toString());
           // } 

         }
      }
      catch (IOException e) {
         throw new ModuleException(e);
      }
      // increment the execution counter to ensure that this module is executed exactly once by the batch execution framework
      m_executionCounter ++;
   }

   /*
   * (non-Javadoc)
   *
   * @see com.telus.framework.batch.DataExtractModule#launch(com.telus.framework.batch.BatchContext)
   */
   public void launch(BatchContext batchContext) throws ModuleException {
      try {
         m_input = new BufferedReader(new java.io.FileReader(m_inputFilename));
         m_output = new PrintStream(new FileOutputStream(m_outputFilename));
      }
      catch (FileNotFoundException fnfe) {
          throw new ModuleException(fnfe);
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
   public int onExit(boolean success) throws ModuleException {
      try {
         m_input.close();
         m_output.close();
      }
      catch (IOException e) {
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
    * Sets the customer id start point.
    */
   public void setCustomerIdStartPoint(String customerIdStartPoint) {
      m_customerIdStartPoint = Integer.parseInt(customerIdStartPoint);
   }

   /**
    * Sets the customer id end point.
    */
   public void setCustomerIdEndPoint(String customerIdEndPoint) {
      m_customerIdEndPoint = Integer.parseInt(customerIdEndPoint);
   }

   /**
    * Sets the driver's license (DL) start point.
    */
   public void setDLStartPoint(String DLStartPoint) {
      m_DLStartPoint = Integer.parseInt(DLStartPoint);
   }

   /**
    * Sets the driver's license (DL) end point.
    */
   public void setDLEndPoint(String DLEndPoint) {
      m_DLEndPoint = Integer.parseInt(DLEndPoint);
   }

   /**
    * Sets the social insurance number (SIN) start point.
    */
   public void setSINStartPoint(String SINStartPoint) {
      m_SINStartPoint = Integer.parseInt(SINStartPoint);
   }

   /**
    * Sets the social insurance number (SIN) end point.
    */
   public void setSINEndPoint(String SINEndPoint) {
      m_SINEndPoint = Integer.parseInt(SINEndPoint);
   }

   /**
    * Sets the field size.
    */
   public void setFieldSize(String fieldSize) {
      m_fieldSize = Integer.parseInt(fieldSize);
   }

   /**
    * Parses the customer id field from the specified line.
    */
   private String parseCustomerId(String line) {
      return line.substring(m_customerIdStartPoint, m_customerIdEndPoint).trim();
   }

   /**
    * Parses the driver's license (DL) field from the specified line.
    */
   private String parseDL(String line) {
      return line.substring(m_DLStartPoint, m_DLEndPoint).trim();
   }

   /**
    * Parses the social insurance number (SIN) field from the specified line.
    */
   private String parseSIN(String line) {
      return line.substring(m_SINStartPoint, m_SINEndPoint).trim();
   }

   /**
    * Decrypts the specifed string using the telus encryption framework.
    */
   private String decrypt(String string) {
      if(string == null || string.length() <= 0) return string;
      return EncryptionUtil.decrypt(string);
   }

   /**
    * Writes a line to the output file.
    */
   private void write(String line) {
      m_output.println(line);
   }
}
