package net.creeperhost.chickens.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Consumes machine contents before spawning drops, avoiding duplicate item
 * entities if a block removal callback is invoked more than once.
 */
public final class MachineInventoryDrops {
    private MachineInventoryDrops() {}

    public static void drain(Level level, BlockPos pos, MachineInventory inventory) {
        if (level.isClientSide) return;
        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack.isEmpty()) continue;
            ItemStack dropped = stack.copy();
            inventory.setStackInSlot(slot, ItemStack.EMPTY);
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), dropped);
        }
    }
}
