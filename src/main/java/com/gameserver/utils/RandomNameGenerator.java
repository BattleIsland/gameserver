package com.gameserver.utils;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class RandomNameGenerator {
    private static final String[] NAMES = {"Alice", "Bob", "Charlie", "David", "Eva", "Frank", "Grace", "Hank", "Ivy", "Jack"};

    public String generateRandomName() {
        Random random = new Random();
        int index = random.nextInt(NAMES.length);
        return NAMES[index];
    }
}
