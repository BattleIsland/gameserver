package com.battleclub.gameserver.game;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.battleclub.gameserver.game.Game.Coordinates;
import com.battleclub.gameserver.game.Game.GameState;
import com.battleclub.gameserver.game.Game.Player;
import com.battleclub.gameserver.player.PlayerInputManager;
import com.battleclub.gameserver.player.PlayerInputMessage;
import com.battleclub.gameserver.player.PlayerMovement;
import com.battleclub.gameserver.user.Users;
import com.battleclub.gameserver.utils.CoordinateUtils;
import com.battleclub.gameserver.utils.WeaponSelection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableScheduling
@Slf4j
@AllArgsConstructor
public class GameUpdateLoop {
    private final Users users;

    private final PlayerMovement playerMovement;

    private final CoordinateUtils coordinateUtils;

    private final WeaponSelection weaponSelection;

    private final Game game;

    private final SimpMessagingTemplate messagingTemplate;

    private final PlayerInputManager playerInputManager;

    @Scheduled(fixedRate = 1000 / 60) // 1 second divided by 60 = 60 tps (ticks per second)
    public void update() {
        // pop out all player inputs and apply on game
        playerInputManager.popAll().stream().forEach(this::applyPlayerInput);

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

    private void applyPlayerInput(PlayerInputManager.PlayerInput playerInput) {
        String sessionId = playerInput.sessionId();
        PlayerInputMessage playerInputMessage = playerInput.playerInputMessage();
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
