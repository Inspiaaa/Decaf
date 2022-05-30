package examples.sound;

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
import inspiaaa.decaf.sound.SoundEffect;
import inspiaaa.decaf.sound.SoundEffectInstance;
import inspiaaa.decaf.sound.SpatialAudio;

public class SpatialSoundScene extends Scene {
    private Music music;
    private Sprite sprite = new Sprite("./Player.png", 16, Sprite.ORIGIN_BOTTOM_CENTER);

    @Override
    public void start(int width, int height) {
        super.start(width, height);

//        music = new Music("Level 1.wav");
//        music.play();

        GameObject go = new GameObject();
        go.addComponent(new Transform(new Vector2(1, 2)));
        go.addComponent(new SpriteRenderer(sprite));
        go.addComponent(new RotateComponent());

//        for (int i = 0; i < 20; i ++) {
//            GameObject g = new GameObject();
//            g.addComponent(new Transform(new Vector2(Random.range(-5f, 5f), Random.range(-5f, 5f))));
//            g.addComponent(new SpriteRenderer(sprite));
//            g.addComponent(new examples.sound.ExampleComponent());
//        }
    }

    private float lastSpawnTime;
    private int spawnCount = 0;

    @Override
    public void update() {
        super.update();

        if (Time.time() - lastSpawnTime > 0.5 && spawnCount < 5000 && Time.time() > 1) {
            lastSpawnTime = Time.time();
            spawnCount ++;

            GameObject g = new GameObject();
            Vector2 pos = new Vector2(Random.range(-5f, 5f), Random.range(-5f, 5f));
            g.addComponent(new Transform(pos));
            g.addComponent(new SpriteRenderer(sprite));
            g.addComponent(new RotateComponent());

            SoundEffect sfx = new SoundEffect("Shield Metal 2_5.wav");
            sfx.setMasterVolume(1);
            SoundEffectInstance instance = sfx.play();
            instance.setPan(SpatialAudio.getPanForWorldPos(pos));
            instance.setVolume(SpatialAudio.getVolumeForWorldPos(pos));
        }

        System.out.println(Time.getInstance().getFps());

        Camera cam = Camera.main();

        cam.setZoom(cam.getZoom() * (float)Math.pow(1.1f, Mouse.getScrollAmount()));
        float moveSpeed = 2;

        if (Keyboard.isKeyDown(Keys.W)) {
            cam.setPosition(cam.getPosition().add(0, -moveSpeed * Time.deltaTime()));
        }
        if (Keyboard.isKeyDown(Keys.A)) {
            cam.setPosition(cam.getPosition().add(-moveSpeed * Time.deltaTime(), 0));
        }
        if (Keyboard.isKeyDown(Keys.S)) {
            cam.setPosition(cam.getPosition().add(0, moveSpeed * Time.deltaTime()));
        }
        if (Keyboard.isKeyDown(Keys.D)) {
            cam.setPosition(cam.getPosition().add(moveSpeed * Time.deltaTime(), 0));
        }

        if (Mouse.isButtonJustDown(1)) {
            SoundEffect sfx = new SoundEffect("Shield Metal 2_5.wav");
            sfx.setMasterVolume(1);
            SoundEffectInstance instance = sfx.play();
            instance.setPan(SpatialAudio.getPanForWorldPos(Camera.main().screenToWorldPos(Mouse.getPosition())));
//
//            if (music.isPlaying()) {
//                music.pause();
//            }
//            else {
//                // music.resume();
//                music.play();
            }
    }
}
