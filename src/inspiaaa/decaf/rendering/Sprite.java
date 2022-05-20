package inspiaaa.decaf.rendering;

import inspiaaa.decaf.maths.Rectangle;
import inspiaaa.decaf.maths.Vector2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Sprite {
    public static final Vector2 ORIGIN_CENTER = new Vector2(0.5f, 0.5f);
    public static final Vector2 ORIGIN_BOTTOM_CENTER = new Vector2(0.5f, 1f);

    private BufferedImage texture;
    private float pixelsPerUnit;
    private Vector2 origin;

    private static BufferedImage readImage(String filepath) {
        try {
            // TODO: Does this need to be converted to BufferedImage.TYPE_INT_ARGB ?
            return ImageIO.read(new File(filepath));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Sprite(String filepath, float pixelsPerUnit) {
        this(readImage(filepath), pixelsPerUnit, ORIGIN_CENTER);
    }

    public Sprite(String filepath, float pixelsPerUnit, Vector2 origin) {
        this(readImage(filepath), pixelsPerUnit, origin);
    }

    public Sprite(BufferedImage texture, float pixelsPerUnit, Vector2 origin) {
        this.texture = texture;
        this.pixelsPerUnit = pixelsPerUnit;
        this.origin = origin;
    }

    public BufferedImage getTexture() {
        return texture;
    }

    public float getPixelsPerUnit() {
        return pixelsPerUnit;
    }

    public Rectangle getWorldBounds() {
        float widthInWorldUnits = texture.getWidth() / pixelsPerUnit;
        float heightInWorldUnits = texture.getHeight() / pixelsPerUnit;

        return new Rectangle(
                -widthInWorldUnits * origin.x,
                -heightInWorldUnits * origin.y,
                widthInWorldUnits,
                heightInWorldUnits
        );
    }

    public Vector2 getOrigin() {
        return origin;
    }
}
