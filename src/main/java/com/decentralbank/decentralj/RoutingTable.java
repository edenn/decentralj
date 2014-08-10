package com.decentralbank.decentralj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class RoutingTable {
	
    private Node node;
    // not sure if this is the best data structure, also just placing a random number.
    private final int CONSTANT=11*6; 
    private ArrayList[] buckets = new ArrayList[CONSTANT];

    public Node getNode() {
        return node;
    }
    
	public String getID() {
		
		return node.getID();
		
	}

    public void setNode(Node node) {
        this.node = node;
    }
    
    public RoutingTable(Node id) {
        this.setNode(id);
        for (int i = 0; i < CONSTANT; i++) {
            buckets[i] = new ArrayList<Object>();
        }
    }

    public void update(Node peer) {
     
    }

    
    public ArrayList findAvailableNodes(Node target, int count){
        ArrayList ret = new ArrayList<Object>();
        
    
        
        return ret;
    }

}