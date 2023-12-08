package com.challenges;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileChallenges {
	//Problem: Find the Most Common Words in a Text File
	/*Problem Description:
	You are given a text file containing a large amount of text. 
	Your task is to find and display the most common words in the text. 
	Common words are defined as words that occur frequently and are not considered stop words 
	(common words like "and," "the," "of," etc. are typically excluded).
	*/
	public void findMostCommonWords() {
        try {
			String text = new String(Files.readAllBytes(Paths.get("src","fileTest.txt")));
			
			Map<String, Integer> map = new HashMap<String, Integer>();
			List<String> ans = new ArrayList<String>();
			List<String> banned = new ArrayList<String>();
			banned.add("de");banned.add("y");banned.add("la");banned.add("que");banned.add("en");
			text = text.replace('\n', ' ');
			Temp max = new Temp();
			max.max = 0;
			Arrays.asList(text.split(" ")).stream().forEach(s -> {
				if(!banned.contains(s) && map.containsKey(s)) {
					int temp = map.get(s) + 1;
					map.put(s, temp);
					if(temp > max.max) {
						updateTemp(max, temp);
						ans.clear();
						ans.add(s);
					}else if(temp == max.max) {
						ans.add(s);
					}
				}else {
					map.put(s, 1);
				}
			});
			
			System.out.println(ans);
			//max = map.values().stream().max(Integer::compare).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
        System.out.println(Paths.get("fileTest.txt"));
        System.out.println("ChatGPT solution: ");
		CommonWordsFinder.main();
	}
	
	public void updateTemp(Temp max, int newValue) {
		max.max = newValue;
	}
}

class Temp{
	int max;
}

class CommonWordsFinder {
    public static void main() {
        try {
            // Read the text file and split it into words
            String text = new String(Files.readAllBytes(Paths.get("src","fileTest.txt")));
            List<String> words = Arrays.asList(text.split("\\s+"));

            // Define a list of stop words to exclude
            List<String> stopWords = Arrays.asList("the", "and", "of", "in", "to", "a", "for", "on");

            // Use Java 8 streams to process the words
            Map<String, Long> wordCounts = words.stream()
                    .map(word -> word.toLowerCase().replaceAll("[^a-zA-Z]", ""))
                    .filter(word -> !stopWords.contains(word))
                    .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

            // Find and display the most common words
            int topN = 10; // Change this value to display a different number of top words
            wordCounts.entrySet().stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                    .limit(topN)
                    .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
