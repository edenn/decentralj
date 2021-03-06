package com.decentralbank.decentralj.dht.interfaces;


public interface IKademliaStorageDataEntry {

    /**
     * Add the content to the storage bucket
     */

    public void setContent(final byte[] data);

    /**
     * Get the content from this storage entry
     */

    public byte[] getContent();

    /**
     * Get the metadata for this storage entry
     */
    public IKademliaStorageDataContent getContentMetadata();
}
