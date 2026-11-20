package net.creeperhost.chickens.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

/**
 * Reads the original Chickens config/chickens/chickens.json structure.
 * Stage 13 intentionally supports only lay_coefficient; other original
 * properties must not be silently reinterpreted.
 */
public final class SpeciesLayConfig {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final float MIN_COEFFICIENT = 0.01f;
    private static final float MAX_COEFFICIENT = 100.0f;

    // Lazy loading: avoid touching paths during the static entity registration.
    private static final class Values {
        private static final Map<String, Float> COEFFICIENTS = load();
    }

    private SpeciesLayConfig() {}

    public static float coefficientFor(String id) {
        return Values.COEFFICIENTS.getOrDefault("chickens:" + id, 1.0f);
    }

    /** Matches original ChickensRegistryItem.getMinLayTime() math. */
    public static int scaledTicks(int baseTicks, float coefficient) {
        return (int) Math.max(1.0f, baseTicks * coefficient);
    }

    private static Map<String, Float> load() {
        Path configFile = FMLPaths.CONFIGDIR.get()
                .resolve("chickens").resolve("chickens.json");
        try {
            if (Files.notExists(configFile)) {
                Files.createDirectories(configFile.getParent());
                JsonObject template = new JsonObject();
                JsonObject example = new JsonObject();
                example.addProperty("lay_coefficient", 1.0f);
                template.add("chickens:iron_chicken", example);
                Files.writeString(configFile,
                        new GsonBuilder().setPrettyPrinting().create().toJson(template) + "\n",
                        StandardCharsets.UTF_8);
            }
            if (Files.size(configFile) > 1024 * 1024) {
                LOGGER.warn("Chickens config is larger than 1 MiB, ignoring: {}", configFile);
                return Map.of();
            }
            try (Reader reader = Files.newBufferedReader(configFile, StandardCharsets.UTF_8)) {
                JsonElement root = JsonParser.parseReader(reader);
                if (!root.isJsonObject()) {
                    LOGGER.warn("Chickens species config must be a JSON object: {}", configFile);
                    return Map.of();
                }
                Map<String, Float> loaded = new HashMap<>();
                for (Map.Entry<String, JsonElement> entry : root.getAsJsonObject().entrySet()) {
                    if (!entry.getKey().startsWith("chickens:")
                            || !entry.getValue().isJsonObject()) {
                        continue;
                    }
                    JsonObject species = entry.getValue().getAsJsonObject();
                    if (!species.has("lay_coefficient")) {
                        continue;
                    }
                    try {
                        float value = species.get("lay_coefficient").getAsFloat();
                        if (!Float.isFinite(value)
                                || value < MIN_COEFFICIENT || value > MAX_COEFFICIENT) {
                            LOGGER.warn("Invalid lay_coefficient for {}: {}", entry.getKey(), value);
                            continue;
                        }
                        loaded.put(entry.getKey(), value);
                    } catch (RuntimeException invalidValue) {
                        LOGGER.warn("Invalid lay_coefficient for {}", entry.getKey(), invalidValue);
                    }
                }
                LOGGER.info("Loaded {} Chickens lay-coefficient overrides", loaded.size());
                return Collections.unmodifiableMap(loaded);
            }
        } catch (IOException | RuntimeException error) {
            LOGGER.error("Failed to read Chickens species config {}; using defaults", configFile, error);
            return Map.of();
        }
    }
}
