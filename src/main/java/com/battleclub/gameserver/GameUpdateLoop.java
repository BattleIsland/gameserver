package com.battleclub.gameserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableScheduling
@Slf4j
public class GameUpdateLoop {
    private final Game game;

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public GameUpdateLoop(SimpMessagingTemplate messagingTemplate, Game game) {
        this.messagingTemplate = messagingTemplate;
        this.game = game;
    }

    @Scheduled(fixedRate = 1000 / 60) // 1 second
    public void update() {
        game.update();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String message = objectMapper.writeValueAsString(game.toGameState());
            messagingTemplate.convertAndSend("/topic/messages", message);
        } catch (JsonProcessingException e) {
            log.error("error with objectmapper", e);
        }
    }
}
