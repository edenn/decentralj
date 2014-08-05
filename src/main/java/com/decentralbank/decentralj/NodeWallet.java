package com.decentralbank.decentralj;

import java.io.File;
import java.io.IOException;

import com.google.bitcoin.core.*;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO Add a Listener to detect changes in values

public class NodeWallet {
	
	private String amountToSend;
	private String recipient;
	private Wallet wallet;  
	final NetworkParameters netParams = NetworkParameters.testNet();
    private Map<Address, Account> NodeInfo = new HashMap<Address, Account>();
    private Map<BigInteger, Transaction> transactionMap = new HashMap<BigInteger, Transaction>();
    
    public String getamountToSend(){
    	return amountToSend;
    }
    
    public void setamountToSend(String amount){
    	amountToSend = amount;
    }
    
    public String getRecipient(){
    	return recipient;
    }
    
    public void setRecipient(String recipient){
    	this.recipient = recipient;
    }
    
	 public void addNewAccount() {
	        Account account = new Account();
	        Address address = account.getAddress();
	        NodeInfo.put(address, account);
	    }

	 /*
	 public void importKey(BigInteger privKey) {
	        Account account = new Account(ECKey.fromPrivate(privKey));
	        Address address = account.getAddress();
	        NodeInfo.put(address, account);
	    }*/
	 
	 public BigInteger getBalance(Address _address) {
	        Address address = _address;
	        return NodeInfo.get(address).getBalance();
	    }
	 
	
	    public void loadWallet(){
	    	
	    	        Wallet wallet = null;
	    	         final File walletFile = new File("test.wallet");
	    	         
	    	         try {
	    	             wallet = new Wallet(netParams);
	    	             
	    	             // 5 times
	    	             for (int i = 0; i < 5; i++) {
	    	                 
	    	                 // create a key and add it to the wallet
	    	                 wallet.addKey(new ECKey());
	    	             }
	    	             
	    	             // save wallet contents to disk
	    	             wallet.saveToFile(walletFile);
	    	             
	    	         } catch (IOException e) {
	    	             System.out.println("Unable to create wallet file.");
	    	        }
	        
	    }
	 
	 public BigInteger totalBalance() {
		 
	        BigInteger sum = BigInteger.ZERO;
	        
	        for (Account account : NodeInfo.values()) {
	            sum = sum.add(account.getBalance());
	        }
	        
	        return sum;
	    }
	 
	 
	 public void buildTransaction(Transaction transaction){
		 
		 
	 }
	   
    
}