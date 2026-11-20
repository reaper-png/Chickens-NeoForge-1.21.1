package net.creeperhost.chickens.client.screens;

import net.creeperhost.chickens.containers.RoostMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public final class RoostScreen extends MachineScreen<RoostMenu> {
    public RoostScreen(RoostMenu menu, Inventory inventory, Component title) { super(menu, inventory, title, "roost"); }
}
