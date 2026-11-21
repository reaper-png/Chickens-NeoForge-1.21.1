package net.creeperhost.chickens.data;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

/**
 * 1.21.1 adapter for Chickens 1.0.32 item stat keys. Other custom data keys
 * (especially id/colourid) must survive a stats update unchanged.
 */
public final class ChickenStatsCodec {
    private ChickenStatsCodec() {}

    public static ChickenStats read(ItemStack stack) {
        CustomData component = stack.get(DataComponents.CUSTOM_DATA);
        if (component == null) {
            // Reading stats must never modify an item stack. Fresh/corrupt
            // legacy items still behave as 1/1/1 without writing new NBT.
            return new ChickenStats();
        }
        CompoundTag tag = component.copyTag();
        return new ChickenStats(
            tag.contains("gain") ? tag.getInt("gain") : 1,
            tag.contains("growth") ? tag.getInt("growth") : 1,
            tag.contains("strength") ? tag.getInt("strength") : 1);
    }

    public static void write(ItemStack stack, ChickenStats stats) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            tag.putInt("gain", stats.getGain());
            tag.putInt("growth", stats.getGrowth());
            tag.putInt("strength", stats.getStrength());
        });
    }
}
