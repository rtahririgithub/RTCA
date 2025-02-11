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
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.credit.fraud.batch.Constants;

/**
 * Encapsulates a specified data file and provides various services
 * to the file comparator.
 *
 * This class will parse the keys present in the specified data files
 * and store these keys in a mathematical set.
 *
 * Also each key will be mapped to a line number in the file for
 * random access.
 *
 * Created by x107469
 */
public class FileReader {

   private static final Log log = LogFactory.getLog(FileReader.class);

   // points to the underlying flat file of records
   protected RandomAccessFile m_file = null;

   // holds the mapping between each key and its corresponding line number in the file
   protected HashMap m_lookupTable = null;

   // the width of each record in the file (in bytes)
   protected int m_lineWidth = 0;

   // the width of the key of each record in file (in bytes)
   protected int m_keyWidth = 0;

   /**
    * Default constructor.
    */
   public FileReader() {}
  
   /**
    * Constructs an instance of this class.
    * @param filename is the path to the underlying flat file
    * @param lineWidth is the width (in bytes) of each record in the file
    * @param keyWidth is the width of the key (in bytes) that uniquely defines each record in the file
    */
   public FileReader(String filename, int lineWidth, int keyWidth) throws Exception
   {
      m_lineWidth = lineWidth;
      m_keyWidth = keyWidth;
      m_lookupTable = new HashMap(1048576);
      try {
         m_file = new RandomAccessFile(filename, "r");
      }
      catch (FileNotFoundException e) {
         throw new Exception(e);
      }
      this.processFile();
   }

   /**
    * Returns a reference to the set of keys.
    *
    * Note: this method breaks encapsulation for performance reasons
    *       since to perform a deep copy of the set before returning
    *       a copy could be expensive
    */
   public Set getSet() {
      return m_lookupTable.keySet();
   }

   /**
    * Display the set of keys to the console.
    */
   public void displaySet()
   {
      //Iterator iter = m_set.iterator();
      //while(iter.hasNext()) {
      //   log.debug("key [" + iter.next() + "]");
      //}
   }

   /**
    * Returns the set difference between this set and the input parameter set.
    *
    *    Let A be the set encapsulated by this class
    *    Let B be the input parameter set
    *    Return: A - B
    */
   public HashSet setDifference(FileReader reader)
   {
      HashSet result = new HashSet();
      Set set = reader.getSet();
      Iterator iter = m_lookupTable.keySet().iterator();
      while(iter.hasNext()) {
         String key = (String) iter.next();
         if( ! set.contains(key)) {
            result.add(key);
         }
      }
      return result;
   }

   /**
    * Returns the set intersection between this set and the input parameter set.
    *
    *    Let A be the set encapsulated by this class
    *    Let B be the input parameter set
    *    Return: A intersection B
    */
   public HashSet setIntersection(FileReader reader)
   {
      HashSet result = new HashSet();
      Set set = reader.getSet();
      Iterator iter = m_lookupTable.keySet().iterator();
      while(iter.hasNext()) {
         String key = (String) iter.next();
         if(set.contains(key)) {
            result.add(key);
         }
      }
      return result;
   }

   /**
    * Returns the line in the file corresponding to the specified unique
    * key as a string.
    */
   public String readLine(String key) throws Exception
   {
      try {
         long lineNumber = ((Integer) m_lookupTable.get(key)).longValue();
         long pos = this.convertToPosition(lineNumber);
         m_file.seek(pos);
         return m_file.readLine();
      }
      catch (Exception e) {
         throw new Exception(e);
      }
   }

   /**
    * Closes all internal resources.
    */
   public void closeResources() throws Exception
   {
      try {
         if(m_file != null) m_file.close();
         if(m_lookupTable != null) m_lookupTable.clear();
      }
      catch (IOException e) {
         throw new Exception(e);
      }
   }

   /**
    * Displays the contents of the look up table which maps a key to a line number.
    */
   public void displayLookupTable()
   {
      Iterator iter = m_lookupTable.keySet().iterator();
      while(iter.hasNext()) {
         String key = (String) iter.next();
         int lineNumber = ((Integer) m_lookupTable.get(key)).intValue();
         log.debug("line number [" + lineNumber + "], key [" + key + "]");
      }
   }

   /**
    * Parses the flat file and performs two operations.
    * 1. Stores each key corresponding to a unique record in the file.
    * 2. Stores the mapping between unique key and its corresponding line number in the file.
    */
   protected void processFile() throws Exception
   {
      int line = 1;
      int duplicates = 0;

      for(;;) {
         String key = this.getKey();
         if(key == null) break;
         if(m_lookupTable.containsKey(key)) {
            // log.debug("FileReader.processFile(): duplicate key [" + key + "]");
            duplicates ++;
         }
         else {
            m_lookupTable.put(key, new Integer(line));
         }
         line ++;
      }
      log.info("sorted set is configured, size = " + m_lookupTable.size());
      log.info("    -- duplicates = " + duplicates);
   }

   /**
    * Returns the unique key corresponding to the next line in the file.
    *
    * Note: this method is intended to be used iteratively while parsing the
    * whole file - see this.processFile()
    */
   protected String getKey() throws Exception {
      String line = this.readLine();
      if(line == null) return null;
      return line.substring(1, m_keyWidth);
   }

   /**
    * Returns the next line in the file as a String.
    *
    * Note: this method is intended to be used in conjuction with the
    * method this.getKey()
    */
   protected String readLine() throws Exception {
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
    *    DOS formatted files imply (m_lineWidth + 2) (/r/n)
    *    Unix formatted files imply (m_lineWidth + 1) (/r)
    *
    * The default file format is assumed to be Unix.
    *
    * @param lineNumber is the line number of the line in the file
    * @return the position in the file that corresponds to the start of the specified line
    */
   protected long convertToPosition(long lineNumber) {
      return (lineNumber - 1) * (m_lineWidth + Constants.LINE_WIDTH_OFFSET);
   }
}
