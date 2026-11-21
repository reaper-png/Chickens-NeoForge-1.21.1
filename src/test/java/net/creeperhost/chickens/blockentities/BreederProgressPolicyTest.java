package net.creeperhost.chickens.blockentities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

final class BreederProgressPolicyTest {
    @Test
    void refillingValidSeedsDoesNotDiscardInProgressCycle() {
        assertFalse(BreederProgressPolicy.invalidates(2, true, false));
    }

    @Test
    void losingTheLastSeedInvalidatesProgress() {
        assertTrue(BreederProgressPolicy.invalidates(2, false, false));
    }

    @Test
    void replacingSeedsAfterOffspringWasChosenInvalidatesPendingResult() {
        assertTrue(BreederProgressPolicy.invalidates(2, true, true));
    }

    @Test
    void changingEitherParentAlwaysInvalidatesProgress() {
        assertTrue(BreederProgressPolicy.invalidates(0, true, false));
        assertTrue(BreederProgressPolicy.invalidates(1, true, false));
    }

    @Test
    void OutputChangesNeverInvalidateProgress() {
        for (int outputSlot = 3; outputSlot < 6; outputSlot++) {
            assertFalse(BreederProgressPolicy.invalidates(outputSlot, true, true));
        }
    }
}
