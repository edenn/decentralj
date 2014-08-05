package com.decentralbank.decentralj;

import org.zeromq.ZMQ.Poller;
import org.zeromq.ZThread;
import org.zeromq.ZMsg;
import org.zeromq.ZContext;
import org.zeromq.ZFrame;

public class heartbeat {
	
    private final static int HEARTBEAT_LIVENESS = 3 ;      //  3-5 is reasonable
    private final static int HEARTBEAT_INTERVAL = 1000;    //  msecs
    private final static int INTERVAL_INIT      = 1000;    //  Initial reconnect
    private final static int INTERVAL_MAX      = 32000;    //  After exponential 

}
