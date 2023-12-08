package com.interviewExcercises;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChatGPTChallenges {
	/*
	'A' -> "1"
	'B' -> "2"
	...
	'Z' -> "26"
	
	To decode an encoded message, all the digits must be grouped then mapped back into letters using the reverse of the mapping above 
	(there may be multiple ways). For example, given the encoded message "12", it could be decoded as "AB" or "L". 
	However, for the encoded message "226", it must be decoded as "BFF", since "2" is mapped to "B," "2" is mapped to "F," 
	and "6" is mapped to "F."

	Write a function num_decodings(s: str) -> int that takes an encoded message and returns the total number of ways to decode it.
	 */
	public void num_decodings(String code) {
		Map map = getCodeMap();
//		List list = code.chars()
//				.forEach(e -> System.out.println(String.valueOf((char)e)));
				//.map(e -> String.valueOf((char)e))
				//.collect(Collectors.toList())
				;
		System.out.println("");
	}
	
	private Map<String, String> getCodeMap(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "A");
		map.put("2", "B");
		map.put("3", "C");
		map.put("4", "D");
		map.put("5", "E");
		map.put("6", "F");
		map.put("7", "G");
		map.put("8", "H");
		map.put("9", "I");
		map.put("10", "J");
		map.put("11", "K");
		map.put("12", "L");
		map.put("13", "M");
		map.put("14", "N");
		map.put("15", "O");
		map.put("16", "P");
		map.put("17", "Q");
		map.put("18", "R");
		map.put("19", "S");
		map.put("20", "T");
		map.put("21", "U");
		map.put("22", "V");
		map.put("23", "W");
		map.put("24", "X");
		map.put("25", "Y");
		map.put("26", "Z");
		return map;
	}
}
