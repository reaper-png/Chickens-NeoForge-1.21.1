package net.creeperhost.chickens.init;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import net.creeperhost.chickens.ChickensMod;
import net.creeperhost.chickens.entity.EntityChickensChicken;
import net.creeperhost.chickens.entity.EntityColoredEgg;
import net.creeperhost.chickens.registry.ChickenSpecies;
import net.creeperhost.chickens.registry.ChickensRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Each chicken retains its original entity registry ID. No added species or machines. */
public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, ChickensMod.MODID);
    public static final DeferredHolder<EntityType<?>, EntityType<EntityColoredEgg>> EGG =
            ENTITIES.register("egg", () -> EntityType.Builder.<EntityColoredEgg>of(EntityColoredEgg::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10).build("chickens:egg"));
    public static final Map<String, DeferredHolder<EntityType<?>, EntityType<EntityChickensChicken>>> CHICKENS = register();

    private static Map<String, DeferredHolder<EntityType<?>, EntityType<EntityChickensChicken>>> register() {
        Map<String, DeferredHolder<EntityType<?>, EntityType<EntityChickensChicken>>> registered = new LinkedHashMap<>();
        for (ChickenSpecies type : ChickensRegistry.getItems()) {
            registered.put(type.id(), ENTITIES.register(type.id(), () ->
                    EntityType.Builder.of(EntityChickensChicken::new, MobCategory.CREATURE)
                            .sized(0.4f, 0.7f).clientTrackingRange(8)
                            .build(ChickensMod.MODID + ":" + type.id())));
        }
        return Collections.unmodifiableMap(registered);
    }

    public static EntityChickensChicken create(ChickenSpecies type, Level level) {
        var holder = CHICKENS.get(type.id());
        return holder == null ? null : holder.get().create(level);
    }

    private ModEntities() {}
}
