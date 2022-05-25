package inspiaaa.decaf.collision;

import java.util.ArrayList;

public class Chunk {
    private final ArrayList<RectCollider> entities;

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

    public Chunk() {
        entities = new ArrayList<RectCollider>();
    }
}
