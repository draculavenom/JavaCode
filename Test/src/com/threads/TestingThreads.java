package com.threads;

public class TestingThreads extends Thread{
	public void run() {
		p("public class TestingThreads extends Thread{\r\n"
				+ "	public void run() {\r\n"
				+ "		content\r\n"
				+ "	}");
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
