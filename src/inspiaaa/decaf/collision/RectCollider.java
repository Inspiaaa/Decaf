package inspiaaa.decaf.collision;

import inspiaaa.decaf.gameobject.Component;
import inspiaaa.decaf.maths.Rectangle;

public class RectCollider extends Component {
    private CollisionEngine collisionEngine;

    // Base collider
    private Rectangle collider;
    private int layerMask;

    // Collider that has been moved in word space
    private Rectangle movedCollider;

    public Rectangle getMovedCollider() {
        return movedCollider;
    }
}
