# Stage 75 — Liquid egg transfer regression matrix

This stage addresses direct tank interactions. It retains the Stage 73 single-egg-copy world placement path and Stage 74 rule that IFluidHandlerItem handles only a single item.

## Validation steps (NeoForge 1.21.1, Java 21)

Run `gradlew.bat clean build`, launch the game, then test in survival (inventory counts and tank amounts must be inspected before/after):

| Starting eggs | Target | Expected result |
| --- | --- | --- |
| 1 water egg | empty tank with >=1000 mB free | tank +1000 mB; egg consumed |
| 16 water eggs | empty tank with >=1000 mB free | tank +1000 mB; stack becomes 15 |
| 16 lava eggs | lava-compatible tank with >=1000 mB free | tank +1000 mB; stack becomes 15 |
| 16 water eggs | tank with only 999 mB free | no fluid or egg change |
| 16 water eggs | full tank | no fluid or egg change |
| 16 water eggs | tank containing incompatible lava | no fluid or egg change |
| 1 water egg | a replaceable world block | place water and consume 1 |
| 16 lava eggs | a replaceable world block | place lava and consume only 1 |
| 16 water eggs | a non-replaceable world block | consume none |

Additional checks:

- Verify a tank's `fill(..., SIMULATE)` does not change its contents.
- Check a 1-item Liquid Egg `IFluidHandlerItem`: simulate drain must not consume it, execute 1000 mB consumes it, and partial drain (<1000 mB) is rejected.
- A 16-item handler must not expose 1000 mB until the caller isolates a one-item stack.
- Verify the clicked tank always takes priority; a rejected tank transfer must **not** place fluid on an adjacent block.
- Include at least one modded tank (if available) and repeat across server/client boundaries.

This audit does not claim any of the above was run in Minecraft by the assistant.
