package com.decentralbank.decentralj.dht;

import com.decentralbank.decentralj.dht.interfaces.IKademliaStorageDataContent;

import java.util.Objects;
import java.util.ArrayList;

public class KademliaStorageDataContent implements IKademliaStorageDataContent
{

    private final String key;
    private final ArrayList<String> ownerId;
    private final String type;
    private final int contentHash;
    private final long updatedTs;

    /* This value is the last time this content was last updated from the network */
    private long lastRepublished;

    public KademliaStorageDataContent(PeerContent content)
    {
        this.key = content.getKey();
        this.ownerId = content.getOwnerId();
        this.type = content.getType();
        this.contentHash = content.hashCode();
        this.updatedTs = content.getLastUpdatedTimestamp();

        this.lastRepublished = System.currentTimeMillis() / 1000L;
    }

    public String getKey()
    {
        return this.key;
    }

    public ArrayList<String> getOwnerId()
    {
        return this.ownerId;
    }

    @Override
    public Contact getContact() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public ArrayList<String> getOwnersId() {
        return null;
    }

    @Override
    public String getType()
    {
        return this.type;
    }

    @Override
    public int getContentHash()
    {
        return this.contentHash;
    }

    @Override
    public long getLastUpdatedTimestamp()
    {
        return this.updatedTs;
    }

    @Override
    public long lastRepublished()
    {
        return this.lastRepublished;
    }

    /**
     * Whenever we republish a content or get this content from the network, we update the last republished time
     */
    @Override
    public void updateLastRepublished()
    {
        this.lastRepublished = System.currentTimeMillis() / 1000L;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof IKademliaStorageDataContent)
        {
            return this.hashCode() == o.hashCode();
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.key);
        hash = 23 * hash + Objects.hashCode(this.ownerId);
        hash = 23 * hash + Objects.hashCode(this.type);
        return hash;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("[StorageEntry: ");

        sb.append("{Key: ");
        sb.append(this.key);
        sb.append("} ");
        sb.append("{Owner: ");
        sb.append(this.ownerId);
        sb.append("} ");
        sb.append("{Type: ");
        sb.append(this.type);
        sb.append("} ");
        sb.append("]");

        return sb.toString();
    }
}
