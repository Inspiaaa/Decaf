package game.core;

import game.Scene;

import java.util.ArrayList;

public class GameObject {
    private final ArrayList<Component> components;
    private final Scene scene;

    public GameObject(Scene scene) {
        this.scene = scene;
        this.components = new ArrayList<>();
    }

    public void addComponent(Component component) {
        component.setGameObject(this);
        component.onStart();

        components.add(component);
        scene.register(component);
    }

    public Component getComponent(Class<? extends Component> c) {
        for (Component component : components) {
            if (c.isInstance(component)) {
                return component;
            }
        }
        return null;
    }

    public boolean hasComponent(Class<? extends Component> c) {
        return getComponent(c) != null;
    }

    // Destroys and removes the component from the GameObject's list of components.
    public void removeComponent(Component component) {
        int index = -1;

        for (int i = 0; i < components.size(); i ++) {
            Component other = components.get(i);

            if (other == component) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            components.remove(index);
            destroyComponent(component);
        }
    }

    // Destroys a component, i.e. unregisters it from the event loop, ...
    private void destroyComponent(Component component) {
        component.onDestroy();
        scene.unregister(component);
    }

    // Destroys this GameObject and all of its components.
    public void destroy() {
        for (Component comp : components) {
            destroyComponent(comp);
        }
        components.clear();
    }

    public Scene getScene() {
        return scene;
    }
}
