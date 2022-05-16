package game;

import game.maths.Vector2;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Camera {
    public static final AffineTransform IDENTITY_TX = new AffineTransform();
    private static Camera main;

    // Low resolution texture for drawing the pixel art,
    // in order to maintain a retro art style and feel.
    private BufferedImage drawTexture;
    private Graphics2D drawGraphics;

    // High resolution image that is drawn to the screen.
    private BufferedImage screenTexture;
    private Graphics2D screenGraphics;

    private float pixelsPerUnit;
    private float zoom;
    private Vector2 position;
    private float targetWorldSize;

    private Color backgroundColor; // TODO

    private AffineTransform worldToDrawTransform;
    private AffineTransform drawToScreenTransform;

    public Camera(float pixelsPerUnit, float targetWorldSize, int startWidth, int startHeight) {
        this.pixelsPerUnit = pixelsPerUnit;
        this.targetWorldSize = targetWorldSize;
        this.zoom = 1;
        this.position = new Vector2();
        this.backgroundColor = Color.WHITE;

        updateTextures(startWidth, startHeight);
    }

    public void markAsMain() {
        main = this;
    }

    public void onResize(int width, int height) {
        updateTextures(width, height);
    }

    private void updateTextures(int width, int height) {
        screenTexture = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Make the draw texture match the targetWorldSize.
        float drawHeight = targetWorldSize * zoom * pixelsPerUnit;
        // But make the width scale up to the aspect ratio of the actual screen.
        float drawWidth = drawHeight * ((float)width / height);
        drawTexture = new BufferedImage((int)drawWidth, (int)drawHeight, BufferedImage.TYPE_INT_RGB);

        screenGraphics = (Graphics2D)screenTexture.getGraphics();
        drawGraphics = (Graphics2D)drawTexture.getGraphics();
        updateTransforms();
    }

    private void updateTransforms() {
        worldToDrawTransform = new AffineTransform();
        worldToDrawTransform.translate(-position.x, -position.y);
        worldToDrawTransform.scale(pixelsPerUnit, pixelsPerUnit);

        drawToScreenTransform = new AffineTransform();
        double scalingFactor = zoom * (double)screenTexture.getWidth() / drawTexture.getWidth();
        drawToScreenTransform.scale(scalingFactor, scalingFactor);
    }

    public void preUpdate() {
        // Clear the screen
        drawGraphics.setTransform(IDENTITY_TX);
        drawGraphics.setColor(backgroundColor);
        drawGraphics.fillRect(0, 0, drawTexture.getWidth(), drawTexture.getHeight());
    }

    public void postUpdate() {
        // Copy the drawTexture to the screenTexture
        screenGraphics.setTransform(drawToScreenTransform);
        screenGraphics.drawImage(drawTexture, 0, 0, null);
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

    public BufferedImage getScreenTexture() {
        return screenTexture;
    }

    public BufferedImage getDrawTexture() {
        return drawTexture;
    }

    public Graphics2D getScreenGraphics() {
        return screenGraphics;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public float getPixelsPerUnit() {
        return pixelsPerUnit;
    }

    public static Camera main() {
        return main;
    }
}
