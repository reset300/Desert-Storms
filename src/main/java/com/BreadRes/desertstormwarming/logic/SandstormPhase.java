package com.BreadRes.desertstormwarming.logic;

import com.BreadRes.desertstormwarming.config.StormConfig;

public enum SandstormPhase {
    PHASE_1(1),
    PHASE_2(2),
    PHASE_3(3),
    PHASE_4(4),
    PHASE_5(5);

    public final float windStrength;
    private final int phaseNumber;

    SandstormPhase(int phaseNumber) {
        this.phaseNumber = phaseNumber;
        this.windStrength = StormConfig.getWindStrength(phaseNumber);
    }

    public float getWindStrength() {
        return StormConfig.getWindStrength(phaseNumber);
    }

    public int getPlayerDamage() {
        return StormConfig.getPlayerDamage(phaseNumber);
    }

    public int getArmorDamage() {
        return StormConfig.getArmorDamage(phaseNumber);
    }

    public int getPlayerDamageInterval() {
        return StormConfig.getPlayerDamageInterval(phaseNumber);
    }

    public int getArmorDamageInterval() {
        return StormConfig.getArmorDamageInterval(phaseNumber);
    }

    public boolean hasFatigue() {
        return StormConfig.hasFatigue(phaseNumber);
    }

    public boolean hasHunger() {
        return StormConfig.hasHunger(phaseNumber);
    }

    public boolean hasBlindness() {
        return StormConfig.hasBlindness(phaseNumber);
    }

    public boolean hasSlowness() {
        return StormConfig.hasSlowness(phaseNumber);
    }

    public boolean hasWeakness() {
        return StormConfig.hasWeakness(phaseNumber);
    }

    public int getAirConsumption() {
        return StormConfig.getAirConsumption(phaseNumber);
    }

    public int getMinDuration() {
        return StormConfig.getMinDuration(phaseNumber);
    }

    public int getMaxDuration() {
        return StormConfig.getMaxDuration(phaseNumber);
    }

    public SandstormPhase getNextPhase() {
        return switch (this) {
            case PHASE_1 -> PHASE_2;
            case PHASE_2 -> PHASE_3;
            case PHASE_3 -> PHASE_4;
            case PHASE_4 -> PHASE_5;
            case PHASE_5 -> PHASE_1;
            default -> PHASE_1;
        };
    }

    public SandstormPhase getPreviousPhase() {
        return switch (this) {
            case PHASE_1, PHASE_2 -> PHASE_1;
            case PHASE_3 -> PHASE_2;
            case PHASE_4 -> PHASE_3;
            case PHASE_5 -> PHASE_4;
            default -> PHASE_1;
        };
    }
}