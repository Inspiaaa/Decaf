package examples.stress;

import examples.sound.RotateComponent;
import inspiaaa.decaf.Scene;
import inspiaaa.decaf.Time;
import inspiaaa.decaf.Transform;
import inspiaaa.decaf.gameobject.GameObject;
import inspiaaa.decaf.input.Keyboard;
import inspiaaa.decaf.input.Keys;
import inspiaaa.decaf.input.Mouse;
import inspiaaa.decaf.maths.Random;
import inspiaaa.decaf.maths.Vector2;
import inspiaaa.decaf.rendering.Camera;
import inspiaaa.decaf.rendering.Sprite;
import inspiaaa.decaf.rendering.SpriteRenderer;
import inspiaaa.decaf.sound.Music;

public class StressTestScene extends Scene {
    private Music music;
    private Sprite sprite = new Sprite("./Player.png", 16, Sprite.ORIGIN_BOTTOM_CENTER);

    @Override
    public void start(int width, int height) {
        super.start(width, height);

        spawnGameObject(0, 0);
    }

    private float lastSpawnTime;
    private int spawnCount = 0;
    private int maxSpawnCount = 5000;
    private float spawnDelay = 0.01f;
    private float delayBeforeStart = 5;

    private float cameraMoveSpeed = 2;

    private void spawnGameObject(float x, float y) {
        GameObject go = new GameObject();
        go.addComponent(new Transform(new Vector2(x, y)));
        go.addComponent(new SpriteRenderer(sprite));
        go.addComponent(new RotateComponent());
        spawnCount ++;
    }

    @Override
    public void update() {
        super.update();

        if (Time.time() - lastSpawnTime > spawnDelay && spawnCount < maxSpawnCount && Time.time() > delayBeforeStart) {
            lastSpawnTime = Time.time();
            spawnCount ++;

            spawnGameObject(Random.range(-5f, 5f), Random.range(-5f, 5f));
        }

        System.out.println("Entities: " + spawnCount + " FPS: " + Time.getInstance().getFps());

        Camera cam = Camera.main();
        cam.setZoom(cam.getZoom() * (float)Math.pow(1.1f, Mouse.getScrollAmount()));

        Vector2 position = cam.getPosition().copy();

        if (Keyboard.isKeyDown(Keys.W)) {
            position.y -= cameraMoveSpeed * Time.deltaTime();
        }
        if (Keyboard.isKeyDown(Keys.A)) {
            position.x -= cameraMoveSpeed * Time.deltaTime();
        }
        if (Keyboard.isKeyDown(Keys.S)) {
            position.y += cameraMoveSpeed * Time.deltaTime();
        }
        if (Keyboard.isKeyDown(Keys.D)) {
            position.x += cameraMoveSpeed * Time.deltaTime();
        }

        cam.setPosition(position);
    }
}
