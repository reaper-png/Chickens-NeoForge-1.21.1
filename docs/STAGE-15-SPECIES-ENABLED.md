# Stage 15 — Species activation flag (first advanced config option)

## Settings

Each registered breed in `config/chickens/chickens.json` now receives `is_enabled: true` alongside its pre-existing `lay_coefficient`.

```json
{
  "chickens:iron_chicken": {
    "lay_coefficient": 1.0,
    "is_enabled": false
  }
}
```

Restart Minecraft after editing. Defaults stay `true`, preserving normal behavior for all original breeds.

## Supported behavior

- Disabled breeds fail the natural spawn predicate.
- Disabled breeds do not spawn as new breeding results in the world or in Breeder.
- If either breeding parent is disabled, breeding returns no offspring.
- Disabling Smart Chicken also blocks conversion of vanilla chickens using books.
- Breeding probability display is zero when the child is disabled.

## Not affected

- Existing entities, stored chickens and eggs remain intact; no save data or registries are deleted.
- Manual spawning from existing items/spawn eggs, commands, creative access and captured chickens are not blocked in this stage.
- `lay_coefficient` works as before; other unported JSON keys are unchanged.
- `is_enabled` does not dynamically unregister entities or remove their IDs.
- No hot reload; restart required.

This is a deliberately **limited** activation policy, not full original-config parity.

## Test checklist

1. `git fetch origin`
2. `git switch feature/stage-15-species-enabled`
3. `.\\gradlew.bat clean build`
4. Launch game with existing `chickens.json`; confirm each of the 46 entries gains `is_enabled: true` and custom coefficients remain.
5. Set `chickens:iron_chicken.is_enabled` to `false`; restart.
6. Confirm Iron Chickens do not spawn naturally or appear as Breeder offspring, while unrelated breeds work.
7. Set `chickens:smart_chicken.is_enabled` to `false`; restart, try book conversion on a vanilla chicken.
8. Restore both settings to `true`; restart and retest.
9. Confirm a previously existing Iron Chicken in an old world is still present and its data is not lost.
10. Confirm malformed `is_enabled` values fall back to `true` with a log warning.

Build and gameplay validation have not been performed by the assistant.
