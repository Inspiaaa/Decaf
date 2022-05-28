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

    private Chunk ensureChunkExists(Vector2Int chunkPos) {
        Chunk chunk = chunksByPos.get(chunkPos);
        if (chunk == null) {
            chunk = new Chunk();
            chunksByPos.put(chunkPos, chunk);
        }
        return chunk;
    }

    public void addEntity(RectCollider entity) {
        for (Vector2Int chunkPos : getChunksUnderRectangle(entity.getMovedCollider())) {
            Chunk chunk = ensureChunkExists(chunkPos);
            chunk.addEntity(entity);
        }
    }

    public void removeEntity(RectCollider entity) {
        for (Vector2Int chunkPos : getChunksUnderRectangle(entity.getMovedCollider())) {
            Chunk chunk = chunksByPos.get(chunkPos);
            if (chunk != null) {
                chunk.removeEntity(entity);
            }
        }
    }

    public void moveEntity(RectCollider entity, Vector2 targetPos) {
        removeEntity(entity);
        entity.setPosition(targetPos);
        // TODO: Call push out of () on each other object in the chunk
        addEntity(entity);
    }

    public void moveAndCollideEntity(RectCollider entity, Vector2 targetPos) {
        removeEntity(entity);

        Vector2 startPos = entity.getPosition();
        Vector2 deltaPos = targetPos.sub(startPos);

        Rectangle originalCollider = entity.getMovedCollider();
        Rectangle colliderInTargetPos = originalCollider.copy();
        colliderInTargetPos.move(deltaPos);

        for (Vector2Int chunkPos : getChunksUnderRectangle(colliderInTargetPos)) {
            Chunk chunk = chunksByPos.get(chunkPos);
            if (chunk != null) {
                deltaPos = chunk.moveAndCollide(originalCollider, deltaPos);
            }
        }

        entity.setPosition(startPos.add(deltaPos));
        addEntity(entity);
    }

    public int detectCollisions(Vector2 pos, RectCollider[] entities) {
        Vector2Int chunkPos = pointToChunkPos(pos);
        Chunk chunk = chunksByPos.get(chunkPos);
        int count = 0;

        // TODO: Move this code to Chunk.java
        if (chunk != null) {
            for (RectCollider entity : chunk.getRawEntities()) {
                if (entity.getMovedCollider().contains(pos)) {
                    entities[count++] = entity;
                }
            }
        }

        return count;
    }

    public void detectCollisions(Rectangle rect, RectCollider[] entities) {
        // TODO
    }
}
