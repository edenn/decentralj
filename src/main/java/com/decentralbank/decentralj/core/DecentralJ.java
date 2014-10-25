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
                    CommandFactory.getCommand(cmd.toLowerCase());
		}
	}

}
