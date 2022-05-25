package inspiaaa.decaf.collision;

public class LayerMask {
    public final int ALL = ~0;
    public final int DEFAULT = 1;

    public static int merge(int maskA, int maskB) {
        return maskA | maskB;
    }

    public static int New(int layer) {
        return 1 << layer;
    }

    public static int New(int... layers) {
        int mask = 0;

        for (int layer : layers) {
            mask |= 1 << layer;
        }

        return mask;
    }

    public static boolean containsLayer(int mask, int layer) {
        return (mask & (1 << layer)) != 0;
    }

    public static boolean containsAnyLayer(int targetMask, int layerMask) {
        return (layerMask & targetMask) != 0;
    }
}
