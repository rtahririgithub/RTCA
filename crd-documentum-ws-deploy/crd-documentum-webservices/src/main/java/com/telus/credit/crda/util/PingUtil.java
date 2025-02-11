package com.telus.credit.crda.util;


public class PingUtil {
	
	private static PingUtil aPingUtil;
	
	public static PingUtil getInstance() {
		if(aPingUtil==null) {
			aPingUtil = new PingUtil();
		}
		return aPingUtil;
	}
	

	public String getpingResultTxt() {
		String txt = "";
		String breakln = System.getProperty("line.separator");

		txt = txt + breakln;
		txt = txt + "[ping result:" + breakln;

		// build info
		txt = txt + getBuildinfo() + breakln;


		return txt;
	}



	private  String getBuildinfo() {
		String txt ="";
		try {
			txt = txt + "[Build Info: " ;
			txt= txt + new VersionUtils().getBuildTimeStamp();
		} catch (Throwable e) {
			txt= txt + "[getBuildTimeStamp Failed, " + CommonUtility.getStackTrace(e)+ "]";
		}	
			txt= txt + "]";
		return txt;
	}
}
