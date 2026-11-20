package net.creeperhost.chickens.client;

/** Minecraft 1.21.x item-color callbacks expect opaque ARGB colors. */
public final class OpaqueTint {
    private OpaqueTint() {}
    public static int fromRgb(int rgb) {
        return 0xFF000000 | (rgb & 0x00FFFFFF);
    }
}
