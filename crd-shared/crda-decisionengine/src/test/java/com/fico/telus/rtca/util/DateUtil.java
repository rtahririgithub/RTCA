package com.fico.telus.rtca.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static String getCurrentDateTimeAsXmlString() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		// Get the date today using Calendar object.
		Date today = Calendar.getInstance().getTime();        
		// Using DateFormat format method we can create a string 
		// representation of a date with the defined format.
		String reportDate = df.format(today);
		return reportDate;
	}
	
	public  static void main(String[] args) {
		System.out.println("Current Date/Time: " + getCurrentDateTimeAsXmlString() );
	}
}
