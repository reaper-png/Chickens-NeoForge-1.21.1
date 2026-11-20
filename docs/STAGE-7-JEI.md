# Stage 7 — Optional JEI support

Adds two JEI categories using the original immutable Chickens registry:

- **Breeding**: two registered parents produce the resulting breed. The UI lists defined cross-breed combinations, not probability-weighted self-breeding outcomes.
- **Laying**: each chicken's configured product is displayed. This is a representative single output; quantities, Growth/Gain stats, and production time are not represented.
- **Catalysts**: Breeder and Roost.
- JEI is an optional client-side integration compiled against the JEI API, **not bundled** into the Chickens jar.
- No changes to block entities, machine mechanics, NBT, natural spawning, or renderers.

## Build and runtime validation (Java 21)

1. Run `gradlew.bat clean build`.
2. Check Maven/JEI API resolution from `https://maven.blamejared.com`.
3. Install JEI NeoForge for Minecraft 1.21.1 in the **client** mods folder for runtime testing.
4. Confirm Chickens launches without JEI.
5. Confirm the JEI plugin loads with JEI installed, two categories are visible, and recipe catalysts work.
6. Verify ChickenItemData variants keep the correct species when focused in JEI.

The integration has not yet been compiled or tested in Minecraft in this environment; this PR is a draft until the Windows build and gameplay validation succeed.
