# Stage 36 — Henhouse inventory safety audit

## Findings

1. **Automation exposes mutable backing stacks.** `HenhouseAutomation.getStackInSlot` returned `inventory.getStackInSlot(slot)` directly. A caller could mutate that reference (e.g. `shrink`, `grow`) without invoking the normal inventory callbacks. The Roost/Breeder automation was already hardened against this in Stage 24.
2. **Removal does not drain slots before spawning drops.** `BlockHenhouse.onRemove` copied inventory stacks into item entities but left the original slots populated until block entity destruction. Repeated or unusual removal callbacks while that entity remains accessible could generate duplicate item entities. The Roost/Breeder removal path was already hardened in Stage 28.

## Fixes

- `HenhouseAutomation.getStackInSlot` now returns a defensive `.copy()` so item handlers cannot alter the Henhouse's backing stacks by mutating the returned object.
- During genuine block-type replacement, the server copies each nonempty stack, clears its slot through `setStackInSlot`, then drops the copy. A later call against the same inventory therefore sees empty slots.
- Preserve output extraction / hay insertion rules, Henhouse energy tracking, fuel consumption, dirt generation, resource collection, inventory size (11 slots), and saved NBT format.

## Findings not changed

The existing `HenhouseFuel` unit tests cover the one-hay-per-100-resources rule and energy/dirt boundaries. No change to the underlying fuel math is proposed in this stage; further in-game tests are still required to confirm collection, output limits, and interaction with automation.

## Validation

```powershell
git fetch origin
git switch fix/stage-36-henhouse-inventory-safety
.\\gradlew.bat clean build
```

1. Insert hay via hopper or another item transport mod. Only input slot 0 should accept hay; automated extraction must not remove it.
2. Collect resources into Henhouse output slots. Automation should extract outputs/dirt normally, not insert into output slots.
3. With a mod/API test, obtain a stack through the external `IItemHandler.getStackInSlot`, mutate the returned stack, and check the real inventory is unchanged.
4. Fill input, dirt, and output slots and break a Henhouse. Confirm every stored item appears once as an item drop.
5. Place and break all six wooden Henhouse variants. Verify identical behavior.
6. Test a Henhouse with full outputs and no hay; uncollected resources should still drop into the world.
7. Save and reopen a world with partly used Henhouse energy; verify inventory and energy persist.

**Assistant has not compiled or executed Minecraft tests. Review and validate before merge.**
