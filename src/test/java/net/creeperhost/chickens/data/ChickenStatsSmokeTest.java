package net.creeperhost.chickens.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Basic regression tests for the legacy ChickenStats behavior. */
final class ChickenStatsSmokeTest {
    @Test
    void defaultsToOne() {
        ChickenStats stats = new ChickenStats();
        assertEquals(1, stats.getGain());
        assertEquals(1, stats.getGrowth());
        assertEquals(1, stats.getStrength());
    }

    @Test
    void clampsValuesAboveTen() {
        ChickenStats stats = new ChickenStats(11, 14, 12);
        assertEquals(10, stats.getGain());
        assertEquals(10, stats.getGrowth());
        assertEquals(10, stats.getStrength());
    }

    @Test
    void preservesLegacyLowerBoundBehavior() {
        ChickenStats stats = new ChickenStats(-1, 0, -2);
        assertEquals(-1, stats.getGain());
        assertEquals(0, stats.getGrowth());
        assertEquals(-2, stats.getStrength());
    }
}
