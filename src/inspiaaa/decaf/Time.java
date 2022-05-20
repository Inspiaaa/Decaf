package inspiaaa.decaf;

public class Time {
    private static Time instance;

    // Time in nano seconds when the timer started.
    private final long startTimeInNs;

    // Time in seconds of the start of the last frame.
    private float lastFrameTime;
    // Time in seconds of the start of the frame
    private float time;
    // Time in seconds between the last frame and the current frame
    private float deltaTime;

    private static final float S_TO_NS = 1000000000f;
    private static final float NS_TO_S = 1f / S_TO_NS;
    private static final float S_TO_MS = 1000f;

    public Time() {
        startTimeInNs = System.nanoTime();
        lastFrameTime = getTimeSinceStart();
        time = lastFrameTime;
    }

    private float getTimeSinceStart() {
        return (System.nanoTime() - startTimeInNs) * NS_TO_S;
    }

    public void update() {
        lastFrameTime = time;
        time = getTimeSinceStart();
        deltaTime = time - lastFrameTime;
    }

    public float getFps() {
        return 1 / deltaTime;
    }

    public void capFps(float fps) {
        if (fps == 0)
            return;

        float elapsedSinceStartOfFrame = getTimeSinceStart() - time;
        float targetDelayBetweenFrames = 1 / fps;

        if (elapsedSinceStartOfFrame >= targetDelayBetweenFrames)
            return;

        float delayToWait = targetDelayBetweenFrames - elapsedSinceStartOfFrame;
        try {
            Thread.sleep((long)(delayToWait * S_TO_MS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public float getTime() {
        return time;
    }

    public static Time getInstance() {
        if (instance == null) {
            instance = new Time();
        }
        return instance;
    }

    public static float time() {
        return instance.time;
    }

    public static float deltaTime() {
        return instance.deltaTime;
    }
}
