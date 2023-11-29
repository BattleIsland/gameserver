package com.battleclub.gameserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
public class PlayerJoinEventListener implements ApplicationListener<SessionConnectedEvent> {
    @Autowired
    private Game game;

    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        // This is a CONNECT message, i.e., a new WebSocket session is being established
        String sessionId = accessor.getSessionId();
        game.addPlayer(sessionId);
    }
}
