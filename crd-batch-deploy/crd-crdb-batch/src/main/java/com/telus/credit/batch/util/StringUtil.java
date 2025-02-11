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

package com.telus.credit.batch.util;

import java.util.ArrayList;

public class StringUtil
{
    /**
     * Parse a string to an ArrayList using String.split()
     * @param originalStr : an input string 
     *        delimiter   : delimiter of the string
     * 
     * @return an ArrayList parsed from the input string
     */
    public static ArrayList parseString(String originalStr, String delimiter) 
    {       
        if ( isNullOrEmpty(originalStr)) {
            return null;
        }
        
        String[] fields = originalStr.split("[" + delimiter + "]", -1);     
        for ( int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].trim();
        }       
        
        ArrayList list = new ArrayList();        
        for (int j=0; j<fields.length; j++)
            list.add((String)fields[j]);
            
        return list;
    }

    /**
     * Check if the input string is the 'null' or empty
     *  
     * @param str : an input string 
     * @return boolean value   
     * 
     */
    public static boolean isNullOrEmpty(String str)
    {   
        final String EMPTY_STRING = "";     
        if (str == null || EMPTY_STRING.equals(str)) 
            return true;
        else
            return false;
    }
    
    /**
     * Create a string with designated character(s)/string, such as " "/blank, "0", "$", "#", or "ABC"....
     * @param designated char/chars/string
     * @param num
     * @return String
     */ 
    public static String designatedStrGenerator(String designated, int num)
    {
        if (num <1) 
            return "";
        
        /*
        char[] blankFill = new char[num];
        Arrays.fill(blankFill, ' ');        
        return (blankFill).toString();
        */      
        String designatedStr = "";      
        for (int i=0; i<num; i++)
            designatedStr += designated;
        
        return designatedStr;
    }
    /**
     * Create a right-justified or left-justified string with a designated character(s)/string, 
     * such as " "/blank, "0", "$", "#", or "ABC"....
     * 
     * @param orgStr: original input string 
     * @param designated char/chars/string
     * @param len: length of the output string
     * @param isRightJustified: right or left justified
     * @return String
     */ 
    public static String justifiedStrGenerator(String orgStr, String designated, int len, boolean isRightJustified)
    {
        if (orgStr == null)
            return "";
        
        if (len < 1)
            return "";
        
        if (orgStr.length() > len)
            return orgStr.substring(0, len);
                
        String justifiedStr = "";
        if (isRightJustified)
            justifiedStr = designatedStrGenerator(designated, len-orgStr.length()) + orgStr;
        else
            justifiedStr = orgStr + designatedStrGenerator(designated, len-orgStr.length());
        
        return justifiedStr;
    }
}
