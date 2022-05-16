package game;

import game.core.Component;
import game.events.IDrawable;
import game.events.IUpdatable;
import game.maths.Rectangle;
import game.maths.Vector2;
import game.rendering.Sprite;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class SpriteRenderer extends Component implements IUpdatable, IDrawable {
    private Sprite sprite;
    private Transform transform;

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
    }

    public void onStart() {
        transform = (Transform)getComponent(Transform.class);
    }

    public void onUpdate() {

    }

    public void onDraw() {
        // Camera.getMain().getDrawGraphics().drawImage(texture);
        Graphics2D g = Camera.main().getDrawGraphics();
        g.setTransform(Camera.IDENTITY_TX);
        g.setColor(new Color(255, 0, 0));
        // g.drawRect(5, 5, 20, 10);
        Vector2 pos2 = Camera.main().worldToDrawPos(new Vector2(1, (int)Time.time() % 5));
        int size = (int)Camera.main().worldToDrawLength(1);
        g.drawRect((int)pos2.x, (int)pos2.y, size, size);

        Camera cam = Camera.main();
        Vector2 pos = transform.getPosition();
        // pos.x = Time.time() * 5 % 4;
        transform.setRotation(Time.time() * 5);

        Rectangle worldBounds = sprite.getWorldBounds();

        AffineTransform tx = new AffineTransform();
        float pixelsPerUnit = cam.getZoom() * cam.getPixelsPerUnit();
        // Move the (0, 0) point for drawing to the correct position on the texture.
        tx.translate(
                pixelsPerUnit * (pos.x - cam.getPosition().x + worldBounds.getLeft()),
                pixelsPerUnit * (pos.y - cam.getPosition().y) + worldBounds.getTop());
        // Rotate the image around its origin.
        tx.rotate(
                transform.getRotation(),
                -worldBounds.getLeft() * pixelsPerUnit,
                -worldBounds.getTop() * pixelsPerUnit);
        // Scale the drawing context so that the width (height) of the sprite matches
        // the correct amount of pixels on the screen.
        float scalingFactorForWidth = pixelsPerUnit / sprite.getPixelsPerUnit();
        tx.scale(scalingFactorForWidth, scalingFactorForWidth);

        g.setTransform(tx);
        g.drawImage(sprite.getTexture(), 0, 0, null);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
