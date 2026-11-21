package net.creeperhost.chickens.entity;

import net.creeperhost.chickens.init.ModEntities;
import net.creeperhost.chickens.init.ModItems;
import net.creeperhost.chickens.registry.ChickenSpecies;
import net.creeperhost.chickens.registry.ChickensRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/** Same projectile ID and hatch probabilities as original Chickens 1.0.32. */
public final class EntityColoredEgg extends ThrowableItemProjectile {
    private static final EntityDataAccessor<String> CHICKEN_TYPE =
            SynchedEntityData.defineId(EntityColoredEgg.class, EntityDataSerializers.STRING);

    public EntityColoredEgg(EntityType<? extends EntityColoredEgg> type, Level level) { super(type, level); }
    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CHICKEN_TYPE, "");
    }
    public void setChickenType(String id) { entityData.set(CHICKEN_TYPE, id); }
    @Override protected Item getDefaultItem() { return ModItems.COLOURED_EGG.get(); }
    @Override protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        hit.getEntity().hurt(damageSources().thrown(this, getOwner()), hit.getEntity() instanceof Blaze ? 3 : 0);
    }
    @Override protected void onHit(HitResult hit) {
        super.onHit(hit);
        if (!level().isClientSide) {
            ChickenSpecies species = ChickensRegistry.getByRegistryName(entityData.get(CHICKEN_TYPE));
            if (species != null) {
                int count = ColoredEggHatching.hatchCount(random::nextInt);
                for (int i = 0; i < count; i++) {
                    EntityChickensChicken chicken = ModEntities.create(species, level());
                    if (chicken != null) {
                        chicken.setPos(hit.getLocation().x, hit.getLocation().y, hit.getLocation().z);
                        level().addFreshEntity(chicken);
                    }
                }
            }
            level().broadcastEntityEvent(this, (byte) 3);
            discard();
        }
    }
    @Override public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("Type", entityData.get(CHICKEN_TYPE));
    }
    @Override public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setChickenType(tag.getString("Type"));
    }
}
