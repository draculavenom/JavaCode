package com.entities;

public class Sequence {
	private int value = 0;
	
	/*public int getNext() {//using synchronized statement
		synchronized(this) {
			value++;
			return value;
		}
	}*/
	
	public synchronized int getNext() {//using synchronized on the method
		value++;
		return value;
	}
}
