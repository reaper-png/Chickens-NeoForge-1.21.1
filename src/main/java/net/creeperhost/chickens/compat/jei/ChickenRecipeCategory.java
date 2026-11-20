package net.creeperhost.chickens.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

/** Displays two parents -> offspring, or one chicken -> one resource. */
public final class ChickenRecipeCategory extends AbstractRecipeCategory<ChickenJeiRecipe> {
    private final boolean breeding;

    public ChickenRecipeCategory(IGuiHelper gui, RecipeType<ChickenJeiRecipe> type, Item icon,
                                 String title, boolean breeding) {
        super(type, Component.translatable(title),
                gui.createDrawableItemLike(icon), 116, 38);
        this.breeding = breeding;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ChickenJeiRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(1, 10).setStandardSlotBackground().addItemStack(recipe.first());
        if (breeding) {
            builder.addInputSlot(28, 10).setStandardSlotBackground().addItemStack(recipe.second());
        }
        builder.addOutputSlot(94, 10).setOutputSlotBackground().addItemStack(recipe.output());
    }
}
