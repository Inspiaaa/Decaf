package game.main;

import game.Scene;
import game.core.GameObject;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
	    System.out.println("Hello world");

	    Scene scene = new Scene();
	    GameObject go = new GameObject(scene);
	    // go.addComponent(new SpriteRenderer());
	    // System.out.println(go.getComponent(SpriteRenderer.class));

		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.setTitle("Game");

		GamePanel gamePanel = new GamePanel();
		window.add(gamePanel);
		window.pack();

		// Center the window
		window.setLocationRelativeTo(null);
		// Show the window
		window.setVisible(true);

		gamePanel.startGame(scene);
    }
}
