package net.creeperhost.chickens.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

/** One single-use, 1000 mB container per egg, as in CreeperHost Chickens 1.0.32.
 * The underlying ItemStack is mutated ONLY by EXECUTE, never by SIMULATE.
 */
public final class LiquidEggItemHandler implements IFluidHandlerItem {
    public static final int BUCKET_VOLUME = 1000;
    private final ItemStack container;

    public LiquidEggItemHandler(ItemStack container) { this.container = container; }

    public static Fluid fluid(ItemStack stack) {
        return ItemLiquidEgg.type(stack)
                // WATER/LAVA are FlowingFluid; EMPTY is their Fluid base type.
                .<Fluid>map(type -> type == LiquidEggType.WATER ? Fluids.WATER : Fluids.LAVA)
                .orElse(Fluids.EMPTY);
    }

    @Override public ItemStack getContainer() { return container; }
    @Override public int getTanks() { return 1; }
    @Override public int getTankCapacity(int tank) { return tank == 0 ? BUCKET_VOLUME : 0; }
    @Override public boolean isFluidValid(int tank, FluidStack resource) { return false; }
    @Override public int fill(FluidStack resource, FluidAction action) { return 0; }

    @Override public FluidStack getFluidInTank(int tank) {
        // NeoForge's IFluidHandlerItem contract requires a single container.
        // Never advertise/drain a stack of eggs as one bucket: bulk handlers
        // must first split off a one-item stack.
        if (tank != 0 || container.getCount() != 1) return FluidStack.EMPTY;
        Fluid fluid = fluid(container);
        return fluid == Fluids.EMPTY ? FluidStack.EMPTY : new FluidStack(fluid, BUCKET_VOLUME);
    }

    @Override public FluidStack drain(FluidStack resource, FluidAction action) {
        FluidStack held = getFluidInTank(0);
        if (resource.isEmpty() || held.isEmpty() || !FluidStack.isSameFluidSameComponents(held, resource)) return FluidStack.EMPTY;
        return drain(resource.getAmount(), action);
    }

    @Override public FluidStack drain(int maxDrain, FluidAction action) {
        FluidStack held = getFluidInTank(0);
        // No partial depletion: one whole egg supplies exactly one bucket.
        if (held.isEmpty() || maxDrain < BUCKET_VOLUME) return FluidStack.EMPTY;
        if (action == FluidAction.EXECUTE) container.shrink(1);
        return held;
    }
}
