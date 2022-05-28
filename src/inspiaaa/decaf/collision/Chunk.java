package inspiaaa.decaf.collision;

import inspiaaa.decaf.maths.Rectangle;
import inspiaaa.decaf.maths.Vector2;

import java.util.ArrayList;

public class Chunk {
    private final ArrayList<RectCollider> entities;

    public Chunk() {
        entities = new ArrayList<RectCollider>();
    }

    public ArrayList<RectCollider> getRawEntities() {
        return entities;
    }

    public void addEntity(RectCollider collider) {
        entities.add(collider);
    }

    public void removeEntity(RectCollider collider) {
        int index = entities.indexOf(collider);
        if (index == -1) {
            return;
        }

        // Instead of removing the entity directly, which would cause an array copy operation under
        // the hood, instead fill the "hole" that would be created by removing the entity with
        // the last entity. (Order is not important)
        int lastIndex = entities.size() - 1;
        RectCollider lastEntity = entities.get(lastIndex);
        entities.set(index, lastEntity);
        entities.remove(lastIndex);
    }

    // TODO: Respect LayerMask

    // Returns the valid deltaPos for moving the collider.
    public Vector2 moveAndCollide(Rectangle collider, Vector2 deltaPos) {
        Vector2 validDeltaPos = deltaPos.copy();

        Rectangle movedCollider = collider.copy();

        // This algorithm assumes that its initial state was valid

        for (RectCollider other : entities) {
            // Try to execute the movement. If it does collide, slide along the collider and then calculate
            // a new, valid motion from this collision, which is then used for every next collider.
            movedCollider.setPosition(collider.x, collider.y);
            movedCollider.moveAndCollide(validDeltaPos, other.getMovedCollider());
            validDeltaPos.x = movedCollider.x - collider.x;
            validDeltaPos.y = movedCollider.y - collider.y;
        }

        return validDeltaPos;
    }
}
