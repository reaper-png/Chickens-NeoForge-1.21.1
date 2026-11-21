package net.creeperhost.chickens.blockentities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Cross-checks resource, fuel and dirt conservation across split collections. */
final class HenhouseFuelInvariantTest {
    @Test
    void splittingACollectionPreservesEnergyAndDirt() {
        for (int initialEnergy = 0; initialEnergy <= 99; initialEnergy++) {
            for (int first = 0; first <= 100; first++) {
                for (int second = 0; second <= 100; second += 5) {
                    int afterFirst = HenhouseFuel.energyRemaining(initialEnergy, first);
                    int afterSecond = HenhouseFuel.energyRemaining(afterFirst, second);
                    int allAtOnce = HenhouseFuel.energyRemaining(initialEnergy, first + second);
                    assertEquals(allAtOnce, afterSecond, "Energy diverged after splitting");

                    int dirtSplit = HenhouseFuel.dirtGenerated(initialEnergy, first)
                            + HenhouseFuel.dirtGenerated(afterFirst, second);
                    int dirtAll = HenhouseFuel.dirtGenerated(initialEnergy, first + second);
                    assertEquals(dirtAll, dirtSplit, "Dirt diverged after splitting");

                    int haySplit = HenhouseFuel.hayNeeded(initialEnergy, first)
                            + HenhouseFuel.hayNeeded(afterFirst, second);
                    int hayAll = HenhouseFuel.hayNeeded(initialEnergy, first + second);
                    assertEquals(hayAll, haySplit, "Hay diverged after splitting");
                    assertTrue(afterSecond >= 0 && afterSecond < 100);
                }
            }
        }
    }
}
