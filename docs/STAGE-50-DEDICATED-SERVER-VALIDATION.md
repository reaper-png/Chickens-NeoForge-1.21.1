# Stage 50 — Dedicated server and client separation

## Code review

- Common entrypoint: `ChickensMod` registers blocks, items, entities, menus, capabilities and gameplay events without referring to the client renderer package.
- `ChickensClientEvents` is explicitly annotated for `Dist.CLIENT`; renderer, color and screen registrations stay client-only.
- JEI and Jade integrations are optional plugin discovery classes, not referenced by the common entrypoint.
- Build uses `compileOnly` for JEI/Jade. Those mods are not bundled into the Chickens JAR.

## Automated guard

`ClientSeparationTest` ensures the common entrypoint does not acquire direct client-only imports and that client event registration retains the client-only annotation. It is a **source-level regression guard**, not a dedicated-server runtime test.

## Required runtime verification

1. `gradlew.bat clean build`
2. On a clean Java 21 NeoForge 1.21.1 dedicated server, install the generated Chickens JAR without JEI, Jade or client-only mods.
3. Start server, ensure registrations and common setup complete without classloading or mixin errors.
4. Join with a matching client; place and interact with Roost, Breeder and Henhouse; verify synchronization.
5. Save and restart server with chickens, partly completed machines and loaded resource-chicken entities.
6. Repeat client test with optional JEI + Jade; check startup without optional plugins on the server.
7. Verify no `net.minecraft.client.*` exception appears in dedicated-server logs.

These runtime steps have **not been performed** by the assistant.
