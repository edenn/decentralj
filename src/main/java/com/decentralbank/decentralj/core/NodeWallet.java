package com.decentralbank.decentralj.core;

import java.io.File;
import java.io.IOException;

import com.google.bitcoin.core.*;
import com.google.bitcoin.script.Script;
import com.google.bitcoin.core.Transaction;
import com.google.bitcoin.script.ScriptBuilder;
import com.google.bitcoin.store.BlockStore;
import com.google.bitcoin.store.BlockStoreException;
import com.google.bitcoin.store.MemoryBlockStore;
import com.google.bitcoin.store.UnreadableWalletException;
import com.google.common.collect.ImmutableList;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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

  
    public static void main(String args[]) throws AddressFormatException {
    	NetworkParameters np = NetworkParameters.testNet();
    	
    	NodeWallet lewallet = new NodeWallet();
    	lewallet.addNewAccount();
    	lewallet.createWallet();
    	lewallet.getTotalWalletBalance();
    	
    }
    
    public NodeWallet() {
    	
    }
    
    // get amount to send
    public String getamountToSend(){
    	return amountToSend;
    }
    
    // set amount to send
    public void setamountToSend(String amount){
    	amountToSend = amount;
    }
    
    //get Recipient
    public String getRecipient(){
    	return recipient;
    }
    
    // set Recipient
    public void setRecipient(String recipient){
    	this.recipient = recipient;
    }
    
    //add a new account to NodeInfo
	 public void addNewAccount() {
	     Account account = new Account();
	     Address address = account.getAddress();
	     NodeInfo.put(address, account);
	 }
	 
	 public BigInteger getBalance(Address _address) {
		 
	    return NodeInfo.get(_address).getBalance();
	        
	 }
	 
	 //set a balance on NodeInfo
	 public void setBalance(Address _address, BigInteger amount) {
		 
	    NodeInfo.get(_address).setBalance(amount);
	        
	 }
	 
	 // create a wallet
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
	 
	 // load a Bitcoin wallet
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
	 
	 //total Bitcoin wallet balance
	 public BigInteger getTotalWalletBalance() {
		 
	        BigInteger sum = BigInteger.ZERO;
	        
	        for (Account account : NodeInfo.values()) {
	            sum = sum.add(account.getBalance());
	        }
	        
	        return sum;
	    }
	 
	 //build transaction
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
	 
	 //serialize a transaction
	 public ECKey.ECDSASignature  serializeTransaction(ECKey key, Transaction contracts ){
			// Assume we get the multisig transaction we're trying to spend from 
			// somewhere, like a network connection.
			ECKey serverKey = key ;
			Transaction contract = contracts;
			Address clientKey = null;
			
			TransactionOutput multisigOutput = contract.getOutput(0);
			Script multisigScript = multisigOutput.getScriptPubKey();
			// Is the output what we expect?
			//checkState(multisigScript.isSentToMultiSig());
			BigInteger value = multisigOutput.getValue();
	
			// OK, now build a transaction that spends the money back to the client.
			Transaction spendTx = new Transaction(netParams);
	
			spendTx.addOutput(value, clientKey);
			spendTx.addInput(multisigOutput);
	
			// It's of the right form. But the wallet can't sign it. So, we have to
			// do it ourselves.
			Sha256Hash sighash = spendTx.hashForSignature(0, multisigScript, Transaction.SigHash.ALL, false);
			ECKey.ECDSASignature signature = serverKey.sign(sighash);
			// We have calculated a valid signature, so send it back to the client:
			return signature;
		
	 }
	 
	 //deserialize a transaction
	 public String deserializeTransaction(){
		 
		 return null;
	 }
	   
	 // create p2sh account
	 public void createMultisig(byte [] publicKeyBytes) throws BlockStoreException, InsufficientMoneyException, InterruptedException, ExecutionException{
			// how man milli-Bitcoins to send
		 BigInteger btcToSend = new BigInteger(amountToSend);
                   
		 // initialize BlockChain object
		 chain = new BlockChain(netParams, wallet, blockStore);
		
		 // instantiate Peer object to handle connections
		// final Peer peer = new Peer(netParams, null, new PeerAddress(InetAddress.getLocalHost()), chain, null);
		
		 PeerGroup peerGroup = new PeerGroup(netParams, chain);
		// Create a random key.
		 ECKey clientKey = new ECKey();
		 // We get the other parties public key from somewhere ...
		 ECKey serverKey = new ECKey(null, publicKeyBytes);

		 // Prepare a template for the contract.
		 Transaction contract = new Transaction(netParams);
		 List<ECKey> keys = ImmutableList.of(clientKey, serverKey);
		 // Create a 2-of-2 multisig output script.
		 Script script = ScriptBuilder.createMultiSigOutputScript(2, keys);
		 // Now add an output for 0.50 bitcoins that uses that script.
		 BigInteger amount = Utils.toNanoCoins(0, 50);
		 contract.addOutput(amount, script);

		 // We have said we want to make 0.5 coins controlled by us and them.
		 // But it's not a valid tx yet because there are no inputs.
		 Wallet.SendRequest req = Wallet.SendRequest.forTx(contract);
		 wallet.completeTx(req);   // Could throw InsufficientMoneyException

		 // Broadcast and wait for it to propagate across the network.
		 // It should take a few seconds unless something went wrong.
		 peerGroup.broadcastTransaction(req.tx).get();
		 
	 }
	 
	 //n lock time implementa
	 public void refund(){
		 
	 }
	 
    
}