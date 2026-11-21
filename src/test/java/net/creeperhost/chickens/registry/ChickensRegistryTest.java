package net.creeperhost.chickens.registry;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;
import org.junit.jupiter.api.Test;

final class ChickensRegistryTest {
    @Test
    void catalogsExactlyTheOriginalFortySixBreeds() {
        assertEquals(46, ChickensRegistry.getItems().size());
        assertEquals(1, ChickensRegistry.getTier(ChickensRegistry.getByRegistryName("flint_chicken")));
        assertEquals(4, ChickensRegistry.getTier(ChickensRegistry.getByRegistryName("diamond_chicken")));
        assertSame(ChickensRegistry.getByRegistryName("diamond_chicken"),
                   ChickensRegistry.getByRegistryName("chickens:diamond_chicken"));
    }

    @Test
    void allPedigreesPointToRegisteredChickens() {
        for (ChickenSpecies species : ChickensRegistry.getItems()) {
            if (species.hasParents()) {
                assertNotNull(ChickensRegistry.getByRegistryName(species.parentA()));
                assertNotNull(ChickensRegistry.getByRegistryName(species.parentB()));
                assertTrue(ChickensRegistry.getTier(species) > 1);
            }
        }
    }

    @Test
    void flintAndLogCanBreedCoalWithoutSpawningUnrelatedSpecies() {
        var flint = ChickensRegistry.getByRegistryName("flint_chicken");
        var log = ChickensRegistry.getByRegistryName("log_chicken");
        var coal = ChickensRegistry.getByRegistryName("coal_chicken");
        Random random = new Random(42);
        int coalCount = 0;
        for (int i = 0; i < 1000; i++) {
            ChickenSpecies child = ChickensRegistry.getRandomChild(flint, log, random::nextInt, species -> true);
            assertTrue(child == flint || child == log || child == coal);
            if (child == coal) coalCount++;
        }
        assertTrue(coalCount > 100 && coalCount < 350);
    }

    @Test
    void disabledOffspringAreExcludedButParentsKeepTheirOriginalWeights() {
        var flint = ChickensRegistry.getByRegistryName("flint_chicken");
        var log = ChickensRegistry.getByRegistryName("log_chicken");
        var coal = ChickensRegistry.getByRegistryName("coal_chicken");
        Random random = new Random(42);
        for (int i = 0; i < 100; i++) {
            ChickenSpecies child = ChickensRegistry.getRandomChild(flint, log,
                    random::nextInt, species -> species != coal);
            assertTrue(child == flint || child == log);
        }
    }

    @Test
    void disabledParentStopsBreeding() {
        var flint = ChickensRegistry.getByRegistryName("flint_chicken");
        var log = ChickensRegistry.getByRegistryName("log_chicken");
        assertNull(ChickensRegistry.getRandomChild(flint, log, bound -> 0,
                species -> species != flint));
    }

    @Test
    void sameSpeciesParentsRetainTheOriginalDoubleWeight() {
        var smart = ChickensRegistry.getByRegistryName("smart_chicken");
        assertSame(smart, ChickensRegistry.getRandomChild(smart, smart, max -> 0, species -> true));
    }
}
