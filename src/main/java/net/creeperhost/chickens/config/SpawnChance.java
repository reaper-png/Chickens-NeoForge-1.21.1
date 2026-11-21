package net.creeperhost.chickens.config;

import java.util.function.IntUnaryOperator;

/**
 * Pure percentage gate for natural spawning. No Minecraft/NeoForge dependencies.
 * 100 preserves the existing spawn predicates; 0 prevents natural spawning.
 */
public final class SpawnChance {
    private SpawnChance() {}

    public static boolean allows(int percent, IntUnaryOperator randomBounded) {
        if (percent <= 0) return false;
        if (percent >= 100) return true;
        return randomBounded.applyAsInt(100) < percent;
    }
}
