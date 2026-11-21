package net.creeperhost.chickens.data;

/**
 * Port of the original 1.0.32 stat semantics. Persistence via ItemStack data components
 * must be implemented by the Minecraft-specific adapter.
 */
public final class ChickenStats {
    private int gain;
    private int growth;
    private int strength;

    public ChickenStats() { this(1, 1, 1); }

    public ChickenStats(int gain, int growth, int strength) {
        setGain(gain);
        setGrowth(growth);
        setStrength(strength);
    }

    public int getGain() { return gain; }
    public int getGrowth() { return growth; }
    public int getStrength() { return strength; }

    public void setGain(int gain) { this.gain = Math.clamp(gain, 1, 10); }
    public void setGrowth(int growth) { this.growth = Math.clamp(growth, 1, 10); }
    public void setStrength(int strength) { this.strength = Math.clamp(strength, 1, 10); }
}
