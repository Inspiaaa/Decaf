import examples.sound.ExampleScene;
import inspiaaa.decaf.main.GameApplication;
import inspiaaa.decaf.main.SceneManager;

public class Main {
    public static void main(String[] args) {
	    System.out.println("Hello world");

		GameApplication app = new GameApplication();
		SceneManager.getInstance().setActiveScene(new ExampleScene());
		app.start();
    }
}
