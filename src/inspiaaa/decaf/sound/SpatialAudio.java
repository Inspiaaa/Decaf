package inspiaaa.decaf.sound;

import inspiaaa.decaf.maths.Vector2;
import inspiaaa.decaf.rendering.Camera;

public class SpatialAudio {
    public static float getPanForWorldPos(Vector2 worldPos) {
        // Algorithm:
        // Assume that the game is playing on a 2d plain and that the camera is in 3d and therefore has
        // a relative distance in z direction to this plain.
        // The formula for the pan is: relative x / distance to camera (in 3d)

        Vector2 cameraPos = Camera.main().getPosition();
        float dx = worldPos.x - cameraPos.x;
        float dy = worldPos.y - cameraPos.y;
        float dz = Camera.main().getHeightsInUnits();

        float distanceToCamera = (float) Math.sqrt(dx*dx + dy*dy + dz*dz);
        return dx / distanceToCamera;
    }
}
