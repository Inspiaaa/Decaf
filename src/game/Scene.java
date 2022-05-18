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

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashSet;

public class Scene implements IScene {
    private Camera camera;
    private float pixelsPerUnit = 16;
    private float cameraSize = 8;

    // Events
    private final HashSet<IUpdatable> objectsToUpdate;
    private final HashSet<IDrawable> objectsToDraw;

    public Scene() {
        this.objectsToUpdate = new HashSet<IUpdatable>();
        this.objectsToDraw = new HashSet<IDrawable>();
    }

    @Override
    public void start(int width, int height) {
        camera = new Camera(pixelsPerUnit, cameraSize, width, height);
        camera.markAsMain();

        GameObject go = new GameObject(this);
        go.addComponent(new Transform(new Vector2(1, 2)));
        go.addComponent(new SpriteRenderer(new Sprite("./Player.png", 16, new Vector2(0.5f, 1))));
    }

    public void update() {
        Keyboard.update();
        Mouse.update();
        camera.preUpdate();

        for (IUpdatable updatable : objectsToUpdate) {
            try {
                updatable.onUpdate();
            }
            catch (Exception e) { e.printStackTrace(); }
        }
        for (IDrawable updatable : objectsToDraw) {
            try {
                updatable.onDraw();
            }
            catch (Exception e) { e.printStackTrace(); }
        }

        camera.postUpdate();
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
