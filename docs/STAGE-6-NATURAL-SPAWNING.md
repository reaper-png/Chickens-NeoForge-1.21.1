# Stage 6 — Natural spawning (NeoForge 1.21.1)

## Implementation

- Spawn placement predicates registered for all species whose `SpawnType` is not `NONE`.
- Spawn entries are data-driven via two NeoForge biome modifiers:
  - `natural_spawn_overworld.json`: 26 `NORMAL` + 1 `SNOW` species.
  - `natural_spawn_nether.json`: 3 `HELL` species.
- Weight `10` and pack size `4` follow the behavior identified in the 1.18.2 original.
- Spawn positions must have an animal-spawnable block below and raw brightness above 8.
- `NORMAL` requires skylight and `HELL` requires ultrawarm dimension properties; `SNOW` has no additional dimension check in the predicate, as in the original.

## Compatibility notes

- Biomes are targeted through `#c:is_overworld` and `#c:is_nether`. Modded biomes missing those tags may differ from the old implementation.
- Despite its name, the legacy `SNOW` predicate does not check temperature or snow blocks.
- The legacy ground and brightness requirements may make Nether spawning very rare or impossible on many common blocks. Do not silently remove those restrictions without checking original behavior.
- Only spawning was modified. This stage does not change chicken renderer, Roost, breeding, or item GUI scale.

## Validation needed

1. Run `gradlew.bat clean build` with Java 21.
2. Start a clean Minecraft 1.21.1 NeoForge test world with the new JAR.
3. Verify neither JSON is rejected in `latest.log` (biome modifier registry).
4. Check that `NONE` species do not naturally spawn.
5. Check natural spawns in Overworld and Nether under appropriate surfaces/light.
6. Verify the Roost preview and inventory rendering are unchanged.

This stage is committed for review; a successful Minecraft runtime test has not yet been reported.
