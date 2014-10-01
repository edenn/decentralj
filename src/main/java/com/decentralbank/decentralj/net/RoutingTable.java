package com.decentralbank.decentralj.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.decentralbank.decentralj.core.Node;

//Routing Table to Keep track of network
public class RoutingTable {

    private Node node;
    private final int CONSTANT=100; //list of 100 nodes
    private ArrayList[] buckets = new ArrayList[CONSTANT];

    public Node getNode() {
        return node;
    }
    
    //get Pool ID
	public String getID() {
		
		return node.getID();
		
	}
	
	//set a node
    public void setNode(Node node) {
        this.node = node;
    }
    
    //routing table
    public RoutingTable(Node id) {
        this.setNode(id);
        for (int i = 0; i < CONSTANT; i++) {
            buckets[i] = new ArrayList<Object>();
        }
    }

    //update a peer
    public void update(Node peer) {
     
    }

    //find available nodes
    public ArrayList findAvailableNodes(Node target, int count){
        ArrayList ret = new ArrayList<Object>();
       
        
        return ret;
    }

}