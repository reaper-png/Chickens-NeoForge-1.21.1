package net.creeperhost.chickens.item;

import net.creeperhost.chickens.entity.EntityChickensChicken;
import net.creeperhost.chickens.data.ChickenStats;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/** Reads original Growth/Gain/Strength values without mutating the chicken. */
public final class ItemAnalyzer extends Item {
    public ItemAnalyzer(Properties properties) { super(properties); }
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!(target instanceof EntityChickensChicken chicken)) return InteractionResult.PASS;
        if (!player.level().isClientSide) {
            ChickenStats stats = chicken.stats();
            chicken.setAnalyzed(true);
            player.displayClientMessage(
                    Component.translatable("entity.ChickensChicken.top.growth")
                            .append(": " + stats.getGrowth() + " | ")
                            .append(Component.translatable("entity.ChickensChicken.top.gain"))
                            .append(": " + stats.getGain() + " | ")
                            .append(Component.translatable("entity.ChickensChicken.top.strength"))
                            .append(": " + stats.getStrength()), true);
        }
        return InteractionResult.SUCCESS;
    }
}
