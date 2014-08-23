package com.decentralbank.decentralj.core;

//This is to be used by a Node to send and receive requests
public class Request {
	
	private String status;
	private String peerID;
	private String message;
	
	public Request() {
		
	}
	
	public Request(String status, String peerID) {
		
		this.status = status;
		this.peerID = peerID;
		this.message = "";
		
	}	
	public Request(String status, String peerID, String responseMessage){
		
		this.status = status;
		this.peerID = peerID;
		this.message = responseMessage;
		
	}
	
	public String getStatus() {
		
		return status;
		
	}
	
	public void setStatus(String status) {
		
		this.status = status;
		
	}
	
	public String getPeerID() {
		
		return peerID;
		
	}

	public void setPeerID(String peerID) {
		
		this.peerID = peerID;
	
	}

	public String getMessage() {
	
		return message;
	
	}
	
	public void setMessage(String message) {
	
		this.message = message;
	
	}
	
	public String toString()
	{

		return this.getStatus()+" "+this.getPeerID()+" "+this.getMessage();
	
	}
	
	

}
