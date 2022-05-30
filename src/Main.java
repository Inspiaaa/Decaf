import examples.bullets.BulletExampleScene;
import examples.stress.StressTestScene;
import inspiaaa.decaf.main.GameApplication;
import inspiaaa.decaf.main.SceneManager;

public class Main {
    public static void main(String[] args) {
	    System.out.println("Hello world");

		GameApplication app = new GameApplication();
		SceneManager.getInstance().setActiveScene(new StressTestScene());
		app.start();
    }
}
