package com.telus.credit.crda.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogUtil {
    private static final Log log = LogFactory.getLog(LogUtil.class);

    static boolean callLogOn = true;
    static boolean infoLogOn = true;

	private static LogUtil single_instance = null;
	private LogUtil(){}
    
	public static LogUtil getInstance() {
        if (single_instance == null)
            single_instance = new LogUtil();
        return single_instance;
    }
    
    public static void traceCalllog(String string) {
        if (callLogOn)
            log.debug(string);
    }

    public static void infolog(String string) {
        if (infoLogOn){
            log.debug(string);
            System.out.println(string);
        }
    }
    public static void errorlog(String string) {
            log.error(string);
    }
    public static void errorlog(String message, Throwable t) {
        log.error(message, t);
    }
    public static String getClassMethodNames(String methodname, String classname) {
        return " Method :" + methodname + ". In Class :" + classname;
    }

	private void error(Class<?> class1, String msg) {
		LogFactory.getLog(class1).error(msg);
	}

	public String getOnelineStackTrace(Throwable e) {
		String msg = CommonUtility.getStackTrace(e);
		msg = removeBrkLine(msg);
		msg = leadingTrailingEscapeChar(msg);
		return msg;
	}

	public void errorlog(Class<?> class1, String msg, Throwable e) {
		msg = msg + CommonUtility.getStackTrace(e);
		msg = removeBrkLine(msg);
		msg = leadingTrailingEscapeChar(msg);
		error(class1, msg);
	}

	public void errorlog(Class<?> class1, String msg) {
		msg = removeBrkLine(msg);
		msg = leadingTrailingEscapeChar(msg);
		error(class1, msg);
	}

	private String removeBrkLine(String str) {
		if (str != null && !str.isEmpty()) {
			try {
				str = str.replaceAll("\\r\\n|\\r|\\n", " ");
			} catch (Throwable e) {
			}
		}
		return str;
	}

	private String leadingTrailingEscapeChar(String str) {
		if (str != null && !str.isEmpty()) {
			try {
				str = str.startsWith("\"") ? str.substring(1) : str;
				str = str.endsWith("\"") ? str.substring(0, str.length() - 1) : str;
			} catch (Throwable e) {
			}
		}
		return str;
	}
}
