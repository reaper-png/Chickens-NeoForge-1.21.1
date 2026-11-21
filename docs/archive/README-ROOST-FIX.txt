CHICKENS 1.21.1 — Roost 3D rendering fix

Compared against the original chickensmod-1.18.2-1.0.32.jar,
RenderRoost.class and original Roost blockstate.

Files changed:
- client/RoostBlockEntityRenderer.java (NEW)
- client/ChickensClientEvents.java (register BlockEntityRenderer)
- blockentities/BlockEntityRoost.java (synchronize slot 0 to client)

Extract at the root of the NeoForge 1.21.1 project and overwrite corresponding files.
The patch does not contain ChickenItemRenderer.java, preserving your existing GUI scaling fix (1.15F).

Build: gradlew.bat clean build
WARNING: This patch has not been compiled or run in Minecraft in this environment.
Check visual direction, scale and synchronization after putting a chicken in the Roost.
