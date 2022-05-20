package inspiaaa.decaf.maths;

// Convenience wrapper around a static RandomHelper
public class Random {
    private static final RandomHelper instance = new RandomHelper();

    public void setSeed(long seed) { instance.setSeed(seed); }
    public float range(float minIncl, float maxIncl) { return instance.range(minIncl, maxIncl); }
    public int range(int minIncl, int maxIncl) { return instance.range(minIncl, maxIncl); }
    public float angleRad() { return instance.angleRad(); }
    public Vector2 insideUnitCircle() { return instance.insideUnitCircle(); }
    public Vector2 insideCircle(float radius) { return instance.insideCircle(radius); }
    public Vector2 onUnitCircle() { return instance.onUnitCircle(); }
    public Vector2 onCircle(float radius) { return instance.onCircle(radius); }
}
