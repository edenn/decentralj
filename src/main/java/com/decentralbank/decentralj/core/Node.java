package com.decentralbank.decentralj.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;

public class Node {
	
	private static final int MAXCON=11;
	private String NextPeerID;
	private String ID;
	private String Port;
	private String PoolId;
	private String[] PoolMembers = new String[MAXCON];
	private String Hostname;
	private String NextPeerHostName;
	private String NextPeerPort;
	private String RedirectPort;
	private String RedirectHostName;
	private NodeWallet NodeWallet;
	//a hashtable to keep track of nodes
	private Hashtable<Integer, String> hashtable;

	
	public int getMaxcon() {
		return MAXCON;
	}
	
	public Node(){
		this.hashtable = new Hashtable<Integer, String>();
	}
	
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getPort() {
		return Port;
	}

	public void setPort(String port) {
		Port = port;
	}
	
	public String getPoolId() {
		return PoolId;
	}
	
	public void setPoolId(String poolId) {
		PoolId = poolId;
	}

	public String [] getPoolMembers() {
		return PoolMembers;
	}
	
	public void setPoolMembers(String [] members) {
		PoolMembers = members.clone();
	}

	public String getNextPeerID() {
		return NextPeerID;
	}

	public void setNextPeerID(String nextPeerID) {
		NextPeerID = nextPeerID;
	}

	public String getHostname() {
		return Hostname;
	}

	public void setHostname(String hostname) {
		Hostname = hostname;
	}

	public String getNextPeerHostName() {
		return NextPeerHostName;
	}

	public void setNextPeerHostName(String NextPeerHostName) {
		this.NextPeerHostName = NextPeerHostName;
	}

	public String getNextPeerPort() {
		return NextPeerPort;
	}

	public void setNextPeerPort(String NextPeerPort) {
		this.NextPeerPort = NextPeerPort;
	}

	public String getRedirectHostName() {
		return RedirectHostName;
	}

	public void setRedirectHostName(String redirectHostName) {
		RedirectHostName = redirectHostName;
	}

	public String getRedirectPort() {
		return RedirectPort;
	}

	public void setRedirectPort(String redirectPort) {
		RedirectPort = redirectPort;
	}


	public Hashtable<Integer, String> getHashtable() {
		return hashtable;
	}

	
	public void setHashtable(Hashtable<Integer, String> lehashtable) {
		hashtable = lehashtable;
	}
	
	public NodeWallet getWallet(){
		
		return NodeWallet;
		
	}
	
	public void createWallet(){
		
		
		
	}
	
	 // return String of object
	@Override
	public String toString(){
		return "Hostname: "+this.Hostname+" Port: "+this.Port+" ID: "+this.ID+" NextHostname: "+this.NextPeerHostName+" NextPort: "+
			this.NextPeerPort+" NextID: "+this.NextPeerID+" Maximum connections: "+this.MAXCON+" Hashtable: "+this.getHashtable().toString();
	}
	
	public void addPeers(){
		
	}
	

}
