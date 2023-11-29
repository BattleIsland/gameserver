package com.battleclub.gameserver;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class Game {
    record Coordinates(int x, int y) {}
    record Item(String id, Coordinates coords, String type) {}
    record Player(String sessionId, int health, int direction, Coordinates coords) {}
    record GameState(List<Item> items, List<Player> players) {}

    public Map<String, Player> players;
    public Map<String, Item> items;
    public AtomicInteger direction;

    public Game() {
        players = new ConcurrentHashMap<>();
        items = new ConcurrentHashMap<>();
        direction = new AtomicInteger(-1);

        items.put("1", new Item("1", new Coordinates(1000, 50), "rock"));
        items.put("2", new Item("2", new Coordinates(30, 70), "rock"));
        items.put("3", new Item("3'", new Coordinates(30, 300), "toilet"));
    }

    public void update() {
        int lowerBound = 30;
        int upperBound = 1000;
        int multiplier = 1;
        if (items.get("1").coords.x <= lowerBound) {
            direction.set(1);
        } else if (items.get("1").coords.x >= upperBound) {
            direction.set(-1);
        }
        Item oldItem = items.get("1");
        items.put(
            "1",
            new Item(
                oldItem.id,
                new Coordinates(
                    oldItem.coords.x + multiplier * direction.get(),
                    oldItem.coords.y
                ),
                oldItem.type
            )
        );
    }

    public void addPlayer(String sessionId) {
        players.put(sessionId, new Player(sessionId, 100, 0, new Coordinates(0, 0)));   
    }

    public void removePlayer(String sessionId) {
        players.remove(sessionId);
    }

    public GameState toGameState() {
        return new GameState(items.values().stream().toList(), players.values().stream().toList());
    }
}
