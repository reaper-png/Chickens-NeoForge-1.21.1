package net.creeperhost.chickens.registry;

import net.creeperhost.chickens.ChickensMod;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;

/** Converts the exact original chicken product mapping into a 1.21 ItemStack. */
public final class ChickenProducts {
    private ChickenProducts() {}

    public static ItemStack produce(ChickenSpecies species) {
        if (species == null || species.product() == null) return ItemStack.EMPTY;
        String[] parts = species.product().split("\\|", 2);
        ResourceLocation resource = ResourceLocation.tryParse(parts[0]);
        if (resource == null || !BuiltInRegistries.ITEM.containsKey(resource)) return ItemStack.EMPTY;
        Item item = BuiltInRegistries.ITEM.get(resource);
        if (item == Items.AIR) return ItemStack.EMPTY;
        ItemStack result = new ItemStack(item);
        if (parts.length == 2) {
            // Legacy liquid eggs used {id:"minecraft:water|lava"}; now held in CUSTOM_DATA.
            CustomData.update(DataComponents.CUSTOM_DATA, result, nbt -> nbt.putString("id", parts[1]));
        }
        return result;
    }
}
