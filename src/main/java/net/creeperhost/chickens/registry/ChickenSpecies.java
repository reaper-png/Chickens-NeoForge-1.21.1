package net.creeperhost.chickens.registry;

/** Immutable default breed definition from CreeperHost Chickens 1.0.32. */
public record ChickenSpecies(
        String id,
        String product,
        String parentA,
        String parentB,
        SpawnType spawnType,
        int backgroundColor,
        int foregroundColor,
        String dyeColor) {
    public enum SpawnType { NONE, NORMAL, HELL, SNOW }
    public boolean hasParents() { return !parentA.isBlank() && !parentB.isBlank(); }
    public boolean isChildOf(ChickenSpecies first, ChickenSpecies second) {
        return hasParents() &&
                ((parentA.equals(first.id()) && parentB.equals(second.id()))
                || (parentA.equals(second.id()) && parentB.equals(first.id())));
    }
}
