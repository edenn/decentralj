package com.decentralbank.decentralj.dht;


import com.decentralbank.decentralj.dht.interfaces.IKademliaStorageDataEntry;
import com.decentralbank.decentralj.dht.interfaces.IKademliaStorageDataContent;

public class KademliaStorageDataContent implements IKademliaStorageDataEntry
{

    private String content;
    private final KademliaStorageDataEntry metadata;

    public KademliaStorageDataContent(final Contact content)
    {
        this(content, new KademliaStorageDataContent(content));
    }

    public KademliaStorageDataContent(final IKademliaStorageDataContent metadata)
    {
        this.metadata = metadata;
    }

    @Override
    public final void setContent(final byte[] data)
    {
        this.content = new String(data);
    }

    @Override
    public final byte[] getContent()
    {
        return this.content.getBytes();
    }

    @Override
    public final IKademliaStorageDataContent getContentMetadata()
    {
        return this.metadata;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("[StorageEntry: ");

        sb.append("[Content: ");
        sb.append(this.getContent());
        sb.append("]");

        sb.append(this.getContentMetadata());

        sb.append("]");

        return sb.toString();
    }
}