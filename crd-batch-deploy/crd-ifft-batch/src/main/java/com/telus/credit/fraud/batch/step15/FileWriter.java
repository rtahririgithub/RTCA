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

import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.credit.fraud.batch.Constants;

/**
 * Provides file writing services to the file comparator.
 *
 * Created by x107469
 */
public class FileWriter {

   private static final Log log = LogFactory.getLog(FileWriter.class);

   private FileReader m_masterFile = null;
   private FileReader m_targetFile = null;
   private PrintStream m_output = null;

   /**
    * Constructs an instance of this class.
    *
    * @param masterFile encapsulates the master file
    * @param targetFile encapsulates the target file
    * @param outputPath is the path to the output file
    */
   public FileWriter(FileReader masterFile, FileReader targetFile, String outputPath) throws Exception
   {
      m_masterFile = masterFile;
      m_targetFile = targetFile;

      try {
         m_output = new PrintStream(new FileOutputStream(outputPath));
      }
      catch (FileNotFoundException e) {
         throw new Exception(e);
      }
   }

   /**
    * Processes the operations map and writes the results to the output file.
    *
    * @param operationsMap contains the list of operations to perform
    */
   public void processOperationsMap(Map operationsMap) throws Exception
   {
      Set keys = operationsMap.keySet();
      Iterator iter = keys.iterator();
      while(iter.hasNext()) {
         String key = (String) iter.next();
         String operation = (String) operationsMap.get(key);
         if(operation != null) {
            if(operation.equals(com.telus.credit.fraud.batch.Constants.DELETE_OPERATION)) {
               this.processDeleteOperation(key);
            }
            else if(operation.equals(Constants.ADD_OPERATION)) {
               this.processAddOperation(key);
            }
            else if(operation.equals(Constants.CHANGE_OPERATION)) {
               this.processChangeOperation(key);
            }
         }
      }
   }

   /**
    * Closes all of the internal resources.
    */
   public void closeResources() throws Exception
   {
      try {
         m_masterFile = null;
         m_targetFile = null;
         m_output.close();
      }
      catch(Exception e) {
         throw new Exception(e);
      }
   }

   /**
    * Performs a delete operation.
    *
    * @param key is the index to the record to perform the operation on
    */
   private void processDeleteOperation(String key) throws Exception
   {
      StringBuffer line = new StringBuffer(this.m_masterFile.readLine(key));
      line.replace(0, 1, Constants.DELETE_OPERATION);
      m_output.println(line);
   }

   /**
    * Performs an add operation.
    *
    * @param key is the index to the record to perform the operation on
    */
   private void processAddOperation(String key) throws Exception
   {
      StringBuffer line = new StringBuffer(this.m_targetFile.readLine(key));
      line.replace(0, 1, Constants.ADD_OPERATION);
      m_output.println(line);
   }

   /**
    * Performs a change operation.
    *
    * @param key is the index to the record to perform the operation on
    */
   private void processChangeOperation(String key) throws Exception
   {
      StringBuffer line = new StringBuffer(this.m_targetFile.readLine(key));
      line.replace(0, 1, Constants.CHANGE_OPERATION);
      m_output.println(line);
   }
}
