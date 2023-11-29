package com.battleclub.gameserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class PlayerInputController {
    @Autowired
    private Game game;

    @MessageMapping("/accept_input")
    public void acceptInput(PlayerInputMessage playerInputMessage, @Header("simpSessionId") String sessionId) {
        log.info(game.players.get(sessionId).toString());
    }
}