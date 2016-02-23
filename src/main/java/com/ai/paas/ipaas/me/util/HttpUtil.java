package com.ai.paas.ipaas.me.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class HttpUtil {
	private static int connectionTimeout = 15000;                
    
	private static int readTimeout = 15000;          
	
	private static final transient Logger LOG = LoggerFactory.getLogger(HttpUtil.class);


	/**
	 * @param url
	 * @param message
	 * @return
	 */
	public static String sendMessageToEndPoint(String url, String message) {
		HttpURLConnection connection = null;
		BufferedReader in = null;
		String result = "";
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Attempting to access " + url);
			}
			URL logoutUrl = new URL(url);
			String output = "";
			if(message!=null){
				if (LOG.isDebugEnabled()) 
					LOG.debug("-----send msg {}------",message);
				output = URLEncoder.encode(message, "UTF-8");
			}

			connection = (HttpURLConnection) logoutUrl.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setReadTimeout(readTimeout);
			connection.setConnectTimeout(connectionTimeout);
			if(message!=null){
				connection.setRequestProperty("Content-Length", ""
						+ Integer.toString(output.getBytes().length));
			}

			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			if(message!=null){
				DataOutputStream printout = new DataOutputStream(connection
						.getOutputStream());
	
				printout.writeBytes(output);
				printout.flush();
				printout.close();
			}

			in = new BufferedReader(new InputStreamReader(connection
					.getInputStream()));
			String s = null;
			while ((s=in.readLine()) != null){
				result += s;
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("-----send msg result {}------",result);
				LOG.debug("Finished sending message to " + url);
			}
//			connection.getResponseCode()
			return result;
		} catch (SocketTimeoutException e) {
			LOG.warn("Socket Timeout Detected while attempting to send message to ["
							+ url + "].");
			return "-1";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return "-2";
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (connection != null)
				connection.disconnect();
		}
	}
}
