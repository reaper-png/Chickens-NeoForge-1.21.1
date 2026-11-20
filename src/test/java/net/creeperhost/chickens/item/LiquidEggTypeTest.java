package net.creeperhost.chickens.item;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

final class LiquidEggTypeTest {
    @Test void resolvesOnlyTheTwoOriginalFluids() {
        assertEquals(LiquidEggType.WATER, LiquidEggType.fromId("minecraft:water").orElseThrow());
        assertEquals(LiquidEggType.LAVA, LiquidEggType.fromId("minecraft:lava").orElseThrow());
        assertTrue(LiquidEggType.fromId("minecraft:milk").isEmpty());
        assertTrue(LiquidEggType.fromId("chickens:water").isEmpty());
        assertTrue(LiquidEggType.fromId("").isEmpty());
    }

    @Test void maintainsExactLegacyIds() {
        assertEquals("minecraft:water", LiquidEggType.WATER.id());
        assertEquals("minecraft:lava", LiquidEggType.LAVA.id());
        assertNotEquals(LiquidEggType.WATER.tint(), LiquidEggType.LAVA.tint());
    }
}
