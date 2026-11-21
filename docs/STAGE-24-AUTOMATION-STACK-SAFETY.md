# Stage 24 — Automation inventory snapshot safety

## Finding

The automation-facing `IItemHandler` proxy (`AutomationItemHandler`) passed through the mutable `ItemStack` reference returned by the backing `MachineInventory`. A caller that modifies the object returned by `getStackInSlot` directly (even though the item-handler contract forbids it) could mutate stored stacks outside `insertItem` / `extractItem`, bypassing normal inventory notifications.

## Change

`AutomationItemHandler.getStackInSlot` now returns `delegate.getStackInSlot(slot).copy()`, so external callers receive a snapshot rather than the live backing stack. The machine's own `MachineInventory` and menu are unchanged.

No change to valid inputs, slot limits, output extraction, insertion simulation, NBT serialization, production, or breeding logic.

**Tradeoff:** Every automation slot inspection allocates a copy of a nonempty `ItemStack`. This fix prioritizes inventory isolation. Its impact should be measured in a large installation with many hoppers/pipes.

## Validation

```powershell
git fetch origin
git switch fix/stage-24-automation-defensive-stack-view
.\\gradlew.bat clean build
```

1. Place chickens in Roost and Breeder; confirm their GUIs and machine processing still work.
2. Use hoppers or pipes to insert chickens/seeds and extract machine outputs.
3. Verify extraction does not remove or alter parents or seeds.
4. Verify automation cannot insert external items into machine output slots.
5. With a test integration, retrieve a stack via `IItemHandler.getStackInSlot(0)` and mutate **only the returned copy**; verify the stored inventory remains unchanged.
6. Compare behavior and frame time with many connected automations before adopting broadly.

No local compilation, game execution, or load testing was performed in the assistant environment.
