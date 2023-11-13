package com.battleclub.gameserver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class Game {
    public Map<String, Player> players;

    public Game() {
        players = new ConcurrentHashMap<>();
    }

    public void addPlayer(String sessionId) {
        players.put(sessionId, new Player(sessionId));   
    }
}
