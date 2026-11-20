package net.creeperhost.chickens.containers;

import net.creeperhost.chickens.blockentities.BlockEntityHenhouse;
import net.creeperhost.chickens.init.ModMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

/** Original Henhouse layout: hay input, dirt output, 3x3 product storage. */
public final class HenhouseMenu extends AbstractContainerMenu {
    private static final int MACHINE_SLOTS = 11;
    private final ContainerData data;
    private final ContainerLevelAccess access;

    public HenhouseMenu(int id, Inventory playerInv) {
        this(id, playerInv, new ItemStackHandler(MACHINE_SLOTS),
                new SimpleContainerData(1), ContainerLevelAccess.NULL);
    }
    public HenhouseMenu(int id, Inventory playerInv, BlockEntityHenhouse house) {
        this(id, playerInv, house.inventory(), house.data(),
                ContainerLevelAccess.create(house.getLevel(), house.getBlockPos()));
    }
    private HenhouseMenu(int id, Inventory playerInv, ItemStackHandler contents,
                         ContainerData data, ContainerLevelAccess access) {
        super(ModMenus.HENHOUSE.get(), id);
        this.data = data;
        this.access = access;
        addSlot(new SlotItemHandler(contents, 0, 25, 19) {
            @Override public boolean mayPlace(ItemStack stack) { return stack.is(Blocks.HAY_BLOCK.asItem()); }
        });
        addSlot(new SlotItemHandler(contents, 1, 25, 55) {
            @Override public boolean mayPlace(ItemStack stack) { return false; }
        });
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                addSlot(new SlotItemHandler(contents, 2 + row * 3 + column,
                        98 + column * 18, 17 + row * 18) {
                    @Override public boolean mayPlace(ItemStack stack) { return false; }
                });
            }
        }
        for (int row = 0; row < 3; row++) for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInv, col + (row + 1) * 9,
                    8 + col * 18, 84 + row * 18));
        }
        for (int col = 0; col < 9; col++) addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
        addDataSlots(data);
    }
    public int energy() { return data.get(0); }
    @Override public boolean stillValid(Player player) {
        return access.evaluate((level, pos) ->
                level.getBlockEntity(pos) instanceof BlockEntityHenhouse &&
                player.distanceToSqr(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5) <= 64, false);
    }
    @Override public ItemStack quickMoveStack(Player player, int slotIndex) {
        if (slotIndex < 0 || slotIndex >= slots.size()) return ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack current = slot.getItem();
        ItemStack original = current.copy();
        if (slotIndex < MACHINE_SLOTS) {
            if (!moveItemStackTo(current, MACHINE_SLOTS, slots.size(), true)) return ItemStack.EMPTY;
        } else if (current.is(Blocks.HAY_BLOCK.asItem()) && moveItemStackTo(current, 0, 1, false)) {
            // Filled the single hay slot.
        } else {
            int split = MACHINE_SLOTS + 27;
            boolean moved = slotIndex < split
                    ? moveItemStackTo(current, split, slots.size(), false)
                    : moveItemStackTo(current, MACHINE_SLOTS, split, false);
            if (!moved) return ItemStack.EMPTY;
        }
        if (current.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();
        slot.onTake(player, current);
        return original;
    }
}
