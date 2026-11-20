package net.creeperhost.chickens.blockentities;

import net.creeperhost.chickens.breeding.ChickenGenetics;
import net.creeperhost.chickens.containers.BreederMenu;
import net.creeperhost.chickens.data.ChickenStatsCodec;
import net.creeperhost.chickens.init.ModBlocks;
import net.creeperhost.chickens.init.ModItems;
import net.creeperhost.chickens.item.ChickenItemData;
import net.creeperhost.chickens.registry.ChickenSpecies;
import net.creeperhost.chickens.registry.ChickensRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;

/** Original Breeder: two parents, one seeds slot, three chicken output slots. */
public final class BlockEntityBreeder extends BlockEntity implements MenuProvider {
    private final MachineInventory inventory = new MachineInventory(MachineInventory.Kind.BREEDER, this::onInventoryChanged);
    private final AutomationItemHandler automation = new AutomationItemHandler(inventory);
    private final ContainerData data = new ContainerData() {
        @Override public int get(int index) { return index == 0 ? progress : 0; }
        @Override public void set(int index, int value) { if (index == 0) progress = value; }
        @Override public int getCount() { return 1; }
    };
    private int progress;

    public BlockEntityBreeder(BlockPos pos, BlockState state) { super(ModBlocks.BREEDER_TILE.get(), pos, state); }
    public MachineInventory inventory() { return inventory; }
    public AutomationItemHandler automation() { return automation; }
    public ContainerData data() { return data; }
    public int progress() { return progress; }

    private void onInventoryChanged(int slot) { if (slot <= 2) progress = 0; setChanged(); }

    public static void tick(Level level, BlockPos pos, BlockState state, BlockEntityBreeder breeder) {
        if (level.isClientSide) return;
        ItemStack parentA = breeder.inventory.getStackInSlot(0);
        ItemStack parentB = breeder.inventory.getStackInSlot(1);
        ItemStack seeds = breeder.inventory.getStackInSlot(2);
        ChickenSpecies first = ChickenItemData.type(parentA);
        ChickenSpecies second = ChickenItemData.type(parentB);
        boolean active = first != null && second != null && seeds.is(Tags.Items.SEEDS);
        if (state.hasProperty(net.creeperhost.chickens.block.BlockBreeder.HAS_SEEDS)) {
            boolean current = state.getValue(net.creeperhost.chickens.block.BlockBreeder.HAS_SEEDS);
            boolean breeding = state.getValue(net.creeperhost.chickens.block.BlockBreeder.IS_BREEDING);
            boolean willBreed = active && breeder.progress > 0;
            if (current != !seeds.isEmpty() || breeding != willBreed) {
                level.setBlock(pos, state.setValue(net.creeperhost.chickens.block.BlockBreeder.HAS_SEEDS, !seeds.isEmpty())
                        .setValue(net.creeperhost.chickens.block.BlockBreeder.IS_BREEDING, willBreed), 3);
            }
        }
        if (!active) { breeder.setProgress(0); return; }
        if (breeder.progress <= 1000) { breeder.setProgress(breeder.progress + 1); return; }

        ChickenSpecies child = ChickensRegistry.getRandomChild(first, second, level.random::nextInt);
        if (child == null) { breeder.setProgress(0); return; }
        ItemStack result = ChickenItemData.create(ModItems.CHICKEN_ITEM.get(), child);
        ChickenItemData.write(result, child, ChickenGenetics.offspring(first, second, child,
                ChickenStatsCodec.read(parentA), ChickenStatsCodec.read(parentB), level.random::nextInt));
        if (!MachineOutputs.canInsert(breeder.inventory, 3, result)) return;
        // Consume seeds only after the output has been fully accepted.
        if (MachineOutputs.insert(breeder.inventory, 3, result)) {
            ItemStack nextSeeds = seeds.copy();
            nextSeeds.shrink(1);
            breeder.inventory.setStackInSlot(2, nextSeeds);
            breeder.setProgress(0);
            level.playSound(null, pos, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 0.5f, 0.8f);
        }
    }

    private void setProgress(int value) {
        if (progress == value) return;
        progress = value;
        setChanged();
    }

    @Override protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
        tag.putInt("progress", progress);
    }
    @Override protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Inventory", 10)) inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        else if (tag.contains("Items", 9)) inventory.deserializeNBT(registries, tag);
        progress = Math.max(0, tag.getInt("progress"));
    }

    @Override public Component getDisplayName() { return Component.translatable("block.chickens.breeder"); }
    @Override public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new BreederMenu(id, playerInv, this);
    }
}
