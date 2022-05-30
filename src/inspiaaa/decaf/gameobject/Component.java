package inspiaaa.decaf.gameobject;

import inspiaaa.decaf.Scene;
import inspiaaa.decaf.events.IDestroyable;

public class Component implements IDestroyable {
    protected GameObject gameObject;

    public void onStart() {

    }

    @Override
    public void onDestroy() {

    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    // Convenience methods that wrap the GameObject's methods

    public void addComponent(Component component) {
        gameObject.addComponent(component);
    }

    public void removeComponent(Component component) {
        gameObject.removeComponent(component);
    }

    public Component getComponent(Class<? extends Component> c) {
        return gameObject.getComponent(c);
    }

    public boolean hasComponent(Class<? extends Component> c) {
        return gameObject.hasComponent(c);
    }

    public boolean hasTag(String tag) {
        return gameObject.hasTag(tag);
    }

    public void addTag(String tag) {
        gameObject.addTag(tag);
    }

    public void removeTag(String tag) {
        gameObject.removeTag(tag);
    }

    public Scene getScene() {
        return gameObject.getScene();
    }
}
