package net.creeperhost.chickens.blockentities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HenhouseFuelTest {
    @Test void newHayProducesOneHundredItemsAndOneDirt() {
        assertEquals(1, HenhouseFuel.hayNeeded(0, 1));
        assertEquals(99, HenhouseFuel.energyRemaining(0, 1));
        assertEquals(0, HenhouseFuel.dirtGenerated(0, 1));
        assertEquals(1, HenhouseFuel.hayNeeded(0, 100));
        assertEquals(0, HenhouseFuel.energyRemaining(0, 100));
        assertEquals(1, HenhouseFuel.dirtGenerated(0, 100));
    }
    @Test void crossingEnergyBoundaryConsumesOnlyRequiredHay() {
        assertEquals(0, HenhouseFuel.hayNeeded(42, 42));
        assertEquals(0, HenhouseFuel.energyRemaining(42, 42));
        assertEquals(1, HenhouseFuel.dirtGenerated(42, 42));
        assertEquals(1, HenhouseFuel.hayNeeded(42, 50));
        assertEquals(92, HenhouseFuel.energyRemaining(42, 50));
        assertEquals(1, HenhouseFuel.dirtGenerated(42, 50));
    }
    @Test void largeCollectionNeverDuplicatesByproducts() {
        assertEquals(3, HenhouseFuel.hayNeeded(0, 250));
        assertEquals(50, HenhouseFuel.energyRemaining(0, 250));
        assertEquals(2, HenhouseFuel.dirtGenerated(0, 250));
        assertEquals(3, HenhouseFuel.dirtGenerated(90, 300));
    }
}
