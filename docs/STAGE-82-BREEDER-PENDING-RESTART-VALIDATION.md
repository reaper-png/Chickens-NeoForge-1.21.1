# Stage 82 — Breeder pending-result persistence

The Breeder stores `PendingOffspring` in the block entity NBT once a result has been rolled. A blocked output must not cause extra RNG draws or seed consumption. After a restart, the pending result may only be inserted if it is still valid for the currently configured parents.

## Test matrix

1. **Normal output:** Give the Breeder two valid parent chickens and seeds. After one cycle, confirm one child output and exactly one seed spent.
2. **Blocked output:** Fill the output slots with incompatible/full stacks, start breeding and wait beyond 1000 progress. Verify no seeds are consumed; free a slot and verify the **same** pending child is emitted.
3. **Save/restart:** Complete a cycle while output is blocked, save and restart the world, then clear an output slot. Verify one pending result and one seed consumed (not duplicated).
4. **Change pedigree across restart:** Block output after a child is selected; stop the server, change the relevant `parent_1`/`parent_2` entries in `config/chickens/chickens.json` so the selected child is no longer a possible cross for the two inserted parents, restart, and clear output. The stale child **must not** be emitted, and seeds must remain unchanged.
5. **Parent offspring remains legal:** A weighted breeding result that equals either parent must remain valid even if no child pedigree matches. This is intentional original behavior.
6. **Disabled species:** Repeat with the pending child or either parent `is_enabled=false`; the result must not be emitted and seeds must remain untouched.
7. **Client/server:** Repeat normal and blocked-output tests on dedicated server with two players viewing the machine, and verify inventory and progress remain synchronized.

The code change uses the current effective pedigree (including validated parent overrides) and does not reroll a valid pending result.

These are manual verification steps, not claims that the assistant ran them.
