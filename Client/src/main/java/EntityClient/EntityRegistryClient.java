package EntityClient;

import java.util.HashMap;
import java.util.Map;

public class EntityRegistryClient {

    private static final Map<Integer, ChangingEntity> entityIDMap = new HashMap<>();

    public static void addEntity(int id, ChangingEntity entity){
        entityIDMap.put(id, entity);
    }

    public static ChangingEntity getChangingEntity(int id){
        return entityIDMap.get(id);
    }
}
