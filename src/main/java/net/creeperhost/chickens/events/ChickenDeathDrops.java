package net.creeperhost.chickens.events;

import net.creeperhost.chickens.entity.EntityChickensChicken;
import net.creeperhost.chickens.registry.ChickenProducts;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;

/**
 * Adds only the explicitly configured extra death drop.
 * Retains normal loot-table results and other mods' drops.
 */
public final class ChickenDeathDrops {
    private ChickenDeathDrops() {}

    public static void onLivingDrops(LivingDropsEvent event) {
        if (!(event.getEntity() instanceof EntityChickensChicken chicken)
                || chicken.level().isClientSide) {
            return;
        }
        // Configured extras must obey the vanilla mob-loot gamerule.
        if (!chicken.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            return;
        }
        ItemStack extra = ChickenProducts.deathDrop(chicken.species());
        if (extra.isEmpty()) {
            return;
        }
        event.getDrops().add(new ItemEntity(chicken.level(),
                chicken.getX(), chicken.getY(), chicken.getZ(), extra));
    }
}
