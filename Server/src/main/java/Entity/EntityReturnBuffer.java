package Entity;

import Client.Client;
import ConnectionServer.ConnectionServer;
import Packet.Position.EntityState;
import Packet.Position.StateReturn;
import StartUpServer.AppServer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class EntityReturnBuffer {
    private final Map<Entity, List<Integer>> entityDestinationsReturnBuffer = new ConcurrentHashMap<>();


    public void sendGameState() {
        Map<Integer, Set<EntityState>> connectionEntityMap = new HashMap<>();

        for (Entity entity : entityDestinationsReturnBuffer.keySet()) {
            List<Integer> connectionIDs = entityDestinationsReturnBuffer.get(entity);
            for (Integer connectionID : connectionIDs) {
                if (connectionEntityMap.containsKey(connectionID)) {
                    connectionEntityMap.get(connectionID).add(entity.adaptToEntityState());
                } else {
                    Set<EntityState> entities = new HashSet<>();
                    entities.add(entity.adaptToEntityState());
                    connectionEntityMap.put(connectionID, entities);
                }
            }
        }

        for (Integer connectionID: connectionEntityMap.keySet()){
            ConnectionServer.sendUDP(new StateReturn(connectionEntityMap.get(connectionID)), connectionID);
        }


//        for (Entity entity : entityDestinationsReturnBuffer.keySet()) {
//            for (Integer connectionID : entityDestinationsReturnBuffer.get(entity)) {
//                ConnectionServer.sendUDP(new StateReturn(entity.adaptToEntityState()), connectionID);
//            }
//        }
        entityDestinationsReturnBuffer.clear();
    }


    private Set<EntityState> getEntityReturnStates() {
        return entityDestinationsReturnBuffer.keySet().stream().
                map(entity -> entity.adaptToEntityState()).
                collect(Collectors.toSet());
    }

    private List<Integer> getAllConnectionIDs() {
        return AppServer.currentGame.getClients().stream().
                map(Client::getConnectionID).
                collect(Collectors.toList());
    }

    public void putEntity(Entity entity) {
        entityDestinationsReturnBuffer.put(entity, getAllConnectionIDs());
    }

    public void putEntity(Entity entity, List<Integer> connectionIDs) {
        entityDestinationsReturnBuffer.put(entity, connectionIDs);
    }


}
