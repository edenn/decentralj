package com.decentralbank.decentralj.core;

import java.math.BigInteger;

import com.decentralbank.decentralj.net.MiniDHT;
import com.decentralbank.decentralj.net.DecentralPeer;
import com.decentralbank.decentralj.net.RoutingTable;
import com.decentralbank.decentralj.net.ServerThread;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.NetworkParameters;

public class Node {
	
	public static final int MAXCON=11;
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
	private NodeWallet wallet;
	private MiniDHT poolDHT; //DHT to keep track of all votin pool members
	private static Node instance = new Node();
	
	//get instance of node
	public static synchronized Node getInstance() {
	      return instance;
	}
	
	//override clone method
	@Override
	public Object clone() throws CloneNotSupportedException {
		 throw new CloneNotSupportedException();
	}
	
	public Node() {
		this.poolDHT = new MiniDHT();
	}
	
	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		ID = ID;
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


	public MiniDHT getDHT() {
		return poolDHT;
	}

	
	public void setDHT(MiniDHT lehashtable) {
		poolDHT = lehashtable;
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
			this.NextPeerPort+" NextID: "+this.NextPeerID+" Maximum connections: "+this.MAXCON+" Hashtable: "+this.getDHT().toString();
	
	}
	
	
	public void addPeer(DecentralPeer PeerModel){
		
		poolDHT.addPeer(PeerModel);	
	
	}
	
	
	//testing
	public static void main(String [] args) throws AddressFormatException {
		
		Node lenode = Node.getInstance();
		NodeWallet lewallet = lenode.getWallet();
		BigInteger leinteger = BigInteger.valueOf(20000000);
		System.out.print(leinteger);
		 NetworkParameters netParams = NetworkParameters.testNet();
		// leaddress.equals("mipcBbFg9gMiCh81Kj8tqqdgoZub1ZJRfn");
		 Address targetAddress = new Address(netParams, "mipcBbFg9gMiCh81Kj8tqqdgoZub1ZJRfn");
		//Address.getParametersFromAddress("mipcBbFg9gMiCh81Kj8tqqdgoZub1ZJRfn");
		//lewallet.setBalance(targetAddress, leinteger);
		//BigInteger result = lewallet.getBalance(leaddress);
		System.out.print(targetAddress.toString());
		
	}
	

}
