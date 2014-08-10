package com.decentralbank.decentralj;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Random;

import org.zeromq.ZMQ;

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

    public RoutingTable getRoutes() {
    	
        return routes;
        
    }

    public void setRoutes(RoutingTable routes) {
    	
        this.routes = routes;
        
    }

    public String getPoolID() {
    	
        return poolID;
        
    }

    public void setPoolID(String poolID) {
    	
        this.poolID = poolID;
        
    }

    public int getPort() {
    	
        return port;
    
    }

    public void setPort(int port) {
    
    	this.port = port;
    
    }

    public String getNode() {
    	
        return getRoutes().getID();
        
    }
    
    public void attach() {
    	
        ZMQ.Context context = ZMQ.context(1);

        // Socket to talk to clients
        ZMQ.Socket responder = context.socket(ZMQ.REP);
        responder.bind("tcp://*:" + getPort());
        
        //finish this

        responder.close();
        context.term();
    }

    //implement automatic port binding between a range 13000-18888?
    private static int getAvailablePort() {
        int port = 18888;
        return port;
    }


}