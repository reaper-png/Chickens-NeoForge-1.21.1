package net.creeperhost.chickens.blockentities;

/**
 * Pure-Java rule for invalidating breeder progress after inventory changes.
 */
public final class BreederProgressPolicy {
    private BreederProgressPolicy() {}

    public static boolean invalidates(int slot, boolean validSeedsRemain, boolean pendingResult) {
        if (slot == 0 || slot == 1) return true;
        return slot == 2 && (!validSeedsRemain || pendingResult);
    }
}
