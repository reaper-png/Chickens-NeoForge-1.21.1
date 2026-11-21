# Stage 25 — Breeder seed replenishment and Shift-click audit

## Finding

The Breeder reset its production progress whenever inventory slot 2 (seeds) changed, even when a player Shift-clicked more valid seeds into an occupied seed slot or a hopper replenished it. The cycle restarted unnecessarily.

## Fix

- Replenishing or replacing seeds **while valid seeds remain and no offspring is pending** preserves the current progress.
- Removing the final valid seed resets progress.
- Changing either parent always resets progress and cancels a pending offspring.
- If a completed offspring is pending due to blocked outputs, changing seeds cancels that outcome as in Stage 23.
- Output slot changes do not reset progress.
- Offspring selection, insertion, one-seed consumption, genetics, and Roost processing remain unchanged.

The rule is extracted into pure Java `BreederProgressPolicy` and covered with five JUnit tests.

## Testing

```powershell
git fetch origin
git switch fix/stage-25-breeder-seed-refill-progress
.\\gradlew.bat clean build
```

1. Insert two parents and one stack of seeds; start a cycle.
2. Shift-click more of the same seed item into slot 2 while the machine is working. Verify that the progress does **not** reset.
3. Replenish seeds with a hopper or modded pipe. Verify that progress remains.
4. Remove all seeds while processing. Verify that progress resets.
5. Change either parent while processing. Verify that progress resets.
6. Fill all three outputs, complete a cycle, then change the seed slot. Verify that the pending offspring is invalidated and no duplicate offspring is delivered.
7. Make room in outputs for a completed cycle without changing seeds. Verify that exactly one offspring and one seed are consumed.

No assistant-side compile or in-game test has been performed. Validate before merging.
