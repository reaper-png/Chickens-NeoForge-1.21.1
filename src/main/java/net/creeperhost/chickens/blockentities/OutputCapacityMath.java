package net.creeperhost.chickens.blockentities;

/**
 * Pure-Java capacity calculation shared by machine output checks.
 * Compatibility is checked by the caller before an occupied slot is counted.
 */
public final class OutputCapacityMath {
    private OutputCapacityMath() {}

    public static int remainingAfterSlot(int remaining, int existingCount,
                                         int slotLimit, int itemMaxStackSize) {
        if (remaining <= 0) return 0;
        int capacity = Math.min(slotLimit, itemMaxStackSize) - existingCount;
        return remaining - Math.min(remaining, Math.max(0, capacity));
    }
}
