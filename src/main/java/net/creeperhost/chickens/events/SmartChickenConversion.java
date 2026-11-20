package net.creeperhost.chickens.events;

import net.creeperhost.chickens.entity.EntityChickensChicken;
import net.creeperhost.chickens.init.ModEntities;
import net.creeperhost.chickens.registry.ChickensRegistry;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/** Legacy mechanic: right-click a vanilla chicken with a book to make it a Smart Chicken. */
public final class SmartChickenConversion {
    private SmartChickenConversion() {}

    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget().getType() != EntityType.CHICKEN
                || !(event.getTarget() instanceof Chicken original)) {
            return;
        }
        ItemStack book = event.getItemStack();
        if (!book.is(Items.BOOK)) {
            return;
        }

        if (event.getLevel().isClientSide()) {
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
            return;
        }

        var species = ChickensRegistry.getByRegistryName("smart_chicken");
        if (species == null) {
            return;
        }
        EntityChickensChicken smart = ModEntities.create(species, event.getLevel());
        if (smart == null) {
            return;
        }

        smart.moveTo(original.getX(), original.getY(), original.getZ(),
                original.getYRot(), original.getXRot());
        smart.setAge(original.getAge());
        smart.setHealth(Math.min(original.getHealth(), smart.getMaxHealth()));
        if (original.hasCustomName()) {
            smart.setCustomName(original.getCustomName());
            smart.setCustomNameVisible(original.isCustomNameVisible());
        }
        if (!event.getLevel().addFreshEntity(smart)) {
            return;
        }

        original.discard();
        if (!event.getEntity().getAbilities().instabuild) {
            book.shrink(1);
        }
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }
}
