package net.creeperhost.chickens.compat.jei;

import net.minecraft.world.item.ItemStack;

/** Presentation-only JEI recipe, never consumed by the machine runtime. */
public record ChickenJeiRecipe(ItemStack first, ItemStack second, ItemStack output) {}
