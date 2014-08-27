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
      private static final int Threads = 10;
      
      
	  public static void main(String[] args) throws IOException {
		  Context context = ZMQ.context(1);
		  ZMQ.Socket router = context.socket(ZMQ.ROUTER);
		  router.bind("tcp://*:7000");
	
          for (int workerNbr = 0; workerNbr < 11; workerNbr++)
          {
        	  Thread worker = new ServerThread(context);
              worker.start();
          }
          
          //  Run for five seconds and then tell workers to end
          long endTime = System.currentTimeMillis () + 5000;
          int workersFired = 0;
          
          while (true) {
              //  Next message gives us least recently used worker
              String identity = router.recvStr ();
              router.sendMore (identity);
              router.recv (0);     //  Envelope delimiter
              router.recv (0);     //  Response from worker
              router.sendMore ("");

              //  Encourage workers until it's time to fire them
              if (System.currentTimeMillis () < endTime)
            	  router.send ("Work harder");
              else {
            	  router.send ("Handshake");
                  if (++workersFired == Threads)
                      break;
              }
          }

          router.close();
          context.term();
		  
	  }
    
}
