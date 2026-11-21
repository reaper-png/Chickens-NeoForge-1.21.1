package net.creeperhost.chickens.item;

import java.util.List;
import net.creeperhost.chickens.entity.EntityChickensChicken;
import net.creeperhost.chickens.init.ModEntities;
import net.creeperhost.chickens.registry.ChickenSpecies;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.context.UseOnContext;

/**
 * Original spawn eggs are separate from captured chicken items.
 * They spawn a fresh, analyzed adult chicken with default genetics.
 */
public final class ItemSpawnEgg extends ItemChicken {
    public ItemSpawnEgg(Properties properties) { super(properties); }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        // Spawn eggs do not represent a captured chicken or its genetic state.
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() == null) return InteractionResult.PASS;
        ChickenSpecies breed = ChickenItemData.type(context.getItemInHand());
        if (breed == null) return InteractionResult.FAIL;
        if (context.getLevel().isClientSide) return InteractionResult.SUCCESS;

        EntityChickensChicken chicken = ModEntities.create(breed, context.getLevel());
        if (chicken == null) return InteractionResult.FAIL;
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        chicken.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D,
                context.getRotation(), 0.0F);
        chicken.setAnalyzed(true);
        // The original spawn egg reads optional genetics and the legacy
        // baby/love flags. Unlike a captured chicken, it does not restore
        // exact age, analyzer status, or remaining lay timer.
        ItemStack egg = context.getItemInHand();
        chicken.applyStats(net.creeperhost.chickens.data.ChickenStatsCodec.read(egg));
        Integer capturedAge = ChickenItemData.capturedAgeTicks(egg);
        if (capturedAge != null) {
            chicken.setAge(capturedAge);
        } else {
            chicken.setBaby(ChickenItemData.wasBaby(egg));
        }
        int loveTime = ChickenItemData.loveTicks(egg);
        if (loveTime > 0) chicken.setInLoveTime(loveTime);

        if (!context.getLevel().addFreshEntity(chicken)) return InteractionResult.FAIL;
        if (!context.getPlayer().getAbilities().instabuild) context.getItemInHand().shrink(1);
        return InteractionResult.SUCCESS;
    }
}
