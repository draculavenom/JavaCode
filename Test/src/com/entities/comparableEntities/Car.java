package com.entities.comparableEntities;

public class Car implements Comparable<Car>{
	private String model;
	private String make;
	private int price;
	
	public Car(String model, String make, int price) {
		super();
		this.model = model;
		this.make = make;
		this.price = price;
	}
	
	public Car() {}
	
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "Car [model=" + model + ", make=" + make + ", price=" + price + "]";
	}

	public int compareTo(Car o) {
		if(this.make.compareTo(o.make) < 0 || this.make.compareTo(o.make) > 0)
			return this.make.compareTo(o.make);
		else if(this.model.compareTo(o.model) < 0 || this.model.compareTo(o.model) > 0)
			return this.model.compareTo(o.model);
		else if(this.price < o.price)
			return -1;
		else if(this.price > o.price)
			return 1;
		return 0;
	}
	
}
