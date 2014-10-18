package com.decentralbank.decentralj.user;

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
    	this.trust = trust;
    }

    //implement
    @Override
    public String toString() {
        return "upTime: "+ upTime + " solvency:" +solvency  + " trust"+trust ;
    }

}