package net.creeperhost.chickens.registry;

import java.util.ArrayList;
import net.creeperhost.chickens.config.SpeciesLayConfig;
import net.creeperhost.chickens.config.LayTimeMath;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;

/**
 * The 46 Chickens 1.0.32 breeds and their original two-parent relationships.
 * Registry data is immutable; weighted breeding is isolated from Minecraft APIs.
 */
public final class ChickensRegistry {
    private static final Map<String, ChickenSpecies> TYPES = buildTypes();
    private static volatile Map<String, ChickenPedigrees.Parents> PARENT_OVERRIDES = Map.of();
    private ChickensRegistry() {}

    private static Map<String, ChickenSpecies> buildTypes() {
        Map<String, ChickenSpecies> map = new LinkedHashMap<>();
        add(map, "smart_chicken", "minecraft:egg", "", "", ChickenSpecies.SpawnType.NONE, 0xFFFFFF, 0xFFFF00, "");
        add(map, "white_chicken", "minecraft:white_dye", "", "", ChickenSpecies.SpawnType.NORMAL, 0xF2F2F2, 0x000000, "white");
        add(map, "yellow_chicken", "minecraft:yellow_dye", "", "", ChickenSpecies.SpawnType.NONE, 0xF2F2F2, 0x000000, "yellow");
        add(map, "blue_chicken", "minecraft:lapis_lazuli", "", "", ChickenSpecies.SpawnType.NONE, 0xF2F2F2, 0x000000, "blue");
        add(map, "green_chicken", "minecraft:green_dye", "", "", ChickenSpecies.SpawnType.NONE, 0xF2F2F2, 0x000000, "green");
        add(map, "red_chicken", "minecraft:red_dye", "", "", ChickenSpecies.SpawnType.NONE, 0xF2F2F2, 0x000000, "red");
        add(map, "black_chicken", "minecraft:black_dye", "", "", ChickenSpecies.SpawnType.NONE, 0xF2F2F2, 0x000000, "black");
        add(map, "pink_chicken", "minecraft:pink_dye", "red_chicken", "white_chicken", ChickenSpecies.SpawnType.NONE, 0xF2F2F2, 0x000000, "pink");
        add(map, "purple_chicken", "minecraft:purple_dye", "blue_chicken", "red_chicken", ChickenSpecies.SpawnType.NONE, 0xF2F2F2, 0x000000, "purple");
        add(map, "orange_chicken", "minecraft:orange_dye", "red_chicken", "yellow_chicken", ChickenSpecies.SpawnType.NONE, 0xF2F2F2, 0x000000, "orange");
        add(map, "light_blue_chicken", "minecraft:light_blue_dye", "white_chicken", "blue_chicken", ChickenSpecies.SpawnType.NONE, 0xF2F2F2, 0x000000, "light_blue");
        add(map, "lime_chicken", "minecraft:lime_dye", "green_chicken", "white_chicken", ChickenSpecies.SpawnType.NONE, 0xF2F2F2, 0x000000, "lime");
        add(map, "gray_chicken", "minecraft:gray_dye", "black_chicken", "white_chicken", ChickenSpecies.SpawnType.NONE, 0xF2F2F2, 0x000000, "gray");
        add(map, "cyan_chicken", "minecraft:cyan_dye", "blue_chicken", "green_chicken", ChickenSpecies.SpawnType.NONE, 0xF2F2F2, 0x000000, "cyan");
        add(map, "light_gray_chicken", "minecraft:light_gray_dye", "gray_chicken", "white_chicken", ChickenSpecies.SpawnType.NONE, 0xF2F2F2, 0x000000, "light_gray");
        add(map, "magenta_chicken", "minecraft:magenta_dye", "purple_chicken", "pink_chicken", ChickenSpecies.SpawnType.NONE, 0xF2F2F2, 0x000000, "magenta");
        add(map, "flint_chicken", "minecraft:flint", "", "", ChickenSpecies.SpawnType.NORMAL, 0x6B6B47, 0xA3A375, "");
        add(map, "quartz_chicken", "minecraft:quartz", "", "", ChickenSpecies.SpawnType.HELL, 0x4D0000, 0x1A0000, "");
        add(map, "log_chicken", "minecraft:oak_log", "", "", ChickenSpecies.SpawnType.NORMAL, 0x98846D, 0x528358, "");
        add(map, "sand_chicken", "minecraft:sand", "", "", ChickenSpecies.SpawnType.NORMAL, 0xECE5B1, 0xA7A06C, "");
        add(map, "string_chicken", "minecraft:string", "black_chicken", "log_chicken", ChickenSpecies.SpawnType.NORMAL, 0x331A00, 0x800000, "");
        add(map, "glowstone_chicken", "minecraft:glowstone_dust", "quartz_chicken", "yellow_chicken", ChickenSpecies.SpawnType.NORMAL, 0xFFFF66, 0xFFFF00, "");
        add(map, "gunpowder_chicken", "minecraft:gunpowder", "sand_chicken", "flint_chicken", ChickenSpecies.SpawnType.NORMAL, 0x999999, 0x404040, "");
        add(map, "redstone_chicken", "minecraft:redstone", "red_chicken", "sand_chicken", ChickenSpecies.SpawnType.NORMAL, 0xE60000, 0x800000, "");
        add(map, "glass_chicken", "minecraft:glass", "quartz_chicken", "redstone_chicken", ChickenSpecies.SpawnType.NORMAL, 0xFFFFFF, 0xEEEEFF, "");
        add(map, "iron_chicken", "minecraft:iron_ingot", "flint_chicken", "white_chicken", ChickenSpecies.SpawnType.NORMAL, 0xFFFFCC, 0xFFCCCC, "");
        add(map, "coal_chicken", "minecraft:coal", "flint_chicken", "log_chicken", ChickenSpecies.SpawnType.NORMAL, 0x262626, 0x000000, "");
        add(map, "brown_chicken", "minecraft:brown_dye", "red_chicken", "green_chicken", ChickenSpecies.SpawnType.NONE, 0xF2F2F2, 0x000000, "brown");
        add(map, "gold_chicken", "minecraft:gold_nugget", "iron_chicken", "yellow_chicken", ChickenSpecies.SpawnType.NORMAL, 0xCCCC00, 0xFFFF80, "");
        add(map, "snowball_chicken", "minecraft:snowball", "blue_chicken", "log_chicken", ChickenSpecies.SpawnType.SNOW, 0x33BBFF, 0x0088CC, "");
        add(map, "water_chicken", "chickens:liquid_egg|minecraft:water", "gunpowder_chicken", "snowball_chicken", ChickenSpecies.SpawnType.NORMAL, 0x000099, 0x8080FF, "");
        add(map, "lava_chicken", "chickens:liquid_egg|minecraft:lava", "coal_chicken", "quartz_chicken", ChickenSpecies.SpawnType.HELL, 0xCC3300, 0xFFFF00, "");
        add(map, "clay_chicken", "minecraft:clay_ball", "snowball_chicken", "sand_chicken", ChickenSpecies.SpawnType.NORMAL, 0xCCCCCC, 0xBFBFBF, "");
        add(map, "leather_chicken", "minecraft:leather", "string_chicken", "brown_chicken", ChickenSpecies.SpawnType.NORMAL, 0xA7A06C, 0x919191, "");
        add(map, "netherwart_chicken", "minecraft:nether_wart", "brown_chicken", "glowstone_chicken", ChickenSpecies.SpawnType.NORMAL, 0x800000, 0x331A00, "");
        add(map, "diamond_chicken", "minecraft:diamond", "glass_chicken", "gold_chicken", ChickenSpecies.SpawnType.NORMAL, 0x99CCFF, 0xE6F2FF, "");
        add(map, "blaze_chicken", "minecraft:blaze_rod", "gold_chicken", "lava_chicken", ChickenSpecies.SpawnType.NORMAL, 0xFFFF66, 0xFF3300, "");
        add(map, "slime_chicken", "minecraft:slime_ball", "clay_chicken", "green_chicken", ChickenSpecies.SpawnType.NORMAL, 0x009933, 0x99FFBB, "");
        add(map, "ender_chicken", "minecraft:ender_pearl", "diamond_chicken", "netherwart_chicken", ChickenSpecies.SpawnType.NORMAL, 0x001A00, 0x001A33, "");
        add(map, "ghast_chicken", "minecraft:ghast_tear", "white_chicken", "blaze_chicken", ChickenSpecies.SpawnType.NORMAL, 0xFFFFCC, 0xFFFFFF, "");
        add(map, "emerald_chicken", "minecraft:emerald", "diamond_chicken", "green_chicken", ChickenSpecies.SpawnType.NORMAL, 0x00CC00, 0x003300, "");
        add(map, "magma_chicken", "minecraft:magma_cream", "slime_chicken", "blaze_chicken", ChickenSpecies.SpawnType.NORMAL, 0x1A0500, 0x000000, "");
        add(map, "pshard_chicken", "minecraft:prismarine_shard", "water_chicken", "blue_chicken", ChickenSpecies.SpawnType.NORMAL, 0x43806E, 0x9FCBBC, "");
        add(map, "pcrystal_chicken", "minecraft:prismarine_crystals", "water_chicken", "emerald_chicken", ChickenSpecies.SpawnType.NORMAL, 0x4E6961, 0xDFE9DC, "");
        add(map, "obsidian_chicken", "minecraft:obsidian", "water_chicken", "lava_chicken", ChickenSpecies.SpawnType.NORMAL, 0x08080E, 0x463A60, "");
        add(map, "soulsand_chicken", "minecraft:soul_sand", "", "", ChickenSpecies.SpawnType.HELL, 0x453125, 0xD52F08, "");
        for (ChickenSpecies type : map.values()) {
            if (type.hasParents()) {
                if (!map.containsKey(type.parentA()) || !map.containsKey(type.parentB())) {
                    throw new IllegalStateException("Missing parent for " + type.id());
                }
            }
        }
        return Collections.unmodifiableMap(map);
    }

    private static void add(Map<String, ChickenSpecies> map, String id, String product, String a,
            String b, ChickenSpecies.SpawnType spawnType, int bg, int fg, String dye) {
        if (map.putIfAbsent(id, new ChickenSpecies(id, product, a, b, spawnType, bg, fg, dye)) != null) {
            throw new IllegalStateException("Duplicate chicken ID: " + id);
        }
    }

    public static Collection<ChickenSpecies> getItems() { return TYPES.values(); }

    /** Atomically installs a validated pedigree map during mod common setup. */
    public static void installParentOverrides(Map<String, ChickenPedigrees.Parents> candidates) {
        PARENT_OVERRIDES = ChickenPedigrees.validate(TYPES, Map.copyOf(candidates));
    }

    public static ChickenPedigrees.Parents parentsOf(ChickenSpecies type) {
        return PARENT_OVERRIDES.getOrDefault(type.id(),
                new ChickenPedigrees.Parents(type.parentA(), type.parentB()));
    }

    public static boolean isChildOf(ChickenSpecies child, ChickenSpecies a, ChickenSpecies b) {
        var parents = parentsOf(child);
        return parents.hasParents()
                && ((parents.first().equals(a.id()) && parents.second().equals(b.id()))
                || (parents.first().equals(b.id()) && parents.second().equals(a.id())));
    }
    public static ChickenSpecies getByRegistryName(String id) {
        if (id == null) return null;
        String local = id.startsWith("chickens:") ? id.substring(9) : id;
        return TYPES.get(local);
    }

    public static int getTier(ChickenSpecies type) {
        return tier(Objects.requireNonNull(type), new java.util.HashSet<>());
    }

    private static int tier(ChickenSpecies type, java.util.Set<String> path) {
        var parents = parentsOf(type);
        if (!parents.hasParents()) return 1;
        if (!path.add(type.id())) throw new IllegalStateException("Cyclic chicken pedigree " + type.id());
        int value = 1 + Math.max(tier(TYPES.get(parents.first()), path), tier(TYPES.get(parents.second()), path));
        path.remove(type.id());
        return value;
    }

    /** Original algorithm: both parents and compatible offspring are weighted by max tier + 1 - tier. */
    public static ChickenSpecies getRandomChild(ChickenSpecies first, ChickenSpecies second, IntUnaryOperator rollBounded) {
        return getRandomChild(first, second, rollBounded, type -> SpeciesLayConfig.isEnabled(type.id()));
    }

    /**
     * Pure weighted breeding selection with an explicit species-availability policy.
     * Keeps the original weighting while allowing tests without starting NeoForge.
     */
    public static ChickenSpecies getRandomChild(ChickenSpecies first, ChickenSpecies second,
            IntUnaryOperator rollBounded, Predicate<ChickenSpecies> enabled) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);
        Objects.requireNonNull(rollBounded);
        Objects.requireNonNull(enabled);
        List<ChickenSpecies> children = new ArrayList<>();
        if (!enabled.test(first) || !enabled.test(second)) {
            return null;
        }
        children.add(first);
        children.add(second); // Deliberately preserve the original double weight for same-breed parents.
        for (ChickenSpecies type : TYPES.values()) {
            if (enabled.test(type) && isChildOf(type, first, second)) children.add(type);
        }
        int max = 1;
        for (ChickenSpecies type : children) max = Math.max(max, getTier(type));
        max++;
        int total = 0;
        for (ChickenSpecies type : children) total += max - getTier(type);
        int roll = rollBounded.applyAsInt(total);
        for (ChickenSpecies type : children) {
            roll -= max - getTier(type);
            if (roll < 0) return type;
        }
        throw new IllegalStateException("Invalid breeding weight");
    }

    public static int getMinLayTime(ChickenSpecies type) {
        return LayTimeMath.scaledTicks(6000 * getTier(type),
                SpeciesLayConfig.coefficientFor(type.id()));
    }
    public static int getMaxLayTime(ChickenSpecies type) { return getMinLayTime(type) * 2; }
    public static float getChildChance(ChickenSpecies child) {
        var parents = parentsOf(child);
        if (!SpeciesLayConfig.isEnabled(child.id()) || !parents.hasParents()) return 0;
        ChickenSpecies a = getByRegistryName(parents.first());
        ChickenSpecies b = getByRegistryName(parents.second());
        if (!SpeciesLayConfig.isEnabled(a.id()) || !SpeciesLayConfig.isEnabled(b.id())) return 0;
        List<ChickenSpecies> list = new ArrayList<>(List.of(a, b));
        for (ChickenSpecies possible : TYPES.values()) {
            if (SpeciesLayConfig.isEnabled(possible.id()) && isChildOf(possible, a, b)) list.add(possible);
        }
        int max = 1;
        for (ChickenSpecies possible : list) max = Math.max(max, getTier(possible));
        max++;
        int total = 0;
        for (ChickenSpecies possible : list) total += max - getTier(possible);
        return 100.0f * (max - getTier(child)) / total;
    }
}
