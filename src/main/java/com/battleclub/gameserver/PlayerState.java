package com.battleclub.gameserver;

import com.battleclub.gameserver.Game.Coordinates;

public class PlayerState {
    // we want to see this player's health, direction, coords, basically everything
    record ThisPlayerState(int health, int direction, Coordinates coordinates, int weaponIdx) {}
    // OtherPlayerState is a bit more private
    // don't show health of other players but show user name (since we don't currently have that use user id instead)
    record OtherPlayerState(String userId, int direction, Coordinates coordinates) {}
}
