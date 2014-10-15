package com.decentralbank.decentralj.core;

import java.io.File;
import java.io.IOException;

import com.sun.crypto.provider.AESWrapCipher;
import org.bitcoinj.core.*;
import org.bitcoinj.script.Script;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.MemoryBlockStore;
import org.bitcoinj.store.UnreadableWalletException;
import com.google.common.collect.ImmutableList;

import javax.transaction.TransactionRequiredException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
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

  
    public static void main(String args[]) throws AddressFormatException, BlockStoreException, UnknownHostException {
        System.out.println("NodeWallet");
    	NetworkParameters np = NetworkParameters.testNet();
    	
    	NodeWallet lewallet = new NodeWallet();
    	lewallet.addNewAccount();
    	//lewallet.createWallet();
        Address leaddress = new Address(NetworkParameters.testNet(),"moMR8WDJTd3PDKRNUpErwPs5MvPVyX2mri");
       //lewallet.loadWallet("wallet.dat");


        //System.out.print(lewallet.getBalance());
    	lewallet.getTotalNodeBalance();
        ECKey lekey = new ECKey();
        ECKey lekey2 = new ECKey();
        byte [] bytes = lekey.getPubKey();


        try {
            lewallet.createMultisigAccount(lekey, lekey2);
        } catch (BlockStoreException e) {
            e.printStackTrace();
        } catch (InsufficientMoneyException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //BigInteger Balance = this.wallet.getBalance();

    	
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
	 
	 public Coin getBalance() {
	    return wallet.getWatchedBalance();
	 }
	 
	 //set a balance on NodeInfo
	 public void setBalance(Address _address, BigInteger amount) {
	    NodeInfo.get(_address).setBalance(amount);
	 }
	 
	 // create a wallet
	 public void createWallet(){

	    Wallet wallet = null;
	    final File walletFile = new File("wallet.dat");
	    	         
	    	try {
	    			wallet = new Wallet(netParams);    	             
	    	        // 5 times
	    	        for (int i = 0; i < 5; i++) {          
	    	             // create a key and add it to the wallet
                        ECKey lekey = new ECKey();
	    	             wallet.addKey(lekey);
                        System.out.println("lekey"+lekey.toAddress(netParams).toString());

	    	        }
                System.out.println(wallet.toString());
	    	             // save wallet contents to disk
	    	         wallet.saveToFile(walletFile);
	    	             
	    	     } catch (IOException e) {
	    	    	 	System.out.println("Unable to create wallet file.");
	    	     }
	        
	 }
	 
	 // load a Bitcoin wallet
	 public void loadWallet(String filename) throws BlockStoreException, UnknownHostException {
			 // wallet file that contains Bitcoins we can send
	         walletFile = new File(filename);	
		     // load wallet from file
		     try {
				wallet = Wallet.loadFromFile(walletFile);

                 /*final PeerGroup peerGroup =
                         new PeerGroup(blockStore, netParams, chain);
                 peerGroup.setUserAgent("MyApp", "1.2");
                 peerGroup.addWallet(wallet);
                 peerGroup.addAddress(
                         new PeerAddress(InetAddress.getLocalHost()));
                 peerGroup.startAsync();*/

                 BlockChain chain = new BlockChain(netParams, wallet,blockStore);
                 PeerGroup peerGroup = new PeerGroup(netParams, chain);
                 peerGroup.addWallet(wallet);
                 peerGroup.startAsync();
                 //peerGroup.;
                 System.out.println("You have : " + wallet.getWatchedBalance() + " bitcoins" + wallet.toString());
                 System.exit(0);

                 System.out.println(wallet.toString());
			 } catch (UnreadableWalletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		           		
	 }
	 
	 //total Node's Bitcoin wallet balance
	 public BigInteger getTotalNodeBalance() {

	        BigInteger sum = BigInteger.ZERO;
	        
	        for (Account account : NodeInfo.values()) {
	            sum = sum.add(account.getBalance());
	        }
	        System.out.println(sum);
	        return sum;
	    }
	 
	 //build transaction
	 public void buildTransaction(Transaction transaction, String passwordDigest, String amountToSend, String recipient) throws BlockStoreException, AddressFormatException, InsufficientMoneyException, IOException{

		    // initialize BlockChain object
		    chain = new BlockChain(netParams, wallet, blockStore);
		
		    // instantiate Peer object to handle connections
		    // final Peer peer = new Peer(netParams, null, new PeerAddress(InetAddress.getLocalHost()), chain, null);
		    PeerGroup peerGroup = new PeerGroup(netParams, chain); // PeerGroup.setFastCatchupTime
		    peerGroup.addWallet(wallet);
		    peerGroup.startAndWait();
		             
		    // recipient address provided by official Bitcoin client
		    Address recipientAddress = new Address(netParams, recipient);
             Wallet.SendRequest req = Wallet.SendRequest.to(a, Coin.parseCoin(amountToSend));
             req.aesKey = wallet.getKeyCrypter().deriveKey("passwordDigest");
             wallet.sendCoins(req);

		   // save wallet with new transaction(s)
		   wallet.saveToFile(walletFile);
		 
	 }

	 
	 //serialize a transaction
	 public ECKey.ECDSASignature  serializeMultisigTransaction (ECKey key, Transaction contracts ){
			// Assume we get the multisig transaction we're trying to spend from 
			// somewhere, like a network connection.
			ECKey serverKey = key ;
			Transaction contract = contracts;
			Address clientKey = null;
			
			TransactionOutput multisigOutput = contract.getOutput(0);
			Script multisigScript = multisigOutput.getScriptPubKey();
			// Is the output what we expect?
			//checkState(multisigScript.isSentToMultiSig());
			//BigInteger value = multisigOutput.getValue();
	
			// OK, now build a transaction that spends the money back to the client.
			Transaction spendTx = new Transaction(netParams);
	
			//spendTx.addOutput(value, clientKey);
			spendTx.addInput(multisigOutput);
	
			// It's of the right form. But the wallet can't sign it. So, we have to
			// do it ourselves.
			Sha256Hash sighash = spendTx.hashForSignature(0, multisigScript, Transaction.SigHash.ALL, false);
			ECKey.ECDSASignature signature = serverKey.sign(sighash);
			// We have calculated a valid signature, so send it back to the client:
			return signature;
		
	 }
	 /* deserialize transactions*/
    public void deserializeMultisigTransaction(Transaction transaction) throws TransactionRequiredException{




    }
	   
	 // create a 2 of 3 p2sh account
	 public Address createMultisigAccount(ECKey clientKey, ECKey clientKey2) throws BlockStoreException, InsufficientMoneyException, InterruptedException, ExecutionException, AddressFormatException {
		 // initialize BlockChain object
		 chain = new BlockChain(netParams, wallet, blockStore);
		 PeerGroup peerGroup = new PeerGroup(netParams, chain);
		 //The Node's Key for This Account
		 ECKey serverKey = new ECKey();

		 // Prepare a template for the contract.
		 Transaction contract = new Transaction(netParams);
		 List<ECKey> keys = ImmutableList.of(clientKey, clientKey2, serverKey);

		 /* Create a 2 of 3 MULTISIG redeem Script  */
		 Script redeemScript = ScriptBuilder.createMultiSigOutputScript(3, keys);
         /* Create a p2sh output script from the redeemScript */
         Script multisig = ScriptBuilder.createP2SHOutputScript(redeemScript);
         //format to address
         Address   multisigAddress = Address.fromP2SHScript(netParams, multisig);
         //return multisigAddress
         System.out.print(multisigAddress.toString());
         return multisigAddress;
		 
	 }

    public void encryptWallet(String passwordDigest) {
        this.wallet.encrypt(passwordDigest);
    }

    public void decryptWallet(String passwordDigest) {
        this.wallet.decrypt(passwordDigest);
    }
	 
	 //n lock time implementation
	 public void refund(){
		 
	 }

    
}