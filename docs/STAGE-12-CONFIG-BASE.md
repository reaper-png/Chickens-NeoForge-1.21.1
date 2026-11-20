# Stage 12 — First legacy configuration options

## Verified against Chickens 1.0.32 JAR

The original `ConfigHandler` bytecode declares the following defaults:
- `spawnProbability = 10`
- `minBroodSize = 3`
- `maxBroodSize = 5`
- `netherSpawnChanceMultiplier = 1.0f`
- `alwaysShowStats = false`

The original also includes `chickens.json` for species settings such as `is_enabled`, `lay_coefficient`, product items and parents. This advanced species configuration is **not implemented** by this PR.

## This PR

- Adds an optional editable NeoForge **COMMON** config `config/chickens-common.toml`.
- Adds `[display] alwaysShowStats = false` (in the generated TOML, subject to TOML serialization).
- The Jade entity overlay now shows Growth/Gain/Strength if the player analyzed the chicken **or** enabled `alwaysShowStats`.
- Corrects natural spawn pack size from a hardcoded 4 to the original 3–5 in both Overworld and Nether biome modifier JSON files. Weight stays at 10.

## What this PR does not do

- Does **not** support configurable species parentage, products, breed enabling or laying coefficients yet.
- Does **not** expose `spawnProbability`, `minBroodSize`, `maxBroodSize`, or the Nether multiplier as runtime options yet; the biome modifier JSON remains datapack-driven.
- Does not change the entity resource laying timer or the native Jade egg timer.
- Does not change Roost, machine logic, item rendering or Smart Chicken conversion.

## Validation

1. Run `git fetch origin` and `git switch feature/stage-12-original-config-basics`.
2. Run `gradlew.bat clean build` with Java 21.
3. Launch with Jade and confirm `config/chickens-common.toml` is generated.
4. Confirm unanalyzed chickens show only tier by default.
5. Set `alwaysShowStats = true` in the generated file and restart the client. Check Growth/Gain/Strength appear for unanalyzed chickens.
6. Confirm already analyzed chickens still show their stats with the config disabled.
7. Validate that only one egg countdown is visible; spawn configurations load without data errors.
8. Confirm the game starts without Jade.

**Compilation and gameplay validation are pending.**
