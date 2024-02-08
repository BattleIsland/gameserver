package com.gameserver.player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Component;

@Component
public class PlayerInputManager {
    public record PlayerInput (PlayerInputMessage playerInputMessage, String sessionId) {}
    private List<PlayerInput> playerInputs;
    private final Lock lock;

    public PlayerInputManager() {
        this.playerInputs = new ArrayList<>();
        this.lock = new ReentrantLock();
    }

    public void push(PlayerInputMessage playerInputMessage, String sessionId) {
        lock.lock();
        try {
            playerInputs.add(new PlayerInput(playerInputMessage, sessionId));
        } finally {
            lock.unlock();
        }
    }

    public List<PlayerInput> popAll() {
        lock.lock();
        try {
            List<PlayerInput> toReturn = playerInputs;
            playerInputs = new ArrayList<>();
            return toReturn;
        } finally {
            lock.unlock();
        }
    }
}
