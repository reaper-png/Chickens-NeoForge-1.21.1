package net.creeperhost.chickens.containers;

import net.creeperhost.chickens.blockentities.BlockEntityBreeder;
import net.creeperhost.chickens.blockentities.MachineInventory;
import net.creeperhost.chickens.init.ModBlocks;
import net.creeperhost.chickens.init.ModMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;

public final class BreederMenu extends MachineMenu {
    public BreederMenu(int id, Inventory playerInv) {
        super(ModMenus.BREEDER.get(), id, playerInv,
                new MachineInventory(MachineInventory.Kind.BREEDER, ignored -> {}),
                new SimpleContainerData(1), ContainerLevelAccess.NULL, ModBlocks.BREEDER.get());
    }
    public BreederMenu(int id, Inventory playerInv, BlockEntityBreeder machine) {
        super(ModMenus.BREEDER.get(), id, playerInv, machine.inventory(), machine.data(),
                ContainerLevelAccess.create(machine.getLevel(), machine.getBlockPos()), ModBlocks.BREEDER.get());
    }
}
