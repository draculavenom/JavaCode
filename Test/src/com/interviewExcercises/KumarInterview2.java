package com.interviewExcercises;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class KumarInterview2 {
	/*
	Write a Java method  that takes a List of integers as input and returns frequency of each number.
 
	ex: if the input list is [1, 2, 2, 3, 3, 3, 4], the method should return  {1=1, 2=2, 3=3, 4=1}.
	*/
	
	public Map<Integer, Integer> test1(List<Integer> input){
		Map<Integer, Integer> ans = new HashMap<Integer, Integer>();
		input.stream().forEach(it -> {
			if(ans.get(it) != null)
				ans.put(it, ans.get(it) + 1);
			else
				ans.put(it, 1);
		});
		return ans;
	}
	
	/*
	Given an array of strings strs, group the anagrams together.
 
	Input: strs = ["eat","tea","tan","ate","nat","bat"] Output: [["bat"],["nat","tan"],["ate","eat","tea"]]
	*/
	public void test2(String[] strs) {
		//I can sort the items, once they are sorted I can check if they are the same as one already checked.
		List<List<String>> ans = new ArrayList<List<String>>();
		//Map with key with the string, then the value will be the list of strings, 
		for(int i= 0; i< strs.length; i++) {
			if(ans.size() == 0) {
				List<String> items = new ArrayList<String>();
				items.add(strs[i]);
				ans.add(items);
			}else {
				//StringBuilder sb1 = new StringBuilder(strs[i]);
				char[] c1 = strs[i].toCharArray();
				boolean flag = true;
				for(List<String> it : ans) {
					//StringBuilder sb2 = new StringBuilder(it.get(0));
					char[] c2 = it.get(0).toCharArray();
					Arrays.sort(c1);
					Arrays.sort(c2);
					System.out.println(c1 + " c1.equals(c2) " + c2 + " = " + c1.equals(c2));
					if(c1.equals(c2)) {
						it.add(strs[i]);
						flag = false;
					}
				}
				if(flag) {
					List<String> items = new ArrayList<String>();
					items.add(strs[i]);
					ans.add(items);
				}
			}
		}
		
		System.out.println(ans);
	}
}
