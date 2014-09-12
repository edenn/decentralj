package com.decentralbank.decentralj.net;

import java.util.HashMap;
import java.util.Map;

//voting pool
public class DecentralGroup
{

    private final String name;
    private final Map <String, DecentralPeer> peers;
    
    private DecentralGroup (String name) {
        this.name = name;
        peers = new HashMap <String, DecentralPeer> ();
    }
    
    //  Construct new group object
    public static DecentralGroup newGroup (String name, Map<String, DecentralGroup> container)
    {
    	DecentralGroup group = new DecentralGroup (name);
        container.put (name, group);
        
        return group;
    }

    //  Add peer to group
    //  Ignore duplicate joins
    public void join (DecentralPeer peer) {
        peers.put (peer.identity (), peer);
        peer.incStatus ();
    }
    
    //  Remove peer from group
    public void leave (DecentralPeer peer) {
        peers.remove (peer.identity ());
        peer.incStatus ();
    }
    
    //  Send message to all peers in group
    public void send (ZreMsg msg) {
        for (DecentralPeer peer: peers.values ())
            peer.send (msg.dup ());
        
        msg.destroy ();
    }

}