package com.decentralbank.decentralj.core;

import java.security.SecureRandom;
import java.util.Scanner;

import com.decentralbank.decentralj.commandline.CommandFactory;
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
		//ZMQ.Socket serverSocket = context.socket(ZMQ.ROUTER);
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
            CommandFactory.setFactory();
			switch(cmd.toLowerCase()){
				case "start":
                    CommandFactory.getCommand("start");
					start();
					break;
                case "?":
                    CommandFactory.getCommand("?");
                    break;
				case "help":
                    CommandFactory.getCommand("help");
					break;
                case "testnet":
                    CommandFactory.getCommand("testnet");
                    break;
                case "gen":
                    CommandFactory.getCommand("gen");
                    break;
                case "port":
                    CommandFactory.getCommand("port");
                    break;
                case "connect":
                    CommandFactory.getCommand("connect");
                    break;
                case "listen":
                    CommandFactory.getCommand("listen");
                    break;
                case "bind":
                    CommandFactory.getCommand("bind");
                    break;
                case "exit":
                    CommandFactory.getCommand("exit");
                    break;
			}
		}
	}
	//running serverThread and instantiating Node.
	public void start() {
		System.out.println("Decentral: Starting server...");
		//running thread alongside
		ServerThread server = new ServerThread(context);
		server.run();
		System.out.println("Decentral: Connecting to peers...");
		decentralNode = new Node();
	}

	public void printHelp(){
		System.out.println("Commands: ");
		System.out.println("    -start                   Starts Decentral sever and clients");
        System.out.println("    -testnet                 Use Bitcoin Testnet (Default)");
        System.out.println("    -gen                     Generate Deposit Multisig Addresses");
        System.out.println("    -connect                 Connect only to the specified node(s)");
        System.out.println("    -listen                  Accept connections from outside");
        System.out.println("    -bind                    Bind to given address and always listen on it.");
        System.out.println("    -exit                    Shutdown");
    }
	

}
