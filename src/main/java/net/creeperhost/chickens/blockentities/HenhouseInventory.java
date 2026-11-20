package net.creeperhost.chickens.blockentities;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.items.ItemStackHandler;

/** One input (hay), one generated dirt output, nine collected-resource outputs. */
public final class HenhouseInventory extends ItemStackHandler {
    private final Runnable changed;
    public HenhouseInventory(Runnable changed) { super(11); this.changed = changed; }
    @Override public boolean isItemValid(int slot, ItemStack stack) {
        return slot == 0 && stack.is(Blocks.HAY_BLOCK.asItem());
    }
    @Override protected void onContentsChanged(int slot) { changed.run(); }
}
