package inspiaaa.decaf.input;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;

public class Keyboard {

    // Separating the Keyboard and the Listener into two separate classes
    // has two advantages:
    // 1. The API of Keyboard is simpler
    // 2. The KeyboardListener (UI thread) can be independent from the game thread and its update
    // behaviour. This also means that the state of every key (whether it is pressed or not) is constant
    // during each frame.

    private static class KeyboardListener implements KeyListener {
        private final HashMap<Integer, Boolean> keysPressed = new HashMap<Integer, Boolean>();

        @Override
        public synchronized void keyTyped(KeyEvent e) {
            // Called when a key is pressed that can be converted to a unicode character.
            // E.g. shift + a => "A" would be registered.
            // This behaviour is not necessary for games.
        }

        @Override
        public synchronized void keyPressed(KeyEvent e) {
            // TODO: Does this method need to be synchronized?
            keysPressed.put(e.getKeyCode(), true);
        }

        @Override
        public synchronized void keyReleased(KeyEvent e) {
            keysPressed.put(e.getKeyCode(), false);
        }

        public HashMap<Integer, Boolean> getKeysPressed() {
            return keysPressed;
        }
    }

    private static final HashMap<Integer, Boolean> keysPressed = new HashMap<Integer, Boolean>();
    private static final HashSet<Integer> keysJustPressed = new HashSet<Integer>();
    private static final HashSet<Integer> keysJustReleased = new HashSet<Integer>();

    private static final KeyboardListener listener = new KeyboardListener();

    public static void setup(JComponent gameContainer) {
        gameContainer.addKeyListener(listener);
    }

    private static void setKeyState(int key, boolean isDownNow) {
        if (!keysPressed.containsKey(key)) {
            keysPressed.put(key, false);
        }

        boolean isCurrentlyDown = keysPressed.get(key);
        if (isCurrentlyDown && !isDownNow) {
            keysJustReleased.add(key);
        }
        else if (!isCurrentlyDown && isDownNow) {
            keysJustPressed.add(key);
        }
        keysPressed.put(key, isDownNow);
    }

    // Queries the current state of the keyboard.
    public static void update() {
        keysJustPressed.clear();
        keysJustReleased.clear();

        // Because the game thread (which reads the keys) is not the same as the UI thread (which
        // writes to the keys), the listener must be locked while iterating over the keys.
        synchronized (listener) {
            HashMap<Integer, Boolean> newKeysPressed = listener.getKeysPressed();

            // Possible optimisation: Keep a HashSet of keys that have been modified
            // and only query (iterate) this set instead of every key
            for (Integer key : newKeysPressed.keySet()) {
                setKeyState(key, newKeysPressed.get(key));
            }
        }
    }

    public static boolean isKeyDown(int key) {
        if (! keysPressed.containsKey(key)) {
            return false;
        }
        return keysPressed.get(key);
    }

    public static boolean isKeyJustDown(int key) {
        return keysJustPressed.contains(key);
    }

    public static boolean isKeyJustUp(int key) {
        return keysJustReleased.contains(key);
    }
}
