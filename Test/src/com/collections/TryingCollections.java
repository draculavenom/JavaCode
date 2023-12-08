package com.collections;

import java.util.*;

public class TryingCollections {
	private ArrayList<String> array;
	private LinkedList<String> list;
	private HashMap<String, String> map;
	private HashSet<String> set;

	TryingCollections(){
		array = new ArrayList<String>();
		list = new LinkedList<String>();
		map = new HashMap<String, String>();
		set = new HashSet<String>();
	}

	public ArrayList<String> getArray() {
		return array;
	}

	public LinkedList<String> getList() {
		return list;
	}

	public HashMap<String, String> getMap() {
		return map;
	}

	public HashSet<String> getSet() {
		return set;
	}

	public void adding(Collection<String> c, String value) {
		if(c instanceof ArrayList) {
			((ArrayList<String>)c).add(value);
		}else if(c instanceof LinkedList) {
			((LinkedList<String>)c).add(value);
		}else if(c instanceof HashMap) {
			((HashMap<String, String>)c).put(value, value);
		}else if(c instanceof HashSet) {
			((HashSet<String>)c).add(value);
		}else {
			System.out.println("The collection was not recognized at all.");
		}
	}

	public void adding(Collection<String> c, String key, String value) {
		if(c instanceof HashMap) {
			((HashMap<String, String>)c).put(key, value);
		}else{
			System.out.println("The collection was not recognized at all.");
		}
	}

	public void deletingByValue(Collection<String> c, String value) {
		if(c instanceof ArrayList) {
			((ArrayList<String>)c).remove(value);
		}else if(c instanceof LinkedList) {
			((LinkedList<String>)c).remove(value);
		}else if(c instanceof HashMap) {
			((HashMap<String, String>)c).remove(value);
		}else if(c instanceof HashSet) {
			((HashSet<String>)c).remove(value);
		}else {
			System.out.println("The collection was not recognized at all.");
		}
	}

	public void deletingByIndex(Collection<String> c, int index) {
		if(c instanceof ArrayList) {
			((ArrayList<String>)c).remove(index);
		}else if(c instanceof LinkedList) {
			((LinkedList<String>)c).remove(index);
		}else if(c instanceof HashMap) {
			System.out.println("HashMap doesn't have a method to delete using an index, "
					+ "you have to have the key or the pair of key/value to delete the record");
		}else if(c instanceof HashSet) {
			System.out.println("HashSet doesn't have a method to delete using an index, "
					+ "you have to use the value directly, as set doesn't allow order there is no "
					+ "index to be used");
		}else {
			System.out.println("The collection was not recognized at all.");
		}
	}
}
