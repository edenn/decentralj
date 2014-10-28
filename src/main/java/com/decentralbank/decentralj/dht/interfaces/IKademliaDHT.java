package com.decentralbank.decentralj.dht.interfaces;

import com.decentralbank.decentralj.dht.Contact;
import com.decentralbank.decentralj.dht.DHTParam;
import com.decentralbank.decentralj.dht.NodeContent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public interface IKademliaDHT {

        /**
         * Initialize this DHT to it's default state
         */
        public void initialize();

        /**
         * Creates a new Serializer or returns an existing serializer
         */
        public IDHTSerializer<IKademliaStorageDataEntry> getSerializer();

        /**
         * Handle storing content locally
         */
        public boolean store(IKademliaStorageDataEntry content) throws IOException;

        public boolean store(NodeContent content) throws IOException;
        /**
         * Retrieves a Content from local storage
         */
        public IKademliaStorageDataEntry retrieve(String key, int hashCode) throws FileNotFoundException, IOException, ClassNotFoundException;

        /**
         * Check if any content for the given criteria exists in this DHT
         */
        public boolean contains(String parameter);

        public IKademliaStorageDataEntry retrieve(Contact key, int hashCode) throws FileNotFoundException, IOException, ClassNotFoundException;

        /**
         * Check if any content for the given criteria exists in this DHT
         *
         * @param param The content search criteria
         *
         * @return boolean Whether any content exist that satisfy the criteria
         */
        public boolean contains(DHTParam param);


    /**
         * Retrieve and create a KadContent object given the StorageEntry object
         */
        public IKademliaStorageDataEntry get(IKademliaStorageDataContent entry) throws IOException, NoSuchElementException;

        /**
         * Get the StorageEntry for the content if any exist.
         */
        public IKademliaStorageDataEntry get(String parameter) throws NoSuchElementException, IOException;

        /**
         * Delete a content from local storage
         */
        public void remove(IKademliaStorageDataContent entry) throws  NoSuchElementException;

        /**
         * @return A List of all StorageEntries for this node
         */
        public List<IKademliaStorageDataContent> getStorageEntries();

        /**
         * Used to add a list of storage entries for existing content to the DHT.
         * Mainly used when retrieving StorageEntries from a saved state file.
         */
        public void putStorageEntries(List<IKademliaStorageDataContent> entries);


}
