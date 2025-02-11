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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DateUtil
{  
    private static final Log log = LogFactory.getLog( DateUtil.class );
    
    /**
     * The date format of yyyyMMdd, ie. 20080510
     */
    public final static String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    public final static SimpleDateFormat SIMPLE_DATE_FORMAT_YYYYMMDD = new SimpleDateFormat( DATE_FORMAT_YYYYMMDD );
    
    /**
     * The date format of yyyy-MM-dd, ie. 2008-05-10
     */
    public final static String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public final static SimpleDateFormat SIMPLE_DATE_FORMAT_YYYY_MM_DD = new SimpleDateFormat( DATE_FORMAT_YYYY_MM_DD );
        
    /**
     * The time format of HHmmss, i.e. 132530
     */
    public final static String TIME_FORMAT_HHMMSS = "HHmmss";
    public final static SimpleDateFormat SIMPLE_TIME_FORMAT_HHMMSS = new SimpleDateFormat( TIME_FORMAT_HHMMSS );
    
    /**
     * The time format of HHmmss, i.e. 132530
     */
    public final static String TIME_FORMAT_HH_MM_SS_SSS = " HH:mm:ss:SSS";
    public final static SimpleDateFormat SIMPLE_TIME_FORMAT_HH_MM_SS_MSS = new SimpleDateFormat( TIME_FORMAT_HH_MM_SS_SSS );
    
    /**
     * Date Time format yyyyMMddHHmmss
     */
    public final static SimpleDateFormat SIMPLE_DATE_FORMAT_YYYYMMDDHHMMSS = new SimpleDateFormat( DATE_FORMAT_YYYYMMDD + TIME_FORMAT_HHMMSS );
    
    /**
     * Date Time format yyyy-MM-dd HH:mm:ss:SSS
     */
    public final static SimpleDateFormat SIMPLE_DATE_FORMAT_YYYY_MM_DD_HH_MM_SS_MSS = new SimpleDateFormat( DATE_FORMAT_YYYY_MM_DD + TIME_FORMAT_HH_MM_SS_SSS );
        
    static final long ONE_HOUR = 60 * 60 * 1000L;
    static final long ONE_MINUTE = 60 * 1000L;

    /**
     * Needed to create XMLGregorianCalendar instances
     */
    private static DatatypeFactory df = null;
    static {         
        // Set Lenient to false        
        SIMPLE_DATE_FORMAT_YYYYMMDD.setLenient( false );
        SIMPLE_DATE_FORMAT_YYYY_MM_DD.setLenient( false );        
        SIMPLE_TIME_FORMAT_HHMMSS.setLenient( false );
        SIMPLE_DATE_FORMAT_YYYYMMDDHHMMSS.setLenient( false );
        SIMPLE_TIME_FORMAT_HH_MM_SS_MSS.setLenient( false );
        SIMPLE_DATE_FORMAT_YYYY_MM_DD_HH_MM_SS_MSS.setLenient( false );

	try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException dce) {
            throw new IllegalStateException(
                "Exception while obtaining DatatypeFactory instance", dce);
        }
    }    

    /**
     *    
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
     *    
     */
    public static java.util.Date asDate(XMLGregorianCalendar xgc) {
        if (xgc == null) {
            return null;
        } else {
            return xgc.toGregorianCalendar().getTime();
        }
    }	
    
    /**
     * Convert the date into a string in a certain format     
     * If theDate is null, return ""
     *  
     * @param theDate The date to be converted
     * @param format The format that the date shall be converted into 
     * the string value 
     * @return string The string value that the date is converted to   
     */
    public static String convertDateToString(Date theDate, SimpleDateFormat format) 
    {   
        if (theDate == null) {
            return "";
        }        
        
        return format.format(theDate);
    }
    
    /**
     * Convert a string date to a Date object
     *  
     * @param strDate : an input string value
     *        format : format of the data type
     * @return Date
     */
    public static Date convertStringToDate(String strDate, SimpleDateFormat format) 
    {
        Date date = null;
        
        if ( strDate != null && !strDate.equals( "" ) ) {
            try {
                // Following method call is not thread-safe, so synchronizing it
                synchronized( format ) {
                    date = format.parse(strDate);
                }
            }
            catch (ParseException pe) {
                log.error("convertStringToDate(): Failed to parse the date, expected format: (yyyy-mm-dd), " + format + " Date: " + strDate);
                date = null;
            }
        }
        
        return date;
    }
    
    /**
     * Calculate days between two dates
     *  
     * @param firstData
     * @param secondData
     * @return long     
     */
    public static long daysBetween(Date firstData, Date secondData)
    {
        return ( (secondData.getTime() - firstData.getTime() + ONE_HOUR) /(ONE_HOUR * 24));
    }
    
    /**
     * Calculate time (in milliseconds) between two dates
     *  
     * @param firstData
     * @param secondData
     * @return long     
     */
    public static long timeBetween(Date firstData, Date secondData)
    {
        return (secondData.getTime() - firstData.getTime());
    }
    
    /**
     * Calculate time (in seconds) between two dates
     *  
     * @param firstData
     * @param secondData
     * @return long     
     */
    public static long secondsBetween(Date firstData, Date secondData)
    {
        return ((secondData.getTime() - firstData.getTime())/1000);
    }
    
    /**
     * Calculate time (in minutes) between two dates
     *  
     * @param firstData
     * @param secondData
     * @return long     
     */
    public static long minutesBetween(Date firstData, Date secondData)
    {
        return ((secondData.getTime() - firstData.getTime())/ONE_MINUTE);
    }
    
    /** Add given month, day and hours to the given date and return the updated date object.
     *  
     * @param d input date
     * @param month - month to add, if negative value is entered then it will subtract the month.
     * @param day - day to add, if negative, then it will subtract the day.
     * @param hours - hours to add, if negative, then it will subtract the hours.
     * @return Updated Date Object.
     */
    public static Date addMonthAndDayAndHour(Date theDate, int month, int day, int hours)
    {
        if (theDate == null)
            return null;
            
        Calendar cal = Calendar.getInstance();
        cal.setTime( theDate );

        cal.add( Calendar.MONTH, month );
        cal.add( Calendar.DAY_OF_MONTH, day );
        cal.add( Calendar.HOUR_OF_DAY, hours );
        
        return cal.getTime();
    }
    
    public static Date getPreviousDate(Date theDate) 
    {        
        if (theDate == null)
            return null;
        
        return (addMonthAndDayAndHour(theDate, 0, -1, 0));
    }
    
    public static String getYesterday() 
    {        
        return (convertDateToString( DateUtil.getPreviousDate( new Date() ), SIMPLE_DATE_FORMAT_YYYYMMDD));
    }
    
    public static Date getTomorrow(Date theDate) 
    {        
        if (theDate == null)
            return null;
        
        return (addMonthAndDayAndHour(theDate, 0, 1, 0));
    }
   
    /** Get current date yyyyMMdd without hour, min, sec
     *  @return String of Data Object.
     */
    public static String getTomorrow() 
    {        
        return (convertDateToString( DateUtil.getTomorrow( new Date() ), SIMPLE_DATE_FORMAT_YYYYMMDD));
    }
    
    /**
     * Change date string of 2009-07-22 to JUL 22, 2009
     * Input data format must be: 2009-07-22 (yyyy-mm-dd)
     * 
     * @param strDate
     * @return String
     */
    public static String formatDateLayout(String strDate) 
	{		
    	if (strDate == null || strDate.equals(""))
    		return "";
    	
		Date aDate = convertStringToDate(strDate, SIMPLE_DATE_FORMAT_YYYY_MM_DD);	
		
		return (StringMonth[aDate.getMonth()] + " " + 
				aDate.getDate() + ", " + 
				String.valueOf(aDate.getYear() + 1900));
	}
    
    /**
     * Change date string of "Wed Oct 15 14:20:03 PDT 2008" to 2008/10/15
     * Input data format must be: 2009-07-22 (yyyy-mm-dd)
     * 
     * @param strDate
     * @return String
     */
    public static String formatDateLayout(Date theDate) 
	{		
    	if (theDate == null)
    		return "";	
		
		return (String.valueOf(theDate.getYear() + 1900) + "/" + 
				String.valueOf(theDate.getMonth() + 1) + "/" + 
				theDate.getDate());
	}
    
    /**
     * Get date in Month and return Month in String such as "JUL"
     * Input data format must be: 2009-07-22 (yyyy-mm-dd)
     * 
     * @param strDate
     * @return String
     */
    public static String getDateStrMonth(String strDate) 
	{		
    	if (strDate == null || strDate.equals(""))
    		return "";
    	
		Date aDate = convertStringToDate(strDate, SIMPLE_DATE_FORMAT_YYYY_MM_DD);		
		return (StringMonth[aDate.getMonth()]);
	}
    
    /**
     * convert Date like 2009-09-15 to 2009/09/15
     * Input data format must be: 2009-07-22 (yyyy-mm-dd)
     * 
     * @param strDate
     * @return String
     */
    public static String convertDateDisplay(String strDate) 
	{		
    	if (strDate == null || strDate.equals(""))
    		return "";
    	
		Date theDate = convertStringToDate(strDate, SIMPLE_DATE_FORMAT_YYYY_MM_DD);
		if (theDate == null || "".equals(theDate.toString()))
			return strDate;
		
		return (String.valueOf(theDate.getYear() + 1900) + "/" + 
				String.valueOf(theDate.getMonth() + 1) + "/" + 
				theDate.getDate());
	}
    
    /**
     * Get date in Month and Year and return a String such as "JUL/2008"
     * Input data format must be: 2009-07-22 (yyyy-mm-dd)
     * 
     * @param strDate
     * @return String
     */
    public static String getDateMonthYear(String strDate) 
	{		
    	if (strDate == null || strDate.equals(""))
    		return "";
    	
		Date aDate = convertStringToDate(strDate, SIMPLE_DATE_FORMAT_YYYY_MM_DD);

		if (aDate != null && "".equals(aDate.toString()))
			return (StringMonth[aDate.getMonth()] +"/"+ String.valueOf(aDate.getYear() + 1900));
		else
			return "";
	}
    
    /**
     * Get the quarter number for today, 
     * e.g. 2009-08-15, the quarter = 3.
     *      2009-01-01, the quarter = 1.
     *      
     * @return int of quarter of the year
     */
    public static int getCurrentQuarterInYear() 
	{
    	int quarter = 0;
    	int month = new Date().getMonth();
    	
    	if (month == 0 || month == 1 || month == 2)
    		quarter = 1;
    	else if (month == 3 || month == 4 || month == 5)
    		quarter = 2;
    	else if (month == 6 || month == 7 || month == 8)
    		quarter = 3;	
    	else if (month == 9 || month == 10 || month == 11)
    		quarter = 4;
    	
    	return quarter;
	}
    
    private static final String[] StringMonth = {"JAN", "FEB", "MAR", "APL", "MAY", "JUN", 
    									         "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};    
}
