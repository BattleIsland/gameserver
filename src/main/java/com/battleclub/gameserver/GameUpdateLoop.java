package com.battleclub.gameserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

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

    @Scheduled(fixedRate = 1000) // 1 second
    public void update() {
        // Generate your message here
        String message = game.toString();

        // Send the message to the "/topic/messages" destination
        messagingTemplate.convertAndSend("/topic/messages", message);
    }
}
