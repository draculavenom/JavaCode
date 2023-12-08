package com.threads.producerConsumer;

import java.util.concurrent.BlockingQueue;

import com.utilities.PrintingUtility;

public class Producer implements Runnable, PrintingUtility{
	int questionNo;
	BlockingQueue<Integer> questionQueue;
	
	public Producer(BlockingQueue<Integer> questionQueue) {
		this.questionQueue = questionQueue;
	}

	@Override
	public void run() {
		while(true) {
			try {
				synchronized(this) {
					int nextQuestion = questionNo++;
					println("Got new question" + nextQuestion);
					questionQueue.put(nextQuestion);				
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
