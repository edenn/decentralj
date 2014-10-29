package com.decentralbank.decentralj.routingtable.interfaces;


import com.decentralbank.decentralj.core.Node;
import com.decentralbank.decentralj.dht.Contact;
import com.decentralbank.decentralj.net.DecentralPeer;
import com.decentralbank.decentralj.routingtable.Bucket;

import java.util.List;

public interface IRoutingTable {

    /**
     * Initialize the RoutingTable to it's default state
     */
    public void initialize();

    /**
     * Adds a contact to the routing table based on how far it is from the LocalNode.
     */
    public void insert(Contact c);

    /**
     * Adds a node to the routing table based on how far it is from the LocalNode.
     */
    public void insert(DecentralPeer n);

    /**
     * Compute the bucket ID in which a given node should be placed; the bucketId is computed based on how far the node is away from the Local Node.
     */
    public int getBucketId(Contact id);

    /**
     * Find the closest set of contacts to a given NodeId
     */
    public List<Node> findClosest(Contact target, int numNodesRequired);

    /**
     * @return List A List of all Nodes in this RoutingTable
     */
    public List getAllNodes();

    /**
     * A List of all Nodes in this RoutingTable
     */
    public List getAllContacts();

    /**
     * All buckets
     */
    public Bucket[] getBuckets();

    /**
     * Method used by operations to notify the routing table of any contacts that have been unresponsive.
     */
    public void setUnresponsiveContacts(List<Node> contacts);

    /**
     * Method used by operations to notify the routing table of any contacts that have been unresponsive.
     */
    public void setUnresponsiveContact(DecentralPeer n);
}
