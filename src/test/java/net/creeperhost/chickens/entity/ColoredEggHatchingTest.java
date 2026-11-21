package net.creeperhost.chickens.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

final class ColoredEggHatchingTest {
    @Test
    void noHatchPerformsOnlyFirstRoll() {
        AtomicInteger rolls = new AtomicInteger();
        int count = ColoredEggHatching.hatchCount(bound -> {
            assertEquals(8, bound);
            rolls.incrementAndGet();
            return 1;
        });
        assertEquals(0, count);
        assertEquals(1, rolls.get());
    }

    @Test
    void normalHatchProducesOneChickenWithTwoRolls() {
        AtomicInteger rolls = new AtomicInteger();
        assertEquals(1, ColoredEggHatching.hatchCount(bound -> {
            int index = rolls.getAndIncrement();
            assertEquals(index == 0 ? 8 : 32, bound);
            return index == 0 ? 0 : 1;
        }));
        assertEquals(2, rolls.get());
    }

    @Test
    void rareHatchProducesFourChickensWithTwoRolls() {
        AtomicInteger rolls = new AtomicInteger();
        assertEquals(4, ColoredEggHatching.hatchCount(bound -> {
            assertEquals(rolls.getAndIncrement() == 0 ? 8 : 32, bound);
            return 0;
        }));
        assertEquals(2, rolls.get());
    }
}
