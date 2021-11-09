package EntityClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityRegistryClient {

    private static final Map<Integer, Entity> entityIDMap = new ConcurrentHashMap<>();

    public static void addEntity(int id, Entity entity){
        entityIDMap.put(id, entity);
    }

    public static void removeEntity(int id){
        entityIDMap.remove(id);
    }

    public static Entity getEntity(int id){
        return entityIDMap.get(id);
    }
}
