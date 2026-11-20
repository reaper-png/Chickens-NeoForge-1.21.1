# Stage 11 — Avoid duplicate next-egg lines in Jade

## Confirmed issue

With Jade and the Chickens overlay installed, the Water Chicken tooltip showed two countdowns:
- `Next egg: 13m 36s` from Jade's own chicken/egg display
- `Next egg in 9 min.` appended by `ChickensJadePlugin`

## Change

Remove the custom egg countdown from the Chickens Jade component while keeping:
- Breed tier
- Growth, Gain and Strength only when analyzed
- The mod's short display name, `Chickens`

No modification to egg production logic, entity NBT, Roost, Smart Chicken conversion, or JEI.

## Important limitation

The two numbers differed because the custom component used `getLayProgress()`, a coarse integer estimate derived from the resource laying timer. The remaining native Jade countdown is derived from Jade's handling of vanilla chickens and **has not been proven to reflect the independent `layTicks` timer used by this port**. This PR is a display-only fix. Synchronizing the real resource timer or overriding Jade's vanilla provider requires a separate verified implementation.

## Local validation

1. `git fetch origin`
2. `git switch fix/stage-11-jade-duplicate-egg-timer`
3. `gradlew.bat clean build`
4. Aim at a Water Chicken and a Smart Chicken with Jade installed: only one next-egg line should remain.
5. Analyze a chicken and confirm Growth, Gain, Strength and tier are still present.
6. Test again without Jade installed.
7. Observe whether Jade's remaining countdown actually matches resource production; report any discrepancy.

This branch has not been built or game-tested by the assistant.
