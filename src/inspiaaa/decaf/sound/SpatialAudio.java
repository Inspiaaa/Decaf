package inspiaaa.decaf.sound;

import inspiaaa.decaf.maths.Vector2;
import inspiaaa.decaf.rendering.Camera;

public class SpatialAudio {
    public static float getPanForWorldPos(Vector2 worldPos) {
        float cameraZ = Camera.main().getHeightsInUnits();
        return getPanForWorldPos(worldPos, cameraZ);
    }

    public static float getPanForWorldPos(Vector2 worldPos, float cameraZ) {
        Vector2 cameraPos = Camera.main().getPosition();
        return getVolumeForWorldPos(worldPos, cameraZ, cameraPos);
    }

    public static float getPanForWorldPos(Vector2 worldPos, float cameraZ, Vector2 cameraPos) {
        // Algorithm:
        // Assume that the game is playing on a 2d plain and that the camera is in 3d and therefore has
        // a relative distance in z direction to this plain.
        // The formula for the pan is: relative x / distance to camera (in 3d)

        // The greater the cameraZ is, the less 3d the audio will sound

        float dx = worldPos.x - cameraPos.x;
        float dy = worldPos.y - cameraPos.y;
        float dz = cameraZ;

        float distanceToCamera = (float) Math.sqrt(dx*dx + dy*dy + dz*dz);
        return dx / distanceToCamera;
    }

    public static float getVolumeForWorldPos(Vector2 worldPos) {
        return getVolumeForWorldPos(worldPos, 0.2f);
    }

    public static float getVolumeForWorldPos(Vector2 worldPos, float volumeDropOffSpeed) {
        Vector2 cameraPos = Camera.main().getPosition();
        return getVolumeForWorldPos(worldPos, volumeDropOffSpeed, cameraPos);
    }

    public static float getVolumeForWorldPos(Vector2 worldPos, float volumeDropOffSpeed, Vector2 cameraPos) {
        float dx = worldPos.x - cameraPos.x;
        float dy = worldPos.y - cameraPos.y;

        float distanceToCamera = (float) Math.sqrt(dx*dx + dy*dy);
        return (float) 1 / (1 + distanceToCamera * volumeDropOffSpeed);
    }
}
