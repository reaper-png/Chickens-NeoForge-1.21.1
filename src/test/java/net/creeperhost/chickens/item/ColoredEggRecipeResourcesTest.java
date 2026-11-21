package net.creeperhost.chickens.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

/**
 * Verifies the 16 original dye egg recipes without requiring Minecraft APIs.
 * Keep the legacy misspelled mangenta recipe ID: renaming it could affect
 * recipe discovery and saved recipe references.
 */
final class ColoredEggRecipeResourcesTest {
    private static final String[] DYES = {
            "white", "orange", "magenta", "light_blue",
            "yellow", "lime", "pink", "gray",
            "light_gray", "cyan", "purple", "blue",
            "brown", "green", "red", "black"
    };

    @Test
    void allSixteenDyeEggRecipesPreserveTheirSpeciesAndVanillaColorIds() throws IOException {
        for (int colorId = 0; colorId < DYES.length; colorId++) {
            String dye = DYES[colorId];
            String recipeName = dye.equals("magenta") ? "mangenta" : dye;
            String path = "data/chickens/recipe/" + recipeName + "_chicken_egg.json";
            try (InputStream input = getClass().getClassLoader().getResourceAsStream(path)) {
                assertNotNull(input, "Missing legacy recipe: " + path);
                String json = new String(input.readAllBytes(), StandardCharsets.UTF_8);
                assertTrue(json.contains("\"minecraft:" + dye + "_dye\""),
                        "Wrong dye ingredient: " + path);
                assertTrue(json.contains("\"chickens:colored_egg\""),
                        "Wrong recipe output: " + path);
                assertTrue(json.contains("\"chickens:" + dye + "_chicken\""),
                        "Wrong egg species: " + path);
                Matcher marker = Pattern.compile("\"colourid\"\\s*:\\s*(\\d+)").matcher(json);
                assertTrue(marker.find(), "Missing color id in " + path);
                assertEquals(colorId, Integer.parseInt(marker.group(1)),
                        "Wrong vanilla DyeColor id in " + path);
            }
        }
    }
}
