package com.java8;

import com.entities.Human;
import com.entities.Robot;
import com.entities.Walkable;
import com.utilities.PrintingUtility;

public class TestingLambdas implements PrintingUtility{
	public void test1() {
		p("Human tom = new Human();\r\n"
				+ "		walker(tom);\r\n"
				+ "		\r\n"
				+ "		Robot wale = new Robot();\r\n"
				+ "		walker(wale);\r\n"
				+ "		\r\n"
				+ "		walker(() -> println(\"Custom object walking...\"));\r\n"
				+ "		\r\n"
				+ "		Walkable walkable = () -> println(\"Custom object walking...\");\r\n"
				+ "		\r\n"
				+ "		walker(walkable);");
		Human tom = new Human();
		walker(tom);
		
		Robot wale = new Robot();
		walker(wale);
		
		walker(() -> println("Custom object walking..."));
		
		Walkable walkable = () -> println("Custom object walking...");
		
		walker(walkable);
	}
	
	public void test2() {
		
	}
	
	public void walker(Walkable walkableEntity) {
		walkableEntity.walk();
	}
}
