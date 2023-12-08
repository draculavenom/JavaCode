package com.entities;

public class PeopleAccount {
	private String name;
	private double amount;
	
	public PeopleAccount() {}
	
	public PeopleAccount(String name, double amount) {
		super();
		this.name = name;
		this.amount = amount;
	}
	
	public PeopleAccount(String name) {
		super();
		this.name = name;
		this.amount = 0;
	}
	
	public void incrementBalance(double amount) {
		this.amount += amount;
	}
	
	public boolean decrementBalance(double amount) {
		if(this.amount - amount > 0) {
			this.amount -= amount;
			return true;
		}else {
			//print(" // This operation is not possible \\\\ ");
			return false;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	private void println(Object o) {
		System.out.println(o);
	}
	
	private void print(Object o) {
		System.out.print(o);
	}
	
	private void p(String s) {
		System.out.println("\n" + s);
	}
}
