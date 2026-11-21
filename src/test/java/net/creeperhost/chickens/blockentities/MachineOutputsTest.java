package net.creeperhost.chickens.blockentities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Pure Java tests: the Gradle test source set does not expose the Minecraft
 * and NeoForge runtime classes required to instantiate ItemStackHandler.
 */
final class MachineOutputsTest {
    @Test
    void usesMatchingStackSpaceBeforeAnEmptySlot() {
        int remaining = OutputCapacityMath.remainingAfterSlot(8, 60, 64, 64);
        assertEquals(4, remaining);
        remaining = OutputCapacityMath.remainingAfterSlot(remaining, 0, 64, 64);
        assertEquals(0, remaining);
    }

    @Test
    void fullSlotsProvideNoCapacity() {
        assertEquals(2, OutputCapacityMath.remainingAfterSlot(2, 64, 64, 64));
        // ItemStack component identity is tested only with a NeoForge-aware
        // integration environment. This pure-Java helper checks capacity. 
    }

    @Test
    void respectsEffectiveItemAndSlotStackLimits() {
        assertEquals(3, OutputCapacityMath.remainingAfterSlot(4, 15, 64, 16));
        assertEquals(2, OutputCapacityMath.remainingAfterSlot(5, 2, 5, 64));
        assertEquals(1, OutputCapacityMath.remainingAfterSlot(1, 63, 64, 16));
    }

    @Test
    void capacityNeverCreatesOrLosesItemsAcrossSlotLimits() {
        for (int slotLimit = 1; slotLimit <= 64; slotLimit++) {
            for (int itemMax = 1; itemMax <= 64; itemMax++) {
                int effectiveLimit = Math.min(slotLimit, itemMax);
                for (int count = 0; count <= effectiveLimit + 1; count++) {
                    int capacity = Math.max(0, effectiveLimit - count);
                    for (int requested : new int[] { 0, 1, 2, effectiveLimit, effectiveLimit + 1, 128 }) {
                        int remaining = OutputCapacityMath.remainingAfterSlot(
                                requested, count, slotLimit, itemMax);
                        int expected = Math.max(0, requested - capacity);
                        assertEquals(expected, remaining,
                                "slotLimit=" + slotLimit + ", itemMax=" + itemMax
                                        + ", count=" + count + ", requested=" + requested);
                        assertTrue(remaining >= 0 && remaining <= requested);
                    }
                }
            }
        }
    }

    @Test
    void exactPartialSpaceAndZeroCapacityAreSafe() {
        assertEquals(0, OutputCapacityMath.remainingAfterSlot(2, 62, 64, 64));
        assertEquals(1, OutputCapacityMath.remainingAfterSlot(3, 62, 64, 64));
        assertEquals(0, OutputCapacityMath.remainingAfterSlot(0, 20, 64, 64));
        assertEquals(3, OutputCapacityMath.remainingAfterSlot(3, 65, 64, 64));
    }
}
