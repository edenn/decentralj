package com.decentralbank.decentralj.dht;


//Peer Information to update DHT
public class Contact extends Addressable{

    private String protocol;
    private String location;
    private String port;

    public Contact() {
        super();
    }

    protected Contact(String protocol, String location, String port) {
        super();
        this.protocol = protocol;
        this.location = location;
        this.port = port;
    }

    private Contact(byte[] id,String protocol, String location, String port) {
        this.id = id;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setId(byte[] id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Contact:" + getId();
    }

}
