package net.creeperhost.chickens.blockentities;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

final class HenhouseRangeTest {
    @Test
    void respectsFractionalChickenPositionAtRangeEdge() {
        // Chicken near the west edge: the eastmost block center is too far.
        assertFalse(HenhouseRange.contains(0.1, 0.5, 0.5, 5, 0, 0, 5));
        assertTrue(HenhouseRange.contains(0.9, 0.5, 0.5, 5, 0, 0, 5));
        // Chicken near the east edge: the westmost block center is too far.
        assertFalse(HenhouseRange.contains(0.9, 0.5, 0.5, -5, 0, 0, 5));
        assertTrue(HenhouseRange.contains(0.1, 0.5, 0.5, -5, 0, 0, 5));
    }

    @Test
    void allThreeAxesMustBeWithinTheOriginalCube() {
        assertTrue(HenhouseRange.contains(0.5, 0.5, 0.5, 5, 5, 5, 5));
        assertFalse(HenhouseRange.contains(0.5, 0.5, 0.5, 6, 0, 0, 5));
    }

    @Test
    void usesActualChickenCoordinatesForNearestHouse() {
        double east = HenhouseRange.distanceSquared(0.9, 0.5, 0.5, 1, 0, 0);
        double west = HenhouseRange.distanceSquared(0.9, 0.5, 0.5, -1, 0, 0);
        assertTrue(east < west);
    }
}
