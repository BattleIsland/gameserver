package com.battleclub.gameserver;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerInputMessage {
    private List<String> keysSelected;
    private Integer angle;
}
