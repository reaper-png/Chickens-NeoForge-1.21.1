package net.creeperhost.chickens.client;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OpaqueTintTest {
    @Test void itemTintNeverDropsAlpha() {
        assertEquals(0xFFFFFFFF, OpaqueTint.fromRgb(0xFFFFFF));
        assertEquals(0xFF08080E, OpaqueTint.fromRgb(0x08080E));
        assertEquals(0xFF123456, OpaqueTint.fromRgb(0x00123456));
        assertEquals(0xFF123456, OpaqueTint.fromRgb(0xAA123456));
    }
}
