package net.creeperhost.chickens.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

final class LayProgressMathTest {
    @Test
    void preservesOriginalDisplayBands() {
        assertEquals(0, LayProgressMath.remainingBand(1));
        assertEquals(0, LayProgressMath.remainingBand(2399));
        assertEquals(1, LayProgressMath.remainingBand(2400));
        assertEquals(1, LayProgressMath.remainingBand(4799));
        assertEquals(2, LayProgressMath.remainingBand(4800));
        assertEquals(2, LayProgressMath.remainingBand(6000));
    }

    @Test
    void avoidsUnchangedBandUpdates() {
        assertFalse(LayProgressMath.needsSync(1, LayProgressMath.remainingBand(3000)));
        assertTrue(LayProgressMath.needsSync(2, LayProgressMath.remainingBand(2399)));
    }

    @Test
    void restoresDifferentBandAfterLoad() {
        assertTrue(LayProgressMath.needsSync(0, LayProgressMath.remainingBand(6000)));
        assertFalse(LayProgressMath.needsSync(2, LayProgressMath.remainingBand(6000)));
    }
}
