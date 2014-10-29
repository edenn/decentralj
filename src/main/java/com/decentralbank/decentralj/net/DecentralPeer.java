package com.decentralbank.decentralj.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.*;

import javax.management.ObjectName;

import com.sun.jndi.toolkit.ctx.Continuation;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import com.decentralbank.decentralj.core.Request;

//Decentral Peer to handle connections with peers

/**
 * A Peer Node in the Decentral network - Contains basic node network information.
 */

public class DecentralPeer {

	private byte[] ip;
	protected int    port;
    protected Socket pipe;              //  Pipe back to application
    private Request packet;                 //  Packet object
    private static final int USHORT_MAX = 0xffff;
    private static final int UBYTE_MAX = 0xff;
	private transient long    lastCheckTime = 0;
	private ZContext ctx;                	//  CZMQ context
    private Socket socket;              	//  ROUTER Socket through to peer 
    private UUID uuid;                      //  Our UUID as binary blob
    private String identity;                //  Our UUID as a string
    private InetAddress address;            //  to get localhost address
    private String host;                    //  Our host IP address
    private String endpoint;             	//  Endpoint connected to
    private long evasive_at;             	//  Peer is being evasive
    private long expired_at;             	//  Peer has expired by now
    private boolean connected;          	//  Peer will send messages
    private boolean ready;              	//  Peer has said Hello to us
    private int status;                 	//  Our status counter
    private int sent_sequence;              // Outgoing message sequence
    private int want_sequence;              // Incoming message sequence

    private Map <String, Request> headers; 	//  Peer headers
    private ArrayList<Request> requests;    // list of requests;
	private Hashtable<String, DecentralPeer> incomingPeers; //  Hash of known peers, fast lookup
    private Map <String, DecentralGroup> peer_pools;     //  Groups that our peers are in
    private Map <String, DecentralGroup> own_pools;      //  Groups that we are in
    private Continuation nodeId;


    public DecentralPeer(){
	}
	

    private DecentralPeer(ZContext ctx, Socket pipe, Socket socket, int port)                        //Request packet, int port)
        {
            this.ctx = ctx;
            this.pipe = pipe;
            this.socket = socket;
            this.packet = packet;
            this.port = port;

            //host = packet.host ();
            host = address.getHostAddress ();
            uuid = UUID.randomUUID ();
            identity = uuid.toString();
            endpoint = String.format ("%s:%d", host, port);

            //  Set up network: Each server binds to an
            //  ephemeral port and publishes a temporary directory that acts
            //  as the outbox for this node.
            //
            /***** Implement ServerThread inside Node ******/

    }
    
    protected static DecentralPeer newPeer (ZContext ctx, Socket pipe)
        {
            Socket socket = ctx.createSocket(ZMQ.ROUTER);
            if (socket == null)      //  Interrupted
                return null;

            Request packet = new Request(DecenInterface.PING_PORT_NUMBER);
            int port = socket.bindToRandomPort ("tcp://*", 0xc000, 0xffff);
            if (port < 0) {          //  Interrupted
                System.err.println ("Failed to bind a random port");
                //packet.destroy ();
                return null;
            }
            return new DecentralPeer(ctx, pipe, socket, port);
    }

    //  ---------------------------------------------------------------------
    //  Connect peer Decentral directly
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
    public String getIdentity() {
        return identity;
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
                ", peerId=" + (getIdentity() == null ? "": getIdentity()) + "]";
    }

   // @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        DecentralPeer peerData = (DecentralPeer) obj;
        return this.getInetAddress().equals(peerData.getInetAddress());
    }


	protected String identity() {
		
		return identity;
		
	}


	protected int incStatus ()
    {
        if (++status > UBYTE_MAX)
            status = 0;
        return status;
    }


	
	    //  ---------------------------------------------------------------------
	    //  Return peer connection endpoint
	
	
	 public String endpoint (){
		 
	        if (connected)
	            return endpoint;
	        else
	            return "";
	                
	 }

	        //  Delete peer for a given endpoint
        private void purgePeer ()
        {
            for (Map.Entry <String, DecentralPeer> entry : incomingPeers.entrySet ()) {
                DecentralPeer peer = entry.getValue ();
                if (peer.endpoint ().equals (endpoint))
                    peer.disconnect ();
            }
        }

        //  Find or create peer via its UUID string
        private DecentralPeer requirePeer (String identity, String address, int port)
        {
            DecentralPeer peer = incomingPeers.get (identity);
            if (peer == null) {
                //  Purge any previous peer on same endpoint
                String endpoint = String.format ("%s:%d", address, port);

                purgePeer ();

                //peer = DecentralPeer.newPeer (identity, peers, ctx);
                peer.connect (this.identity, endpoint);

                //  Handshake discovery by sending HELLO as first message
                DecenMsg msg = new DecenMsg(DecenMsg.HELLO);
                //msg.setIpaddress (this.udp.host ());
                msg.setport(this.port);
                msg.setpools(own_pools.keySet());
                msg.setStatus (status);
                msg.setHeaders (new HashMap<String, Request> (headers));
                peer.send (msg);

                //log.info (ZreLogMsg.ZRE_LOG_MSG_EVENT_ENTER,peer.endpoint (), endpoint);

                //  Now tell the caller about the peer
                pipe.sendMore ("ENTER");
                pipe.send (identity);
            }
            return peer;
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
    

        //  Find or create pool via its name for local node
        private DecentralGroup requirePeerPool(String name)
        {
            DecentralGroup group = own_pools.get(name);
            if (group == null)
                group = DecentralGroup.newGroup (name, own_pools);
            return group;

        }

        private DecentralGroup joinPeerGroup (DecentralPeer peer, String name)
        {
            DecentralGroup pool = requirePeerPool(name);
            pool.join (peer);

            //  Now tell the caller about the peer joined a group
            pipe.sendMore ("JOIN");
            pipe.sendMore (peer.identity ());
            pipe.send (name);

            return pool;
        }


        public boolean ready (){
            return ready;
        }

        private DecentralGroup leavePeerGroup (DecentralPeer peer, String name)
        {
            DecentralGroup pool = requirePeerPool(name);
            pool.leave(peer);

            //  Now tell the caller about the peer joined a group
            pipe.sendMore ("LEAVE");
            pipe.sendMore (peer.identity ());
            pipe.send (name);

            return pool;
        }

        // Return peer future expired time
        public long expiredAt (){
            return expired_at;
        }
        // ---------------------------------------------------------------------
        // Return peer future evasive time
        public long evasiveAt (){
            return evasive_at;
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
                if (--want_sequence < 0) // Rollback
                    want_sequence = USHORT_MAX;
            }
            return valid;
        }


        //  Here we handle messages coming from other peers
        protected boolean recvFromPeer ()
        {
            //  Router socket tells us the identity of this peer
            DecenMsg msg = DecenMsg.recv(socket);
            if (msg == null)
                return false;               //  Interrupted

            String identity = new String (msg.address ().getData ());

            //  On HELLO we may create the peer if it's unknown
            //  On other commands the peer must already exist
            DecentralPeer peer = incomingPeers.get (identity);
            if (msg.id () == DecenMsg.HELLO) {
                peer = requirePeer (
                        identity, msg.ipaddress (), msg.port());
                assert (peer != null);
                peer.setReady (true);
            }
            //  Ignore command if peer isn't ready
            if (peer == null || !peer.ready()) {
                msg.destroy ();
                return true;
            }

            if (!peer.checkMessage (msg)) {
                System.err.printf ("W: [%s] lost messages from %s\n", this.identity, identity);
                assert (false);
            }

            //  Now process each command
            if (msg.id () == DecenMsg.HELLO) {
                //  Join peer to listed groups
                for (String name : msg.pools()) {
                    joinPeerGroup (peer, name);
                }
                //  Hello command holds latest status of peer
                peer.setStatus (msg.status ());

                //  Store peer headers for future reference
                //peer.setHeaders (msg.headers ());

                //  connect to peer
                String publisher = msg.headersString ("X-FILEMQ", null);
                //  if (publisher != null)
                //fmq_client.connect (publisher);
            }
            else
            if (msg.id () == DecenMsg.ALIVE) {
                //  Pass up to caller API as WHISPER event
                ZFrame cookie = msg.content ();
                pipe.sendMore ("WHISPER");
                pipe.sendMore (identity);
                //cookie.sendAndKeep (pipe); // let msg free the frame
            }
            else
            if (msg.id () == DecenMsg.BROADCAST) {
                //  Pass up to caller as SHOUT event
                ZFrame cookie = msg.content ();
                pipe.sendMore ("SHOUT");
                pipe.sendMore (identity);
                pipe.sendMore (msg.pool());
                //cookie.sendAndKeep (pipe); // let msg free the frame
            }
            else
            if (msg.id () == DecenMsg.PING) {
                DecenMsg pingOK = new DecenMsg(DecenMsg.DECENACK);
                peer.send (pingOK);
            }
            else
            if (msg.id () == DecenMsg.JOIN) {
                joinPeerGroup (peer, msg.pool());
                assert (msg.status () == peer.status ());
            }
            else
            if (msg.id () == DecenMsg.LEAVE) {
                leavePeerGroup (peer, msg.pool());
                assert (msg.status () == peer.status ());
            }
            msg.destroy ();

            //  Activity from peer resets peer timers
            // peer.refresh ();
            return true;
        }

    private void setReady(boolean b) {
    }

    //  Handle beacon
        protected boolean recvHeartBeat ()
        {
            ByteBuffer buffer = ByteBuffer.allocate (HeartBeat.BEACON_SIZE);

            //  Get beacon frame from network
            int size = 0;
            buffer.rewind ();

            //  Basic validation on the frame
            if (size != HeartBeat.BEACON_SIZE
                    || buffer.get () != 'D'
                    || buffer.get () != 'E'
                    || buffer.get () != 'C'
                    || buffer.get () != 'E'
                    || buffer.get () != 'N'
                    || buffer.get () != HeartBeat.BEACON_VERSION)
                return true;       //  Ignore invalid beacons

            //  If we got a UUID and it's not our own beacon, we have a peer
            HeartBeat heartBeat = new HeartBeat(buffer);
            if (!heartBeat.uuid.equals (uuid)) {
                String identity = heartBeat.uuid.toString();
                //DecentralPeer peer = requirePeer(identity, udp.from (), beacon.port);
                //peer.refresh ();
            }

            return true;
        }

        //  Send more heartbeat beacons
        public void sendHeartBeat()
        {
            HeartBeat beacon = new HeartBeat(uuid, port);
        }


        //  Send message to all peers
        private static void sendPeers (Map <String, DecentralPeer> peers, DecenMsg msg)
        {
            for (DecentralPeer peer : peers.values ())
                peer.send(msg);
        }

        //  Remove peer from group, if it's a member
        private static void deletePeerFromPools(Map <String, DecentralGroup> groups, DecentralPeer peer)
        {
            for (DecentralGroup group : groups.values ())
                group.leave (peer);
        }
        //  We do this once a second:
        //  - if peer has gone quiet, send TCP ping
        //  - if peer has disappeared, expire it
        public void pingAllPeers ()
        {
            Iterator <Map.Entry <String, DecentralPeer>> it = incomingPeers.entrySet ().iterator ();
            while (it.hasNext ()) {
                Map.Entry<String, DecentralPeer> entry = it.next ();
                String identity = entry.getKey ();
                DecentralPeer peer = entry.getValue ();
                if (System.currentTimeMillis () >= peer.expiredAt ()) {
                    pipe.sendMore ("EXIT");
                    pipe.send (identity);
                    deletePeerFromPools(peer_pools, peer);
                    it.remove ();
                    peer.destroy ();
                }
                else
                if (System.currentTimeMillis () >= peer.evasiveAt ()){
                    //  If peer is being evasive, force a TCP ping.
                    //  TODO: do this only once for a peer in this state;
                    //  it would be nicer to use a proper state machine
                    //  for peer management.
                    DecenMsg msg = new DecenMsg(DecenMsg.PING);
                    peer.send (msg);
                }
            }
        }

	    
	 public void setStatus (int status){
		 
	        this.status = status;
	 }
	    
	    
	 public int status (){
		 
	        return status;
	 }


    public Continuation getNodeId() {
        return nodeId;
    }
}




