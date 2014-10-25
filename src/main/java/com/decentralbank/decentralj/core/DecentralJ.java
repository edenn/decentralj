package com.decentralbank.decentralj.core;

import java.security.SecureRandom;
import java.util.Scanner;

import com.decentralbank.decentralj.commandline.CommandFactory;

public class DecentralJ {

	public DecentralJ(){
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

}
