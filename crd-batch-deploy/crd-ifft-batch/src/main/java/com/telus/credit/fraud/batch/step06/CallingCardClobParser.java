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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

import com.telus.credit.fraud.batch.Utils;


/**
 * Parses the Calling Card (CC) CLOB records.
 * 1. Retrieves the value of the "cardNumber" property of the above product from the CLOB.
 *
 * @author Damian Etarsky
 */
public class CallingCardClobParser {

   // encapulates the input file
   private BufferedReader m_input = null;

   // encapulates the output file
   private BufferedWriter m_output = null;

   // holds the path to the input file
   private String m_inputFilename = null;

   // holds the path to the output file
   private String m_outputFilename = null;

   // specifies one of the keys to be parsed in the OMS clob structure
   private String m_parameterOne = null;

   // specifies an additional parameter to pass to the parser
   private String m_parameterTwo = null;

   // dictates the size of the buffered streams (this is used for performance tuning)
   private int m_bufferSize = 0;

   // dictates the exact size (padded if necessary) of each field written to the output file (in bytes)
   private int m_fieldSize = 0;

   // specifies how key/value pairs are delimited in the OMS clob structure
   private String m_clobDelimiter = null;

   // specifies how one key/value pair is delimited in the OMS clob structure
   private String m_propertyDelimiter = null;

   /**
    * Constructs a public instance of this class.
    *
    * @param inputFilename is the path to the input file
    * @param outputFilename is the path to the output file
    * @param parameterOne specifies one of the keys to be parsed in the OMS clob structure
    * @param parameterTwo specifies an additional parameter to pass to the parser
    * @param bufferSize dictates the size of the buffered streams (this is used for performance tuning)
    * @param fieldSize dictates the exact size (padded if necessary) of each field written to the output file (in bytes)
    * @param clobDelimiter specifies how key/value pairs are delimited in the OMS clob structure
    * @param propertyDelimiter specifies how one key/value pair is delimited in the OMS clob structure
    *
    * @throws Exception if the parser did not execute successfully
    */
   public CallingCardClobParser(String inputFilename, String outputFilename, String parameterOne, String parameterTwo,
                                int bufferSize, int fieldSize, String clobDelimiter, String propertyDelimiter)
           throws Exception
   {
      // grab the input paramaters
      m_inputFilename = inputFilename;
      m_outputFilename = outputFilename;
      m_parameterOne = parameterOne;
      m_parameterTwo = parameterTwo;
      m_bufferSize = bufferSize;
      m_fieldSize = fieldSize;
      m_clobDelimiter = clobDelimiter;
      m_propertyDelimiter = propertyDelimiter;

      try {
         m_input = new BufferedReader(new FileReader(m_inputFilename));
         m_output = new BufferedWriter(new FileWriter(m_outputFilename), m_bufferSize);
         execute();
      }
      catch(Exception e) {
         throw new Exception(e);
      }
      finally {
         if(m_input != null) {
            m_input.close();
         }
         if(m_output != null) {
            m_output.close();
         }
      }
   }

   /**
    * Executes the business logic.
    *
    * @throws IOException if this method does not execute correctly
    */
   private void execute() throws IOException
   {
      String line = null;
      StringBuffer buffer = new StringBuffer();

      while ((line = m_input.readLine()) != null) {

         /* parse the value associated with parameter one from the clob */
         String propertyValue = parse(line, m_parameterOne);
         buffer.append(Utils.padleft(propertyValue, m_fieldSize));

         /* append parameter two to the buffer */
         buffer.append(Utils.padleft(m_parameterTwo, m_fieldSize));
         buffer.append(" ");

         /* write the buffer to the output file */
         m_output.write(buffer.toString());
         m_output.newLine();

         /* flush the buffer */
         Utils.flushBuffer(buffer);
      }
   }

   /**
    * Parses a value associated with the specified property name from a series
    * of key/value pairs.
    *
    * Assume that each key/value pair is delimited with a semi-colon (this is
    * configurable by the m_clobDelimiter class attribute.
    *
    * Assume that one key/value pair is delimited with a plus sign (this is
    * configurable by the m_propertyDelimiter class attribute.
    *
    * Goal: parse the value associated with the "subscriptionNumber" property name
    * Input: subscriptionNumber+value;key(n)+value(n);
    * Output: the value associated with the "subscriptionNumber" property name
    *
    * @param line contains a series of key/value pairs (see the input format)
    * @param propertyName specifies which key/value pair to parse
    *
    * @return the value associated with the specified property name
    */
   private String parse(String line, String propertyName)
   {
      StringTokenizer parser = new StringTokenizer(line, m_clobDelimiter);
      String value = null;
      while (parser.hasMoreTokens()) {
         String token = parser.nextToken();
         if(token.startsWith(propertyName)) {
            value = token.substring((token.indexOf(m_propertyDelimiter)) + 1);
            break;
         }
      }
      return value;
   }
}