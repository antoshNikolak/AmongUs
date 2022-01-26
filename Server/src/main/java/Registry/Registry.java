package Registry;

import Utils.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Registry<T> {

    protected final Map<Integer, T> IDMap = new ConcurrentHashMap<>();

    public void addEntity(T entity) {
        IDMap.put(getFreeID(), entity);
    }

//    public void unregisterEntity(int id) {
//        IDMap.remove(id);
//    }

    public void removeEntity(int id) {
        IDMap.remove(id);
//        unregisterEntity(entityID);//remove registration ID
        //removed fully if overriden

////        synchronized (AppServer.currentGame.getEntityReturnBuffer()) {
//            AppServer.currentGame.getEntityReturnBuffer().removeEntity(entity);//remove from buffer
//            unregisterEntity(entityID);//remove registration ID
////        }
//        ConnectionServer.sendTCPToAllPlayers(new ClearEntityReturn(entityID));//removes entity on the client side
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

//    public  void removeEntities(List<Integer> entityIDs){
//        ConnectionServer.sendTCPToAllPlayers(new ClearEntityReturn(entityIDs));
//        for (Integer ID: entityIDs) {
//            unregisterEntity(ID);
//        }
//    }


    public T getItem(int id) {
        return IDMap.get(id);
    }

    public synchronized Integer getItemID(T item) {
        return CollectionUtils.getKey(IDMap, item);
    }

    public int getFreeID() {
        int counter = 0;
        while (true) {
            if (!IDMap.containsKey(counter)) {
                return counter;
            }
            counter++;
        }
    }



}
