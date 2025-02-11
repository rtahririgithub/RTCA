package com.telus.credit.fraud.batch.validate.file;

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
import java.util.TreeSet;

/**
 * Designed to encapsulate, parse, and validate data extract files
 * and audit files.
 *
 * Created by x107469
 */
public class FileReader {

   // holds the collection of primary keys from an extract file
   private TreeSet primaryKeys = new TreeSet();

   // marks the start point of the record count in the audit file
   private final int RECORD_COUNT_START_POINT = 88;

   // marks the end point of the record count in the audit file
   private final int RECORD_COUNT_END_POINT = 97;

   // encapsulates the data exract input file
   private BufferedReader m_dataExtract = null;

   // encapsulates the audit input file
   private BufferedReader m_audit = null;

   // holds the path to the data exract input file
   private String m_dataExtractFilename = null;

   // holds the path to the audit file
   private String m_auditFilename = null;

   /**
    * Constructs a public instance of this class.
    *
    * @param dataExtractFilename the path to the extract file
    * @param auditFilename the path to the audit file
    */
   public FileReader(String dataExtractFilename, String auditFilename) throws Exception
   {
      m_dataExtractFilename = dataExtractFilename;
      m_auditFilename = auditFilename;
      try {
         m_dataExtract = new BufferedReader(new java.io.FileReader(m_dataExtractFilename));
         m_audit = new BufferedReader(new java.io.FileReader(m_auditFilename));
      }
      catch (Exception e) {
         throw new Exception(e);
      }
   }

   /**
    * Validates the extract file with the following checks:
    * <p>
    * 1. Each record in the extract file is exactly the same width as the specified record width parameter
    * 2. The extract width exactly the number of records specified by the audit record count parameter
    * <p>
    * An extract file that does not pass the above checks is considered invalid
    *
    * @param recordWidth the specified width (in bytes) of each record in the extract file
    * @param auditRecordCount the specified number of records in the extract file
    *
    * @throws InvalidDataExtractException if the extract file is found to be invalid
    */
   public void validateExtractFile(int recordWidth, int auditRecordCount) throws InvalidDataExtractException
   {
      int recordCounter = 0;
      try {
         for(;;) {
            String record = m_dataExtract.readLine();
            if(record == null) break;
            if(record.length() != recordWidth) {
               throw new InvalidDataExtractException("The data extract file " + m_dataExtractFilename + " has an invalid record.\n " +
                       "--> at line no " + (recordCounter + 1) + ": width is " + record.length() + " and != to the specified width of " + recordWidth);
            }
            recordCounter ++;
         }
         if(recordCounter != auditRecordCount) {
            throw new InvalidDataExtractException("The data extract file " + m_dataExtractFilename + " has an invalid number of records.\n " +
                    "--> the record count is " + recordCounter + " and != to the specified record count of " + auditRecordCount);
         }
      }
      catch (Exception e) {
         throw new InvalidDataExtractException(e);
      }
   }

   /**
    * Validates the extract file with the following checks:
    * <p>
    * 1. Each record in the extract file is exactly the same width as the specified record width parameter
    * 2. The extract width exactly the number of records specified by the audit record count parameter
    * 3. Each records in the extract file is uniquely identified by the primary key
    * <p>
    * An extract file that does not pass the above checks is considered invalid
    *
    * @param recordWidth the specified width (in bytes) of each record in the extract file
    * @param primaryKeyWidth the specified width (in bytes) of each record in the extract file
    * @param auditRecordCount the specified number of records in the extract file
    *
    * @throws InvalidDataExtractException if the extract file is found to be invalid
    */
   public void validateExtractFile(int recordWidth, int primaryKeyWidth, int auditRecordCount) throws InvalidDataExtractException
   {
      int recordCounter = 0;
      try {
         for(;;) {
            String record = m_dataExtract.readLine();
            if(record == null) break;
            if(record.length() != recordWidth) {
               throw new InvalidDataExtractException("The data extract file " + m_dataExtractFilename + " has an invalid record.\n " +
                       "--> at line no " + (recordCounter + 1) + ": width is " + record.length() + " and != to the specified width of " + recordWidth);
            }
            if( ! this.isRecordUnique(record, primaryKeyWidth)) {
               throw new InvalidDataExtractException("The data extract file " + m_dataExtractFilename + " contains a duplicate record.\n " +
                       "--> at line no " + (recordCounter + 1));
            }
            recordCounter ++;
         }
         if(recordCounter != auditRecordCount) {
            throw new InvalidDataExtractException("The data extract file " + m_dataExtractFilename + " has an invalid number of records.\n " +
                    "--> the record count is " + recordCounter + " and != to the specified record count of " + auditRecordCount);
         }
      }
      catch (Exception e) {
         throw new InvalidDataExtractException(e);
      }
   }

   /**
    * Parses the audit file for the record count and returns it.
    */
   public int getAuditRecordCount() throws InvalidAuditFileException {
      try {
        String line = m_audit.readLine();
        return Integer.parseInt(line.substring(RECORD_COUNT_START_POINT, RECORD_COUNT_END_POINT));
      }
      catch (Exception e) {
         throw new InvalidAuditFileException("The audit file " + m_auditFilename + " is invalid.", e);
      }
   }

   /**
    * Closes all resources.
    */
   public void close() {
      try {
         m_dataExtract.close();
         m_audit.close();
         primaryKeys.clear();
      }
      catch (IOException e) {
         // do nothing
      }
   }

   /**
    * Ensures that each specified record is unique to the extract file.
    */
   private boolean isRecordUnique(String record, int primaryKeyWidth) {
      String primaryKey = record.substring(0, primaryKeyWidth);
      return primaryKeys.add(primaryKey);
   }
}
