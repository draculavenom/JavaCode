package com.threads.producerConsumer;

import java.util.concurrent.BlockingQueue;

import com.utilities.PrintingUtility;

public class Consumer implements Runnable, PrintingUtility{
	BlockingQueue<Integer> questionQueue;
	
	public Consumer(BlockingQueue<Integer> questionQueue) {
		this.questionQueue = questionQueue;
	}

	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(100);
				println("Answered Question: " + questionQueue.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
