# Stage 21 — Spawn reason audit

## Finding
The stage 19 natural spawn acceptance predicate receives a `MobSpawnType` but previously ignored it. As a result, the natural-only configuration `natural_spawn_chance` and legacy block/light/dimension requirements were applied indiscriminately to callers that checked this spawn placement predicate, regardless of spawn reason.

## Correction
The registered predicate now evaluates the Chickens natural-only conditions only when `reason` is `MobSpawnType.NATURAL` or `MobSpawnType.CHUNK_GENERATION`. All other reasons (such as `SPAWN_EGG`, `COMMAND`, `SPAWNER`, `BREEDING` and `CONVERSION`) return true from this **specific predicate**. Note that other Minecraft and mod checks may still apply to spawning: this is not a universal bypass.

The existing natural spawn conditions remain unchanged:
- `is_enabled` and `natural_spawn_chance`;
- spawn type NORMAL/HELL/SNOW and suitable dimension;
- required block and brightness;
- biome modifier weights, group sizes and biomes.

No change to stored entities, species IDs, breeding, drops, JEI, or config file format.

## Local validation
```powershell
git fetch origin
git switch fix/stage-21-natural-spawn-reasons
.\\gradlew.bat clean build
```

In Minecraft:
1. Set `chickens:iron_chicken.natural_spawn_chance = 0` in `chickens.json`, restart.
2. Confirm fresh **natural** Iron Chicken spawns are blocked.
3. Verify manual `/summon chickens:iron_chicken` works.
4. If you have an Iron Chicken spawn egg, verify it can still spawn the species.
5. Restore `natural_spawn_chance=100` and verify natural spawns resume.
6. Test other previous config features, especially `is_enabled=false` still blocking breeding/natural spawns.

**Important:** In this implementation, `is_enabled=false` remains a *natural spawning and breeding* policy, not a restriction on all manual creation. This matches the documented limitation introduced in stage 15.

This change has not been compiled or game-tested by the assistant.
