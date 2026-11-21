package net.creeperhost.chickens.init;

import net.creeperhost.chickens.ChickensMod;
import net.creeperhost.chickens.item.ItemChicken;
import net.creeperhost.chickens.item.ItemSpawnEgg;
import net.creeperhost.chickens.item.ItemColoredEgg;
import net.creeperhost.chickens.item.ItemChickenCatcher;
import net.creeperhost.chickens.item.ItemLiquidEgg;
import net.creeperhost.chickens.item.ItemAnalyzer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Preserves the original six item IDs and eight block-item IDs. */
public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ChickensMod.MODID);
    public static final DeferredItem<Item> SPAWN_EGG = ITEMS.register("spawn_egg", () -> new ItemSpawnEgg(new Item.Properties()));
    public static final DeferredItem<Item> COLOURED_EGG = ITEMS.register("colored_egg", () -> new ItemColoredEgg(new Item.Properties()));
    public static final DeferredItem<Item> LIQUID_EGG = ITEMS.register("liquid_egg", () -> new ItemLiquidEgg(new Item.Properties()));
    public static final DeferredItem<Item> ANALYZER = ITEMS.register("analyzer", () -> new ItemAnalyzer(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> CHICKEN_ITEM = ITEMS.register("chicken_item", () -> new ItemChicken(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> CATCHER_ITEM = ITEMS.register("catcher", () -> new ItemChickenCatcher(new Item.Properties()));
    public static final DeferredItem<BlockItem> HEN_HOUSE_ITEM = ITEMS.registerSimpleBlockItem("henhouse", ModBlocks.HEN_HOUSE);
    public static final DeferredItem<BlockItem> HEN_HOUSE_ACACIA_ITEM = ITEMS.registerSimpleBlockItem("henhouse_acacia", ModBlocks.HEN_HOUSE_ACACIA);
    public static final DeferredItem<BlockItem> HEN_HOUSE_BIRCH_ITEM = ITEMS.registerSimpleBlockItem("henhouse_birch", ModBlocks.HEN_HOUSE_BIRCH);
    public static final DeferredItem<BlockItem> HEN_HOUSE_DARK_OAK_ITEM = ITEMS.registerSimpleBlockItem("henhouse_dark_oak", ModBlocks.HEN_HOUSE_DARK_OAK);
    public static final DeferredItem<BlockItem> HEN_HOUSE_JUNGLE_ITEM = ITEMS.registerSimpleBlockItem("henhouse_jungle", ModBlocks.HEN_HOUSE_JUNGLE);
    public static final DeferredItem<BlockItem> HEN_HOUSE_SPRUCE_ITEM = ITEMS.registerSimpleBlockItem("henhouse_spruce", ModBlocks.HEN_HOUSE_SPRUCE);
    public static final DeferredItem<BlockItem> BREEDER_ITEM = ITEMS.registerSimpleBlockItem("breeder", ModBlocks.BREEDER);
    public static final DeferredItem<BlockItem> ROOST_ITEM = ITEMS.registerSimpleBlockItem("roost", ModBlocks.ROOST);
    private ModItems() {}
}
