# Stage 17 — Optional species death drops

The generated `config/chickens/chickens.json` now gains an optional `drop_item` property for all 46 species. Unlike `lay_item`, it defaults to the empty string because this stage does **not** establish the original 1.0.32 death-drop mapping.

Example:
```json
{
  "chickens:iron_chicken": {
    "lay_coefficient": 1.0,
    "is_enabled": true,
    "lay_item": "minecraft:iron_ingot",
    "drop_item": "minecraft:diamond"
  }
}
```

With this setting, free Iron Chickens and Roosts still produce iron, but a dying Iron Chicken additionally drops one diamond (subject to the normal living-drops event). The new drop is **additive**, not a replacement: vanilla loot and other mods' drops stay untouched. Empty `drop_item` means no additional drop.

JEI "Laying" continues using `lay_item`; JEI "Drops" now previews only nonempty configured extra drops, rather than incorrectly mirroring laying outputs. The JEI category does not attempt to list vanilla loot table drops.

Valid values are registered item IDs (`namespace:item`) or valid liquid egg descriptors (`chickens:liquid_egg|namespace:fluid`), using the existing item-resolution rules. Invalid custom values are ignored with a warning.

Edits take effect after restarting the game. Existing JSON keys and custom values remain intact.

## Validation

1. `git fetch origin`
2. `git switch feature/stage-17-configurable-death-drops`
3. `.\\gradlew.bat clean build`
4. Launch with the new JAR; confirm 46 JSON entries gain `drop_item: ""` without overwriting previous settings.
5. Set Iron Chicken's `drop_item` to `minecraft:diamond`, restart.
6. Kill an Iron Chicken: expect one additional diamond alongside any normal death loot.
7. Confirm laying/ Roost still produce iron and JEI Laying shows iron; JEI Drops shows diamond.
8. Clear the `drop_item`, restart and confirm the extra diamond is gone.
9. Try an invalid ID: no crash, no extra item, warning in log.
10. Confirm an unconfigured breed's death loot remains unchanged.

The precise default death-drop behavior of legacy Chickens 1.0.32 remains a separate parity audit; this patch intentionally does not claim full equivalence.

Not compiled or tested in this assistant environment.
