package game.main;

import game.Camera;
import game.IScene;
import game.Scene;
import game.Time;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    private Thread gameThread;
    private boolean isRunning;

    private IScene activeScene;

    private float maxFps = 60;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 500));
        setBackground(Color.WHITE);
        setDoubleBuffered(true);
    }

    public void startGame(IScene scene) {
        activeScene = scene;
        scene.start(getWidth(), getHeight());
        isRunning = true;


        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        Time timeManager = Time.getInstance();
        int width = getWidth();
        int height = getHeight();

        while (isRunning) {
            int newWidth = getWidth();
            int newHeight = getHeight();

            // Although using a Listener would be more efficient, this polling based
            // solution for checking whether the screen has resized saves any
            // synchronisation hassles between the UI thread and the game thread.
            if (newWidth != width || newHeight != height) {
                activeScene.onResize(newWidth, newHeight);
                width = newWidth;
                height = newHeight;
            }

            timeManager.update();

            try {
                repaint();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            timeManager.capFps(maxFps);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        activeScene.update();
        g.drawImage(activeScene.getScreen(), 0, 0, null);
    }
}
