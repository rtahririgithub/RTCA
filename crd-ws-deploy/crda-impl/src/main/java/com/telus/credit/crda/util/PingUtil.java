package com.telus.credit.crda.util;

import org.springframework.beans.factory.annotation.Autowired;

import com.blazesoft.server.base.NdServerException;
import com.blazesoft.server.base.NdServiceException;
import com.blazesoft.server.base.NdServiceSessionException;
import com.fico.telus.rtca.blaze.RuleServicesBean;
import com.telus.erm.refpds.access.client.ReferencePdsAccess;
import com.telus.framework.config.ConfigContext;

public class PingUtil {
	
	private static PingUtil aPingUtil;
	
	public static PingUtil getInstance() {
		if(aPingUtil==null) {
			aPingUtil = new PingUtil();
		}
		return aPingUtil;
	}
	
	@Autowired
	private RuleServicesBean m_ruleServicesBean;
	
	public String getpingResultTxt() {
		String txt = "";
		String breakln = System.getProperty("line.separator");

		txt = txt + breakln;
		txt = txt + "[EnterpriseCreditAssessmentService_2_1 ping result:" + breakln;

		// build info
		txt = txt + getBuildinfo() + breakln;
		// Fico shakedown
		txt= txt + ficoShakedown()+ breakln;
		// getLdapData
		// txt= txt + getLdapData()+ breakln;

		// shakedown_RefPDS
		txt = txt + shakedown_RefPDS() + breakln;

		// crypto
		// txt= txt + shakedown_crypto_Data()+ breakln;

		// SourceOfLog4JUrl
		txt = txt + getSourceOfLog4JUrl() + breakln;

		// fw_Meta_Data
		txt = txt + getFramework_Meta_Data() + breakln;

		// credit Gateway svc
		// txt= txt + shakedown_CreditGatewaySvc()+ breakln;

		// ApplicationRuntime Details
		//txt = txt + getApplicationRuntimeHelperDetails() + breakln;

		return txt;
	}

	private String ficoShakedown() {
		
		String msg = "[FicoPing: ";
		if(m_ruleServicesBean!=null) {
			try {
				msg += m_ruleServicesBean.getRulesServer().ping();
			} catch (Exception e) {
				msg += e.getMessage();
				e.printStackTrace();
			}
		} else {
			msg += "RuleServicesBean is null";
		}
		return  msg += "]";
	}

	private String getApplicationRuntimeHelperDetails() {
		String txt="";
		
		try {
			txt =txt +"[ApplicationRuntimeHelperDetails:";
			String applicationName = "default-application";
			ApplicationRuntimeHelper applicationRuntimeHelper = new ApplicationRuntimeHelper(applicationName, "devmonitor", "monitor1");
			
			txt =txt + "DomainName=" + applicationRuntimeHelper.getDomainName();
			txt =txt + ",NodeName=" + applicationRuntimeHelper.getNodeName();
			//txt =txt + ",ApplicationName=" + applicationRuntimeHelper.getApplicationName();
			txt =txt + ",ServerVersion=" + applicationRuntimeHelper.getServerVersion();
			txt =txt +  applicationRuntimeHelper.getServerString() ;
			txt =txt +"]";
		} catch (Throwable e) {
			//txt =txt +"Failed to get ApplicationRuntime";
		}
				
		return txt ;
	}


	private  String shakedown_RefPDS() {
		String txt;
		txt = "  [RefPds LastRefreshDateAndTime:[";
		try {
			txt = txt + ReferencePdsAccess.getLastRefreshDateAndTime() + "]";
		} catch (Throwable e) {
			txt = txt + "RefPDS Ping  failed." + CommonUtility.getStackTrace(e);
			LogUtil.getInstance().errorlog(PingUtil.class,
					"RefPDS Ping  failed." + CommonUtility.getStackTrace(e));
		}
		return txt;
	}
	private  String getSourceOfLog4JUrl() {
		String txt="";
    	try{
    		txt = txt +  "  [getSourceOfLog4JUrl:" ;
    		txt= txt + CommonUtility.getSourceOfLog4JUrl() ;
    	} catch (Throwable e) {
			txt= txt + "[getSourceOfLog4JUrl Failed, ";// + CommonUtility.getStackTrace(e)+ "]";
			LogUtil.getInstance().errorlog("PingUtil.class [getSourceOfLog4JUrl Failed, ");// + CommonUtility.getStackTrace(e)+ "]");
    	}
    	txt= txt + "] ";
		return txt;
	}
	private  String getBuildinfo() {
		String txt ="";
		try {
			txt = txt + "[Build Info: " ;
			txt= txt + new VersionUtils().getBuildTimeStamp();
		} catch (Throwable e) {
			txt= txt + "[getBuildTimeStamp Failed, " + CommonUtility.getStackTrace(e)+ "]";
			LogUtil.getInstance().errorlog(PingUtil.class, "[getBuildTimeStamp Failed, " + CommonUtility.getStackTrace(e)+ "]");
		}	
			txt= txt + "]";
		return txt;
	}
	private  String getFramework_Meta_Data() {
		String fw_appId = ConfigContext.getProperty("fw_appId");
		String fw_appName = ConfigContext.getProperty("fw_appName");
		String fw_appVersion = ConfigContext.getProperty("fw_appVersion");
		String fw_appOwner = ConfigContext.getProperty("fw_appOwner");
		String fw_cmdbAppId = ConfigContext.getProperty("fw_cmdbAppId");
		String fw_deploymentFile = ConfigContext.getProperty("fw_deploymentFile");		
	  	String fw_buildDate = ConfigContext.getProperty("fw_buildDate");
    	String fw_buildLabel = ConfigContext.getProperty("fw_buildLabel");
	    String txt = "  [fw_Meta_Data:  ";
    	 txt = txt +  "[fw_appId=" + fw_appId  +"]";
    	 txt = txt +  "[fw_appName=" + fw_appName  +"]";
    	 txt = txt +  "[fw_appVersion=" + fw_appVersion  +"]";
    	 txt = txt +  "[fw_cmdbAppId=" + fw_cmdbAppId  +"]";
    	 txt = txt +  "[fw_deploymentFile=" + fw_deploymentFile  +"]";
    	 txt = txt +  "[fw_buildDate=" + fw_buildDate  +"]";
    	 txt = txt +  "[fw_buildLabel=" + fw_buildLabel  +"]";
    	 txt = txt +  "] ";
    	 txt= txt + "] ";
		
		return txt;
	}

	public RuleServicesBean getRuleServicesBean() {
		return m_ruleServicesBean;
	}

	public void setRuleServicesBean(RuleServicesBean m_ruleServicesBean) {
		this.m_ruleServicesBean = m_ruleServicesBean;
	}
}
