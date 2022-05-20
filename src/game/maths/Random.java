package game.maths;

// Convenience wrapper around a static RandomHelper
public class Random {
    private static final RandomHelper instance = new RandomHelper();

    public void setSeed(long seed) { instance.setSeed(seed); }
    public float range(float minIncl, float maxIncl) { return instance.range(minIncl, maxIncl); }
    public int range(int minIncl, int maxIncl) { return instance.range(minIncl, maxIncl); }
}
