package Registry;

import Utils.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Registry<T> {

    protected final Map<Integer, T> IDMap = new ConcurrentHashMap<>();//maps ID to item


    public void addEntity(T entity) {
        IDMap.put(getFreeID(), entity);
    }

    public void removeEntity(int id) {
        IDMap.remove(id);
    }


    public void removeEntity(T item) {
        removeEntity(getItemID(item));
    }

    public void removeEntities(Collection<? extends T> items) {
        for (T item : items) {
            removeEntity(item);
        }
    }

    public void removeAllItems() {
        IDMap.values().forEach(this::removeEntity);
    }

    public T getItem(int id) {
        return IDMap.get(id);
    }

    public synchronized Integer getItemID(T item) {
        return CollectionUtils.getKey(IDMap, item);
    }

    public int getFreeID() {
        int counter = 0;
        while (true) {//linear search to find free ID key.
            if (!IDMap.containsKey(counter)) {
                return counter;
            }
            counter++;
        }
    }



}
