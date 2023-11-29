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
    private Users users;

    @Autowired
    private PlayerMovement playerMovement;

    @Autowired
    private CoordinateUtils coordinateUtils;

    @Autowired
    private WeaponSelection weaponSelection;

    @MessageMapping("/accept_input")
    public void acceptInput(PlayerInputMessage playerInputMessage, @Header("simpSessionId") String sessionId) {
        String userId = users.getUsers().get(sessionId).userId();
        Player oldPlayerValue = game.players.get(userId);
        if (oldPlayerValue != null) {
            // net movement is calculated from player input keys selected
            Coordinates netMovement = playerMovement.calculateNetMovement(playerInputMessage.getKeysSelected());
            Coordinates newCoords = coordinateUtils.add(netMovement, oldPlayerValue.coords());
            int inputWeaponIdx = weaponSelection.getWeaponIndex(playerInputMessage.getKeysSelected());
            // if -1, then retain old weapon idx else use input weapon idx
            int newWeaponIdx = inputWeaponIdx == -1 ? oldPlayerValue.weaponIdx() : inputWeaponIdx;
            // new player value is composed of net movement + old player coords and player input angle
            Player newPlayerValue = new Player(userId, oldPlayerValue.health(), playerInputMessage.getAngle(), newCoords, newWeaponIdx);
            // records are immutable so we need to copy over all old values, merge them with new ones and then add back into concurrent map
            game.players.put(userId, newPlayerValue);
        }
    }

}
