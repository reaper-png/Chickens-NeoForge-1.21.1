package net.creeperhost.chickens.compat.jei;

import java.util.ArrayList;
import java.util.List;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.creeperhost.chickens.ChickensMod;
import net.creeperhost.chickens.init.ModBlocks;
import net.creeperhost.chickens.init.ModItems;
import net.creeperhost.chickens.item.ChickenItemData;
import net.creeperhost.chickens.registry.ChickenProducts;
import net.creeperhost.chickens.registry.ChickenSpecies;
import net.creeperhost.chickens.registry.ChickensRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Optional JEI integration: it is loaded through JEI's @JeiPlugin discovery,
 * not referenced by the Chickens common entrypoint.
 */
@JeiPlugin
public final class ChickenJeiPlugin implements IModPlugin {
    static final RecipeType<ChickenJeiRecipe> BREEDING =
            RecipeType.create(ChickensMod.MODID, "breeding", ChickenJeiRecipe.class);
    static final RecipeType<ChickenJeiRecipe> LAYING =
            RecipeType.create(ChickensMod.MODID, "laying", ChickenJeiRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(ChickensMod.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var gui = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new ChickenRecipeCategory(gui, BREEDING, ModBlocks.BREEDER.get().asItem(),
                        "jei.chickens.breeding", true),
                new ChickenRecipeCategory(gui, LAYING, ModBlocks.ROOST.get().asItem(),
                        "jei.chickens.laying", false));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<ChickenJeiRecipe> breeding = new ArrayList<>();
        List<ChickenJeiRecipe> laying = new ArrayList<>();
        for (ChickenSpecies species : ChickensRegistry.getItems()) {
            ItemStack chicken = ChickenItemData.create(ModItems.CHICKEN_ITEM.get(), species);
            ItemStack product = ChickenProducts.produce(species);
            if (!product.isEmpty()) {
                laying.add(new ChickenJeiRecipe(chicken, ItemStack.EMPTY, product));
            }
            if (species.hasParents()) {
                ChickenSpecies a = ChickensRegistry.getByRegistryName(species.parentA());
                ChickenSpecies b = ChickensRegistry.getByRegistryName(species.parentB());
                if (a == null || b == null) continue;
                breeding.add(new ChickenJeiRecipe(
                        ChickenItemData.create(ModItems.CHICKEN_ITEM.get(), a),
                        ChickenItemData.create(ModItems.CHICKEN_ITEM.get(), b),
                        chicken));
            }
        }
        registration.addRecipes(BREEDING, breeding);
        registration.addRecipes(LAYING, laying);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.BREEDER.get()), BREEDING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ROOST.get()), LAYING);
    }
}
