package inspiaaa.decaf.collision;

import inspiaaa.decaf.Transform;
import inspiaaa.decaf.gameobject.Component;
import inspiaaa.decaf.maths.Rectangle;
import inspiaaa.decaf.maths.Vector2;

public class RectCollider extends Component {
    private CollisionEngine collisionEngine;
    private Transform transform;

    // Base collider
    private Rectangle collider;
    private int layerMask;

    // Collider that has been moved in world space
    private Rectangle movedCollider;
    private Vector2 position;

    public Rectangle getMovedCollider() {
        return movedCollider;
    }

    public void setPosition(Vector2 position) {
        // TODO: Update transform position
        movedCollider.setPosition(collider.getPosition().add(position));
        this.position = position;
    }

    public Vector2 getPosition() {
        return position;
    }
}
