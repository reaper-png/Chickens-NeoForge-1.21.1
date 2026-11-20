package net.creeperhost.chickens.breeding;

import java.util.Objects;
import java.util.function.IntUnaryOperator;
import net.creeperhost.chickens.data.ChickenStats;
import net.creeperhost.chickens.registry.ChickenSpecies;

/** Original Chickens 1.0.32 inheritance and mutation rules, shared by world breeding and Breeder. */
public final class ChickenGenetics {
    private ChickenGenetics() {}

    public static ChickenStats offspring(ChickenSpecies first, ChickenSpecies second, ChickenSpecies child,
                                         ChickenStats a, ChickenStats b, IntUnaryOperator randomBounded) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);
        Objects.requireNonNull(child);
        if (first.id().equals(second.id()) && child.id().equals(first.id())) {
            int s = a.getStrength();
            int t = b.getStrength();
            return new ChickenStats(
                    mutated(a.getGain(), b.getGain(), s, t, randomBounded),
                    mutated(a.getGrowth(), b.getGrowth(), s, t, randomBounded),
                    mutated(s, t, s, t, randomBounded));
        }
        if (child.id().equals(first.id())) return new ChickenStats(a.getGain(), a.getGrowth(), a.getStrength());
        if (child.id().equals(second.id())) return new ChickenStats(b.getGain(), b.getGrowth(), b.getStrength());
        return new ChickenStats();
    }

    private static int mutated(int a, int b, int strengthA, int strengthB, IntUnaryOperator randomBounded) {
        int weighted = (a * strengthA + b * strengthB) / Math.max(1, strengthA + strengthB);
        return Math.clamp(weighted + randomBounded.applyAsInt(2) + 1, 1, 10);
    }
}
