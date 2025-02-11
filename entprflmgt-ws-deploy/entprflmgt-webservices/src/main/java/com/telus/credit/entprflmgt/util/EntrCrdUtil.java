package com.telus.credit.entprflmgt.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Date;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class EntrCrdUtil {

    
    /**
     * The date format of yyyyMMdd, ie. 20050616
     */
    public final static String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    public final static SimpleDateFormat SIMPLE_DATE_FORMAT_YYYYMMDD = new SimpleDateFormat( DATE_FORMAT_YYYYMMDD );
    
    
    /**
     * Needed to create XMLGregorianCalendar instances
     */
    private static DatatypeFactory df = null;
    static {
        SIMPLE_DATE_FORMAT_YYYYMMDD.setLenient( false );
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException dce) {
            throw new IllegalStateException(
					    "Exception while obtaining DatatypeFactory instance", dce);
        }
        
    }
    
    public static Date convertStringToDate( String strDate )
    {
        Date result = null;
        if ( strDate != null && strDate.length() > 0 ) {
            try {
                // Following method call is not thread-safe,
                // so synchronizing it
                synchronized ( SIMPLE_DATE_FORMAT_YYYYMMDD ) {
                    result = SIMPLE_DATE_FORMAT_YYYYMMDD.parse( strDate );
                }
            }
            catch (ParseException pe) {
                result = null;
            }
        }
        return result;
    }
    
    /**
     * use binding   
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
     * use binding   
     */
    public static java.util.Date asDate(XMLGregorianCalendar xgc) {
        if (xgc == null) {
            return null;
        } else {
            return xgc.toGregorianCalendar().getTime();
        }
    }	
    
    public static String getBooleanStringValue( boolean b ) {
	return b ? "Y" : "N";
    }
    
    public static boolean getBooleanValue( String b ) {
	return (b != null && (b.equals("Y") || b.equals("1") || b.equalsIgnoreCase("true"))) ? true : false;
    }
}
