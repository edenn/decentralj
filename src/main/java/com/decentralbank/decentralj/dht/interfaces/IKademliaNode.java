package com.decentralbank.decentralj.dht.interfaces;

import com.decentralbank.decentralj.core.Node;
import com.decentralbank.decentralj.dht.DHTParam;
import com.decentralbank.decentralj.dht.KademliaStorageDataContent;
import com.decentralbank.decentralj.dht.NodeContent;
import com.decentralbank.decentralj.routingtable.RoutingTable;

import java.io.IOException;
import java.util.NoSuchElementException;

public interface IKademliaNode {
    /**
     * Schedule the recurring refresh operation
     */
    public void startHeartBeat();

    /**
     * Stop the recurring refresh operation
     */
    public void stopHeartBeat();

    /**
     * The local node for this system
     */
    public Node getNode();

    /**
     * The DHT for this instance
     */
    public IKademliaDHT getDHT();

    /**
     * Connect to an existing peer-to-peer network.
     * */
    public void bootstrap(Node n) throws IOException;

    /**
     * Stores the specified value under the given key
     * This value is stored on K nodes on the network, or all nodes if there are > K total nodes in the network
     */
    public int put(NodeContent content) throws IOException;

    /**
     * Stores the specified value under the given key
     * This value is stored on K nodes on the network, or all nodes if there are > K total nodes in the network
     */
    public int put(KademliaStorageDataContent entry) throws IOException;

    /**
     * Store a content on the local node's DHT
     */
    public void putLocally(NodeContent content) throws IOException;

    /**
     * Get some content stored on the DHT
     */
    public KademliaStorageDataContent get(DHTParam param) throws NoSuchElementException, IOException;

    /**
     * @return String The ID of the owner of this local network
     */
    public String getOwnerId();

    /**
     * @return Integer The port on which this kad instance is running
     */
    public int getPort();

    /**
     * Handle The System shut down
     */
    public void shutdown(final boolean saveState) throws IOException;

    /**
     * Saves the node state to a text file
     */
    public void saveNodeState() throws IOException;

    /**
     * @return The routing table for this node.
     */
    public RoutingTable getRoutingTable();

}
