package net.creeperhost.chickens.compat.jade;

import net.creeperhost.chickens.ChickensMod;
import net.creeperhost.chickens.entity.EntityChickensChicken;
import net.creeperhost.chickens.registry.ChickenSpecies;
import net.creeperhost.chickens.registry.ChickensRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;

/**
 * Optional Jade overlay for live Chickens entities.
 *
 * All accessed state is already SynchedEntityData. No new networking, TOP
 * dependency or changes to Roost/machine entity renderers are needed.
 */
@WailaPlugin
public final class ChickensJadePlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(ChickenInfo.INSTANCE, EntityChickensChicken.class);
    }

    private enum ChickenInfo implements IEntityComponentProvider {
        INSTANCE;

        private static final ResourceLocation UID =
                ResourceLocation.fromNamespaceAndPath(ChickensMod.MODID, "chicken_info");

        @Override
        public ResourceLocation getUid() {
            return UID;
        }

        @Override
        public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
            if (!(accessor.getEntity() instanceof EntityChickensChicken chicken)) {
                return;
            }
            ChickenSpecies breed = chicken.species();
            if (breed == null) {
                return;
            }

            tooltip.add(Component.translatable("entity.ChickensChicken.top.tier")
                    .append(": " + ChickensRegistry.getTier(breed)));

            if (chicken.isAnalyzed()) {
                var stats = chicken.stats();
                tooltip.add(Component.translatable("entity.ChickensChicken.top.growth")
                        .append(": " + stats.getGrowth()));
                tooltip.add(Component.translatable("entity.ChickensChicken.top.gain")
                        .append(": " + stats.getGain()));
                tooltip.add(Component.translatable("entity.ChickensChicken.top.strength")
                        .append(": " + stats.getStrength()));
            }

            // Jade already supplies its own chicken egg countdown.
            // Do not append a second, approximate timer here.
            // This overlay only adds Chickens-specific tier and analyzed stats.
        }
    }
}
