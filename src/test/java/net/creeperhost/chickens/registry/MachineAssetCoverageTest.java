package net.creeperhost.chickens.registry;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

/** Verify block state and inventory model resources for all machine variants. */
final class MachineAssetCoverageTest {
    private static final String[] BLOCKS = {
            "henhouse", "henhouse_acacia", "henhouse_birch",
            "henhouse_dark_oak", "henhouse_jungle", "henhouse_spruce",
            "roost", "breeder"
    };

    @Test
    void everyMachineHasBlockStateAndItemModel() throws IOException {
        for (String name : BLOCKS) {
            assertResource("assets/chickens/blockstates/" + name + ".json");
            assertResource("assets/chickens/models/item/" + name + ".json");
        }
    }

    private static void assertResource(String path) throws IOException {
        try (InputStream resource = MachineAssetCoverageTest.class.getClassLoader()
                .getResourceAsStream(path)) {
            assertNotNull(resource, "Missing asset: " + path);
            assertEquals(1, resource.readNBytes(1).length, "Empty asset: " + path);
        }
    }
}
