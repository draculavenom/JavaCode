package com.java8;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import com.entities.User;

public class TestingStreams {
	//reference to check the methods in Stream class on Java:
	//https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html
	
	LinkedList<String> testStringCollection = new LinkedList<String>();
	LinkedList<Integer> testIntCollection = new LinkedList<Integer>();
	LinkedList<User> testUserCollection = new LinkedList<User>(); 

	/*
	 * Returns whether all elements of this stream match the provided predicate.
	 * You can send a lambda expression with the condition, then if all elements matches that condition it returns true
	 * */
	public void allMatch() {
		fillStringCollection();
		println("testing if the collection has all elements with length greather than 5");
		println(testStringCollection.stream().allMatch(s -> s.length() > 5));
		

		println("testing if the collection has all elements with length greather than 0");
		println(testStringCollection.stream().allMatch(s -> s.length() > 0));
	}
	
	/*
	 * Returns whether any elements of this stream match the provided predicate.
	 * You can send a lambda expression with the condition, then if at least one element matches that condition it returns true
	 * */
	public void anyMatch() {
		fillStringCollection();
		println("testing if the collection has any element with length greather than 5");
		println(testStringCollection.stream().anyMatch(s -> s.length() > 5));
		

		println("testing if the collection has any element with length lower than 0");
		println(testStringCollection.stream().anyMatch(s -> s.length() < 0));
	}
	
	/*
	 * Returns a builder for a Stream.
	 * You can use it to create a builder, then do more things using that builder
	 * */
	public void builder(){
		Stream.Builder<String> builder = Stream.builder();

        Stream<String> stream = builder.add("Juan")
                                    .add("Manuel")
                                    .add("Antunez")
                                    .add("Armella")
                                    .build();
  
        stream.forEach(System.out::println);
	}
	
	/*
	 * Performs a mutable reduction operation on the elements of this stream.
	 * In the example below using StringBuilder you can see how to create a single result from a list of object in using different types of appends
	 * */
	public void collect() {
		List<String> vowels = List.of("a", "e", "i", "o", "u");

		// sequential stream - nothing to combine
		StringBuilder result = vowels.stream().collect(StringBuilder::new, (x, y) -> x.append(y),
				(a, b) -> a.append(",").append(b));
		System.out.println(result.toString());

		// parallel stream - combiner is combining partial results
		StringBuilder result1 = vowels.parallelStream().collect(StringBuilder::new, (x, y) -> x.append(y),
				(a, b) -> a.append(",").append(b));
		System.out.println(result1.toString());
	}
	
	/*
	 * Performs a mutable reduction operation on the elements of this stream using a Collector.
	 * You can convert a stream back to a collection using this method, in the examle you can see how to convert it to a list, a set and a map
	 * */
	public void collect(int typeOfCollection) {
		List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
		System.out.println(numbers);
		switch(typeOfCollection) {
			case 1:
				List<Integer> evenNumbers = numbers.stream().filter(x -> x % 2 == 0).collect(Collectors.toList());
				System.out.println("removing odd numbers and converting it to a list");
				System.out.println(evenNumbers);  // [2, 4, 6]
				break;
			case 2:
				Set<Integer> oddNumbers = numbers.parallelStream().filter(x -> x % 2 != 0).collect(Collectors.toSet());
				System.out.println("removing pair numbers and converting it to a set");
				System.out.println(oddNumbers); // [1, 3, 5]
				break;
			case 3:
				Map<Integer, String> mapOddNumbers = numbers.parallelStream().filter(x -> x % 2 != 0)
				.collect(Collectors.toMap(Function.identity(), x -> String.valueOf(x)));
				System.out.println("removing pair numbers and converting it to a map");
				System.out.println(mapOddNumbers); // {1=1, 3=3, 5=5}
				break;
			default:
				break;
		}
	}
	
	/*
	 * Creates a lazily concatenated stream whose elements are all the elements of the first stream followed by all the elements of the second stream.
	 * You can use it to merge two streams
	 * NOTE: All the elements will be sorted by insertion order
	 * */
	public void concat() {
		List<String> list1 = Arrays.asList("Juan", "Manuel", "Francisco");
		List<String> list2 = Arrays.asList("Antunez", "Armella");
		List<String> list3 = Arrays.asList("Sr.");
        
        System.out.print("\nlist1: ");
        list1.stream().forEach(element -> System.out.print(element + " "));
        System.out.print("\nlist2: ");
        list2.stream().forEach(element -> System.out.print(element + " "));
        System.out.print("\nlist3: ");
        list3.stream().forEach(element -> System.out.print(element + " "));
        System.out.print("\n");
        
        System.out.print("Stream.concat(list1.stream(), list2.stream()):\n");
        Stream.concat(list1.stream(), list2.stream())
            .forEach(element -> System.out.print(element + " "));
        System.out.print("\n");

        System.out.print("Stream.concat(list2.stream(), list1.stream()):\n");
        Stream.concat(list2.stream(), list1.stream())
            .forEach(element -> System.out.print(element + " "));
        System.out.print("\n");
        
        System.out.print("Stream.concat(list3.stream(), Stream.concat(list1.stream(), list2.stream())):\n");
        Stream.concat(list3.stream(), Stream.concat(list1.stream(), list2.stream()))
            .forEach(element -> System.out.print(element + " "));
        System.out.print("\n");
	}
	
	/*
	 * Returns the count of elements in this stream.
	 * You can use it to know the number of elements in the stream
	 * */
	public void count() {
		fillIntCollection();
		System.out.println(testIntCollection.stream().count());
	}
	
	/*
	 * Returns a stream consisting of the distinct elements (according to Object.equals(Object)) of this stream.
	 * NOTE: guarantees the ordering for the streams backed by an ordered collection
	 * */
	public void distinct() {
		fillIntCollection();
		testIntCollection.stream().distinct()
			.forEach(e -> System.out.print(e + ", "));
		System.out.print("\nTotal: ");
		System.out.println(testIntCollection.stream().distinct().count());
	}
	
	/*
	 * Returns an empty sequential Stream.
	 * Allows you to delete the elements of an stream
	 * */
	public void empty() {
		Stream<String> stream = Stream.empty();
        stream.forEach(System.out::println);
	}
	
	/*
	 * Returns a stream consisting of the elements of this stream that match the given predicate.
	 * You can use it to remove items that doesn't fit an specific criteria using a lambda expression
	 */
	public void filter() {
		fillStringCollection();
		//System.out.println("printing the original list of strings:");
		//testStringCollection.stream().forEach(System.out::println);
		System.out.println("\n\n\n\n\n");
		System.out.println("filtering the strings with a length over 5");
		testStringCollection.stream().filter(x -> x.length() > 5).forEach(System.out::println);
		System.out.println("\n\n\n\n\n");
		System.out.println("filtering the strings with a space");
		testStringCollection.stream().filter(x -> x.contains(" ")).forEach(System.out::println);
		System.out.println("\n\n\n\n\n");
		System.out.println("filtering the strings that doesn't have the letter 'a' nor 'A'");
		testStringCollection.stream().filter(x -> !x.contains("a")&&!x.contains("A")).forEach(System.out::println);
	}
	
	/*
	 * Returns an Optional describing some element of the stream, or an empty Optional if the stream is empty.
	 * NOTE: it will return the first element in the case of a list as they are sorted
	 */
	public void findAny() {
		List<Integer> list = Arrays.asList(2, 4, 6, 8, 10);
		System.out.println(list);
		System.out.println("list.stream().findAny()");
		
		Optional<Integer> answer = list.stream().findAny();

        if (answer.isPresent()) {
            System.out.println(answer.get());
        }
        else {
            System.out.println("no value");
        }
	}
	
	/*
	 * Returns an Optional describing the first element of this stream, or an empty Optional if the stream is empty.
	 * NOTE: it will return the first element in the case of a list as they are sorted
	 */
	public void findFirst() {
		List<Integer> list = Arrays.asList(2, 4, 6, 8, 10);
		System.out.println(list);
		System.out.println("list.stream().findFirst()");
		
		Optional<Integer> answer = list.stream().findFirst();

        if (answer.isPresent()) {
            System.out.println(answer.get());
        }
        else {
            System.out.println("no value");
        }
	}
	
	/*
	 * Returns a stream consisting of the results of replacing each element of this stream with the contents of a mapped stream produced by 
	 * applying the provided mapping function to each element.
	 * NOTE: the flattened elements will keep their order
	 */
	public void flatMap() {
		fillIntCollection();
		println("We can use flatMap to convert the Integers into Strings: testIntCollection.stream().flatMap(num -> Stream.of(num))");
		testIntCollection.stream().flatMap(num -> Stream.of(num)).forEach(System.out::println);
		
		println("\n\n\nAnother example of flatmap is when you want to merge several list into one single result");
		List<Integer> PrimeNumbers = Arrays.asList(5, 7, 11,13);
		println("PrimeNumbers");
		println(PrimeNumbers);
        List<Integer> OddNumbers = Arrays.asList(1, 3, 5);
        println("OddNumbers");
        println(OddNumbers);
        List<Integer> EvenNumbers = Arrays.asList(2, 4, 6, 8);
        println("EvenNumbers");
        println(EvenNumbers);
        List<List<Integer>> listOfListofInts = Arrays.asList(PrimeNumbers, OddNumbers, EvenNumbers);
        println("listOfListofInts");
        println(listOfListofInts);
  
        System.out.println("The Structure before flattening is : " + listOfListofInts);
          
        List<Integer> listofInts  = listOfListofInts.stream().flatMap(list -> list.stream())
                                    .collect(Collectors.toList());
  
        System.out.println("The Structure after flattening is : " + listofInts);
        println("listOfListofInts.stream().flatMap(list -> list.stream())");
	}
	
	/*
	 * Returns an DoubleStream consisting of the results of replacing each element of this stream with the contents of a mapped 
	 * stream produced by applying the provided mapping function to each element.
	 * NOTE: the flattened elements will keep their order
	 */
	public void flatMapToDouble() {
		List<String> list = Arrays.asList("1.5", "2.7", "3", "4", "5.6");
		println(list);
		println("list.stream().flatMapToDouble(num  -> DoubleStream.of(Double.parseDouble(num)))");
		list.stream().flatMapToDouble(num  -> DoubleStream.of(Double.parseDouble(num))).forEach(System.out::println);
		
		list = Arrays.asList("Juan", "Manuel", "Antunez", "Armella");
		println("");
		println(list);
		println("list.stream().flatMapToDouble(str -> DoubleStream.of(str.length()))");
		list.stream().flatMapToDouble(str -> DoubleStream.of(str.length())).forEach(System.out::println);
	}
	
	/*
	 * Returns an IntStream consisting of the results of replacing each element of this stream with the contents of a mapped 
	 * stream produced by applying the provided mapping function to each element.
	 * NOTE: the flattened elements will keep their order
	 */
	public void flatMapToInt() {
		List<String> list = Arrays.asList("1", "5", "3", "90", "63");
		println(list);
		println("list.stream().flatMapToInt(num  -> IntStream.of(Integer.parseInt(num)))");
		list.stream().flatMapToInt(num  -> IntStream.of(Integer.parseInt(num))).forEach(System.out::println);
		
		list = Arrays.asList("Juan", "Manuel", "Antunez", "Armella");
		println("");
		println(list);
		println("list.stream().flatMapToInt(str -> IntStream.of(str.length()))");
		list.stream().flatMapToInt(str -> IntStream.of(str.length())).forEach(System.out::println);
	}
	
	/*
	 * Returns an LongStream consisting of the results of replacing each element of this stream with the contents of a mapped 
	 * stream produced by applying the provided mapping function to each element.
	 * NOTE: the flattened elements will keep their order
	 */
	public void flatMapToLong() {
		List<String> list = Arrays.asList("1", "5", "3", "90", "63");
		println(list);
		println("list.stream().flatMapToLong(num  -> LongStream.of(Long.parseLong(num)))");
		list.stream().flatMapToLong(num  -> LongStream.of(Long.parseLong(num))).forEach(System.out::println);
		
		list = Arrays.asList("Juan", "Manuel", "Antunez", "Armella");
		println("");
		println(list);
		println("list.stream().flatMapToLong(str -> LongStream.of(str.length()))");
		list.stream().flatMapToLong(str -> LongStream.of(str.length())).forEach(System.out::println);
	}
	
	/*
	 * Performs an action for each element of this stream.
	 */
	public void forEach() {
		fillIntCollection();
		println("testIntCollection.stream().forEach(n -> println(n))");
		testIntCollection.stream().forEach(n -> println(n));
	}
	
	/*
	 * Performs an action for each element of this stream, in the encounter order of the stream if the stream has a defined encounter order.
	 */
	public void forEachOrdered() {
		fillIntCollection();
		println("testIntCollection.stream().forEachOrdered(n -> println(n))");
		testIntCollection.stream().forEachOrdered(n -> println(n));
		println("They are sorted by insertion order");
	}
	
	/*
	 * Returns an infinite sequential unordered stream where each element is generated by the provided Supplier.
	 */
	public void generate() {
		println("generating 5 random integers");
		println("Stream.generate(new Random()::nextInt).limit(5).forEach(System.out::println)");
		Stream.generate(new Random()::nextInt).limit(5).forEach(System.out::println);
	}
	
	/*
	 * Returns an infinite sequential ordered Stream produced by iterative application of a function f to an initial element seed, 
	 * producing a Stream consisting of seed, f(seed), f(f(seed)), etc.
	 */
	public void iterate() {
		println("stream = Stream.iterate(1, i -> i <= 20, i -> i * 2)");
		Stream<Integer> stream = Stream.iterate(1, i -> i <= 20, i -> i * 2);
	    stream.forEach(System.out::println);
	    
	    println("streamD = Stream.iterate(2.0, decimal -> decimal > 0.25, decimal -> decimal / 2)");
	    Stream<Double> streamD = Stream.iterate(2.0, decimal -> decimal > 0.25, decimal -> decimal / 2);
	    streamD.forEach(System.out::println);
	}
	
	/*
	 * Returns a stream consisting of the elements of this stream, truncated to be no longer than maxSize in length.
	 */
	public void limit() {
		fillStringCollection();
		println("testStringCollection.stream().limit(3)");
		testStringCollection.stream().limit(3).forEach(System.out::println);
	}
	
	/*
	 * Returns a stream consisting of the results of applying the given function to the elements of this stream.
	 * You can use it to convert each element form one collection to something else, it's commonly used while working with Objects
	 */
	public void map() {
		fillIntCollection();
		println("testIntCollection.stream().limit(5).map(n -> n * 2)");
		testIntCollection.stream().limit(5).map(n -> n * 2).forEach(System.out::println);
		
		fillStringCollection();
		println("testStringCollection.stream().filter(n -> n.length() > 3 && n.length() < 10).limit(5).map(String::toUpperCase)");
		testStringCollection.stream().filter(n -> n.length() > 3 && n.length() < 10).limit(5).map(String::toUpperCase).forEach(System.out::println);
		
		fillUserCollection();
		println("let get the names of the collection of users");
		println("testUserCollection.stream().map(n -> n.getName())");
		testUserCollection.stream().map(n -> n.getName() + " " + n.getLastName()).forEach(System.out::println);
	}
	
	/*
	 * Returns a DoubleStream consisting of the results of applying the given function to the elements of this stream.
	 */
	public void mapToDouble() {
		fillIntCollection();
		println("testIntCollection.stream().limit(5).mapToDouble(n -> (double)n / 2)");
		testIntCollection.stream().limit(8).mapToDouble(n -> (double)n / 2).forEach(System.out::println);
	}
	
	/*
	 * Returns an IntStream consisting of the results of applying the given function to the elements of this stream.
	 */
	public void mapToInt() {
		fillIntCollection();
		println("testIntCollection.stream().limit(5).mapToInt(n -> n * 2)");
		testIntCollection.stream().limit(8).mapToInt(n -> n * 2).forEach(System.out::println);
	}
	
	/*
	 * Returns a LongStream consisting of the results of applying the given function to the elements of this stream.
	 */
	public void mapToLong() {
		fillIntCollection();
		println("testIntCollection.stream().limit(5).mapToLong(n -> n * 2)");
		testIntCollection.stream().limit(8).mapToLong(n -> n * 2).forEach(System.out::println);
	}
	
	/*
	 * Returns the maximum element of this stream according to the provided Comparator.
	 */
	public void max() {
		fillIntCollection();
		println("testIntCollection.stream().max(Integer::compare).get()");
		System.out.print("The maximum value is : ");
		Integer var = testIntCollection.stream().max(Integer::compare).get();
        System.out.println(var);
        println("");
        
		println("testIntCollection.stream().max(Comparator.reverseOrder()).get()");
		System.out.print("The maximum value is : ");
		var = testIntCollection.stream().max(Comparator.reverseOrder()).get();
        System.out.println(var);
        println("");
        
        fillStringCollection();
        println("Getting the value in the list of strings with the greater character at the end of the string");
        println("testStringCollection.stream().max((str1, str2) -> Character.compare(str1.charAt(str1.length() - 1), str2.charAt(str2.length() - 1)))");
        Optional<String> MAX = testStringCollection.stream().max((str1, str2) -> Character.compare(str1.charAt(str1.length() - 1), str2.charAt(str2.length() - 1)));
        if (MAX.isPresent())
            System.out.println(MAX.get());
        else
            System.out.println("-1");
	}
	
	/*
	 * Returns the minimum element of this stream according to the provided Comparator.
	 */
	public void min() {
		fillIntCollection();
		println("testIntCollection.stream().min(Integer::compare).get()");
		System.out.print("The minimum value is : ");
		Integer var = testIntCollection.stream().min(Integer::compare).get();
        System.out.println(var);
        println("");
        
		println("testIntCollection.stream().min(Comparator.reverseOrder()).get()");
		System.out.print("The minimum value is : ");
		var = testIntCollection.stream().min(Comparator.reverseOrder()).get();
        System.out.println(var);
        println("");
        
        fillStringCollection();
        println("Getting the value in the list of strings with the lower character at the end of the string");
        println("testStringCollection.stream().min((str1, str2) -> Character.compare(str1.charAt(str1.length() - 1), str2.charAt(str2.length() - 1)))");
        Optional<String> MIN = testStringCollection.stream().min((str1, str2) -> Character.compare(str1.charAt(str1.length() - 1), str2.charAt(str2.length() - 1)));
        if (MIN.isPresent())
            System.out.println(MIN.get());
        else
            System.out.println("-1");
	}
	
	/*
	 * Returns whether no elements of this stream match the provided predicate.
	 */
	public void noneMatch() {
		fillStringCollection();
		println("Using noneMatch we can verify if we don't have any element in the list that has a length of 4");
		println("testStringCollection.stream().noneMatch(str -> str.length() == 4)");
		if(testStringCollection.stream().noneMatch(str -> str.length() == 4))
			println("There is none element that has a length of 4");
		else
			println("There is at least one element that has a length of 4");
	}
	
	/*
	 * Returns a sequential ordered stream whose elements are the specified values.
	 * Returns a sequential Stream containing a single element.
	 */
	public void of() {
		println("stream = Stream.of(\"Juan\")");
		Stream<String> stream = Stream.of("Juan");
		stream.forEach(System.out::println);
		
		println("");
		println("stream = Stream.of(\"Juan\", \"Manuel\", \"Antúnez\")");
		stream = Stream.of("Juan", "Manuel", "Antúnez");
		stream.forEach(System.out::println);
	}
	
	/*
	 * Returns a stream consisting of the elements of this stream, additionally performing the provided action on each element 
	 * as elements are consumed from the resulting stream.
	 */
	public void peek() {
		fillIntCollection();
		println("testIntCollection.stream().peek(System.out::println).count()");
		testIntCollection.stream().peek(System.out::println).count();
	}
	
	/*
	 * Performs a reduction on the elements of this stream, using an associative accumulation function, and returns an 
	 * Optional describing the reduced value, if any.
	 * NOTE: While using the reduce, make sure the reduce will return something, if you are not sure, you can use orElse
	 */
	public void reduce() {
		fillStringCollection();
		println("We can use reduce to get the element with the max length of the list");
		println("val = testStringCollection.stream().reduce((val1, val2) -> val1.length() > val2.length() ? val1 : val2)");
		Optional<String> val = testStringCollection.stream().reduce((val1, val2) -> val1.length() > val2.length() ? val1 : val2);
		val.ifPresent(System.out::println);
		
		println("");
		println("We can use reduce to concatenate the string on the list");
		println("val = testStringCollection.stream().filter(n -> n.length() < 5).reduce((val1, val2) -> val1 + \" - \" + val2)");
		val = testStringCollection.stream().filter(n -> n.length() < 5).reduce((val1, val2) -> val1 + " - " + val2);
		val.ifPresent(System.out::println);
		
		fillIntCollection();
		println("");
		println("We can use reduce to sum the values of the elements in the list");
		println("valI = testIntCollection.stream().reduce(0, (val1, val2) -> val1 + val2)");
		int valI = testIntCollection.stream().reduce(0, (val1, val2) -> val1 + val2);
		println(valI);
		
		println("");
		println("We can use reduce to apply an operation to the elements in the list and return a value");
		println("valI = testIntCollection.stream().reduce((val1, val2) -> val1 - val2).orElse(-1)");
		valI = testIntCollection.stream().reduce((val1, val2) -> val1 - val2).orElse(-1);
		println(valI);
	}
	
	/*
	 * Returns a stream consisting of the remaining elements of this stream after discarding the first n elements of the stream.
	 */
	public void skip() {
		fillStringCollection();
		println("testStringCollection.stream().skip(9)");
		testStringCollection.stream().skip(9).forEach(System.out::println);
	}
	
	/*
	 * Returns a stream consisting of the elements of this stream, sorted according to natural order.
	 * Returns a stream consisting of the elements of this stream, sorted according to the provided Comparator.
	 */
	public void sorted() {
		fillIntCollection();
		println("testIntCollection.stream().sorted()");
		testIntCollection.stream().sorted().forEach(System.out::println);
		
		println("");
		fillUserCollection();
		println("");
		testUserCollection.stream().sorted((u1, u2) -> u1.getDateOfBirth().compareTo(u2.getDateOfBirth())).forEach(System.out::println);
	}
	
	/*
	 * Returns an array containing the elements of this stream.
	 */
	public void toArray() {
		fillStringCollection();
		println("testStringCollection.stream().toArray()");
		Object[] values = testStringCollection.stream().toArray();
		println(Arrays.toString(values));
	}
	
	public void fillStringCollection() {
		testStringCollection = new LinkedList<String>();
		testStringCollection.add("Hello");
		testStringCollection.add("World");
		testStringCollection.add("Juan");
		testStringCollection.add("Manuel");
		testStringCollection.add("Francisco");
		testStringCollection.add("Antúnez");
		testStringCollection.add("Armella");
		testStringCollection.add("This is a longer text");
		testStringCollection.add("Lorem ipsum dolor sit amet; consectetuer adipiscing elit. Aenean et est a dui semper facilisis. Pellentesque placerat elit a nunc. Nullam tortor odio; rutrum quis; egestas ut; posuere sed; felis. Vestibulum placerat feugiat nisl. Suspendisse lacinia; odio non feugiat vestibulum; sem erat blandit metus; ac nonummy magna odio pharetra felis. Vivamus vehicula velit non metus faucibus auctor. Nam sed augue. Donec orci. Cras eget diam et dolor dapibus sollicitudin. In lacinia; tellus vitae laoreet ultrices; lectus ligula dictum dui; eget condimentum velit dui vitae ante. Nulla nonummy augue nec pede. Pellentesque ut nulla. Donec at libero. Pellentesque at nisl ac nisi fermentum viverra. Praesent odio. Phasellus tincidunt diam ut ipsum. Donec eget est.");
		testStringCollection.add("this");
		testStringCollection.add("was");
		testStringCollection.add("a");
		testStringCollection.add("bad");
		testStringCollection.add("idea");
		println(testStringCollection);
	}
	
	public void fillIntCollection() {
		testIntCollection = new LinkedList<Integer>();
		testIntCollection.add(Integer.valueOf(1));
		testIntCollection.add(Integer.valueOf(59));
		testIntCollection.add(Integer.valueOf(25));
		testIntCollection.add(Integer.valueOf(45));
		testIntCollection.add(Integer.valueOf(75));
		testIntCollection.add(Integer.valueOf(84));
		testIntCollection.add(Integer.valueOf(95));
		testIntCollection.add(Integer.valueOf(81));
		testIntCollection.add(Integer.valueOf(62));
		testIntCollection.add(Integer.valueOf(32));
		testIntCollection.add(Integer.valueOf(81));
		testIntCollection.add(Integer.valueOf(92));
		testIntCollection.add(Integer.valueOf(64));
		testIntCollection.add(Integer.valueOf(81));
		testIntCollection.add(Integer.valueOf(29));
		testIntCollection.add(Integer.valueOf(27));
		testIntCollection.add(Integer.valueOf(38));
		testIntCollection.add(Integer.valueOf(27));
		testIntCollection.add(Integer.valueOf(26));
		testIntCollection.add(Integer.valueOf(15));
		testIntCollection.add(Integer.valueOf(38));
		testIntCollection.add(Integer.valueOf(39));
		testIntCollection.add(Integer.valueOf(36));
		testIntCollection.add(Integer.valueOf(33));
		testIntCollection.add(Integer.valueOf(423));
		testIntCollection.add(Integer.valueOf(99));
		println(testIntCollection);
	}
	
	public void fillUserCollection() {
		testUserCollection = new LinkedList<User>();
		User user = new User();
		user.setId((long)1);
		user.setUsername("Draculavenom");
		user.setPassword("DraculavenomPassword");
		user.setName("Juan");
		user.setLastName("Antunez");
		user.setDateOfBirth(new Date(100, 00, 01));
		testUserCollection.add(user);
		user = new User();
		user.setId((long)2);
		user.setUsername("DarkMaster");
		user.setPassword("DarkMasterPassword");
		user.setName("Manuel");
		user.setLastName("Armella");
		user.setDateOfBirth(new Date(90, 06, 18));
		testUserCollection.add(user);
		testUserCollection.stream().forEach(n -> println(n));
	}
	
	private void println(Object arg) {
		System.out.println(arg);
	}
	
	private void print(Object arg) {
		System.out.print(arg);
	}
}
