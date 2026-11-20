package net.creeperhost.chickens.init;

import net.creeperhost.chickens.ChickensMod;
import net.creeperhost.chickens.block.BlockBreeder;
import net.creeperhost.chickens.block.BlockHenhouse;
import net.creeperhost.chickens.block.BlockRoost;
import net.creeperhost.chickens.block.LegacyFacingBlock;
import net.creeperhost.chickens.blockentities.BlockEntityBreeder;
import net.creeperhost.chickens.blockentities.BlockEntityHenhouse;
import net.creeperhost.chickens.blockentities.BlockEntityRoost;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Original registry IDs, including functional Henhouse wooden variants. */
public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ChickensMod.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ChickensMod.MODID);

    public static final DeferredBlock<Block> HEN_HOUSE = wood("henhouse");
    public static final DeferredBlock<Block> HEN_HOUSE_ACACIA = wood("henhouse_acacia");
    public static final DeferredBlock<Block> HEN_HOUSE_BIRCH = wood("henhouse_birch");
    public static final DeferredBlock<Block> HEN_HOUSE_DARK_OAK = wood("henhouse_dark_oak");
    public static final DeferredBlock<Block> HEN_HOUSE_JUNGLE = wood("henhouse_jungle");
    public static final DeferredBlock<Block> HEN_HOUSE_SPRUCE = wood("henhouse_spruce");
    public static final DeferredBlock<Block> BREEDER = BLOCKS.register("breeder", () -> new BlockBreeder(properties()));
    public static final DeferredBlock<Block> ROOST = BLOCKS.register("roost", () -> new BlockRoost(properties()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityHenhouse>> HEN_HOUSE_TILE =
            BLOCK_ENTITIES.register("henhouse", () -> BlockEntityType.Builder.of(BlockEntityHenhouse::new,
                    HEN_HOUSE.get(), HEN_HOUSE_ACACIA.get(), HEN_HOUSE_BIRCH.get(),
                    HEN_HOUSE_DARK_OAK.get(), HEN_HOUSE_JUNGLE.get(), HEN_HOUSE_SPRUCE.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityRoost>> ROOST_TILE =
            BLOCK_ENTITIES.register("roost", () -> BlockEntityType.Builder.of(BlockEntityRoost::new, ROOST.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityBreeder>> BREEDER_TILE =
            BLOCK_ENTITIES.register("breeder", () -> BlockEntityType.Builder.of(BlockEntityBreeder::new, BREEDER.get()).build(null));

    private static BlockBehaviour.Properties properties() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2f).noOcclusion();
    }
    private static DeferredBlock<Block> wood(String id) {
        return BLOCKS.register(id, () -> new BlockHenhouse(properties()));
    }
    private ModBlocks() {}
}
