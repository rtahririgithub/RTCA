package com.telus.credit.crda.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

import org.apache.logging.log4j.Logger;

public class CommonUtility {
	
	public static final String TIMESTAMPFORMAT  = "yyyy-MM-dd HH:mm:ss";//09/27/2015 03:46:43.314  yyyy-mm-dd hh:mm:ss 
	
	public static String getStackTrace(Throwable t) {
		
		if(t!= null){
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw, true);
			t.printStackTrace(pw);
			pw.flush();
			sw.flush();
		String stckTraceStr ="[ StackTrace : "  + 	sw.toString()  +"EndOfStackTrace]";
		stckTraceStr=removeBrkLine(stckTraceStr);
		stckTraceStr=leadingTrailingEscapeChar(stckTraceStr);		
		return stckTraceStr;
		}
		return "";
	} 
	
	public static  String getSourceOfLog4JUrl(){
		String loggerLocationStr="";
		try {
			ProtectionDomain loggerProtectionDomain = Logger.class.getProtectionDomain();
			CodeSource loggerCodeSource = loggerProtectionDomain.getCodeSource();
			URL loggerLocation = loggerCodeSource.getLocation();
			loggerLocationStr = loggerLocation.toString();
		} catch (Throwable e) {
			loggerLocationStr ="Failed to get the location associated with the Logger Class CodeSource(Sourcing Log4j from).";
		}
		return "[SourceOfLog4JUrl: Location associated with the Logger Class CodeSource(Sourcing Log4j from)=<" + loggerLocationStr + ">]";	
	}
	
	private static   String leadingTrailingEscapeChar( String str) {
		try{
		   str = str.startsWith("\"") ? str.substring(1) : str;
		   str = str.endsWith("\"") ? str.substring(0,str.length()-1) : str;
		}catch (Throwable e){}
		return str;
	}	
	private static   String removeBrkLine(String str) {
		try{
			str = str.replaceAll("\\r\\n|\\r|\\n", " ");
		}catch (Throwable e){}
		return str;
	}	
}
