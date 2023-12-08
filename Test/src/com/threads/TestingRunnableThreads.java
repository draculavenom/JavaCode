package com.threads;

public class TestingRunnableThreads implements Runnable{
	public void run() {
		p("public class TestingRunnableThreads implements Runnable{\r\n"
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
