package net.creeperhost.chickens.blockentities;

/** Legacy Henhouse reach is a per-axis cube around each block's center. */
public final class HenhouseRange {
    private HenhouseRange() {}

    public static boolean contains(double chickenX, double chickenY, double chickenZ,
                                   int blockX, int blockY, int blockZ, double radius) {
        return Math.abs(chickenX - (blockX + 0.5D)) <= radius
                && Math.abs(chickenY - (blockY + 0.5D)) <= radius
                && Math.abs(chickenZ - (blockZ + 0.5D)) <= radius;
    }

    public static double distanceSquared(double chickenX, double chickenY, double chickenZ,
                                         int blockX, int blockY, int blockZ) {
        double x = chickenX - (blockX + 0.5D);
        double y = chickenY - (blockY + 0.5D);
        double z = chickenZ - (blockZ + 0.5D);
        return x * x + y * y + z * z;
    }
}
