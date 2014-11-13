package com.decentralbank.decentralj.dht;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import com.decentralbank.decentralj.dht.interfaces.IDHTSerializer;
import com.decentralbank.decentralj.dht.interfaces.IKademliaDHT;
import com.decentralbank.decentralj.dht.interfaces.IKademliaStorageDataEntry;
import com.decentralbank.decentralj.dht.interfaces.IKademliaStorageDataContent;
import com.decentralbank.decentralj.net.DecentralPeer;
import com.decentralbank.decentralj.routingtable.RoutingTable;
import org.zeromq.ZMQ;

import com.decentralbank.decentralj.core.Node;
import com.decentralbank.decentralj.core.Request;

//Decentralized Hash Table for Voting Pool Members 
public class DHT implements IKademliaDHT {
	
    private RoutingTable routes; // all routes of pool
    private String poolID; // id of the pool
    private int port;  	// port number for pool
    private HashMap<String, Request> identities = new HashMap<>(); // id to data 
    private ArrayList<DecentralPeer> peers = new ArrayList<DecentralPeer>(); // List of Peers
    
    public DHT() {
		//this.routes = new RoutingTable(Node.getInstance());
    }
    
    public DHT(String poolID) {
        this();
        this.poolID = poolID;
        this.port = getAvailablePort();
    }

    public DHT(int port, String poolID) {
        this();
        this.poolID = poolID;
        this.port = port;
    }

    //get Routes
    public RoutingTable getRoutes() {
        return routes;
    }

    //set Routes
    public void setRoutes(RoutingTable routes) {
        this.routes = routes;
    }

    //get Pool ID
    public String getPoolID() {
        return poolID;
    }

    //set Pool Id
    public void setPoolID(String poolID) {
        this.poolID = poolID;
    }

    //get Port
    public int getPort() {
        return port;
    }

    //set Port
    public void setPort(int port) {
    	this.port = port;
    }

    //get Node ID
    public String getNodeID() {
       // return getRoutes().getID();
        return null;
    }
    
    //handle connections to a node
    public void connect(String ID,String peerId) {
        //initialize request packet
        Request packet = new Request();
        //set local id to packet
        packet.setPeerID(ID);
        //Node Status(if available accept new petitions to form pool)
        packet.setStatus("available");
        packet.setMessage(Node.getInstance().toString()); //redo this
        //open context
        ZMQ.Context context = ZMQ.context(1); 
        //get Node instance
        Node localhost = Node.getInstance();
        // Socket to talk to servers
        ZMQ.Socket responder = context.socket(ZMQ.DEALER);
        //set socket identifier to the localhost id
        responder.setIdentity(localhost.getID().getBytes());
        //connect to a ROUTER socket
        responder.connect("tcp://*:" + getPort());
        while (!Thread.currentThread().isInterrupted()) {
            byte[] request = responder.recv(0);
            String messageType = new String(request).split("/")[0];
            responder.send("Hello");
            responder.sendMore(packet.toString());
            //implement message handling
            if (messageType.contains(messageType)) {
               // Request response = handlers.get(messageType).
          
                responder.send("OK".getBytes(), 0);
            } else {
                responder.send("ERR".getBytes(), 0);
            }
        }

        responder.close();
        context.term();
    }

    //get available port at 8888
    private static int getAvailablePort() {
        int port = 0;
        do {
            port = 8888;
        } while (!isPortAvailable(port));

        return port;
    }

    //check for an available port
    private static boolean isPortAvailable(final int port) {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            return true;
        } catch (final IOException e) {
        } finally {
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e2) {
                }
            }
        }

        return false;
    }

    public void disconnect() {
    }

	public void addPeer(DecentralPeer peerModel) {
		peers.add(peerModel);
    }


    @Override
    public void initialize() {

    }

    @Override
    public IDHTSerializer<IKademliaStorageDataEntry> getSerializer() {
        return null;
    }

    @Override
    public boolean store(IKademliaStorageDataEntry content) throws IOException {
        return false;
    }

    @Override
    public boolean store(PeerContent content) throws IOException {
        return false;
    }

    @Override
    public IKademliaStorageDataEntry retrieve(String key, int hashCode) throws FileNotFoundException, IOException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean contains(String parameter) {
        return false;
    }

    @Override
    public IKademliaStorageDataEntry retrieve(Contact key, int hashCode) throws FileNotFoundException, IOException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean contains(DHTParam param) {
        return false;
    }

    @Override
    public IKademliaStorageDataEntry get(IKademliaStorageDataContent entry) throws IOException, NoSuchElementException {
        return null;
    }

    @Override
    public IKademliaStorageDataEntry get(String parameter) throws NoSuchElementException, IOException {
        return null;
    }

    @Override
    public void remove(IKademliaStorageDataContent entry) throws NoSuchElementException {

    }

    @Override
    public List<IKademliaStorageDataContent> getStorageEntries() {
        return null;
    }

    @Override
    public void putStorageEntries(List<IKademliaStorageDataContent> entries) {

    }
}