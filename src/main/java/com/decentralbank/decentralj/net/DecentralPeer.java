package com.decentralbank.decentralj.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.ObjectName;

import org.zeromq.ZMQ.Socket;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import com.decentralbank.decentralj.core.Request;

//Decentral Peer to handle connections with peers
public class DecentralPeer {

	private byte[] ip;
	private int    port;
	private byte[] peerId;
    
    private static final int USHORT_MAX = 0xffff;
    private static final int UBYTE_MAX = 0xff;

	private transient boolean isOnline = false;
	private transient long    lastCheckTime = 0;
	
	private ZContext ctx;                	//  CZMQ context
    private Socket socket;              	//  Socket through to peer
    private String identity;            	//  Identity string
    private String endpoint;             	//  Endpoint connected to
    private long evasive_at;             	//  Peer is being evasive
    private long expired_at;             	//  Peer has expired by now
    private boolean connected;          	//  Peer will send messages
    private boolean ready;              	//  Peer has said Hello to us
    private int status;                 	//  Our status counter
    private int sent_sequence;           	//  Outgoing message sequence
    private int want_sequence;           	//  Incoming message sequence
    private Map <String, Request> headers; 	//  Peer headers
    private ArrayList<Request> requests;    // list of requests;
    private List<DecentralGroup> pools;     //pools peer is a member of
	
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
    //  Connect peer Decentral
    //  Configures socket and connects to peer's router endpoint
    public void connect (String replyTo, String endpoint)
    {
        //  Create a Dealer socket (drop any messages in transit)
        socket = ctx.createSocket (ZMQ.DEALER);

        //  Null if shutting down
        if (socket != null) {
            //  Set our caller 'From' identity so that receiving node knows
            //  who each message came from.
            socket.setIdentity (replyTo.getBytes ());
    
            //  Set a high-water mark that allows for reasonable activity
            socket.setSndHWM (DecenInterface.PEER_EXPIRED * 100);
           
            //  Send messages immediately or return EAGAIN
            socket.setSendTimeOut (0);
    
            //  Connect through to peer node
            socket.connect (String.format ("tcp://%s", endpoint));
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
    public boolean send(DecenMsg msg) {
        if (connected) {
            if (++sent_sequence > USHORT_MAX)
                sent_sequence = 0;
            msg.setSequence (sent_sequence);
            if (!msg.send (socket)) {
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
        ctx.destroySocket(socket);
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
	  
	
	public Request header (String key, Request frames){
		
	        if (headers.containsKey (key))
	            return headers.get (key);
	        
	        return frames;
	}
	    
	   
	public void setHeaders (Map<String, Request> headers){
		
	        this.headers = new HashMap <String, Request> (headers);
	    
	}
  
	
	public boolean checkMessage (DecenMsg msg){
		
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

	
	    //  ---------------------------------------------------------------------
	    //  Return peer connection endpoint
	
	
	 public String endpoint (){
		 
	        if (connected)
	            return endpoint;
	        else
	            return "";
	                
	 }

		
	 public void invoke(ObjectName timer, String string, Object[] objects, String[] strings) {
			// TODO Auto-generated method stub
			
	}
		
		
	 //  Return peer future expired time    
	 public long expiredAt (){
		 
	        return expired_at;
	        
	 }
	    
	 //  ---------------------------------------------------------------------    
	 //  Return peer future evasive time
	    
	 public long evasiveAt (){
	        return evasive_at;
	 }

	    
	 public void setReady (boolean ready){
		 
		 
	        this.ready = ready;
	 }

	    
	 public boolean ready (){
		 
	        return ready;
	 }

	    
	 public void setStatus (int status){
		 
	        this.status = status;
	 }
	    
	    
	 public int status (){
		 
	        return status;
	 }



}