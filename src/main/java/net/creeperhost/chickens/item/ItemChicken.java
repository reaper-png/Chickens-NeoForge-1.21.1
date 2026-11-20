package net.creeperhost.chickens.item;

import java.util.List;
import net.creeperhost.chickens.data.ChickenStats;
import net.creeperhost.chickens.data.ChickenStatsCodec;
import net.creeperhost.chickens.entity.EntityChickensChicken;
import net.creeperhost.chickens.init.ModEntities;
import net.creeperhost.chickens.registry.ChickenSpecies;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;

/** The 1.0.32 single chicken-item ID; variants live in CustomData, not separate registry entries. */
public class ItemChicken extends Item {
    public ItemChicken(Properties properties) { super(properties); }

    @Override
    public Component getName(ItemStack stack) {
        ChickenSpecies breed = ChickenItemData.type(stack);
        return breed == null ? super.getName(stack) :
                Component.translatable("entity.chickens." + breed.id());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        if (ChickenItemData.type(stack) == null) return;
        ChickenStats stats = ChickenStatsCodec.read(stack);
        tooltip.add(Component.literal("Growth: " + stats.getGrowth()).withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.literal("Gain: " + stats.getGain()).withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.literal("Strength: " + stats.getStrength()).withStyle(ChatFormatting.DARK_PURPLE));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() == null) return InteractionResult.PASS;
        ItemStack stack = context.getItemInHand();
        ChickenSpecies breed = ChickenItemData.type(stack);
        if (breed == null) return InteractionResult.FAIL;
        if (context.getLevel().isClientSide) return InteractionResult.SUCCESS;
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        EntityChickensChicken chicken = ModEntities.create(breed, context.getLevel());
        if (chicken == null) return InteractionResult.FAIL;
        ChickenStats stats = ChickenStatsCodec.read(stack);
        chicken.applyStats(stats);
        chicken.setBaby(ChickenItemData.wasBaby(stack));
        if (ChickenItemData.loveTicks(stack) > 0) chicken.setInLoveTime(ChickenItemData.loveTicks(stack));
        chicken.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, context.getRotation(), 0);
        if (!context.getLevel().addFreshEntity(chicken)) return InteractionResult.FAIL;
        if (!context.getPlayer().getAbilities().instabuild) stack.shrink(1);
        return InteractionResult.SUCCESS;
    }
}
