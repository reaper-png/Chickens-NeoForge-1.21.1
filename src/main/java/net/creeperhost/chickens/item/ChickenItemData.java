package net.creeperhost.chickens.item;

import net.creeperhost.chickens.data.ChickenStats;
import net.creeperhost.chickens.data.ChickenStatsCodec;
import net.creeperhost.chickens.registry.ChickenSpecies;
import net.creeperhost.chickens.registry.ChickensRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

/** Legacy ChickenType:{id:"chickens:..."} stored inside 1.21 CUSTOM_DATA. */
public final class ChickenItemData {
    private ChickenItemData() {}

    public static ChickenSpecies type(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return null;
        CompoundTag root = data.copyTag();
        if (root.contains("ChickenType", 10)) {
            return ChickensRegistry.getByRegistryName(root.getCompound("ChickenType").getString("id"));
        }
        // 1.0.32 colored eggs use a flat {id:"chickens:..."} field.
        if (root.contains("id", 8)) return ChickensRegistry.getByRegistryName(root.getString("id"));
        return null;
    }

    public static void write(ItemStack stack, ChickenSpecies type, ChickenStats stats) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, root -> {
            CompoundTag chickenType = new CompoundTag();
            chickenType.putString("id", "chickens:" + type.id());
            root.put("ChickenType", chickenType);
        });
        ChickenStatsCodec.write(stack, stats);
    }

    public static ItemStack create(net.minecraft.world.item.Item item, ChickenSpecies type) {
        ItemStack stack = new ItemStack(item);
        write(stack, type, new ChickenStats());
        return stack;
    }

    public static void saveCapturedState(ItemStack stack, boolean baby, int loveTicks) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, root -> {
            root.putBoolean("baby", baby);
            root.putInt("love", loveTicks);
        });
    }

    /** Exact age (negative for babies), analyzer state and existing legacy fields. */
    public static void saveCapturedState(ItemStack stack, int ageTicks, int loveTicks, boolean analyzed) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, root -> {
            root.putBoolean("baby", ageTicks < 0); // Retain compatibility with legacy item readers.
            root.putInt("age_ticks", ageTicks);
            root.putInt("love", Math.max(0, loveTicks));
            root.putBoolean("analyzed", analyzed);
        });
    }

    /** Null for items captured before exact age was stored. */
    public static Integer capturedAgeTicks(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return null;
        CompoundTag root = data.copyTag();
        return root.contains("age_ticks", 3) ? root.getInt("age_ticks") : null;
    }

    public static boolean wasAnalyzed(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data != null && data.copyTag().getBoolean("analyzed");
    }

    public static boolean wasBaby(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data != null && data.copyTag().getBoolean("baby");
    }

    public static int loveTicks(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data == null ? 0 : data.copyTag().getInt("love");
    }
}
