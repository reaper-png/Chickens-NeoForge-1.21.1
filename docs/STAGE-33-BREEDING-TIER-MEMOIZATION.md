# Stage 33 — Breeding tier calculation audit

## Finding

The original weighted-child algorithm used `getTier` repeatedly while finding the maximum tier, summing weights, and resolving the selected outcome. Every `getTier` recursively traversed the pedigree graph again, even for shared ancestors. The same pattern appeared in the JEI child-chance calculation.

## Optimization

- Memoize effective tier values in a short-lived `HashMap<String, Integer>` for one weighted breeding selection or one child-chance computation.
- Reuse those values across maximum-tier, total-weight, and weighted-roll traversal.
- The standalone `getTier` API still returns the same tier and uses its own fresh local memoization.
- No global cache is retained: installed `parent_1`/`parent_2` overrides always influence a subsequent calculation, without manual invalidation.
- Preserve **exactly** the candidate order, parent duplicate weighting, original tier formula, final total weight and the single random draw.

This reduces repeated recursion during world breeding, Breeder machine output selection, and child-chance queries. It does not remove or change any of the 46 original Chickens breeds or the breeding table.

## Validation

```powershell
git fetch origin
git switch perf/stage-33-breeding-tier-memoization
.\\gradlew.bat clean build
```

1. The regression tests verify that Flint + White still selects Flint at roll 0–1, White at 2–3, and Iron at 4 (bound 5), and calls RNG exactly once.
2. Breed Flint + White naturally and in the Breeder, verifying that Iron still appears with the same relative probability over many trials.
3. Compare JEI's breeding chances for the combination before and after.
4. Verify `parent_1`/`parent_2` override loading still works after restarting the game.
5. Verify same-breed breeding and genetic mutation still behave as before.
6. For performance claims, profile breeding-heavy scenarios: no benchmark is included.

**Not compiled or run in Minecraft by the assistant; review and test before merge.**
