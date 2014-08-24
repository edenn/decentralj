package com.decentralbank.decentralj;
import java.io.IOException;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZThread;

import com.decentralbank.decentralj.net.ServerThread;

public class App 
{
	  private static  ZMQ.Socket serverSocket = null;
      private boolean listening = true;
      private static ZMQ.Context context = ZMQ.context(1);
      
	  public static void main(String[] args) throws IOException {
	    

	        Context context = ZMQ.context(1);

	        //ZMQ.Socket clients = context.socket(ZMQ.ROUTER);
	       // clients.connect ("tcp://*:59049");

	        ZMQ.Socket workers = context.socket(ZMQ.DEALER);
	        workers.bind ("inproc://servers");
	        
	        for(int thread_nbr = 0; thread_nbr < 5; thread_nbr++) {
	            ZThread worker = new ServerThread(context);
	         //   worker.start(arg0, arg1);;
	    
	           // worker.start();
	        }
	     
	        //  Connect work threads to client threads via a queue
	        //ZMQ.proxy (clients, workers, null);

	        //  We never get here but clean up anyhow
	        //clients.close();
	        workers.close();
	        context.term();
	            //passing the object to the ServerThread object
	           // while (listening)
	            //	new ServerThread(serverSocket.accept(), peer).start();

	           // serverSocket.close();
	        
		  
		  
	  }
    
}
