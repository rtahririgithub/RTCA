package com.telus.credit.crda.util.mapping;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import com.telus.credit.crda.util.CrdaUtility;
import org.dozer.CustomConverter;


public class Date_To_XMLGregorianCalendar_Converter implements CustomConverter {
	   
	public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
	        if (source == null) {
	            return null;
	        }
	        if (source instanceof Date) {
	        	XMLGregorianCalendar dest = CrdaUtility.asXMLGregorianCalendar((Date)source);
	            return dest;
	        }
	        return null;
	   }

}
