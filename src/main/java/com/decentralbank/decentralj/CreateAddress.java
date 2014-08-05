package com.decentralbank.decentralj;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.Address;

public class CreateAddress {
	

	public static void main(String[] args) throws Exception {
	            // use testnet
		        String net = "test";
		        
		        if (args.length >= 1 && (args[0].equals("test") || args[0].equals("prod"))) {
		            net = args[0];
		            System.out.println("Using " + net + " network.");
		        }
		        
		        // create a new EC Key ...
		        ECKey key = new ECKey();
		        
		        System.out.println("We created key:\n" + key);
		        
		        // either test or production net are possible
		        final NetworkParameters netParams;
	        
		        if (net.equals("prod")) {
		            netParams = NetworkParameters.prodNet();
		        } else {
		            netParams = NetworkParameters.testNet();
		        }
		        
		        // get valid Bitcoin address from public key
		        Address addressFromKey = key.toAddress(netParams);
		        
		        System.out.println("On the " + net + " network, we can use this address:\n" + addressFromKey);
		   }
	
	
	
	

}
