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
- [ ] Verify upstream Chickens 1.0.32 license and attribution/redistribution terms. Do not assume the current MIT metadata is authoritative for upstream assets/code.
- [ ] Only after passing the above, choose an actual release version and create a changelog/release artifact.

**The assistant has not run the build, runtime tests, or license due diligence.**
