package com.decentralbank.decentralj;

import java.io.File;
import java.io.IOException;

import com.google.bitcoin.core.*;
import com.google.bitcoin.store.BlockStore;
import com.google.bitcoin.store.BlockStoreException;
import com.google.bitcoin.store.MemoryBlockStore;
import com.google.bitcoin.store.UnreadableWalletException;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
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
	private File walletFile;
    private Map<Address, Account> NodeInfo = new HashMap<Address, Account>();
    private Map<BigInteger, Transaction> transactionMap = new HashMap<BigInteger, Transaction>();
    private BlockStore blockStore = new MemoryBlockStore(netParams);
    private BlockChain chain;
    //final Peer peer = new Peer(netParams, new PeerAddress(InetAddress.getLocalHost()), chain);

  
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
	 
	 public BigInteger getBalance(Address _address) {
		 
	        return NodeInfo.get(_address).getBalance();
	        
	    }
	 
	 public void setBalance(Address _address,BigInteger amount) {
		 
	       NodeInfo.get(_address).setBalance(amount);
	        
	    }
	 
	
	 public void createWallet(){
	    	
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
	    
	 public void loadWallet(String filename){
			 // wallet file that contains Bitcoins we can send
	          walletFile = new File(filename);	
		     // load wallet from file
		     try {
				wallet = Wallet.loadFromFile(walletFile);
			} catch (UnreadableWalletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		            
		
	 }
	 
	 
	 public BigInteger totalWalletBalance() {
		 
	        BigInteger sum = BigInteger.ZERO;
	        
	        for (Account account : NodeInfo.values()) {
	            sum = sum.add(account.getBalance());
	        }
	        
	        return sum;
	    }
	 
	 
	 public void buildTransaction(Transaction transaction) throws BlockStoreException, AddressFormatException, InsufficientMoneyException, IOException{
		// how man milli-Bitcoins to send
		 BigInteger btcToSend = new BigInteger(amountToSend);
                   
		 // initialize BlockChain object
		 chain = new BlockChain(netParams, wallet, blockStore);
		
		 // instantiate Peer object to handle connections
		// final Peer peer = new Peer(netParams, null, new PeerAddress(InetAddress.getLocalHost()), chain, null);
		
		 PeerGroup peerGroup = new PeerGroup(netParams, chain);
		 peerGroup.addWallet(wallet);
		 peerGroup.startAndWait();
		             
		 // recipient address provided by official Bitcoin client
		 Address recipientAddress = new Address(netParams, recipient);
		 
		 // tell peer to send amountToSend to recipientAddress
		 //Transaction sendTxn = wallet.sendCoins(peer, recipientAddress, btcToSend);
		 Wallet.SendResult result = wallet.sendCoins(peerGroup, recipientAddress, Utils.toNanoCoins(1, 23));           
		 // null means we didn't have enough Bitcoins in our wallet for the transaction
		 if (result == null) {
		 System.out.println("Cannot send requested amount of " + Utils.bitcoinValueToFriendlyString(btcToSend)
		  + " BTC; wallet only contains " + Utils.bitcoinValueToFriendlyString(wallet.getBalance()) + " BTC.");
		 } else {              
		             // once communicated to the network (via our local peer),
		             // the transaction will appear on Bitcoin explorer sooner or later
	                 System.out.println(Utils.bitcoinValueToFriendlyString(btcToSend) + " BTC sent. You can monitor the transaction here:\n"
		                        + "http://blockexplorer.com/tx/" + result.toString());
	            }
		 
		            // save wallet with new transaction(s)
		            wallet.saveToFile(walletFile);
		 
	 }
	 
	 
	 /*
	 public void importKey(BigInteger privKey) {
	        Account account = new Account(ECKey.fromPrivate(privKey));
	        Address address = account.getAddress();
	        NodeInfo.put(address, account);
	    }*/
	   
    
}