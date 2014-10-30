package com.decentralbank.decentralj.routingtable;

import java.util.*;

import com.decentralbank.decentralj.config.Config;
import com.decentralbank.decentralj.core.Node;
import com.decentralbank.decentralj.dht.Contact;
import com.decentralbank.decentralj.dht.KademliaId;
import com.decentralbank.decentralj.dht.KeyComparator;
import com.decentralbank.decentralj.net.DecentralPeer;
import com.decentralbank.decentralj.routingtable.interfaces.IRoutingTable;

//Routing Table to Keep track of network
public class RoutingTable implements IRoutingTable {

    private DecentralPeer localNode;
    private transient Bucket[] buckets;
    private transient Config config;


    public RoutingTable(DecentralPeer localNode, Config config)
    {
        this.localNode = localNode;
        this.config = config;

        /* Initialize all of the buckets to a specific depth */
        this.initialize();

        /* Insert the local node */
        this.insert(localNode);
    }

    /**
     * Initialize the RoutingTable to it's default state
     */

    public final void initialize()
    {
        this.buckets = new Bucket[KademliaId.ID_LENGTH];
        for (int i = 0; i < KademliaId.ID_LENGTH; i++)
        {
            buckets[i] = new Bucket(i);
        }
    }

    /**
     * Adds a contact to the routing table based on how far it is from the LocalNode.
     */

    public synchronized final void insert(Contact c)
    {
        this.buckets[this.getBucketId(c.getNode().getNodeId())].insert(c);
    }

    /**
     * Adds a node to the routing table based on how far it is from the LocalNode.
     *
     * @param n The node to add
     */

    public synchronized final void insert(DecentralPeer n)
    {
        this.buckets[this.getBucketId(n.getNodeId())].insert(n);
    }

    /**
     * Compute the bucket ID in which a given node should be placed; the bucketId is computed based on how far the node is away from the Local Node.
     */

    public int getBucketId(KademliaId nid)
    {
        int bId = this.localNode.getNodeId().getDistance(nid) - 1;

        /* If we are trying to insert a node into it's own routing table, then the bucket ID will be -1, so let's just keep it in bucket 0 */
        return bId < 0 ? 0 : bId;
    }

    /**
     * Find the closest set of contacts to a given NodeId
     */

    public synchronized final List<DecentralPeer> findClosest(KademliaId target, int numNodesRequired)
    {
        TreeSet<DecentralPeer> sortedSet = new TreeSet<>(new KeyComparator(target));
        sortedSet.addAll(this.getAllNodes());

        List<DecentralPeer> closest = new ArrayList<>(numNodesRequired);

        /* Now we have the sorted set, lets get the top numRequired */
        int count = 0;
        for (DecentralPeer n : sortedSet)
        {
            closest.add(n);
            if (++count == numNodesRequired)
            {
                break;
            }
        }
        return closest;
    }

    /**
     * @return List A List of all Nodes in this RoutingTable
     */
    @Override
    public synchronized final List<DecentralPeer> getAllNodes()
    {
        List<DecentralPeer> nodes = new ArrayList<>();

        for (Bucket b : this.buckets)
        {
            for (Contact c : b.getContacts())
            {
                nodes.add(c.getNode());
            }
        }

        return nodes;
    }

    /**
     * @return List A List of all Nodes in this JKademliaRoutingTable
     */

    public final List<Contact> getAllContacts()
    {
        List<Contact> contacts = new ArrayList<>();

        for (Bucket b : this.buckets)
        {
            contacts.addAll(b.getContacts());
        }

        return contacts;
    }

    /**
     * @return Bucket[] The buckets in this Instance
     */

    public final Bucket[] getBuckets()
    {
        return this.buckets;
    }

    /**
     * Set the Buckets of this routing table, mainly used when retrieving saved state
     */
    public final void setBuckets(Bucket[] buckets)
    {
        this.buckets = buckets;
    }

    /**
     * Method used by operations to notify the routing table of any contacts that have been unresponsive.
     */

    public void setUnresponsiveContacts(List<DecentralPeer> contacts)
    {
        if (contacts.isEmpty())
        {
            return;
        }
        for (DecentralPeer n : contacts)
        {
            this.setUnresponsiveContact(n);
        }
    }

    /**
     * Method used by operations to notify the routing table of any contacts that have been unresponsive.
     */

    public synchronized void setUnresponsiveContact(DecentralPeer n)
    {
        int bucketId = this.getBucketId(n.getNodeId());

        /* Remove the contact from the bucket */
        this.buckets[bucketId].removeNode(n);
    }


    public synchronized final String toString()
    {
        StringBuilder sb = new StringBuilder("\nPrinting Routing Table Started ***************** \n");
        int totalContacts = 0;
        for (Bucket b : this.buckets)
        {
            if (b.numContacts() > 0)
            {
                totalContacts += b.numContacts();
                sb.append("# nodes in Bucket with depth ");
                sb.append(b.getDepth());
                sb.append(": ");
                sb.append(b.numContacts());
                sb.append("\n");
                sb.append(b.toString());
                sb.append("\n");
            }
        }

        sb.append("\nTotal Contacts: ");
        sb.append(totalContacts);
        sb.append("\n\n");

        sb.append("Printing Routing Table Ended ******************** ");

        return sb.toString();
    }

}