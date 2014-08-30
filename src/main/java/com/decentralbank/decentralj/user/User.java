package com.decentralbank.decentralj.user;

import java.io.Serializable;
import java.security.KeyPair;
import java.util.List;

import com.decentralbank.decentralj.core.NodeWallet;

// The User is persisted locally
// The User Class represents the person who sets up the server.

public class User implements Serializable {
	
	//create transient property for states
	
	private KeyPair messageKeyPair;
	private String accountID;
	// TODO make it thread safe
	private List<NodeWallet> Accounts;
	private NodeWallet currentAccount;
	
	public void addBankAccount(NodeWallet Account){
	        
		   if (!Accounts.contains(Account)){
			 
			   Accounts.add(Account);
		   
		   }

	       setCurrentAccount(Account);
	}

	public String getAccount(){
		return accountID;
		   
	}
	
	private void setCurrentAccount(NodeWallet account) {
	
		this.currentAccount = account;	   
		
	}

	public void removeCurrentAccount(){
		
	        if (currentAccount != null){
	        	
	        	Accounts.remove(currentAccount);
	       
	        }

	        if (Accounts.isEmpty()){
	        	
	        	currentAccount = null;
	        
	        } else{
	        	
	        	setCurrentAccount(Accounts.get(0));
	       
	        }
	}
	    


}
