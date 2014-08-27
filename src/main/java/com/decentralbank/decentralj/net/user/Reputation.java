package com.decentralbank.decentralj.net.user;

import java.io.Serializable;

/**
 * Reputation for Users
 */
//left as a Todo 
public class Reputation implements Serializable {
    private long upTime;
    private String solvency;
    private String trust;



   
    public Reputation (long upTime, String solvency, String trust) {
    	this.upTime = upTime;
    	this.solvency = solvency;
    }

    //implement
    @Override
    public String toString() {
        return "99.99999";
    }

}