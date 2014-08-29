package com.decentralbank.decentralj.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

// model for node peer's data

public class PeerModel {

	private byte[] ip;
	private int    port;
	private byte[] peerId;
    private byte   capabilities;

	private transient boolean isOnline = false;
	private transient long    lastCheckTime = 0;

	//peer model
    public PeerModel(byte[] ip, int port, byte[] peerId) {
        this.ip = ip;
        this.port = port & 0xFFFF;
        this.peerId = peerId;
    }
    
    //get localhost ip address
    public InetAddress getInetAddress() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getByAddress(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new Error("malformed ip");
        }
        return addr;
    }
    //get ip address
    public byte[] getIp() {
        return ip;
    }

    //get port number
    public int getPort() {
        return port;
    }

    //get peer id
    public byte[] getPeerId() {
        return peerId;
    }

    //detect if peer is online
    public boolean isOnline() {
        if (capabilities < 7) return false;
        return isOnline;
    }

    //set online peer
    public void setOnline(boolean online) {
        isOnline = online;
    }

    //get last checked time
    public long getLastCheckTime() {
        return lastCheckTime;
    }

    //set last checked time
    public void setLastCheckTime(long lastCheckTime) {
        this.lastCheckTime = lastCheckTime;
    }



    @Override
    public String toString() {
        return "Peer: [ip=" + getInetAddress().getHostAddress() + ", port=" + getPort() +
                ", peerId=" + (getPeerId() == null ? "": getPeerId()) + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        PeerModel peerData = (PeerModel) obj;
        return this.getInetAddress().equals(peerData.getInetAddress());
    }

}