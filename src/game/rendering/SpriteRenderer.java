package game.rendering;

import game.Time;
import game.Transform;
import game.core.Component;
import game.events.IDrawable;
import game.maths.Rectangle;
import game.maths.Vector2;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class SpriteRenderer extends Component implements IDrawable {
    private Sprite sprite;
    private int sortingOrder;
    private Transform transform;

    public SpriteRenderer(Sprite sprite) {
        this(sprite, 0);
    }

    public SpriteRenderer(Sprite sprite, int sortingOrder) {
        this.sprite = sprite;
        this.sortingOrder = sortingOrder;
    }

    public void onStart() {
        transform = (Transform)getComponent(Transform.class);
    }

    @Override
    public void onDraw() {
        Camera cam = Camera.main();
        Graphics2D g = cam.getDrawGraphics();

        Vector2 worldPos = transform.getPosition();
        Vector2 scale = transform.getScale();
        Rectangle worldBounds = sprite.getWorldBounds();
        float pixelsPerUnit = cam.getZoom() * cam.getPixelsPerUnit();

        AffineTransform tx = new AffineTransform();

        // Move the (0, 0) point for drawing to the correct position on the texture.
        tx.translate(
                pixelsPerUnit * (worldPos.x - cam.getPosition().x + worldBounds.getLeft()),
                pixelsPerUnit * (worldPos.y - cam.getPosition().y + worldBounds.getTop()));

        // Subtract the origin, so that the sprite can be scaled and rotated around the origin.
        tx.translate(
                -worldBounds.getLeft() * pixelsPerUnit,
                -worldBounds.getTop() * pixelsPerUnit);

        // Rotate the image around its origin.
        tx.rotate(transform.getRotation());
        tx.scale(scale.x, scale.y);

        // Move it by its origin again
        tx.translate(
                worldBounds.getLeft() * pixelsPerUnit,
                worldBounds.getTop() * pixelsPerUnit);

        // Scale the drawing context so that the width (height) of the sprite matches
        // the correct amount of pixels on the screen.
        float scalingFactorForWidth = pixelsPerUnit / sprite.getPixelsPerUnit();
        tx.scale(scalingFactorForWidth, scalingFactorForWidth);

        g.setTransform(tx);
        g.drawImage(sprite.getTexture(), 0, 0, null);
    }

    @Override
    public int getSortingOrder() {
        return sortingOrder;
    }

    public void setSortingOrder(int sortingOrder) {
        this.sortingOrder = sortingOrder;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
