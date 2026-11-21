package net.creeperhost.chickens.blockentities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

final class MachineProgressRecoveryTest {
    @Test
    void preservesOrdinaryAndCompletedBreederCycles() {
        assertEquals(0, MachineProgressRecovery.breeder(0));
        assertEquals(500, MachineProgressRecovery.breeder(500));
        assertEquals(1000, MachineProgressRecovery.breeder(1000));
        assertEquals(1001, MachineProgressRecovery.breeder(1001));
    }

    @Test
    void boundsInvalidBreederProgressOnRestore() {
        assertEquals(0, MachineProgressRecovery.breeder(-10));
        assertEquals(1001, MachineProgressRecovery.breeder(Integer.MAX_VALUE));
    }

    @Test
    void preservesRoostGrowthOvershootAndCompletedCycle() {
        assertEquals(0, MachineProgressRecovery.roost(0));
        assertEquals(500, MachineProgressRecovery.roost(500));
        assertEquals(1001, MachineProgressRecovery.roost(1001));
        assertEquals(1050, MachineProgressRecovery.roost(1050));
    }

    @Test
    void boundsInvalidRoostProgressOnRestore() {
        assertEquals(0, MachineProgressRecovery.roost(Integer.MIN_VALUE));
        assertEquals(1050, MachineProgressRecovery.roost(Integer.MAX_VALUE));
    }
}
