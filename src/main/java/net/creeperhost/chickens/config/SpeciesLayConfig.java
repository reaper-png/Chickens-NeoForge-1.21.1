package net.creeperhost.chickens.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.creeperhost.chickens.registry.ChickenSpecies;
import net.creeperhost.chickens.registry.ChickensRegistry;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;

/**
 * Reads the legacy Chickens config/chickens/chickens.json structure.
 * Only lay_coefficient is active; never reinterpret unported legacy fields.
 */
public final class SpeciesLayConfig {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final float MIN_COEFFICIENT = 0.01f;
    private static final float MAX_COEFFICIENT = 100.0f;
    private static final long MAX_CONFIG_BYTES = 1024L * 1024L;

    private static final class Values {
        private static final Map<String, Float> COEFFICIENTS = load();
    }

    private SpeciesLayConfig() {}

    /** Called during common setup so the file exists before a chicken spawns. */
    public static void initialize() {
        Values.COEFFICIENTS.size();
    }

    public static float coefficientFor(String id) {
        return Values.COEFFICIENTS.getOrDefault("chickens:" + id, 1.0f);
    }

    /**
     * Adds missing registered species and missing lay_coefficient properties only.
     * Leaves all existing values, unknown keys and other JSON settings untouched.
     */
    static boolean addMissingSpecies(JsonObject root) {
        boolean changed = false;
        for (ChickenSpecies type : ChickensRegistry.getItems()) {
            String key = "chickens:" + type.id();
            if (!root.has(key)) {
                JsonObject settings = new JsonObject();
                settings.addProperty("lay_coefficient", 1.0f);
                root.add(key, settings);
                changed = true;
            } else if (root.get(key).isJsonObject()) {
                JsonObject settings = root.getAsJsonObject(key);
                if (!settings.has("lay_coefficient")) {
                    settings.addProperty("lay_coefficient", 1.0f);
                    changed = true;
                }
            } else {
                LOGGER.warn("Skipping non-object species entry {} in chickens.json", key);
            }
        }
        return changed;
    }

    private static Map<String, Float> load() {
        Path configFile = FMLPaths.CONFIGDIR.get()
                .resolve("chickens").resolve("chickens.json");
        try {
            JsonObject root;
            if (Files.exists(configFile)) {
                if (Files.size(configFile) > MAX_CONFIG_BYTES) {
                    LOGGER.warn("Chickens config exceeds 1 MiB; leaving it unchanged: {}", configFile);
                    return Map.of();
                }
                try (Reader reader = Files.newBufferedReader(configFile, StandardCharsets.UTF_8)) {
                    JsonElement parsed = JsonParser.parseReader(reader);
                    if (!parsed.isJsonObject()) {
                        LOGGER.warn("Chickens species config must be a JSON object; leaving it unchanged: {}", configFile);
                        return Map.of();
                    }
                    root = parsed.getAsJsonObject();
                }
            } else {
                root = new JsonObject();
            }

            boolean changed = addMissingSpecies(root);
            if (changed || Files.notExists(configFile)) {
                Files.createDirectories(configFile.getParent());
                Files.writeString(configFile,
                        new GsonBuilder().setPrettyPrinting().create().toJson(root) + "\n",
                        StandardCharsets.UTF_8);
                LOGGER.info("Updated Chickens species template: {}", configFile);
            }

            Map<String, Float> loaded = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
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
            LOGGER.info("Loaded {} Chickens lay-coefficient settings", loaded.size());
            return Collections.unmodifiableMap(loaded);
        } catch (IOException | RuntimeException error) {
            LOGGER.error("Failed to load Chickens species config {}; using defaults", configFile, error);
            return Map.of();
        }
    }
}
