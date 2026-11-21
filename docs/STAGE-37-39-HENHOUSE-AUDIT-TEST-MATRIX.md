# Henhouse audit stages 37–39 — manual validation matrix

These changes are deliberately isolated in separate PR branches. Merge and test sequentially or validate each separately.

## Stage 37 — Dirt output limits and partial collection

- Dirt output with 0 space: a collection may proceed **only until** just before the next dirt generation boundary, and the remainder must drop into the world.
- Dirt output with 1 free slot: collection may cross **exactly one** dirt generation boundary.
- A hay bale provides 100 items worth of collection energy, without generating free extra resources.
- A partly full product slot accepts the correct subset of matching items; unmatched items remain uncollected.
- Run the new pure-Java test verifying the dirt-capacity formula for every energy state (0..100) and dirt room (0..64).

## Stage 38 — Menu identity

- Open Henhouse UI, then have another player break the block. It must no longer be a valid server menu.
- Repeat with `/setblock` replacement and with a different wood variant at the same position.
- Confirm the menu remains usable while the **same** Henhouse entity exists, including normal item movement and Shift+Click.

## Stage 39 — Block-entity synchronization

- With a client watching, insert/remove hay in slot 0 and verify the server issues an explicit block-entity update.
- Change a dirt/output slot and confirm this does not request a redundant block-entity packet.
- GUI still uses standard container slot and energy synchronization.
- Join a world with an already loaded Henhouse and verify its initial block entity data is available.
- Save/reload a Henhouse with partially depleted energy and confirm exact persistence.

## Build

```powershell
git fetch origin
git switch fix/stage-37-henhouse-dirt-capacity
.\\gradlew.bat clean build
```

Then repeat on the other two branches, or on the merged main after review.

**None of the three stages was compiled or run in Minecraft by the assistant.**
