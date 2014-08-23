package com.decentralbank.decentralj;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Random;

import org.zeromq.ZMQ;

//Decentralized Hash Table for Voting Pool Members 
public class DHT {
	
    private RoutingTable routes;
    private String poolID;
    private int port;
 
    public DHT() {
    	
		this.routes = new RoutingTable(new Node());
		
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
    	
        return getRoutes().getID();
        
    }
    
    public void attach() {
    	
        ZMQ.Context context = ZMQ.context(1);

        // Socket to talk to clients
        ZMQ.Socket responder = context.socket(ZMQ.REP);
        responder.bind("tcp://*:" + getPort());
        
        //finish this
       /* while (!Thread.currentThread().isInterrupted()) {
            // Wait for next request from the client
            byte[] request = responder.recv(0);
            String messageType = new String(request).split("/")[0];

            if (messageType.contains(messageType)) {
  
                
                
                responder.send("OK".getBytes(), 0);
            } else {
                responder.send("ERR".getBytes(), 0);
            }
        }*/
        responder.close();
        context.term();
    }

    //implement automatic port binding between a range 13000-18888?
    private static int getAvailablePort() {
        int port = 18888;
        return port;
    }


}