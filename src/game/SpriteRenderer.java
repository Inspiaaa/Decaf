package game;

import game.core.Component;
import game.events.IDrawable;
import game.events.IUpdatable;
import game.maths.Vector2;
import game.rendering.Sprite;

import java.awt.*;

public class SpriteRenderer extends Component implements IUpdatable, IDrawable {
    private Sprite sprite;

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
    }

    public void onStart() {

    }

    public void onUpdate() {

    }

    public void onDraw() {
        // Camera.getMain().getDrawGraphics().drawImage(texture);
        Graphics2D g = Camera.main().getDrawGraphics();
        g.setColor(new Color(255, 0, 0));
        // g.drawRect(5, 5, 20, 10);
        Vector2 pos = Camera.main().worldToDrawPos(new Vector2(1, (int)Time.time() % 5));
        int size = (int)Camera.main().worldToDrawLength(1);
        g.drawRect((int)pos.x, (int)pos.y, size, size);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
