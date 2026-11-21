# Chickens — NeoForge 1.21.1

An **unofficial, work-in-progress port** of [CreeperHost Chickens](https://github.com/CreeperHost/Chickens) **1.0.32** (Minecraft 1.18.2 / Forge) to **Minecraft 1.21.1 / NeoForge**. This project preserves the original mod's identity, registry IDs, and gameplay mechanics. **It does not use Modern Chickens.**

> **Current status: `0.1.0-rc.1` — release candidate, not a stable/public release.** The maintainer has continuously tested PR builds on a server during development; independent performance and provenance checks are not fully documented. **Back up your worlds before testing.**

## Features

- **46 original chicken species** with original registry IDs, textures, breeding pedigrees, resource drops, and **Growth / Gain / Strength** genetics.
- **3D captured chicken items.** The **Chicken Staff (Catcher)** captures a chicken as an item dropped into the world (rather than placing it directly in inventory). The item preserves its species, genetics, age, analysis status, and remaining laying timer.
- Species-specific **spawn eggs**, **16 throwable colored egg variants**, and water/lava **liquid eggs** containing 1,000 mB each. Liquid eggs support world placement and compatible fluid tanks.
- **Roost**, **Breeder**, and **six Henhouse variants** with their inventories, menus, automation capabilities, saved state, and output backpressure handling.
- Natural spawning, resource laying, weighted breeding, Smart Chicken conversion, and configurable species behavior.
- Optional integrations for **JEI** and **Jade**, which are **not bundled** into the mod JAR.
- Language resources for **English (US), Brazilian Portuguese, and French**.

Species can be configured in `config/chickens/chickens.json`, created on first load. The configuration includes species enablement, laying rates/products, death drops, breeding parents, and natural spawn chance. Stop the game/server and make a backup before editing.

## Requirements

| Component | Version |
| --- | --- |
| Minecraft | **1.21.1** |
| NeoForge | **21.1.240** (development version in `gradle.properties`; runtime requirement is declared in the mod metadata) |
| Java / JDK | **21** |
| Port version | **0.1.0-rc.1** (`gradle.properties`) |

## Building

Use JDK 21 and a compatible Gradle installation with access to the required Maven repositories.

**This repository currently does not track a Gradle Wrapper** (`gradlew` / `gradlew.bat`). For a fresh clone, use a locally installed compatible Gradle version:

```powershell
gradle clean build
```

If you already have a working Gradle Wrapper in your local development checkout, you may instead run:

```powershell
.\gradlew.bat clean build
```

The resulting mod JAR is placed in `build/libs/`. The build runs JUnit checks and verifies the packaged mod version, original MIT notice and required textures, loot tables, and blockstates. **A successful build does not prove that the mod works in-game.**

For development runs:

```powershell
gradle runClient
gradle runServer
```

To test the packaged mod, copy **only the compiled JAR** from `build/libs/` into the `mods/` directory of a Minecraft 1.21.1 NeoForge client or server. Remove older builds of this port from `mods/` to avoid duplicate `chickens` mod IDs.

## Repository structure

| Path | Purpose |
| --- | --- |
| `src/main/java/` | Java implementation |
| `src/main/resources/` | Textures, models, translations, recipes, loot tables, and mod metadata |
| `src/test/java/` | JUnit regression tests |
| `docs/` | Technical notes, audit reports, and manual validation matrices |
| `docs/archive/` | Historical incremental patch notes — **not** current installation instructions |
| `legacy_species_manifest.json` | Reference manifest for the original species |
| `LICENSE-original-MIT.txt` | Preserved original license notice pending redistribution review |

Generated files such as `.gradle/`, `build/`, `*.zip`, and `*.jar` should not be committed.

## Release candidate

See [the changelog](CHANGELOG.md) and [candidate procedure](docs/RELEASE-CANDIDATE.md). Successful GitHub Actions builds attach a temporary candidate JAR for review; **this does not automatically publish a release**.

## Release validation

See the [release checklist](docs/STAGE-51-RELEASE-CHECKLIST.md) for the full test matrix. Before publishing a release:

1. Run `gradle clean build` with all tests passing, and inspect the produced JAR.
2. Boot a **dedicated NeoForge 1.21.1 server on Java 21**, without optional client-side JEI/Jade dependencies, and connect a client.
3. Check all **46 species**, their textures, babies, capture/release behavior, genetics, breeding, and laying timers.
4. Verify Roost, Breeder, and Henhouse inventories, hoppers/pipes, full outputs, world reloads, and save migration.
5. Test water/lava eggs individually and in stacks, including failed transfers and tanks from other mods.
6. Profile performance (MSPT/TPS) with large numbers of chickens and machines.
7. Confirm licensing, original asset attribution, and redistribution permissions **before any public release**.

## Credits and licensing

Original project: **CreeperHost Chickens**, with credit to **CreeperHost, gigabit101, and the original contributors**. This is a community port and is **not affiliated with or endorsed by the original authors**.

The NeoForge metadata currently declares `MIT`, and this repository contains `LICENSE-original-MIT.txt`. The upstream [CreeperHost Chickens MIT notice](https://github.com/CreeperHost/Chickens/blob/master/LICENSE.txt) matches the preserved `LICENSE-original-MIT.txt`, which is also packaged as `META-INF/LICENSE-original-MIT.txt` inside the mod JAR. This verifies that upstream notice, **not** the complete provenance of every reused asset or possible third-party Roost component; review those before public distribution.
