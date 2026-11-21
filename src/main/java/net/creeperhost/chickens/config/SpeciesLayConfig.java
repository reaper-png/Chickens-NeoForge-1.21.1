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
 * Supports lay_coefficient, is_enabled, lay_item and optional drop_item.
 */
public final class SpeciesLayConfig {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final float MIN_COEFFICIENT = 0.01f;
    private static final float MAX_COEFFICIENT = 100.0f;
    private static final long MAX_CONFIG_BYTES = 1024L * 1024L;

    private static final class Values {
        private static final Settings SETTINGS = load();
    }

    private record Settings(Map<String, Float> coefficients, Map<String, Boolean> enabled,
                            Map<String, String> layItems, Map<String, String> dropItems) {}

    private SpeciesLayConfig() {}

    /** Called during common setup so the file exists before a chicken spawns. */
    public static void initialize() {
        Values.SETTINGS.coefficients().size();
    }

    public static float coefficientFor(String id) {
        return Values.SETTINGS.coefficients().getOrDefault("chickens:" + id, 1.0f);
    }

    /**
     * Configured item descriptor for resource laying; null means use the original product.
     * The runtime item registry is validated in ChickenProducts.
     */
    public static String layItemFor(String id) {
        String key = id.startsWith("chickens:") ? id : "chickens:" + id;
        return Values.SETTINGS.layItems().get(key);
    }

    /** Empty or absent drop_item means no extra custom death drop. */
    public static String dropItemFor(String id) {
        String key = id.startsWith("chickens:") ? id : "chickens:" + id;
        return Values.SETTINGS.dropItems().get(key);
    }

    /** Disabled species cannot spawn naturally or be produced by breeding. */
    public static boolean isEnabled(String id) {
        String key = id.startsWith("chickens:") ? id : "chickens:" + id;
        return Values.SETTINGS.enabled().getOrDefault(key, true);
    }

    /**
     * Adds missing registered species and active configuration properties only.
     * Leaves all existing values, unknown keys and other JSON settings untouched.
     */
    static boolean addMissingSpecies(JsonObject root) {
        boolean changed = false;
        for (ChickenSpecies type : ChickensRegistry.getItems()) {
            String key = "chickens:" + type.id();
            if (!root.has(key)) {
                JsonObject settings = new JsonObject();
                settings.addProperty("lay_coefficient", 1.0f);
                settings.addProperty("is_enabled", true);
                settings.addProperty("lay_item", type.product());
                settings.addProperty("drop_item", "");
                root.add(key, settings);
                changed = true;
            } else if (root.get(key).isJsonObject()) {
                JsonObject settings = root.getAsJsonObject(key);
                if (!settings.has("lay_coefficient")) {
                    settings.addProperty("lay_coefficient", 1.0f);
                    changed = true;
                }
                if (!settings.has("is_enabled")) {
                    settings.addProperty("is_enabled", true);
                    changed = true;
                }
                if (!settings.has("lay_item")) {
                    settings.addProperty("lay_item", type.product());
                    changed = true;
                }
                if (!settings.has("drop_item")) {
                    settings.addProperty("drop_item", "");
                    changed = true;
                }
            } else {
                LOGGER.warn("Skipping non-object species entry {} in chickens.json", key);
            }
        }
        return changed;
    }

    private static Settings defaults() {
        return new Settings(Map.of(), Map.of(), Map.of(), Map.of());
    }

    private static Settings load() {
        Path configFile = FMLPaths.CONFIGDIR.get()
                .resolve("chickens").resolve("chickens.json");
        try {
            JsonObject root;
            if (Files.exists(configFile)) {
                if (Files.size(configFile) > MAX_CONFIG_BYTES) {
                    LOGGER.warn("Chickens config exceeds 1 MiB; leaving it unchanged: {}", configFile);
                    return defaults();
                }
                try (Reader reader = Files.newBufferedReader(configFile, StandardCharsets.UTF_8)) {
                    JsonElement parsed = JsonParser.parseReader(reader);
                    if (!parsed.isJsonObject()) {
                        LOGGER.warn("Chickens species config must be a JSON object; leaving it unchanged: {}", configFile);
                        return defaults();
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
            Map<String, Boolean> enabled = new HashMap<>();
            Map<String, String> layItems = new HashMap<>();
            Map<String, String> dropItems = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                if (!entry.getKey().startsWith("chickens:")
                        || !entry.getValue().isJsonObject()) {
                    continue;
                }
                JsonObject species = entry.getValue().getAsJsonObject();
                if (species.has("is_enabled")) {
                    JsonElement active = species.get("is_enabled");
                    if (active.isJsonPrimitive() && active.getAsJsonPrimitive().isBoolean()) {
                        enabled.put(entry.getKey(), active.getAsBoolean());
                    } else {
                        LOGGER.warn("Invalid is_enabled value for {}; using true", entry.getKey());
                    }
                }
                if (species.has("lay_item")) {
                    JsonElement layItem = species.get("lay_item");
                    if (layItem.isJsonPrimitive() && layItem.getAsJsonPrimitive().isString()) {
                        String descriptor = layItem.getAsString().trim();
                        if (!descriptor.isEmpty()) {
                            layItems.put(entry.getKey(), descriptor);
                        } else {
                            LOGGER.warn("Blank lay_item for {}; using original product", entry.getKey());
                        }
                    } else {
                        LOGGER.warn("Invalid lay_item for {}; using original product", entry.getKey());
                    }
                }
                if (species.has("drop_item")) {
                    JsonElement dropItem = species.get("drop_item");
                    if (dropItem.isJsonPrimitive() && dropItem.getAsJsonPrimitive().isString()) {
                        String descriptor = dropItem.getAsString().trim();
                        if (!descriptor.isEmpty()) {
                            dropItems.put(entry.getKey(), descriptor);
                        }
                    } else {
                        LOGGER.warn("Invalid drop_item for {}; ignoring", entry.getKey());
                    }
                }
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
            LOGGER.info("Loaded {} laying coefficients, {} enabled flags, {} laying products and {} death drops",
                    loaded.size(), enabled.size(), layItems.size(), dropItems.size());
            return new Settings(Collections.unmodifiableMap(loaded),
                    Collections.unmodifiableMap(enabled), Collections.unmodifiableMap(layItems),
                    Collections.unmodifiableMap(dropItems));
        } catch (IOException | RuntimeException error) {
            LOGGER.error("Failed to load Chickens species config {}; using defaults", configFile, error);
            return defaults();
        }
    }
}
