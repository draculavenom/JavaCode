package com.interviewExcercises;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class VeerChallenges {
	
	public void printHalfPyramid(int n, String letter) {
		for(int i = 0; i<n; i++) {
			for(int j = 0; j<= i; j++) {
				System.out.print(letter + " ");
			}
			System.out.println("");
		}
	}
	
	public void printHalfNumberPyramid(int n) {
		for(int i = 0; i<n; i++) {
			for(int j = 0; j<= i; j++) {
				System.out.print((j + 1) + " ");
			}
			System.out.println("");
		}
	}
	
	public void printReverseHalfNumberPyramid(int n) {
		for(int i = n; i>0; i--) {
			for(int j = 1; j<=i; j++) {
				System.out.print((j) + " ");
			}
			System.out.println("");
		}
	}
	
	public void test1(int n) {
		printHalfPyramid(n, "*");
	}
	
	public void test2(int n) {
		printHalfNumberPyramid(n);
	}
	
	public void test3(int n) {////Program to print Inverted half pyramid using numbers
		printReverseHalfNumberPyramid(n);
	}
	
	public void test4(String[] input) {
		List<List<String>> ans = new ArrayList<List<String>>();
		for(int i = 0; i<input.length; i++) {
			if(i == 0) {
				ans.add(new ArrayList<String>());
				ans.get(0).add(input[0]);
				continue;
			}
			final int ind = i;
			TempClass check = new TempClass() {
				boolean flag = true;
			};
			ans.stream().forEach(l -> {
				if(check.flag) {
					if(compareStrings(new StringBuilder(l.get(0).toLowerCase()), new StringBuilder(input[ind].toLowerCase()))) {
						l.add(input[ind]);
						updateCheckFlag(check);
					}
				}
			});
			if(check.flag) {
				ans.add(new ArrayList<String>());
				ans.get(ans.size()-1).add(input[i]);
			}
		}
		ans.stream().forEach(System.out::println);
	}
	
	public boolean compareStrings(StringBuilder i1, StringBuilder i2) {
		if(i1.length() != i2.length()) {
			return false;
		}
		for(int i = 0; i<i1.length(); ) {
			if(i2.toString().equals("acr"))
				System.out.println(i2.toString() + ": " + i1.charAt(0));
			if(i2.toString().contains(String.valueOf(i1.charAt(0)))) {
				i2.deleteCharAt(i2.indexOf(String.valueOf(i1.charAt(0))));
				i1.deleteCharAt(0);
			}else {
				return false;
			}
		}
		return true;
	}
	
	private void updateCheckFlag(TempClass check) {
		check.flag = false;
	}
	
	public void test4VeerSolution(String[] words) {
		List<List<String>> result = new ArrayList<>();
		 HashMap<String, List<String>> map = new HashMap<>();

		 for(String str: words){

			 char[] chArr = str.toLowerCase().toCharArray();
			 Arrays.sort(chArr);
			 String key = new String(chArr);

			 if(map.containsKey(key)) {
				 map.get(key).add(str);
			 }else {

				 List<String> strList = new ArrayList<>();
				 strList.add(str);
				 map.put(key, strList);
			 }

		 }

		 result.addAll(map.values());


		 for(List<String> list : result) {
			 System.out.println(list);
		 }
	}
}

class TempClass {
	boolean flag = true;
}

/*
 		String []words = {"Act","Pqr","CAT","rqp","tac", "qrp"};

		//Out put:
			//[Pqr, rqp, qrp] 
			//[Act, CAT, tac] 



		 List<List<String>> result = new ArrayList<>();
		 HashMap<String, List<String>> map = new HashMap<>();

		 for(String str: words){

			 char[] chArr = str.toCharArray();
			 Arrays.sort(chArr);
			 String key = new String(chArr).toLowerCase();

			 if(map.containsKey(key)) {
				 map.get(key).add(str);
			 }else {

				 List<String> strList = new ArrayList<>();
				 strList.add(str);
				 map.put(key, strList);
			 }

		 }

		 result.addAll(map.values());


		 for(List<String> list : result) {
			 System.out.println(list);
		 }

	}
 */
