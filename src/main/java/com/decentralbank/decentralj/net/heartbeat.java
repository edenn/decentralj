package com.decentralbank.decentralj.net;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.UUID;

import javax.management.ObjectName;


public class HeartBeat {
	
    public final static int HEARTBEAT_LIVENESS = 4 ;      //  3-5 is reasonable
    public final static int HEARTBEAT_INTERVAL = 10000;    //  msecs
    public final static int INTERVAL_INIT      = 10000;    //  Initial reconnect
    public final static int INTERVAL_MAX      = 32000;    //  After exponential
    
    // The notification message field 
    public static final String NOTIFICATION_MSG = "heartbeat report";
    
    //Agent 
    private DecentralPeer agent = null;

    //Heart-beat interval in secs 
    private long interval = 0;
    
    //Timer name 
    private ObjectName timer = null;
    
    // The id of the scheduled event 
    private Integer heartbeatSchedule = null;
    
    public static final int BEACON_SIZE = 22;

    public static final String BEACON_PROTOCOL = "ZRE";
    public static final byte BEACON_VERSION = 0x01;
    
    private final byte [] protocol = BEACON_PROTOCOL.getBytes ();
    private final byte version = BEACON_VERSION;
    private UUID uuid;
    private int port;
    
    public HeartBeat() {
    	
    }
    
    public HeartBeat (UUID uuid,ByteBuffer buffer)
    {
        long msb = buffer.getLong ();
        long lsb = buffer.getLong ();
        uuid = new UUID (msb, lsb);
        port = buffer.getShort ();
        if (port < 0)
            port = (0xffff) & port;
    }
    
    public HeartBeat (UUID uuid, int port)
    {
        this.uuid = uuid;
        this.port = port;
    }
    
    public ByteBuffer getBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate (BEACON_SIZE);
        buffer.put (protocol);
        buffer.put (version);
        buffer.putLong (uuid.getMostSignificantBits ());
        buffer.putLong (uuid.getLeastSignificantBits ());
        buffer.putShort ((short) port);
        buffer.flip ();
        return buffer;
    }
    

    public HeartBeat createRequest(UUID uuid, ByteBuffer data) {
    	HeartBeat request = new HeartBeat(uuid, data);       
        return request;
    }

    public HeartBeat createResponse(UUID uuid, ByteBuffer data) {
    	HeartBeat response = new HeartBeat(uuid, data);
        return response;
    }
   
 
    

}


