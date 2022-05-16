package game;

import game.core.Component;
import game.maths.Vector2;

public class Transform extends Component {
    private Vector2 position;
    private Vector2 scale;
    private float rotation;

    public Transform() {
        this(new Vector2(0, 0));
    }

    public Transform(Vector2 position) {
        this.position = position;
        this.scale = new Vector2(1, 1);
        this.rotation = 0;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public Vector2 getScale() {
        return scale;
    }

    public void setScale(Vector2 scale) {
        this.scale = scale;
    }
}
