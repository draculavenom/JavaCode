package com.collections;

import java.util.ArrayList;
import java.util.HashMap;

public class TestingCollections {
	public void testArrayList() {
		ArrayList<String> array = new ArrayList<String>();
		p("ArrayList<String> array = new ArrayList<String>();");
		array.add("first element");
		println("array.add(\"first element\");");
		array.add("third element");
		println("array.add(\"third element\");");
		array.add(1, "second element");
		println("array.add(1, \"second element\");");
		p("array.forEach(i -> println(i));");
		array.forEach(i -> println(i));
		ArrayList<String> array2;
		p("ArrayList<String> array2;");
		array2 = (ArrayList<String>) array.clone();
		p("array2 = (ArrayList<String>) array.clone();");
		array.clear();
		p("array.clear();");
		p("array.forEach(i -> println(i));");
		array.forEach(i -> println(i));
		p("array2.forEach(i -> println(i));");
		array2.forEach(i -> println(i));
		p("println(array2.get(2));");
		println(array2.get(2));
		p("println(array2.indexOf(\"first element\"));");
		println(array2.indexOf("first element"));
		p("println(array.isEmpty());");
		println(array.isEmpty());
		p("array2.remove(2);");
		array2.remove(2);
		p("array2.remove(\"first element\");");
		array2.remove("first element");
		p("array2.forEach(i -> println(i));");
		array2.forEach(i -> println(i));
		p("array2.set(0, \"new element\");");
		array2.set(0, "new element");
		p("array2.add(\"something\");");
		array2.add("something");
		p("array2.replaceAll(i -> i + \" rocks\");");
		array2.replaceAll(i -> i + " rocks");
		p("array2.forEach(i -> println(i));");
		array2.forEach(i -> println(i));
		p("println(array2.size());");
		println(array2.size());
	}
	
	public void testHasMap() {
		HashMap<String, String> hm = new HashMap<String, String>();
		p("HashMap<String, String> hm = new HashMap<String, String>();");
		hm.put("1", "first");
		println("hm.put(\"1\", \"first\");");
		hm.put("2", "second");
		println("hm.put(\"2\", \"second\");");
		hm.put("5", "fifth");
		println("hm.put(\"5\", \"fifth\");");
		p("hm.forEach((k, v) -> println(k + \" -> \" + v));");
		hm.forEach((k, v) -> println(k + " -> " + v));
		HashMap<String, String> hm2;
		p("HashMap<String, String> hm2;");
		hm2 = (HashMap<String, String>) hm.clone();
		p("hm2 = (HashMap<String, String>) hm.clone();");
		hm.clear();
		p("hm.clear();");
		p("hm.forEach((k, v) -> println(k + \" -> \" + v));");
		hm.forEach((k, v) -> println(k + " -> " + v));
		p("hm2.forEach((k, v) -> println(k + \" -> \" + v));");
		hm2.forEach((k, v) -> println(k + " -> " + v));
		hm = hm2;
		p("hm = hm2;");
		hm.compute("2", (k, v) -> v.toUpperCase());
		p("hm.compute(\"2\", (k, v) -> v.toUpperCase());");
		p("hm.forEach((k, v) -> println(k + \" -> \" + v));");
		hm.forEach((k, v) -> println(k + " -> " + v));
		p("println(hm.containsKey(\"3\"));");
		println(hm.containsKey("3"));
		p("println(hm.containsKey(\"5\"));");
		println(hm.containsKey("5"));
		p("println(hm.containsValue(\"fifth\"));");
		println(hm.containsValue("fifth"));
		p("converting a map to a set");
		println("hm.entrySet().forEach(i -> println(i));");
		hm.entrySet().forEach(i -> println(i));
		p("println(hm.get(\"1\"));");
		println(hm.get("1"));
		p("println(hm.getOrDefault(\"3\", \"default value\"));");
		println(hm.getOrDefault("3", "default value"));
		p("println(hm.isEmpty());");
		println(hm.isEmpty());
		p("hm.keySet().forEach(i -> println(i));");
		hm.keySet().forEach(i -> println(i));
		hm.merge("3", "third", (k, v) -> v+" some");
		p("hm.merge(\"3\", \"third\", (k, v) -> v+\" some\");");
		p("hm.forEach((k, v) -> println(k + \" -> \" + v));");
		hm.forEach((k, v) -> println(k + " -> " + v));
		hm.remove("2");
		p("hm.remove(\"2\");");
		p("hm.forEach((k, v) -> println(k + \" -> \" + v));");
		hm.forEach((k, v) -> println(k + " -> " + v));
		hm.replace("3", "fourth");
		p("hm.replace(\"3\", \"fourth\");");
		p("hm.forEach((k, v) -> println(k + \" -> \" + v));");
		hm.forEach((k, v) -> println(k + " -> " + v));
		p("println(hm.size());");
		println(hm.size());
		p("println(hm.values());");
		println(hm.values());
	}
	
	private void println(Object o) {
		System.out.println(o);
	}
	
	private void p(String s) {
		System.out.println("\n" + s);
	}
}
