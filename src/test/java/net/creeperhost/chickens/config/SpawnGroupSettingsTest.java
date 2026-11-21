package net.creeperhost.chickens.config;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

final class SpawnGroupSettingsTest {
    @Test
    void legacyDefaultsArePreserved() {
        assertEquals(new SpawnGroupSettings(10, 3, 5), SpawnGroupSettings.of(10, 3, 5));
    }

    @Test
    void groupMaximumCannotBeSmallerThanMinimum() {
        assertEquals(new SpawnGroupSettings(10, 6, 6), SpawnGroupSettings.of(10, 6, 2));
    }

    @Test
    void invalidSizesStayInSafeBounds() {
        assertEquals(new SpawnGroupSettings(0, 1, 64), SpawnGroupSettings.of(-3, -1, 100));
    }

    @Test
    void zeroWeightDisablesAdditionWithoutChangingGroupBounds() {
        assertEquals(new SpawnGroupSettings(0, 3, 5), SpawnGroupSettings.of(0, 3, 5));
    }
}
