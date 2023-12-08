package com.threads;

import java.util.Random;

import com.entities.PeopleAccount;

public class TestingThreadsWithAccounts extends Thread{
	PeopleAccount peopleAccount;
	int max, min;
	
	public TestingThreadsWithAccounts(PeopleAccount peopleAccount) {
		this.peopleAccount = peopleAccount;
		max = 500;
		min = 100;
	}
	
	public void run() {
		Random rand = new Random();
		//int num = rand.nextInt(max - min) + min;
		boolean increaseAmount = rand.nextBoolean();
		synchronized(this) {
			String info = "" + peopleAccount.getName() + " :";
			if(increaseAmount) {
				int num = rand.nextInt(max - min) + min;
				info += "INCREMENT: current balance: " + peopleAccount.getAmount() + " and the increase is: " + num;
				peopleAccount.incrementBalance(num);
				info += " --> SUCCESS";
			}else {
				int num = rand.nextInt(max - min) + 1;
				info += "DECREMENT: current balance: " + peopleAccount.getAmount() + " and the decrease is: " + num;
				if(peopleAccount.decrementBalance(num))
					info += " --> SUCCESS";
				else
					info += " --> Operation not possible";
			}
			info += " --> Final balance: " + peopleAccount.getAmount();
			println(info);
		}
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
