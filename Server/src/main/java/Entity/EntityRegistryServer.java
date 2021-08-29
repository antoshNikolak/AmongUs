package Entity;

import Utils.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

public class EntityRegistryServer {
    //    private static final Map<Integer, Entity> entityIDMap = new HashMap<>();
    private static final Map<Integer, Entity> entityIDMap = new HashMap<>();


//    public static void addEntity(int id, Entity entity) {
//        entityIDMap.put(id, entity);
//    }

    public static void addEntity(Entity entity) {
        int id = getFreeID();
//        if (entity instanceof  Tile) {
//            System.out.println("Tile id: "+ id);
//        }else {
//            System.out.println("no tile id: "+ id);
//        }
        entityIDMap.put(id, entity);
    }

    public static Entity getEntity(int id) {
        return entityIDMap.get(id);
    }

    public static Integer getEntityID(Entity entity) {
        return CollectionUtils.getKey(entityIDMap, entity);
    }

    public static int getFreeID() {
        int counter = 0;
        while (true) {
            if (!entityIDMap.containsKey(counter)) {
                return counter;
            }
            counter++;
        }
    }


}
