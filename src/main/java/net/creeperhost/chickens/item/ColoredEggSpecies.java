package net.creeperhost.chickens.item;

import net.creeperhost.chickens.registry.ChickenSpecies;

/** A colored throw-egg belongs only to one of the original dye breeds. */
public final class ColoredEggSpecies {
    private ColoredEggSpecies() {}

    public static boolean isEligible(ChickenSpecies species) {
        return species != null && !species.dyeColor().isEmpty();
    }
}
