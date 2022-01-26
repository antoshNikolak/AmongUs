package Registry;

import ConnectionServer.ConnectionServer;
import Entity.Entity;
import Packet.Position.ClearEntityReturn;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import StartUpServer.AppServer;
import Utils.*;
import Entity.DeadBody;

public class EntityRegistryServer extends Registry<Entity> {
    //    private static final Map<Integer, Entity> entityIDMap = new HashMap<>();
//    private static final Map<Integer, Entity> entityIDMap = new ConcurrentHashMap<>();


//    public static void addEntity(int id, Entity entity) {
//        entityIDMap.put(id, entity);
//    }

//    public static void addEntity(Entity entity) {
//        entityIDMap.put(getFreeID(), entity);
//    }

//    @Override
//    public void unregisterEntity(int id) {
//        Entity entity = IDMap.get(id);
//        synchronized (AppServer.currentGame.getEntityReturnBuffer()) {
//            AppServer.currentGame.getEntityReturnBuffer().removeEntity(entity);//remove from buffer
//            ConnectionServer.sendTCPToAllPlayers(new ClearEntityReturn(id));//removes entity on the client side
//            IDMap.remove(id);
//        }
////        ConnectionServer.sendTCPToAllPlayers(new ClearEntityReturn(entityID));//removes entity on the client side
////        synchronized (AppServer.currentGame.getEntityReturnBuffer()) {
////            IDMap.remove(id);
////        }
//    }

    @Override
    public void removeEntity(int entityID) {
        Entity entity = IDMap.get(entityID);
        synchronized (AppServer.currentGame.getEntityReturnBuffer()) {
            AppServer.currentGame.getEntityReturnBuffer().removeEntity(entity);//remove from buffer
            ConnectionServer.sendTCPToAllPlayers(new ClearEntityReturn(entityID));//removes entity on the client side
            IDMap.remove(entityID);
        }
//        Entity entity = IDMap.get(entityID);
//        synchronized (AppServer.currentGame.getEntityReturnBuffer()) {
//            AppServer.currentGame.getEntityReturnBuffer().removeEntity(entity);//remove from buffer
//            unregisterEntity(entityID);//remove registration ID
//        }
//        ConnectionServer.sendTCPToAllPlayers(new ClearEntityReturn(entityID));//removes entity on the client side
    }


}
