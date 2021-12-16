package Entity;

import ConnectionServer.ConnectionServer;
import Packet.Position.ClearEntityReturn;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import Utils.*;

public class EntityRegistryServer {
    //    private static final Map<Integer, Entity> entityIDMap = new HashMap<>();
    private static final Map<Integer, Entity> entityIDMap = new ConcurrentHashMap<>();


//    public static void addEntity(int id, Entity entity) {
//        entityIDMap.put(id, entity);
//    }

    public static void addEntity(Entity entity) {
        entityIDMap.put(getFreeID(), entity);
    }

    public static void unregisterEntity(int id){
        entityIDMap.remove(id);
    }

    public static void removeEntity(int entityID){
        ConnectionServer.sendTCPToAllPlayers(new ClearEntityReturn(entityID));
        EntityRegistryServer.unregisterEntity(entityID);
    }

    public static void removeEntity(Entity entity){
        removeEntity(getEntityID(entity));
    }

    public static void removeEntities(Collection<DeadBody> entities){
        for (Entity entity : entities){
            removeEntity(entity);
        }
    }


    public static void removeEntities(List<Integer> entityIDs){
        ConnectionServer.sendTCPToAllPlayers(new ClearEntityReturn(entityIDs));
        for (Integer ID: entityIDs) {
            EntityRegistryServer.unregisterEntity(ID);
        }
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
