package com.decentralbank.decentralj;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

public class BlockchainInfo {
	private String root="https://blockchain.info/";
	//TODO: make sure to validate ssl cert
	// use SOCKS5
	//example addresses to check 1F6UU9EBPNyAFyPojDqHAtoCiNDX9mFmBP|1GDHkj6EoeQiG3HeUgdUnxLgL8pj1A2yAp|1H4WfM5u1sCQEQZLWeHXaKBBq6kcXknzrd
	
	//query unspent inputs given addresses array
	public String unspent(String [] addresses){
		  
		  String result="";
		  StringBuilder builder = new StringBuilder();
		  
		  for(String s : addresses) {
		      builder.append(s+"|");
		  }
		  
		  String values= builder.toString();
		  String https_url=root+"unspent?active="+values;
		  
		  try {
		
				URL url = new URL(https_url);
				HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
				result = print_content(conn);
				 
				       
		  }catch (IOException e) {
			   e.printStackTrace();
			}
		  

		  return result;
	}
	
	//query all balances from an addresses array
	public String multiaddr(String [] addresses){
		 
		 String result="";
		 StringBuilder builder = new StringBuilder();
		 
		 for(String s : addresses) {
		     builder.append(s+"|");
		 }
		 
		 String values= builder.toString();	
		 String https_url=root+"mutiaddr?active="+values;
		 try {
			 
				URL url = new URL(https_url);
				HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ conn.getResponseCode());
				}
		 
				result = print_content(conn);
		 
			  } catch (MalformedURLException e) {
		 
				e.printStackTrace();
		 
			  } catch (IOException e) {
		 
				e.printStackTrace();
		 
			  }
		return result;		
		
		
	}
	
	// broadcast a transaction
	public String pushTx(String tx_serialized, String tx_hash){
		
		 String PostData="";
		 
		/* Map<String, String> dictionary = new HashMap<String, String>();
		 dictionary.put("format","plain");
		 dictionary.put("tx", tx_serialized);
		 dictionary.put("hash", tx_hash);*/
		 
		 String result="{format:'plain',tx:'"+tx_serialized+"',hash:'"+tx_hash+"'}";
		 String https_url=root+"mutiaddr?pushtx/";
		 
		 try {
			 
				URL url = new URL(https_url);
				HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
				conn.setRequestProperty("charset", "utf-8");
				
				
				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ conn.getResponseCode());
				}
				conn.setRequestProperty("Content-Length", "" + Integer.toString(PostData.getBytes().length));
				conn.setUseCaches (false);
				
				result = print_content(conn);
		 
			  } catch (MalformedURLException e) {
		 
				e.printStackTrace();
		 
			  } catch (IOException e) {
		 
				e.printStackTrace();
		 
			  } 
		 
		return result;
			
	}
	
	 private String print_content(HttpsURLConnection con){
		 
		 	String result="";
		 	
			if(con!=null){
		 
			try {
		 
			   System.out.println("****** Content of the URL ********");			
			   BufferedReader br = 
				new BufferedReader(
					new InputStreamReader(con.getInputStream()));
		 
			   String input;
		 
			   while ((input = br.readLine()) != null){
			      result=input;
			   }
			   br.close();
		 
			} catch (IOException e) {
			   e.printStackTrace();
			}
		 
		       }
			return result;
			
		   }

}
