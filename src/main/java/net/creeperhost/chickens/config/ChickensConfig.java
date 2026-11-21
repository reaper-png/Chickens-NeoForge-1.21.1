package net.creeperhost.chickens.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * First NeoForge configuration options restored from original Chickens 1.0.32.
 * The original defaults include alwaysShowStats=false and spawning groups 3..5.
 *
 * More elaborate legacy JSON breed customization is a separate migration step.
 */
public final class ChickensConfig {
    public static final ModConfigSpec SPEC;
    private static final ModConfigSpec.BooleanValue ALWAYS_SHOW_STATS;
    private static final ModConfigSpec.IntValue SPAWN_WEIGHT;
    private static final ModConfigSpec.IntValue SPAWN_GROUP_MIN;
    private static final ModConfigSpec.IntValue SPAWN_GROUP_MAX;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("display");
        ALWAYS_SHOW_STATS = builder
                .comment("Show chicken Growth, Gain and Strength without using the Analyzer first.",
                        "Original Chickens default: false.")
                .define("alwaysShowStats", false);
        builder.pop();

        builder.push("spawning");
        SPAWN_WEIGHT = builder.comment(
                "Global relative spawn weight for all naturally spawning Chickens breeds.",
                "Original 1.0.32 default: 10. Zero disables spawn-list additions.")
                .defineInRange("weight", 10, 0, 1000);
        SPAWN_GROUP_MIN = builder.comment(
                "Minimum size of a natural Chickens spawn group.",
                "Original default: 3. Values above maxGroupSize are clamped at runtime.")
                .defineInRange("minGroupSize", 3, 1, 64);
        SPAWN_GROUP_MAX = builder.comment(
                "Maximum size of a natural Chickens spawn group. Original default: 5.")
                .defineInRange("maxGroupSize", 5, 1, 64);
        builder.pop();

        SPEC = builder.build();
    }

    private ChickensConfig() {}

    public static boolean alwaysShowStats() {
        return ALWAYS_SHOW_STATS.get();
    }

    public static SpawnGroupSettings spawnGroupSettings() {
        return SpawnGroupSettings.of(SPAWN_WEIGHT.get(), SPAWN_GROUP_MIN.get(), SPAWN_GROUP_MAX.get());
    }
}
