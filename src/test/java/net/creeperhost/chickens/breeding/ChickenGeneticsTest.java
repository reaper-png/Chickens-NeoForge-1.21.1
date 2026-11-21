package net.creeperhost.chickens.breeding;

import net.creeperhost.chickens.data.ChickenStats;
import net.creeperhost.chickens.registry.ChickensRegistry;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

final class ChickenGeneticsTest {
    @Test
    void mutationChangesStatsWithinLegacyLimits() {
        var coal = ChickensRegistry.getByRegistryName("coal_chicken");
        var a = new ChickenStats(7, 8, 10);
        var b = new ChickenStats(9, 9, 10);
        var child = ChickenGenetics.offspring(coal, coal, coal, a, b, max -> max - 1);
        assertEquals(10, child.getGain());
        assertEquals(10, child.getGrowth());
        assertEquals(10, child.getStrength());
    }

    @Test
    void parentsPassStatsWhenChildIsOneOfThem() {
        var coal = ChickensRegistry.getByRegistryName("coal_chicken");
        var flint = ChickensRegistry.getByRegistryName("flint_chicken");
        var result = ChickenGenetics.offspring(coal, flint, flint,
                new ChickenStats(8, 3, 4), new ChickenStats(2, 9, 7), bound -> 0);
        assertEquals(2, result.getGain());
        assertEquals(9, result.getGrowth());
        assertEquals(7, result.getStrength());
    }

    @Test
    void newSpeciesBeginWithDefaultStats() {
        var coal = ChickensRegistry.getByRegistryName("coal_chicken");
        var flint = ChickensRegistry.getByRegistryName("flint_chicken");
        var log = ChickensRegistry.getByRegistryName("log_chicken");
        var result = ChickenGenetics.offspring(flint, log, coal,
                new ChickenStats(10, 10, 10), new ChickenStats(10, 10, 10), bound -> 0);
        assertEquals(1, result.getGain());
        assertEquals(1, result.getGrowth());
        assertEquals(1, result.getStrength());
    }
    @Test
    void sameSpeciesBreedingUsesExactlyThreeMutationRolls() {
        var coal = ChickensRegistry.getByRegistryName("coal_chicken");
        var rolls = new java.util.concurrent.atomic.AtomicInteger();
        ChickenStats child = ChickenGenetics.offspring(coal, coal, coal,
                new ChickenStats(2, 3, 4), new ChickenStats(4, 5, 6),
                bound -> {
                    assertEquals(2, bound);
                    rolls.incrementAndGet();
                    return 0;
                });
        assertEquals(3, rolls.get());
        assertEquals(4, child.getGain());
        assertEquals(5, child.getGrowth());
        assertEquals(6, child.getStrength());
    }

    @Test
    void differentSpeciesParentInheritanceUsesNoMutationRolls() {
        var coal = ChickensRegistry.getByRegistryName("coal_chicken");
        var flint = ChickensRegistry.getByRegistryName("flint_chicken");
        ChickenStats child = ChickenGenetics.offspring(coal, flint, coal,
                new ChickenStats(6, 7, 8), new ChickenStats(2, 3, 4),
                bound -> { throw new AssertionError("Different-species inheritance must not roll mutations"); });
        assertEquals(6, child.getGain());
        assertEquals(7, child.getGrowth());
        assertEquals(8, child.getStrength());
    }

    @Test
    void invalidParentStatsAreBoundedBeforeInheritance() {
        var coal = ChickensRegistry.getByRegistryName("coal_chicken");
        var flint = ChickensRegistry.getByRegistryName("flint_chicken");
        ChickenStats child = ChickenGenetics.offspring(coal, flint, coal,
                new ChickenStats(-10, 0, 100), new ChickenStats(2, 3, 4),
                bound -> { throw new AssertionError("Parent copy must not use RNG"); });
        assertEquals(1, child.getGain());
        assertEquals(1, child.getGrowth());
        assertEquals(10, child.getStrength());
    }

}
