package com.decentralbank.decentralj.core;

import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.Transaction;

public class NodeFee {

    private static String userAddress;
    private static String BuyAddress;
    private static String SellAddress;

    private final NetworkParameters params;

    public NodeFee(NetworkParameters params) {
        this.params = params;
    }

    public Address getAddressForEscrowFee(String userAddress) {
        try {
            return new Address(params, userAddress);
        } catch (AddressFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Address getAddressForBuyOfferFee(String BuyAddress) {
        try {
            return new Address(params, BuyAddress);
        } catch (AddressFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Address getAddressSellOfferFee(String SellAddress) {
        try {
            return new Address(params, SellAddress);
        } catch (AddressFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}