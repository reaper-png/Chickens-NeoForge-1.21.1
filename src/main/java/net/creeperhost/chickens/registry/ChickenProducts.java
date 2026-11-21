package net.creeperhost.chickens.registry;

import net.creeperhost.chickens.config.SpeciesLayConfig;
import net.creeperhost.chickens.item.LiquidEggType;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

/** Produces the original product unless a valid lay_item override is configured. */
public final class ChickenProducts {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Set<String> INVALID_WARNED = ConcurrentHashMap.newKeySet();

    private ChickenProducts() {}

    public static ItemStack produce(ChickenSpecies species) {
        if (species == null || species.product() == null) return ItemStack.EMPTY;

        String configured = SpeciesLayConfig.layItemFor(species.id());
        if (configured != null) {
            ItemStack replacement = fromDescriptor(configured);
            if (!replacement.isEmpty()) return replacement;
            if (INVALID_WARNED.add(species.id() + "=" + configured)) {
                LOGGER.warn("Invalid lay_item '{}' for chickens:{}; using original product",
                        configured, species.id());
            }
        }
        return fromDescriptor(species.product());
    }

    /**
     * Extra death drop is independent from the laying product.
     * Missing or blank configuration never changes vanilla loot.
     */
    public static ItemStack deathDrop(ChickenSpecies species) {
        if (species == null) return ItemStack.EMPTY;
        String configured = SpeciesLayConfig.dropItemFor(species.id());
        if (configured == null || configured.isBlank()) return ItemStack.EMPTY;
        ItemStack result = fromDescriptor(configured);
        if (result.isEmpty() && INVALID_WARNED.add("drop:" + species.id() + "=" + configured)) {
            LOGGER.warn("Invalid drop_item '{}' for chickens:{}; ignoring death-drop override",
                    configured, species.id());
        }
        return result;
    }

    private static ItemStack fromDescriptor(String descriptor) {
        if (descriptor == null || descriptor.isBlank()) return ItemStack.EMPTY;
        String[] parts = descriptor.split("\\|", -1);
        if (parts.length > 2) return ItemStack.EMPTY;
        ResourceLocation resource = ResourceLocation.tryParse(parts[0]);
        if (resource == null || !BuiltInRegistries.ITEM.containsKey(resource)) return ItemStack.EMPTY;
        Item item = BuiltInRegistries.ITEM.get(resource);
        if (item == Items.AIR) return ItemStack.EMPTY;

        boolean liquidEgg = resource.equals(ResourceLocation.fromNamespaceAndPath("chickens", "liquid_egg"));
        // A liquid egg must always carry its original water/lava fluid ID.
        // The bare item ID creates an unusable egg without CUSTOM_DATA.id.
        if (liquidEgg && (parts.length != 2 || LiquidEggType.fromId(parts[1]).isEmpty())) {
            return ItemStack.EMPTY;
        }
        // Fluid descriptors are meaningful only for the original liquid egg.
        if (!liquidEgg && parts.length == 2) return ItemStack.EMPTY;

        ItemStack result = new ItemStack(item);
        if (parts.length == 2) {
            // Legacy liquid eggs stored the fluid id; port uses CUSTOM_DATA.
            CustomData.update(DataComponents.CUSTOM_DATA, result, nbt -> nbt.putString("id", parts[1]));
        }
        return result;
    }
}
