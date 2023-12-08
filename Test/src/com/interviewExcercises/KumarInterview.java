package com.interviewExcercises;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KumarInterview {
	public void question1() {
		List<Integer> integers = Arrays.asList(2, 4, 5, 19, 18, 40, 13, 6);
        //Find the 3rd 4th and 5th highest valued integer
		
		integers.stream()
		.sorted((x, y) -> x > y ? -1 : 1).limit(5)
		.sorted((x, y) -> x > y ? 1 : -1).limit(3)
		.sorted((x, y) -> x > y ? -1 : 1)
		//.findFirst().orElse(0)
		.forEach(x -> System.out.println(x));
	}
	
	public void question2() {
		Map<String, Integer> map = new HashMap<>();
	        map.put("Amar", 1200);
	        map.put("Mohan", 1240);
	        map.put("Milan", 1500);
	        map.put("Ajeet", 900);
	        map.put("Ajay", 1700);
	        map.put("Rahul", 1900);
	        map.put("MohanLal", 1600);
	        
	    //second highest salary employee
	    System.out.println(
		    map.entrySet().stream()
		    .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
		    .limit(2)
		    .sorted((entry1, entry2) -> entry1.getValue().compareTo(entry2.getValue()))
		    .findFirst().orElseGet(null)
	    )
	    ;
	}
}
