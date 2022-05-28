package examples.collisions;

import inspiaaa.decaf.Transform;
import inspiaaa.decaf.collision.RectCollider;
import inspiaaa.decaf.events.IDrawable;
import inspiaaa.decaf.gameobject.Component;
import inspiaaa.decaf.maths.Rectangle;
import inspiaaa.decaf.maths.Vector2;
import inspiaaa.decaf.rendering.Camera;
import inspiaaa.decaf.rendering.GraphicsHelper;

import java.awt.*;

public class DrawCollider extends Component implements IDrawable {
    private RectCollider collider;
    private Transform transform;

    @Override
    public void onStart() {
        transform = (Transform) getComponent(Transform.class);
        collider = (RectCollider) getComponent(RectCollider.class);
    }

    @Override
    public void onDraw() {
        Camera cam = Camera.main();
        Graphics2D g = (Graphics2D) cam.getDrawTexture().getGraphics();
        GraphicsHelper.resetTransform(g);

        Rectangle rect = collider.getMovedCollider();
        Vector2 bottomLeft = cam.worldToDrawPos(rect.getPosition());
        float width = cam.worldToDrawLength(rect.width);
        float height = cam.worldToDrawLength(rect.height);
        g.setColor(Color.RED);
        g.drawRect((int)bottomLeft.x, (int)bottomLeft.y, (int)width, (int)height);
    }

    @Override
    public int getSortingOrder() {
        return 1;
    }
}
