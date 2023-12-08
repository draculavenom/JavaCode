package com.interviewExcercises;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SubbuInterview {
	public void test1() {
		List<Integer> l = new ArrayList<Integer>();
		l.add(1);
		l.add(2);
		l.stream().map(e-> {
			System.out.println(e); 
			return e*2;
		} );
		System.out.println(l);
	}
	
	/*
	 write a program that receive a string and string contents multiple words
	 first letter in the word should not repeat in the same word, collect the words in a list and show the result. 
	 */
	public void test2 (String input) {
		/*
		subbu
		//armella
		 */
		List<String> si = Arrays.asList(input.split(" "));
		si = si.stream()
		.filter(s -> !s.substring(1).contains(String.valueOf(s.charAt(0))))
		.collect(Collectors.toList());
		System.out.println(si);
		//System.out.println(input.substring(1));
		
	}
}
