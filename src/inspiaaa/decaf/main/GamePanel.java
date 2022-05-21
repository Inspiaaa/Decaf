package inspiaaa.decaf.main;

import inspiaaa.decaf.Time;
import inspiaaa.decaf.input.Keyboard;
import inspiaaa.decaf.input.Mouse;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    private Thread gameThread;
    private boolean isRunning;

    private SceneManager sceneManager;

    private float maxFps = 60;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 500));
        setBackground(Color.WHITE);
        setDoubleBuffered(true);

        setFocusable(true);
        requestFocus();

        Mouse.setup(this);
        Keyboard.setup(this);
    }

    public void startGame() {
        sceneManager = SceneManager.getInstance();
        sceneManager.start(getWidth(), getHeight());
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
                sceneManager.onResize(newWidth, newHeight);
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
        if (isRunning) {
            super.paintComponent(g);
            sceneManager.update();
            g.drawImage(sceneManager.getScreen(), 0, 0, null);
        }
    }
}
