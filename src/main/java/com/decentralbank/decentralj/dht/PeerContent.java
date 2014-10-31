package com.decentralbank.decentralj.dht;


import java.util.ArrayList;

public class PeerContent {
    private String key;
    private String type;
    private long lastUpdatedTimestamp;
    private ArrayList<String> ownerId;

    public String getKey() {

        return key;
    }

    public ArrayList<String> getOwnerId() {
        return ownerId;
    }

    public String getType() {
        return type;
    }

    public long getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public byte[] toSerializedForm() {
        return new byte[0];
    }
}
