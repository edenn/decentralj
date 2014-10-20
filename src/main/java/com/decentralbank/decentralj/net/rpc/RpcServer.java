package com.decentralbank.decentralj.net.rpc;

import com.decentralbank.decentralj.core.CommandLine;
import com.googlecode.jsonrpc4j.JsonRpcServer;
import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import java.io.IOException;
import javax.portlet.ResourceRequest;
import javax.servlet.ServletConfig;

public class RpcServer extends HttpServlet {

    private JsonRpcServer jsonRpcServer;
    private CommandLine commands;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)  throws IOException{
        this.jsonRpcServer.handle(req, resp);

    }

    public void init(ServletConfig config) {
        //this.userService = ...;
        this.jsonRpcServer = new JsonRpcServer(this.commands, CommandLine.class);
        String value = getServletConfig().getInitParameter("host");
        System.out.print(value);
    }



}
