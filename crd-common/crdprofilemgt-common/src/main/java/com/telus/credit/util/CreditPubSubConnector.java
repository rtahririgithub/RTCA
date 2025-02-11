/**
 * 
 */
package com.telus.credit.util;

/**
 * @author x158788
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.misc.BASE64Encoder;


public class CreditPubSubConnector   {

	private static Log s_log = LogFactory.getLog(CreditPubSubConnector.class);
	
	public static final String  HTTPS_METHOD = "POST";
	public static final String  CONTENT_TYPE = "Content-Type";
	public static final String  CONTENT_TYPE_VALUE = "application/json";
	public static final String   ACCEPT="Accept";

	private String pubSubURL;


	private String pubSubTopic;

	private String username;

	private String password;
	
	private String envId;
	
	
	void CreditPubSubConnector(){
		
		if(envId!=null && !envId.equalsIgnoreCase("PR")){				
				pubSubTopic =envId +"." +pubSubTopic;
			}
			s_log.debug("CreditPubSubConnector : pubSubTopic :"+pubSubTopic);	
		
	}
	
	public String getPubSubURL() {
		return pubSubURL;
	}


	public void setPubSubURL(String pubSubURL) {
		this.pubSubURL = pubSubURL;
	}

	public String getPubSubTopic() {
		return pubSubTopic;
	}


	public void setPubSubTopic(String pubSubTopic) {
		this.pubSubTopic = pubSubTopic;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	public String getEnvId() {
		return envId;
	}

	public void setEnvId(String envId) {
		this.envId = envId;
	}

	private boolean envFlag = true;

	public  void publishMsgToPubSub( String messageString	) {
		
		s_log.debug("publishMsgToPubSub");
		HttpsURLConnection conn = null;
		s_log.debug("publishMsgToPubSub-> Input-> "+messageString);
//		System.out.println(envId);
		try {
			
		    if(envFlag && envId!=null && !envId.equalsIgnoreCase("PR")){		
               		 pubSubTopic =envId +"." +pubSubTopic;
					  envFlag	= false;
			   }
	        
			
			s_log.debug("CreditPubSubConnector : pubSubTopic :"+pubSubTopic);	
			
			String url =  pubSubURL + pubSubTopic;
			
			s_log.debug("CreditPubSubConnector : user :"+username +", password :"+password);
			s_log.debug("CreditPubSubConnector : url :"+url +", pubSubTopic :"+pubSubTopic);
			s_log.debug("CreditPubSubConnector : messageString :"+messageString);
//			System.out.println(messageString);
			//URL httpsurl = new URL(url);
			URL httpsurl = new URL(null, url, new sun.net.www.protocol.https.Handler());
			s_log.debug("httpsurl class : " + httpsurl.openConnection().getClass());
			conn = (HttpsURLConnection) httpsurl.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(HTTPS_METHOD);
			conn.setRequestProperty(CONTENT_TYPE, CONTENT_TYPE_VALUE);
			conn.addRequestProperty(ACCEPT, CONTENT_TYPE_VALUE);
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(15000);

			String userpassword =  username + ":" + password;
			conn.setRequestProperty("Authorization", "Basic "+new BASE64Encoder().encode(userpassword.getBytes()));

			OutputStream os = conn.getOutputStream();
			OutputStreamWriter writer =new OutputStreamWriter(os);
			writer.write(messageString);
			writer.flush();
			os.flush();
			writer.close();
			os.close();		


			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				s_log.error("Failed : HTTP error code : "+ conn.getResponseCode());
				throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
			}else{
				s_log.debug("publishMsgToPubSub -SUCCESS->"+conn.getResponseMessage());
				System.out.println("publishMsgToPubSub -SUCCESS->"+conn.getResponseMessage());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			
			while ((output = br.readLine()) != null) {
				s_log.debug("CreditPubSubConnector : output :"+output);
			}

		} catch (Throwable e) {
			s_log.error("Failure in Publishing To PubSub",e);
			//throw new RuntimeException("Failure in Publishing To PubSub",e);

		}finally{
			
		
			if(conn != null)
				conn.disconnect();
			
		}

	}

	

	
}
