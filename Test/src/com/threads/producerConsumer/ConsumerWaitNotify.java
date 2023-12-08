package com.threads.producerConsumer;

import java.util.List;

import com.utilities.PrintingUtility;

public class ConsumerWaitNotify implements Runnable, PrintingUtility{
	
	List<Integer> questionList = null;
	
	public ConsumerWaitNotify(List<Integer> questionList) {
		this.questionList = questionList;
	}
	
	public void answerQuestion() throws InterruptedException {
		synchronized(questionList) {
			while(questionList.isEmpty()) {
				println("No questions to answer.. waiting for producer to get questions");
				questionList.wait();
			}
		}
		synchronized(questionList) {
			Thread.sleep(500);
			println("Answered question: " + questionList.remove(0));
			questionList.notify();
		}
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				answerQuestion();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
