package com.telus.credit.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import sun.misc.BASE64Encoder;

public class Testpubsub {

	
	private String pubSubURL;

	public String getPubSubURL() {
		return pubSubURL;
	}


	public void setPubSubURL(String pubSubURL) {
		this.pubSubURL = pubSubURL;
	}


	public String getPubSubAction() {
		return pubSubAction;
	}


	public void setPubSubAction(String pubSubAction) {
		this.pubSubAction = pubSubAction;
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

	private String pubSubAction;

	private String pubSubTopic;

	private String username;

	private String password;
	
	private String messageContentType;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		publishMsgToPubSub("Hi");
	}

	
	protected static void publishMsgToPubSub( String messageString	) {
		
		HttpsURLConnection conn = null;
		
		try {
			
			

			URL httpsurl = new URL("https://enterprisemessagewebk-is02.tsl.telus.com/publisher/publish/PT05.Party.Customer.CreditInfo");
			conn = (HttpsURLConnection) httpsurl.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.addRequestProperty("Accept", "application/json");
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(15000);

			String userpassword =  "9343" + ":" + "LvQX&98B";
			conn.setRequestProperty("Authorization", "Basic "+new BASE64Encoder().encode(userpassword.getBytes()));

			OutputStream os = conn.getOutputStream();
			OutputStreamWriter writer =new OutputStreamWriter(os);
			writer.write(messageString);
			writer.flush();
			os.flush();
			writer.close();
			os.close();		


			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				
				throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

		} catch (MalformedURLException e) {

			//s_log.error("Failure in Publishing To PubSub",e);
			throw new RuntimeException("Failure in Publishing To PubSub",e);

		} catch (IOException e) {

			//s_log.error("Failure in Publishing To PubSub",e);
			throw new RuntimeException("Failure in Publishing To PubSub",e);

		}finally{
			
		
			if(conn != null)
				conn.disconnect();
			
		}

	}

	protected String getHttpConnectionString() {

		String urlString = "https://enterprisemessagewebk-is02.tsl.telus.com/publisher/publish/PT05.Party.Customer.CreditInfo" ;
			

		return urlString;

	}



}
