package net.creeperhost.chickens.config;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

final class SpawnChanceTest {
    @Test
    void defaultChanceAlwaysAllowsSpawnWithoutDrawingRandom() {
        assertTrue(SpawnChance.allows(100, bound -> { throw new AssertionError("should not roll"); }));
    }

    @Test
    void zeroChanceBlocksSpawnWithoutDrawingRandom() {
        assertFalse(SpawnChance.allows(0, bound -> { throw new AssertionError("should not roll"); }));
    }

    @Test
    void intermediateChanceUsesOnePercentRoll() {
        assertTrue(SpawnChance.allows(25, bound -> { assertEquals(100, bound); return 24; }));
        assertFalse(SpawnChance.allows(25, bound -> { assertEquals(100, bound); return 25; }));
    }
}
