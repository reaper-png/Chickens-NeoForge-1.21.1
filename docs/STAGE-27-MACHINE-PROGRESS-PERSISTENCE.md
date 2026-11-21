# Stage 27 — Validate restored machine progress

## Audit finding

Roost and Breeder already serialize their machine inventories and progress, and Breeder additionally saves a selected `PendingOffspring`. The previous `loadAdditional` accepted arbitrarily large `progress` values from stored NBT, only correcting negative values. Such values are not reachable through the normal tick path but can occur in malformed or externally edited world data.

## Fix

Only the restored progress value is bounded:

- Breeder: **0 through 1001**. It advances by one per tick while `progress <= 1000`, so **1001** means ready to complete the cycle.
- Roost: **0 through 1050**. It can advance by up to 50 points in a single tick from `progress = 1000`, so 1050 is a valid completed-cycle overshoot.
- Values in-range stay **exactly unchanged**, including completed cycles.
- Negative NBT values recover as zero, as they did previously. Abnormally large NBT values recover as the highest valid completed progress.
- Inventory serialization, `PendingOffspring` serialization, recipe selection, seed consumption, and actual production tick calculations remain unchanged.

This is a defensive recovery guard, not a demonstrated ordinary-world item-loss bug.

## Testing

```powershell
git fetch origin
git switch fix/stage-27-machine-progress-load-validation
.\\gradlew.bat clean build
```

1. Create a Roost and Breeder and let both reach around half of their cycles. Save and close the world, reopen it and verify progress was retained.
2. Fill the Roost outputs and complete a cycle; save/reopen and then free output space. Check production occurs once.
3. Complete a Breeder cycle with blocked output; save/reopen and free output space. Check the original pending offspring is delivered once and one seed is consumed.
4. Verify normal output inventory and chicken item contents are unchanged.
5. JUnit tests cover 0, mid-cycle values, valid completed values and out-of-range saved numbers.

**No assistant-side Gradle build or Minecraft runtime validation performed. Do not merge before validation.**
