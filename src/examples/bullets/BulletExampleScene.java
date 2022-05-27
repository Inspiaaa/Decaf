package examples.bullets;

import inspiaaa.decaf.Scene;
import inspiaaa.decaf.gameobject.GameObject;

public class BulletExampleScene extends Scene {
    @Override
    public void start(int width, int height) {
        super.start(width, height);
        GameObject go = new GameObject();
        go.addComponent(new SpawnerComponent());
    }

    @Override
    public void update() {
        super.update();
    }
}
