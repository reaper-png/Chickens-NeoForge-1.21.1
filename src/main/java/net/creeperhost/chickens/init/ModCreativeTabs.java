package net.creeperhost.chickens.init;

import net.creeperhost.chickens.ChickensMod;
import net.creeperhost.chickens.item.ChickenItemData;
import net.creeperhost.chickens.item.ItemLiquidEgg;
import net.creeperhost.chickens.item.LiquidEggType;
import net.creeperhost.chickens.registry.ChickenSpecies;
import net.creeperhost.chickens.registry.ChickensRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.component.CustomData;
import java.util.Locale;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** One original chicken item ID, but all 46 typed variants in the creative inventory. */
public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ChickensMod.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CHICKENS =
            TABS.register("chickens", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.chickens"))
                    .icon(() -> ChickenItemData.create(ModItems.CHICKEN_ITEM.get(),
                            ChickensRegistry.getByRegistryName("smart_chicken")))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .displayItems((parameters, output) -> {
                        for (ChickenSpecies type : ChickensRegistry.getItems()) {
                            output.accept(ChickenItemData.create(ModItems.CHICKEN_ITEM.get(), type));
                        }
                        // Original ItemSpawnEgg exposed one spawn egg per breed.
                        // Keep registry IDs and type metadata identical.
                        for (ChickenSpecies type : ChickensRegistry.getItems()) {
                            output.accept(ChickenItemData.create(ModItems.SPAWN_EGG.get(), type));
                        }
                        // The original creative inventory also exposes all
                        // 16 dye-colored throwable eggs, not only chicken items.
                        for (ChickenSpecies type : ChickensRegistry.getItems()) {
                            if (type.dyeColor().isEmpty()) continue;
                            ItemStack egg = new ItemStack(ModItems.COLOURED_EGG.get());
                            int colourId = DyeColor.valueOf(
                                    type.dyeColor().toUpperCase(Locale.ROOT)).getId();
                            CustomData.update(DataComponents.CUSTOM_DATA, egg, tag -> {
                                tag.putString("id", "chickens:" + type.id());
                                tag.putInt("colourid", colourId);
                            });
                            output.accept(egg);
                        }
                        // Liquid eggs remain distinct from chicken entities and must be
                        // discoverable as usable items, as in the original mod.
                        output.accept(ItemLiquidEgg.of(LiquidEggType.WATER));
                        output.accept(ItemLiquidEgg.of(LiquidEggType.LAVA));
                        output.accept(ModItems.CATCHER_ITEM.get());
                        output.accept(ModItems.ANALYZER.get());
                        output.accept(ModItems.HEN_HOUSE_ITEM.get());
                        output.accept(ModItems.HEN_HOUSE_ACACIA_ITEM.get());
                        output.accept(ModItems.HEN_HOUSE_BIRCH_ITEM.get());
                        output.accept(ModItems.HEN_HOUSE_DARK_OAK_ITEM.get());
                        output.accept(ModItems.HEN_HOUSE_JUNGLE_ITEM.get());
                        output.accept(ModItems.HEN_HOUSE_SPRUCE_ITEM.get());
                        output.accept(ModItems.BREEDER_ITEM.get());
                        output.accept(ModItems.ROOST_ITEM.get());
                    }).build());
    private ModCreativeTabs() {}
}
