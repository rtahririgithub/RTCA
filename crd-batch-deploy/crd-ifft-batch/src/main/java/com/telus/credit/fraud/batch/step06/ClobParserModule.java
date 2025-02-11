package com.telus.credit.fraud.batch.step06;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.framework.batch.BatchContext;
import com.telus.framework.batch.Module;
import com.telus.framework.batch.exception.ModuleException;

/**
 * Acts as a front side controller module for the clob parsers.
 *
 * This class is intended to act as the execution framework interface
 * and to route service calls to the various clob parsers in the
 * step06 package.
 *
 * Created by x107469
 */
public class ClobParserModule implements Module {

   private static final Log log = LogFactory.getLog(ClobParserModule.class);

   // command constants
   private static final String CALLING_CARD_CLOB_PARSER = "CallingCard";
   private static final String SINGLE_LINE_CLOB_PARSER = "SingleLine";
   private static final String LONG_DISTANCE_CLOB_PARSER = "LongDistance";
   private static final String SMART_RING_CLOB_PARSER = "SmartRing";

   // dictates which clob parser to delegate to
   private String m_command = null;

   // holds the path to the input file
   private String m_inputFilename = null;

   // holds the path to the output file
   private String m_outputFilename = null;

   // specifies one of the keys to be parsed in the OMS clob structure
   private String m_parameterOne = null;

   // specifies one of the keys to be parsed in the OMS clob structure
   private String m_parameterTwo = null;

   // specifies an additional parameter to pass to the parser
   private String m_parameterThree = null;

   // dictates the size of the buffered streams (this is used for performance tuning)
   private int m_bufferSize = 0;

   // dictates the exact size (padded if necessary) of each field written to the output file (in bytes)
   private int m_fieldSize = 0;

   // specifies how key/value pairs are delimited in the OMS clob structure
   private String m_clobDelimiter = null;

   // specifies how one key/value pair is delimited in the OMS clob structure
   private String m_propertyDelimiter = null;

   // used to ensure that the batch execution framework executes this module exactly once
   private int m_executionCounter = 1;

   /*
   * (non-Javadoc)
   *
   * @see com.telus.framework.batch.Module#execute()
   */
   public void execute() throws ModuleException {

      if(m_command == null || m_command.length() <= 0) {
         throw new ModuleException("The command must be set in the job definition file, process will terminate.");
      }
      try {
         if(m_command.equals(SINGLE_LINE_CLOB_PARSER)) {
            new SingleLineClobParser(m_inputFilename, m_outputFilename, m_parameterOne, m_parameterTwo, m_parameterThree,
                    m_bufferSize, m_fieldSize, m_clobDelimiter, m_propertyDelimiter);
         }
         else if(m_command.equals(LONG_DISTANCE_CLOB_PARSER)) {
            new LongDistanceClobParser(m_inputFilename, m_outputFilename, m_parameterOne, m_parameterTwo,
                    m_bufferSize, m_fieldSize, m_clobDelimiter, m_propertyDelimiter);
         }
         else if(m_command.equals(SMART_RING_CLOB_PARSER)) {
            new SmartRingClobParser(m_inputFilename, m_outputFilename, m_parameterOne, m_parameterTwo,
                    m_bufferSize, m_fieldSize, m_clobDelimiter, m_propertyDelimiter);
         }
         else if(m_command.equals(CALLING_CARD_CLOB_PARSER)) {
            new CallingCardClobParser(m_inputFilename, m_outputFilename, m_parameterOne, m_parameterTwo,
                    m_bufferSize, m_fieldSize, m_clobDelimiter, m_propertyDelimiter);
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
    * Sets the buffer size.
    */
   public void setBufferSize(String bufferSize) {
      m_bufferSize = Integer.parseInt(bufferSize);
   }

   /**
    * Sets the field size.
    */
   public void setFieldSize(String fieldSize) {
      m_fieldSize = Integer.parseInt(fieldSize);
   }

   /**
    * Sets the command.
    */
   public void setCommand(String command) {
      m_command = command;
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
    * Sets parameter one.
    */
   public void setParameterOne(String parameterOne) {
      m_parameterOne = parameterOne;
   }

   /**
    * Sets parameter two.
    */
   public void setParameterTwo(String parameterTwo) {
      m_parameterTwo = parameterTwo;
   }

   /**
    * Sets parameter three.
    */
   public void setParameterThree(String parameterThree) {
      m_parameterThree = parameterThree;
   }

   /**
    * Sets the clob delimiter.
    */
   public void setClobDelimiter(String clobDelimiter) {
      m_clobDelimiter = clobDelimiter;
   }

   /**
    * Sets the property delimiter.
    */
   public void setPropertyDelimiter(String propertyDelimiter) {
      m_propertyDelimiter = propertyDelimiter;
   }
}
