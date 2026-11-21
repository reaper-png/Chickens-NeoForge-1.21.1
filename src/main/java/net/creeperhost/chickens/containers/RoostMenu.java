package net.creeperhost.chickens.containers;

import net.creeperhost.chickens.blockentities.BlockEntityRoost;
import net.creeperhost.chickens.blockentities.MachineInventory;
import net.creeperhost.chickens.init.ModBlocks;
import net.creeperhost.chickens.init.ModMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;

public final class RoostMenu extends MachineMenu {
    public RoostMenu(int id, Inventory playerInv) {
        super(ModMenus.ROOST.get(), id, playerInv,
                new MachineInventory(MachineInventory.Kind.ROOST, ignored -> {}),
                new SimpleContainerData(1), ContainerLevelAccess.NULL, ModBlocks.ROOST.get(), null);
    }
    public RoostMenu(int id, Inventory playerInv, BlockEntityRoost machine) {
        super(ModMenus.ROOST.get(), id, playerInv, machine.inventory(), machine.data(),
                ContainerLevelAccess.create(machine.getLevel(), machine.getBlockPos()), ModBlocks.ROOST.get(), machine);
    }
}
