# Stage 29 — Capture / release state parity

## Audit finding

The Chickens catcher already preserved species, Gain/Growth/Strength, a `baby` flag and `love` ticks. However, it did not preserve `EntityChickensChicken.isAnalyzed()`, and storing age as a boolean discarded the exact age countdown of a juvenile chicken. When released, an already-growing juvenile could restart as a new baby.

## Fix

- Capture now writes `age_ticks` (the exact `AgeableMob.getAge()` integer) and `analyzed` to chicken item `CUSTOM_DATA`.
- Existing `baby` and `love` fields are kept, and the original stat/species data is unchanged.
- On placement, use the exact `age_ticks` when present; fall back to the original `baby` flag for previously captured items.
- Restore the analyzer flag; older items without it default to `false`.
- If a catcher encounters a resource-chicken entity with an unresolvable species, capture now fails without creating an invalid item or discarding the entity.
- The catcher and item placement still run on the server. None of the JSON/TOML configuration or species registration changes.

Note: this stage does **not** preserve the entity's internal resource-laying timer; that remains outside the captured item data. The existing item release path also retains its original placement rules.

## Validation

```powershell
git fetch origin
git switch fix/stage-29-capture-age-analyzed
.\\gradlew.bat clean build
```

1. Catch and release an adult chicken with nondefault stats, then verify species and all three stats.
2. Analyze a chicken, capture and release it; verify the analyzed state survives.
3. Catch a young chicken after it has already grown for a while, release it, and verify its remaining growth time is not reset.
4. Verify a captured item created by an older version (with only `baby`) still releases correctly.
5. Catch a chicken in love mode and verify the previously supported love-time restoration still works.
6. Verify stacks in Roost and Breeder still accept item variants and their species and stats remain intact.

**This commit has not been compiled or tested in Minecraft by the assistant. Validate before merge.**
