package inspiaaa.decaf.rendering;

import inspiaaa.decaf.maths.Vector2;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Camera {
    /*
    Camera - How it works and what it does:
    The Camera class manages a texture that will be rendered to the screen and gives other class
    enough information (such as pixels per unit, offset, ...) so that they can render the objects
    properly.
    To make the zoom operation as efficient as possible, the camera works in the following way:
    Internally a texture is stored with screen resolution. If the camera is zoomed in, the sprites
    can be drawn with the target pixels per unit on to this texture (only a part of it will actually
    be visible and drawn to the screen).
    If the camera is zoomed out too far, so that the size of the texture would actually have to be
    increased in order to preserve the pixels per unit, it is kept at the screen resolution
    (because a higher resolution texture couldn't be rendered to the screen without losses).
    The actual pixels per unit therefore differ from the target pixels per unit.
     */
    private static Camera main;
    private static Camera ui;

    private BufferedImage screenTexture;
    // "Subimage" of the part of the full screenTexture that is drawn to the screen.
    // Its data is shared with the screenTexture
    private BufferedImage drawTexture;

    private Graphics2D drawGraphics;

    private float targetPixelsPerUnit;
    private float targetHeightInUnits;

    // Dynamically calculated after each resize
    private float screenPixelsPerUnit;
    // Sometimes when the targetPixelsPerUnit can not be reached (e.g. when zoomed out too far)
    // or when no targetPixelsPerUnit is given, the actual pixels per unit of the texture does not
    // match the target pixels per unit.
    private float actualPixelsPerUnit;

    // TODO: Introduce a boolean whether it should match the screen resolution
    // Then it should dynamically update the pixelsPerUnit to match the screen resolution

    private float zoom;
    // Position of the top left corner of the camera's rendering field
    private Vector2 topLeftPosition;
    // Position of the center of the camera
    private Vector2 position;

    private int screenWidth;
    private int screenHeight;
    private float drawWidth;
    private float drawHeight;

    private AffineTransform drawToScreenTransform;

    public Camera(float targetPixelsPerUnit, float targetHeightInUnits, int startWidth, int startHeight) {
        this.targetPixelsPerUnit = targetPixelsPerUnit;
        this.targetHeightInUnits = targetHeightInUnits;
        this.zoom = 1;
        this.position = new Vector2();

        onResize(startWidth, startHeight);
    }

    public void onResize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
        screenPixelsPerUnit = screenHeight / targetHeightInUnits;

        screenTexture = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        updateViewedPartOfTexture();
        updateTopLeftPos();
        updateTransform();
    }

    private void updateTopLeftPos() {
        topLeftPosition = position.sub(
                0.5f * drawWidth / actualPixelsPerUnit,
                0.5f * drawHeight / actualPixelsPerUnit
        );
    }

    private void updateViewedPartOfTexture() {
        if (targetPixelsPerUnit != -1) {
            // Make the draw texture match the targetHeightInUnits.
            drawHeight = targetHeightInUnits * targetPixelsPerUnit / zoom;
            // But make the width scale up to the aspect ratio of the actual screen.
            drawWidth = drawHeight * ((float)screenWidth / screenHeight);

            if (drawWidth > screenWidth) {
                drawWidth = screenWidth;
                drawHeight = screenHeight;
            }
        }
        else {
            drawWidth = screenWidth;
            drawHeight = screenHeight;
        }

        actualPixelsPerUnit = drawHeight * zoom / targetHeightInUnits;

        float widthDiff = screenWidth - drawWidth;
        float heightDiff = screenHeight - drawHeight;
        drawTexture = screenTexture.getSubimage(
                (int)Math.floor(widthDiff / 2),   // x
                (int)Math.floor(heightDiff / 2),  // y
                (int)Math.ceil(drawWidth),
                (int)Math.ceil(drawHeight)
        );
        drawGraphics = (Graphics2D)drawTexture.getGraphics();
    }

    private void updateTransform() {
        drawToScreenTransform = new AffineTransform();
        double scalingFactor = zoom * (screenPixelsPerUnit / actualPixelsPerUnit);
        drawToScreenTransform.scale(scalingFactor, scalingFactor);
    }

    public void beginNextFrame() {
        GraphicsHelper.clear(drawTexture);
    }

    public void renderToScreen(BufferedImage screen) {
        Graphics2D g = (Graphics2D)screen.getGraphics();

        // Copy the drawTexture to the screen
        g.setTransform(drawToScreenTransform);
        g.drawImage(drawTexture, 0, 0, null);
    }

    public Vector2 worldToDrawPos(Vector2 worldPos) {
        return worldPos.sub(topLeftPosition).mul(targetPixelsPerUnit * zoom);
    }

    public Vector2 screenToWorldPos(Vector2 screenPos) {
        return topLeftPosition.add(screenPos.div(screenPixelsPerUnit * zoom));
    }

    public Vector2 worldToScreenPos(Vector2 worldPos) {
        return worldPos.sub(topLeftPosition).mul(screenPixelsPerUnit * zoom);
    }

    public float worldToDrawLength(float lengthInWorld) {
        return lengthInWorld * zoom * targetPixelsPerUnit;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        updateTopLeftPos();
    }

    public Vector2 getPosition() {
        // TODO: Copy this instance?
        return this.position;
    }

    public Vector2 getTopLeftPosition() {
        return topLeftPosition;
    }

    public void setZoom(float zoom) {
        if (this.zoom == zoom) {
            return;
        }

        this.zoom = zoom;
        updateViewedPartOfTexture();
        updateTopLeftPos();
        updateTransform();
    }

    public float getZoom() {
        return zoom;
    }

    public Graphics2D getDrawGraphics() {
        return drawGraphics;
    }

    public BufferedImage getDrawTexture() {
        return drawTexture;
    }

    public float getPixelsPerUnit() {
        return actualPixelsPerUnit;
    }



    public void markAsMain() {
        main = this;
    }

    public void markAsUi() {
        ui = this;
    }

    public static Camera main() {
        return main;
    }

    public static Camera ui() {
        return ui;
    }
}
