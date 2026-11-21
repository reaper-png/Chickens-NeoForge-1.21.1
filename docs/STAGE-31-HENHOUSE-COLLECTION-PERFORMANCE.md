# Stage 31 — Henhouse collection search performance

## Finding

Each adult resource chicken's laying cycle calls `BlockEntityHenhouse.collectNearby`. Previously, each collection allocated an `ArrayList`, scanned up to 11x11x11 = **1331 positions**, accumulated every loaded Henhouse, sorted that list by squared distance, then attempted insertion in order.

The search area was correct, but allocating/sorting the Henhouse list for each produced stack was avoidable overhead, especially in areas containing many chickens.

## Optimization

- Precompute a **single immutable list** of the same 1331 relative block offsets, ordered nearest-first using squared distance.
- Generate offsets in the original `BlockPos.betweenClosed` order and use a stable sort so equal-distance ties keep the original scan ordering.
- On each laying event, check offsets in that order and immediately return when the whole item stack has been accepted. Do **not** collect/sort a new list of Henhouses.
- Continue calling `level.hasChunkAt(pos)` before `level.getBlockEntity(pos)`, so this code never forces an unloaded chunk.
- Preserve the original Manhattan-axis search cube (`RANGE=5` in X/Y/Z), distance priority, and `Henhouse.accept` logic (fuel, dirt, products and inventory handling).

No breed stats, Gain/Growth formula, laying intervals, save format, configuration files, or item types are changed.

## Performance expectations and limits

This removes the per-laying-event Henhouse-list allocation and sort, and allows searching to stop as soon as a Henhouse accepts all items. It **does not** eliminate up to 1331 world-position checks when no Henhouse accepts the items. No benchmark has been run, and any measurable TPS improvement must be verified under a representative game load.

## Windows validation

```powershell
git fetch origin
git switch perf/stage-31-henhouse-collection-search
.\\gradlew.bat clean build
```

1. Put a Henhouse within five blocks of a mature resource chicken. Feed it hay and verify it collects the expected quantity and generates dirt as before.
2. Place two Henhouses within range, one nearer the chicken than the other. Confirm that the nearer unit receives resources first and the farther one receives the remainder when the first is full.
3. Test with all Henhouses full or out of fuel; the remaining produced resources must still drop into the world.
4. Test Henhouses near a chunk border, with neighboring chunks unloaded. Verify no forced chunk loading or exception.
5. Compare average server tick time / MSPT with many chickens before and after, keeping world layout and chicken counts equivalent.
6. Confirm the resource-laying cycle, genetics, and capture/release laying timer from Stage 30 still work normally.

**Assistant-side build and Minecraft profiling have not been performed. Validate before merging.**
