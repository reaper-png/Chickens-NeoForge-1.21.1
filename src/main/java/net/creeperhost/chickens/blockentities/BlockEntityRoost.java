package net.creeperhost.chickens.blockentities;

import net.creeperhost.chickens.data.ChickenStats;
import net.creeperhost.chickens.data.ChickenStatsCodec;
import net.creeperhost.chickens.containers.RoostMenu;
import net.creeperhost.chickens.init.ModBlocks;
import net.creeperhost.chickens.item.ChickenItemData;
import net.creeperhost.chickens.registry.ChickenProducts;
import net.creeperhost.chickens.registry.ChickenSpecies;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
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

/** Original Roost: 16 matching chickens in slot 0, 4 output slots, original 1000-point cycle. */
public final class BlockEntityRoost extends BlockEntity implements MenuProvider {
    private final MachineInventory inventory = new MachineInventory(MachineInventory.Kind.ROOST, this::onInventoryChanged);
    private final AutomationItemHandler automation = new AutomationItemHandler(inventory);
    private final ContainerData data = new ContainerData() {
        @Override public int get(int index) { return index == 0 ? progress : 0; }
        @Override public void set(int index, int value) { if (index == 0) progress = value; }
        @Override public int getCount() { return 1; }
    };
    private int progress;

    public BlockEntityRoost(BlockPos pos, BlockState state) { super(ModBlocks.ROOST_TILE.get(), pos, state); }
    public MachineInventory inventory() { return inventory; }
    public AutomationItemHandler automation() { return automation; }
    public ContainerData data() { return data; }
    public int progress() { return progress; }

    private void onInventoryChanged(int slot) {
        if (slot == 0) progress = 0;
        setChanged();
        if (level instanceof ServerLevel serverLevel && slot == 0) {
            serverLevel.getChunkSource().blockChanged(worldPosition);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BlockEntityRoost machine) {
        if (level.isClientSide) return;
        ItemStack input = machine.inventory.getStackInSlot(0);
        ChickenSpecies chicken = ChickenItemData.type(input);
        if (chicken == null) { machine.setProgress(0); return; }
        ItemStack output = ChickenProducts.produce(chicken);
        if (output.isEmpty()) { machine.setProgress(0); return; }
        ChickenStats stats = ChickenStatsCodec.read(input);
        int gainFactor = 1 + (stats.getGain() >= 5 ? 1 : 0) + (stats.getGain() >= 10 ? 1 : 0);
        output.setCount(output.getCount() * gainFactor * input.getCount());
        // When all output slots are full, pause without discarding completed work.
        // Resume from the same progress once the player or automation frees space.
        if (!MachineOutputs.canInsert(machine.inventory, 1, output)) return;

        if (machine.progress <= 1000) {
            machine.setProgress(machine.progress + Math.min(stats.getGrowth() + input.getCount() + 1, 50));
        } else if (MachineOutputs.insert(machine.inventory, 1, output)) {
            machine.setProgress(0);
            level.playSound(null, pos, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 0.5f, 0.8f);
        }
    }

    private void setProgress(int value) {
        if (progress == value) return;
        progress = value;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
        tag.putInt("progress", progress);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Inventory", 10)) inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        else if (tag.contains("Items", 9)) inventory.deserializeNBT(registries, tag); // Old machine save layout
        progress = MachineProgressRecovery.roost(tag.getInt("progress"));
    }

    @Override public Component getDisplayName() { return Component.translatable("block.chickens.roost"); }
    @Override public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new RoostMenu(id, playerInv, this);
    }
}
