package net.creeperhost.chickens.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.creeperhost.chickens.registry.ChickenSpecies;
import net.creeperhost.chickens.registry.ChickensRegistry;
import org.junit.jupiter.api.Test;

final class ColoredEggSpeciesTest {
    @Test
    void exactlySixteenLegacyDyeBreedsCanHaveColoredEggs() {
        long permitted = ChickensRegistry.getItems().stream()
                .filter(ColoredEggSpecies::isEligible)
                .count();
        assertEquals(16, permitted);
    }

    @Test
    void rejectsNullAndNonDyeBreeds() {
        assertFalse(ColoredEggSpecies.isEligible(null));
        assertFalse(ColoredEggSpecies.isEligible(
                ChickensRegistry.getByRegistryName("iron_chicken")));
        assertTrue(ColoredEggSpecies.isEligible(
                ChickensRegistry.getByRegistryName("red_chicken")));
    }
}
