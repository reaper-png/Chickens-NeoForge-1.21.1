# Stage 32 — Resource chicken lay-progress synchronization

## Finding

`EntityChickensChicken.aiStep` called `entityData.set(LAY_PROGRESS, layTicks / 2400)` every active server tick, although the displayed progress only changes when the integer value crosses a 2400-tick boundary. Minecraft's `SynchedEntityData` already avoids flagging equal values as dirty, so this is primarily avoidable per-tick work, **not evidence of a network packet being sent every tick**.

A related world-load discrepancy existed: `readAdditionalSaveData` restored `EggLayTime` but did not immediately reconcile `LAY_PROGRESS`, leaving the synced display value at its default until the next active tick.

## Changes

- Add `LayProgressMath.remainingBand` and `needsSync` as pure Java functions with three JUnit tests.
- Centralize updating `LAY_PROGRESS` in `EntityChickensChicken.syncLayProgress`; call `entityData.set` only when the band changes.
- Use it at the same points as before: during normal countdown, after random timer reset, and after restoring the capture item timer.
- Also call it when loading `EggLayTime` from world NBT so the display value agrees with the loaded timer immediately.
- Keep the same `layTicks / 2400` formula, item quantities, Growth/Gain influence, mature/baby and jockey checks, saved NBT fields, and capture/release timer.

No change to the actual laying time, event frequency, spawn list, config or species.

## Validation on Windows

```powershell
git fetch origin
git switch perf/stage-32-lay-progress-sync
.\\gradlew.bat clean build
```

1. Place mature resource chickens of multiple tiers; monitor their lay-progress indicator across the 2400-tick band boundaries.
2. Confirm that resource drops and Henhouse collection still occur at their original intervals and amounts.
3. Save/reopen a world with chickens mid-cycle; verify the displayed progress is immediately consistent with the restored countdown.
4. Capture/release a chicken partway through its laying cycle; verify the countdown and display state survive as in Stage 30.
5. Test juveniles and chicken jockeys; they should not decrement the laying timer while those conditions hold.
6. If investigating performance, compare **MSPT/TPS** with equivalent numbers of loaded chickens before and after; no benchmark is included in this stage.

**No Gradle compilation, Minecraft execution or TPS profiling has been performed by the assistant.**
