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

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("display");
        ALWAYS_SHOW_STATS = builder
                .comment("Show chicken Growth, Gain and Strength without using the Analyzer first.",
                        "Original Chickens default: false.")
                .define("alwaysShowStats", false);
        builder.pop();
        SPEC = builder.build();
    }

    private ChickensConfig() {}

    public static boolean alwaysShowStats() {
        return ALWAYS_SHOW_STATS.get();
    }
}
