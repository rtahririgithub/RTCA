package com.telus.credit.util;

import java.util.Set;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.InitialContext;
public class WeblogicRunTimeUtil {

	private static WeblogicRunTimeUtil instance = new WeblogicRunTimeUtil();
	private static String domainName;
	private static String managedServerName;

    public WeblogicRunTimeUtil() {
    }

    private static WeblogicRunTimeUtil getInstance() {
        return instance;
    }

    private static String getDomainName() {
        if (domainName == null) {
            try {
                MBeanServer server = getMBeanServer();
                ObjectName domainMBean = (ObjectName) server.getAttribute(getRuntimeService(), "DomainConfiguration");
                domainName = (String) server.getAttribute(domainMBean, "Name");
            } catch (Exception ex) {
            	domainName="";
            }
        }

        return domainName;
    }

/*    private static String getManagedServerName() {
        if (managedServerName == null) {
            try {
            	 MBeanServer server = getMBeanServer();
                managedServerName = (String) server.getAttribute(getRuntimeService(), "ServerName");
            } catch (Exception ex) {
            }
        }

        return managedServerName;
    }*/

    private static MBeanServer getMBeanServer() {
        MBeanServer retval = null;
        InitialContext ctx = null;

        try {
            //fetch the RuntimeServerMBean using the
            //MBeanServer interface
            ctx = new InitialContext();
            retval = (MBeanServer) ctx.lookup("java:comp/env/jmx/runtime");

        } catch (Exception ex) {
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception dontCare) {
                }
            }
        }

        return retval;
    }

    private static ObjectName getRuntimeService() {
        ObjectName retval = null;

        try {
            retval = new ObjectName("com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");
        } catch (Exception ex) {
        }
        return retval;
    }
    public static String getEnvId() {
    	String defaultEnvname ="DV01";
    	//output 
    	String envname =defaultEnvname;
   	
    	String domainname = getDomainName();
    	domainname = (domainname!=null)?domainname:"";
    	String esbDomain="CustomerManagementWestESB";
    	try{
	    	int domainnameIndex = domainname.indexOf(esbDomain);    
	    	if(domainnameIndex!= -1){
	    		envname = domainname.substring(0, domainnameIndex);
	    	}
    	}catch (Throwable e){
    		envname=defaultEnvname;
    	}
    	return envname;

	}
    
/*    public static String getAllRunTimeAttributes(){
    	String allRunTimeAttributes="";
    	MBeanServer mBeanServer = getMBeanServer();
    	if(mBeanServer== null )
    		return "";
    	Set mbeans = mBeanServer.queryNames(null, null);
    	for (Object mbean : mbeans)
    	{
    		allRunTimeAttributes = allRunTimeAttributes + getAttributes(mBeanServer, (ObjectName)mbean);
    	}
    	return allRunTimeAttributes;
    }  
	private static String getAttributes(final MBeanServer mBeanServer, final ObjectName http)
	{
		String output="";
	    try {
			MBeanInfo info = mBeanServer.getMBeanInfo(http);
			MBeanAttributeInfo[] attrInfo = info.getAttributes();


			output = output + ("Attributes for object: " + http +":\n");
			for (MBeanAttributeInfo attr : attrInfo)
			{
				output = output + ("  " + attr.getName() + "  " + attr.getType() + "  " + attr.getDescriptor() +"\n");
				
			}
		} catch (Throwable e) {
		} 
	    return output;
	}*/
         
/*     
    public static void main(String[] args) {
    	getAllRunTimeAttributes();
    	String envname= getEnvId();
    	System.out.println("envname=" + envname);
	}*/
    
}