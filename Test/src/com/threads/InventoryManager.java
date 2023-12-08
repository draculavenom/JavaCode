package com.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import com.entities.Product;
import com.utilities.PrintingUtility;

public class InventoryManager implements PrintingUtility{
	List<Product> soldProductList = new CopyOnWriteArrayList<Product>();
	List<Product> purchasedProductList = new ArrayList<Product>();
	
	Vector<Product> vector = new Vector<>();
	
	public void populateSoldProducts() {
		for(int i = 0; i< 200; i++) {
			Product prod = new Product(i, "test_product_" + i);
			soldProductList.add(prod);
			println("Added: " + prod);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void displaySoldProducts() {
		for(Product product : soldProductList) {
			println("Printing the sold: " + product);
		}
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
