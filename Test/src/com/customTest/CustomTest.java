package com.customTest;

import java.util.InputMismatchException;

public class CustomTest {
	public static void main(String[] args) {
		Vehicle v = new Car();
		v.start();
		v.check();
		v.isWorking();
		
		TestingWithStyle t = new TestingWithStyle();
		try{
			t.failingWithStyle();
		}catch(Exception e) {
			System.out.println(e);
			System.out.println(e.getCause());
		}
	}
}

class Vehicle{
	public void start() {
		System.out.println("Starting the engine");
	}
	
	public void check() {
		System.out.println("The vehicle is working as expected");
	}
	
	public void isWorking() throws InputMismatchException{
		System.out.println("It's working fine");
	}
}

class Car extends Vehicle{
	public void start() throws NullPointerException{
		System.out.println("Car runs fast");
	}
	
	public void check() {
		System.out.println("The car is working as expected");
	}
	
	@Override
	public void isWorking() throws InputMismatchException{
		System.out.println("It's working just fine");
	}
}

class TestingWithStyle{
	public void failingWithStyle() {
		NumberFormatException ex = new NumberFormatException("Number format is wrong");
		ex.initCause(new NullPointerException("There is no object to be referenced"));
		throw ex;
	}
}