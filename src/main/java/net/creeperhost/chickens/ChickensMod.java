package net.creeperhost.chickens;

import net.creeperhost.chickens.events.SmartChickenConversion;
import net.creeperhost.chickens.events.ChickenDeathDrops;
import net.creeperhost.chickens.config.ChickensConfig;
import net.creeperhost.chickens.config.SpeciesLayConfig;
import net.creeperhost.chickens.config.SpawnChance;
import net.creeperhost.chickens.init.ModBlocks;
import net.creeperhost.chickens.init.ModCreativeTabs;
import net.creeperhost.chickens.init.ModEntities;
import net.creeperhost.chickens.init.ModItems;
import net.creeperhost.chickens.init.ModMenus;
import net.creeperhost.chickens.item.LiquidEggItemHandler;
import net.creeperhost.chickens.registry.ChickenSpecies;
import net.creeperhost.chickens.registry.ChickensRegistry;
import net.creeperhost.chickens.worldgen.ChickenBiomeModifiers;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.minecraft.world.entity.animal.Chicken;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

/** Chickens 1.0.32 legacy-only NeoForge port, no ModernChickens dependencies. */
@Mod(ChickensMod.MODID)
public final class ChickensMod {
    public static final String MODID = "chickens";
    public ChickensMod(IEventBus modEventBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, ChickensConfig.SPEC);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlocks.BLOCK_ENTITIES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);
        ChickenBiomeModifiers.SERIALIZERS.register(modEventBus);
        modEventBus.addListener(ChickensMod::onAttributes);
        modEventBus.addListener(ChickensMod::onCapabilities);
        modEventBus.addListener(ChickensMod::onSpawnPlacements);
        modEventBus.addListener(ChickensMod::onCommonSetup);
        NeoForge.EVENT_BUS.addListener(SmartChickenConversion::onEntityInteract);
        NeoForge.EVENT_BUS.addListener(ChickenDeathDrops::onLivingDrops);
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(SpeciesLayConfig::initialize);
    }

    private static void onCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.FluidHandler.ITEM,
                (stack, context) -> new LiquidEggItemHandler(stack), ModItems.LIQUID_EGG.get());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlocks.HEN_HOUSE_TILE.get(),
                (henhouse, side) -> henhouse.automation());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlocks.ROOST_TILE.get(),
                (roost, side) -> roost.automation());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlocks.BREEDER_TILE.get(),
                (breeder, side) -> breeder.automation());
    }

    /**
     * Port of the legacy Chickens spawn predicate.
     * Biome spawn lists are supplied separately by NeoForge biome modifiers.
     */
    private static void onSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        for (ChickenSpecies species : ChickensRegistry.getItems()) {
            if (species.spawnType() == ChickenSpecies.SpawnType.NONE) {
                continue;
            }
            var holder = ModEntities.CHICKENS.get(species.id());
            if (holder == null) {
                throw new IllegalStateException("Missing registered chicken: " + species.id());
            }
            event.register(
                    holder.get(),
                    SpawnPlacementTypes.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    (type, level, reason, pos, random) -> canNaturallySpawn(species, level, reason, pos, random),
                    RegisterSpawnPlacementsEvent.Operation.REPLACE);
        }
    }

    private static boolean canNaturallySpawn(
            ChickenSpecies species, ServerLevelAccessor level, MobSpawnType reason,
            BlockPos pos, RandomSource random) {
        // Placement restrictions configured for natural spawning must not block
        // other spawn reasons (commands, eggs, spawners, conversions, etc.).
        if (reason != MobSpawnType.NATURAL && reason != MobSpawnType.CHUNK_GENERATION) {
            return true;
        }
        if (!SpeciesLayConfig.isEnabled(species.id())) {
            return false;
        }
        if (!level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON)) {
            return false;
        }
        if (level.getRawBrightness(pos, 0) <= 8) {
            return false;
        }
        boolean allowedDimension = switch (species.spawnType()) {
            case NONE -> false;
            case NORMAL -> level.dimensionType().hasSkyLight();
            case HELL -> level.dimensionType().ultraWarm();
            case SNOW -> true; // The original SNOW predicate has no dimension check.
        };
        return allowedDimension && SpawnChance.allows(
                SpeciesLayConfig.naturalSpawnChanceFor(species.id()), random::nextInt);
    }

    private static void onAttributes(EntityAttributeCreationEvent event) {
        ModEntities.CHICKENS.values().forEach(holder -> event.put(holder.get(), Chicken.createAttributes().build()));
    }
}
