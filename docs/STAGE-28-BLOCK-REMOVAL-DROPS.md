# Stage 28 — Block removal inventory drop safety

## Audit finding

Both `BlockRoost.onRemove` and `BlockBreeder.onRemove` copied each internal inventory stack and spawned an item entity, without clearing the original slot first. In the usual single removal path the Minecraft block entity is removed by the superclass, but a repeated or unexpected removal callback while the block entity remains accessible could spawn duplicate drops.

## Fix

- Add a shared `MachineInventoryDrops.drain` method for machine inventories.
- During an actual block-type replacement, and **only on the logical server**, copy each nonempty stack, clear its slot and then spawn the copied items into the world.
- Subsequent calls against the same still-accessible inventory find empty slots, so they cannot drop the same contents a second time.
- Changes of block state that keep the same block type, such as Breeder `has_seeds` / `is_breeding`, do not drop inventories.
- No change to loot-table block-item drops, item counts, genetics, breeding cycle or config.

The Breeder's hidden `PendingOffspring` is **not** an inventory slot and is intentionally not dropped. A completed offspring waiting for output space is not an additional produced item until it is inserted; making it a separate drop would change machine semantics.

## Test locally

```powershell
git fetch origin
git switch fix/stage-28-machine-inventory-removal-drain
.\\gradlew.bat clean build
```

1. Fill Roost input and output slots; break Roost and count the dropped stacks. Every stored item must be dropped exactly once.
2. Fill Breeder parent, seed and output slots; break Breeder and check that every stored stack drops exactly once.
3. Reassemble both machines; inspect their new inventories to ensure they start empty.
4. Let Breeder run and toggle its `has_seeds` and `is_breeding` block states. Verify it does **not** drop or clear stored items.
5. Change machine inputs via GUI and hoppers; confirm normal production and inventory syncing continue to work.
6. Test breaking both machines using ordinary player and command-driven block replacement (for example `/setblock`) and inspect logs for unexpected exceptions.

**Assistant has not compiled or run Minecraft tests. Validate before merging.**
