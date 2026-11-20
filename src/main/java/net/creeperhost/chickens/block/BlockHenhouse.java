package net.creeperhost.chickens.block;

import com.mojang.serialization.MapCodec;
import net.creeperhost.chickens.blockentities.BlockEntityHenhouse;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/** All six original 1.0.32 henhouse wooden variants have the same inventory. */
public final class BlockHenhouse extends LegacyFacingBlock implements EntityBlock {
    public static final MapCodec<BlockHenhouse> CODEC = simpleCodec(BlockHenhouse::new);
    public BlockHenhouse(Properties properties) { super(properties); }
    @Override protected MapCodec<? extends HorizontalDirectionalBlock> codec() { return CODEC; }
    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityHenhouse(pos, state);
    }
    @Override public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof MenuProvider provider ? provider : null;
    }
    @Override public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                                       Player player, BlockHitResult hit) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            MenuProvider provider = getMenuProvider(state, level, pos);
            if (provider != null) serverPlayer.openMenu(provider);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
    @Override public void onRemove(BlockState state, Level level, BlockPos pos,
                                   BlockState next, boolean moved) {
        if (!state.is(next.getBlock()) && !level.isClientSide &&
                level.getBlockEntity(pos) instanceof BlockEntityHenhouse henhouse) {
            for (int slot = 0; slot < henhouse.inventory().getSlots(); slot++) {
                ItemStack stack = henhouse.inventory().getStackInSlot(slot);
                if (!stack.isEmpty()) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack.copy());
                }
            }
        }
        super.onRemove(state, level, pos, next, moved);
    }
}
