package inspiaaa.decaf.rendering;

import inspiaaa.decaf.Transform;
import inspiaaa.decaf.core.Component;
import inspiaaa.decaf.events.IDrawable;
import inspiaaa.decaf.maths.Vector2;

import java.awt.*;

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
        Vector2 cameraPos = cam.getTopLeftPosition();
        float pixelsPerUnit = cam.getPixelsPerUnit();

        float xInPixels = pixelsPerUnit * (worldPos.x - cameraPos.x);
        float yInPixels = pixelsPerUnit * (worldPos.y - cameraPos.y);

        // Scale the drawing context so that the width (height) of the sprite matches
        // the correct amount of pixels on the screen.
        float scalingFactorForWidth = pixelsPerUnit / sprite.getPixelsPerUnit();

        Vector2 scale = transform.getScale().mul(scalingFactorForWidth);
        float rotation = transform.getRotation();
        GraphicsHelper.drawImageWithScaleRotationOrigin(
                g,
                sprite.getTexture(),
                xInPixels,
                yInPixels,
                scale,
                rotation,
                sprite.getOrigin());

        GraphicsHelper.resetTransform(g);
        g.setColor(Color.RED);
        g.drawRect((int)xInPixels, (int)yInPixels, 1, 1);
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
