package net.creeperhost.chickens.init;

import net.creeperhost.chickens.ChickensMod;
import net.creeperhost.chickens.containers.BreederMenu;
import net.creeperhost.chickens.containers.HenhouseMenu;
import net.creeperhost.chickens.containers.RoostMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Original menu IDs for save and network compatibility. */
public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, ChickensMod.MODID);
    public static final DeferredHolder<MenuType<?>, MenuType<HenhouseMenu>> HENHOUSE =
            MENUS.register("henhouse", () -> new MenuType<>(HenhouseMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final DeferredHolder<MenuType<?>, MenuType<RoostMenu>> ROOST =
            MENUS.register("roost", () -> new MenuType<>(RoostMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final DeferredHolder<MenuType<?>, MenuType<BreederMenu>> BREEDER =
            MENUS.register("breeder", () -> new MenuType<>(BreederMenu::new, FeatureFlags.DEFAULT_FLAGS));
    private ModMenus() {}
}
