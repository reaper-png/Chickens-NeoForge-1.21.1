package net.creeperhost.chickens.blockentities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.creeperhost.chickens.containers.HenhouseMenu;
import net.creeperhost.chickens.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/** Original Henhouse collection: hay -> energy -> collected items + dirt. */
public final class BlockEntityHenhouse extends BlockEntity implements MenuProvider {
    private static final int RANGE = 5;
    private final HenhouseInventory inventory = new HenhouseInventory(this::setChanged);
    private final HenhouseAutomation automation = new HenhouseAutomation(inventory);
    private final ContainerData data = new ContainerData() {
        @Override public int get(int index) { return index == 0 ? energy : 0; }
        @Override public void set(int index, int value) { if (index == 0) energy = value; }
        @Override public int getCount() { return 1; }
    };
    private int energy;

    public BlockEntityHenhouse(BlockPos pos, BlockState state) {
        super(ModBlocks.HEN_HOUSE_TILE.get(), pos, state);
    }
    public HenhouseInventory inventory() { return inventory; }
    public HenhouseAutomation automation() { return automation; }
    public ContainerData data() { return data; }
    public int energy() { return energy; }

    /** Only examines loaded positions in the old five-block neighbourhood; never forces chunks. */
    public static ItemStack collectNearby(Level level, Vec3 chickenPos, ItemStack dropped) {
        if (dropped.isEmpty() || level.isClientSide) return dropped;
        BlockPos center = BlockPos.containing(chickenPos);
        List<BlockEntityHenhouse> nearby = new ArrayList<>();
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-RANGE, -RANGE, -RANGE),
                                                   center.offset(RANGE, RANGE, RANGE))) {
            if (level.hasChunkAt(pos) && level.getBlockEntity(pos) instanceof BlockEntityHenhouse henhouse) {
                nearby.add(henhouse);
            }
        }
        nearby.sort(Comparator.comparingDouble(h -> h.getBlockPos().distSqr(center)));
        ItemStack remaining = dropped.copy();
        for (BlockEntityHenhouse henhouse : nearby) {
            remaining = henhouse.accept(remaining);
            if (remaining.isEmpty()) break;
        }
        return remaining;
    }

    /** Inserts only the amount that fits both outputs and fuel byproducts. */
    private ItemStack accept(ItemStack incoming) {
        if (incoming.isEmpty()) return incoming;
        ItemStack hay = inventory.getStackInSlot(0);
        ItemStack dirt = inventory.getStackInSlot(1);
        int fuelAvailable = energy + (hay.is(Items.HAY_BLOCK) ? hay.getCount() * HenhouseFuel.ENERGY_PER_HAY : 0);
        int dirtRoom = dirt.isEmpty() ? 64 : dirt.is(Items.DIRT) ? dirt.getMaxStackSize() - dirt.getCount() : 0;
        int upperBound = Math.min(incoming.getCount(), fuelAvailable);
        int accepted = 0;
        for (int amount = upperBound; amount > 0; amount--) {
            if (HenhouseFuel.dirtGenerated(energy, amount) > dirtRoom) continue;
            if (MachineOutputs.canInsert(inventory, 2, incoming.copyWithCount(amount))) {
                accepted = amount;
                break;
            }
        }
        if (accepted == 0) return incoming;
        if (!MachineOutputs.insert(inventory, 2, incoming.copyWithCount(accepted))) return incoming;
        int hayUsed = HenhouseFuel.hayNeeded(energy, accepted);
        int dirtMade = HenhouseFuel.dirtGenerated(energy, accepted);
        energy = HenhouseFuel.energyRemaining(energy, accepted);
        if (hayUsed > 0) {
            ItemStack nextHay = hay.copy();
            nextHay.shrink(hayUsed);
            inventory.setStackInSlot(0, nextHay);
        }
        if (dirtMade > 0) {
            ItemStack nextDirt = dirt.isEmpty() ? new ItemStack(Items.DIRT) : dirt.copy();
            nextDirt.setCount(dirt.getCount() + dirtMade);
            inventory.setStackInSlot(1, nextDirt);
        }
        setChanged();
        ItemStack remainder = incoming.copy();
        remainder.shrink(accepted);
        return remainder;
    }

    @Override protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
        tag.putInt("energy", energy);
    }
    @Override protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Inventory", 10)) inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        else if (tag.contains("Items", 9)) inventory.deserializeNBT(registries, tag);
        energy = Math.max(0, Math.min(100, tag.getInt("energy")));
    }
    @Override public Component getDisplayName() { return Component.translatable("block.chickens.henhouse"); }
    @Override public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new HenhouseMenu(id, playerInv, this);
    }
}
