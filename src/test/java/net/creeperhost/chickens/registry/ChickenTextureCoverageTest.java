package net.creeperhost.chickens.registry;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

/** Ensures all original registered chicken skins are packaged as PNG resources. */
final class ChickenTextureCoverageTest {
    private static final byte[] PNG_SIGNATURE = {
            (byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a
    };

    @Test
    void everyRegisteredBreedHasAnEntityTexture() throws IOException {
        assertEquals(46, ChickensRegistry.getItems().size(), "Original Chickens breed count");
        for (ChickenSpecies species : ChickensRegistry.getItems()) {
            String path = "assets/chickens/textures/entity/" + species.id() + ".png";
            try (InputStream stream = getClass().getClassLoader().getResourceAsStream(path)) {
                assertNotNull(stream, "Missing texture: " + path);
                assertArrayEquals(PNG_SIGNATURE, stream.readNBytes(PNG_SIGNATURE.length),
                        "Invalid PNG header: " + path);
            }
        }
    }
}
