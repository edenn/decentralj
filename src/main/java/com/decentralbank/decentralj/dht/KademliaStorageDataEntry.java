package com.decentralbank.decentralj.dht;


import com.decentralbank.decentralj.dht.interfaces.IKademliaStorageDataEntry;

public class KademliaStorageDataEntry implements IKademliaStorageDataEntry
{

    private String content;
    private final KademliaStorageDataContent metadata;

    public KademliaStorageDataEntry(final PeerContent content)
    {
        this(content, new KademliaStorageDataContent(content));
    }

    public KademliaStorageDataEntry(PeerContent content, final KademliaStorageDataContent metadata)
    {
        this.setContent(content.toSerializedForm());
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
    public final KademliaStorageDataContent getContentMetadata()
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