# Stage 30 — Preserve resource-laying timer when capturing chickens

## Audit finding

The world save path for `EntityChickensChicken` persists `EggLayTime`, but the catcher item previously serialized only species, genetics, age, love time, and analyzed state. When the player captured and then released a chicken, `EntityChickensChicken` started with its constructor's default `layTicks = 6000` rather than continuing the original production cycle.

## Fix

- Expose `EntityChickensChicken.getLayTicks()` and `restoreLayTicks(int)`.
- When catching, save the remaining ticks to the item CustomData key `lay_ticks`, leaving the existing `baby`, `age_ticks`, `love`, `analyzed`, species and stats untouched.
- On releasing a chicken, restore `lay_ticks` when present and refresh the entity's `LAY_PROGRESS` synchronized field.
- Older chicken items without `lay_ticks` continue with the previously used initial timer.
- Clamp stored/restored remaining ticks to a minimum of one to avoid invalid or negative countdown values.
- The timer **does not advance while the chicken exists as an item**. It resumes when the entity is placed in the world. This preserves rather than rerolls the remaining production time.

No change to genetic inheritance, Roost / Breeder processing, world NBT key `EggLayTime`, item IDs, species IDs, or configuration files.

## Validation

```powershell
git fetch origin
git switch fix/stage-30-capture-laying-timer
.\\gradlew.bat clean build
```

1. Spawn a mature resource chicken, let it live for part of its laying cycle, capture it with the catcher, and release it. The remaining lay time must continue rather than restarting at 6000 ticks.
2. Capture and release an already analyzed chicken, and check it retains its age, stats, species and analyzed state from Stage 29.
3. Capture a baby chicken and verify that it remains a baby with its previous age and does not lay until mature.
4. Release an old chicken item created before this change, without the `lay_ticks` key. It must still work.
5. Save and reload a world with a free-roaming chicken and verify its existing `EggLayTime` save/load remains functional.
6. Check one chicken with high Gain/Growth and confirm that the item quantities and future laying-cycle reset continue using the existing behavior.

**Not compiled or tested in Minecraft by the assistant. Validate before merging.**
