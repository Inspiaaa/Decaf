package inspiaaa.decaf.rendering;

import inspiaaa.decaf.maths.Vector2;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
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

    // TODO: Introduce a boolean whether it should match the screen resolution
    // Then it should dynamically update the pixelsPerUnit to match the screen resolution

    private float zoom;
    private Vector2 position;

    private int screenWidth;
    private int screenHeight;

    private AffineTransform worldToDrawTransform;
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
        updateTexture();
    }

    private void updateTexture() {
        // Make the draw texture match the targetHeightInUnits.
        float drawHeight = targetHeightInUnits * zoom * pixelsPerUnit;
        // But make the width scale up to the aspect ratio of the actual screen.
        float drawWidth = drawHeight * ((float)screenWidth / screenHeight);
        drawTexture = new BufferedImage((int)drawWidth, (int)drawHeight, BufferedImage.TYPE_INT_ARGB);

        drawGraphics = (Graphics2D)drawTexture.getGraphics();
        updateTransforms();
    }

    private void updateTransforms() {
        worldToDrawTransform = new AffineTransform();
        worldToDrawTransform.translate(-position.x, -position.y);
        worldToDrawTransform.scale(pixelsPerUnit, pixelsPerUnit);

        drawToScreenTransform = new AffineTransform();
        double scalingFactor = zoom * (double)screenWidth / drawTexture.getWidth();
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

    public Vector2 worldToDrawPos(Vector2 pos) {
        Point2D transformed = worldToDrawTransform.transform(new Point2D.Float(pos.x, pos.y), null);
        return new Vector2((float)transformed.getX(), (float)transformed.getY());
    }

    public float worldToDrawLength(float lengthInWorld) {
        return lengthInWorld * (float)worldToDrawTransform.getScaleX();
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        updateTransforms();
    }

    public Vector2 getPosition() {
        // TODO: Copy this instance?
        return this.position;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
        updateTransforms();
    }

    public float getZoom() {
        return zoom;
    }

    public AffineTransform getWorldToDrawTransform() {
        return worldToDrawTransform;
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
