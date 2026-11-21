package net.creeperhost.chickens.item;

import net.creeperhost.chickens.entity.EntityChickensChicken;
import net.creeperhost.chickens.init.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/** Captures the original breed/stats, not a vanilla chicken. */
public final class ItemChickenCatcher extends Item {
    public ItemChickenCatcher(Properties properties) { super(properties); }

    @Override
    public InteractionResult interactLivingEntity(ItemStack tool, Player player, LivingEntity target, InteractionHand hand) {
        if (!(target instanceof EntityChickensChicken chicken)) return InteractionResult.PASS;
        if (player.level().isClientSide) return InteractionResult.SUCCESS;
        var species = chicken.species();
        if (species == null) return InteractionResult.FAIL;
        ItemStack captured = ChickenItemData.create(ModItems.CHICKEN_ITEM.get(), species);
        net.creeperhost.chickens.data.ChickenStatsCodec.write(captured, chicken.stats());
        ChickenItemData.saveCapturedState(captured, chicken.getAge(), chicken.getInLoveTime(),
                chicken.isAnalyzed());
        ChickenItemData.saveCapturedLayTicks(captured, chicken.getLayTicks());
        // Original Chickens 1.0.32 spawned the captured item at the
        // chicken's location; it was never inserted straight into inventory.
        ItemEntity dropped = new ItemEntity(player.level(),
                chicken.getX(), chicken.getY(), chicken.getZ(), captured);
        dropped.setDefaultPickUpDelay();
        if (!player.level().addFreshEntity(dropped)) return InteractionResult.FAIL;
        chicken.discard();
        return InteractionResult.SUCCESS;
    }
}
