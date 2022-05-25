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

    private void addEntity(RectCollider collider) {
        for (Vector2Int chunkPos : getChunksUnderRectangle(collider.getMovedCollider())) {
            Chunk chunk = chunksByPos.get(chunkPos);
            chunk.addEntity(collider);
        }
    }

    private void removeEntity(RectCollider collider) {
        for (Vector2Int chunkPos : getChunksUnderRectangle(collider.getMovedCollider())) {
            Chunk chunk = chunksByPos.get(chunkPos);
            chunk.removeEntity(collider);
        }
    }

    private void moveEntity(RectCollider collider) {
        // Remove the entity from the previous chunks
        // Move the entity
        // Add the entity to the new chunks
    }

    private void moveAndCollideEntity(RectCollider collider) {
        // Remove the entity from the chunks

        // Move the entity and resolve collisions
        // Assume that its initial state was valid
        // For each other collider, try the movement and if it does collide, try to resolve the collision:
        // Create a new movement, that slides along the object (if it can) and then check this
        // new valid motion for every next collider

        // Add the entity to the chunks again
    }
}
