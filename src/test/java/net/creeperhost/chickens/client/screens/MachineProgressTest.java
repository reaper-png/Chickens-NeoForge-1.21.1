package net.creeperhost.chickens.client.screens;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

final class MachineProgressTest {
    @Test void boundsAreClamped() {
        assertEquals(0, MachineProgress.width(-5));
        assertEquals(0, MachineProgress.width(0));
        assertEquals(1, MachineProgress.width(1));
        assertEquals(26, MachineProgress.width(1000));
        assertEquals(26, MachineProgress.width(1001));
        assertEquals(0.0, MachineProgress.percent(-1));
        assertEquals(100.0, MachineProgress.percent(1000));
        assertEquals(100.0, MachineProgress.percent(2000));
    }

    @Test void widthNeverRegressesWithinCycle() {
        int previous = 0;
        for (int ticks=0; ticks<=1000; ticks++) {
            int current = MachineProgress.width(ticks);
            assertTrue(current >= previous);
            assertTrue(current >= 0 && current <= 26);
            previous = current;
        }
    }
}
