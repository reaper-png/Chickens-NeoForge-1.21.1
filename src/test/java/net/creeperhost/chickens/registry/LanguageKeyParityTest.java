package net.creeperhost.chickens.registry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

/** Plain JUnit resource test; does not require Minecraft classes. */
final class LanguageKeyParityTest {
    private static final Pattern ENTRY = Pattern.compile("^\\s*\\\"([^\\\"]+)\\\"\\s*:");
    
    @Test
    void languagesContainEveryEnglishTranslationKey() throws IOException {
        Set<String> english = keys("en_us");
        assertFalse(english.isEmpty());
        for (String language : new String[] { "fr_fr", "pt_br" }) {
            Set<String> translated = keys(language);
            Set<String> missing = new HashSet<>(english);
            missing.removeAll(translated);
            assertEquals(Set.of(), missing, "Missing translation keys in " + language);
        }
    }

    private static Set<String> keys(String language) throws IOException {
        String path = "assets/chickens/lang/" + language + ".json";
        try (InputStream in = LanguageKeyParityTest.class.getClassLoader().getResourceAsStream(path)) {
            assertNotNull(in, "Missing language file: " + path);
            Set<String> keys = new HashSet<>();
            for (String line : new String(in.readAllBytes(), StandardCharsets.UTF_8).split("\\R")) {
                Matcher match = ENTRY.matcher(line);
                if (match.find()) keys.add(match.group(1));
            }
            return keys;
        }
    }
}
