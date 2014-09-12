package com.decentralbank.decentralj.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.management.ObjectName;

import org.zeromq.ZMQ.Socket;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

// model for node peer's data

public class DecentralPeer {

	private byte[] ip;
	private int    port;
	private byte[] peerId;
    
    private static final int USHORT_MAX = 0xffff;
    private static final int UBYTE_MAX = 0xff;

	private transient boolean isOnline = false;
	private transient long    lastCheckTime = 0;
	
	private ZContext ctx;                //  CZMQ context
    private Socket mailbox;              //  Socket through to peer
    private String identity;             //  Identity string
    private String endpoint;             //  Endpoint connected to
    private long evasive_at;             //  Peer is being evasive
    private long expired_at;             //  Peer has expired by now
    private boolean connected;           //  Peer will send messages
    private boolean ready;               //  Peer has said Hello to us
    private int status;                  //  Our status counter
    private int sent_sequence;           //  Outgoing message sequence
    private int want_sequence;           //  Incoming message sequence
    private Map <String, String> headers;           //  Peer headers
	
	public DecentralPeer(){
		
	}

	
	//peer model
    public DecentralPeer(byte[] ip, int port, byte[] peerId, ZContext ctx, String identity) {
        this.ip = ip;
        this.port = port & 0xFFFF;
        this.peerId = peerId;
        
        this.ctx = ctx;
        this.identity = identity;
        
        ready = false;
        connected = false;
        sent_sequence = 0;
        want_sequence = 0;
        
    }
    
    //  ---------------------------------------------------------------------
    //  Connect peer mailbox
    //  Configures mailbox and connects to peer's router endpoint
    public void connect (String replyTo, String endpoint)
    {
        //  Create new outgoing socket (drop any messages in transit)
        mailbox = ctx.createSocket (ZMQ.DEALER);

        //  Null if shutting down
        if (mailbox != null) {
            //  Set our caller 'From' identity so that receiving node knows
            //  who each message came from.
            mailbox.setIdentity (replyTo.getBytes ());
    
            //  Set a high-water mark that allows for reasonable activity
            mailbox.setSndHWM (ZreInterface.PEER_EXPIRED * 100);
           
            //  Send messages immediately or return EAGAIN
            mailbox.setSendTimeOut (0);
    
            //  Connect through to peer node
            mailbox.connect (String.format ("tcp://%s", endpoint));
            this.endpoint = endpoint;
            connected = true;
            ready = false;        
        }
    }
    
    //get localhost ip address
    public String getInetAddress() {
        String addr = null;
        try {
			addr = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return addr;
    }
    
    //send message
    public boolean send (ZreMsg msg)
    {
        if (connected) {
            if (++sent_sequence > USHORT_MAX)
                sent_sequence = 0;
            msg.setSequence (sent_sequence);
            if (!msg.send (mailbox)) {
                disconnect ();
                return false;
            }
        }
        else
            msg.destroy ();

        return true;
    }
    
    //  ---------------------------------------------------------------------
    //  Destroy peer object
    public void destroy ()
    {
        disconnect ();
    }
    
//  No more messages will be sent to peer until connected again
    public void disconnect ()
    {
        ctx.destroySocket (mailbox);
        endpoint = null;
        connected = false;
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
    public boolean peerIsOnline() {
    	//implement heartBeatMethod here

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
        return "Peer: [ip=" + getInetAddress() + ", port=" + getPort() +
                ", peerId=" + (getPeerId() == null ? "": getPeerId()) + "]";
    }

   // @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        DecentralPeer peerData = (DecentralPeer) obj;
        return this.getInetAddress().equals(peerData.getInetAddress());
    }


	public String identity() {
		return identity;
	}


	public void incStatus() {
		if (++status > UBYTE_MAX)
            status = 0;		
	}
	  public String header (String key, String defaultValue)
	    {
	        if (headers.containsKey (key))
	            return headers.get (key);
	        
	        return defaultValue;
	    }
	    
	    public void setHeaders (Map<String, String> headers)
	    {
	        this.headers = new HashMap <String, String> (headers);
	    }

	    public boolean checkMessage (ZreMsg msg)
	    {
	        int recd_sequence = msg.sequence ();
	        if (++want_sequence > USHORT_MAX)
	            want_sequence = 0;
	        
	        boolean valid = want_sequence == recd_sequence;
	        if (!valid) {
	            if (--want_sequence < 0)    //  Rollback
	                want_sequence = USHORT_MAX;
	        }
	        return valid;
	    }


		public void invoke(ObjectName timer, String string, Object[] objects,
				String[] strings) {
			// TODO Auto-generated method stub
			
		}



}