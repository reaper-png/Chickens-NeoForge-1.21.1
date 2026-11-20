package net.creeperhost.chickens.client.screens;

import net.creeperhost.chickens.containers.BreederMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public final class BreederScreen extends MachineScreen<BreederMenu> {
    public BreederScreen(BreederMenu menu, Inventory inventory, Component title) { super(menu, inventory, title, "breeder"); }
}
