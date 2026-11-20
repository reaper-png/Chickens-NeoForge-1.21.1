# Stage 13 — Start restoring species-specific JSON configuration

## Original behavior verified

The original Chickens 1.0.32 `ConfigHandler` uses `config/chickens/chickens.json`, keyed by namespaced species IDs. Each section has a `lay_coefficient` with default **1.0**, bounded **0.01–100.0**.

The original `ChickensRegistryItem.getMinLayTime()` computes:
`(int) max(1, 6000 * tier * lay_coefficient)`; the maximum is twice the minimum.

## What is implemented

- Parse existing `config/chickens/chickens.json` safely at first use.
- If missing, create a minimal sample file containing the Iron Chicken entry.
- Respect only `lay_coefficient`, leaving other original fields in existing JSON files untouched.
- Ignore invalid/out-of-range values, keeping default `1.0`.
- Apply the multiplier through `ChickensRegistry.getMinLayTime` (used by living chickens' laying-time calculation).
- No changes to the fixed Roost production cycle. No reload while running; restart after changes.
- Add JUnit tests for coefficient math.

## Sample

```json
{
  "chickens:iron_chicken": {
    "lay_coefficient": 0.5
  },
  "chickens:diamond_chicken": {
    "lay_coefficient": 2.0
  }
}
```

The first has a faster laying cycle; the second a slower one. Other breeds retain the original default. Existing saved laying timers are not forcibly reset on startup.

## Explicitly not implemented in this stage

- `is_enabled`, `lay_item`, `drop_item`, `parent_1`, `parent_2`, `spawn_type`, new species definitions or editing registered entity IDs.
- Configurable natural spawn probability and pack sizes.
- Synchronization or hot-reload of JSON configuration.
- Override of vanilla/Jade's egg countdown (which may differ from resource laying time).

## Validation

1. `git fetch origin`
2. `git switch feature/stage-13-species-lay-coefficient`
3. `gradlew.bat clean build`
4. Launch once and inspect `config/chickens/chickens.json`.
5. Change Iron Chicken `lay_coefficient` to `0.5`, restart, and verify it produces resources more frequently compared with default.
6. Restore to `1.0`, restart, and confirm normal behavior.
7. Confirm no errors when the file is missing, empty/invalid, or contains an invalid coefficient.
8. Verify Roost, Breeder, Jade and JEI still work.

The code has not been compiled or game-tested by the assistant.
