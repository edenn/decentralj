package com.decentralbank.decentralj.core.test;

import java.math.BigInteger;
import java.util.Hashtable;

import org.junit.Assert;
import org.junit.Test;
//import org.hamcrest.CoreMatchers;

import org.bitcoinj.core.*;
import com.decentralbank.decentralj.core.Node;
import com.decentralbank.decentralj.net.DecentralPeer;

public class NodeTest {

	@Test
	public void getInstance() {
		Node n1 = Node.getInstance();
		Node n2 = Node.getInstance();
		Assert.assertNotNull(n1);
		Assert.assertSame(n1, n2);
	}

	@Test
	public void getID() {
		Assert.assertNotNull(Node.getInstance().getID());
	}
	
	@Test
	public void setID() {
		Node.getInstance().setID("hello");
		Assert.assertEquals(Node.getInstance().getID(), "hello");
	}
	
	@Test
	public void getPort() {
		Assert.assertNotNull(Node.getInstance().getPort());
	}
	
	@Test
	public void setPort() {
		Node.getInstance().setPort("will it fail?");
		Assert.assertEquals(Node.getInstance().getPort(), "will it fail?");
	}
	
	@Test
	public void getPoolId() {
		Assert.assertNotNull(Node.getInstance().getPoolId());
	}
	
	@Test
	public void setPoolId() {
		Node.getInstance().setPoolId("will it fail?");
		Assert.assertEquals(Node.getInstance().getPoolId(), "will it fail?");
	}

	@Test
	public void getPoolMembers() {
		Assert.assertNotNull(Node.getInstance().getPoolMembers());
	}
	
	@Test
	public void setPoolMembers() {
		String poolMembers [] = {"hello", "hi"};
		Node.getInstance().setPoolMembers(poolMembers);
		String nodePoolMembers []= Node.getInstance().getPoolMembers();
		Assert.assertArrayEquals(nodePoolMembers, poolMembers);
		Assert.assertNotSame(nodePoolMembers, poolMembers);
	}
	
	@Test
	public void getNextPeerID() {
		Assert.assertNotNull(Node.getInstance().getNextPeerID());
	}
	
	@Test
	public void setNextPeerID() {
		Node.getInstance().setNextPeerID("will it fail?");
		Assert.assertEquals(Node.getInstance().getNextPeerID(), "will it fail?");
	}
	
	@Test
	public void getHostname() {
		Assert.assertNotNull(Node.getInstance().getHostname());
	}
	
	@Test
	public void setHostname() {
		Node.getInstance().setHostname("will it fail?");
		Assert.assertEquals(Node.getInstance().getHostname(), "will it fail?");
	}
	
	@Test
	public void getNextPeerHostName() {
		Assert.assertNotNull(Node.getInstance().getNextPeerHostName());
	}
	
	@Test
	public void setNextPeerHostName() {
		Node.getInstance().setNextPeerHostName("will it fail?");
		Assert.assertEquals(Node.getInstance().getNextPeerHostName(), "will it fail?");
	}
	
	@Test
	public void getNextPeerPort() {
		Assert.assertNotNull(Node.getInstance().getNextPeerPort());
	}
	
	@Test
	public void setNextPeerPort() {
		Node.getInstance().setNextPeerPort("will it fail?");
		Assert.assertEquals(Node.getInstance().getNextPeerPort(), "will it fail?");
	}
	
	@Test
	public void getRedirectHostName() {
		Assert.assertNotNull(Node.getInstance().getRedirectHostName());
	}
	
	@Test
	public void setRedirectHostName() {
		Node.getInstance().setRedirectHostName("will it fail?");
		Assert.assertEquals(Node.getInstance().getRedirectHostName(), "will it fail?");
	}
	
	@Test
	public void getRedirectPort() {
		Assert.assertNotNull(Node.getInstance().getRedirectPort());
	}
	
	@Test
	public void setRedirectPort() {
		Node.getInstance().setRedirectPort("will it fail?");
		Assert.assertEquals(Node.getInstance().getRedirectPort(), "will it fail?");
	}
/*
	@Test
	public void getHashtable() {
		Assert.assertNotNull(Node.getInstance().getHashtable());
	}

	@Test
	public void setHashtable() {
		Hashtable<Integer, DecentralPeer> table= new Hashtable<>();
		Node.getInstance().setHashtable(table);
		Assert.assertSame(Node.getInstance().getHashtable(), table);
	}
*/

}
