# Stage 9 — Optional Jade integration

**Target:** NeoForge 1.21.1, Java 21, Jade 15.10.6+neoforge.

## Integration
- Uses Jade's `@WailaPlugin`, `IWailaPlugin`, and `IEntityComponentProvider` APIs.
- Adds a Jade tooltip to live `EntityChickensChicken` entities only (not Roost blocks).
- Shows breed tier, Growth/Gain/Strength after analysis, and estimated time to laying for adults.
- Reads existing synced entity data; no server custom payload is added.
- The Jade artifact is `compileOnly` so the integration is optional.
- Does **not** add The One Probe dependency or code.

## Validate before merging
1. From the branch, run `gradlew.bat clean build` using Java 21.
2. Launch the Chickens mod with **Jade absent** to confirm it remains optional.
3. Launch with Jade NeoForge 1.21.1 installed; aim at an adult chicken.
4. Verify the tier, and check stats become visible after analyzing the chicken.
5. Check baby/adult laying display, and whether Jade offers the toggle under plugin config.
6. Verify no crash when aiming at a vanilla chicken or Roost block.

Build and gameplay testing must be performed locally; this PR is a draft pending validation.
