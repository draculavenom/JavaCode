package com.draculavenom.usersHandler.controller;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TemporaryPasswordService {
    
    private final SecureRandom random = new SecureRandom();

    public String generateLoginPassword() {
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        List<Character> chars = new ArrayList<>();
        
        for(int i = 0; i < 4; i++) {
            chars.add(uppercase.charAt(random.nextInt(uppercase.length())));
        }

        for(int i = 0; i < 4; i++) {
            chars.add(lowercase.charAt(random.nextInt(lowercase.length())));
        }

        for(int i = 0; i < 3; i++){
            chars.add(numbers.charAt(random.nextInt(numbers.length())));
        }

        Collections.shuffle(chars, random);

        StringBuilder password = new StringBuilder();
        chars.forEach(password::append);

        return password.toString();
    }
}
