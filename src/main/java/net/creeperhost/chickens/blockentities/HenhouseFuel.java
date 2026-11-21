package net.creeperhost.chickens.blockentities;

/** Original Henhouse fuel balance: one hay bale supports 100 collected items. */
public final class HenhouseFuel {
    public static final int ENERGY_PER_HAY = 100;
    private HenhouseFuel() {}

    public static int hayNeeded(int currentEnergy, int amount) {
        if (amount <= currentEnergy) return 0;
        return (amount - currentEnergy + ENERGY_PER_HAY - 1) / ENERGY_PER_HAY;
    }

    public static int dirtGenerated(int currentEnergy, int amount) {
        if (amount <= 0) return 0;
        int untilFirstDirt = currentEnergy == 0 ? ENERGY_PER_HAY : currentEnergy;
        if (amount < untilFirstDirt) return 0;
        return 1 + (amount - untilFirstDirt) / ENERGY_PER_HAY;
    }

    /**
     * Largest resource batch that fits the remaining dirt output capacity.
     * Includes items that do not yet complete the next 100-resource cycle.
     */
    public static int maxItemsForDirtRoom(int currentEnergy, int dirtRoom) {
        int untilFirstDirt = currentEnergy == 0 ? ENERGY_PER_HAY : currentEnergy;
        return untilFirstDirt - 1 + Math.max(0, dirtRoom) * ENERGY_PER_HAY;
    }

    public static int energyRemaining(int currentEnergy, int amount) {
        if (amount == 0) return currentEnergy;
        int fuel = currentEnergy + hayNeeded(currentEnergy, amount) * ENERGY_PER_HAY;
        return fuel - amount;
    }
}
