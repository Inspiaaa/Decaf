package inspiaaa.decaf.maths;

import java.util.Random;

// Utility class for generating random numbers
public class RandomHelper {
    private Random random;

    public RandomHelper() {
        random = new Random();
    }

    public void setSeed(long seed) {
        random.setSeed(seed);
    }

    public float range(float minIncl, float maxIncl) {
        return random.nextFloat() * (maxIncl - minIncl) + minIncl;
    }

    public int range(int minIncl, int maxIncl) {
        return random.nextInt(maxIncl - minIncl + 1) + minIncl;
    }

    public float angleRad() {
        return 2 * (float)Math.PI * random.nextFloat();
    }

    public Vector2 insideUnitCircle() {
        return insideCircle(1);
    }

    public Vector2 insideCircle(float radius) {
        float uniformRadiusInUnitCircle = (float)Math.sqrt(random.nextFloat());
        return Vector2.fromPolarRad(radius * uniformRadiusInUnitCircle, angleRad());
    }

    public Vector2 onUnitCircle() {
        return onCircle(1);
    }

    public Vector2 onCircle(float radius) {
        return Vector2.fromPolarRad(radius, angleRad());
    }
}
