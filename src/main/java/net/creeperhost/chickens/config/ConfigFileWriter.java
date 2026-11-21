package net.creeperhost.chickens.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Writes completed configuration contents to a sibling temporary file before
 * replacing the target. Independent of the Minecraft/NeoForge runtime.
 */
final class ConfigFileWriter {
    private ConfigFileWriter() {}

    static void writeSafely(Path configFile, String contents) throws IOException {
        Path directory = configFile.getParent();
        Files.createDirectories(directory);
        Path temporary = Files.createTempFile(directory, "chickens-", ".json.tmp");
        try {
            Files.writeString(temporary, contents, StandardCharsets.UTF_8);
            try {
                Files.move(temporary, configFile,
                        StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException unsupported) {
                Files.move(temporary, configFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }
}
