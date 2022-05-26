package inspiaaa.decaf.main;

import inspiaaa.decaf.IScene;

import java.awt.image.BufferedImage;

public class SceneManager {
    private static final SceneManager instance = new SceneManager();

    private IScene activeScene;
    private int screenWidth = 1;
    private int screenHeight = 1;

    private boolean hasStarted = false;

    public void start(int width, int height) {
        screenWidth = width;
        screenHeight = height;

        if (activeScene != null) {
            activeScene.start(width, height);
        }

        hasStarted = true;
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

        if (hasStarted) {
            newScene.start(screenWidth, screenHeight);
        }
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
