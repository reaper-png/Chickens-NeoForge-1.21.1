# Stage 16 — Configurable species laying products

The mod now writes `lay_item` to each existing species entry in `config/chickens/chickens.json`. It defaults to the original product ID, preserving all 46 breeds' behavior.

Example:
```json
{
  "chickens:iron_chicken": {
    "lay_coefficient": 1.0,
    "is_enabled": true,
    "lay_item": "minecraft:diamond"
  },
  "chickens:water_chicken": {
    "lay_coefficient": 1.0,
    "is_enabled": true,
    "lay_item": "chickens:liquid_egg|minecraft:water"
  }
}
```

## Behavior

- Uses the configured `lay_item` in the chicken entity's resource-laying, the Roost, and JEI laying/drop previews (all call `ChickenProducts.produce`).
- Standard format: `namespace:item_id` for registered items.
- Fluid egg format: `chickens:liquid_egg|namespace:fluid_id`, preserving the port's CUSTOM_DATA fluid ID.
- A malformed or unavailable item falls back to the original product. Warning is logged once per invalid descriptor.
- Existing JSON entries and edits are preserved; `lay_item` is added only where missing.
- No game IDs, genetic chances or machine progression formulas are changed.
- This is a **laying-output** setting; separate legacy `drop_item` support and parent overrides are deferred.
- Configuration takes effect after restarting Minecraft; `lay_coefficient` and `is_enabled` remain intact.

## Validation

1. `git fetch origin`
2. `git switch feature/stage-16-configurable-lay-items`
3. `.\\gradlew.bat clean build`
4. Start Minecraft with the new JAR and verify all 46 species gained `lay_item`.
5. Change `chickens:iron_chicken.lay_item` to `minecraft:diamond`; restart.
6. Verify the Iron Chicken lays diamonds when free, the Roost outputs diamonds, and the laying preview in JEI shows diamonds.
7. Restore `minecraft:iron_ingot`.
8. Check the Water Chicken still creates liquid eggs with the appropriate fluid tag.
9. Set an invalid ID temporarily and confirm the original item is still produced.
10. Check saved worlds, Jade and Breeder still work.

No compilation or in-game validation has been performed in the assistant environment.
