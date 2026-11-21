package net.creeperhost.chickens.registry;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

/** No FML/NeoForge classes are loaded by these tests. */
final class ChickenPedigreesTest {
    private static Map<String, ChickenSpecies> catalog() {
        Map<String, ChickenSpecies> catalog = new HashMap<>();
        for (ChickenSpecies type : ChickensRegistry.getItems()) catalog.put(type.id(), type);
        return catalog;
    }

    @Test
    void acceptsValidParentOverrides() {
        var pair = new ChickenPedigrees.Parents("flint_chicken", "log_chicken");
        var validated = ChickenPedigrees.validate(catalog(), Map.of("iron_chicken", pair));
        assertEquals(pair, validated.get("iron_chicken"));
    }

    @Test
    void rejectsUnknownParents() {
        assertThrows(IllegalArgumentException.class, () -> ChickenPedigrees.validate(
                catalog(), Map.of("iron_chicken",
                        new ChickenPedigrees.Parents("missing_chicken", "log_chicken"))));
    }

    @Test
    void rejectsDirectSelfCycles() {
        assertThrows(IllegalArgumentException.class, () -> ChickenPedigrees.validate(
                catalog(), Map.of("iron_chicken",
                        new ChickenPedigrees.Parents("iron_chicken", "log_chicken"))));
    }

    @Test
    void rejectsIndirectCycles() {
        assertThrows(IllegalArgumentException.class, () -> ChickenPedigrees.validate(
                catalog(), Map.of("flint_chicken",
                        new ChickenPedigrees.Parents("iron_chicken", "log_chicken"))));
    }

    @Test
    void rejectsIncompletePairs() {
        assertThrows(IllegalArgumentException.class, () -> ChickenPedigrees.validate(
                catalog(), Map.of("iron_chicken", new ChickenPedigrees.Parents("", "log_chicken"))));
    }

    @Test
    void supportsRemovingARecipe() {
        assertEquals(new ChickenPedigrees.Parents("", ""),
                ChickenPedigrees.validate(catalog(), Map.of("iron_chicken",
                        new ChickenPedigrees.Parents("", ""))).get("iron_chicken"));
    }
}
