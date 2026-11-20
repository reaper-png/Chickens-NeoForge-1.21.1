package net.creeperhost.chickens.item;

import java.util.Arrays;
import java.util.Optional;

/** The two legacy liquid egg variants. No extra fluids or species are introduced. */
public enum LiquidEggType {
    WATER("minecraft:water", 0x366DDE),
    LAVA("minecraft:lava", 0xF38B26);

    private final String id;
    private final int tint;

    LiquidEggType(String id, int tint) {
        this.id = id;
        this.tint = tint;
    }

    public String id() { return id; }
    public int tint() { return tint; }

    public static Optional<LiquidEggType> fromId(String id) {
        return Arrays.stream(values()).filter(fluid -> fluid.id.equals(id)).findFirst();
    }
}
