package com.threads.producerConsumer;

import java.util.List;

import com.utilities.PrintingUtility;

public class ProducerWaitNotify implements Runnable, PrintingUtility{
	
	List<Integer> questionList = null;
	final int LIMIT = 5;
	private int questionNo;
	
	public ProducerWaitNotify(List<Integer> questionList) {
		this.questionList = questionList;
	}
	
	public void readQuestion(int questionNo) throws InterruptedException {
		synchronized(questionList) {
			while(questionList.size() == LIMIT) {
				println("Questions have piled uo.. wait for answers");
				questionList.wait();
			}
		}
		synchronized(questionList) {
			println("New Question: " + questionNo);
			questionList.add(questionNo);
			Thread.sleep(100);
			questionList.notify();
		}
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				readQuestion(questionNo++);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
