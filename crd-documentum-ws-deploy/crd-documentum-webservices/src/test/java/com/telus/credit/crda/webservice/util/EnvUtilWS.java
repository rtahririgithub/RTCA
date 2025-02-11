package com.telus.credit.crda.webservice.util;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;



public class EnvUtilWS {

	public static String environmentFile_Folder="/../../app-config/test";
	public static String appCtxpropertiesFile_Folder= "/src/main/resources";	
	public static String appCtxpropertiesFileName="appCtx.properties";	
	public static String springfile_Folder= "src/main/resources";
	

	public static void setupTestEnv() throws Exception {
	
		String currentlyrunningcontextFolder = System.getProperty("user.dir");
		String enviromentFile_FullPath= currentlyrunningcontextFolder+environmentFile_Folder;
		addPath(enviromentFile_FullPath); 
		addPath(appCtxpropertiesFileName);
		
		addPath(springfile_Folder); 
		addPath(appCtxpropertiesFile_Folder); 
		System.setProperty("configAppCtxFile", appCtxpropertiesFileName);
	}	

	public static void addPath(String s) throws Exception {
	    File f = new File(s);
	    URI u = f.toURI();
	    URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
	    Class<URLClassLoader> urlClass = URLClassLoader.class;
	    Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
	    method.setAccessible(true);
	    method.invoke(urlClassLoader, new Object[]{u.toURL()});
	}	
}
