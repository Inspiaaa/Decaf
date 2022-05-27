package examples.bullets;

import examples.collisions.PlayerController;
import inspiaaa.decaf.Transform;
import inspiaaa.decaf.collision.RectCollider;
import inspiaaa.decaf.events.IUpdatable;
import inspiaaa.decaf.gameobject.Component;
import inspiaaa.decaf.gameobject.GameObject;
import inspiaaa.decaf.input.Mouse;
import inspiaaa.decaf.input.MouseButtons;
import inspiaaa.decaf.rendering.Camera;
import inspiaaa.decaf.rendering.Sprite;
import inspiaaa.decaf.rendering.SpriteRenderer;

import java.util.ArrayList;

public class SpawnerComponent extends Component implements IUpdatable {
    private Sprite sprite = new Sprite("./Player.png", 16, Sprite.ORIGIN_BOTTOM_CENTER);

    private ArrayList<GameObject> objects = new ArrayList<GameObject>();

    @Override
    public void onUpdate() {
        if (Mouse.isButtonJustDown(MouseButtons.LEFT)) {
            GameObject go = new GameObject();
            go.addComponent(new Transform(Camera.main().screenToWorldPos(Mouse.getPosition())));
            go.addComponent(new SpriteRenderer(sprite));
            go.addComponent(new PlayerController());
            go.addComponent(new RectCollider(sprite.getWorldBounds()));
            objects.add(go);
        }

        if (Mouse.isButtonJustDown(MouseButtons.RIGHT)) {
            if (objects.size() > 0) {
                int index = 0;
                GameObject go = objects.get(index);
                objects.remove(index);
                go.destroy();
            }
        }
    }
}
