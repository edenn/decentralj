package com.decentralbank.decentralj.commandline;

import java.util.HashMap;

public class CommandFactory {
    static HashMap<String, Command> map = new HashMap<>();

    public static void setFactory() {
        map.put("start", new start());
        map.put("?", new printHelp());
        map.put("help", new printHelp());
        map.put("gen", new generateAddress());
        map.put("port", new setPort());
        map.put("connect", new connect());
        map.put("listen", new listen());
        map.put("bind", new bindToPort());
        map.put("add", new add());
    }

    public static Command getCommand(String type){
        switch(type){
            case "start":
                map.get("start").execute();
                break;
            case "?":
                map.get("?").execute();
                break;
            case "help":
                map.get("help").execute();
                break;
            case "gen":
                map.get("gen").execute();
                break;
            case "port":
                map.get("port").execute();
                break;
            case "connect":
                map.get("connect").execute();
                break;
            case "listen":
                map.put("listen", new listen());
                break;
            case "bind":
                map.get("bind").execute();
                break;
        }
        return null;
    }
}
