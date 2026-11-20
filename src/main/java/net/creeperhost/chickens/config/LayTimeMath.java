package net.creeperhost.chickens.config;

/** Pure laying interval calculation, without Minecraft or NeoForge runtime dependencies. */
public final class LayTimeMath {
    private LayTimeMath() {}

    /** Preserve the original minimum of one tick and truncation semantics. */
    public static int scaledTicks(int baseTicks, float coefficient) {
        return (int) Math.max(1.0f, baseTicks * coefficient);
    }
}
