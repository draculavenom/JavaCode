package com.entities.sales;

import java.text.SimpleDateFormat;

public class Product {
	private long id;
	private String name;
	private String category;
	private double price;
	
	public Product() {}
	
	public Product(long id, String name, String category, double price) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.price = price;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String caterogy) {
		this.category = caterogy;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", category=" + category + ", price=" + price + "]";
	}
	
}
