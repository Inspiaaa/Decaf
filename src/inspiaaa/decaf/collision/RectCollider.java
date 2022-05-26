package inspiaaa.decaf.collision;

import inspiaaa.decaf.IPositionListener;
import inspiaaa.decaf.Transform;
import inspiaaa.decaf.events.IUpdatable;
import inspiaaa.decaf.gameobject.Component;
import inspiaaa.decaf.maths.Rectangle;
import inspiaaa.decaf.maths.Vector2;

public class RectCollider extends Component implements IPositionListener {
    private CollisionEngine collisionEngine;
    private Transform transform;

    // Base collider
    private Rectangle collider;
    // Collider that has been moved in world space
    private Rectangle movedCollider;

    private int layerMask;
    private boolean isUpdatingPosition;

    public RectCollider(Rectangle collider) {
        this.collider = collider;
        this.movedCollider = collider.copy();
        this.isUpdatingPosition = false;
    }

    @Override
    public void onStart() {
        collisionEngine = getScene().getCollisionEngine();
        transform = (Transform) getComponent(Transform.class);

        transform.addPositionListener(this);
        collisionEngine.addEntity(this);
    }

    @Override
    public void onDestroy() {
        transform.removePositionListener(this);
    }

    @Override
    public void onPositionChange(Vector2 newPos) {
        if (isUpdatingPosition) return;

        moveTo(newPos);
    }

    public void teleportTo(Vector2 pos) {
        collisionEngine.moveEntity(this, pos);
    }

    public void moveTo(Vector2 pos) {
        // TODO: Call moveEntity() instead if this object should not collide
        collisionEngine.moveAndCollideEntity(this, pos);
    }

    public void moveBy(Vector2 deltaPos) {
        // TODO: Call moveEntity() instead if this object should not collide
        collisionEngine.moveAndCollideEntity(this, transform.getPosition().add(deltaPos));
    }

    protected void setPosition(Vector2 position) {
        if (isUpdatingPosition) return;
        isUpdatingPosition = true;
        try {
            movedCollider.setPosition(collider.getPosition().add(position));
            transform.setPosition(position);
        }
        finally {
            isUpdatingPosition = false;
        }
    }

    protected Vector2 getPosition() {
        return transform.getPosition();
    }

    public Rectangle getMovedCollider() {
        return movedCollider;
    }
}
