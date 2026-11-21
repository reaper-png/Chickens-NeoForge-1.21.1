package net.creeperhost.chickens.blockentities;

/**
 * Bounds persisted machine progress without changing the meaning of a
 * completed cycle. Both machines produce on the tick AFTER progress > 1000.
 */
public final class MachineProgressRecovery {
    private static final int BREEDER_MAX_PROGRESS = 1001;
    // Roost can advance by up to 50 points from progress=1000.
    private static final int ROOST_MAX_PROGRESS = 1050;

    private MachineProgressRecovery() {}

    public static int breeder(int savedProgress) {
        return Math.clamp(savedProgress, 0, BREEDER_MAX_PROGRESS);
    }

    public static int roost(int savedProgress) {
        return Math.clamp(savedProgress, 0, ROOST_MAX_PROGRESS);
    }
}
