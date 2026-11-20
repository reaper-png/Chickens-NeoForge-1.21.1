# Stage 8 — JEI parity: subtypes and drops

Compared against the bytecode in the user's original `chickensmod-1.18.2-1.0.32.jar` (not the previously assembled textures ZIP).

## Changes
- Register a JEI subtype interpreter for `chickens:chicken_item`.
- Use only the breed ID from `ChickenItemData.type`, **not** individual Gain/Growth/Strength values, to identify one of 46 variants.
- Restore the `chicken_drops` category found in the original JEI plugin.
- Source drops from the same breed product mapping used by the original `ChickenDropsCategory` recipes (which call `createDropItem`), represented here by `ChickenProducts.produce`.
- Add translations for `jei.chickens.drops` in English and Portuguese.

## Limitations and checks
- JEI's `drops` and `laying` both show the configured product for each breed because the original plugin uses the same product mapping. This is informational; it does not modify the in-world entity loot tables.
- No textures from third-party mods were added.
- Build with `gradlew.bat clean build` after merging or by switching to the feature branch.
- With JEI installed, check distinct recipe lookups for Iron/Gold/Diamond chickens; verify their recipes are not conflated due to the shared item ID.
- Confirm the three categories Breeding, Laying, and Drops appear, and the client still starts **without** JEI.
- The code has not been built or tested by the assistant; the PR should be merged only after verification.
