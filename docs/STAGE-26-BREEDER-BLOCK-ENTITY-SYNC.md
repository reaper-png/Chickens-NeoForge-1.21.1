# Stage 26 — Breeder block entity synchronization audit

## Finding

The Roost already implemented `getUpdateTag`, `getUpdatePacket` and notified the chunk tracker when its chicken input changed. The Breeder had neither update hook, even though parent/seed input changes alter its block-entity inventory. This left client-side **block-entity data consumers outside the menu** without the equivalent update path.

## Fix

- Add `getUpdateTag` and `getUpdatePacket` to the Breeder, using the existing NBT serialization (like the Roost).
- Notify the chunk source via `blockChanged(worldPosition)` **only when an input slot** (parents 0/1 or seeds 2) changes.
- Leave output slot notifications and per-tick progress out of block-entity broadcasts. Standard menu synchronization (`addDataSlots` and container slot tracking) already handles inventory/progress for players with an open GUI.
- Preserve the existing breeding and seed logic, including pending offspring persistence.

## Validate

```powershell
git fetch origin
git switch fix/stage-26-breeder-block-entity-sync
.\\gradlew.bat clean build
```

1. Place and open a Breeder; add/remove parents and seeds. Observe slot updates and progress.
2. Refill seeds while running: progress remains as expected from Stage 25.
3. Connect a hopper or automation pipe: inputs and output slots must continue working.
4. In multiplayer with two clients, open the same Breeder: both GUIs must show consistent slots and progress.
5. Fill outputs, complete a cycle, save/restart, and free an output slot: a pending child must be delivered once, and one seed consumed.
6. Watch logs for block-entity update packet issues. With many breeders, confirm that ordinary per-tick progress does not emit a block-entity update packet.

**Not compiled or game-tested by the assistant.**
