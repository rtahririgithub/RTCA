package com.telus.credit.fraud.batch.step14;

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

import java.io.*;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.credit.fraud.batch.Utils;
import com.telus.framework.batch.exception.ModuleException;
import com.telus.framework.batch.Module;
import com.telus.framework.batch.BatchContext;

/**
 * Processes the candidate Target Feed File for delivery to NDS.
 *    1. Formats the City and Region fields.
 *    2. Writes a candidate target file to the master box
 *
 * Please consult the Cust Prof Connectwave Design and Interface
 * Specification (v1.0.5) for more details.
 *
 * @author x107469
 */
public class FormatCandidateTargetFile implements Module {

   private static final Log log = LogFactory.getLog(FormatCandidateTargetFile.class);

   // encapsulates the input file
   private BufferedReader m_input = null;

   // encapsulates the output file
   private PrintStream m_output = null;

   // holds the path to the input file
   private String m_inputFilename = null;

   // holds the path to the output file
   private String m_outputFilename = null;

   // marks the beginning of the city/province field for the service address from the beginning of each line (in bytes)
   private int m_serviceStartPoint = 106;

   // marks the end of the city/province field for the service address from the beginning of each line (in bytes)
   private int m_serviceEndPoint = 128;

   // marks the beginning of the city/province field for the billing address from the beginning of each line (in bytes)
   private int m_billingStartPoint = 178;

   // marks the end of the city/province field for the billing address from the beginning of each line (in bytes)
   private int m_billingEndPoint = 200;

   // marks the beginning of the city/province field for the billing address in the string buffer
   private int m_bufferStartPoint = 186;

   // marks the end of the city/province field for the billing address in the string buffer
   private int m_bufferEndPoint = 208;

   // dictates the exact size (padded if necessary) of the (city and province) field
   private int m_fieldSize = 30;

   // used for padding in the output file (30 whitespace characters)
   private String m_padding = "                              ";

   // used to ensure that the batch execution framework executes this module exactly once
   private int m_executionCounter = 1;

   /*
   * (non-Javadoc)
   *
   * @see com.telus.framework.batch.Module#execute()
   */
   public void execute() throws ModuleException {

      int lineCounter = 1;
      try {
         for(;;) {
            String line = m_input.readLine();
            if(line == null) break;
            StringBuffer buffer = new StringBuffer(line);

            // parse the service and billing address fields from the line
            String cityProvinceService = this.processLine(m_serviceStartPoint, m_serviceEndPoint, line, lineCounter);
            String cityProvinceBilling = this.processLine(m_billingStartPoint, m_billingEndPoint, line, lineCounter);

            // determine how to format the line (4 conditions)
            // - note the service and billing fields are guaranteed to be present in the
            //   underlying file
            //
            if(cityProvinceService != null && cityProvinceBilling != null) {
               buffer.replace(m_serviceStartPoint, m_serviceEndPoint, cityProvinceService);
               buffer.replace(m_bufferStartPoint, m_bufferEndPoint, cityProvinceBilling);
            }
            else if(cityProvinceService != null && cityProvinceBilling == null) {
               buffer.replace(m_serviceStartPoint, m_serviceEndPoint, cityProvinceService);
               buffer.replace(m_bufferStartPoint, m_bufferEndPoint, m_padding);
            }
            else if(cityProvinceService == null && cityProvinceBilling != null) {
               buffer.replace(m_serviceStartPoint, m_serviceEndPoint, m_padding);
               buffer.replace(m_bufferStartPoint, m_bufferEndPoint, cityProvinceBilling);
            }
            else if(cityProvinceService == null && cityProvinceBilling == null) {
               buffer.replace(m_serviceStartPoint, m_serviceEndPoint, m_padding);
               buffer.replace(m_bufferStartPoint, m_bufferEndPoint, m_padding);
            }

            // write the formatted line to the output file
            m_output.println(buffer);

            // flush the buffer
            Utils.flushBuffer(buffer);

            // new line
            lineCounter ++;
         }
      }
      catch (Exception e) {
         throw new ModuleException(e);
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
         m_input = new BufferedReader(new FileReader(m_inputFilename));
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
    * Processes the portion of the specified line dictated by the
    * start and end parameters.
    *
    * Input:  Vancouver      BC
    * Output: Vanouver BC
    *
    * Note: the output is right padded to 30 spaces
    *
    * @param start the beginning index of the portion to process
    * @param end the ending index of the portion to process
    * @param line is the string to process
    * @param lineCounter is the line in the file being processed
    * @return a processed string
    */
   private String processLine(int start, int end, String line, int lineCounter) throws Exception {

      // extract the desired string fragment
      String temp = line.substring(start, end);
      if(temp == null) {
         return null;
      }


	  String city = "";
	  if(temp.indexOf(' ')==-1)
	  {
	      city=temp;
	  } else if(temp.length()>19)
	  {
	  	  city = temp.substring(0,20);
	  }else
	  {
	      city =temp.substring(0, temp.indexOf(' '));
      }
      if(city !=null){
	      city = city.trim();
      }

      // trim whitespace
      temp = temp.trim();
      if(temp.length() <= 0) {
         return null;
      }

      // extract the city field
      // fix defect 21501 (null billing city)
      //String city = temp.substring(0, temp.indexOf(' ')).trim();

     // String city = "";
     // if(temp.indexOf(' ')==-1)
     // 	city=temp;
     // else
     // 	city =temp.substring(0, temp.indexOf(' '));

      //String city = temp.substring(0, temp.indexOf(' '));


      // extract the province field
      // fix defect 21501 (null billing province)
      //String province = temp.substring(temp.lastIndexOf(' '), temp.length()).trim();
      String province = "";
      if(temp.lastIndexOf(' ')!=-1){
      	province = temp.substring(temp.lastIndexOf(' '), temp.length());
      }

      if(province !=null){
         province = province.trim();
      }
      // return the formatted and padded string
      return Utils.pad(city, province, m_fieldSize);
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
    * Sets the service start point.
    */
   public void setServiceStartPoint(String serviceStartPoint) {
      m_serviceStartPoint = Integer.parseInt(serviceStartPoint);
   }

   /**
    * Sets the service start end point.
    */
   public void setServiceEndPoint(String serviceEndPoint) {
      m_serviceEndPoint = Integer.parseInt(serviceEndPoint);
   }

   /**
    * Sets the billing start point.
    */
   public void setBillingStartPoint(String billingStartPoint) {
      m_billingStartPoint = Integer.parseInt(billingStartPoint);
   }

   /**
    * Sets the billing end point.
    */
   public void setBillingEndPoint(String billingEndPoint) {
      m_billingEndPoint = Integer.parseInt(billingEndPoint);
   }

   /**
    * Sets the buffer start point for billing.
    */
   public void setBufferStartPoint(String bufferStartPoint) {
      m_bufferStartPoint = Integer.parseInt(bufferStartPoint);
   }

   /**
    * Sets the buffer end point for billing.
    */
   public void setBufferEndPoint(String bufferEndPoint) {
      m_bufferEndPoint = Integer.parseInt(bufferEndPoint);
   }

   /**
    * Sets the field size.
    */
   public void setFieldSize(String fieldSize) {
      m_fieldSize = Integer.parseInt(fieldSize);
   }

   /**
    * Sets the padding.
    */
   public void setPadding(String padding) {
      m_padding = padding;
   }
}
