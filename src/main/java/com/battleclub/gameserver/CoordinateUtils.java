package com.battleclub.gameserver;

import org.springframework.stereotype.Component;

import com.battleclub.gameserver.Game.Coordinates;

@Component
public class CoordinateUtils {
    public Coordinates add(Coordinates a, Coordinates b) {
        return new Coordinates(a.x() + b.x(), a.y() + b.y());
    }

    public Coordinates multiply(int multiplier, Coordinates a) {
        return new Coordinates(a.x() * multiplier, a.y() * multiplier);
    }

    public Coordinates divide(double divisor, Coordinates a) {
        return new Coordinates(a.x() / divisor, a.y() / divisor);
    }
}
