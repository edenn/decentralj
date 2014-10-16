package com.decentralbank.decentralj.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import com.decentralbank.decentralj.core.Node;
import com.decentralbank.decentralj.core.Request;

import javax.portlet.ResourceRequest;

//Decentralized Hash Table for Voting Pool Members 
public class MiniDHT {
	
    private RoutingTable routes; // all routes of pool
    private String poolID; // id of the pool
    private int port;  	// port number for pool
    private HashMap<String, Request> identities = new HashMap<>(); // id to data 
    private ArrayList<DecentralPeer> peers = new ArrayList<DecentralPeer>(); // List of Peers
    
    public MiniDHT() {
		this.routes = new RoutingTable(Node.getInstance());
    }
    
    public MiniDHT(String poolID) {
        this();
        this.poolID = poolID;
        this.port = getAvailablePort();
    }

    public MiniDHT(int port, String poolID) {
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
        return getRoutes().getID();
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



}