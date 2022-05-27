package inspiaaa.decaf.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

// Class that acts like a sparse set datatype (similar to a HashSet),
// but allows for efficient iteration and addition / removal of objects during iteration.
// O(1) add, remove
// O(n) iteration

// Objects can be added to the set during iteration, but objects that need to be removed
// are only removed after iteration.

public class ObjectPool<T> implements Iterable<T> {
    private final ArrayList<T> objects;
    private final HashMap<T, Integer> objectToIndex;
    private final HashSet<T> objectsToRemoveAfterIteration;

    private int lockCount;

    public ObjectPool() {
        objects = new ArrayList<T>();
        objectToIndex = new HashMap<T, Integer>();
        objectsToRemoveAfterIteration = new HashSet<T>();
    }

    public void add(T obj) {
        int index = objects.size();
        if (! objectToIndex.containsKey(obj)) {
            objectToIndex.put(obj, index);
            objects.add(obj);
        }
        objectsToRemoveAfterIteration.remove(obj);
    }

    private void actuallyRemove(T obj) {
        Integer possibleIndex = objectToIndex.get(obj);
        if (possibleIndex == null) {
            return;
        }

        // Instead of removing the item from the densely packed objects list and then shifting all
        // subsequent objects, fill the resulting "hole" with the last item.

        int index = possibleIndex;
        int lastIndex = objects.size() - 1;

        T lastObj = objects.get(lastIndex);
        objects.set(index, lastObj);
        objects.remove(lastIndex);

        objectToIndex.put(lastObj, index);
        objectToIndex.remove(obj);
    }

    public void remove(T obj) {
        if (isLocked()) {
            objectsToRemoveAfterIteration.add(obj);
        }
        else {
            actuallyRemove(obj);
        }
    }

    private void removeBufferedObjectsToRemove() {
        for (T obj : objectsToRemoveAfterIteration) {
            actuallyRemove(obj);
        }
        objectsToRemoveAfterIteration.clear();
    }

    public void lock() {
        lockCount ++;
    }

    public void unlock() {
        if (lockCount > 0) {
            lockCount --;
        }

        if (lockCount == 0) {
            removeBufferedObjectsToRemove();
        }
    }

    public void ensureIsUnlocked() {
        lockCount = 0;
    }

    public boolean isLocked() {
        return lockCount > 0;
    }

    public void clear() {
        objects.clear();
        objectToIndex.clear();
        objectsToRemoveAfterIteration.clear();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < objects.size();
            }

            @Override
            public T next() {
                return objects.get(index ++);
            }
        };
    }
}
