package net.creeperhost.chickens.entity;

/** Pure calculation for the original 2400-tick resource-laying display bands. */
public final class LayProgressMath {
    private static final int TICKS_PER_BAND = 2400;

    private LayProgressMath() {}

    public static int remainingBand(int remainingTicks) {
        return remainingTicks / TICKS_PER_BAND;
    }

    public static boolean needsSync(int displayedBand, int remainingBand) {
        return displayedBand != remainingBand;
    }
}
