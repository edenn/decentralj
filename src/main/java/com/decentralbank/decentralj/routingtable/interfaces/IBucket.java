package com.decentralbank.decentralj.routingtable.interfaces;


import com.decentralbank.decentralj.core.Node;
import com.decentralbank.decentralj.dht.Contact;

import java.util.List;

public interface IBucket {
    /**
     * Adds a contact to the bucket
     */
    public void insert(Contact c);

    /**
     * Create a new contact and insert it into the bucket.
     */
    public void insert(Node n);

    /**
     * Checks if this bucket contain a contact
     */
    public boolean containsContact(Contact c);

    /**
     * Checks if this bucket contain a node
     */
    public boolean containsNode(Node n);

    /**
     * Remove a contact from this bucket.
     */
    public boolean removeContact(Contact c);

    /**
     * Remove the contact object related to a node from this bucket
     */
    public boolean removeNode(Node n);

    /**
     * Counts the number of contacts in this bucket.
     */
    public int numContacts();

    /**
     *The depth of this bucket in the RoutingTable
     */
    public int getDepth();

    /**
     * All contacts in this bucket
     */
    public List<Contact> getContacts();
}
