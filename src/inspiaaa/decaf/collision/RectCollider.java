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
    private boolean shouldCollideWithOthers;
    // TODO: Maybe add: should others collide with self?

    // Collider that has been moved in world space
    private Rectangle movedCollider;

    private Vector2 lastPosition;
    private boolean isUpdatingPosition;

    public RectCollider(Rectangle collider) {
        this(collider, LayerMask.DEFAULT);
    }

    public RectCollider(Rectangle collider, int layerMask) {
        this(collider, layerMask, true);
    }

    public RectCollider(Rectangle collider, int layerMask, boolean shouldCollideWithOthers) {
        this.collider = collider;
        this.movedCollider = collider.copy();
        this.isUpdatingPosition = false;
        this.layerMask = layerMask;
        this.shouldCollideWithOthers = shouldCollideWithOthers;
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

        teleportTo(newPos);
    }

    public void teleportTo(Vector2 pos) {
        if (shouldCollideWithOthers) {
            collisionEngine.teleportAndCollideEntity(this, pos);
        }
        else {
            collisionEngine.teleportEntity(this, pos);
        }
    }

    public void moveTo(Vector2 pos) {
        if (shouldCollideWithOthers) {
            collisionEngine.moveAndCollideEntity(this, pos);
        }
        else {
            collisionEngine.teleportEntity(this, pos);
        }
    }

    public void moveBy(Vector2 deltaPos) {
        Vector2 targetPos = transform.getPosition().add(deltaPos);
        if (shouldCollideWithOthers) {
            collisionEngine.moveAndCollideEntity(this, targetPos);
        }
        else {
            collisionEngine.teleportEntity(this, targetPos);
        }
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

    public int getLayerMask() {
        return layerMask;
    }
}
