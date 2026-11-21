# Changelog

## [0.1.0-rc.1] — Unreleased release candidate

First candidate of the **unofficial** Chickens 1.0.32 port to **Minecraft 1.21.1 / NeoForge**. This is a pre-release, not a declaration of production stability.

### Ported from the original mod
- All 46 original chicken species, natural spawning, resource laying, 1/1/1 to 10/10/10 Growth/Gain/Strength genetics, weighted breeding and Smart Chicken.
- Chicken Staff capture and release with species, age, genetics, analyzer state and remaining laying time preserved.
- 16 colored throwable egg variants, species spawn eggs, and water/lava liquid eggs (1,000 mB each), including supported tank and world interactions.
- Roost, Breeder and six Henhouse variants with inventory handling, automation and persistent progress.
- Optional JEI/Jade integrations and language resources in English, Brazilian Portuguese and French.

### Hardening
- Better handling for full machine outputs, pending Breeder offspring, reloads and inventory transfers.
- Species JSON customization with validated descriptors, parent overrides and safe replacement when filling missing keys.
- Automated Java 21 builds with JUnit tests and packaged-asset/version checks.
- Preserve the original CreeperHost Chickens MIT notice inside the JAR.

### Requirements
- Minecraft 1.21.1; NeoForge 21.1+ (developed with 21.1.240); Java 21.
- Remove earlier builds with the same `chickens` mod ID before installing. Back up existing worlds.

### Known release gates
- Confirm complete redistribution provenance of any reused third-party Roost assets/code before **public publication**.
- Maintainer has run server tests throughout development; this does not mean every historical or integration scenario has been recorded as passing.
- This candidate is not automatically published to GitHub Releases. The workflow only attaches the JAR as a temporary CI artifact for review.

Original upstream project: https://github.com/CreeperHost/Chickens
