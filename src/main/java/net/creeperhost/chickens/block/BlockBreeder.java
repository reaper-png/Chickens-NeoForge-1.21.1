package net.creeperhost.chickens.block;

import com.mojang.serialization.MapCodec;
import net.creeperhost.chickens.blockentities.BlockEntityBreeder;
import net.creeperhost.chickens.blockentities.MachineInventoryDrops;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

/** Keeps the original breeder's two boolean model properties. */
public final class BlockBreeder extends BaseEntityBlock {
    public static final BooleanProperty IS_BREEDING = BooleanProperty.create("is_breeding");
    public static final BooleanProperty HAS_SEEDS = BooleanProperty.create("has_seeds");
    public static final MapCodec<BlockBreeder> CODEC = simpleCodec(BlockBreeder::new);
    public BlockBreeder(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(HAS_SEEDS, false).setValue(IS_BREEDING, false));
    }
    @Override protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(HAS_SEEDS, IS_BREEDING);
    }
    @Override public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }
    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new BlockEntityBreeder(pos, state); }
    @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (world, pos, block, be) -> { if (be instanceof BlockEntityBreeder breeder) BlockEntityBreeder.tick(world, pos, block, breeder); };
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
        if (!state.is(nextState.getBlock()) && !level.isClientSide && level.getBlockEntity(pos) instanceof BlockEntityBreeder breeder) {
            MachineInventoryDrops.drain(level, pos, breeder.inventory());
        }
        super.onRemove(state, level, pos, nextState, moved);
    }
}
