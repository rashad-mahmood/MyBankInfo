package com.rashad.UI;

public class Transaction {

	private String name;
	private double amount;
	
	public Transaction(String name, double amount) {
		super();
		this.amount = amount;
		this.name = name;
	}

	
	public String getName() {
		return name;
	}


	public double getAmount() {
		return amount;
	}


	public void setName(String name) {
		this.name = name;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Transaction [name=" + name + ", amount=" + amount + "]";
	}
	
	
	
	
}
