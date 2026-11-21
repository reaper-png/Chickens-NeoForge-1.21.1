# Stage 20 — Global spawn weight and group size (NeoForge 1.21.1)

The original Chickens spawn group definition is **weight 10, group size 3–5** for 27 Overworld and 3 Nether species. This stage replaces the fixed `neoforge:add_spawns` JSONs with a Chickens-specific biome modifier, so the settings can come from `config/chickens-common.toml` when the world loads.

Example:

```toml
[spawning]
weight = 10
minGroupSize = 3
maxGroupSize = 5
```

* `weight`: relative spawn-list weight, 0–1000. **0 excludes all 30 original natural-spawn entries from Chickens' own biome modifier.** Not a percentage or absolute mob frequency.
* `minGroupSize` / `maxGroupSize`: integer group bounds, 1–64. If minimum is greater than maximum, effective maximum is clamped upward to the minimum.
* `natural_spawn_chance` in `config/chickens/chickens.json` remains a separate per-species acceptance gate for spawn attempts. `is_enabled` remains separate.
* The modifier still targets `#c:is_overworld` and `#c:is_nether` and preserves the **original 27/3 species split**. No new biome routing or entity IDs.
* Commands/spawn eggs, breeding, already existing animals, Roost, JEI and other mods' biome modifiers are unaffected.
* A dedicated server's config controls natural spawns. Restart world/server after editing worldgen-related values; changing a running config may not retroactively rebuild biome spawn lists.

## Validation checklist

1. `git fetch origin`
2. `git switch feature/stage-20-global-spawn-groups`
3. `.\\gradlew.bat clean build`
4. Launch Minecraft and create/load a world. Look for biome modifier decode errors in `latest.log`.
5. Check `config/chickens-common.toml` contains `[spawning]` with the three new defaults.
6. At defaults, verify natural spawns still function in Overworld and Nether.
7. Change `weight=0`, restart world/server, and verify new natural spawns no longer originate from the Chickens modifier.
8. Restore `weight=10`, set `minGroupSize=1`, `maxGroupSize=2`; restart, inspect new groups.
9. Restore defaults `10/3/5`; test a pre-existing world for regressions.
10. Confirm tests cover the default and invalid min/max combinations without requiring NeoForge to load in JUnit.

This PR has not been compiled or game-tested in the assistant environment. If decoding or startup fails, do **not** merge.
