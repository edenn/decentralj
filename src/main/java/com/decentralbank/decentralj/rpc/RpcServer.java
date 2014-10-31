package com.decentralbank.decentralj.rpc;

import com.googlecode.jsonrpc4j.JsonRpcServer;

import javax.servlet.http.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class RpcServer extends HttpServlet {

    private JsonRpcServer jsonRpcServer;
    int maxThreads = 50;
    int port = 1420;
    InetAddress bindAddress;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)  throws IOException{
        this.jsonRpcServer.handle(req, resp);
    }

    public void init() {
        System.out.print("geeee");
        //this.userService = ...;
        //String value = this.jsonRpcServer.toString();
        //this.jsonRpcServer = new JsonRpcServer(this.commands,CommandServiceImpl.class);
        //System.out.print(value);
        // create the stream server


        try {
            bindAddress = InetAddress.getByName("");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


       // StreamServer streamServer = new StreamServer(jsonRpcServer, maxThreads, port, bindAddress);

// start it, this method doesn't block
       // streamServer.start();
    }



}
