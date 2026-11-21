package net.creeperhost.chickens.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Basic regression tests for ChickenStats defaults and 1..10 validation. */
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
    void clampsValuesBelowOne() {
        ChickenStats stats = new ChickenStats(-1, 0, -2);
        assertEquals(1, stats.getGain());
        assertEquals(1, stats.getGrowth());
        assertEquals(1, stats.getStrength());
    }
}
