package game;

import java.awt.image.BufferedImage;

public interface IScene {
    public void start(int width, int height);
    public void update();
    public void destroy();
    // Always called after update()
    public BufferedImage getScreen();
    public void onResize(int width, int height);
}
