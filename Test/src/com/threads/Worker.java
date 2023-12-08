package com.threads;

import com.entities.Sequence;
import com.utilities.PrintingUtility;

public class Worker extends Thread implements PrintingUtility{
	Sequence Sequence = null;
	public Worker(Sequence sequence){
		this.Sequence = sequence;
	}
	
	public void run() {
		for(int i = 0; i<100; i++) {
			println(Thread.currentThread().getName() + " got value: " + Sequence.getNext());
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
