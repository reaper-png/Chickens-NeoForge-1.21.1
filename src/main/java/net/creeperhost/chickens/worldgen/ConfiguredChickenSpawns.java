package net.creeperhost.chickens.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.creeperhost.chickens.config.ChickensConfig;
import net.creeperhost.chickens.config.SpawnGroupSettings;
import net.creeperhost.chickens.init.ModEntities;
import net.creeperhost.chickens.registry.ChickenSpecies;
import net.creeperhost.chickens.registry.ChickensRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

/**
 * Adds the original 27 Overworld and 3 Nether natural-spawn species with
 * configurable common spawn weight and pack sizes. Does not change biome tags.
 */
public record ConfiguredChickenSpawns(HolderSet<Biome> biomes, String region) implements BiomeModifier {
    public static final MapCodec<ConfiguredChickenSpawns> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Biome.LIST_CODEC.fieldOf("biomes").forGetter(ConfiguredChickenSpawns::biomes),
                    Codec.STRING.fieldOf("region").forGetter(ConfiguredChickenSpawns::region)
            ).apply(instance, ConfiguredChickenSpawns::new));

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase != Phase.ADD || !biomes.contains(biome)) return;
        SpawnGroupSettings settings = ChickensConfig.spawnGroupSettings();
        if (settings.weight() == 0) return;
        for (ChickenSpecies species : ChickensRegistry.getItems()) {
            boolean isOverworld = region.equals("overworld")
                    && (species.spawnType() == ChickenSpecies.SpawnType.NORMAL
                    || species.spawnType() == ChickenSpecies.SpawnType.SNOW);
            boolean isNether = region.equals("nether")
                    && species.spawnType() == ChickenSpecies.SpawnType.HELL;
            if (!isOverworld && !isNether) continue;
            var registered = ModEntities.CHICKENS.get(species.id());
            if (registered == null) throw new IllegalStateException("Missing chicken entity: " + species.id());
            var entity = registered.get();
            builder.getMobSpawnSettings().addSpawn(entity.getCategory(),
                    new MobSpawnSettings.SpawnerData(entity, settings.weight(),
                            settings.minCount(), settings.maxCount()));
        }
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return ChickenBiomeModifiers.CONFIGURED_SPAWNS.get();
    }
}
