package inspiaaa.decaf.collision;

import inspiaaa.decaf.maths.Rectangle;
import inspiaaa.decaf.maths.Vector2;
import inspiaaa.decaf.maths.Vector2Int;

import java.util.HashMap;

public class CollisionEngine {
    private final HashMap<Vector2Int, Chunk> chunksByPos;
    private final float chunkSize;

    public CollisionEngine(float chunkSize) {
        this.chunksByPos = new HashMap<Vector2Int, Chunk>();
        this.chunkSize = chunkSize;
    }

    private Vector2Int pointToChunkPos(Vector2 point) {
        return pointToChunkPos(point.x, point.y);
    }

    private Vector2Int pointToChunkPos(float x, float y) {
        return new Vector2Int((int)Math.floor(x / chunkSize), (int)Math.floor(y / chunkSize));
    }

    private int xToChunkX(float x) {
        return (int)Math.floor(x / chunkSize);
    }

    private int yToChunkY(float y) {
        return (int)Math.floor(y / chunkSize);
    }

    private Vector2Int[] getChunksUnderRectangle(Rectangle rect) {
        int minChunkX = xToChunkX(rect.getLeft());
        int minChunkY = yToChunkY(rect.getBottom());
        int maxChunkX = xToChunkX(rect.getRight());
        int maxChunkY = yToChunkY(rect.getTop());

        int chunkCount = (maxChunkX - minChunkX + 1) * (maxChunkY - minChunkY + 1);
        Vector2Int[] chunks = new Vector2Int[chunkCount];

        int idx = 0;
        for (int x = minChunkX; x <= maxChunkX; x ++) {
            for (int y = minChunkY; y <= maxChunkY; y ++) {
                chunks[idx] = new Vector2Int(x, y);
                idx ++;
            }
        }

        return chunks;
    }

    // TODO: Idea: Make these methods independent of RectCollider (but keep an ease of use overload
    // for RectCollider)

    // TODO: Respect LayerMask

    private void addEntity(RectCollider entity) {
        for (Vector2Int chunkPos : getChunksUnderRectangle(entity.getMovedCollider())) {
            Chunk chunk = chunksByPos.get(chunkPos);
            chunk.addEntity(entity);
        }
    }

    private void removeEntity(RectCollider entity) {
        for (Vector2Int chunkPos : getChunksUnderRectangle(entity.getMovedCollider())) {
            Chunk chunk = chunksByPos.get(chunkPos);
            chunk.removeEntity(entity);
        }
    }

    private void moveEntity(RectCollider entity, Vector2 targetPos) {
        removeEntity(entity);
        entity.setPosition(targetPos);
        addEntity(entity);
    }

    private void moveAndCollideEntity(RectCollider entity, Vector2 targetPos) {
        removeEntity(entity);

        Rectangle originalCollider = entity.getMovedCollider();
        Rectangle colliderInTargetPos = originalCollider.copy();
        colliderInTargetPos.setPosition(targetPos);

        Vector2 deltaPos = targetPos.sub(originalCollider.getPosition());

        for (Vector2Int chunkPos : getChunksUnderRectangle(colliderInTargetPos)) {
            Chunk chunk = chunksByPos.get(chunkPos);
            deltaPos = chunk.moveAndCollide(colliderInTargetPos, deltaPos);
        }

        entity.setPosition(entity.getPosition().add(deltaPos));
        addEntity(entity);
    }

    public void detectCollisions(Rectangle rect, RectCollider[] entities) {
        // TODO
    }
}
