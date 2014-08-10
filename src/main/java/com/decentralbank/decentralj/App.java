package com.decentralbank.decentralj;
import java.io.IOException;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;

public class App 
{
	  private static  ZMQ.Socket serverSocket = null;
      private boolean listening = true;
      private static ZMQ.Context context = ZMQ.context(1);
      
	  public static void main(String[] args) throws IOException {
	    
		  Node lepeer= new Node();
		  lepeer.setID("sd");

		  		//lepeer.process();
		  		//lepeer.run();
	            
	            try {
	            	serverSocket = context.socket(ZMQ.REP);
	    			System.out.println("Server "+lepeer.getID()+" Started!");
	            } catch (Exception e) {
	                System.err.println("Could not listen on port: "+Integer.parseInt(lepeer.getPort()));
	                System.exit(-1);
	            }
	            //passing the object to the ServerThread object
	           // while (listening)
	            //	new ServerThread(serverSocket.accept(), peer).start();

	           // serverSocket.close();
	        
		  
		  
	  }
    
}
