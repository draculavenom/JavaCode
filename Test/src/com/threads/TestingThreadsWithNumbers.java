package com.threads;

public class TestingThreadsWithNumbers extends Thread{
	static int number;
	
	public void run() {
		if(number == 0)
			println("number++;");
		number++;
	}
	
	public static int getNumber() {
		return number;
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
