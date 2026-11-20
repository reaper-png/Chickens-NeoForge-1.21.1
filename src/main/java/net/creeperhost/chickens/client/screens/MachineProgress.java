package net.creeperhost.chickens.client.screens;

/** Render-only mapping of the original 1.0.32 1000-tick-progress GUI. */
public final class MachineProgress {
    public static final int CYCLE = 1000;
    public static final int WIDTH = 26;

    private MachineProgress() {}

    public static int width(int progress) {
        if (progress <= 0) return 0;
        int clamped = Math.min(CYCLE, progress);
        return Math.min(WIDTH, 1 + clamped * (WIDTH - 1) / CYCLE);
    }

    public static double percent(int progress) {
        return Math.max(0, Math.min(CYCLE, progress)) / 10.0;
    }
}
