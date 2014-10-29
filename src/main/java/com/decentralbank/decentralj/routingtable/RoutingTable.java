package com.decentralbank.decentralj.routingtable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.decentralbank.decentralj.core.Node;
import com.decentralbank.decentralj.dht.Contact;
import com.decentralbank.decentralj.net.DecentralPeer;
import com.decentralbank.decentralj.routingtable.interfaces.IRoutingTable;

//Routing Table to Keep track of network
public class RoutingTable implements IRoutingTable {

    private Node node;
    final int BUCKET_SIZE = 20;
    private final int CONSTANT=100; //list of 100 nodes
    private ArrayList[] buckets = new ArrayList[CONSTANT];

    public Node getNode() {
        return node;
    }
    
    //get Pool ID
	public String getID() {
		
		return node.getID();
		
	}

    //update
    public void update(Contact contact) {
        //int distance = Contact.getLength(contact.xor(getNode()));
        ArrayList bucket = buckets[BUCKET_SIZE]; // replace BUCKET_SIZE for distance
        Contact found = null;
        for (int i = 0; i < bucket.size(); i++) {
            Contact el = (Contact) bucket.get(i);
            if (el.equals(getNode())) {
                found = contact;
                break;
            }
        }

        if (found == null) {
            if (bucket.size() <= BUCKET_SIZE) {
                bucket.add(0, found);
            } else {
                System.out.println("**** Routing table needs GC ");
            }
        }
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

    @Override
    public void initialize() {

    }

    @Override
    public void insert(Contact c) {

    }

    @Override
    public void insert(DecentralPeer n) {

    }

    @Override
    public int getBucketId(Contact id) {
        return 0;
    }

    @Override
    public List<Node> findClosest(Contact target, int numNodesRequired) {
        return null;
    }

    @Override
    public List getAllNodes() {
        return null;
    }

    @Override
    public List getAllContacts() {
        return null;
    }

    @Override
    public Bucket[] getBuckets() {
        return new Bucket[0];
    }

    @Override
    public void setUnresponsiveContacts(List<Node> contacts) {

    }

    @Override
    public void setUnresponsiveContact(DecentralPeer n) {

    }

}