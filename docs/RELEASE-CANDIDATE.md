# 0.1.0-rc.1 release candidate procedure

## Status
This procedure is for a pre-release candidate, **not** a stable public release. The maintainer has tested PRs on a server during development; these tests are recognized and need not be repeated merely because they were not logged in an older checklist.

## Produce and inspect the candidate
1. Merge the release-preparation PR only after its Java 21 GitHub Actions checks pass.
2. Download the `chickens-neoforge-1.21.1-candidate` artifact from the green workflow run, or build the exact merge commit with `gradle clean build` (a compatible Gradle installation is required; the wrapper is not tracked).
3. Confirm the produced JAR contains `META-INF/neoforge.mods.toml` with `version="0.1.0-rc.1"`, and `META-INF/LICENSE-original-MIT.txt`.
4. Verify it is the expected candidate JAR, not a `-sources` or other auxiliary archive.
5. Record SHA-256, Git commit, test date, Minecraft/NeoForge/Java versions, and observed results before labeling a build approved.

## Change-focused regression checks
No broad, repeated server test cycle is required solely for this release bookkeeping PR. A fresh server smoke test is warranted if the candidate JAR has not yet been used on the actual target environment, or when behavior changes. Preserve backups for existing worlds.

## Public publication blocker
The upstream MIT notice was compared with the original CreeperHost repo and is packaged. **That alone does not establish the complete provenance of every reused third-party asset and Roost component.** Confirm permissions and attribution where applicable before publishing binaries publicly.

## Publish only when approved
- Choose whether to mark the build as a GitHub pre-release.
- Use tag `v0.1.0-rc.1` at the exact validated commit; attach the matching JAR and the notes from `CHANGELOG.md`.
- Do not label the candidate stable, automatically merge/publish, or silently change to `1.0.0`.
