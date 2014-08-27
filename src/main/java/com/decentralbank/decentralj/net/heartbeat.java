package com.decentralbank.decentralj.net;

import org.zeromq.ZMQ.Poller;
import org.zeromq.ZThread;
import org.zeromq.ZMsg;
import org.zeromq.ZContext;
import org.zeromq.ZFrame;

public class HeartBeat {
	
    private final static int HEARTBEAT_LIVENESS = 4 ;      //  3-5 is reasonable
    private final static int HEARTBEAT_INTERVAL = 10000;    //  msecs
    private final static int INTERVAL_INIT      = 10000;    //  Initial reconnect
    private final static int INTERVAL_MAX      = 32000;    //  After exponential 

}
