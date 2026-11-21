package net.creeperhost.chickens.entity;

import java.util.Objects;
import java.util.function.IntUnaryOperator;

/** Original Chickens 1.0.32 two-roll colored egg hatch distribution. */
public final class ColoredEggHatching {
    private ColoredEggHatching() {}

    /** 7/8: none; 31/256: one; 1/256: four chickens. */
    public static int hatchCount(IntUnaryOperator randomBounded) {
        Objects.requireNonNull(randomBounded);
        if (randomBounded.applyAsInt(8) != 0) return 0;
        return randomBounded.applyAsInt(32) == 0 ? 4 : 1;
    }
}
