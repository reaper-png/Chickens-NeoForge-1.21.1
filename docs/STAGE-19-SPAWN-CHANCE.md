# Stage 19 — Per-species natural spawn chance

The existing `config/chickens/chickens.json` receives a new numeric `natural_spawn_chance` property for all 46 original species. It defaults to **100**, preserving current natural spawn behavior.

Example:

```json
{
  "chickens:iron_chicken": {
    "lay_coefficient": 1.0,
    "is_enabled": true,
    "lay_item": "minecraft:iron_ingot",
    "drop_item": "",
    "parent_1": "flint_chicken",
    "parent_2": "white_chicken",
    "natural_spawn_chance": 25
  }
}
```

**Meaning:** `natural_spawn_chance` is a percentage that accepts/rejects a natural spawn placement attempt after the species' existing block/light/dimension checks pass. `100` always accepts; `0` always rejects; `25` accepts around one in four qualifying attempts. This **is not a guarantee of a particular final number of chickens**: Minecraft's mob-spawn scheduler, caps and group attempts affect observed populations.

Existing biome modifier spawn weights (`10`) and group bounds (`3..5`) remain unchanged. This setting does not add any species to a biome, does not change an existing species' spawn dimension or alter commands/spawn eggs. Species with `SpawnType.NONE` still cannot naturally spawn even if this value is 100. The existing `is_enabled=false` restriction remains in force.

The config loader accepts only whole numbers from 0 through 100. Invalid/out-of-range values fall back to 100 with a warning. Existing custom values and fields are preserved; restart Minecraft to load changes.

## Testing

1. `git fetch origin`
2. `git switch feature/stage-19-natural-spawn-chance`
3. `.\\gradlew.bat clean build`
4. Launch with new JAR and verify 46 `natural_spawn_chance: 100` entries were added without replacing the other config fields.
5. Set `chickens:iron_chicken.natural_spawn_chance` to `0`, restart, and verify fresh Iron Chicken natural spawns are blocked while existing Iron Chickens remain.
6. Set to `25` and restart; observe a reduced fraction of spawn attempts over a meaningful sample rather than expecting a precise world population.
7. Restore `100`; retest ordinary spawn.
8. Confirm JEI, Breeder, Roost, Jade, laying and death drops remain unchanged.
9. `gradlew.bat test` includes `SpawnChanceTest` without loading game classes.

**Server/client note:** spawn decisions are server-side. The server's `chickens.json` governs multiplayer spawn behavior.

Not compiled or game-tested in this assistant environment.
