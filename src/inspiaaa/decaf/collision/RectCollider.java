package inspiaaa.decaf.collision;

import inspiaaa.decaf.IPositionListener;
import inspiaaa.decaf.Transform;
import inspiaaa.decaf.gameobject.Component;
import inspiaaa.decaf.maths.Rectangle;
import inspiaaa.decaf.maths.Vector2;

public class RectCollider extends Component implements IPositionListener {
    private CollisionEngine collisionEngine;
    private Transform transform;

    // Base collider
    private Rectangle collider;
    private Vector2 lastPosition;
    // Collider that has been moved in world space
    private Rectangle movedCollider;

    private int layerMask;
    private boolean isUpdatingPosition;

    // FIXME: Problem: the target position is the same as getPosition()!

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

        lastPosition = transform.getPosition();
        updateMovedCollider();
        collisionEngine.addEntity(this);
    }

    @Override
    public void onDestroy() {
        transform.removePositionListener(this);
    }

    @Override
    public void onPositionChange(Vector2 newPos) {
        if (isUpdatingPosition) return;

        // TODO: Call teleportTo() instead
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
        if (isUpdatingPosition) {
            return;
        }

        isUpdatingPosition = true;
        try {
            transform.setPosition(position);
            this.lastPosition = position;
            updateMovedCollider();
        }
        finally {
            isUpdatingPosition = false;
        }
    }

    private void updateMovedCollider() {
        movedCollider.setPosition(collider.getPosition().add(lastPosition));
    }

    protected Vector2 getPosition() {
         return lastPosition;
    }

    public Rectangle getMovedCollider() {
        return movedCollider;
    }
}
