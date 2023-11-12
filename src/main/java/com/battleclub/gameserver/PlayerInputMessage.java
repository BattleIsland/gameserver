package com.battleclub.gameserver;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerInputMessage {
    private boolean isMoving;
    private String direction;
    private boolean isUsingCurrentWeapon;
    private boolean isSwitchingWeapon;
    private Integer newWeaponIndex;
}
