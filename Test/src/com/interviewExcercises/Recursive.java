package com.interviewExcercises;

import java.util.Comparator;
import java.util.stream.Collectors;

public class Recursive {
	int count;
	public Recursive() {
		count = 0;
	}
	/*given a number of 4 digits, get each number and sort the numbers ascending and descending, then rest the ascending to the descending
	with the new number do the same until the number is exactly the same as before. and tell how many times it took.
	*/
	public void solution(int input) {
		reduce(input, 0);
	}
	
	public void reduce(int input, int previousInput) {
		if(input == previousInput) {
			System.out.println("the number is: " + input + " and it took: " + count + " times.");
		}else if(count == 100){
			System.out.println("I don't think the number was found");
		}else{
			previousInput = input;
			input = sortNumber(input, false) - sortNumber(input, true);
			count++;
			System.out.println(input);
			reduce(input, previousInput);
		}
	}
	
	public int sortNumber(int input, boolean asc) {
		if(asc) {
			input = String.valueOf(input).chars().sorted().map(n -> Character.getNumericValue(n)).reduce((n1, n2) -> n1 = Integer.parseInt(String.valueOf(n1) + String.valueOf(n2))).getAsInt();
			System.out.print(input + " = ");
		}else {
			input = String.valueOf(input).chars().sorted().map(n -> Character.getNumericValue(n)).reduce((n1, n2) -> n2 = Integer.parseInt(String.valueOf(n2) + String.valueOf(n1))).getAsInt();
			System.out.print(input + " - ");
		}
		return input;
	}
}
