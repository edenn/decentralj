package com.decentralbank.decentralj;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZThread;

import com.decentralbank.decentralj.core.Node;
import com.decentralbank.decentralj.net.ServerThread;

public class App 
{          
	  public static void main(String[] args) throws IOException {
		  
		  	Node app = new Node();
		  	app.start();
	  }
    
}
