package com.decentralbank.decentralj.net;

import java.util.ArrayList;
import java.util.Map;

//peers that are members of a voting pool
public class DecentralGroup
{

    private final String poolName;
    private final ArrayList<DecentralPeer> peers;
    private DHT pool;
    
    private DecentralGroup (String name) {
        this.poolName = name;
        peers = new ArrayList<DecentralPeer> ();
        pool = new DHT();
    }
    
    //  Construct new group object
    public static DecentralGroup newGroup (String name, Map<String, DecentralGroup> container)
    {
    	DecentralGroup group = new DecentralGroup (name);
        container.put (name, group);
        
        return group;
    }

    //  Add peers to group
    public void join(DecentralPeer peer) {
        peers.add(peer);
        peer.incStatus ();
    }
    
    //  Remove peer from group
    public void leave (DecentralPeer peer) {
        peers.remove (peer.identity ());
        peer.incStatus ();
    }
    
    //  Send message to all peers in group
    public void send (DecenMsg msg) {
        for (DecentralPeer peer: peers)
               peer.send (msg);
        msg.destroy ();
    }

    public void destroy() {
    }
}