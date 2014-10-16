package com.decentralbank.decentralj.net;
/*  =========================================================================
DecenInterface - interface to a Decen network
*/

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMsg;
import org.zeromq.ZThread;

public class DecenInterface
{
public static final int UBYTE_MAX = 0xff;
//  Defined port numbers, pending IANA submission
public static final int PING_PORT_NUMBER = 9991;
public static final int LOG_PORT_NUMBER = 9992;

//  Constants, to be configured/reviewed
public static final int PING_INTERVAL   = 1000;   //  Once per second
public static final int PEER_EVASIVE    = 5000;   //  Five seconds' silence is evasive
public static final int PEER_EXPIRED   = 10000;   //  Ten seconds' silence is expired

private ZContext ctx;       //  Our context wrapper
private Socket pipe;        //  Pipe through to agent

//  ---------------------------------------------------------------------
//  Constructor

public DecenInterface()
{
    ctx = new ZContext ();
    pipe = ZThread.fork (ctx, new ZreInterfaceAgent ());
}

//  ---------------------------------------------------------------------
//  Destructor
public void destroy ()
{
    ctx.destroy ();
}

//  ---------------------------------------------------------------------
//  Receive next message from interface
//  Returns ZMsg object, or NULL if interrupted
public ZMsg recv ()
{
    return ZMsg.recvMsg (pipe);
}

//  ---------------------------------------------------------------------
//  Join a group
public void join (String group) 
{
    pipe.sendMore ("JOIN");
    pipe.send (group);
}

//  ---------------------------------------------------------------------
//  Leave a group
public void leave (String group) 
{
    pipe.sendMore ("LEAVE");
    pipe.send (group);
}

//  ---------------------------------------------------------------------
//  Send message to single peer; peer ID is first frame in message
//  Destroys message after sending
public void whisper (ZMsg msg) 
{
    pipe.sendMore ("WHISPER");
    msg.send (pipe);
}

//  ---------------------------------------------------------------------
//  Send message to a group of peers
public void shout (ZMsg msg) 
{
    pipe.sendMore ("SHOUT");
    msg.send (pipe);
}

//  ---------------------------------------------------------------------
//  Return interface handle, for polling
public Socket handle ()
{
    return pipe;
}

//  ---------------------------------------------------------------------
//  Set node header value
public void setHeader (String name, String format, Object ... args)
{
    pipe.sendMore ("SET");
    pipe.sendMore (name);
    pipe.send (String.format (format, args));
}

//  ---------------------------------------------------------------------
//  Publish file into virtual space
public void publish (String pathname, String virtual)
{
    pipe.sendMore ("PUBLISH");
    pipe.sendMore (pathname);
    pipe.send (virtual);
}

//  =====================================================================
//  Asynchronous part, works in the background

//  Beacon frame has this format:
//
//  Z R E       3 bytes
//  version     1 byte, %x01
//  UUID        16 bytes
//  port        2 bytes in network order

protected static class Beacon 
{
    public static final int BEACON_SIZE = 22;

    public static final String BEACON_PROTOCOL = "ZRE";
    public static final byte BEACON_VERSION = 0x01;
    
    private final byte [] protocol = BEACON_PROTOCOL.getBytes ();
    private final byte version = BEACON_VERSION;
    private UUID uuid;
    private int port;
    
    public Beacon (ByteBuffer buffer)
    {
        long msb = buffer.getLong ();
        long lsb = buffer.getLong ();
        uuid = new UUID (msb, lsb);
        port = buffer.getShort ();
        if (port < 0)
            port = (0xffff) & port;
    }
    
    public Beacon (UUID uuid, int port)
    {
        this.uuid = uuid;
        this.port = port;
    }
    
    public ByteBuffer getBuffer ()
    {
        ByteBuffer buffer = ByteBuffer.allocate (BEACON_SIZE);
        buffer.put (protocol);
        buffer.put (version);
        buffer.putLong (uuid.getMostSignificantBits ());
        buffer.putLong (uuid.getLeastSignificantBits ());
        buffer.putShort ((short) port);
        buffer.flip ();
        return buffer;
    }

}

private static String uuidStr (UUID uuid)
{
    return uuid.toString ().replace ("-","").toUpperCase ();
}

private static final String OUTBOX = ".outbox";
private static final String INBOX = ".inbox";

protected static class Agent 
{
    private final ZContext ctx;             //  CZMQ context
    private final Socket pipe;              //  Pipe back to application
    //private final ZreUdp udp;               //  UDP object
    private InetAddress address; 
    private final UUID uuid;                //  Our UUID as binary blob
    private final String identity;          //  Our UUID as hex string
    private final Socket inbox;             //  Our inbox socket (ROUTER)
    private final String host;              //  Our host IP address
    private final int port;                 //  Our inbox port number
    private final String endpoint;          //  ipaddress:port endpoint
    private int status;                     //  Our own change counter
    private final Map <String, DecentralPeer> peers;            //  Hash of known peers, fast lookup
    private final Map <String, DecentralGroup> peer_groups;     //  Groups that our peers are in
    private final Map <String, DecentralGroup> own_groups;      //  Groups that we are in
    private final Map <String, String> headers;           //  Our header values
   
    
    private Agent (ZContext ctx, Socket pipe, Socket inbox, int port)                        //ZreUdp udp, int port)
    {
        this.ctx = ctx;
        this.pipe = pipe;
        this.inbox = inbox;
        //this.udp = udp;
        this.port = port;
        
		//host = udp.host ();
        host = address.getHostAddress ();
        uuid = UUID.randomUUID ();
        identity = uuidStr (uuid);
        endpoint = String.format ("%s:%d", host, port);
        peers = new HashMap <String, DecentralPeer> ();
        peer_groups = new HashMap <String, DecentralGroup> ();
        own_groups = new HashMap <String, DecentralGroup> ();
        headers = new HashMap <String, String> ();
        
        
        //  Set up content distribution network: Each server binds to an
        //  ephemeral port and publishes a temporary directory that acts
        //  as the outbox for this node.
        //
        /*fmq_outbox = String.format ("%s/%s", OUTBOX, identity);
        new File (fmq_outbox).mkdir ();
        
        fmq_inbox = String.format ("%s/%s", INBOX, identity);
        new File (fmq_inbox).mkdir ();
        */
        /*fmq_server = new FmqServer ();
        fmq_service = fmq_server.bind ("tcp://*:*");
        fmq_server.publish (fmq_outbox, "/");
        fmq_server.setAnonymous (1);
        String publisher = String.format ("tcp://%s:%d", host, fmq_service);
        headers.put ("X-FILEMQ", publisher);
        
        //  Client will connect as it discovers new nodes
        fmq_client = new FmqClient ();
        fmq_client.setInbox (fmq_inbox);
        fmq_client.setResync (1);
        fmq_client.subscribe ("/");*/
    }
    
    protected static Agent newAgent (ZContext ctx, Socket pipe) 
    {
        Socket inbox = ctx.createSocket (ZMQ.ROUTER);
        if (inbox == null)      //  Interrupted
            return null;

        //ZreUdp udp = new ZreUdp (PING_PORT_NUMBER);
        int port = inbox.bindToRandomPort ("tcp://*", 0xc000, 0xffff);
        if (port < 0) {          //  Interrupted
            System.err.println ("Failed to bind a random port");
            //udp.destroy ();
            return null;
        }
		return null;
        
        //return new Agent (ctx, pipe, inbox, udp, port);
    }
    
    protected void destroy () 
    {
        //FmqDir inbox = FmqDir.newFmqDir (fmq_inbox, null);
        /*if (inbox != null) {
            inbox.remove (true);
            inbox.destroy ();
        }
        
        //FmqDir outbox = FmqDir.newFmqDir (fmq_outbox, null);
        if (outbox != null) {
            outbox.remove (true);
            outbox.destroy ();
        }
        
        for (DecentralPeer peer : peers.values ())
            peer.destroy ();
        for (DecentralGroup group : peer_groups.values ())
            group.destroy ();
        for (DecentralGroup group : own_groups.values ())
            group.destroy ();
        
        fmq_server.destroy ();
        fmq_client.destroy ();
        udp.destroy ();*/
        
    }
    
    private int incStatus ()
    {
        if (++status > UBYTE_MAX)
            status = 0;
        return status;
    }
    
    //  Delete peer for a given endpoint
    private void purgePeer ()
    {
        for (Map.Entry <String, DecentralPeer> entry : peers.entrySet ()) {
            DecentralPeer peer = entry.getValue ();
            if (peer.endpoint ().equals (endpoint))
                peer.disconnect ();
        }
    }
    
    //  Find or create peer via its UUID string
    private DecentralPeer requirePeer (String identity, String address, int port)
    {
        DecentralPeer peer = peers.get (identity);
        if (peer == null) {
            //  Purge any previous peer on same endpoint
            String endpoint = String.format ("%s:%d", address, port);
            
            purgePeer ();

            //peer = DecentralPeer.newPeer (identity, peers, ctx);
            peer.connect (this.identity, endpoint);

            //  Handshake discovery by sending HELLO as first message
            DecenMsg msg = new DecenMsg(DecenMsg.HELLO);
            //msg.setIpaddress (this.udp.host ()); 
            msg.setMailbox (this.port);
            msg.setGroups (own_groups.keySet ());
            msg.setStatus (status);
            msg.setHeaders (new HashMap <String, String> (headers));
            peer.send (msg);

            //log.info (ZreLogMsg.ZRE_LOG_MSG_EVENT_ENTER,peer.endpoint (), endpoint);

            //  Now tell the caller about the peer
            pipe.sendMore ("ENTER");
            pipe.send (identity);
        }
        return peer;
    }
    
    //  Find or create group via its name
    private DecentralGroup requirePeerGroup (String name)
    {
        DecentralGroup group = peer_groups.get (name);
        if (group == null)
            group = DecentralGroup.newGroup (name, peer_groups);
        return group;

    }
    
    private DecentralGroup joinPeerGroup (DecentralPeer peer, String name)
    {
        DecentralGroup group = requirePeerGroup (name);
        group.join (peer);
        
        //  Now tell the caller about the peer joined a group
        pipe.sendMore ("JOIN");
        pipe.sendMore (peer.identity ());
        pipe.send (name);
        
        return group;
    }
    
    private DecentralGroup leavePeerGroup (DecentralPeer peer, String name)
    {
        DecentralGroup group = requirePeerGroup (name);
        group.leave (peer);
        
        //  Now tell the caller about the peer joined a group
        pipe.sendMore ("LEAVE");
        pipe.sendMore (peer.identity ());
        pipe.send (name);
        
        return group;
    }
    
    //  Here we handle messages coming from other peers
    protected boolean recvFromPeer ()
    {
        //  Router socket tells us the identity of this peer
        DecenMsg msg = DecenMsg.recv(inbox);
        if (msg == null)
            return false;               //  Interrupted

        String identity = new String (msg.address ().getData ());
        
        //  On HELLO we may create the peer if it's unknown
        //  On other commands the peer must already exist
        DecentralPeer peer = peers.get (identity);
        if (msg.id () == DecenMsg.HELLO) {
            peer = requirePeer (
                identity, msg.ipaddress (), msg.mailbox ());
            assert (peer != null);
            peer.setReady (true);
        }
        //  Ignore command if peer isn't ready
        if (peer == null || !peer.ready ()) {
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
            for (String name : msg.groups ()) {
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
        if (msg.id () == DecenMsg.WHISPER) {
            //  Pass up to caller API as WHISPER event
            ZFrame cookie = msg.content ();
            pipe.sendMore ("WHISPER");
            pipe.sendMore (identity);
            //cookie.sendAndKeep (pipe); // let msg free the frame
        }
        else
        if (msg.id () == DecenMsg.SHOUT) {
            //  Pass up to caller as SHOUT event
            ZFrame cookie = msg.content ();
            pipe.sendMore ("SHOUT");
            pipe.sendMore (identity);
            pipe.sendMore (msg.group ());
            //cookie.sendAndKeep (pipe); // let msg free the frame
        }
        else
        if (msg.id () == DecenMsg.PING) {
            DecenMsg pingOK = new DecenMsg(DecenMsg.PING_OK);
            peer.send (pingOK);
        }
        else
        if (msg.id () == DecenMsg.JOIN) {
            joinPeerGroup (peer, msg.group ());
            assert (msg.status () == peer.status ());
        }
        else
        if (msg.id () == DecenMsg.LEAVE) {
            leavePeerGroup (peer, msg.group ());
            assert (msg.status () == peer.status ());
        }
        msg.destroy ();

        //  Activity from peer resets peer timers
       // peer.refresh ();
        return true;
    }

    //  Handle beacon
    protected boolean recvUdpBeacon ()
    {
        ByteBuffer buffer = ByteBuffer.allocate (Beacon.BEACON_SIZE);
        
        //  Get beacon frame from network
        int size = 0;
        buffer.rewind ();
        
        //  Basic validation on the frame
        if (size != Beacon.BEACON_SIZE
                || buffer.get () != 'Z'
                || buffer.get () != 'R'
                || buffer.get () != 'E'
                || buffer.get () != Beacon.BEACON_VERSION)
            return true;       //  Ignore invalid beacons
        
        //  If we got a UUID and it's not our own beacon, we have a peer
        Beacon beacon = new Beacon (buffer);
        if (!beacon.uuid.equals (uuid)) {
            String identity = uuidStr (beacon.uuid);
            //DecentralPeer peer = requirePeer (identity, udp.from (), beacon.port);
            //peer.refresh ();
        }
        
        return true;
    }

    //  Send moar beacon
    public void sendBeacon ()
    {
        Beacon beacon = new Beacon (uuid, port);
    }
    
    //  We do this once a second:
    //  - if peer has gone quiet, send TCP ping
    //  - if peer has disappeared, expire it
    public void pingAllPeers ()
    {
        Iterator <Map.Entry <String, DecentralPeer>> it = peers.entrySet ().iterator ();
        while (it.hasNext ()) {
            Map.Entry<String, DecentralPeer> entry = it.next ();
            String identity = entry.getKey ();
            DecentralPeer peer = entry.getValue ();
            if (System.currentTimeMillis () >= peer.expiredAt ()) {            
                pipe.sendMore ("EXIT");
                pipe.send (identity);
                deletePeerFromGroups (peer_groups, peer);
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
   
}

//  Send message to all peers
private static void sendPeers (Map <String, DecentralPeer> peers, DecenMsg msg)
{
    for (DecentralPeer peer : peers.values ())
        peer.send (msg);
}

//  Remove peer from group, if it's a member
private static void deletePeerFromGroups (Map <String, DecentralGroup> groups, DecentralPeer peer)
{
    for (DecentralGroup group : groups.values ())
        group.leave (peer);
}

private static class ZreInterfaceAgent 
                        implements ZThread.IAttachedRunnable 
{

    @Override
    public void run (Object[] args, ZContext ctx, Socket pipe)
    {
        Agent agent = Agent.newAgent (ctx, pipe);
        if (agent == null)   //  Interrupted
            return;
        
        long pingAt = System.currentTimeMillis ();
        Poller items = ctx.getContext ().poller ();
        
        items.register (agent.pipe, Poller.POLLIN);
        items.register (agent.inbox, Poller.POLLIN);
        
        while (!Thread.currentThread ().isInterrupted ()) {
            long timeout = pingAt - System.currentTimeMillis ();
            assert (timeout <= PING_INTERVAL);
            
            if (timeout < 0)
                timeout = 0;
            
            if (items.poll (timeout) < 0)
                break;      // Interrupted
            
            if (items.pollin (0))
                agent.recvFromPeer ();
            
            if (items.pollin (1))
                agent.recvUdpBeacon ();
            
            if (System.currentTimeMillis () >= pingAt) {
                agent.sendBeacon ();
                pingAt = System.currentTimeMillis () + PING_INTERVAL;
                //  Ping all peers and reap any expired ones
                agent.pingAllPeers ();
            }
        }
        agent.destroy ();
    }
}
}