package net.creeperhost.chickens.config;

/** Validated immutable biome-spawn group settings without game dependencies. */
public record SpawnGroupSettings(int weight, int minCount, int maxCount) {
    public static SpawnGroupSettings of(int weight, int requestedMin, int requestedMax) {
        int min = Math.max(1, Math.min(64, requestedMin));
        int max = Math.max(min, Math.min(64, requestedMax));
        return new SpawnGroupSettings(Math.max(0, weight), min, max);
    }
}
