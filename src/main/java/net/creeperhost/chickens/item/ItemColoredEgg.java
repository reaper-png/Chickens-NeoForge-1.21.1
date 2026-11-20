package net.creeperhost.chickens.item;

import net.creeperhost.chickens.entity.EntityColoredEgg;
import net.creeperhost.chickens.init.ModEntities;
import net.creeperhost.chickens.registry.ChickenSpecies;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

/** Original throw-and-hatch colored eggs; species retained in CUSTOM_DATA. */
public final class ItemColoredEgg extends ItemChicken {
    public ItemColoredEgg(Properties properties) { super(properties); }
    @Override public InteractionResult useOn(UseOnContext context) { return InteractionResult.PASS; }
    @Override public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ChickenSpecies species = ChickenItemData.type(stack);
        if (species == null) return InteractionResultHolder.fail(stack);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.EGG_THROW, SoundSource.NEUTRAL, 0.5f,
                0.4f / (level.random.nextFloat() * 0.4f + 0.8f));
        if (!level.isClientSide) {
            EntityColoredEgg egg = ModEntities.EGG.get().create(level);
            if (egg == null) return InteractionResultHolder.fail(stack);
            egg.setChickenType("chickens:" + species.id());
            egg.setItem(stack.copyWithCount(1));
            egg.setPos(player.getX(), player.getEyeY() - .1, player.getZ());
            egg.setOwner(player);
            egg.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.5f, 1f);
            if (!level.addFreshEntity(egg)) return InteractionResultHolder.fail(stack);
            if (!player.getAbilities().instabuild) stack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
