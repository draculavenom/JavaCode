package com.interviewExcercises;

import java.util.HashSet;

public class TestingFeatures {
	
	public void test1() {
		String s1 = "abc";
		String s2 = "abc";

		System.out.println("s1 == s2 is:" + s1 == s2);
	}
	
	public void test2() {
		String s3 = "JournalDev";
		int start = 1;
		char end = 5;

		System.out.println(s3.substring(start, end));
	}
	
	public void test3() {
		
		HashSet<Short> shortSet = new HashSet();

			for (short i = 0; i < 100; i++) {
		    shortSet.add(i);
		    shortSet.remove(i - 1);
		}
			
		System.out.println(shortSet.size());
	}
	
	public void test4() {

		String str = null;
		String str1="abc";

		System.out.println(str1.equals("abc") | str.equals(null));
	}
	
}

//ourn