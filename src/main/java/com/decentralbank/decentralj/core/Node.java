package com.decentralbank.decentralj.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;

import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.NetworkParameters;

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
	private static NodeWallet wallet;
	//a hashtable to keep track of nodes
	private Hashtable<Integer, String> hashtable;

	public static void main(String [] args) throws AddressFormatException {
		
		Node lenode = new Node();
		NodeWallet lewallet = lenode.getWallet();
		BigInteger leinteger = BigInteger.valueOf(20000000);
		System.out.print(leinteger);
		 NetworkParameters netParams = NetworkParameters.testNet();
		// leaddress.equals("mipcBbFg9gMiCh81Kj8tqqdgoZub1ZJRfn");
		 Address targetAddress = new Address(netParams, "mipcBbFg9gMiCh81Kj8tqqdgoZub1ZJRfn");
		//Address.getParametersFromAddress("mipcBbFg9gMiCh81Kj8tqqdgoZub1ZJRfn");
		lewallet.setBalance(targetAddress, leinteger);
		//BigInteger result = lewallet.getBalance(leaddress);
		System.out.print(targetAddress.toString());
		
	}
	
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
		
		return wallet;
		
	}
	
	public void createWallet(){
		
		if (wallet != null) {
		   return;
		}	
		wallet = new NodeWallet();
		
	}
	
	 // return String of object
	@Override
	public String toString(){
		return "Hostname: "+this.Hostname+" Port: "+this.Port+" ID: "+this.ID+" NextHostname: "+this.NextPeerHostName+" NextPort: "+
			this.NextPeerPort+" NextID: "+this.NextPeerID+" Maximum connections: "+this.MAXCON+" Hashtable: "+this.getHashtable().toString();
	}
	
	public void addPeer(int PeerID, String PeerHash){
		
		
		
	}
	

}
