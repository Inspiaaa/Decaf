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
    private int layerMask;

    // Collider that has been moved in world space
    private Rectangle movedCollider;

    private Vector2 lastPosition;
    private boolean isUpdatingPosition;

    public RectCollider(Rectangle collider) {
        this.collider = collider;
        this.movedCollider = collider.copy();
        this.isUpdatingPosition = false;
        this.layerMask = LayerMask.DEFAULT;
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
        collisionEngine.removeEntity(this);
    }

    @Override
    public void onPositionChange(Vector2 newPos) {
        if (isUpdatingPosition) return;

        // TODO: Call teleportTo() instead
        teleportTo(newPos);
    }

    public void teleportTo(Vector2 pos) {
        collisionEngine.teleportEntity(this, pos);
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
