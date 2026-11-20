package net.creeperhost.chickens.client.screens;

import net.creeperhost.chickens.containers.MachineMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/** Both original 176x132-pixel interfaces, without introducing new artwork. */
public abstract class MachineScreen<M extends MachineMenu> extends AbstractContainerScreen<M> {
    private final ResourceLocation texture;

    protected MachineScreen(M menu, Inventory playerInv, Component title, String textureName) {
        super(menu, playerInv, title);
        this.texture = ResourceLocation.fromNamespaceAndPath("chickens", "textures/gui/" + textureName + ".png");
        this.imageWidth = 176;
        this.imageHeight = 132;
        this.titleLabelX = 8;
        this.titleLabelY = 5;
        this.inventoryLabelY = 40;
    }

    private static final int TOTAL_PROGRESS = MachineProgress.CYCLE;
    private static final int BAR_WIDTH = MachineProgress.WIDTH;

    @Override protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(texture, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);
        int width = progressWidth(menu.progress());
        if (width > 0) {
            // Coordinates from the 1.0.32 GUIs: Roost (48,20) / Breeder (84,22).
            int x = isBreeder() ? 84 : 48;
            int y = isBreeder() ? 22 : 20;
            int height = isBreeder() ? 12 : 16;
            graphics.blit(texture, leftPos + x, topPos + y, 176, 0, width, height);
        }
    }

    private boolean isBreeder() { return this.menu instanceof net.creeperhost.chickens.containers.BreederMenu; }

    /** Maps the authoritative server progress to the original 26px GUI arrow. */
    public static int progressWidth(int progress) {
        return MachineProgress.width(progress);
    }

    @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
        int x = leftPos + (isBreeder() ? 84 : 48);
        int y = topPos + (isBreeder() ? 22 : 20);
        if (mouseX >= x && mouseX < x + BAR_WIDTH && mouseY >= y && mouseY < y + (isBreeder() ? 12 : 16)) {
            graphics.renderTooltip(font,
                    Component.literal(String.format(java.util.Locale.ROOT, "%.1f%%", MachineProgress.percent(menu.progress()))),
                    mouseX, mouseY);
        }
    }
}
