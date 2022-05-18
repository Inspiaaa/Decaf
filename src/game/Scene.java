package game;

import game.core.GameObject;
import game.events.IDrawable;
import game.events.IUpdatable;
import game.input.Keyboard;
import game.input.Mouse;
import game.maths.Vector2;
import game.rendering.Camera;
import game.rendering.Sprite;
import game.rendering.SpriteRenderer;
import game.sound.Music;
import game.sound.SoundEffect;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

public class Scene implements IScene {
    private Camera camera;
    private float pixelsPerUnit = 16;
    private float cameraSize = 8;

    // Events
    private final HashSet<IUpdatable> objectsToUpdate;
    private final HashSet<IDrawable> objectsToDraw;

    private Music music;

    public Scene() {
        this.objectsToUpdate = new HashSet<IUpdatable>();
        this.objectsToDraw = new HashSet<IDrawable>();

        music = new Music("Level 1.wav");
        music.play();
    }

    @Override
    public void start(int width, int height) {
        camera = new Camera(pixelsPerUnit, cameraSize, width, height);
        camera.markAsMain();

        GameObject go = new GameObject(this);
        go.addComponent(new Transform(new Vector2(1, 2)));
        go.addComponent(new SpriteRenderer(new Sprite("./Player.png", 16, new Vector2(0.5f, 1))));
    }

    // Calls the onUpdate method of all objects that have subscribed to this event.
    private void dispatchOnUpdate() {
        for (IUpdatable obj : objectsToUpdate) {
            try {
                obj.onUpdate();
            }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    // Calls the onDraw method of all objects that have subscribed to this event.
    private void dispatchOnDraw() {
        // Sorts the objects so that the objects with the highest sorting order are drawn on top
        // of objects with a lower sorting order.
        IDrawable[] objectsToDrawSortedByDepth = objectsToDraw.toArray(new IDrawable[objectsToDraw.size()]);
        Arrays.sort(objectsToDrawSortedByDepth, new Comparator<IDrawable>() {
            @Override
            public int compare(IDrawable a, IDrawable b) {
                return Integer.compare(a.getSortingOrder(), b.getSortingOrder());
            }
        });

        for (IDrawable obj : objectsToDrawSortedByDepth) {
            try {
                obj.onDraw();
            }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    public void update() {
        Keyboard.update();
        Mouse.update();

        camera.preUpdate();

        dispatchOnUpdate();
        dispatchOnDraw();

        camera.postUpdate();
        // System.out.println(Time.getInstance().getFps());

        if (Mouse.isButtonJustDown(1)) {
            SoundEffect sfx = new SoundEffect("Shield Metal 2_5.wav");
            sfx.setMasterVolume(1);
            sfx.play();

//            if (music.isPlaying()) {
//                music.pause();
//            }
//            else {
//                music.resume();
//                // music.play();
//            }
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public BufferedImage getScreen() {
        return camera.getScreenTexture();
    }

    @Override
    public void onResize(int width, int height) {
        camera.onResize(width, height);
    }

    // Adds an object to the event loop, based on its interfaces (e.g. IUpdatable)
    public void register(Object obj) {
        if (obj instanceof IUpdatable) objectsToUpdate.add((IUpdatable)obj);
        if (obj instanceof IDrawable) objectsToDraw.add((IDrawable)obj);
    }

    // Removes an object from the event loop.
    public void unregister(Object obj) {
        if (obj instanceof IUpdatable) objectsToUpdate.remove((IUpdatable)obj);
        if (obj instanceof IDrawable) objectsToDraw.remove((IDrawable)obj);
    }
}
