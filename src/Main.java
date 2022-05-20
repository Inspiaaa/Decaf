import game.Scene;
import game.main.GameApplication;
import game.main.SceneManager;

public class Main {
    public static void main(String[] args) {
	    System.out.println("Hello world");

		GameApplication app = new GameApplication();
		SceneManager.getInstance().setActiveScene(new Scene());
		app.start();
    }
}
