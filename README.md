# Chickens — NeoForge 1.21.1

Unofficial port of CreeperHost **Chickens 1.0.32** (Minecraft 1.18.2) to **Minecraft 1.21.1 / NeoForge / Java 21**.

## Status: work in progress, not a validated release

The repository contains the port's Java sources, resources and automated tests. This is **not** a statement that the mod has passed full multiplayer, dedicated-server or long-running world tests.

Implemented or under active verification:

- All 46 legacy chicken species, metadata-based chicken items and corresponding textures.
- Chicken breeding, weighted pedigrees, genetic statistics, resource laying and natural spawning.
- Roost, Breeder and six Henhouse variants with inventories, output processing and automation.
- Item capture/release, colored eggs, liquid eggs (water and lava), analyzer and Smart Chicken conversion.
- Optional JEI and Jade integration; client entity, item and Roost rendering.
- World and item NBT migration paths and JUnit regression checks.

For detailed stage-by-stage changes, review the merged GitHub pull requests and the `docs/` directory.

## Building and testing

**Requirements:** JDK 21, working Gradle Wrapper and network access for development dependencies.

On Windows:

```powershell
.\gradlew.bat clean build
```

To start local development environments:

```powershell
.\gradlew.bat runClient
.\gradlew.bat runServer
```

Never assume a successful Gradle build proves that the mod works in-game. Before publishing, test a clean client, dedicated server without optional mods, multiplayer, item/tank automation, machine saving/reloading, all chicken species, and loaded entity performance.

## Versions and metadata

The version is controlled by `mod_version` in `gradle.properties`. Gradle also inserts that value into the packaged `META-INF/neoforge.mods.toml`. The current version is `0.0.1-wip`; do **not** tag it as stable solely because this README was updated.

Minecraft support: 1.21.1. NeoForge: configured in `gradle.properties`. Java: 21.

## Compatibility and distribution

JEI/Jade are optional integrations rather than bundled dependencies. Preserve original registry IDs and item/NBT formats when updating the port. Do not distribute builds as stable before verifying license attribution and the original project's redistribution terms.

## Credits and license

Original mod: CreeperHost Chickens, including credits to CreeperHost and gigabit101. This is an unofficial community port, not a release by the original authors. The metadata currently declares MIT; **verify the original project's license and the attribution/redistribution requirements before a public release**, and retain all notices required by the source.
