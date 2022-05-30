package examples.sound;

import inspiaaa.decaf.Time;
import inspiaaa.decaf.Transform;
import inspiaaa.decaf.gameobject.Component;
import inspiaaa.decaf.events.IUpdatable;

public class RotateComponent extends Component implements IUpdatable {
    private Transform transform;

    @Override
    public void onStart() {
        transform = (Transform)getComponent(Transform.class);
    }

    @Override
    public void onUpdate() {
        transform.setRotation(Time.time());
    }
}
