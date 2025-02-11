package com.telus.credit.entprflmgt.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.framework.config.ConfigContext;

	public class VersionUtils {
		private static final Log m_Log = LogFactory.getLog(VersionUtils.class);

		public  String getBuildinfo() {
			String txt ="";
			try {
				
				txt = txt + "[Build Info: " ;
			  	String fw_buildDate = ConfigContext.getProperty("buildDate");
		    	String fw_buildLabel = ConfigContext.getProperty("buildLabel");
		    	if( fw_buildDate != null ) {
			    	txt = txt +  " [buildDate=" + fw_buildDate  +"]";		    		
		    	}
		    	if( fw_buildLabel != null ) {
			    	txt = txt +  " [buildLabel=" + fw_buildLabel  +"]";		    		
		    	}
				txt = txt + "[" + getBuildTimeStamp() +"]";
				//txt = txt + "[" + getBuildTimeStamp1() +"]";
			} catch (Throwable e) {
				txt= txt + "[getBuildTimeStamp Failed, " + getStackTrace(e)+ "]";
				m_Log.error("[getBuildTimeStamp Failed, " + e.getMessage() + "]", e);
			}	
				txt= txt + "]";
			return txt;
		}
		
		public String getConfigInfo() {
			String txt ="";
			try {
				
				txt = txt + "[Config Info: " ;
				String creditGatewayServiceUrl = ConfigContext.getProperty("connections/SOA/serviceURLs/creditGatewayServiceUrl");
				txt = txt + "[creditGatewayServiceUrl =" + creditGatewayServiceUrl+"]";

				String consumerCustMgtServiceBaseUrl = ConfigContext.getProperty("connections/SOA/serviceURLs/consumerCustMgtServiceBaseUrl");
				txt = txt + "[consumerCustMgtServiceBaseUrl =" + consumerCustMgtServiceBaseUrl +"]";

				String consumerCustMgtServiceUrl = ConfigContext.getProperty("connections/SOA/serviceURLs/consumerCustMgtServiceUrl");
				txt = txt + "[consumerCustMgtServiceUrl =" + consumerCustMgtServiceUrl +"]";

				String enterpriseCreditAssessmentServiceUrl = ConfigContext.getProperty("connections/SOA/serviceURLs/enterpriseCreditAssessmentServiceUrl");
				txt = txt + "[enterpriseCreditAssessmentServiceUrl =" + enterpriseCreditAssessmentServiceUrl +"]";

				String enterpriseCreditProfileMgtServiceUrl = ConfigContext.getProperty("connections/SOA/serviceURLs/enterpriseCreditProfileMgtServiceUrl");
				txt = txt + "[enterpriseCreditProfileMgtServiceUrl =" + enterpriseCreditProfileMgtServiceUrl +"]";

				String wirelineCreditPrflDataMgtServiceBaseUrl = ConfigContext.getProperty("connections/SOA/serviceURLs/wirelineCreditPrflDataMgtServiceBaseUrl");
				txt = txt + "[wirelineCreditPrflDataMgtServiceBaseUrl =" + wirelineCreditPrflDataMgtServiceBaseUrl +"]";

				String wirelineCreditPrflDataMgtServiceUrl = ConfigContext.getProperty("connections/SOA/serviceURLs/wirelineCreditPrflDataMgtServiceUrl");
				txt = txt + "[wirelineCreditPrflDataMgtServiceUrl =" + wirelineCreditPrflDataMgtServiceUrl +"]";

				String wirelineCreditPrflMgtServiceBaseUrl = ConfigContext.getProperty("connections/SOA/serviceURLs/wirelineCreditPrflMgtServiceBaseUrl");
				txt = txt + "[wirelineCreditPrflMgtServiceBaseUrl =" + wirelineCreditPrflMgtServiceBaseUrl +"]";

				String wirelineCreditPrflMgtServiceUrl = ConfigContext.getProperty("connections/SOA/serviceURLs/wirelineCreditPrflMgtServiceUrl");
				txt = txt + "[wirelineCreditPrflMgtServiceUrl =" + wirelineCreditPrflMgtServiceUrl +"]";

				String wirelineCreditPrflMgtProxyServiceBaseUrl = ConfigContext.getProperty("connections/SOA/serviceURLs/wirelineCreditPrflMgtProxyServiceBaseUrl");
				txt = txt + "[wirelineCreditPrflMgtProxyServiceBaseUrl =" + wirelineCreditPrflMgtProxyServiceBaseUrl +"]";
				
				String wirelessCreditPrflDataMgtServiceUrl = ConfigContext.getProperty("connections/SOA/serviceURLs/wirelessCreditPrflDataMgtServiceUrl");
				txt = txt + "[wirelessCreditPrflDataMgtServiceUrl =" + wirelessCreditPrflDataMgtServiceUrl +"]";
				
				String wirelineCreditPrflMatchServiceBaseUrl = ConfigContext.getProperty("connections/SOA/serviceURLs/wirelineCreditPrflMatchServiceBaseUrl");
				txt = txt + "[wirelineCreditPrflMatchServiceBaseUrl =" + wirelineCreditPrflMatchServiceBaseUrl +"]";
				
			} catch (Throwable e) {
				txt= txt + "[getConfigInfo Failed., " + e.getMessage() + "]";
				m_Log.error(txt, e);			
			}
			txt= txt + "]  ";
	       	return txt;
		}

		private String getBuildTimeStamp() throws Throwable {
			String txt = "  ";
			try {
				InputStream in = getClass().getResourceAsStream("/version.txt");
				
				if (in != null) {
					BufferedReader br = new BufferedReader(new InputStreamReader(in));
					String aLine;
			        while ((aLine = br.readLine()) != null) { 
			          txt  = txt + " , " + aLine;
			         }  
					return " build details : " + txt  ;
				}else{
					txt = "Could not get the build date .";
				}
			} catch (Throwable e) {
				throw e;
			}
			return txt;
		}
		
		private String getBuildTimeStamp1() throws Throwable {
			String version ="could not get the implementation version";
			InputStream in =null;
			String txt ="";
			try {	
				 in = getClass().getClassLoader().getResourceAsStream("/META-INF/MANIFEST.MF");
			
			} catch (Throwable e) {
				m_Log.error("Failed to read file MANIFEST.MF", e);
				throw e;
			}				
			try {
				Manifest manifest = new Manifest(in);
				Attributes attributes = manifest.getMainAttributes();
				version =attributes.getValue("Implementation-Version");
				 if (version== null){
					 version="Could not find attribute Implementation-Version";
				 }				
				 txt =  "Implementation-Version = " + version  +
				 "Built-By = " +attributes.getValue("Built-By") +
				 "Build-Jdk = " +attributes.getValue("Build-Jdk");
				 

			} catch (Throwable e) {
				m_Log.error("Failed to get attribute Implementation-Version", e);
				throw e;
			}
			return txt;
		}	
	 
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
		
		private static String leadingTrailingEscapeChar( String str) {
			try{
			   str = str.startsWith("\"") ? str.substring(1) : str;
			   str = str.endsWith("\"") ? str.substring(0,str.length()-1) : str;
			}catch (Throwable e){}
			return str;
		}	
		
		private static String removeBrkLine(String str) {
			try{
				str = str.replaceAll("\\r\\n|\\r|\\n", " ");
			}catch (Throwable e){}
			return str;
		}	
	}