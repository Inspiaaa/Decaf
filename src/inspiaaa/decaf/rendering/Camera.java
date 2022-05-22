package inspiaaa.decaf.rendering;

import inspiaaa.decaf.maths.Vector2;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Camera {
    private static Camera main;
    private static Camera ui;

    // Low resolution texture for drawing the pixel art,
    // in order to maintain a retro art style and feel.
    private BufferedImage drawTexture;
    private Graphics2D drawGraphics;

    private float pixelsPerUnit;
    private float targetHeightInUnits;

    // Dynamically calculated after each resize
    private float screenPixelsPerUnit;

    // TODO: Introduce a boolean whether it should match the screen resolution
    // Then it should dynamically update the pixelsPerUnit to match the screen resolution

    private float zoom;
    // Position of the top left corner of the camera
    private Vector2 topLeftPosition;
    // Position of the center of the camera
    private Vector2 position;

    private int screenWidth;
    private int screenHeight;

    private AffineTransform drawToScreenTransform;

    public Camera(float pixelsPerUnit, float targetHeightInUnits, int startWidth, int startHeight) {
        this.pixelsPerUnit = pixelsPerUnit;
        this.targetHeightInUnits = targetHeightInUnits;
        this.zoom = 1;
        this.position = new Vector2();

        onResize(startWidth, startHeight);
    }

    public void onResize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
        screenPixelsPerUnit = screenHeight / targetHeightInUnits;
        updateTopLeftPos();

        updateTexture();
    }

    private void updateTopLeftPos() {
        topLeftPosition = position.sub(
                0.5f * screenWidth / screenPixelsPerUnit,
                0.5f * screenHeight / screenPixelsPerUnit
        );
    }

    private void updateTexture() {
        // Make the draw texture match the targetHeightInUnits.
        float drawHeight = targetHeightInUnits * pixelsPerUnit;
        // But make the width scale up to the aspect ratio of the actual screen.
        float drawWidth = drawHeight * ((float)screenWidth / screenHeight);
        drawTexture = new BufferedImage((int)drawWidth, (int)drawHeight, BufferedImage.TYPE_INT_ARGB);

        drawGraphics = (Graphics2D)drawTexture.getGraphics();
        updateTransform();
    }

    private void updateTransform() {
        drawToScreenTransform = new AffineTransform();

        // As the texture is scaled from the top left corner, if zoom > 1 it will overshoot in size
        // to the right and to the bottom. To avoid this, it is scaled from the center of the screen.
        drawToScreenTransform.translate(
                -0.5f * screenWidth * (zoom - 1),
                -0.5f * screenHeight * (zoom - 1));

        double scalingFactor = zoom * (screenPixelsPerUnit / pixelsPerUnit);
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
        return worldPos.sub(topLeftPosition).mul(pixelsPerUnit * zoom);
    }

    public Vector2 screenToWorldPos(Vector2 screenPos) {
        return topLeftPosition.add(screenPos.div(screenPixelsPerUnit * zoom));
    }

    public Vector2 worldToScreenPos(Vector2 worldPos) {
        return worldPos.sub(topLeftPosition).mul(screenPixelsPerUnit * zoom);
    }

    public float worldToDrawLength(float lengthInWorld) {
        return lengthInWorld * zoom * pixelsPerUnit;
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
        this.zoom = zoom;
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
        return pixelsPerUnit;
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
