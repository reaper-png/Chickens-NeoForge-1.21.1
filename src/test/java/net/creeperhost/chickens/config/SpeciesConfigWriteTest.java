package net.creeperhost.chickens.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Regression checks for the stage 93 replace-after-write config persistence. */
class SpeciesConfigWriteTest {
    @TempDir Path directory;

    @Test
    void createsMissingConfigAndCleansTemporaryFile() throws IOException {
        Path target = directory.resolve("chickens.json");
        ConfigFileWriter.writeSafely(target, "{\"new\":true}\n");

        assertEquals("{\"new\":true}\n", Files.readString(target, StandardCharsets.UTF_8));
        assertNoTemporaryFiles();
    }

    @Test
    void replacesExistingConfigWithoutLeavingStagingFiles() throws IOException {
        Path target = directory.resolve("chickens.json");
        Files.writeString(target, "{\"old\":true}\n", StandardCharsets.UTF_8);

        ConfigFileWriter.writeSafely(target, "{\"old\":true,\"added\":1}\n");

        assertEquals("{\"old\":true,\"added\":1}\n", Files.readString(target, StandardCharsets.UTF_8));
        assertNoTemporaryFiles();
    }

    @Test
    void refusedDirectoryTargetKeepsExistingContentsAndCleansTempFile() throws IOException {
        Path target = directory.resolve("chickens.json");
        Files.createDirectory(target);
        Path sentinel = target.resolve("keep.txt");
        Files.writeString(sentinel, "original", StandardCharsets.UTF_8);

        org.junit.jupiter.api.Assertions.assertThrows(IOException.class,
                () -> ConfigFileWriter.writeSafely(target, "{\"test\":1}\n"));

        assertTrue(Files.isDirectory(target));
        assertEquals("original", Files.readString(sentinel, StandardCharsets.UTF_8));
        assertNoTemporaryFiles();
    }

    private void assertNoTemporaryFiles() throws IOException {
        try (Stream<Path> contents = Files.list(directory)) {
            assertFalse(contents.anyMatch(path ->
                    path.getFileName().toString().startsWith("chickens-")
                            && path.getFileName().toString().endsWith(".json.tmp")));
        }
    }
}
