package com.BreadRes.desertstormwarming.logic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class SandstormUtils {

    public static boolean isSandstormBiome(ResourceLocation biomeKey) {
        if (biomeKey == null) return false;
        String path = biomeKey.getPath().toLowerCase();

        // Vanilla desert biomes
        if (path.contains("desert")) return true;
        if (path.contains("badlands")) return true;
        if (path.contains("mesa")) return true;

        // Biomes O' Plenty
        if (path.contains("wasteland")) return true;
        if (path.contains("volcanic_plains")) return true;
        if (path.contains("lush_desert")) return true;
        if (path.contains("cold_desert")) return true;
        if (path.contains("dryland")) return true;
        if (path.contains("scrubland")) return true;
        if (path.contains("shrubland")) return true;
        if (path.contains("rocky_shrubland")) return true;
        if (path.contains("tundra")) return true;
        if (path.contains("dead_forest")) return true;
        if (path.contains("old_growth_dead_forest")) return true;

        // Terralith
        if (path.contains("arid")) return true;
        if (path.contains("savanna_badlands")) return true;
        if (path.contains("red_desert")) return true;

        // Other mods
        if (path.contains("ash")) return true;
        if (path.contains("barren")) return true;
        if (path.contains("dry")) return true;

        return false;
    }

    public static float getDesertProximity(Player player) {
        Level level = player.level();
        BlockPos center = player.blockPosition();

        int radius = 64;
        float maxWeight = 0f;

        for (int dx = -radius; dx <= radius; dx += 8) {
            for (int dz = -radius; dz <= radius; dz += 8) {
                BlockPos pos = center.offset(dx, 0, dz);
                Biome biome = level.getBiome(pos).value();
                ResourceLocation key = level.registryAccess().registryOrThrow(Registries.BIOME).getKey(biome);

                if (isSandstormBiome(key)) {
                    double dist = Math.sqrt(dx * dx + dz * dz);
                    float weight = Mth.clamp(1.0f - (float)dist / radius, 0f, 1f);
                    if (weight > maxWeight) maxWeight = weight;
                }
            }
        }

        return maxWeight;
    }

    public static BlockPos findClosestDesert(Player player, int radius) {
        BlockPos center = player.blockPosition();
        Level level = player.level();

        for (int dx = -radius; dx <= radius; dx += 16) {
            for (int dz = -radius; dz <= radius; dz += 16) {
                BlockPos check = center.offset(dx, 0, dz);
                Biome b = level.getBiome(check).value();
                ResourceLocation biomeKey = level.registryAccess().registryOrThrow(Registries.BIOME).getKey(b);

                if (isSandstormBiome(biomeKey)) {
                    return check;
                }
            }
        }
        return null;
    }
}