package inspiaaa.decaf.rendering;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class GraphicsHelper {
    public static final AffineTransform IDENTITY_TX = new AffineTransform();

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
}
