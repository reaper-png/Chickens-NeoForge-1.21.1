# Stage 23 — Breeder output backpressure and reproducible offspring

## Finding

The Breeder previously called `ChickensRegistry.getRandomChild` (and, when applicable, genetic mutation) **every server tick after completing its cycle**, even when no output slot could accept the offspring. Depending on which chicken variant could fit in the output slots, this could reroll the result until a compatible one was found. The random outcome therefore depended on output congestion rather than being fixed for the completed cycle.

## Fix

- On the first completed-cycle tick, select one child and compute its genetics **once**.
- Save the resulting chicken `ItemStack` as `PendingOffspring` in the block entity NBT; it survives world save/reload.
- If all 3 output slots cannot fit the pending stack, keep the same pending result and the same progress.
- When a complete insertion becomes possible, deliver it, consume **one seed**, reset progress and clear the pending result.
- Changing either parent or the seed-slot contents cancels the pending outcome and starts a new cycle. Invalid input also cancels it.
- Preserve the existing all-or-nothing output insertion and the original genetic calculations and slot rules.

No entity IDs, JSON/TOML configuration, recipe definitions, breeding weights, or Roost mechanics are changed.

## Validation on Windows

```powershell
git fetch origin
git switch fix/stage-23-breeder-pending-offspring
.\\gradlew.bat clean build
```

### In-game checks

1. Insert two valid chicken parents and seeds; confirm one offspring is produced and **exactly one seed** is consumed.
2. Fill all three output slots with incompatible items and complete a production cycle. The progress should remain complete and seeds should not be consumed.
3. Save, exit and reopen the world with the output still blocked. Clear an output slot; exactly one child should be delivered, with one seed consumed.
4. While output is blocked after a cycle, swap a parent or change the seed slot; the finished result must be invalidated and the cycle restarted.
5. Test a stack of same-species parents and confirm genetics continue to mutate as before.
6. Test hopper/automation output extraction: once an output slot is freed, the pending child should be delivered.
7. Check the game log for block-entity NBT serialization or deserialization errors.

**Build and in-game checks have not been executed by the assistant. Do not merge until verified.**
