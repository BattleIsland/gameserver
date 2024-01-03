package com.battleclub.gameserver.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.battleclub.gameserver.game.Game;
import com.battleclub.gameserver.user.Users;
import com.battleclub.gameserver.user.Users.User;

@Component
public class PlayerLeaveEventListener implements ApplicationListener<SessionDisconnectEvent> {
    @Autowired
    private Game game;

    @Autowired
    private Users users;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        User user = users.users.get(accessor.getSessionId());
        if (user != null) {
            game.removePlayer(user.userId());
            users.users.remove(user.sessionId());
        }
    }
}
