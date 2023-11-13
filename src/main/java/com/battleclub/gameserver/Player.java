package com.battleclub.gameserver;

import lombok.Data;

@Data
public class Player {
    private String sessionId;
    private int health;
    private int x;
    private int y;
    private int direction;

    public Player(String sessionId) {
        this.sessionId = sessionId;
        health = 100;
        x = 0;
        y = 0;
        direction = 0;
    }
}
