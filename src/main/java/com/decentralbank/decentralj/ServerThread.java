package com.decentralbank.decentralj;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;


public class ServerThread {
	
	    private ZMQ.Context context = ZMQ.context(1);
	    private ZMQ.Socket socket = null;
	    private Node peer;

	    public ServerThread(Socket socket, Node peer) {
			this.socket = socket;
			this.peer = peer;
	    }
	    
	    public void run() {

	    	 ZMQ.Socket socket = context.socket(ZMQ.REP);
	    	 
		        socket.bind("tcp://*:5557");
		        
		        for (int requestNbr = 0; requestNbr != 10; requestNbr++) {
		            String request = "Hello";
		            System.out.println("Sending Hello " );
		            socket.send(request.getBytes(), 0);

		            byte[] reply = socket.recv(0);
		            System.out.println("Received " + new String(reply) + " ");
		        }
		        
		        
		        while (!Thread.currentThread().isInterrupted()) {
		            // Wait for next request from the client
		            byte[] request = socket.recv(0);
		            // Send reply back to client
		            String reply = "I'm alive!";
		            socket.send(reply.getBytes(), 0);
		        }
		        socket.close();
		        context.term();
		    }
	        
	    
}


