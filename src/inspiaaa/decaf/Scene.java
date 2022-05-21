package inspiaaa.decaf;

import inspiaaa.decaf.events.IDrawable;
import inspiaaa.decaf.events.IUpdatable;
import inspiaaa.decaf.input.Keyboard;
import inspiaaa.decaf.input.Mouse;
import inspiaaa.decaf.rendering.Camera;
import inspiaaa.decaf.rendering.GraphicsHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

public class Scene implements IScene {
    private Camera worldCamera;
    private Camera uiCamera;

    private float pixelsPerUnit = 16;
    private float cameraSize = 8;

    private Color backgroundColor;
    private BufferedImage screen;

    // Events
    private final HashSet<IUpdatable> objectsToUpdate;
    private final HashSet<IDrawable> objectsToDraw;

    public Scene() {
        this.objectsToUpdate = new HashSet<IUpdatable>();
        this.objectsToDraw = new HashSet<IDrawable>();

        backgroundColor = Color.WHITE;
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

        worldCamera.renderToScreen(screen);
        uiCamera.renderToScreen(screen);
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
}
