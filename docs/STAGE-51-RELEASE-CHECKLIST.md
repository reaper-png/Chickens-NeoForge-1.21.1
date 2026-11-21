# Stage 51 — Release gate

Release readiness is a checklist, not a declaration of stability.

- [ ] `gradlew.bat clean build` succeeds, including all JUnit tests.
- [ ] Confirm the built JAR's `META-INF/neoforge.mods.toml` contains `version="0.0.1-wip"` (or the current `mod_version`), **not** a raw placeholder.
- [ ] Boot a dedicated NeoForge 1.21.1 server on Java 21 with the JAR and no JEI/Jade/client-only mods.
- [ ] Join multiplayer, operate Roost, Breeder, Henhouse, run inventory automation and verify client sync.
- [ ] Test all 46 chicken entity textures and captured item rendering, including babies and Roost previews.
- [ ] Test natural spawning in the intended dimensions/biomes with disabled breed configuration.
- [ ] Test saved and upgraded worlds, legacy item NBT, capture/release, partly completed cycles and pending Breeder results.
- [ ] Test water/lava liquid eggs into supported tanks and world placement.
- [ ] Profile MSPT/TPS with many chickens and machine block entities. Record baselines.
- [x] Compare the preserved upstream MIT notice to the [CreeperHost Chickens repository](https://github.com/CreeperHost/Chickens/blob/master/LICENSE.txt); package the notice in the built JAR and verify its presence automatically (stage 94).\n- [ ] Review provenance/permissions of any third-party Roost code/assets beyond that upstream MIT notice before public distribution.
- [ ] Only after passing the above, choose an actual release version and create a changelog/release artifact.

**Validation scope:** GitHub Actions runs the Java 21 build and source/package tests. The maintainer reports testing each PR on a server throughout development. This historical testing is acknowledged; it is not equivalent to a recorded result for every scenario above. The assistant has not personally run Minecraft runtime tests. Do not rerun completed cases without an affected change or a reproducible regression.
