package inspiaaa.decaf.maths;

// Convenience wrapper around a static RandomHelper
public class Random {
    private static final RandomHelper instance = new RandomHelper();

    public static void setSeed(long seed) { instance.setSeed(seed); }
    public static float range(float minIncl, float maxIncl) { return instance.range(minIncl, maxIncl); }
    public static int range(int minIncl, int maxIncl) { return instance.range(minIncl, maxIncl); }
    public static float angleRad() { return instance.angleRad(); }
    public static Vector2 insideUnitCircle() { return instance.insideUnitCircle(); }
    public static Vector2 insideCircle(float radius) { return instance.insideCircle(radius); }
    public static Vector2 onUnitCircle() { return instance.onUnitCircle(); }
    public static Vector2 onCircle(float radius) { return instance.onCircle(radius); }
}
