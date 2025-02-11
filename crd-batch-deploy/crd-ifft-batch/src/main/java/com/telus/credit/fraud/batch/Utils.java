package com.telus.credit.fraud.batch;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Contains commonly used methods.
 * 
 * @author x107469
 */
public class Utils {

   private static final Log log = LogFactory.getLog(Utils.class);

   // date formatting
   private static final String DATE_PATTERN = "yyyyMMddhhmm";

   /**
    * Creates a public instance of this class.
    */
   public Utils() {}

   /**
    * Appends the specified string with blank spaces as dictated
    * by the spacing parameter.
    *
    * @param string is the string to padright
    * @param spacing dictates how many spaces to padright the string
    * @return a padded string
    */
	public static String padright(String string, int spacing) {
	    StringBuffer pad = new StringBuffer("");
	    int padLength = spacing - string.length();
	    for(int i = 0; i < padLength; i++) {
	        pad.append(" ");
	    }
	    return string + pad.toString();
	}

   /**
    * Prepends the specified string with blank spaces as dictated
    * by the spacing parameter.
    *
    * @param string is the string to pad left
    * @param spacing dictates how many spaces to pad left
    * @return a padded string
    */
	public static String padleft(String string, int spacing) {
	    StringBuffer pad = new StringBuffer("");
	    int padLength = spacing - string.length();
	    for(int i = 0; i < padLength; i++) {
	        pad.append(" ");
	    }
	    return pad.toString() + string;
	}

   /**
    * Concatenates the two strings with a one space separator and
    * appends whitespace according to the specified padright.
    *
    * Output format: string1 + " " + string2 + padright
    *
    * @param string1 is the first string
    * @param string2 is the second string
    * @param pad is the spacing to append to the concatenation of string1 and string2
    * @return a formatted and padded string
    */
	public static String pad(String string1, String string2, int pad) {
      StringBuffer buffer = new StringBuffer();
      buffer.append(string1).append(" ").append(string2);
      int padLength = pad - buffer.length();
      for(int i = 0; i < padLength; i++) {
          buffer.append(" ");
      }
      return buffer.toString();
	}

   /**
    * Returns today's date formatted according to the DATE_PATTERN constant.
    *
    * @return today's date
    */
   public static String getTodaysDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
		return dateFormat.format(new Date(System.currentTimeMillis()));
	}

   /**
    * Retrieves the record count from the file produced by the
    * FileComparator class.
    */
   public static String getRecordCount(String filename) {

      BufferedReader temp = null;
      try {
         temp = new BufferedReader(new FileReader(filename));
         return temp.readLine();
      }
      catch(Exception e) {
         log.error(e.getMessage());
         return null;
      }
      finally {
         try {
            if(temp != null) {
               temp.close();
            }
         }
         catch (IOException e) {
            // ignore...
         }
      }
   }

   /**
    * Flushes a string buffer.
    *
    * @param buffer is the buffer to flush.
    */
   public static void flushBuffer(StringBuffer buffer) {
       buffer.delete(0, buffer.length());
   }
}
