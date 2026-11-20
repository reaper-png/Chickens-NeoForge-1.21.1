package net.creeperhost.chickens.blockentities;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

/** All-or-nothing output insertion. Never consumes ingredients if output cannot fit. */
public final class MachineOutputs {
    private MachineOutputs() {}

    public static boolean canInsert(ItemStackHandler inventory, int first, ItemStack produced) {
        return plan(inventory, first, produced) != null;
    }

    public static boolean insert(ItemStackHandler inventory, int first, ItemStack produced) {
        List<ItemStack> next = plan(inventory, first, produced);
        if (next == null) return false;
        for (int i = first; i < inventory.getSlots(); i++) {
            ItemStack target = next.get(i - first);
            if (!ItemStack.matches(inventory.getStackInSlot(i), target)) {
                inventory.setStackInSlot(i, target);
            }
        }
        return true;
    }

    private static List<ItemStack> plan(ItemStackHandler inventory, int first, ItemStack produced) {
        if (produced.isEmpty() || first < 0 || first >= inventory.getSlots()) return null;
        int remaining = produced.getCount();
        List<ItemStack> slots = new ArrayList<>();
        for (int i = first; i < inventory.getSlots(); i++) slots.add(inventory.getStackInSlot(i).copy());
        // Stack with existing matching output first, then use empty slots.
        for (int pass = 0; pass < 2 && remaining > 0; pass++) {
            for (int i = first; i < inventory.getSlots() && remaining > 0; i++) {
                int index = i - first;
                ItemStack existing = slots.get(index);
                if (pass == 0 && existing.isEmpty()) continue;
                if (pass == 1 && !existing.isEmpty()) continue;
                if (!existing.isEmpty() && !ItemStack.isSameItemSameComponents(existing, produced)) continue;
                int max = Math.min(inventory.getSlotLimit(i), produced.getMaxStackSize());
                int space = max - existing.getCount();
                if (space <= 0) continue;
                int moved = Math.min(remaining, space);
                if (existing.isEmpty()) {
                    slots.set(index, produced.copyWithCount(moved));
                } else {
                    existing.grow(moved);
                }
                remaining -= moved;
            }
        }
        return remaining == 0 ? slots : null;
    }
}
