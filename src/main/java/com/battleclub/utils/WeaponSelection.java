package com.battleclub.utils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class WeaponSelection {
    private static Set<String> VALID_KEYS = Set.of("1", "2", "3");

    // returns -1 if no valid weapon pressed
    public int getWeaponIndex(List<String> keysPressed) {
        Optional<String> firstKeyPressed = keysPressed.stream()
            .sorted(String::compareTo)
            .filter((keyPressed) -> VALID_KEYS.contains(keyPressed))
            .findFirst();
        if (!firstKeyPressed.isPresent()) {
            return -1;
        } else {
            try {
                return Integer.parseInt(firstKeyPressed.get());
            } catch (NumberFormatException e) {
                return -1;
            }
        }
    }
}
