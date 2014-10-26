package com.decentralbank.decentralj.core;

import java.math.BigInteger;

import com.decentralbank.decentralj.dht.DHT;
import com.decentralbank.decentralj.net.DecentralPeer;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.NetworkParameters;

public class Node {
	
	public static final int MAXCON=11;
	private String nextPeerID;
	private String id;
    private String status;
	private String port;
	private String poolId;
	private String[] poolMembers = new String[MAXCON];
	private String hostname;
	private String nextPeerHostName;
	private String nextPeerPort;
	private String redirectPort;
	private String redirectHostName;
	private NodeWallet wallet;
	private DHT poolDHT; //DHT to keep track of all votin pool members
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
		this.poolDHT = new DHT();
	}
	
	public String getID() {
		return id;
	}

	public void setID(String ID) {
		this.id = ID;
	}

	public String getPort() {
		return this.port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
	public String getPoolId() {
		return this.poolId;
	}
	
	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}

	public String [] getPoolMembers() {
		return poolMembers;
	}
	
	public void setPoolMembers(String [] members) {
		poolMembers = members.clone();
	}

	public String getNextPeerID() {
		return nextPeerID;
	}

	public void setNextPeerID(String nextPeerID) {
		nextPeerID = nextPeerID;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		hostname = hostname;
	}

	public String getNextPeerHostName() {
		return nextPeerHostName;
	}

	public void setNextPeerHostName(String NextPeerHostName) {
		this.nextPeerHostName = NextPeerHostName;
	}

	public String getNextPeerPort() {
		return nextPeerPort;
	}

	public void setNextPeerPort(String NextPeerPort) {
		this.nextPeerPort = NextPeerPort;
	}

	public String getRedirectHostName() {
		return redirectHostName;
	}

	public void setRedirectHostName(String redirectHostName) {
		redirectHostName = redirectHostName;
	}

	public String getRedirectPort() {
		return redirectPort;
	}

	public void setRedirectPort(String redirectPort) {
		redirectPort = redirectPort;
	}

	public DHT getDHT() {
        return this.poolDHT;
    }

	public void setDHT(DHT lehashtable) {
		poolDHT = lehashtable;
	}
	
	public NodeWallet getWallet(){
		return wallet;
	}
	
	public void createWallet() {
		if (wallet != null) {
		   return;
		}	
		wallet = new NodeWallet();
	}

    public String getStatus() {
        return this.status;
    }
	
	 // return String of object
	@Override
	public String toString(){
		return "Hostname: "+this.hostname+" Port: "+this.port+" ID: "+this.id+" NextHostname: "+this.nextPeerHostName+" NextPort: "+
			this.nextPeerPort+" NextID: "+this.nextPeerID+" Maximum connections: "+this.MAXCON+" Routing Table: "+this.getDHT().toString();
	}
	
	
	public void addPeer(DecentralPeer PeerModel){
		poolDHT.addPeer(PeerModel);
	}

    public void connectPool(String localID, String PoolID) {
        poolDHT.connect( localID, PoolID);
    }

    public void connectPeer(String localID, String PeerID) {
        DecentralPeer peer = new DecentralPeer();
        peer.connect(localID, PeerID);
    }

    public void disconnect() {
        poolDHT.disconnect();
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
