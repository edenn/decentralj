package com.decentralbank.decentralj.dht;

import com.decentralbank.decentralj.net.DecentralPeer;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.math.BigInteger;
import java.util.Comparator;

public class KeyComparator implements Comparator<DecentralPeer> {

    private final BigInteger key;

    public KeyComparator(KademliaId key)
    {
        this.key = key.getInt();
    }

    @Override
    public int compare(DecentralPeer n1, DecentralPeer n2)
    {
        BigInteger b1 = n1.getNodeId().getInt();
        BigInteger b2 = n2.getNodeId().getInt();

        b1 = b1.xor(key);
        b2 = b2.xor(key);

        return b1.abs().compareTo(b2.abs());
    }
}
