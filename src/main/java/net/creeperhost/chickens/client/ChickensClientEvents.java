package net.creeperhost.chickens.client;

import java.util.Locale;
import net.creeperhost.chickens.ChickensMod;
import net.creeperhost.chickens.entity.EntityChickensChicken;
import net.creeperhost.chickens.init.ModEntities;
import net.creeperhost.chickens.init.ModBlocks;
import net.creeperhost.chickens.init.ModItems;
import net.creeperhost.chickens.init.ModMenus;
import net.creeperhost.chickens.client.screens.RoostScreen;
import net.creeperhost.chickens.client.screens.BreederScreen;
import net.creeperhost.chickens.client.screens.HenhouseScreen;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.creeperhost.chickens.item.ChickenItemData;
import net.creeperhost.chickens.item.ItemLiquidEgg;
import net.creeperhost.chickens.client.OpaqueTint;
import net.creeperhost.chickens.registry.ChickenSpecies;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.DyeColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

/** Dedicated client-side registry; original entity skins remain in textures/entity. */
@EventBusSubscriber(modid = ChickensMod.MODID, value = Dist.CLIENT)
public final class ChickensClientEvents {
    private ChickensClientEvents() {}

    public static final class Renderer extends ChickenRenderer {
        public Renderer(EntityRendererProvider.Context context) { super(context); }
        @Override
        public ResourceLocation getTextureLocation(Chicken entity) {
            return entity instanceof EntityChickensChicken chicken ? chicken.texture() : super.getTextureLocation(entity);
        }
    }

    @SubscribeEvent
    public static void onRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ModEntities.CHICKENS.values().forEach(holder -> event.registerEntityRenderer(holder.get(), Renderer::new));
        event.registerEntityRenderer(ModEntities.EGG.get(), ThrownItemRenderer::new);
        event.registerBlockEntityRenderer(ModBlocks.ROOST_TILE.get(), RoostBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.HENHOUSE.get(), HenhouseScreen::new);
        event.register(ModMenus.ROOST.get(), RoostScreen::new);
        event.register(ModMenus.BREEDER.get(), BreederScreen::new);
    }

    /** Register special rendering only for a captured chicken, never for an egg. */
    @SubscribeEvent
    public static void onClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            private final ChickenItemRenderer renderer = new ChickenItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        }, ModItems.CHICKEN_ITEM.get());
    }

    @SubscribeEvent
    public static void onColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, layer) -> ItemLiquidEgg.type(stack)
                .map(liquid -> OpaqueTint.fromRgb(liquid.tint()))
                .orElse(OpaqueTint.fromRgb(0xFFFFFF)), ModItems.LIQUID_EGG.get());
        event.register((stack, tint) -> {
            ChickenSpecies species = ChickenItemData.type(stack);
            if (species == null) return OpaqueTint.fromRgb(0xFFFFFF);
            int base = species.backgroundColor();
            int overlay = species.foregroundColor();
            if (!species.dyeColor().isEmpty()) {
                // Legacy dye chicken metadata stored the real dye separately.
                // Tint the egg itself with its dye, not the black fallback color.
                base = DyeColor.valueOf(species.dyeColor().toUpperCase(Locale.ROOT)).getTextColor();
                overlay = 0xF2F2F2;
            }
            return OpaqueTint.fromRgb(tint == 0 ? base : tint == 1 ? overlay : 0xFFFFFF);
        }, ModItems.SPAWN_EGG.get(), ModItems.COLOURED_EGG.get());
    }
}
