package com.interviewExcercises;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class MapString {
	
	static String name = "Juan Manuel Francisco Antunez Armella";
	/*
	 Given a string with your full name, you should get the map with the values of each character and the number of times it repeats 
	 */
	public void optim() {
		Map<Character, Integer> chars = new HashMap<Character, Integer>();//HashMap won't keep any sorting, but it's the optim.
		name.chars().forEach(x -> {
			Character c = Character.valueOf((char) x);
			if(chars.get(c) != null)
				chars.put(c, chars.get(c).intValue()+1);
			else
				chars.put(c, 1);
			});
		System.out.println(chars);
	}

	public void naturalSort() {
		Map<Character, Integer> chars = new TreeMap<Character, Integer>();//TreeMap will keep natural order.
		name.chars().forEach(x -> {
			Character c = Character.valueOf((char) x);
			if(chars.get(c) != null)
				chars.put(c, chars.get(c).intValue()+1);
			else
				chars.put(c, 1);
			});
		System.out.println(chars);
	}

	public void insertionSort() {
		Map<Character, Integer> chars = new LinkedHashMap<Character, Integer>();//LinkedHashMap will keep insertion order.
		name.chars().forEach(x -> {
			Character c = Character.valueOf((char) x);
			if(chars.get(c) != null)
				chars.put(c, chars.get(c).intValue()+1);
			else
				chars.put(c, 1);
			});
		System.out.println(chars);
	}
}
