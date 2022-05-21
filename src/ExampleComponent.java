import inspiaaa.decaf.Time;
import inspiaaa.decaf.Transform;
import inspiaaa.decaf.core.Component;
import inspiaaa.decaf.events.IUpdatable;

public class ExampleComponent extends Component implements IUpdatable {
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
