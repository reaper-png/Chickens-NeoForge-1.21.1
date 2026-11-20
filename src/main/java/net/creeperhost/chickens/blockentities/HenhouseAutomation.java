package net.creeperhost.chickens.blockentities;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

/** Automation can insert hay and extract products/dirt, never pull input hay. */
public final class HenhouseAutomation implements IItemHandler {
    private final HenhouseInventory inventory;
    public HenhouseAutomation(HenhouseInventory inventory) { this.inventory = inventory; }
    @Override public int getSlots() { return inventory.getSlots(); }
    @Override public ItemStack getStackInSlot(int slot) { return inventory.getStackInSlot(slot); }
    @Override public int getSlotLimit(int slot) { return inventory.getSlotLimit(slot); }
    @Override public boolean isItemValid(int slot, ItemStack stack) { return inventory.isItemValid(slot, stack); }
    @Override public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return slot == 0 ? inventory.insertItem(slot, stack, simulate) : stack;
    }
    @Override public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return slot >= 1 ? inventory.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }
}
