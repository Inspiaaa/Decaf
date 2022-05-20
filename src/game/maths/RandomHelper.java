package game.maths;

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
}
