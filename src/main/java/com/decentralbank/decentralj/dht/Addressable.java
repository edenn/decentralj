package com.decentralbank.decentralj.dht;

import java.util.Random;

import javax.xml.bind.DatatypeConverter;

public abstract class Addressable implements Comparable<Addressable> {
    protected byte[] id;

    public Addressable() {
        id = createRandomId();
    }
    protected Addressable(byte[] id) {
        this.id = id;
    }

    private static byte[] createRandomId() {
        Random r = new Random();
        byte[] id = new byte[DHTConstants.ID_LENGTH];
        for (int i = 0; i < DHTConstants.ID_LENGTH; i++) {
            id[i] = (byte) r.nextInt(256);
        }
        return id;
    }

    public boolean equals(Addressable other) {
        for (int i = 0; i < DHTConstants.ID_LENGTH; i++) {
            if (this.id[i] != other.id[i]) {
                return false;
            }
        }
        return true;
    }

    public int compareTo(Addressable other) {
        for (int i = 0; i < DHTConstants.ID_LENGTH; i++) {
            if (this.id[i] != other.id[i]) {
                if (this.id[i] < other.id[i]) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
        return 0;
    }

    protected byte[] xor(Addressable other) {
        byte[] id = new byte[DHTConstants.ID_LENGTH];
        for (int i = 0; i < DHTConstants.ID_LENGTH; i++) {
            id[i] = (byte) (this.id[i] ^ other.id[i]);
        }
        return id;
    }

    protected static int getPrefixLength(byte[] id) {
        for (int i = 0; i < DHTConstants.ID_LENGTH; i++) {
            for (int j = 0; j < 8; j++) {
                if (((id[i] >> (7 - j)) & 0x1) != 0) {
                    return i * 8 + j;
                }
            }
        }
        return DHTConstants.ID_LENGTH * 8 - 1;
    }

    private static byte[] idFromString(String data) {
        return DatatypeConverter.parseHexBinary(data);
    }

    private static String idToString(byte[] data) {
        return DatatypeConverter.printHexBinary(data);
    }

    public String getId() {
        return idToString(id);
    }

    public void setSeenNow() {

    };

    public void resetStaleCount(){
    };

    public void staleCount(){
    };

    public void incrementStaleCount() {

    };
}
