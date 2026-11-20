package net.creeperhost.chickens.client.screens;

import net.creeperhost.chickens.containers.HenhouseMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/** Original 176 x 166 GUI, including the 57px fuel bar. */
public final class HenhouseScreen extends AbstractContainerScreen<HenhouseMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("chickens", "textures/gui/henhouse.png");
    public HenhouseScreen(HenhouseMenu menu, Inventory playerInventory, Component name) {
        super(menu, playerInventory, name);
        imageWidth = 176;
        imageHeight = 166;
    }
    @Override protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        int bar = Math.max(0, Math.min(57, menu.energy() * 57 / 100));
        if (bar > 0) {
            graphics.blit(TEXTURE, leftPos + 75, topPos + 71 - bar,
                    195, 57 - bar, 12, bar);
        }
    }
    @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}
