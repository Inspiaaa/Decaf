package examples.collisions;

import inspiaaa.decaf.Time;
import inspiaaa.decaf.Transform;
import inspiaaa.decaf.events.IUpdatable;
import inspiaaa.decaf.gameobject.Component;
import inspiaaa.decaf.input.Keyboard;
import inspiaaa.decaf.input.Keys;
import inspiaaa.decaf.maths.Vector2;

public class PlayerController extends Component implements IUpdatable {
    private Transform transform;

    @Override
    public void onStart() {
        transform = (Transform) getComponent(Transform.class);
    }

    @Override
    public void onUpdate() {
        Vector2 input = new Vector2();
        if (Keyboard.isKeyDown(Keys.W)) input.y -= 1;
        if (Keyboard.isKeyDown(Keys.A)) input.x -= 1;
        if (Keyboard.isKeyDown(Keys.S)) input.y += 1;
        if (Keyboard.isKeyDown(Keys.D)) input.x += 1;

        transform.setPosition(transform.getPosition().add(input.mul(Time.deltaTime() * 3)));
    }
}
