package com.battleclub.gameserver.player;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.battleclub.gameserver.Game;
import com.battleclub.gameserver.Game.Coordinates;
import com.battleclub.utils.CoordinateUtils;

@Component
public class PlayerMovement {
    @Autowired
    private CoordinateUtils coordinateUtils;

    private static int UNIT = 1;
    private static int MULTIPLIER = 1;

    private static Map<String, Coordinates> KEY_TO_MOVEMENT = Map.of(
        "w", new Coordinates(0, -UNIT),
        "a", new Coordinates(-UNIT, 0),
        "s", new Coordinates(0, UNIT),
        "d", new Coordinates(UNIT, 0),
        "ArrowUp", new Coordinates(0, -UNIT),
        "ArrowLeft", new Coordinates(-UNIT, 0),
        "ArrowDown", new Coordinates(0, UNIT),
        "ArrowRight", new Coordinates(UNIT, 0)
    );

    public Coordinates calculateNetMovement(List<String> keysClicked) {
        Coordinates netMovement = keysClicked.stream()
            .filter(KEY_TO_MOVEMENT::containsKey)
            .map(KEY_TO_MOVEMENT::get)
            .reduce(new Coordinates(0, 0), coordinateUtils::add);
        double normalizationFactor = Math.sqrt(netMovement.x() * netMovement.x() + netMovement.y() * netMovement.y());
        normalizationFactor = normalizationFactor == 0 ? 1 : normalizationFactor;
        return coordinateUtils.divide(normalizationFactor, coordinateUtils.multiply(MULTIPLIER, netMovement));
    }
}
