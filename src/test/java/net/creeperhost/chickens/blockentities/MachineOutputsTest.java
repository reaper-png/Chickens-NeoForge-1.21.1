package net.creeperhost.chickens.blockentities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.junit.jupiter.api.Test;

final class MachineOutputsTest {
    @Test
    void fillsMatchingStacksBeforeUsingEmptySlots() {
        ItemStackHandler slots = new ItemStackHandler(3);
        slots.setStackInSlot(0, new ItemStack(Items.DIRT, 64)); // input, untouched
        slots.setStackInSlot(1, new ItemStack(Items.IRON_INGOT, 60));
        ItemStack output = new ItemStack(Items.IRON_INGOT, 8);
        assertTrue(MachineOutputs.canInsert(slots, 1, output));
        assertTrue(MachineOutputs.insert(slots, 1, output));
        assertEquals(64, slots.getStackInSlot(1).getCount());
        assertEquals(4, slots.getStackInSlot(2).getCount());
        assertEquals(64, slots.getStackInSlot(0).getCount());
    }

    @Test
    void refusesInsertionWhenAllOutputsAreFullOrIncompatible() {
        ItemStackHandler slots = new ItemStackHandler(3);
        slots.setStackInSlot(1, new ItemStack(Items.DIRT, 64));
        slots.setStackInSlot(2, new ItemStack(Items.IRON_INGOT, 64));
        assertFalse(MachineOutputs.canInsert(slots, 1, new ItemStack(Items.IRON_INGOT)));
        assertFalse(MachineOutputs.insert(slots, 1, new ItemStack(Items.IRON_INGOT)));
        assertEquals(64, slots.getStackInSlot(1).getCount());
        assertEquals(64, slots.getStackInSlot(2).getCount());
    }

    @Test
    void componentMismatchMustNotMergeIntoExistingOutput() {
        ItemStackHandler slots = new ItemStackHandler(2);
        ItemStack named = new ItemStack(Items.IRON_INGOT, 63);
        named.set(DataComponents.CUSTOM_NAME, Component.literal("Special"));
        slots.setStackInSlot(1, named);

        ItemStack ordinary = new ItemStack(Items.IRON_INGOT);
        assertFalse(MachineOutputs.canInsert(slots, 1, ordinary));
        assertFalse(MachineOutputs.insert(slots, 1, ordinary));
        assertEquals(63, slots.getStackInSlot(1).getCount());
    }

    @Test
    void capacityCheckIsReadOnlyAndRespectsPartialSpace() {
        ItemStackHandler slots = new ItemStackHandler(2);
        slots.setStackInSlot(1, new ItemStack(Items.IRON_INGOT, 62));
        assertTrue(MachineOutputs.canInsert(slots, 1, new ItemStack(Items.IRON_INGOT, 2)));
        assertFalse(MachineOutputs.canInsert(slots, 1, new ItemStack(Items.IRON_INGOT, 3)));
        assertEquals(62, slots.getStackInSlot(1).getCount());
    }
}
