package net.creeperhost.chickens.registry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Checks resource coverage at the actual NeoForge 1.21.1 loot_table paths.
 * No Minecraft runtime or NeoForge classes are required.
 */
final class LootResourceCoverageTest {
    private static final Set<String> BLOCKS = Set.of(
            "breeder", "roost", "henhouse", "henhouse_acacia",
            "henhouse_birch", "henhouse_dark_oak", "henhouse_jungle", "henhouse_spruce");

    @Test
    void everyOriginalChickenHasAnEntityLootTable() throws IOException {
        assertEquals(46, ChickensRegistry.getItems().size());
        for (ChickenSpecies species : ChickensRegistry.getItems()) {
            String resource = read("data/chickens/loot_table/entities/" + species.id() + ".json");
            assertTrue(resource.contains("\"minecraft:entity\""),
                    "Incorrect entity loot table type: " + species.id());
        }
    }

    @Test
    void allEightMachineVariantsHaveBlockLootTables() throws IOException {
        assertEquals(8, BLOCKS.size());
        for (String block : BLOCKS) {
            String resource = read("data/chickens/loot_table/blocks/" + block + ".json");
            assertTrue(resource.contains("\"minecraft:block\""),
                    "Incorrect block loot table type: " + block);
            assertTrue(resource.contains("\"chickens:" + block + "\""),
                    "Missing expected block drop: " + block);
        }
    }

    private static String read(String path) throws IOException {
        try (InputStream input = LootResourceCoverageTest.class.getClassLoader()
                .getResourceAsStream(path)) {
            assertNotNull(input, "Missing loot resource: " + path);
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
