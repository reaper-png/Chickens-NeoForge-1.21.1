# Stage 34 — Genetics and chicken attribute bounds

## Finding

Natural breeding (`EntityChickensChicken.getBreedOffspring`) and the Breeder (`BlockEntityBreeder.tick`) already share `ChickenGenetics.offspring`. The weighted mutation procedure is therefore consistent across these two paths.

However, `ChickenStats` used `Math.min(10, value)` in each constructor/setter, accepting zero and negative Gain/Growth/Strength from malformed item/entity NBT or external API callers. The legacy intended attribute range is **1..10**. Unbounded low values could also affect weighted mutation, strength weights, and time calculations.

## Fix

- Clamp Gain, Growth and Strength with `Math.clamp(value, 1, 10)` on every construction/setter.
- Retain all existing values in the valid range unchanged.
- Do not change `ChickenGenetics.offspring` or its weighted algorithm, the order/number of RNG calls, or the split between same-species mutation / parent inheritance / new-species defaults.
- No changes to item NBT keys, species IDs or config fields.
- Add three direct `ChickenStats` boundary tests and three breeding regression tests (3 mutation rolls for same species, 0 for parent-copy, invalid parent normalization).

## Validation

```powershell
git fetch origin
git switch fix/stage-34-genetic-stat-bounds
.\\gradlew.bat clean build
```

1. Breed two chickens of the same species with differing stats naturally and in the Breeder. Both must use the same weighted inheritance behavior.
2. Breed different species and inspect the offspring: a selected parent species copies its corresponding parent stats; a new species starts at 1/1/1.
3. Check displayed values and production timings for chickens with 1/1/1 and 10/10/10.
4. Test corrupted saved/input values of 0, negative and over-10 (use a disposable test world) and verify that stats are normalized to 1..10 on read.
5. Capture/release and restart a world to ensure valid existing attributes are unchanged.

**Build and gameplay tests have not been executed by the assistant.** The change deliberately normalizes previously invalid low values; any content that relied on them will behave differently.
