package com.java8;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.entities.User1;

public class TestingWithUsers {
	public static void test1() {
        // Assuming userList is the list of users
        List<User1> userList = retrieveUserList(); // Replace this with the actual list retrieval method

        // Filter users with age less than 50
        List<User1> usersBelow50 = userList.stream()
                .filter(user -> LocalDate.now().getYear() - user.getDateOfBirth().getYear() < 50)
                .collect(Collectors.toList());

        // Sort users by age and print each user's age
        usersBelow50.stream()
                .sorted(Comparator.comparing(user -> LocalDate.now().getYear() - user.getDateOfBirth().getYear()))
                .forEach(user -> System.out.println("User: " + user.getName() + ", Age: " +
                        (LocalDate.now().getYear() - user.getDateOfBirth().getYear())));

        // Sort users by phone number
        List<User1> usersByPhoneNumber = userList.stream()
                .sorted(Comparator.comparing(User1::getPhoneNumber).reversed())
                .limit(10)
                .collect(Collectors.toList());
        
        usersByPhoneNumber.stream().forEach(user -> System.out.println("User: " + user.getName() + ", phone number: " + user.getPhoneNumber()));
    }

    // Method to retrieve the list of users (replace this with actual data retrieval logic)
    private static List<User1> retrieveUserList() {
    	List<User1> userList = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= 30; i++) {
            User1 user = new User1();
            user.setId(i);
            user.setName("User" + (i<10 ? "0"+i : i));
            user.setDateOfBirth(generateRandomDate());
            user.setPhoneNumber(generateRandomPhoneNumber());
            userList.add(user);
        }

        return userList;
    }
    
    private static LocalDate generateRandomDate() {
    	Random random = new Random();
        // Generating a random date between 1930 and 2000
        int minDay = (int) LocalDate.of(1930, 1, 1).toEpochDay();
        int maxDay = (int) LocalDate.of(2000, 1, 1).toEpochDay();
        long randomDay = minDay + random.nextInt(maxDay - minDay);

        return LocalDate.ofEpochDay(randomDay);
    }

    private static String generateRandomPhoneNumber() {
    	Random random = new Random();
        // Generating a random phone number (for demonstration purposes)
        StringBuilder phoneNumber = new StringBuilder("+");

        for (int i = 0; i < 11; i++) {
            phoneNumber.append(random.nextInt(10));
        }

        return phoneNumber.toString();
    }
}
