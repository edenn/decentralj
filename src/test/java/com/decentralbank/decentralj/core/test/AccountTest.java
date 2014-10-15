package com.decentralbank.decentralj.core.test;

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;

import org.bitcoinj.core.*;
import com.decentralbank.decentralj.core.Account;

public class AccountTest {

	@Test
	public void AccountConstructor() {
		Account ac = new Account();
		Assert.assertNotNull("Account should not be null", ac);
		Assert.assertEquals("nounce should be initialized to zero",
				ac.getNonce(), BigInteger.ZERO);
		Assert.assertEquals("balance should be initialized to zero",
				ac.getBalance(), BigInteger.ZERO);
	}

	@Test
	public void AccountConstructor_ECKey() {
		Account ac = new Account(new ECKey());
		Assert.assertNotNull("Account should not be null", ac);
		Assert.assertEquals("nounce should be initialized to zero",
				ac.getNonce(), BigInteger.ZERO);
		Assert.assertEquals("balance should be initialized to zero",
				ac.getBalance(), BigInteger.ZERO);
	}

	@Test
	public void AccountConstructor_ECKeyBigIntegerBigInteger() {
		Account ac = new Account(new ECKey(), BigInteger.ONE, BigInteger.ONE);
		Assert.assertNotNull("Account should not be null", ac);
		Assert.assertEquals("nounce should be initialized to set value",
				ac.getNonce(), BigInteger.ONE);
		Assert.assertEquals("balance should be initialized to set value",
				ac.getBalance(), BigInteger.ONE);
	}

	@Test
	public void getNounce() {
		Account ac = new Account(new ECKey(), BigInteger.ONE, BigInteger.ONE);
		Assert.assertEquals("nounce get should be equal to initialization",
				ac.getNonce(), BigInteger.ONE);
	}

	@Test
	public void getECKey() {
		ECKey key = new ECKey();
		Account ac = new Account(key, BigInteger.ONE, BigInteger.ONE);
		Assert.assertEquals("ECKey get should return same as initialized",
				ac.getECKey(), key);
	}

	@Test
	public void getAddress() {
		Account ac = new Account();
		Assert.assertNotNull("Address get works",
				ac.getAddress());
	}
	
	@Test
	public void setAddress() {
		Account ac = new Account();
		ac.setAddress(null);
		Assert.assertNull("should be set to null", ac.getAddress());
	}

	@Test
	public void getBalance() {
		Account ac = new Account(new ECKey(), BigInteger.ONE, BigInteger.ONE);
		Assert.assertEquals("balance get should be equal to initialization",
				ac.getBalance(), BigInteger.ONE);
	}
	
	@Test
	public void setBalance() {
		Account ac = new Account(new ECKey(), BigInteger.ONE, BigInteger.ONE);
		ac.setBalance(BigInteger.TEN);
		Assert.assertEquals("balance get should be equal to setBalance parameter",
				ac.getBalance(), BigInteger.TEN);
	}
	
}
