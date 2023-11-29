package com.battleclub.gameserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserIdController {
    @Autowired
    private Users users;

    @Autowired
    private Game game;

    @MessageMapping("/accept_userid")
    public void accceptUserId(UserIdMessage userIdMessage, @Header("simpSessionId") String sessionId) {
        log.info("user id works");
        users.addUser(sessionId, userIdMessage.getUserId());
        game.addPlayer(userIdMessage.getUserId());
    }
}
