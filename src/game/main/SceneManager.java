package game.main;

import game.IScene;

import java.awt.image.BufferedImage;

public class SceneManager {
    private static final SceneManager instance = new SceneManager();

    private IScene activeScene;
    private int screenWidth = 1;
    private int screenHeight = 1;

    public void start(int width, int height) {
        screenWidth = width;
        screenHeight = height;

        if (activeScene != null) {
            activeScene.start(width, height);
        }
    }

    public void update() {
        if (activeScene != null) {
            try {
                activeScene.update();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setActiveScene(IScene newScene) {
        try {
            if (activeScene != null) {
                activeScene.destroy();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        activeScene = newScene;
        newScene.start(screenWidth, screenHeight);
    }

    public IScene getActiveScene() {
        return activeScene;
    }

    public void onResize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
        if (activeScene != null) {
            activeScene.onResize(width, height);
        }
    }

    public BufferedImage getScreen() {
        if (activeScene == null) {
            return null;
        }
        return activeScene.getScreen();
    }

    public static SceneManager getInstance() {
        return instance;
    }
}
