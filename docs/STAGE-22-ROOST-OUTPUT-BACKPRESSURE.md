# Stage 22 — Roost output backpressure

## Audit finding

`BlockEntityRoost.tick` previously reset progress to zero every tick when `MachineOutputs.canInsert` returned false. This happened when the output slots were full or did not have sufficient room for an entire batch. The Breeder already handled full outputs by keeping its progress and waiting.

## Fix

The Roost now returns early without changing its progress while its outputs cannot accept the batch. Once space becomes available, the existing cycle continues. It still performs **all-or-nothing output insertion**, and does not produce or insert any items when its output inventory is blocked.

The other reset conditions remain unchanged: removing/altering the chicken input or having no valid product still resets progress. The output amount, `gainFactor`, growth speed, and inventory rules are not modified.

## Test

```powershell
git fetch origin
git switch fix/stage-22-roost-output-backpressure
.\\gradlew.bat clean build
```

1. Place a valid chicken item into a Roost and let production progress partway.
2. Fill all four output slots so no complete batch can fit; verify progress stops rather than resetting.
3. Free enough output space; verify production resumes from its previous progress and outputs the expected batch exactly once.
4. Check that hoppers/automation removing output items also resumes production.
5. Remove the input chicken and verify progress still resets.
6. Check a Breeder with full outputs to confirm its existing waiting behavior remains unchanged.

No local compilation or gameplay validation was performed by the assistant.
