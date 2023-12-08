package com.interviewExcercises;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Ivan {
	
	/*
	Given a number in the form of a list of digits, return all possible permutations.
	For example, given [1,2,3], return [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]].
	*/
	
	private List<List<Integer>> perm(List<Integer> input) {
		List<List<Integer>> output = new ArrayList<List<Integer>>();
		if(input.size() == 2) {
			List<Integer> newInput = new ArrayList<Integer>();
			newInput.addAll(input);
			output.add(newInput);
			output.add(switchListPlaces(input, 0, 1));
		}else {
			for(int i = 0; i<input.size(); i++) {
				List<Integer> newInput = new ArrayList<Integer>();
				for(int j = 1; j<input.size(); j++) {
					newInput.add(input.get(j));
				}
				List<List<Integer>> newList = perm(newInput);
				output.addAll(uniteValues(newList, input.get(0), i));
			}
		}
		return output;
	}
	
	private List<List<Integer>> uniteValues(List<List<Integer>> input, int val, int pos) {
		List<List<Integer>> newInput = new ArrayList<List<Integer>>();
		for(List<Integer> i : input) {
			List<Integer> newList = new ArrayList<Integer>();
			newList.addAll(i);
			//System.out.println(newList);
			newList.add(pos, val);
			//System.out.println(newList);
			newInput.add(newList);
		}
		return newInput;
	}
	
	private List<Integer> switchListPlaces(List<Integer> input, int p1, int p2) {
		List<Integer> newInput = new ArrayList<Integer>();
		newInput.addAll(input);
		Integer temp = newInput.get(p1);
		newInput.set(p1, newInput.get(p2));
		newInput.set(p2, temp);
		return newInput;
	}
	
	public void permutations(int[] input) {
		List<Integer> newInput = new ArrayList<Integer>();
		for(int i = 0; i<input.length; i++) {
			newInput.add(input[i]);
		}
		List<List<Integer>> output = perm(newInput);
		
		System.out.println(output);
	}
	
	/*
	resolver si es un palindrom, pero puedes tener hasta 3 letras que no coinciden.
	*/
	public void palindrom(String word) {
		boolean ans = true;
		for(int i = 0; i < word.length() / 2; i++) {
			if(word.charAt(i) != word.charAt(word.length() - 1 - i)) {
				ans = false;
			}
		}
		
		if(!ans) {
			ans = palindromUpToNErros(word, 3);//test for 3 erros
		}
		
		System.out.println("El resultado es " + ans);
	}
	
	public boolean palindromUpToNErros(String word, int n) {
		int breakingPoint = 0;
		List<String> spareChars = new ArrayList<String>();
		boolean ans = true;

		int ii = 0;
		int ei = 0;
		while(spareChars.size() <= n) {
			for(int i = 0; i < (word.length() - spareChars.size()) / 2; i++) {
				breakingPoint++;
				if (breakingPoint > 50)
					System.exit(0);
				System.out.println(ans);
				if(word.charAt(ii) == 'v') {
					System.out.println("ii: " + ii);
					System.out.println("i: " + i);
					System.out.println("word.charAt(ii): " + String.valueOf(word.charAt(ii)));
					System.out.println("word.charAt(i): " + String.valueOf(word.charAt(ei)));
				}
				if(ii > word.length() - 1 - ei) {
					break;
				}
				System.out.println(word.charAt(ii) + " != " + word.charAt(word.length() - 1 - ei) + ": " + String.valueOf(word.charAt(ii) != word.charAt(word.length() - 1 - ei)));
				if(word.charAt(ii) != word.charAt(word.length() - 1 - ei)) {
					ans = false;
					for(int j = 1; j<=n; j++) {
						System.out.println(word.charAt(ii + j) + " == " + word.charAt(word.length() - 1 - ei) + ": " + String.valueOf(word.charAt(ii + j) == word.charAt(word.length() - 1 - ei)));
						if(word.charAt(ii + j) == word.charAt(word.length() - 1 - ei)) {
							ans = true;
							System.out.println("word.charAt(ii + j) found " + word.charAt(ii + j));
							for(int k = 0; k < j; k++) {
								spareChars.add(String.valueOf(word.charAt(ii + k)));
							}
							ii += j;
							if(spareChars.size() > n) {
								System.out.println("----------------------------------------");
								System.out.println(spareChars.size());								
								ans = false;
							}
							break;
						}
					}
					for(int j = 1; j<=n; j++) {
						if(word.charAt(ii) == word.charAt(word.length() - 1 - ei - j)) {
							ans = true;
							System.out.println("word.charAt(ei + j) found " + word.charAt(word.length() - 1 - ei - j));
							for(int k = 0; k < j; k++) {
								spareChars.add(String.valueOf(word.charAt(word.length() - 1 - ei - k)));
							}
							ei += j;
							if(spareChars.size() > n) {
								System.out.println("----------------------------------------");
								System.out.println(spareChars.size());								
								ans = false;
							}
							break;
						}
					}
					if(spareChars.size() > n) {
						System.out.println("----------------------------------------");
						System.out.println(spareChars.size());
						ans = false;
						break;
					}
				}
				ii++;
				ei++;
			}
			if(ans)
				break;
		}
		
		System.out.println("Errors found: ");
		spareChars.stream().forEach(i -> System.out.print(i + ", "));
		System.out.println();
		return ans;
	}
	
	public void matrix5by5Problem(int size){
		int[][] matrix = randomMatrix(size);
		int smallest = 100;
		int largest = 0;
		double average = 0;
		int sum = 0;
		Map<Integer, Integer> repeated = new HashMap<Integer, Integer>();
		for(int i = 0; i<size;i++) {
			for(int j = 0; j<size;j++) {
				int value = matrix[i][j];
				if(smallest > value)
					smallest = value;
				if(largest < value)
					largest = value;
				sum += value;
				if(repeated.get(value) != null)
					repeated.put(value, repeated.get(value) + 1);
				else
					repeated.put(value, 1);
				//System.out.println(matrix[i][j] + ": " + repeated.get(matrix[i][j]));
			}
		}
		average = (double)sum / (size * size);
		int mostRepeated = (int) repeated.entrySet().stream()
				.reduce((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? entry1 : entry2)
				.get().getKey();
		System.out.println("a) the smallest number is: " + smallest);
		System.out.println("b) the largest number is: " + largest);
		System.out.println("c) the average number is: " + average);
		System.out.println("d) the most repeated number is: " + mostRepeated);
	}
	
	public int[][] randomMatrix(int size){
		int[][] ans = new int[size][size];
		for(int i = 0; i<size;i++) {
			for(int j = 0; j<size;j++) {
				ans[i][j] = (int) (Math.random() * 100) + 1;
				if(ans[i][j] < 10)
					System.out.print("00");
				else if(ans[i][j] < 100)
					System.out.print("0");
				System.out.print(ans[i][j] + " ");
			}
			System.out.println("");
		}
		return ans;
	}
	
	public void longestSubstringconsecutively(String input) {
		StringBuilder temp = new StringBuilder(input.substring(0,1));
		String ans = input.substring(0,1);
		for(int i = 1; i< input.length(); i++) {
			if(input.charAt(i-1) == input.charAt(i)) {
				temp.append(input.charAt(i));
			}else {
				temp = new StringBuilder(String.valueOf(input.charAt(i)));
			}
			if(ans.length() < temp.length()) {
				ans = temp.toString();
			}
		}
		System.out.println(ans);
	}
	
	public void binaryTreeProblem(int x, int y) {
		Map<Integer, String> tree = new HashMap<Integer, String>();
		if(x > 1 && x < 10 && y <= 99 && y >= 0) {
			tree.put(x, blankSpaces(x) + twoDigitNumber(y));
			binaryTreeSolution(x -1, y, tree);
			
			tree.entrySet()
			.stream()
			.sorted((entry, entry2) -> entry2.getKey()-entry.getKey())
			.forEach(entry -> {
				System.out.println(entry.getValue());
			});
		}else {
			System.out.println("x need to be greather than 1 and less than 10 also Y can be between 0 and 99");
		}
	}
	
	private void binaryTreeSolution(int x, int y, Map<Integer, String> tree) {
		if(x == 1) {
			int y1 =  generateRandomNumber(y);
			int y2 = y - y1;
			printRow(x, y1, y2, tree);
		}else {
			int y1 =  generateRandomNumber(y);
			int y2 = y - y1;
			printRow(x, y1, y2, tree);
			binaryTreeSolution(x - 1, y1, tree);
			binaryTreeSolution(x - 1, y2, tree);
		}
	}
	
	private void printRow(int x, int y1, int y2, Map<Integer, String> tree) {
		if(tree.get(x) == null) {
			tree.put(x, blankSpaces(x) + twoDigitNumber(y1) + blankSpaces(x));
			tree.put(x, tree.get(x) + blankSpaces(x) + twoDigitNumber(y2) + blankSpaces(x));
		}else {
			tree.put(x, tree.get(x) + blankSpaces(x)  + twoDigitNumber(y1) + blankSpaces(x));
			tree.put(x, tree.get(x) + blankSpaces(x) + twoDigitNumber(y2) + blankSpaces(x));
		}
	}
	
	private String twoDigitNumber(int y) {
		if(y >= 10)
			return String.valueOf(y);
		else
			return "0" + String.valueOf(y);
	}
	
	private String blankSpaces(int x) {
		String ans = "";
		int ins = (int) Math.pow(2, x) ;
		for(int j = 1; j<ins; j++)
			ans += " ";
		return ans;
	}
	
	private int generateRandomNumber(int y) {
		if(y == 0)
			return 0;
		return new Random().nextInt(y);
	}
	
	public void randomNumbersDropFirst(int[] input) {
		int len = input.length;
		ArrayList<Integer> output = new ArrayList<Integer>();
		boolean flag = true;
		for(int i = 1; i< len; i++) {
			System.out.print(input[i]);
			System.out.print(", ");
			if(i < len-1 && input[i-1] < input[i] && flag) {
				flag = false;
			}else
				output.add(input[i-1]);
		}
		System.out.println("");
		
		output.forEach(i -> System.out.print(i + ", "));
		System.out.println("");
	}
	
	public int[] generateRandomNumbers() {
		int[] output = new int[10];

        Random rand = new Random();
        for(int i  = 0; i < 10; i++) {
        	output[i] = rand.nextInt((20 - 0) + 1) + 0;        	
        }
		return output;
	}
	
	public void test() {
		int[] numbers = new int[10];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = (int) (Math.random() * 20) + 1;
        }

        // Print the original array
        System.out.println("Original Array:");
        for (int number : numbers) {
            System.out.print(number + " ");
        }
        System.out.println();

        // Find and remove the first number that is lower than its predecessor
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] < numbers[i - 1]) {
                for (int j = i; j < numbers.length - 1; j++) {
                    numbers[j] = numbers[j + 1];
                }
                numbers[numbers.length - 1] = 0; // Set the last element to 0
                break;
            }
        }

        // Print the new array
        System.out.println("New Array:");
        for (int number : numbers) {
            if (number != 0) {
                System.out.print(number + " ");
            }
        }
	}
}
