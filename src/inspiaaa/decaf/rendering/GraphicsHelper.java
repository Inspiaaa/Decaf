package inspiaaa.decaf.rendering;

import inspiaaa.decaf.maths.Vector2;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class GraphicsHelper {
    public static final AffineTransform IDENTITY_TX = new AffineTransform();
    private static final AffineTransform cachedTransform = new AffineTransform();

    public static void fill(BufferedImage image, Color color) {
        Graphics2D g = (Graphics2D)image.getGraphics();

        g.setTransform(IDENTITY_TX);
        g.setColor(color);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
    }

    public static void overwriteFill(BufferedImage image, Color color) {
        Graphics2D g = (Graphics2D)image.getGraphics();

        g.setTransform(IDENTITY_TX);
        g.setColor(color);

        // When filling with transparent colors, by default the image will not be affected.
        // By changing the composite rule, the color can overwrite the image regardless of its
        // alpha value.
        g.setComposite(AlphaComposite.Src);

        g.fillRect(0, 0, image.getWidth(), image.getHeight());

        // Reset to default
        g.setComposite(AlphaComposite.SrcOver);
    }

    public static void clear(BufferedImage image) {
        Graphics2D g = (Graphics2D)image.getGraphics();

        g.setTransform(IDENTITY_TX);
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        // Reset to default
        g.setComposite(AlphaComposite.SrcOver);
    }

    public static void resetTransform(Graphics2D graphics) {
        graphics.setTransform(IDENTITY_TX);
    }

    public static void drawImage(Graphics2D g, BufferedImage image, float x, float y) {
        resetTransform(g);
        g.drawImage(image, (int)x, (int)y, null);
    }

    // Draws an image that is scaled and rotated around its origin.
    // This overload reuses an internal AffineTransform instance and thus should not be used by
    // multiple threads.
    public static void drawImageWithScaleRotationOrigin(
            Graphics2D g,
            BufferedImage image,
            float x,
            float y,
            Vector2 scale,
            float rotation,
            Vector2 origin) {

        drawImageWithScaleRotationOrigin(
                g,
                image,
                x,
                y,
                scale,
                rotation,
                origin,
                cachedTransform
        );
    }

    // Draws an image that is scaled and rotated around its origin.
    // Allows you to pass an AffineTransform in (instead of it automatically reusing an AffineTransform instance).
    public static void drawImageWithScaleRotationOrigin(
            Graphics2D g,
            BufferedImage image,
            float x,
            float y,
            Vector2 scale,
            float rotation,
            Vector2 origin,
            AffineTransform tx) {

        tx.setToIdentity();

        int width = image.getWidth();
        int height = image.getHeight();

        float originOffsetX = width * origin.x;
        float originOffsetY = height * origin.y;

        // It seems that Rotate, Scale, Translate order common for transform matrices is reversed.
        // Reading the operations from top to bottom can help to understand what is being done.

        tx.translate(x, y);

        if (scale.x != 1 || scale.y != 1) {
            // Scale around (0, 0) (the origin)
            tx.scale(scale.x, scale.y);
        }

        if (rotation != 0) {
            // Rotate the image around (0, 0) (the origin)
            tx.rotate(rotation);
        }

        if (originOffsetX != 0 || originOffsetY != 0) {
            // Make the origin (0, 0)
            tx.translate(-originOffsetX, -originOffsetY);
        }

        g.setTransform(tx);
        g.drawImage(image, 0, 0, null);
    }
}
