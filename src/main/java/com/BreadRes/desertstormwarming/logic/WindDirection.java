package com.BreadRes.desertstormwarming.logic;

import net.minecraft.core.Direction;

import java.util.Random;

public enum WindDirection {
    NORTH(Direction.NORTH),
    SOUTH(Direction.SOUTH),
    WEST(Direction.WEST),
    EAST(Direction.EAST);

    public final Direction mcDirection;

    WindDirection(Direction dir) {
        this.mcDirection = dir;
    }

    public static WindDirection random(Random random) {
        return values()[random.nextInt(values().length)];
    }
}
