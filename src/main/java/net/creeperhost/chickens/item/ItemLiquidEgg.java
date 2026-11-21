package net.creeperhost.chickens.item;

import java.util.List;
import java.util.Optional;
import net.creeperhost.chickens.init.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;

/** Legacy one-use liquid egg; accepts tanks and places water/lava like a bucket. */
public final class ItemLiquidEgg extends Item {
    public ItemLiquidEgg(Properties properties) { super(properties); }

    public static Optional<LiquidEggType> type(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data == null ? Optional.empty() : LiquidEggType.fromId(data.copyTag().getString("id"));
    }

    public static ItemStack of(LiquidEggType type) {
        ItemStack stack = new ItemStack(ModItems.LIQUID_EGG.get());
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putString("id", type.id()));
        return stack;
    }

    @Override public Component getName(ItemStack stack) {
        // Component.translatable returns MutableComponent, but Item.getName
        // returns Component. Avoid Optional's narrower inferred type.
        return type(stack)
                .<Component>map(liquid -> Component.translatable("item.liquid_egg."
                        + liquid.name().toLowerCase(java.util.Locale.ROOT) + ".name"))
                .orElseGet(() -> super.getName(stack));
    }

    @Override public void appendHoverText(ItemStack stack, TooltipContext context,
                                          List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        if (type(stack).isPresent()) tooltip.add(Component.translatable("item.liquid_egg.tooltip"));
    }

    @Override public InteractionResult useOn(UseOnContext context) {
        var level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack egg = context.getItemInHand();
        if (player == null || type(egg).isEmpty()) return InteractionResult.PASS;

        // A tank (or modded fluid handler) takes precedence over world placement.
        // Never place a fluid next to a tank that rejected the transfer.
        var tank = level.getCapability(Capabilities.FluidHandler.BLOCK,
                context.getClickedPos(), context.getClickedFace());
        if (tank != null) {
            if (level.isClientSide) return InteractionResult.SUCCESS;
            // IFluidHandlerItem always represents a single container. The
            // generic hand helper receives the full stack, which can contain
            // multiple eggs; use one isolated egg for this tank transfer.
            ItemStack singleEgg = egg.copyWithCount(1);
            var transfer = FluidUtil.tryEmptyContainer(singleEgg, tank,
                    LiquidEggItemHandler.BUCKET_VOLUME, player, true);
            if (!transfer.isSuccess()) return InteractionResult.PASS;
            // The isolated item was consumed, not the stack in the hand.
            // Only a successful transfer removes one actual egg.
            egg.shrink(1);
            return InteractionResult.SUCCESS;
        }

        if (level.isClientSide) return InteractionResult.SUCCESS;
        // Fluid handlers operate on ONE container. Never expose a multi-egg
        // hand stack to the fluid placement algorithm: it can otherwise
        // drain/alter the whole stack while providing just one bucket.
        ItemStack singleEgg = egg.copyWithCount(1);
        var fluid = LiquidEggItemHandler.fluid(singleEgg);
        boolean placed = FluidUtil.tryPlaceFluid(player, level, context.getHand(),
                context.getClickedPos().relative(context.getClickedFace()),
                new LiquidEggItemHandler(singleEgg),
                new FluidStack(fluid, LiquidEggItemHandler.BUCKET_VOLUME));
        if (placed) {
            // The handler consumed the isolated egg, not the player's hand.
            // Decrement the original stack exactly once after a successful
            // placement or vaporization. Failed placement consumes nothing.
            egg.shrink(1);
        }
        return placed ? InteractionResult.SUCCESS : InteractionResult.FAIL;
    }
}
