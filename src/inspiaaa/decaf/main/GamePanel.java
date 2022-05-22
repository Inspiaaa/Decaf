package inspiaaa.decaf.main;

import inspiaaa.decaf.Time;
import inspiaaa.decaf.input.Keyboard;
import inspiaaa.decaf.input.Mouse;
import inspiaaa.decaf.rendering.GraphicsHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GamePanel extends JPanel implements Runnable {
    private Thread gameThread;
    private boolean isRunning;

    private SceneManager sceneManager;
    private volatile BufferedImage backBuffer;
    private final Lock backBufferLock = new ReentrantLock();

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
        resizeBackBuffer();

        isRunning = true;

        gameThread = new Thread(this);
        gameThread.start();
    }

    private void resizeBackBuffer() {
        backBufferLock.lock();
        try {
            backBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        }
        finally {
            backBufferLock.unlock();
        }
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
                resizeBackBuffer();
                width = newWidth;
                height = newHeight;
            }

            timeManager.update();

            try {
                sceneManager.update();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // paintComponent() isn't actually called directly when repaint() is called.
            // Probably also runs in multiple different threads.
            backBufferLock.lock();
            try {
                backBuffer.getGraphics().drawImage(sceneManager.getScreen(), 0, 0, null);
            }
            finally {
                backBufferLock.unlock();
            }
            repaint();

            timeManager.capFps(maxFps);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (isRunning) {
            super.paintComponent(g);

            backBufferLock.lock();
            try {
                g.drawImage(backBuffer, 0, 0, null);
            }
            finally {
                backBufferLock.unlock();
            }
        }
    }
}
