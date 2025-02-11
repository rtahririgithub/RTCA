package com.telus.credit.crda.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.springframework.beans.factory.annotation.Autowired;

public class VersionUtils {
	@Autowired 
	protected LogUtil m_LogUtil; 
	public String getBuildTimeStamp() throws Throwable {
		String txt = "  ";
		try {
			InputStream in = getClass().getResourceAsStream("/version.txt");
			
			if (in != null) {
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String aLine;
		        while ((aLine = br.readLine()) != null) { 
		          txt  = txt + aLine + "  " ;
		         }  
				return txt;
			}else{
				txt = "Could not get the build date .";
			}
		} catch (Throwable e) {
			throw e;
		}
		return txt;
	}
	
	public   String getBuildTimeStamp1() throws Throwable {
		String version ="could not get the implementation version";
		InputStream in =null;
		String txt ="";
		try {	
			 in = getClass().getClassLoader().getResourceAsStream("/META-INF/MANIFEST.MF");
		
		} catch (Throwable e) {
			m_LogUtil.errorlog("VersionUtils Failed to read file MANIFEST.MF", e);
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
			m_LogUtil.errorlog("VersionUtils Failed to get attribute Implementation-Version", e);
			throw e;
		}
		return txt;
	}	
}
