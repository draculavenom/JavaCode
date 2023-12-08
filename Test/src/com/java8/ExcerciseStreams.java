package com.java8;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.entities.sales.Customer;
import com.entities.sales.Order;
import com.entities.sales.Product;

public class ExcerciseStreams {
	
	//https://blog.devgenius.io/15-practical-exercises-help-you-master-java-stream-api-3f9c86b1cf82
	LinkedList<Customer> customerList;
	LinkedList<Product> productList;
	LinkedList<Order> orderList;
	
	public ExcerciseStreams() {
		dataToTest();
		excercises();
	}
	
	public void dataToTest() {
		customerList = new LinkedList<Customer>();
		productList = new LinkedList<Product>();
		orderList = new LinkedList<Order>();
		//Filling the products
		productList.add(new Product(1, "Dracula", "toy", 50));
		productList.add(new Product(2, "Venom", "toy", 60));
		productList.add(new Product(3, "Spiderman", "toy", 55));
		productList.add(new Product(4, "Morbius", "toy", 45));
		productList.add(new Product(5, "Lizard", "toy", 48));
		productList.add(new Product(6, "Ping pong ball", "sport", 3));
		productList.add(new Product(7, "Ping pong raquet", "sport", 14));
		productList.add(new Product(8, "Ping pong table", "sport", 800));
		productList.add(new Product(9, "Ping pong net", "sport", 10));
		productList.add(new Product(10, "Ping pong scorer", "sport", 120));
		productList.add(new Product(11, "t-shirt", "cloth", 20));
		productList.add(new Product(12, "socks", "cloth", 6));
		productList.add(new Product(13, "shorts", "cloth", 22));
		productList.add(new Product(14, "jeans", "cloth", 38));
		//Filling the customers
		customerList.add(new Customer(1, "Juan", 1));
		customerList.add(new Customer(2, "Manuel", 1));
		customerList.add(new Customer(3, "Francisco", 2));
		customerList.add(new Customer(4, "Roberto", 2));
		customerList.add(new Customer(5, "Alejandro", 3));
		customerList.add(new Customer(6, "Fernando", 3));
		//Filling the orders
		orderList.add(new Order(1, "active", LocalDate.of(2023, 3, 16), LocalDate.of(2023, 3, 16), productListGenerationById(new int[]{1,5,8}), customerList.get(0)));
		orderList.add(new Order(2, "active", LocalDate.of(2023, 2, 16), LocalDate.of(2023, 2, 16), productListGenerationById(new int[]{6,9,8,4,1}), customerList.get(0)));
		orderList.add(new Order(3, "cancelled", LocalDate.of(2023, 3, 15), LocalDate.of(2023, 3, 15), productListGenerationById(new int[]{14,14,14,14,13,12}), customerList.get(1)));
		orderList.add(new Order(4, "active", LocalDate.of(2023, 3, 15), LocalDate.of(2023, 3, 15), productListGenerationById(new int[]{10,9,8,7,6}), customerList.get(1)));
		orderList.add(new Order(5, "active", LocalDate.of(2023, 3, 15), LocalDate.of(2023, 3, 15), productListGenerationById(new int[]{2,5,8,11,14}), customerList.get(2)));
		orderList.add(new Order(6, "active", LocalDate.of(2023, 3, 16), LocalDate.of(2023, 3, 16), productListGenerationById(new int[]{3,5,7,1,9}), customerList.get(2)));
		orderList.add(new Order(7, "active", LocalDate.of(2023, 3, 18), LocalDate.of(2023, 3, 18), productListGenerationById(new int[]{12,12,12,13}), customerList.get(2)));
		orderList.add(new Order(8, "active", LocalDate.of(2023, 2, 13), LocalDate.of(2023, 2, 13), productListGenerationById(new int[]{10,11,12,13,14,10}), customerList.get(3)));
		orderList.add(new Order(9, "active", LocalDate.of(2023, 3, 13), LocalDate.of(2023, 3, 13), productListGenerationById(new int[]{9,8,4,1,2,4,5,6,3,7,8,5,4,1,2,5,6,3,9,8,4}), customerList.get(3)));
		orderList.add(new Order(10, "cancelled", LocalDate.of(2023, 3, 13), LocalDate.of(2023, 3, 13), productListGenerationById(new int[]{5,4,7,8,6,3,2}), customerList.get(4)));
		orderList.add(new Order(11, "active", LocalDate.of(2023, 3, 17), LocalDate.of(2023, 3, 18), productListGenerationById(new int[]{11,12,14,10,5,6,8,4,13,10,8}), customerList.get(5)));
		orderList.add(new Order(12, "cancelled", LocalDate.of(2023, 2, 16), LocalDate.of(2023, 2, 16), productListGenerationById(new int[]{8,4,5,6,3,9,10,11}), customerList.get(5)));
	}
	
	public void excercises() {
		println("Exercise 1 — Obtain a list of products belongs to category “sport” with price > 100");
		println("Exercise 2 — Obtain a list of order with products belong to category “toy”");
		println("Exercise 3 — Obtain a list of product with category = “toy” and then apply 10% discount");
		println("Exercise 4 — Obtain a list of products ordered by customer of tier 2 between 15-Mar-2023 and 16-Mar-2023");
		println("Exercise 5 — Get the cheapest products of “cloth” category");
		println("Exercise 6 — Get the 3 most recent placed order");
		println("Exercise 7 — Get a list of orders which were ordered on 15-Mar-2023, log the order records to the console and then return its product list");
		println("Exercise 8 — Calculate total of the products price sum of all orders placed in Feb 2023");
		println("Exercise 9 — Calculate order average payment placed on 13-Mar-2023");
		println("Exercise 10 — Obtain a collection of statistic figures (i.e. sum, average, max, min, count) for all products of category “sport”");
		println("Exercise 11 — Obtain a data map with order id and order’s product count");
		println("Exercise 12 — Produce a data map with order records grouped by customer");
		println("Exercise 13 — Produce a data map with order record and product total sum");
		println("Exercise 14 — Obtain a data map with list of product name by category");
		println("Exercise 15 — Get the most expensive product by category");
	}
	
	public void excercise1Solution() {
		println("\nexcercise 1 Solution:");
		productList.stream().filter(p -> p.getCategory() == "sport" && p.getPrice() > 100).forEach(System.out::println);
	}
	
	public void excercise2Solution() {
		println("\nexcercise 2 Solution:");
		orderList.stream().filter(o -> o.getProducts().stream().anyMatch(p -> p.getCategory() == "toy")).forEach(System.out::println);
	}
	
	public void excercise3Solution() {
		println("\nexcercise 3 Solution:");
		productList.stream().filter(p -> p.getCategory() == "toy").map(p -> {p.setPrice(p.getPrice() * 0.9); return p;}).forEach(System.out::println);
	}
	
	public void excercise4Solution() {
		println("\nexcercise 4 Solution:");
		orderList.stream().filter(o -> o.getCustomer().getTier() == 2 && Order.isBetween(o.getOrderDate(), LocalDate.of(2023, 3, 15), LocalDate.of(2023, 3, 16))).forEach(System.out::println);
	}
	
	public void excercise5Solution() {
		println("\nexcercise 5 Solution:");
		Optional<Product> val = productList.stream().reduce((val1, val2) -> val1.getPrice() < val2.getPrice() ? val1 : val2);
		val.ifPresent(System.out::println);
		
		//println(productList.stream().reduce((val1, val2) -> val1.getPrice() < val2.getPrice() ? val1 : val2).orElse(null));
	}
	
	public void excercise6Solution() {
		println("\nexcercise 6 Solution:");
		//orderList.stream().sorted((o1, o2) -> o1.getOrderDate().compareTo(o2.getOrderDate())).skip(orderList.size() - 3).forEach(System.out::println);
		orderList.stream().sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate())).limit(3).forEach(System.out::println);
	}
	
	public void excercise7Solution() {
		println("\nexcercise 7 Solution:");
//		orderList.stream().filter(o -> o.getOrderDate().isEqual(LocalDate.of(2023, 3, 15)))
//				.map(o -> {HashMap map = new HashMap(); map.put(o.getId(), o.getProducts()); return map;}).forEach(System.out::println);
		
		orderList.stream().filter(o -> o.getOrderDate().isEqual(LocalDate.of(2023, 3, 15)))
				.peek(o -> println(o))
				.flatMap(o -> o.getProducts().stream())
				.distinct()
				.forEach(System.out::println);
	}
	
	public void excercise8Solution() {
		println("\nexcercise 8 Solution:");
//		List<Product> prices = orderList.stream().filter(o -> Order.isBetween(o.getOrderDate(), LocalDate.of(2023, 2, 01), LocalDate.of(2023, 2, 28)))
//				.flatMap(o -> o.getProducts().stream())
//				.collect(Collectors.toList());
//		println("Sum: " + prices.stream().map(p -> p.getPrice()).reduce((double)0, (i1, i2) -> i1 + i2));
		
//		double sumValue = orderList.stream().filter(o -> Order.isBetween(o.getOrderDate(), LocalDate.of(2023, 2, 01), LocalDate.of(2023, 2, 28)))
//				.flatMap(o -> o.getProducts().stream())
//				.map(p -> p.getPrice())
//				.reduce((double)0, (i1, i2) -> i1 + i2);
//		println("Sum: " + sumValue);
		
		double sumValue = orderList.stream().filter(o -> Order.isBetween(o.getOrderDate(), LocalDate.of(2023, 2, 01), LocalDate.of(2023, 2, 28)))
				.flatMap(o -> o.getProducts().stream())
				.mapToDouble(p -> p.getPrice())
				.sum();
		println("Sum: " + sumValue);
		
	}
	
	public void excercise9Solution() {
		println("\nexcercise 9 Solution:");
//		double sumValue = orderList.stream().filter(o -> o.getOrderDate().isEqual(LocalDate.of(2023, 3, 13)))
//				.flatMap(o -> o.getProducts().stream())
//				.map(p -> p.getPrice())
//				.reduce((double)0, (i1, i2) -> i1 + i2);
//		int quantity = (int) orderList.stream().filter(o -> o.getOrderDate().isEqual(LocalDate.of(2023, 3, 13)))
//		.flatMap(o -> o.getProducts().stream()).count();
//		println("the average for the day 13-Mar-2023 was: " + (sumValue / quantity));
		
		double value = orderList.stream().filter(o -> o.getOrderDate().isEqual(LocalDate.of(2023, 3, 13)))
				.flatMap(o -> o.getProducts().stream())
				.mapToDouble(p -> p.getPrice())
				.average().getAsDouble();
		println("the average for the day 13-Mar-2023 was: " + value);
	}
	
	public void excercise10Solution() {
		println("\nexcercise 10 Solution:");
//		long count = productList.stream().filter(p -> p.getCategory() == "sport").count();
//		double max = productList.stream().filter(p -> p.getCategory() == "sport").map(p -> p.getPrice()).max(Double::compare).orElse((double) -1);
//		double min = productList.stream().filter(p -> p.getCategory() == "sport").map(p -> p.getPrice()).min(Double::compare).orElse((double) -1);
//		double sum = productList.stream().filter(p -> p.getCategory() == "sport").map(p -> p.getPrice()).reduce((double)0, (i1, i2) -> i1 + i2);
//		println("count: " + count + " " + "max: " + max + " "   + "min: " + min + " "   + "sum: " + sum + " " );
		
		DoubleSummaryStatistics statistics = productList.stream().filter(p -> p.getCategory() == "sport").mapToDouble(p -> p.getPrice()).summaryStatistics();
		println(String.format("count = %1$d, average = %2$f, max = %3$f, min = %4$f, sum = %5$f", 
			    statistics.getCount(), statistics.getAverage(), statistics.getMax(), statistics.getMin(), statistics.getSum()));
	}
	
	public void excercise11Solution() {
		println("\nexcercise 11 Solution:");
//		orderList.stream().map(o -> {
//				HashMap<Long, Long> map = new HashMap<Long, Long>();
//				map.put(o.getId(), o.getProducts().stream().count());
//				return map;
//			})
//			.forEach(System.out::println);
		
		Map<Object, Object> map = orderList.stream().collect(Collectors.toMap(
				order -> order.getId(),
				order -> order.getProducts().size()
				));
		println(map);
	}
	
	public void excercise12Solution() {
		println("\nexcercise 12 Solution:");
		Map<Customer, List<Order>> map = orderList.stream().collect(Collectors.groupingBy(Order::getCustomer));
		println(map);
	}
	
	public void excercise13Solution() {
		println("\nexcercise 13 Solution:");
		Map<Order, Double> map = orderList.stream().collect(
				Collectors.toMap(
						Function.identity(),
						order -> order.getProducts().stream().mapToDouble(p -> p.getPrice()).sum()
						)
				);
		println(map);
	}
	
	public void excercise14Solution() {
		println("\nexcercise 14 Solution:");
//		Map<String, List<Product>> map = productList.stream().collect(Collectors.groupingBy(Product::getCategory));
//		println(map);
		
		Map<String, List<Object>> map2 = productList.stream().collect(Collectors.groupingBy(
				Product::getCategory,
				Collectors.mapping(p -> p.getName(), Collectors.toList())
				));
		println(map2);
	}
	
	public void excercise15Solution() {
		println("\nexcercise 15 Solution:");
		Map<String, Optional<Product>> map = productList.stream().collect(
				Collectors.groupingBy(
						Product::getCategory,
						Collectors.maxBy(Comparator.comparing(Product::getPrice))
						)
				);
		
		println(map);
	}
	
	public LinkedList<Product> productListGenerationById(int[] numbers){
		LinkedList<Product> proList = new LinkedList<Product>();
		Arrays.stream(numbers).forEach(n -> proList.add(productList.stream().filter(pro -> pro.getId() == n).findFirst().orElse(null)));
		return proList;
	}
	
	private void println(Object arg) {
		System.out.println(arg);
	}
	
	private void print(Object arg) {
		System.out.print(arg);
	}
}
