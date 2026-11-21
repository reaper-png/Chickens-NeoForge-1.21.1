# Stage 18 — Configurable chicken breeding parents

The original 46 species and entity IDs stay registered. The generated `config/chickens/chickens.json` now adds `parent_1` and `parent_2` for each species. The defaults come directly from `ChickensRegistry`, so no existing breeding changes until the player edits the file.

Example: the original Iron Chicken parents are Flint and White. Set both fields to another valid, non-cyclic pair to change the recipe:

```json
{
  "chickens:iron_chicken": {
    "lay_coefficient": 1.0,
    "is_enabled": true,
    "lay_item": "minecraft:iron_ingot",
    "drop_item": "",
    "parent_1": "flint_chicken",
    "parent_2": "log_chicken"
  }
}
```

**Behavior**
- Runtime weighted breeding (world and Breeder), tier-based laying time, tier display and offspring chances use the validated parents.
- JEI breeding previews use the same effective parent pairs.
- The original double weighting of the two parent types is preserved.
- Parents may be written as `flint_chicken` or `chickens:flint_chicken`.
- A pair of empty strings removes the special breeding recipe for that species.
- Both parents must exist among the 46 registered species. Unknown species, incomplete pairs and any direct or indirect ancestry cycles reject **all custom parent overrides**, reverting to the original breeding graph; other settings still apply. Errors are logged.
- An already existing world, chicken species ID, saved entity and item are not removed. The file is updated only to insert missing fields. Restart after editing.
- Runtime config is read on each Minecraft instance; if a multiplayer server uses different config files from clients, JEI/tier previews on the client may not reflect the server rules. Automatic config synchronization is not implemented.

**Testing**
1. `git fetch origin`
2. `git switch feature/stage-18-custom-breeding-parents`
3. `.\\gradlew.bat clean build`
4. Start Minecraft and verify all 46 entries acquire original `parent_1` and `parent_2` defaults without overwriting `lay_item`, `drop_item`, `is_enabled`, or `lay_coefficient`.
5. For Iron Chicken, set `parent_1` to `flint_chicken` and `parent_2` to `log_chicken`. Restart; verify these parents can yield Iron Chicken in the Breeder and the JEI recipe has changed. (Coal Chicken is another valid outcome for this pair.)
6. Restore Iron Chicken parents to `flint_chicken` and `white_chicken`. Verify the original outcome returns.
7. Temporarily set `parent_1` for Iron Chicken to `iron_chicken` to introduce a self-cycle. Restart; verify a validation error and that the original graph remains functional.
8. Confirm `gradlew.bat test` includes the new pedigree validation tests without loading NeoForge.

Build and gameplay validation have not been performed in the assistant environment.