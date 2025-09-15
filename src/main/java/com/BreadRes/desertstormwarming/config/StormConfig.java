package com.BreadRes.desertstormwarming.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class StormConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue ENABLE_STORMS;
    public static final ForgeConfigSpec.BooleanValue ENABLE_STORM_SOUNDS;
    public static final ForgeConfigSpec.BooleanValue ENABLE_WIND_EFFECTS;
    public static final ForgeConfigSpec.BooleanValue ENABLE_STORM_DAMAGE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_LIGHTNING;
    public static final ForgeConfigSpec.BooleanValue ENABLE_SHELTER_SYSTEM;
    public static final ForgeConfigSpec.BooleanValue ENABLE_ARMOR_PROTECTION;
    public static final ForgeConfigSpec.BooleanValue ENABLE_CREATE_INTEGRATION;
    public static final ForgeConfigSpec.BooleanValue ENABLE_AUTO_TRIGGER;
    public static final ForgeConfigSpec.IntValue AUTO_TRIGGER_INTERVAL;
    public static final ForgeConfigSpec.IntValue MAX_STORM_PHASE;
    public static final ForgeConfigSpec.IntValue RAIN_TRIGGER_PHASE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_GLASS_BREAKING;

    public static final ForgeConfigSpec.BooleanValue USE_SKYLIGHT_SYSTEM;
    public static final ForgeConfigSpec.IntValue OLD_LOGIC_HEIGHT;
    public static final ForgeConfigSpec.BooleanValue IGNORE_ARMOR_FOR_WIND;
    public static final ForgeConfigSpec.BooleanValue IGNORE_ARMOR_FOR_EFFECTS;

    public static final ForgeConfigSpec.DoubleValue PHASE_1_WIND_STRENGTH;
    public static final ForgeConfigSpec.IntValue PHASE_1_PLAYER_DAMAGE;
    public static final ForgeConfigSpec.IntValue PHASE_1_ARMOR_DAMAGE;
    public static final ForgeConfigSpec.IntValue PHASE_1_PLAYER_DAMAGE_INTERVAL;
    public static final ForgeConfigSpec.IntValue PHASE_1_ARMOR_DAMAGE_INTERVAL;
    public static final ForgeConfigSpec.BooleanValue PHASE_1_FATIGUE;
    public static final ForgeConfigSpec.BooleanValue PHASE_1_HUNGER;
    public static final ForgeConfigSpec.BooleanValue PHASE_1_BLINDNESS;
    public static final ForgeConfigSpec.BooleanValue PHASE_1_SLOWNESS;
    public static final ForgeConfigSpec.BooleanValue PHASE_1_WEAKNESS;
    public static final ForgeConfigSpec.IntValue PHASE_1_MIN_DURATION;
    public static final ForgeConfigSpec.IntValue PHASE_1_MAX_DURATION;

    public static final ForgeConfigSpec.DoubleValue PHASE_2_WIND_STRENGTH;
    public static final ForgeConfigSpec.IntValue PHASE_2_PLAYER_DAMAGE;
    public static final ForgeConfigSpec.IntValue PHASE_2_ARMOR_DAMAGE;
    public static final ForgeConfigSpec.IntValue PHASE_2_PLAYER_DAMAGE_INTERVAL;
    public static final ForgeConfigSpec.IntValue PHASE_2_ARMOR_DAMAGE_INTERVAL;
    public static final ForgeConfigSpec.BooleanValue PHASE_2_FATIGUE;
    public static final ForgeConfigSpec.BooleanValue PHASE_2_HUNGER;
    public static final ForgeConfigSpec.BooleanValue PHASE_2_BLINDNESS;
    public static final ForgeConfigSpec.BooleanValue PHASE_2_SLOWNESS;
    public static final ForgeConfigSpec.BooleanValue PHASE_2_WEAKNESS;
    public static final ForgeConfigSpec.IntValue PHASE_2_MIN_DURATION;
    public static final ForgeConfigSpec.IntValue PHASE_2_MAX_DURATION;

    public static final ForgeConfigSpec.DoubleValue PHASE_3_WIND_STRENGTH;
    public static final ForgeConfigSpec.IntValue PHASE_3_PLAYER_DAMAGE;
    public static final ForgeConfigSpec.IntValue PHASE_3_ARMOR_DAMAGE;
    public static final ForgeConfigSpec.IntValue PHASE_3_PLAYER_DAMAGE_INTERVAL;
    public static final ForgeConfigSpec.IntValue PHASE_3_ARMOR_DAMAGE_INTERVAL;
    public static final ForgeConfigSpec.BooleanValue PHASE_3_FATIGUE;
    public static final ForgeConfigSpec.BooleanValue PHASE_3_HUNGER;
    public static final ForgeConfigSpec.BooleanValue PHASE_3_BLINDNESS;
    public static final ForgeConfigSpec.BooleanValue PHASE_3_SLOWNESS;
    public static final ForgeConfigSpec.BooleanValue PHASE_3_WEAKNESS;
    public static final ForgeConfigSpec.IntValue PHASE_3_MIN_DURATION;
    public static final ForgeConfigSpec.IntValue PHASE_3_MAX_DURATION;

    public static final ForgeConfigSpec.DoubleValue PHASE_4_WIND_STRENGTH;
    public static final ForgeConfigSpec.IntValue PHASE_4_PLAYER_DAMAGE;
    public static final ForgeConfigSpec.IntValue PHASE_4_ARMOR_DAMAGE;
    public static final ForgeConfigSpec.IntValue PHASE_4_PLAYER_DAMAGE_INTERVAL;
    public static final ForgeConfigSpec.IntValue PHASE_4_ARMOR_DAMAGE_INTERVAL;
    public static final ForgeConfigSpec.BooleanValue PHASE_4_FATIGUE;
    public static final ForgeConfigSpec.BooleanValue PHASE_4_HUNGER;
    public static final ForgeConfigSpec.BooleanValue PHASE_4_BLINDNESS;
    public static final ForgeConfigSpec.BooleanValue PHASE_4_SLOWNESS;
    public static final ForgeConfigSpec.BooleanValue PHASE_4_WEAKNESS;
    public static final ForgeConfigSpec.IntValue PHASE_4_MIN_DURATION;
    public static final ForgeConfigSpec.IntValue PHASE_4_MAX_DURATION;

    public static final ForgeConfigSpec.DoubleValue PHASE_5_WIND_STRENGTH;
    public static final ForgeConfigSpec.IntValue PHASE_5_PLAYER_DAMAGE;
    public static final ForgeConfigSpec.IntValue PHASE_5_ARMOR_DAMAGE;
    public static final ForgeConfigSpec.IntValue PHASE_5_PLAYER_DAMAGE_INTERVAL;
    public static final ForgeConfigSpec.IntValue PHASE_5_ARMOR_DAMAGE_INTERVAL;
    public static final ForgeConfigSpec.BooleanValue PHASE_5_FATIGUE;
    public static final ForgeConfigSpec.BooleanValue PHASE_5_HUNGER;
    public static final ForgeConfigSpec.BooleanValue PHASE_5_BLINDNESS;
    public static final ForgeConfigSpec.BooleanValue PHASE_5_SLOWNESS;
    public static final ForgeConfigSpec.BooleanValue PHASE_5_WEAKNESS;
    public static final ForgeConfigSpec.IntValue PHASE_5_MIN_DURATION;
    public static final ForgeConfigSpec.IntValue PHASE_5_MAX_DURATION;

    public static final ForgeConfigSpec.IntValue AIR_CONSUMPTION_PHASE_2;
    public static final ForgeConfigSpec.IntValue AIR_CONSUMPTION_PHASE_3;
    public static final ForgeConfigSpec.IntValue AIR_CONSUMPTION_PHASE_4;
    public static final ForgeConfigSpec.IntValue AIR_CONSUMPTION_PHASE_5;

    static {
        BUILDER.push("General");
        ENABLE_STORMS = BUILDER.define("enableStorms", true);
        ENABLE_STORM_SOUNDS = BUILDER.define("enableStormSounds", true);
        ENABLE_WIND_EFFECTS = BUILDER.define("enableWindEffects", true);
        ENABLE_STORM_DAMAGE = BUILDER.define("enableStormDamage", true);
        ENABLE_LIGHTNING = BUILDER.define("enableLightning", true);
        ENABLE_SHELTER_SYSTEM = BUILDER.define("enableShelterSystem", true);
        ENABLE_ARMOR_PROTECTION = BUILDER.define("enableArmorProtection", true);
        ENABLE_CREATE_INTEGRATION = BUILDER.define("enableCreateIntegration", true);
        ENABLE_AUTO_TRIGGER = BUILDER.define("enableAutoTrigger", true);
        AUTO_TRIGGER_INTERVAL = BUILDER.defineInRange("autoTriggerIntervalMinutes", 30, 5, 180);
        MAX_STORM_PHASE = BUILDER.defineInRange("maxStormPhase", 5, 1, 5);
        RAIN_TRIGGER_PHASE = BUILDER.defineInRange("rainTriggerPhase", 3, 1, 5);
        USE_SKYLIGHT_SYSTEM = BUILDER.define("useSkylightSystem", true);
        OLD_LOGIC_HEIGHT = BUILDER.defineInRange("oldLogicHeight", 63, 0, 320);
        IGNORE_ARMOR_FOR_WIND = BUILDER.define("ignoreArmorForWind", true);
        IGNORE_ARMOR_FOR_EFFECTS = BUILDER.define("ignoreArmorForEffects", true);
        ENABLE_GLASS_BREAKING = BUILDER.define("enableGlassBreaking", true);
        BUILDER.pop();

        BUILDER.push("Phase 1");
        PHASE_1_WIND_STRENGTH = BUILDER.defineInRange("windStrength", 0.1, 0.0, 2.0);
        PHASE_1_PLAYER_DAMAGE = BUILDER.defineInRange("playerDamage", 0, 0, 20);
        PHASE_1_ARMOR_DAMAGE = BUILDER.defineInRange("armorDamage", 0, 0, 10);
        PHASE_1_PLAYER_DAMAGE_INTERVAL = BUILDER.defineInRange("playerDamageInterval", 20*30, 20, 20*60);
        PHASE_1_ARMOR_DAMAGE_INTERVAL = BUILDER.defineInRange("armorDamageInterval", 20*30, 20, 20*60);
        PHASE_1_FATIGUE = BUILDER.define("fatigue", false);
        PHASE_1_HUNGER = BUILDER.define("hunger", false);
        PHASE_1_BLINDNESS = BUILDER.define("blindness", false);
        PHASE_1_SLOWNESS = BUILDER.define("slowness", false);
        PHASE_1_WEAKNESS = BUILDER.define("weakness", false);
        PHASE_1_MIN_DURATION = BUILDER.defineInRange("minDuration", 18000, 1200, 72000);
        PHASE_1_MAX_DURATION = BUILDER.defineInRange("maxDuration", 30000, 1200, 72000);
        BUILDER.pop();

        BUILDER.push("Phase 2");
        PHASE_2_WIND_STRENGTH = BUILDER.defineInRange("windStrength", 0.2, 0.0, 2.0);
        PHASE_2_PLAYER_DAMAGE = BUILDER.defineInRange("playerDamage", 1, 0, 20);
        PHASE_2_ARMOR_DAMAGE = BUILDER.defineInRange("armorDamage", 1, 0, 10);
        PHASE_2_PLAYER_DAMAGE_INTERVAL = BUILDER.defineInRange("playerDamageInterval", 20*15, 20, 20*60);
        PHASE_2_ARMOR_DAMAGE_INTERVAL = BUILDER.defineInRange("armorDamageInterval", 20*15, 20, 20*60);
        PHASE_2_FATIGUE = BUILDER.define("fatigue", false);
        PHASE_2_HUNGER = BUILDER.define("hunger", false);
        PHASE_2_BLINDNESS = BUILDER.define("blindness", false);
        PHASE_2_SLOWNESS = BUILDER.define("slowness", false);
        PHASE_2_WEAKNESS = BUILDER.define("weakness", false);
        PHASE_2_MIN_DURATION = BUILDER.defineInRange("minDuration", 24000, 1200, 72000);
        PHASE_2_MAX_DURATION = BUILDER.defineInRange("maxDuration", 48000, 1200, 72000);
        BUILDER.pop();

        BUILDER.push("Phase 3");
        PHASE_3_WIND_STRENGTH = BUILDER.defineInRange("windStrength", 0.3, 0.0, 2.0);
        PHASE_3_PLAYER_DAMAGE = BUILDER.defineInRange("playerDamage", 1, 0, 20);
        PHASE_3_ARMOR_DAMAGE = BUILDER.defineInRange("armorDamage", 1, 0, 10);
        PHASE_3_PLAYER_DAMAGE_INTERVAL = BUILDER.defineInRange("playerDamageInterval", 20*10, 20, 20*60);
        PHASE_3_ARMOR_DAMAGE_INTERVAL = BUILDER.defineInRange("armorDamageInterval", 20*10, 20, 20*60);
        PHASE_3_FATIGUE = BUILDER.define("fatigue", true);
        PHASE_3_HUNGER = BUILDER.define("hunger", false);
        PHASE_3_BLINDNESS = BUILDER.define("blindness", false);
        PHASE_3_SLOWNESS = BUILDER.define("slowness", true);
        PHASE_3_WEAKNESS = BUILDER.define("weakness", false);
        PHASE_3_MIN_DURATION = BUILDER.defineInRange("minDuration", 12000, 1200, 72000);
        PHASE_3_MAX_DURATION = BUILDER.defineInRange("maxDuration", 30000, 1200, 72000);
        BUILDER.pop();

        BUILDER.push("Phase 4");
        PHASE_4_WIND_STRENGTH = BUILDER.defineInRange("windStrength", 0.4, 0.0, 2.0);
        PHASE_4_PLAYER_DAMAGE = BUILDER.defineInRange("playerDamage", 2, 0, 20);
        PHASE_4_ARMOR_DAMAGE = BUILDER.defineInRange("armorDamage", 2, 0, 10);
        PHASE_4_PLAYER_DAMAGE_INTERVAL = BUILDER.defineInRange("playerDamageInterval", 20*6, 20, 20*60);
        PHASE_4_ARMOR_DAMAGE_INTERVAL = BUILDER.defineInRange("armorDamageInterval", 20*6, 20, 20*60);
        PHASE_4_FATIGUE = BUILDER.define("fatigue", true);
        PHASE_4_HUNGER = BUILDER.define("hunger", true);
        PHASE_4_BLINDNESS = BUILDER.define("blindness", true);
        PHASE_4_SLOWNESS = BUILDER.define("slowness", true);
        PHASE_4_WEAKNESS = BUILDER.define("weakness", false);
        PHASE_4_MIN_DURATION = BUILDER.defineInRange("minDuration", 24000, 1200, 72000);
        PHASE_4_MAX_DURATION = BUILDER.defineInRange("maxDuration", 54000, 1200, 72000);
        BUILDER.pop();

        BUILDER.push("Phase 5");
        PHASE_5_WIND_STRENGTH = BUILDER.defineInRange("windStrength", 0.5, 0.0, 2.0);
        PHASE_5_PLAYER_DAMAGE = BUILDER.defineInRange("playerDamage", 4, 0, 20);
        PHASE_5_ARMOR_DAMAGE = BUILDER.defineInRange("armorDamage", 4, 0, 10);
        PHASE_5_PLAYER_DAMAGE_INTERVAL = BUILDER.defineInRange("playerDamageInterval", 20*4, 20, 20*60);
        PHASE_5_ARMOR_DAMAGE_INTERVAL = BUILDER.defineInRange("armorDamageInterval", 20*4, 20, 20*60);
        PHASE_5_FATIGUE = BUILDER.define("fatigue", true);
        PHASE_5_HUNGER = BUILDER.define("hunger", true);
        PHASE_5_BLINDNESS = BUILDER.define("blindness", true);
        PHASE_5_SLOWNESS = BUILDER.define("slowness", true);
        PHASE_5_WEAKNESS = BUILDER.define("weakness", true);
        PHASE_5_MIN_DURATION = BUILDER.defineInRange("minDuration", 54000, 1200, 72000);
        PHASE_5_MAX_DURATION = BUILDER.defineInRange("maxDuration", 162000, 1200, 216000);
        BUILDER.pop();

        BUILDER.push("Air Consumption");
        AIR_CONSUMPTION_PHASE_2 = BUILDER.defineInRange("phase2AirConsumption", 5, 0, 50);
        AIR_CONSUMPTION_PHASE_3 = BUILDER.defineInRange("phase3AirConsumption", 8, 0, 50);
        AIR_CONSUMPTION_PHASE_4 = BUILDER.defineInRange("phase4AirConsumption", 12, 0, 50);
        AIR_CONSUMPTION_PHASE_5 = BUILDER.defineInRange("phase5AirConsumption", 15, 0, 50);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static float getWindStrength(int phase) {
        return switch (phase) {
            case 1 -> PHASE_1_WIND_STRENGTH.get().floatValue();
            case 2 -> PHASE_2_WIND_STRENGTH.get().floatValue();
            case 3 -> PHASE_3_WIND_STRENGTH.get().floatValue();
            case 4 -> PHASE_4_WIND_STRENGTH.get().floatValue();
            case 5 -> PHASE_5_WIND_STRENGTH.get().floatValue();
            default -> 0.1f;
        };
    }

    public static int getPlayerDamage(int phase) {
        return switch (phase) {
            case 1 -> PHASE_1_PLAYER_DAMAGE.get();
            case 2 -> PHASE_2_PLAYER_DAMAGE.get();
            case 3 -> PHASE_3_PLAYER_DAMAGE.get();
            case 4 -> PHASE_4_PLAYER_DAMAGE.get();
            case 5 -> PHASE_5_PLAYER_DAMAGE.get();
            default -> 0;
        };
    }

    public static int getArmorDamage(int phase) {
        return switch (phase) {
            case 1 -> PHASE_1_ARMOR_DAMAGE.get();
            case 2 -> PHASE_2_ARMOR_DAMAGE.get();
            case 3 -> PHASE_3_ARMOR_DAMAGE.get();
            case 4 -> PHASE_4_ARMOR_DAMAGE.get();
            case 5 -> PHASE_5_ARMOR_DAMAGE.get();
            default -> 0;
        };
    }

    public static int getPlayerDamageInterval(int phase) {
        return switch (phase) {
            case 1 -> PHASE_1_PLAYER_DAMAGE_INTERVAL.get();
            case 2 -> PHASE_2_PLAYER_DAMAGE_INTERVAL.get();
            case 3 -> PHASE_3_PLAYER_DAMAGE_INTERVAL.get();
            case 4 -> PHASE_4_PLAYER_DAMAGE_INTERVAL.get();
            case 5 -> PHASE_5_PLAYER_DAMAGE_INTERVAL.get();
            default -> 20*30;
        };
    }

    public static int getArmorDamageInterval(int phase) {
        return switch (phase) {
            case 1 -> PHASE_1_ARMOR_DAMAGE_INTERVAL.get();
            case 2 -> PHASE_2_ARMOR_DAMAGE_INTERVAL.get();
            case 3 -> PHASE_3_ARMOR_DAMAGE_INTERVAL.get();
            case 4 -> PHASE_4_ARMOR_DAMAGE_INTERVAL.get();
            case 5 -> PHASE_5_ARMOR_DAMAGE_INTERVAL.get();
            default -> 20*30;
        };
    }

    public static int getMinDuration(int phase) {
        return switch (phase) {
            case 1 -> PHASE_1_MIN_DURATION.get();
            case 2 -> PHASE_2_MIN_DURATION.get();
            case 3 -> PHASE_3_MIN_DURATION.get();
            case 4 -> PHASE_4_MIN_DURATION.get();
            case 5 -> PHASE_5_MIN_DURATION.get();
            default -> 0;
        };
    }

    public static int getMaxDuration(int phase) {
        return switch (phase) {
            case 1 -> PHASE_1_MAX_DURATION.get();
            case 2 -> PHASE_2_MAX_DURATION.get();
            case 3 -> PHASE_3_MAX_DURATION.get();
            case 4 -> PHASE_4_MAX_DURATION.get();
            case 5 -> PHASE_5_MAX_DURATION.get();
            default -> 0;
        };
    }

    public static boolean hasFatigue(int phase) {
        return switch (phase) {
            case 1 -> PHASE_1_FATIGUE.get();
            case 2 -> PHASE_2_FATIGUE.get();
            case 3 -> PHASE_3_FATIGUE.get();
            case 4 -> PHASE_4_FATIGUE.get();
            case 5 -> PHASE_5_FATIGUE.get();
            default -> false;
        };
    }

    public static boolean hasHunger(int phase) {
        return switch (phase) {
            case 1 -> PHASE_1_HUNGER.get();
            case 2 -> PHASE_2_HUNGER.get();
            case 3 -> PHASE_3_HUNGER.get();
            case 4 -> PHASE_4_HUNGER.get();
            case 5 -> PHASE_5_HUNGER.get();
            default -> false;
        };
    }

    public static boolean hasBlindness(int phase) {
        return switch (phase) {
            case 1 -> PHASE_1_BLINDNESS.get();
            case 2 -> PHASE_2_BLINDNESS.get();
            case 3 -> PHASE_3_BLINDNESS.get();
            case 4 -> PHASE_4_BLINDNESS.get();
            case 5 -> PHASE_5_BLINDNESS.get();
            default -> false;
        };
    }

    public static boolean hasSlowness(int phase) {
        return switch (phase) {
            case 1 -> PHASE_1_SLOWNESS.get();
            case 2 -> PHASE_2_SLOWNESS.get();
            case 3 -> PHASE_3_SLOWNESS.get();
            case 4 -> PHASE_4_SLOWNESS.get();
            case 5 -> PHASE_5_SLOWNESS.get();
            default -> false;
        };
    }

    public static boolean hasWeakness(int phase) {
        return switch (phase) {
            case 1 -> PHASE_1_WEAKNESS.get();
            case 2 -> PHASE_2_WEAKNESS.get();
            case 3 -> PHASE_3_WEAKNESS.get();
            case 4 -> PHASE_4_WEAKNESS.get();
            case 5 -> PHASE_5_WEAKNESS.get();
            default -> false;
        };
    }

    public static int getAirConsumption(int phase) {
        return switch (phase) {
            case 2 -> AIR_CONSUMPTION_PHASE_2.get();
            case 3 -> AIR_CONSUMPTION_PHASE_3.get();
            case 4 -> AIR_CONSUMPTION_PHASE_4.get();
            case 5 -> AIR_CONSUMPTION_PHASE_5.get();
            default -> 0;
        };
    }
}