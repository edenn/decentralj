package com.decentralbank.decentralj.core;

import java.security.SecureRandom;
import java.util.Scanner;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;

import com.decentralbank.decentralj.net.DecentralGroup;
import com.decentralbank.decentralj.net.ServerThread;

public class DecentralJ {
	
	public static Node decentralNode;
	private static  ZMQ.Socket serverSocket = null;
    private boolean listening = true;
    private static ZMQ.Context context = ZMQ.context(1);
    private static final int Threads = 10;
	
	public DecentralJ(){
		ZMQ.Socket serverSocket = context.socket(ZMQ.ROUTER);
		SecureRandom sr = new SecureRandom();
	}
	
	public void run() throws InterruptedException {
		System.out.println("Decentral Network Version 0.0.1");
		System.out.println("Type 'help' for a list of commands");
		String cmd = null;
		while (true){
			System.out.println("Enter a command:");
			Scanner in = new Scanner(System.in);
			System.out.print(">>> ");	
			cmd = in.nextLine();
			switch(cmd.toLowerCase()){
				case "start":
					start();
					break;
					
				case "next":
					break;	
					
				case "help":
					printHelp();
					break;
			}
		}
	}
	
	public void start() {
		System.out.println("Decentral: Starting server...");
		new Thread(new Runnable() {
		    public void run() {
		    	  Context context = ZMQ.context(1);
				  ZMQ.Socket router = context.socket(ZMQ.ROUTER);
				  router.bind("tcp://*:7001");
			
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
		}).start();
		System.out.println("Decentral: Connecting to peers...");
		decentralNode = new Node();
	}
	
	public void printHelp(){
		System.out.println("Commands: ");
		System.out.println("    -start                   Starts Decentral sever and clients");
	}
	

}
