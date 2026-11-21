package net.creeperhost.chickens.registry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

/** Pure registry tests avoid initializing NeoForge's config runtime. */
final class BreedingTierMemoizationTest {
    @Test
    void retainsOriginalWeightedSelectionAndOneRoll() {
        ChickenSpecies flint = ChickensRegistry.getByRegistryName("flint_chicken");
        ChickenSpecies white = ChickensRegistry.getByRegistryName("white_chicken");
        assertNotNull(flint);
        assertNotNull(white);

        // Both parents are tier 1 (weight 2 each); iron is tier 2 (weight 1).
        assertEquals("flint_chicken", select(flint, white, 0));
        assertEquals("flint_chicken", select(flint, white, 1));
        assertEquals("white_chicken", select(flint, white, 2));
        assertEquals("white_chicken", select(flint, white, 3));
        assertEquals("iron_chicken", select(flint, white, 4));
    }

    @Test
    void doesNotChangeTierValues() {
        assertEquals(1, ChickensRegistry.getTier(ChickensRegistry.getByRegistryName("flint_chicken")));
        assertEquals(1, ChickensRegistry.getTier(ChickensRegistry.getByRegistryName("white_chicken")));
        assertEquals(2, ChickensRegistry.getTier(ChickensRegistry.getByRegistryName("iron_chicken")));
    }

    private static String select(ChickenSpecies first, ChickenSpecies second, int requestedRoll) {
        AtomicInteger calls = new AtomicInteger();
        ChickenSpecies child = ChickensRegistry.getRandomChild(first, second, bound -> {
            assertEquals(5, bound);
            assertEquals(1, calls.incrementAndGet());
            return requestedRoll;
        }, species -> true);
        assertEquals(1, calls.get());
        return child.id();
    }
}
