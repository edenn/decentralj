package com.decentralbank.decentralj.net;
import java.net.InetAddress;
import java.util.HashMap;

import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMQException;
import org.zeromq.ZMsg;
import org.zeromq.ZThread;

import com.decentralbank.decentralj.core.Node;
//The server runs as a background thread so that we can run multiple
//engines at once.
public class ServerThread extends Thread  {
	
	    private Context context;
	    private DecentralPeer peermodel;
	    private HeartBeat heartbeat;
	    private Node node;
	    public static HashMap<String, String> identities = new HashMap<String,String>();
	    //set PeerModel to retrieve info from client server ip address and stuff
	    private DecentralPeer PeerNetInfo = new DecentralPeer();
	    
	    public ServerThread(Context context) {
			this.context = context;
	    }
	    
	    public void run(){
	    	
			    Context context = ZMQ.context(1);
		        Socket sink = context.socket(ZMQ.ROUTER);
		        sink.bind("inproc://example");

		        //test socket
		        Socket anonymous = context.socket(ZMQ.DEALER);
		        anonymous.setIdentity("PEER".getBytes());
		        anonymous.connect("inproc://example");
		 
		    	int i = 0;
		        while (!Thread.currentThread().isInterrupted()) {
		        	   
		 
		            // Wait for next request from the client, wrap it inside ZMsg		        	 
		        	ZMsg reque = new ZMsg();
		        	ZMsg request = reque.recvMsg(sink);
		        		
		        	if (request == null)
		        		break;          	//  break if null
		        	 //create a list to store frames in
		        	 Object[] list = new Object[request.size()];     
		        	  list = request.toArray();
		        	  //list[0] is peer identity, list[1] is data
		        	  identities.put(list[0].toString(), list[1].toString());
		        	//print identities
		            System.out.println(identities);
		            
		            // sleep 1 sec
		            try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            i++;
		        }

		    
		        sink.close ();
		        anonymous.close ();
		        context.term();

		    }
	        
	    
}

