package com.telus.credit.crda.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.springframework.beans.factory.annotation.Autowired;

public class ApplicationRuntimeHelper {
	@Autowired
	protected LogUtil m_LogUtil;

	private String jmxPrincipal;
	
	private String jmxCredential;
	
	private String applicationName;
	
	private String domainName;
	
	private String nodeName;
	
	private String serverVersion;
	
	private String serverString="";
	
	private List<ObjectName> registeredObjectNames = new ArrayList<ObjectName>();
	
	public ApplicationRuntimeHelper(String applicationName, String jmxPrincipal, String jmxCredential) {
		this.applicationName = applicationName;
		this.jmxPrincipal = jmxPrincipal;
		this.jmxCredential = jmxCredential;
		
		MBeanServer server = getMBeanServer();
		if (server != null) {
			try {

				ObjectName runtimeServiceName = new ObjectName("com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");
				nodeName = (String) server.getAttribute(runtimeServiceName, "ServerName");

				ObjectName domainRuntime = (ObjectName) server.getAttribute(runtimeServiceName, "DomainConfiguration");
				domainName = (String) server.getAttribute(domainRuntime, "Name");
				serverVersion = (String) server.getAttribute(domainRuntime, "ConfigurationVersion");
				serverString =server.toString();
			} catch (Exception e) {	
				m_LogUtil.errorlog(this.getClass(), "Error accessing MBean server: " + e.getMessage());
			}
		}
	}
	
	private MBeanServer getMBeanServer() {
		Context context = getInitialContext();
		
		MBeanServer server = null;

		if (context != null) {
			server = getMBeanServer("java:comp/env/jmx/runtime", context);
			if (server == null) {
				server = getMBeanServer("java:comp/jmx/runtime", context);
			}
			close(context);
		}
		return server;
	}
	
	private MBeanServer getMBeanServer(String jndiName, Context context) {
		try {
			return (MBeanServer) context.lookup(jndiName); 
		} catch (Exception e) {
			m_LogUtil.errorlog(this.getClass(),"unable to locate MBean server using jndiName [" + jndiName + "]: " + e.getMessage());
	
		}
		return null;
	}
	
	private Context getInitialContext() {
		Hashtable<String, String> env = new Hashtable<String, String>();
		
		if (jmxPrincipal != null) {
			env.put(Context.SECURITY_PRINCIPAL, jmxPrincipal);
		}
		if (jmxCredential != null) {
			env.put(Context.SECURITY_CREDENTIALS, jmxCredential);
		}
		
		try {

			return new InitialContext(env);
			
		} catch (Throwable t) {
			m_LogUtil.errorlog(this.getClass(),"Unable to obtain initial context using principal [" + (jmxPrincipal == null ? "anonymous" : jmxPrincipal) + "]: " + t.getMessage());
		}
		return null;
	}	

	private void close(Context context) {
		try {
			if (context != null) {
				context.close();
			}
		} catch (Exception e) {
			m_LogUtil.errorlog(this.getClass(),"Error closing naming context: " + e.getMessage(), e);
		}
	}

	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @return the domainName
	 */
	public String getDomainName() {
		return domainName;
	}

	/**
	 * @return the nodeName
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @return the serverVersion
	 */
	public String getServerVersion() {
		return serverVersion;
	}
	public String getServerString() {
		if(serverString!=null && !serverString.isEmpty()){
			serverString="[" + serverString +"]";
		}
		return serverString;
	}
	public void registerMBean(StandardMBean bean, String name) {
		try {
			MBeanServer server = getMBeanServer();
			if (server != null) {
				ObjectName objectName = new ObjectName(name);

				if (server.isRegistered(objectName)) {
					server.unregisterMBean(objectName);
				}
				
				server.registerMBean(bean, objectName);
				registeredObjectNames.add(objectName);
			}
		} catch (Exception e) {
			m_LogUtil.errorlog(this.getClass(),"Error registering MBean [" + bean + "] under name [" + name + "]: " + e.getMessage(), e);
		}
	}
	
	public void unregisterAllMBeans() {
		MBeanServer server = getMBeanServer();
		if (server != null) {
			for (ObjectName objectName : registeredObjectNames) {
				try {
					m_LogUtil.errorlog(this.getClass(),"Unregistering MBean for name [" + objectName + "].");
					server.unregisterMBean(objectName);
				} catch (Exception e) {
					m_LogUtil.errorlog(this.getClass(),"Unable to unregister mbean for name [" + objectName + "]: " + e.getMessage());
				}
			}
		}
	}

}
