package com.telus.crd.assessment.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;

import com.telus.framework.exception.RuntimeEnvironmentException;
/**
 * @author x089759
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */
public class DateUtil {

	public static String[] MONTH = { "01", "02", "03", "04", "05", "06", "07",
			"08", "09", "10", "11", "12" };

	public static String[] DATE = { "00", "01", "02", "03", "04", "05", "06",
			"07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
			"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
			"29", "30", "31" };

	public static String getDateFromDateTime(String datetime) {

		String date = null;

		if (datetime.length() >= 8) {
			date = datetime.substring(0, 8);
		}
		return date;
	}

	public static String insertDateSlashes(String datestring) {

		if	(datestring == null){
			return "";
		} else {
			return 	datestring.substring(0,4) + "/" + datestring.substring(4,6) + "/" +	datestring.substring(6,8);
		}
	}

	public static boolean isAfterToday(String checkDate) {

		// checkdate must be YYYYMMDD e.g. 20050930
		// Returns true if checkDate is after the current system date

		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String todayDate = dateFormat.format(now);


		//Example:
		//todayDate = "20050928"
		//int Check1 = todayDate.compareTo("20051001"); = -1
		//int Check2 = todayDate.compareTo("20050928"); = 0
		//int Check3 = todayDate.compareTo("20050927"); = 1


		if (checkDate == null) {
			return true;
		} else {

			if (todayDate.compareTo(checkDate) < 0) {
				return true;
			} else {
				return false;
			}
		}


	}

	public static int getDayDiff(String start, String end, String dateformat) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);
			Date startDate = dateFormat.parse(start);
			Date endDate = dateFormat.parse(end);
			GregorianCalendar calStart = new GregorianCalendar();
			calStart.setTime(startDate);
			GregorianCalendar calEnd = new GregorianCalendar();
			calEnd.setTime(endDate);
			if (calStart.get(Calendar.YEAR) == calEnd.get(Calendar.YEAR)) {
				return calEnd.get(Calendar.DAY_OF_YEAR)
						- calStart.get(Calendar.DAY_OF_YEAR);
			} else if ((calEnd.get(Calendar.YEAR) - calStart.get(Calendar.YEAR)) == 1) {
				int daysEndYear = calEnd.get(Calendar.DAY_OF_YEAR);
				int daysStartYear = calStart
						.getActualMaximum(Calendar.DAY_OF_YEAR)
						- calStart.get(Calendar.DAY_OF_YEAR);
				return daysEndYear + daysStartYear;
			} else {
				int startYear = calStart.get(Calendar.YEAR);
				int endYear = calEnd.get(Calendar.YEAR);
				GregorianCalendar cal = new GregorianCalendar();
				int days = 0;
				for (int i = startYear + 1; i < endYear; i++) {
					cal.set(Calendar.YEAR, i);
					days += cal.getActualMaximum(Calendar.DAY_OF_YEAR);
				}
				days += calEnd.get(Calendar.DAY_OF_YEAR);
				days += (calStart.getActualMaximum(Calendar.DAY_OF_YEAR) - calStart
						.get(Calendar.DAY_OF_YEAR));
				return days;
			}
		} catch (ParseException e) {
			return -1;
		}
	}
	 public static String formatDateToString(Date aDate,String format)
	    {
	        SimpleDateFormat formatter = new SimpleDateFormat(format);
	        return formatter.format(aDate);
	    }

	    public static Date formatStringToDate(String aDateStr,String format)
	    {
	        Date aDate = null;
	        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format);
	        try
	        {
	            aDate = formatter.parse(aDateStr);
	        }
	        catch (ParseException e)
	        {
	            throw new RuntimeEnvironmentException(e);
	        }
	        return aDate;
	    }


}
