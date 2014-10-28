package com.decentralbank.decentralj.dht.interfaces;


import com.decentralbank.decentralj.dht.Contact;

import java.util.ArrayList;

public interface IKademliaStorageDataContent {
    /**
     * @return The Contact information of this content
     */
    public Contact getContact();

    /**
     * @return The id information of this content
     */
    public String getId();

    /**
     * @return The content's owners ID
     */
    public ArrayList<String> getOwnersId();

    /**
     * @return The type of this content
     */
    public String getType();

    /**
     * @return A hash of the content
     */
    public int getContentHash();

    /**
     * @return The last time this content was updated
     */
    public long getLastUpdatedTimestamp();

    /**
     * @return The timestamp for the last time this content was republished
     */
    public long lastRepublished();

    /**
     * Whenever we republish a content or get this content from the network, we update the last republished time
     */
    public void updateLastRepublished();
}
