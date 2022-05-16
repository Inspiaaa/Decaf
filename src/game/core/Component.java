package game.core;

public class Component {
    protected GameObject gameObject;

    public void onStart() {

    }

    public void onDestroy() {

    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    // Convenience methods that wrap the GameObject's methods

    protected void addComponent(Component component) {
        gameObject.addComponent(component);
    }

    protected void removeComponent(Component component) {
        gameObject.removeComponent(component);
    }

    protected Component getComponent(Class<? extends Component> c) {
        return gameObject.getComponent(c);
    }

    protected boolean hasComponent(Class<? extends Component> c) {
        return gameObject.hasComponent(c);
    }
}
