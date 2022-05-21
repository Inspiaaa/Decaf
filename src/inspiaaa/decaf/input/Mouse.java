package inspiaaa.decaf.input;

import inspiaaa.decaf.maths.Vector2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.HashSet;

public class Mouse {
    // TODO: Make Mouse less static

    private static class MouseListener implements java.awt.event.MouseListener, MouseWheelListener {
        private final HashMap<Integer, Boolean> buttonsPressed = new HashMap<Integer, Boolean>();
        private float scrollAmount;

        @Override
        public void mouseClicked(MouseEvent e) { }

        @Override
        public void mousePressed(MouseEvent e) {
            buttonsPressed.put(e.getButton(), true);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            buttonsPressed.put(e.getButton(), false);
        }

        @Override
        public void mouseEntered(MouseEvent e) { }

        @Override
        public void mouseExited(MouseEvent e) { }

        public HashMap<Integer, Boolean> getButtonsPressed() {
            return buttonsPressed;
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            scrollAmount -= e.getWheelRotation();
        }
    }

    private static final HashMap<Integer, Boolean> buttonsPressed = new HashMap<Integer, Boolean>();
    private static final HashSet<Integer> buttonsJustPressed = new HashSet<Integer>();
    private static final HashSet<Integer> buttonsJustReleased = new HashSet<Integer>();

    private static Vector2 position = new Vector2();
    private static float scrollAmount = 0;

    private static final MouseListener listener = new MouseListener();
    private static JComponent gameContainer;

    public static void setup(JComponent gameContainer) {
        Mouse.gameContainer = gameContainer;
        gameContainer.addMouseListener(listener);
        gameContainer.addMouseWheelListener(listener);
    }

    private static void setButtonState(int button, boolean isDownNow) {
        if (!buttonsPressed.containsKey(button)) {
            buttonsPressed.put(button, false);
        }

        boolean isCurrentlyDown = buttonsPressed.get(button);
        if (isCurrentlyDown && !isDownNow) {
            buttonsJustReleased.add(button);
        }
        else if (!isCurrentlyDown && isDownNow) {
            buttonsJustPressed.add(button);
        }
        buttonsPressed.put(button, isDownNow);
    }

    // Queries the current state of the mouse.
    public static void update() {
        buttonsJustPressed.clear();
        buttonsJustReleased.clear();

        // Because the game thread (which reads the keys) is not the same as the UI thread (which
        // writes to the keys), the listener must be locked while iterating over the keys.
        synchronized (listener) {
            scrollAmount = listener.scrollAmount;
            listener.scrollAmount = 0;

            HashMap<Integer, Boolean> newButtonsPressed = listener.getButtonsPressed();

            for (Integer button : newButtonsPressed.keySet()) {
                setButtonState(button, newButtonsPressed.get(button));
            }
        }

        if (gameContainer != null) {
            Point point = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(point, gameContainer);
            position = new Vector2(point.getLocation().x, point.getLocation().y);
        }
    }

    public static boolean isButtonDown(int button) {
        if (! buttonsPressed.containsKey(button)) {
            return false;
        }
        return buttonsPressed.get(button);
    }

    public static boolean isButtonJustDown(int button) {
        return buttonsJustPressed.contains(button);
    }

    public static boolean istButtonJustUp(int button) {
        return buttonsJustReleased.contains(button);
    }

    public static float getScrollAmount() {
        return scrollAmount;
    }

    public static Vector2 getPosition() {
        return position.copy();
    }
}
