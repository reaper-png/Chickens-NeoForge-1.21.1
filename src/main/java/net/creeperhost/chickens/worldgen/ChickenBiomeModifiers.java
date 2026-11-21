package net.creeperhost.chickens.worldgen;

import com.mojang.serialization.MapCodec;
import net.creeperhost.chickens.ChickensMod;
import net.neoforged.neoforge.common.NeoForgeRegistries;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Codec registration for Chickens' configurable natural spawning. */
public final class ChickenBiomeModifiers {
    public static final DeferredRegister<MapCodec<? extends BiomeModifier>> SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, ChickensMod.MODID);

    public static final DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<ConfiguredChickenSpawns>>
            CONFIGURED_SPAWNS = SERIALIZERS.register("configured_spawns", () -> ConfiguredChickenSpawns.CODEC);

    private ChickenBiomeModifiers() {}
}
