package com.decentralbank.decentralj.core.test;

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;

import com.google.bitcoin.core.*;
import com.decentralbank.decentralj.core.Request;

public class RequestTest {
	
	@Test
	public void Request() {
		Request r = new Request();
		Assert.assertNotNull(r);
		Assert.assertNotNull(r.getMessage());
		Assert.assertNotNull(r.getPeerID());
		Assert.assertNotNull(r.getStatus());
	}
	
	@Test
	public void Request_StringString() {
		Request r = new Request("","");
		Assert.assertNotNull(r);
		Assert.assertNotNull(r.getMessage());
		Assert.assertNotNull(r.getPeerID());
		Assert.assertNotNull(r.getStatus());
	}
	
	@Test
	public void Request_StringStringString() {
		Request r = new Request("","","");
		Assert.assertNotNull(r);
		Assert.assertNotNull(r.getMessage());
		Assert.assertNotNull(r.getPeerID());
		Assert.assertNotNull(r.getStatus());
	}
	
	@Test
	public void getStatus() {
		Request r = new Request();
		Assert.assertNotNull(r.getStatus());
	}
	
	@Test
	public void setStatus() {
		Request r = new Request();
		r.setStatus("a string");
		Assert.assertEquals(r.getStatus(), "a string");
	}
	
	@Test
	public void getPeerID() {
		Request r = new Request();
		Assert.assertNotNull(r.getPeerID());
	}

	@Test
	public void setPeerID() {
		Request r = new Request();
		r.setPeerID("a string");
		Assert.assertEquals(r.getPeerID(), "a string");
	}

	@Test
	public void getMessage() {
		Request r = new Request();
		Assert.assertNotNull(r.getMessage());
	}
	
	@Test
	public void setMessage() {
		Request r = new Request();
		r.setMessage("a string");
		Assert.assertEquals(r.getMessage(), "a string");
	}
	
	@Test
	public void accountToString() {
		Request r = new Request("a","b","c");
		Assert.assertEquals(r.toString(), "a b c");
	}
	
}
