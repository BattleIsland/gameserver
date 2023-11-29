package com.battleclub.gameserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.battleclub.gameserver.Game.Coordinates;
import com.battleclub.gameserver.Game.Player;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class PlayerInputController {
    @Autowired
    private Game game;

    @Autowired
    private PlayerMovement playerMovement;

    @Autowired
    private CoordinateUtils coordinateUtils;

    @MessageMapping("/accept_input")
    public void acceptInput(PlayerInputMessage playerInputMessage, @Header("simpSessionId") String sessionId) {
        log.info(game.players.get(sessionId).toString());
        Player oldPlayerValue = game.players.get(sessionId);
        if (oldPlayerValue != null) {
            // net movement is calculated from player input keys selected
            Coordinates netMovement = playerMovement.calculateNetMovement(playerInputMessage.getKeysSelected());
            Coordinates newCoords = coordinateUtils.add(netMovement, oldPlayerValue.coords());
            // new player value is composed of net movement + old player coords and player input angle
            Player newPlayerValue = new Player(sessionId, oldPlayerValue.health(), playerInputMessage.getAngle(), newCoords);
            // records are immutable so we need to copy over all old values, merge them with new ones and then add back into concurrent map
            game.players.put(sessionId, newPlayerValue);
        }
    }

}
