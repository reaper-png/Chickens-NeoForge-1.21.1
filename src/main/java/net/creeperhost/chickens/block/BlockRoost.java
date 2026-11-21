package net.creeperhost.chickens.block;

import com.mojang.serialization.MapCodec;
import net.creeperhost.chickens.blockentities.BlockEntityRoost;
import net.creeperhost.chickens.blockentities.MachineInventoryDrops;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/** Visual model remains the original facing roost; adds the real machine block entity. */
public final class BlockRoost extends LegacyFacingBlock implements EntityBlock {
    public static final MapCodec<BlockRoost> CODEC = simpleCodec(BlockRoost::new);
    public BlockRoost(Properties properties) { super(properties); }
    @Override protected MapCodec<? extends net.minecraft.world.level.block.HorizontalDirectionalBlock> codec() { return CODEC; }
    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new BlockEntityRoost(pos, state); }
    @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (world, pos, block, be) -> { if (be instanceof BlockEntityRoost roost) BlockEntityRoost.tick(world, pos, block, roost); };
    }
    @Override public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof MenuProvider provider ? provider : null;
    }
    @Override public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            MenuProvider provider = getMenuProvider(state, level, pos);
            if (provider != null) serverPlayer.openMenu(provider);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
    @Override public void onRemove(BlockState state, Level level, BlockPos pos, BlockState nextState, boolean moved) {
        if (!state.is(nextState.getBlock()) && !level.isClientSide && level.getBlockEntity(pos) instanceof BlockEntityRoost roost) {
            MachineInventoryDrops.drain(level, pos, roost.inventory());
        }
        super.onRemove(state, level, pos, nextState, moved);
    }
}
