package com.java11;

import java.util.ArrayList;
import java.util.List;

public class AdvancedEncapsulation {
	public void explanation() {
		System.out.println("\n	AdvancedEncapsulation\r\n"
				+ "It is the ability to create immutable classes. In order to do this, you need to set any variable/method private.\r\n"
				+ "The constructor is private and you need a public method that uses the private constructor to create the instance of the object and return it.\r\n"
				+ "For the variable you will have public get methods but not set methods, once you create the object that object cannot be modified in any way.\r\n"
				+ "There is an specific case that if we have an attribute that is an object, we need to get rid of the reference, \r\n"
				+ "because if we store the reference, later on other part of the code, someone can update the value in the reference and \r\n"
				+ "that means that the original object was modified which breaks the immutable paradigm.\r\n"
				+ "");
		example();
	}
	
	public void example() {
		System.out.println("class Department{\r\n"
				+ "	private String name;\r\n"
				+ "	private int numEmployees;\r\n"
				+ "	private List<String> employees;\r\n"
				+ "	\r\n"
				+ "	private Department(String name, int num, List<String> employees) {\r\n"
				+ "		this.name = name;\r\n"
				+ "		this.numEmployees = num;\r\n"
				+ "		this.employees = new ArrayList<String>(employees);//to avoid references, we create new objects.\r\n"
				+ "	}\r\n"
				+ "	\r\n"
				+ "	public Department createDepartment(String name, int num, List<String> employees) {\r\n"
				+ "		return new Department(name, num, employees);\r\n"
				+ "	}\r\n"
				+ "	\r\n"
				+ "	public String getName() {return name;}\r\n"
				+ "	public int getNumEmployees() {return numEmployees;}\r\n"
				+ "	public List<String> getEmployees(){return new ArrayList<String>(employees);}//to avoid references, we create new objects.\r\n"
				+ "}\r\n");
		
		System.out.println("List<String> emp = new ArrayList<String>();\r\n"
				+ "emp.add(\"Juan\");\r\n"
				+ "emp.add(\"Manuel\");\r\n"
				+ "Department dep = Department.createDepartment(\"IT\", 2, emp);\r\n"
				+ "System.out.println(dep);\r\n"
				+ "\r\n"
				+ "String name = dep.getName();\r\n"
				+ "int num = dep.getNumEmployees();\r\n"
				+ "List empl = dep.getEmployees();\r\n"
				+ "name = \"No It\";\r\n"
				+ "num = 3;\r\n"
				+ "emp.add(\"Francisco\");//This doesn't update the list, because there is no reference to it\r\n"
				+ "\r\n"
				+ "System.out.println(dep);\r\n");
		List<String> emp = new ArrayList<String>();
		emp.add("Juan");
		emp.add("Manuel");
		Department dep = Department.createDepartment("IT", 2, emp);
		System.out.println(dep);
		
		String name = dep.getName();
		int num = dep.getNumEmployees();
		List empl = dep.getEmployees();
		name = "No It";
		num = 3;
		emp.add("Francisco");//This doesn't update the list, because there is no reference to it
		
		System.out.println(dep);
	}
}

class Department{
	private String name;
	private int numEmployees;
	private List<String> employees;
	
	private Department(String name, int num, List<String> employees) {
		this.name = name;
		this.numEmployees = num;
		this.employees = new ArrayList<String>(employees);//to avoid references, we create new objects.
	}
	
	public static Department createDepartment(String name, int num, List<String> employees) {
		return new Department(name, num, employees);
	}
	
	public String getName() {return name;}
	public int getNumEmployees() {return numEmployees;}
	public List<String> getEmployees(){return new ArrayList<String>(employees);}//to avoid references, we create new objects.
	
	public String toString() {
		return "Department{name= " + name + ", numEmployees= " + numEmployees + ", employees= " + employees + "}";
	}
}