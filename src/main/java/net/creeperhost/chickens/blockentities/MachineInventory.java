package net.creeperhost.chickens.blockentities;

import java.util.function.IntConsumer;
import net.creeperhost.chickens.init.ModItems;
import net.creeperhost.chickens.item.ChickenItemData;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.items.ItemStackHandler;

/** Five slots for Roost or six for Breeder; outputs cannot accept externally inserted items. */
public final class MachineInventory extends ItemStackHandler {
    public enum Kind { ROOST, BREEDER }
    private final Kind kind;
    private final IntConsumer changed;

    public MachineInventory(Kind kind, IntConsumer changed) {
        super(kind == Kind.ROOST ? 5 : 6);
        this.kind = kind;
        this.changed = changed;
    }

    public int firstOutput() { return kind == Kind.ROOST ? 1 : 3; }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (slot < 0 || slot >= getSlots() || stack.isEmpty()) return false;
        if (slot >= firstOutput()) return false;
        if (kind == Kind.BREEDER && slot == 2) return stack.is(Tags.Items.SEEDS);
        return stack.is(ModItems.CHICKEN_ITEM.get()) && ChickenItemData.type(stack) != null;
    }

    @Override
    public int getSlotLimit(int slot) {
        return (kind == Kind.ROOST ? slot == 0 : slot < 2) ? 16 : 64;
    }

    @Override
    protected void onContentsChanged(int slot) {
        changed.accept(slot);
    }
}
