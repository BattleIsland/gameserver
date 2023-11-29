package com.battleclub.gameserver;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.battleclub.gameserver.PlayerState.OtherPlayerState;
import com.battleclub.gameserver.PlayerState.ThisPlayerState;

import lombok.Data;

@Component
@Data
public class Game {
    record Coordinates(int x, int y) {}
    record Item(String id, Coordinates coords, String type) {}
    record Player(String userId, int health, int direction, Coordinates coords) {}
    record GameState(ThisPlayerState thisPlayerState, List<OtherPlayerState> otherPlayerStates, List<Item> items) {}

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

    public void addPlayer(String userId) {
        players.put(userId, new Player(userId, 100, 0, new Coordinates(0, 0)));   
    }

    public void removePlayer(String userId) {
        players.remove(userId);
    }

    public Map<String, GameState> toGameStates() {
        // items are the same for everyone
        List<Item> allItems = items.values().stream().toList();
        return players.entrySet().stream().collect(Collectors.toMap((playerEntry) -> playerEntry.getKey(), (playerEntry) -> {
            Player thisPlayer = playerEntry.getValue();
            if (thisPlayer != null) {
                List<OtherPlayerState> otherPlayerStates = players.values().stream()
                    .filter((player) -> player.userId != thisPlayer.userId)
                    .map((otherPlayer) -> {
                        return new OtherPlayerState(otherPlayer.userId, otherPlayer.direction, otherPlayer.coords);
                    })
                    .toList();
                ThisPlayerState thisPlayerState = new ThisPlayerState(thisPlayer.health, thisPlayer.direction, thisPlayer.coords);
                return new GameState(thisPlayerState, otherPlayerStates, allItems);
            }
            return new GameState(new ThisPlayerState(100, 0, new Coordinates(0, 0)), List.of(new OtherPlayerState("", 0, new Coordinates(0, 0))), allItems);
        }));
    }
}
