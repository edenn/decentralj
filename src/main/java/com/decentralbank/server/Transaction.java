package com.decentralbank.server;

import java.sql.Timestamp;



public class Transaction {
	private String accountName;
	private String accountAddress;
	private Timestamp timeStamp;

	public String getAccountName() {

		return accountName;
	}

	public void setAccountName(String accountName) {

		this.accountName = accountName;
	}

	public String getAccountAddress() {

		return accountAddress;
	}

	public void setAccountAddress(String accountAddress) {

		this.accountAddress = accountAddress;
	}
	
	public Timestamp getTimeStamp() {

		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {

		this.timeStamp = timeStamp;
	}

}




