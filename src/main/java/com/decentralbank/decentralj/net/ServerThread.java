package com.decentralbank.decentralj.net;
import java.net.InetAddress;

import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMQException;
import org.zeromq.ZThread;

import com.decentralbank.decentralj.core.Node;
//The server runs as a background thread so that we can run multiple
//engines at once.
public class ServerThread extends Thread  {
	
	    private Context context;
	    private PeerModel peermodel;
	    private HeartBeat heartbeat;
	    private Node node;
	    //set PeerModel to retrieve info from client server ip address and stuff
	    private PeerModel PeerNetInfo = new PeerModel();
	    
	    public ServerThread(Context context) {
			this.context = context;
	    }
	    
	    public void run(){
	    	
	    	   Node Decentral = Node.getInstance();
	    	   
	    	   //generate ip address or hash to classify a connection without requiring ip addresses
	    	   Decentral.addPeer(123, PeerNetInfo);
	    	   String PeerInfo = PeerNetInfo.toString();
	    	   System.out.println(PeerInfo);
	    	 
	    	   Context context = ZMQ.context(1);
	           Socket worker = context.socket(ZMQ.DEALER);
	           worker.connect("tcp://127.0.0.1:7000");
	            
	            int total = 0;
	            while (true) {
	                //  Tell the broker we're ready for work
	                worker.send ("Handshake");
	            	System.out.print("herro");
	                //  Get workload from broker, until finished
	            	worker.send(PeerInfo);
	            	String received = worker.recvStr();
	                worker.recvStr ();   //  Envelope delimiter
	                String workload = worker.recvStr ();
	                boolean finished = workload.equals ("Fired!");
	                if (finished) {
	                    System.out.printf("Completed: %d tasks\n", total);
	                    break;
	                }
	                total++;

	                //  sleep
	                try {
	                    Thread.sleep (4);
	                } catch (InterruptedException e) {
	                }
	            }
	            worker.close();
	            context.term();

		    }
	        
	    
}

