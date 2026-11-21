package net.creeperhost.chickens.entity;

import net.creeperhost.chickens.data.ChickenStats;
import net.creeperhost.chickens.blockentities.BlockEntityHenhouse;
import net.creeperhost.chickens.breeding.ChickenGenetics;
import net.creeperhost.chickens.registry.ChickenProducts;
import net.creeperhost.chickens.init.ModEntities;
import net.creeperhost.chickens.init.ModItems;
import net.creeperhost.chickens.registry.ChickenSpecies;
import net.creeperhost.chickens.registry.ChickensRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

/** First runtime port of the original 1.0.32 resource chickens: pedigree, breeding, stats, laying, save data. */
public class EntityChickensChicken extends Chicken {
    private static final EntityDataAccessor<Integer> GROWTH = SynchedEntityData.defineId(EntityChickensChicken.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> GAIN = SynchedEntityData.defineId(EntityChickensChicken.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> STRENGTH = SynchedEntityData.defineId(EntityChickensChicken.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ANALYZED = SynchedEntityData.defineId(EntityChickensChicken.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> LAY_PROGRESS = SynchedEntityData.defineId(EntityChickensChicken.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(EntityChickensChicken.class, EntityDataSerializers.STRING);
    private int layTicks;

    public EntityChickensChicken(EntityType<? extends Chicken> type, Level level) {
        super(type, level);
        // Never lay a vanilla egg; resource-laying uses an independent timer.
        this.eggTime = Integer.MAX_VALUE;
        this.layTicks = 6000;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(GROWTH, 1);
        builder.define(GAIN, 1);
        builder.define(STRENGTH, 1);
        builder.define(ANALYZED, false);
        builder.define(LAY_PROGRESS, 0);
        builder.define(TYPE, "");
    }

    public ChickenSpecies species() {
        ChickenSpecies data = ChickensRegistry.getByRegistryName(this.entityData.get(TYPE));
        if (data != null) return data;
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(getType());
        return ChickensRegistry.getByRegistryName(id.toString());
    }

    public ResourceLocation texture() {
        ChickenSpecies type = species();
        String id = type == null ? "smart_chicken" : type.id();
        return ResourceLocation.fromNamespaceAndPath("chickens", "textures/entity/" + id + ".png");
    }

    public ChickenStats stats() {
        return new ChickenStats(entityData.get(GAIN), entityData.get(GROWTH), entityData.get(STRENGTH));
    }

    public void applyStats(ChickenStats stats) {
        entityData.set(GROWTH, stats.getGrowth());
        entityData.set(GAIN, stats.getGain());
        entityData.set(STRENGTH, stats.getStrength());
    }

    public boolean isAnalyzed() { return entityData.get(ANALYZED); }
    public void setAnalyzed(boolean value) { entityData.set(ANALYZED, value); }
    public int getLayProgress() { return entityData.get(LAY_PROGRESS); }

    /** Avoid redundant data writes during the many ticks in the same progress band. */
    private void syncLayProgress() {
        int remainingBand = LayProgressMath.remainingBand(layTicks);
        if (LayProgressMath.needsSync(entityData.get(LAY_PROGRESS), remainingBand)) {
            entityData.set(LAY_PROGRESS, remainingBand);
        }
    }

    /** Exact number of ticks remaining until the next resource-laying cycle. */
    public int getLayTicks() { return layTicks; }

    /** Restores a captured chicken's existing laying cycle without rerolling it. */
    public void restoreLayTicks(int remainingTicks) {
        layTicks = Math.max(1, remainingTicks);
        syncLayProgress();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide || isBaby() || isChickenJockey()) return;
        ChickenSpecies breed = species();
        if (breed == null) return;
        if (--layTicks > 0) {
            syncLayProgress();
            return;
        }
        ItemStack laid = ChickenProducts.produce(breed);
        if (!laid.isEmpty()) {
            int extra = entityData.get(GAIN) >= 10 ? 2 : entityData.get(GAIN) >= 5 ? 1 : 0;
            laid.grow(laid.getCount() * extra);
            ItemStack remainder = BlockEntityHenhouse.collectNearby(level(), position(), laid);
            if (!remainder.isEmpty()) spawnAtLocation(remainder);
            playSound(SoundEvents.CHICKEN_EGG, 1f, 0.9f + random.nextFloat() * 0.2f);
        }
        resetLayingTime(breed);
    }

    private void resetLayingTime(ChickenSpecies breed) {
        int min = ChickensRegistry.getMinLayTime(breed);
        int base = min + random.nextInt(min);
        int growth = entityData.get(GROWTH);
        layTicks = (int) Math.max(1, base * (11.0 - growth) / 10.0) * 2;
        syncLayProgress();
    }

    @Override
    public EntityChickensChicken getBreedOffspring(ServerLevel level, AgeableMob partner) {
        if (!(partner instanceof EntityChickensChicken mate)) return null;
        ChickenSpecies one = species();
        ChickenSpecies two = mate.species();
        if (one == null || two == null) return null;
        ChickenSpecies child = ChickensRegistry.getRandomChild(one, two, this.random::nextInt);
        // Not every pair has a valid offspring. Vanilla may request this directly,
        // including pairs not present in the original Chickens breeding table.
        if (child == null) return null;
        EntityChickensChicken result = ModEntities.create(child, level);
        if (result == null) return null;
        result.applyStats(ChickenGenetics.offspring(one, two, child, stats(), mate.stats(), this.random::nextInt));
        result.resetLayingTime(child);
        return result;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        ChickenSpecies breed = species();
        tag.putString("Type", breed == null ? "" : "chickens:" + breed.id());
        tag.putBoolean("Analyzed", isAnalyzed());
        tag.putInt("Growth", entityData.get(GROWTH));
        tag.putInt("Gain", entityData.get(GAIN));
        tag.putInt("Strength", entityData.get(STRENGTH));
        tag.putInt("EggLayTime", layTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        ChickenSpecies breed = ChickensRegistry.getByRegistryName(tag.getString("Type"));
        if (breed != null) entityData.set(TYPE, "chickens:" + breed.id());
        entityData.set(ANALYZED, tag.getBoolean("Analyzed"));
        applyStats(new ChickenStats(tag.contains("Gain") ? tag.getInt("Gain") : 1,
                tag.contains("Growth") ? tag.getInt("Growth") : 1,
                tag.contains("Strength") ? tag.getInt("Strength") : 1));
        layTicks = tag.contains("EggLayTime") ? Math.max(1, tag.getInt("EggLayTime")) : 6000;
        syncLayProgress();
    }
}
