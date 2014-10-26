package com.decentralbank.decentralj.dht.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public interface KademliaDHT {

        /**
         * Initialize this DHT to it's default state
         */
        public void initialize();

        /**
         * Creates a new Serializer or returns an existing serializer
         */
        public DHTSerializer<KademliaStorageBucket> getSerializer();

        /**
         * Handle storing content locally
         */
        public boolean store(KademliaStorageBucket content) throws IOException;

        /**
         * Retrieves a Content from local storage
         */
        public KademliaStorageBucket retrieve(String key, int hashCode) throws FileNotFoundException, IOException, ClassNotFoundException;

        /**
         * Check if any content for the given criteria exists in this DHT
         */
        public boolean contains(String parameter);

        /**
         * Retrieve and create a KadContent object given the StorageEntry object
         */
        public KademliaStorageBucket get(KademliaStorageBucketData entry) throws IOException, NoSuchElementException;

        /**
         * Get the StorageEntry for the content if any exist.
         */
        public KademliaStorageBucket get(String parameter) throws NoSuchElementException, IOException;

        /**
         * Delete a content from local storage
         */
        public void remove(KademliaStorageBucketData entry) throws  NoSuchElementException;

        /**
         * @return A List of all StorageEntries for this node
         */
        public List<KademliaStorageBucketData> getStorageEntries();

        /**
         * Used to add a list of storage entries for existing content to the DHT.
         * Mainly used when retrieving StorageEntries from a saved state file.
         */
        public void putStorageEntries(List<KademliaStorageBucketData> entries);


}
