package inspiaaa.decaf;

import inspiaaa.decaf.gameobject.Component;
import inspiaaa.decaf.maths.Vector2;

import java.util.ArrayList;

public class Transform extends Component {
    private Vector2 position;
    private Vector2 scale;
    private float rotation;

    // Lazy initialised
    private ArrayList<IPositionListener> positionListeners;

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
        if (this.position.equals(position)) {
            return;
        }
        this.position = position;
        informPositionListeners();
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

    private void informPositionListeners() {
        if (positionListeners == null) {
            return;
        }

        for (IPositionListener listener : positionListeners) {
            listener.onPositionChange(position);
        }
    }

    public void addPositionListener(IPositionListener listener) {
        if (positionListeners == null) {
            positionListeners = new ArrayList<IPositionListener>();
        }
        positionListeners.add(listener);
    }

    public void removePositionListener(IPositionListener listener) {
        if (positionListeners != null) {
            positionListeners.remove(listener);
        }
    }
}
