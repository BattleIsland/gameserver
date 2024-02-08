package com.gameserver.user;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gameserver.utils.RandomNameGenerator;

import lombok.Data;

@Component
@Data
public class Users {
    @Autowired
    RandomNameGenerator randomNameGenerator;

    public record User(String userId, String userName, String sessionId) {}

    // session id to user
    public Map<String, User> users;

    public Users() {
        users = new ConcurrentHashMap<>();
    }

    public void addUser(String sessionId, String userId) {
        users.put(sessionId, new User(userId, randomNameGenerator.generateRandomName(), sessionId));
    }
}
