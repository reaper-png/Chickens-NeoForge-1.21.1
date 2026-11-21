package net.creeperhost.chickens.compat.jei;

import java.util.ArrayList;
import java.util.List;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.ISubtypeRegistration;
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
    static final RecipeType<ChickenJeiRecipe> DROPS =
            RecipeType.create(ChickensMod.MODID, "chicken_drops", ChickenJeiRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(ChickensMod.MODID, "jei_plugin");
    }

    /**
     * Chickens is one registered item with 46 CustomData variants. JEI must
     * index those variants independently, without treating stats as new species.
     */
    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(ModItems.CHICKEN_ITEM.get(), new ChickenSubtype());
    }

    private static final class ChickenSubtype implements ISubtypeInterpreter<ItemStack> {
        private String speciesId(ItemStack stack) {
            ChickenSpecies species = ChickenItemData.type(stack);
            return species == null ? "" : species.id();
        }

        @Override
        public Object getSubtypeData(ItemStack ingredient, UidContext context) {
            return speciesId(ingredient);
        }

        @Override
        @SuppressWarnings("deprecation")
        public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
            return speciesId(ingredient);
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var gui = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new ChickenRecipeCategory(gui, BREEDING, ModBlocks.BREEDER.get().asItem(),
                        "jei.chickens.breeding", true),
                new ChickenRecipeCategory(gui, LAYING, ModBlocks.ROOST.get().asItem(),
                        "jei.chickens.laying", false),
                new ChickenRecipeCategory(gui, DROPS, ModItems.CHICKEN_ITEM.get(),
                        "jei.chickens.drops", false));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<ChickenJeiRecipe> breeding = new ArrayList<>();
        List<ChickenJeiRecipe> laying = new ArrayList<>();
        List<ChickenJeiRecipe> drops = new ArrayList<>();
        for (ChickenSpecies species : ChickensRegistry.getItems()) {
            ItemStack chicken = ChickenItemData.create(ModItems.CHICKEN_ITEM.get(), species);
            ItemStack product = ChickenProducts.produce(species);
            if (!product.isEmpty()) {
                laying.add(new ChickenJeiRecipe(chicken, ItemStack.EMPTY, product));

            }
            ItemStack deathDrop = ChickenProducts.deathDrop(species);
            if (!deathDrop.isEmpty()) {
                drops.add(new ChickenJeiRecipe(chicken.copy(), ItemStack.EMPTY, deathDrop));
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
        registration.addRecipes(DROPS, drops);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.BREEDER.get()), BREEDING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ROOST.get()), LAYING);
    }
}
