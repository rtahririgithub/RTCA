package com.telus.credit.orderDepositCalculator.webservice.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * 
 * @author t829939
 * 
 * @stereotype Utility Class
 * @version 1.0
 */

public class Util {
	
	public final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH24:mm:ss";
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
    
    /*
     * Cast List to object safely 
     */
    public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) 
    {     List<T> r = new ArrayList<T>(c.size());     
          for(Object o: c)       
        	  r.add(clazz.cast(o));     
           return r;
    }

}
