package com.decentralbank.decentralj;

import org.spongycastle.util.encoders.Hex;

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

    public PeerModel(byte[] ip, int port, byte[] peerId) {
        this.ip = ip;
        this.port = port & 0xFFFF;
        this.peerId = peerId;
    }

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

    public byte[] getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public byte[] getPeerId() {
        return peerId;
    }

    public boolean isOnline() {
        if (capabilities < 7) return false;
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public long getLastCheckTime() {
        return lastCheckTime;
    }

    public void setLastCheckTime(long lastCheckTime) {
        this.lastCheckTime = lastCheckTime;
    }

    public byte getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(byte capabilities) {
        this.capabilities = capabilities;
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