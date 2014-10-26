package com.decentralbank.decentralj.dht;


import com.decentralbank.decentralj.dht.interfaces.KademliaStorageBucket;
import com.decentralbank.decentralj.dht.interfaces.KademliaStorageBucketData;
import com.decentralbank.decentralj.dht.ImplKademliaStorageBucketData;

public class ImplKademliaStorageBucket implements KademliaStorageBucket
{

    private String content;
    private final ImplKademliaStorageBucketData metadata;

    public ImplKademliaStorageBucket(final Contact content)
    {
        this(content, new ImplKademliaStorageBucket(content));
    }

    public ImplKademliaStorageBucket(final KademliaStorageBucketData metadata)
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
    public final KademliaStorageBucketData getContentMetadata()
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