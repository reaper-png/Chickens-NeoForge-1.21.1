package net.creeperhost.chickens;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/**
 * Static guard for the common mod entrypoint: avoid introducing client-only
 * imports that would fail class loading on a dedicated Minecraft server.
 */
final class ClientSeparationTest {
    @Test
    void commonEntrypointDoesNotImportClientOnlyTypes() throws IOException {
        Path entrypoint = Path.of("src/main/java/net/creeperhost/chickens/ChickensMod.java");
        String source = Files.readString(entrypoint);
        assertFalse(source.contains("import net.minecraft.client."),
                "Common entrypoint must not import Minecraft client classes");
        assertFalse(source.contains("import net.neoforged.neoforge.client."),
                "Common entrypoint must not import NeoForge client classes");
        assertFalse(source.contains("import net.creeperhost.chickens.client."),
                "Client implementation must not be referenced from common entrypoint");
    }

    @Test
    void clientEventsAreRestrictedToClientDistribution() throws IOException {
        Path events = Path.of("src/main/java/net/creeperhost/chickens/client/ChickensClientEvents.java");
        String source = Files.readString(events);
        assertTrue(source.contains("value = Dist.CLIENT"),
                "Client rendering events must be gated to the client distribution");
    }
}
