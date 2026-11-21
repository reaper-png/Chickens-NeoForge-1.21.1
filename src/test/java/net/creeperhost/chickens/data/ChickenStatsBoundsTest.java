package net.creeperhost.chickens.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

final class ChickenStatsBoundsTest {
    @Test
    void clampsInvalidLowValuesDuringConstruction() {
        ChickenStats stats = new ChickenStats(0, -50, Integer.MIN_VALUE);
        assertEquals(1, stats.getGain());
        assertEquals(1, stats.getGrowth());
        assertEquals(1, stats.getStrength());
    }

    @Test
    void clampsInvalidHighValuesDuringConstruction() {
        ChickenStats stats = new ChickenStats(11, 100, Integer.MAX_VALUE);
        assertEquals(10, stats.getGain());
        assertEquals(10, stats.getGrowth());
        assertEquals(10, stats.getStrength());
    }

    @Test
    void preservesOrdinaryValuesAndClampsSetterCalls() {
        ChickenStats stats = new ChickenStats(2, 7, 10);
        assertEquals(2, stats.getGain());
        assertEquals(7, stats.getGrowth());
        assertEquals(10, stats.getStrength());
        stats.setGain(-1);
        stats.setGrowth(12);
        stats.setStrength(0);
        assertEquals(1, stats.getGain());
        assertEquals(10, stats.getGrowth());
        assertEquals(1, stats.getStrength());
    }
}
