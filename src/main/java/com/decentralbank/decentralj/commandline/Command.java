package com.decentralbank.decentralj.commandline;

import com.decentralbank.decentralj.core.Node;
import com.decentralbank.decentralj.net.ServerThread;
import org.zeromq.ZMQ;

interface Command {
    public void execute(Object ... args);
}

class start implements Command
{
    Node decentralNode = Node.getInstance();
    ZMQ.Context context = ZMQ.context(1);
    public void execute(Object ... args) {
        System.out.println("Decentral: Starting server...");
        //running thread alongside
        ServerThread server = new ServerThread(context);
        server.run();
        System.out.println("Decentral: Connecting to peers...");
        decentralNode = new Node();
    }
}

class printHelp implements Command
{
    public void execute(Object ... args) {
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

class listen implements Command
{
    Node decentralNode = Node.getInstance();
    ZMQ.Context context = ZMQ.context(1);
    public void execute(Object ... args) {

    }
}

class setPort implements Command
{

    public void execute(Object ... args) {

    }
}

class connect implements Command
{
    public void execute(Object ... args) {

    }

}

class generateAddress implements Command
{

    public void execute(Object ... args) {

    }
}

class bindToPort implements Command
{

    public void execute(Object ... args) {

    }
}


class exit implements Command
{

    public void execute(Object ... args) {
        Runtime.getRuntime().halt(0);
    }
}

class add implements Command
{

    public void execute(Object... args) {
        try {
            if(args.length == 2)
            {
                int a = (int)args[0];
                int b = (int)args[1];
                System.out.println(a + b);
            }
            else throw new Exception("incorrect num of inputs");
        }
        catch(Exception e){
            System.out.println(e);
        }

    }

}
