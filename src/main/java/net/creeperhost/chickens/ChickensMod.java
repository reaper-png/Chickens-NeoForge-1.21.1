package net.creeperhost.chickens;

import net.creeperhost.chickens.entity.EntityChickensChicken;
import net.creeperhost.chickens.init.ModBlocks;
import net.creeperhost.chickens.init.ModCreativeTabs;
import net.creeperhost.chickens.init.ModEntities;
import net.creeperhost.chickens.init.ModItems;
import net.creeperhost.chickens.init.ModMenus;
import net.creeperhost.chickens.item.LiquidEggItemHandler;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.minecraft.world.entity.animal.Chicken;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

/** Chickens 1.0.32 legacy-only NeoForge port, no ModernChickens dependencies. */
@Mod(ChickensMod.MODID)
public final class ChickensMod {
    public static final String MODID = "chickens";
    public ChickensMod(IEventBus modEventBus) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlocks.BLOCK_ENTITIES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);
        modEventBus.addListener(ChickensMod::onAttributes);
        modEventBus.addListener(ChickensMod::onCapabilities);
    }

    private static void onCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.FluidHandler.ITEM,
                (stack, context) -> new LiquidEggItemHandler(stack), ModItems.LIQUID_EGG.get());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlocks.HEN_HOUSE_TILE.get(),
                (henhouse, side) -> henhouse.automation());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlocks.ROOST_TILE.get(),
                (roost, side) -> roost.automation());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlocks.BREEDER_TILE.get(),
                (breeder, side) -> breeder.automation());
    }

    private static void onAttributes(EntityAttributeCreationEvent event) {
        ModEntities.CHICKENS.values().forEach(holder -> event.put(holder.get(), Chicken.createAttributes().build()));
    }
}
