package net.creeperhost.chickens.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class SpeciesLayConfigTest {
    @Test
    void originalDefaultLeavesBaseTimeUnchanged() {
        assertEquals(6000, SpeciesLayConfig.scaledTicks(6000, 1.0f));
    }

    @Test
    void coefficientScalesOriginalBaseTime() {
        assertEquals(3000, SpeciesLayConfig.scaledTicks(6000, 0.5f));
        assertEquals(12000, SpeciesLayConfig.scaledTicks(6000, 2.0f));
    }

    @Test
    void minimumTimeIsOneTick() {
        assertEquals(1, SpeciesLayConfig.scaledTicks(0, 0.01f));
    }
}
