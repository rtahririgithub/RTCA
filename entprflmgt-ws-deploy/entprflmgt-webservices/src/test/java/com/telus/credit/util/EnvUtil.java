package com.telus.credit.util;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;


public class EnvUtil {
	public static String appCtxpropertiesFileName="appCtx.properties";
	public static String resourcesFolder= "src/main/resources"; // location of spring.xml
	public static String webinfSpring= "src/main/webapp/WEB-INF/spring"; // location of spring.xml
	public static String app_configFolder= "src/main/resources";	 //location of apCtx and other config files
	//public static String springCtxFileName_Test="wlscrda-ws-context-test.xml";//"wlscrda-ws-context.xml";
	//public static String springBatchCtxFileName_Test="wlscrda-batch-context-test.xml";//"wlscrda-ws-context.xml";
	//public static String springCtxFileName="wlscrda-ws-context-test.xml";

	public static void setupTestEnv() throws Exception {
		addPath(resourcesFolder); 
		addPath(webinfSpring); 
		addPath(app_configFolder); 
		System.setProperty("configAppCtxFile", appCtxpropertiesFileName);


		
		com.telus.framework.config.ldap.LdapNodeProvider  a;
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
