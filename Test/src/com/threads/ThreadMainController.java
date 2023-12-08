package com.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import com.entities.PeopleAccount;
import com.entities.Sequence;
import com.threads.producerConsumer.Consumer;
import com.threads.producerConsumer.ConsumerWaitNotify;
import com.threads.producerConsumer.Producer;
import com.threads.producerConsumer.ProducerWaitNotify;
import com.utilities.PrintingUtility;

public class ThreadMainController implements PrintingUtility{
	public void test1() {
		TestingThreads testingThreads = new TestingThreads();
		testingThreads.start();
		p("TestingThreads testingThreads = new TestingThreads();\r\n"
				+ "		testingThreads.start();");
	}
	
	public void test2() {
		TestingRunnableThreads testingRunnableThreads = new TestingRunnableThreads();
		Thread thread1 = new Thread(testingRunnableThreads);
		thread1.start();
		p("TestingRunnableThreads testingRunnableThreads = new TestingRunnableThreads();\r\n"
				+ "		Thread thread1 = new Thread(testingRunnableThreads);\r\n"
				+ "		thread1.start();");
	}
	
	public void test3() {
		p("TestingThreadsWithNumbers testingThreadsWithNumbers = new TestingThreadsWithNumbers();\r\n"
				+ "		println(TestingThreadsWithNumbers.getNumber());\r\n"
				+ "		testingThreadsWithNumbers.start();\r\n"
				+ "		println(TestingThreadsWithNumbers.getNumber());");
		TestingThreadsWithNumbers testingThreadsWithNumbers = new TestingThreadsWithNumbers();
		println(TestingThreadsWithNumbers.getNumber());
		testingThreadsWithNumbers.start();
		println(TestingThreadsWithNumbers.getNumber());
		p("CONCLUSION: We can see that even we started the thread before printing the value for the second time, it's printed after, \n"
				+ "that's because the thread is started but the main thread of the main method is sitll running");
	}
	
	public void test4() {
		p("println(TestingThreadsWithNumbers.getNumber());\r\n"
				+ "		for(int i = 0; i< 100; i++) {\r\n"
				+ "			new TestingThreadsWithNumbers().start();\r\n"
				+ "		}\r\n"
				+ "		println(TestingThreadsWithNumbers.getNumber());");
		println(TestingThreadsWithNumbers.getNumber());
		for(int i = 0; i< 100; i++) {
			new TestingThreadsWithNumbers().start();
		}
		println(TestingThreadsWithNumbers.getNumber());
	}
	
	public void test5() {
		p("		PeopleAccount peopleAccount1 = new PeopleAccount(\"Dracula\");\r\n"
				+ "		PeopleAccount peopleAccount2 = new PeopleAccount(\"Venom\");\r\n"
				+ "		TestingThreadsWithAccounts t = new TestingThreadsWithAccounts(peopleAccount1);\r\n"
				+ "		\r\n"
				+ "		for(int i = 0; i < 100; i++) {\r\n"
				+ "			t = new TestingThreadsWithAccounts(peopleAccount1);\r\n"
				+ "			t.start();\r\n"
				+ "			t = new TestingThreadsWithAccounts(peopleAccount2);\r\n"
				+ "			t.start();\r\n"
				+ "		}\r\n"
				+ "		\r\n"
				+ "		try {\r\n"
				+ "			t.join();//This join helps to wait for the other threads to finish befre printing the last Final Balance, if \r\n"
				+ "			//we don't use it we can have it printed before the other threads finished, that means that the Final Balance \r\n"
				+ "			//won't be the real one. \r\n"
				+ "		} catch (InterruptedException e) {\r\n"
				+ "			e.printStackTrace();\r\n"
				+ "		}\r\n"
				+ "		\r\n"
				+ "		println(\"FINAL BALANCE FOR \" + peopleAccount1.getName() + \": \" + peopleAccount1.getAmount());\r\n"
				+ "		println(\"FINAL BALANCE FOR \" + peopleAccount2.getName() + \": \" + peopleAccount2.getAmount());");
		PeopleAccount peopleAccount1 = new PeopleAccount("Dracula");
		PeopleAccount peopleAccount2 = new PeopleAccount("Venom");
		TestingThreadsWithAccounts t = new TestingThreadsWithAccounts(peopleAccount1);
		
		for(int i = 0; i < 100; i++) {
			t = new TestingThreadsWithAccounts(peopleAccount1);
			t.start();
			t = new TestingThreadsWithAccounts(peopleAccount2);
			t.start();
		}
		
		try {
			t.join();//This join helps to wait for the other threads to finish befre printing the last Final Balance, if 
			//we don't use it we can have it printed before the other threads finished, that means that the Final Balance 
			//won't be the real one. 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		println("FINAL BALANCE FOR " + peopleAccount1.getName() + ": " + peopleAccount1.getAmount());
		println("FINAL BALANCE FOR " + peopleAccount2.getName() + ": " + peopleAccount2.getAmount());
	}
	
	public void test6() {
		p("\r\n"
				+ "		Sequence seq = new Sequence();\r\n"
				+ "		new Worker(seq).start();\r\n"
				+ "		new Worker(seq).start();");
		Sequence seq = new Sequence();
		new Worker(seq).start();
		new Worker(seq).start();
	}
	
	public void test7() {
		InventoryManager inventoryManager = new InventoryManager();
		Thread inventoryTask = new Thread(new Runnable() {
			@Override
			public void run() {
				inventoryManager.populateSoldProducts();
			}
		});
		Thread displayTask = new Thread(new Runnable() {
			@Override
			public void run() {
				inventoryManager.displaySoldProducts();
			}
		});
		inventoryTask.start();
		try {
			Thread.sleep(2000);
//			inventoryTask.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		displayTask.start();
	}
	
	public void test8() {
		p("List<Integer> questionList = new ArrayList<>();\r\n"
				+ "		Thread t1 = new Thread(new Producer(questionList));\r\n"
				+ "		Thread t2 = new Thread(new Consumer(questionList));\r\n"
				+ "		\r\n"
				+ "		t1.start();\r\n"
				+ "		t2.start();");
		List<Integer> questionList = new ArrayList<>();
		Thread t1 = new Thread(new ProducerWaitNotify(questionList));
		Thread t2 = new Thread(new ConsumerWaitNotify(questionList));
		
		t1.start();
		t2.start();
	}
	
	public void test9() {
		p("BlockingQueue<Integer> questionQueue = new ArrayBlockingQueue<Integer>(5);\r\n"
				+ "		Thread t1 = new Thread(new Producer(questionQueue));\r\n"
				+ "		Thread t2 = new Thread(new Consumer(questionQueue));\r\n"
				+ "		\r\n"
				+ "		t1.start();\r\n"
				+ "		t2.start();");
		BlockingQueue<Integer> questionQueue = new ArrayBlockingQueue<Integer>(5);
		Thread t1 = new Thread(new Producer(questionQueue));
		Thread t2 = new Thread(new Consumer(questionQueue));
		
		t1.start();
		t2.start();
	}
}
