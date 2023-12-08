package com.utilities;

public interface PrintingUtility {
	default void println(Object o) {
		System.out.println(o);
	}
	
	default void print(Object o) {
		System.out.print(o);
	}
	
	default void p(String s) {
		System.out.println("\n" + s);
	}
}
