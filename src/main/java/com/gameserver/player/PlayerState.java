package com.gameserver.player;

import com.gameserver.game.Game;
import com.gameserver.game.Game.Coordinates;

public class PlayerState {
    // we want to see this player's health, direction, coords, basically everything
    public record ThisPlayerState(int health, int direction, Coordinates coordinates, int weaponIdx) {}
    // OtherPlayerState is a bit more private
    // don't show health of other players but show user name (since we don't currently have that use user id instead)
    public record OtherPlayerState(String userId, int direction, Coordinates coordinates) {}
}
