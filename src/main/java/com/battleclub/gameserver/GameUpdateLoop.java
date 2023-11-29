package com.battleclub.gameserver;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.battleclub.gameserver.Game.GameState;
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

    @Scheduled(fixedRate = 1000 / 60) // 1 second divided by 60 = 60 tps (ticks per second)
    public void update() {
        game.update();
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, GameState> userToGameState = game.toGameStates();
        userToGameState.entrySet().stream().forEach((entry) -> {
            try {
                String userId = entry.getKey();
                String message = objectMapper.writeValueAsString(entry.getValue());
                log.info(userId);
                log.info(message);
                messagingTemplate.convertAndSend("/topic/" + userId + "/messages", message);
            } catch (JsonProcessingException e) {
                log.error("error with objectmapper", e);
            }
        });

        
    }
}
