package com.decentralbank.decentralj.crypto;

import java.security.SignatureException;

import org.spongycastle.crypto.params.KeyParameter;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.core.Utils;
import com.google.bitcoin.crypto.KeyCrypterException;

public class Crypto {
	
	    public Crypto() {
	    }

	    public byte[] getEmbeddedAccountRegistrationData(ECKey key, String NodeAccounts) {
	        String signedNodes = signMessage(key, NodeAccounts, null);
	        return null;
	    }

	    //TODO implement signMEssage
	    private String signMessage(ECKey registrationKey,String NodeAccounts, Object object) {
			return null;
		}

		public String signContract(ECKey key, String contract) {
			
	    	return null;
	    }
	    
	    private String concatenateChunks(String nodeAccount, String nodeId) {
	        return nodeAccount + nodeId;
	    }
	    
	    public boolean verifyHash(String _hash, String msg, String sig) {
	        byte [] hash = createHash(msg, sig);
	        return hash.equals(_hash);
	    }

	    private byte[] createHash(String msg, String sig) {
	    	//check to see if it is returning Bytes
	        byte[] hashBytes = concatenateChunks(msg, sig).getBytes();
	        return Utils.sha256hash160(hashBytes);
	    }


	    public boolean verifySignature(byte[] pubKey, String msg, String sig) {
			
	    	
	    	return false;
	    }


	
	

}
