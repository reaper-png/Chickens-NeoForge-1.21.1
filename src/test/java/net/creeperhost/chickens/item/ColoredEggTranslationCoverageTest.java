package net.creeperhost.chickens.item;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import net.creeperhost.chickens.registry.ChickenSpecies;
import net.creeperhost.chickens.registry.ChickensRegistry;
import org.junit.jupiter.api.Test;

/** Prevents missing translation keys for the 16 original dye chicken eggs. */
final class ColoredEggTranslationCoverageTest {
    @Test
    void allSupportedLanguagesHaveEveryColoredEggNameAndTooltip() throws IOException {
        for (String language : new String[] { "en_us", "fr_fr", "pt_br" }) {
            String resourcePath = "assets/chickens/lang/" + language + ".json";
            try (InputStream resource = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
                assertNotNull(resource, "Missing language file: " + resourcePath);
                String json = new String(resource.readAllBytes(), StandardCharsets.UTF_8);
                assertTrue(json.contains("\"item.colored_egg.tooltip\""),
                        "Missing colored egg tooltip in " + language);
                for (ChickenSpecies species : ChickensRegistry.getItems()) {
                    if (!ColoredEggSpecies.isEligible(species)) continue;
                    String key = "item.colored_egg." + species.dyeColor() + ".name";
                    assertTrue(json.contains("\"" + key + "\""),
                            "Missing translation " + key + " in " + language);
                }
            }
        }
    }
}
