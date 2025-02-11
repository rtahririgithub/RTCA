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

package com.telus.credit.wlnprflmatch.webservice.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


public class DataConvertUtil {
	
	public final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public final static String NULL_DATE_STRING_VALUE = "4444-12-31 00:00:00";
	
	/**
     * Needed to create XMLGregorianCalendar instances
     */
    private static DatatypeFactory df = null;
    static {
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException dce) {
            throw new IllegalStateException(
                "Exception while obtaining DatatypeFactory instance", dce);
        }
    }  
    
    public static XMLGregorianCalendar getXMLGregorianCalendarNow() 

    {
          GregorianCalendar gregorianCalendar = new GregorianCalendar();
          return df.newXMLGregorianCalendar(gregorianCalendar);
    }

    /**
     * Converts a java.util.Date into an instance of XMLGregorianCalendar
     *
     * @param date Instance of java.util.Date or a null reference
     * @return XMLGregorianCalendar instance whose value is based upon the
     *  value in the date parameter. If the date parameter is null then
     *  this method will simply return null.
     */
    public static XMLGregorianCalendar asXMLGregorianCalendar(java.util.Date date) {
        if (date == null) {
            return null;
        } else {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(date.getTime());
            return df.newXMLGregorianCalendar(gc);
        }
    }

    /**
     * Converts an XMLGregorianCalendar to an instance of java.util.Date
     *
     * @param xgc Instance of XMLGregorianCalendar or a null reference
     * @return java.util.Date instance whose value is based upon the
     *  value in the xgc parameter. If the xgc parameter is null then
     *  this method will simply return null.
     */
    public static java.util.Date asDate(XMLGregorianCalendar xgc) {
        if (xgc == null) {
            return null;
        } else {
            return xgc.toGregorianCalendar().getTime();
        }
    }
    
    /**
     * Converts a long to int 
     */
    public static int LongToInt(long l) {     
    	int i = (int)l;     
    	if ((long)i != l) {         
    		throw new IllegalArgumentException(l + " cannot be casted to int.");     
    		}    
    	return i; 
    	} 
    
    /*
     * Cast List to object safely 
     */
    public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) 
    {     List<T> r = new ArrayList<T>(c.size());     
          for(Object o: c)       
        	  r.add(clazz.cast(o));     
           return r;
    }
    
    /*
     * Converts a String to Date
     */
    public static java.util.Date convertStringToDate(String strDate)
    {
    	SimpleDateFormat formatter = new SimpleDateFormat( DATE_TIME_FORMAT );
    	java.util.Date convertedDate = null;
        try {
	              convertedDate = formatter.parse(strDate);
	        }
	    catch (ParseException pe) {
	            convertedDate = null;
	        }
	    return convertedDate;
      }
    
    /*
     * Converts a Date to String
     */
    public static String convertDateToString(java.util.Date theDate) 
    {        
        SimpleDateFormat formatter = new SimpleDateFormat( DATE_TIME_FORMAT );
        
        if (theDate == null) {
            return NULL_DATE_STRING_VALUE;
        }        
        
	    return formatter.format(theDate);
    }
    
	/**
	 * Convert a string value in either 'true' of 'false' to a boolean value
	 * 
	 * @param anValue : an input String	      
	 * @return boolean 
	 */
	public static boolean convertStringToBoolean(String anValue) 
	{
	    final String YES_STRING = "YES";
	    final String Y_STRING = "Y";
	    final String TRUE_STRING = "TRUE";
	    	    
	    if(anValue.equalsIgnoreCase(TRUE_STRING) || anValue.equalsIgnoreCase(YES_STRING) || anValue.equalsIgnoreCase(Y_STRING) )
			return true;
	    else 
	        return false;
	}
    
}
