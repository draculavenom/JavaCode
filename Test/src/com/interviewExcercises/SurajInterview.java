package com.interviewExcercises;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SurajInterview {
	
	//one string multiple words, you want the words that doesn't have any vowel
	public List<String> firstQuestion(String input){
		List<String> answer = new ArrayList();
		//input = input.toLowerCase();
		String[] inputArray = input.split(" ");
		for(int i = 0; i< inputArray.length; i++) {
			if(inputArray[i].contains("a") || inputArray[i].contains("e") || inputArray[i].contains("i") || inputArray[i].contains("o") || inputArray[i].contains("u")) {
				continue;
			}
			answer.add(inputArray[i]);
		}
		answer = answer.stream().sorted().unordered().collect(Collectors.toList());
		return answer;
	}
}


/*
 deep knowledge in java 8
 about the patterns
 angular in general (javascript)
 */
 