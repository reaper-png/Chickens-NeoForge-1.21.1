package net.creeperhost.chickens.blockentities;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

/** Separate automation view: input can be inserted, output extracted, inputs never pulled by hoppers. */
public final class AutomationItemHandler implements IItemHandler {
    private final MachineInventory delegate;

    public AutomationItemHandler(MachineInventory delegate) { this.delegate = delegate; }
    @Override public int getSlots() { return delegate.getSlots(); }
    // An external integration must not receive a mutable reference to the
    // backing inventory. Changes must pass through insertItem/extractItem.
    @Override public ItemStack getStackInSlot(int slot) { return delegate.getStackInSlot(slot).copy(); }
    @Override public int getSlotLimit(int slot) { return delegate.getSlotLimit(slot); }
    @Override public boolean isItemValid(int slot, ItemStack stack) { return delegate.isItemValid(slot, stack); }
    @Override public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return delegate.isItemValid(slot, stack) ? delegate.insertItem(slot, stack, simulate) : stack;
    }
    @Override public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return slot < delegate.firstOutput() ? ItemStack.EMPTY : delegate.extractItem(slot, amount, simulate);
    }
}
