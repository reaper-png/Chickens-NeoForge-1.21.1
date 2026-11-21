package net.creeperhost.chickens.containers;

import net.creeperhost.chickens.blockentities.MachineInventory;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.SlotItemHandler;

/** Shared UI slot and quick-move behavior; machines own the authoritative inventories. */
public abstract class MachineMenu extends AbstractContainerMenu {
    protected final MachineInventory contents;
    private final ContainerLevelAccess access;
    private final Block block;
    private final BlockEntity openedMachine;
    private final ContainerData data;

    protected MachineMenu(MenuType<?> type, int id, Inventory playerInv, MachineInventory contents,
                          ContainerData data, ContainerLevelAccess access, Block block,
                          BlockEntity openedMachine) {
        super(type, id);
        this.contents = contents;
        this.access = access;
        this.block = block;
        this.openedMachine = openedMachine;
        this.data = data;
        for (int i = 0; i < contents.getSlots(); i++) {
            int x;
            if (contents.getSlots() == 5) {
                x = i == 0 ? 26 : 80 + (i - 1) * 18;
            } else {
                x = i == 0 ? 44 : i == 1 ? 62 : i == 2 ? 8 : 116 + (i - 3) * 18;
            }
            final int slot = i;
            this.addSlot(new SlotItemHandler(contents, slot, x, 20) {
                @Override public boolean mayPlace(ItemStack stack) { return contents.isItemValid(slot, stack); }
            });
        }
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(playerInv, x + (y + 1) * 9, 8 + x * 18, 51 + y * 18));
            }
        }
        for (int x = 0; x < 9; x++) this.addSlot(new Slot(playerInv, x, 8 + x * 18, 109));
        this.addDataSlots(data);
    }

    public int progress() { return data.get(0); }
    @Override public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(access, player, block)
                && access.evaluate((level, pos) ->
                        openedMachine != null && level.getBlockEntity(pos) == openedMachine, true);
    }
    @Override public ItemStack quickMoveStack(Player player, int slotIndex) {
        if (slotIndex < 0 || slotIndex >= this.slots.size()) return ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack source = slot.getItem();
        ItemStack original = source.copy();
        int machineEnd = contents.getSlots();
        if (slotIndex < machineEnd) {
            if (!moveItemStackTo(source, machineEnd, this.slots.size(), true)) return ItemStack.EMPTY;
        } else {
            boolean moved = moveItemStackTo(source, 0, contents.firstOutput(), false);
            if (!moved) {
                int inventoryMiddle = machineEnd + 27;
                if (slotIndex < inventoryMiddle) moved = moveItemStackTo(source, inventoryMiddle, this.slots.size(), false);
                else moved = moveItemStackTo(source, machineEnd, inventoryMiddle, false);
            }
            if (!moved) return ItemStack.EMPTY;
        }
        if (source.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();
        slot.onTake(player, source);
        return original;
    }
}
