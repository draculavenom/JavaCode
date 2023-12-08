package com.interviewExcercises;

import java.util.ArrayList;
import java.util.List;

public class Venkatainterview {
	/*
	Sample 1:
	InputarrList:{"Red","Blue","Red","Red","Red","Yellow","Yellow","Blue","Blue","White","Whit"}
	 
	Output:
	 
	arrList1:{ "Red","Blue","Yellow","White","Whit"}
	 
	arrList2:{"Red","Red","Red,"Yellow","Blue","Blue"}
	*/
	
	public void test1(List<String> input){
		System.out.println(input);
		List<String> l1 = new ArrayList<String>();
		List<String> l2 = new ArrayList<String>();
		for(String it : input) {
			if(l1.size() == 0) {
				l1.add(it);
			}else {
				boolean flag = true;
				for(String e : l1) {
					if(it.equals(e)) {
						l2.add(it);
						flag = false;
						break;
					}
				}
				if(flag) {
					l1.add(it);
				}
			}
		}
		System.out.println(l1);
		System.out.println(l2);
	}
}
