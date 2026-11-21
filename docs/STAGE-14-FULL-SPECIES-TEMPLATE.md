# Stage 14 — Complete species configuration template

## Behavior

At mod startup, the existing `config/chickens/chickens.json` is read and augmented with **all 46 registered species** in `ChickensRegistry`.

Every previously absent species receives a `lay_coefficient` of `1.0`. An existing species object missing `lay_coefficient` also receives this default.

Existing custom coefficient values, other fields, and unknown entries are preserved. The file is not rewritten if no additions are necessary. Missing files are created automatically.

Malformed JSON, a non-object root or oversized files are **not** rewritten. Invalid coefficients are ignored at runtime as before.

Only `lay_coefficient` is functional. No products, parent definitions, enable/disable settings, or spawn behavior is changed.

## Test checklist

1. `git fetch origin`
2. `git switch feature/stage-14-full-species-config-template`
3. `.\\gradlew.bat clean build`
4. Launch Minecraft with the updated JAR.
5. Inspect `<instance>/config/chickens/chickens.json` and count **46 `chickens:...` species entries**.
6. Confirm the original `chickens:iron_chicken` coefficient is unchanged if customized.
7. Restart and confirm the file remains unchanged when no new entries need to be added.
8. Change `lay_coefficient` for a species, restart and confirm its production interval changes.
9. Confirm existing saves, Jade, Roost, Breeder and JEI still work.

Build and gameplay validation are pending.