package game.main;

import javax.swing.*;

public class GameApplication {
    private JFrame window;
    private GamePanel gamePanel;

    public GameApplication() {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle("Game");

        gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();

        // Center the window
        window.setLocationRelativeTo(null);
    }

    public void start() {
        // Show the window
        window.setVisible(true);
        gamePanel.startGame();
    }
}
