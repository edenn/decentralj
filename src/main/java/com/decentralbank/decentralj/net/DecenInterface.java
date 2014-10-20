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

import com.decentralbank.decentralj.core.Request;
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
//  Defined port numbers
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
    ctx = new ZContext();
    pipe = ZThread.fork (ctx, new DecenInterfaceDecentralPeer());
}

//  ---------------------------------------------------------------------
//  Destructor
public void destroy()
{
    ctx.destroy ();
}

//  ---------------------------------------------------------------------
//  Receive next message from interface
//  Returns ZMsg object, or NULL if interrupted
public ZMsg recv()
{
    return ZMsg.recvMsg (pipe);
}

//  ---------------------------------------------------------------------
//  Join a pool
public void join(String group)
{
    pipe.sendMore("JOIN");
    pipe.send(group);
}

//  ---------------------------------------------------------------------
//  Leave a pool
public void leave(String group)
{
    pipe.sendMore("LEAVE");
    pipe.send(group);
}

//  ---------------------------------------------------------------------
//  Send message to single peer; peer ID is first frame in message
//  Destroys message after sending
public void alive(ZMsg msg)
{
    pipe.sendMore ("ALIVE");
    msg.send (pipe);
}

//  ---------------------------------------------------------------------
//  Send message to a group of peers
public void broadcast(ZMsg msg)
{
    pipe.sendMore ("HEARTBEAT");
    msg.send (pipe);
}

//  ---------------------------------------------------------------------
//  Return interface handle, for polling
public Socket handle()
{
    return pipe;
}

//  ---------------------------------------------------------------------
//  Set node header value
public void setHeader(String name, String format, Object ... args)
{
    pipe.sendMore ("SET");
    pipe.sendMore (name);
    pipe.send (String.format (format, args));
}


private static String uuidStr (UUID uuid)
{
    return uuid.toString ().replace ("-","").toUpperCase ();
}

private static final String OUTBOX = ".outbox";
private static final String INBOX = ".inbox";



private static class DecenInterfaceDecentralPeer
                        implements ZThread.IAttachedRunnable 
{

    @Override
    public void run (Object[] args, ZContext ctx, Socket pipe)
    {
        DecentralPeer agent = DecentralPeer.newPeer(ctx, pipe);
        if (agent == null)   //  Interrupted
            return;
        
        long pingAt = System.currentTimeMillis ();
        Poller items = ctx.getContext ().poller ();
        
        //items.register (agent.pipe, Poller.POLLIN);
        //items.register (agent.port, Poller.POLLIN);
        
        while (!Thread.currentThread ().isInterrupted ()) {
            long timeout = pingAt - System.currentTimeMillis ();
            assert (timeout <= PING_INTERVAL);
            
            if (timeout < 0)
                timeout = 0;
            
            if (items.poll (timeout) < 0)
                break;      // Interrupted
            
            if (items.pollin (0))
                agent.recvFromPeer();
            
            if (items.pollin (1))
                agent.recvHeartBeat();
            
            if (System.currentTimeMillis () >= pingAt) {
                agent.sendHeartBeat();
                pingAt = System.currentTimeMillis () + PING_INTERVAL;
                //  Ping all peers and reap any expired ones
                agent.pingAllPeers();
            }
        }
        agent.destroy ();
    }
}
}