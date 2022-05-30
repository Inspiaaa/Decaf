package inspiaaa.decaf;

import inspiaaa.decaf.collision.CollisionEngine;
import inspiaaa.decaf.events.IDestroyable;
import inspiaaa.decaf.events.IDrawable;
import inspiaaa.decaf.events.IUpdatable;
import inspiaaa.decaf.input.Keyboard;
import inspiaaa.decaf.input.Mouse;
import inspiaaa.decaf.rendering.Camera;
import inspiaaa.decaf.rendering.GraphicsHelper;
import inspiaaa.decaf.util.ObjectPool;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

public class Scene implements IScene {
    private Camera worldCamera;
    private Camera uiCamera;
    private final CollisionEngine collisionEngine;

    private float pixelsPerUnit = 16;
    private float cameraSize = 8;
    private float collisionEngineChunkSize = 5;

    private Color backgroundColor;
    private BufferedImage screen;

    // Events
    // If the number of objects can change during iteration, it uses the custom ObjectPool class
    // which is similar to a HashSet, but allows for mutations during iteration.
    private final ObjectPool<IUpdatable> objectsToUpdate;
    private final HashSet<IDrawable> objectsToDraw;
    private final ObjectPool<IDestroyable> objectsToDestroy;

    // Cached array for sorting the objectsToDraw (to avoid unnecessary GC alloc on each draw call)
    private IDrawable[] objectsToDrawSortedByDepth = new IDrawable[0];

    public Scene() {
        this.objectsToUpdate = new ObjectPool<IUpdatable>();
        this.objectsToDraw = new HashSet<IDrawable>();
        this.objectsToDestroy = new ObjectPool<IDestroyable>();
        this.collisionEngine = new CollisionEngine(collisionEngineChunkSize);

        this.backgroundColor = Color.WHITE;
    }

    @Override
    public void start(int width, int height) {
        screen = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        worldCamera = new Camera(pixelsPerUnit, cameraSize, width, height);
        worldCamera.markAsMain();
        uiCamera = new Camera(pixelsPerUnit, cameraSize, width, height);
        uiCamera.markAsUi();
    }

    @Override
    public void onResize(int width, int height) {
        screen = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        worldCamera.onResize(width, height);
        uiCamera.onResize(width, height);
    }

    public void update() {
        Keyboard.update();
        Mouse.update();

        GraphicsHelper.fill(screen, backgroundColor);
        worldCamera.beginNextFrame();
        uiCamera.beginNextFrame();

        dispatchOnUpdate();
        dispatchOnDraw();
        dispatchOnDestroy();

        worldCamera.renderToScreen(screen);
        uiCamera.renderToScreen(screen);
    }

    // Calls the onUpdate method of all objects that have subscribed to this event.
    private void dispatchOnUpdate() {
        objectsToUpdate.lock();
        for (IUpdatable obj : objectsToUpdate) {
            try {
                obj.onUpdate();
            }
            catch (Exception e) { e.printStackTrace(); }
        }
        objectsToUpdate.unlock();
    }

    // Calls the onDraw method of all objects that have subscribed to this event.
    private void dispatchOnDraw() {
        // Sorts the objects so that the objects with the highest sorting order are drawn on top
        // of objects with a lower sorting order.

        // Reduce the size of the array when it goes below a certain threshold of items (3/4 empty)
        if (objectsToDrawSortedByDepth.length > objectsToDraw.size() * 4) {
            objectsToDrawSortedByDepth = new IDrawable[objectsToDraw.size()];
        }

        objectsToDrawSortedByDepth = objectsToDraw.toArray(objectsToDrawSortedByDepth);
        Arrays.sort(objectsToDrawSortedByDepth, 0, objectsToDraw.size(), new Comparator<IDrawable>() {
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

    private void dispatchOnDestroy() {
        objectsToDestroy.lock();
        for (IDestroyable obj : objectsToDestroy) {
            try {
                obj.onDestroy();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        objectsToDestroy.unlock();
        objectsToDestroy.clear();
    }

    @Override
    public void destroy() {

    }

    @Override
    public BufferedImage getScreen() {
        return screen;
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

    public void registerForDestroy(IDestroyable obj) {
        objectsToDestroy.add(obj);
    }

    public void unregisterForDestroy(IDestroyable obj) {
        objectsToDestroy.remove(obj);
    }

    public CollisionEngine getCollisionEngine() {
        return collisionEngine;
    }
}
