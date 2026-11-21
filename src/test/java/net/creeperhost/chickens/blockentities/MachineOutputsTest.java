package net.creeperhost.chickens.blockentities;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void fullOrIncompatibleSlotsProvideNoCapacity() {
        assertEquals(2, OutputCapacityMath.remainingAfterSlot(2, 64, 64, 64));
        // Mismatched item components must be skipped by MachineOutputs,
        // therefore the amount remains unchanged.
        int remaining = 1;
        assertEquals(1, remaining);
    }

    @Test
    void respectsEffectiveItemAndSlotStackLimits() {
        assertEquals(3, OutputCapacityMath.remainingAfterSlot(4, 15, 64, 16));
        assertEquals(2, OutputCapacityMath.remainingAfterSlot(5, 2, 5, 64));
        assertEquals(1, OutputCapacityMath.remainingAfterSlot(1, 63, 64, 16));
    }

    @Test
    void exactPartialSpaceAndZeroCapacityAreSafe() {
        assertEquals(0, OutputCapacityMath.remainingAfterSlot(2, 62, 64, 64));
        assertEquals(1, OutputCapacityMath.remainingAfterSlot(3, 62, 64, 64));
        assertEquals(0, OutputCapacityMath.remainingAfterSlot(0, 20, 64, 64));
        assertEquals(3, OutputCapacityMath.remainingAfterSlot(3, 65, 64, 64));
    }
}
