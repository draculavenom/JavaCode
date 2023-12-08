package com.java11;

import java.util.ArrayList;

public class LocalVariableTypeInference {
	public void explanation() {
		System.out.println("\n	LocalVariableTypeInference\n"
				+ "Java is a strongly-typed language i.e. you must specify the type of a variable when introducing it.\n"
				+ "However, since Java 10, we have the option, in certain scenarios, of using the keyword `var` instead of the type.\n"
				+ "In these situations, the compiler infers the type e.g. var x = \"abc\"; //x is a String\n"
				+ "Remember that the variables must be local and must be initialized on the line where they are declared.");
	}
	
	public void example() {
		System.out.println("var x = \"abc\";");
		var x = "abc";
		System.out.println("var list = new ArrayList<>();");
		var list = new ArrayList<>();
	}
}
