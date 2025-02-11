package com.fico.telus.rtca.testUtil;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;



public class EnvUtil {
	public static String environmentFile_Folder="src/test/resources/config";
	public static String appCtxpropertiesFile_Folder= "src/test/resources/config";	
	
	public static String appCtxpropertiesFileName="appCtx.properties";	
	public static String springfile_Folder= "src/main/resources";


	public static void setupTestEnv() throws Exception {
		addPath(environmentFile_Folder); 
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
