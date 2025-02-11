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

import com.telus.credit.fraud.batch.Constants;

import java.util.TreeSet;
import java.util.TreeMap;
import java.util.Iterator;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Encapsulates a specified data file and provides various services
 * to the file processor.
 *
 * For data structures will model the underlying file:
 * 1. Maps the unique TN associated service instance id to a line number.
 * 2. Maps the unique SRTN associated service instance id to a line number.
 * 3. Maps the TN associated WTN's to a line number.
 * 4. Builds a set of SRTN associated service instance id's.
 *
 * Created by x107469
 */
public class FileReader {

   private static final Log log = LogFactory.getLog(FileReader.class);

   // points to the underlying flat file of records
   private RandomAccessFile m_file = null;

   // holds the set of keys that define each record in the file
   private TreeSet m_set = null;

   // holds the mapping between the TN associated service id and its corresponding line number in the file
   private TreeMap m_lookupTableTN = null;

   // holds the mapping between the SRTN associated service id and its corresponding line number in the file
   private TreeMap m_lookupTableSRTN = null;

   // holds the mapping between the TN associated WTN's and its corresponding line number in the file
   private TreeMap m_lookupTableWTN = null;

   // the width of each record in the file (in bytes)
   private int m_lineWidth = 0;

   // the width of the key of each record in file (in bytes)
   private int m_keyWidth = 0;

   // constants to delineate the type code field and the wtn field in each record
   private static final String TYPE_CODE_TN = "TN  ";
   private static final String TYPE_CODE_SRTN = "SRTN";
   private static final int TYPE_CODE_START = 51;
   private static final int TYPE_CODE_END = 55;
   private static final int WTN_START = 18;
   private static final int WTN_END = 28;

   /**
    * Constructs an instance of this class.
    *
    * @param filename is the path to the underlying flat file
    * @param lineWidth is the width (in bytes) of each record in the file
    * @param keyWidth is the width of the key (in bytes) that uniquely defines each record in the file
    */
   public FileReader(String filename, int lineWidth, int keyWidth) throws Exception
   {
      m_lineWidth = lineWidth;
      m_keyWidth = keyWidth;

      m_set = new TreeSet();
      m_lookupTableTN = new TreeMap();
      m_lookupTableSRTN = new TreeMap();
      m_lookupTableWTN = new TreeMap();

      try {
         if( ! new File(filename).exists()) {
            throw new Exception("The file [" + filename + "], does not exist.");
         }
         m_file = new RandomAccessFile(filename, "r");
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      try {
         this.processFile();
      }
      catch(Exception e) {
         throw new Exception(e);
      }

//      this.displaySets();
//      this.displayLookupTables();
   }

   /**
    * Returns the set of service instance id's associated
    * with TN records.
    */
   public TreeSet getSet() {
      return m_set;
   }

   /**
    * Display the set of keys to the console.
    */
   public void displaySets() {
      log.debug("SRTN FileReader");
      Iterator iter = m_set.iterator();
      while(iter.hasNext()) {
         log.debug("key [" + iter.next() + "]");
      }
   }

   /**
    * Returns the line number in the file corresponding to the specified unique
    * TN associated service intance id as a string.
    */
   public long getLineNumberTn(String key) {
      if( ! m_lookupTableTN.containsKey(key)) return -1;
      return ((Integer) this.m_lookupTableTN.get(key)).longValue();
   }

   /**
    * Returns the line number in the file corresponding to the specified unique
    * SRTN associated service intance id as a string.
    */
   public long getLineNumberSrtn(String key) {
      if( ! m_lookupTableSRTN.containsKey(key)) return -1;
      return ((Integer) this.m_lookupTableSRTN.get(key)).longValue();
   }

   /**
    * Returns the working telephone number (WTN) associated with the line number
    * in the file.
    */
   public String getWtn(long lineNumber) {
      if( ! m_lookupTableWTN.containsKey(Long.toString(lineNumber))) return null;
      return (String) this.m_lookupTableWTN.get(Long.toString(lineNumber));
   }

   /**
    * Closes all internal resources.
    */
   public void closeResources() throws Exception {
      try {
         if(m_file != null) m_file.close();
         if(m_set != null) m_set.clear();
         if(m_lookupTableTN != null) m_lookupTableTN.clear();
         if(m_lookupTableSRTN != null) m_lookupTableSRTN.clear();
         if(m_lookupTableWTN != null) m_lookupTableWTN.clear();
      }
      catch (Exception e) {
         throw new Exception(e);
      }
   }

   /**
    * Displays the contents of the look up table which maps a key to a line number.
    */
   public void displayLookupTables()
   {
      log.debug("TN lookup table");
      Iterator iter = m_lookupTableTN.keySet().iterator();
      while(iter.hasNext()) {
         String key = (String) iter.next();
         int lineNumber = ((Integer) m_lookupTableTN.get(key)).intValue();
         log.debug("line number [" + lineNumber + "], key [" + key + "]");
      }
      log.debug("SRTN lookup table");
      iter = m_lookupTableSRTN.keySet().iterator();
      while(iter.hasNext()) {
         String key = (String) iter.next();
         int lineNumber = ((Integer) m_lookupTableSRTN.get(key)).intValue();
         log.debug("line number [" + lineNumber + "], key [" + key + "]");
      }
      log.debug("WTN lookup table");
      iter = m_lookupTableWTN.keySet().iterator();
      while(iter.hasNext()) {
         String lineNumber = (String) iter.next();
         String WTN = ((String) m_lookupTableWTN.get(lineNumber));
         log.debug("line number [" + lineNumber + "], WTN [" + WTN + "]");
      }
   }

   /**
    * Parses the flat file and performs the following operations:
    * 1. Maps the unique TN associated service instance id to a line number.
    * 2. Maps the unique SRTN associated service instance id to a line number.
    * 3. Maps the TN associated WTN's to a line number.
    * 4. Builds a set of SRTN associated service instance id's.
    */
   private void processFile() throws Exception
   {
      int lineNum = 1;
      for(;;) {
         String line = this.readLine();
         if(line == null) break;
         String key = this.getKey(line);
         String typeCode = this.getTypeCode(line);
         String wtn = this.getWTN(line);
         if(typeCode.equals(TYPE_CODE_TN)) {
            this.m_lookupTableTN.put(key, new Integer(lineNum));
            if(wtn != null) {
               this.m_lookupTableWTN.put(Integer.toString(lineNum), wtn);
            }
         }
         if(typeCode.equals(TYPE_CODE_SRTN)) {
            this.m_set.add(key);
            this.m_lookupTableSRTN.put(key, new Integer(lineNum));
         }
         lineNum ++;
      }
      log.debug("SRTN set is configured, size = " + m_set.size());
   }

   /**
    * Returns the unique key corresponding to the next line in the file.
    *
    * Note: this method is intended to be used iteratively while parsing the
    * whole file - see this.processFile()
    */
   private String getKey(String line) {
      return line.substring(0, m_keyWidth);
   }

   /**
    * Returns the type code corresponding to a record.
    */
   private String getTypeCode(String line) {
      return line.substring(TYPE_CODE_START, TYPE_CODE_END);
   }

   /**
    * Returns the WTN corresponding to a record.
    */
   private String getWTN(String line) {
      return line.substring(WTN_START, WTN_END);
   }

   /**
    * Returns the next line in the file as a String.
    *
    * Note: this method is intended to be used in conjuction with the
    * method this.getKey()
    */
   private String readLine() throws Exception {
      try {
         return m_file.readLine();
      }
      catch (IOException e) {
         throw new Exception(e);
      }
   }

   /**
    * Converts the specified line number to the position within the file
    * that corresponds to the start of the specified line.
    *
    * Note:
    *    Unix formatted files imply (m_lineWidth + 1) (/r)
    *
    * The default file format is assumed to be Unix.
    *
    * @param lineNumber is the line number of the line in the file
    * @return the position in the file that corresponds to the start of the specified line
    */
   public long convertToPosition(long lineNumber) {
      return (lineNumber - 1) * (m_lineWidth + Constants.LINE_WIDTH_OFFSET);
   }
}
