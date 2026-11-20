# Stage 10 — Vanilla chicken to Smart Chicken

## Functionality

The original Chickens mod lets the player use a vanilla book on a vanilla chicken to turn it into a Smart Chicken.

This port registers a normal NeoForge entity-interaction listener for that action:

- Only the actual vanilla `minecraft:chicken` entity qualifies; modded chicken breeds are excluded.
- Only a vanilla `minecraft:book` qualifies.
- Creates the registered `chickens:smart_chicken` at the original position with matching age, rotation, remaining health, and custom name.
- Performs entity replacement and book consumption on the server only.
- Consumes one book in survival/adventure, none in creative.
- Does not remove the original entity or consume a book if spawning the replacement fails.
- No new item ID or texture is introduced.

## Jade display name

The NeoForge mod metadata `displayName` is now simply **Chickens**, rather than the long port/work-in-progress text. The technical version and description remain in metadata.

## Validation

1. `git fetch origin` and `git switch feature/stage-10-smart-chicken-conversion`.
2. `gradlew.bat clean build` under Java 21.
3. With one book, right-click a vanilla adult chicken in survival. Confirm it becomes a Smart Chicken and one book is consumed.
4. Try a baby vanilla chicken, preserving its age.
5. Try the interaction in creative (book stays).
6. Check other modded chickens cannot be converted with a book.
7. Confirm Jade shows **Chickens** as the mod name and the original entity overlay still works.

Not compiled or tested in this environment.
