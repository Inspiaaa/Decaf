package examples.collisions;

import inspiaaa.decaf.Scene;
import inspiaaa.decaf.Transform;
import inspiaaa.decaf.collision.RectCollider;
import inspiaaa.decaf.gameobject.GameObject;
import inspiaaa.decaf.input.Keyboard;
import inspiaaa.decaf.input.Keys;
import inspiaaa.decaf.input.Mouse;
import inspiaaa.decaf.maths.Vector2;
import inspiaaa.decaf.rendering.Camera;
import inspiaaa.decaf.rendering.Sprite;
import inspiaaa.decaf.rendering.SpriteRenderer;

public class CollisionExampleScene extends Scene {
    @Override
    public void start(int width, int height) {
        super.start(width, height);

        Sprite sprite = new Sprite("./Player.png", 16, Sprite.ORIGIN_BOTTOM_CENTER);

        GameObject player = new GameObject();
        player.addComponent(new Transform());
        player.addComponent(new SpriteRenderer(sprite));
        player.addComponent(new PlayerController());
        player.addComponent(new RectCollider(sprite.getWorldBounds()));
        player.addComponent(new DrawCollider());

        GameObject other = new GameObject();
        other.addComponent(new Transform(new Vector2(1, 1)));
        other.addComponent(new SpriteRenderer(sprite));
        other.addComponent(new RectCollider(sprite.getWorldBounds()));
        other.addComponent(new DrawCollider());
    }

    @Override
    public void update() {
        if (Keyboard.isKeyJustDown(Keys.SPACE)) {
            System.out.println("Hello");
        }

//        Vector2 mousePos = Camera.main().screenToWorldPos(Mouse.getPosition());
//        RectCollider[] hits = new RectCollider[5];
//        int count = getCollisionEngine().detectCollisions(mousePos, hits);
//        System.out.println(count);

        super.update();
    }
}
