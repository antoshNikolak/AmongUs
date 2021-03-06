package Entity;

import ConnectionServer.ConnectionServer;
import Packet.EntityState.ExistingEntityState;
import Packet.EntityState.NewEntityState;
import Packet.EntityState.NewLineState;
import Packet.Position.StateReturn;
import Registry.RegistryHandler;
import StartUpServer.AppServer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class EntityReturnBuffer {
    private final Map<Entity, List<Integer>> entityDestinationsReturnBuffer = new ConcurrentHashMap<>();
    //maps entity and connection ID's of relevant clients to send the entity state to

    public void sendGameState() {
        Map<Integer, Set<ExistingEntityState>> connectionEntityMap = new HashMap<>();//map connection ID and set of entity states to send
        for (Entity entity : entityDestinationsReturnBuffer.keySet()) {//for each entity in buffer
            List<Integer> connectionIDs = entityDestinationsReturnBuffer.get(entity);
            for (Integer connectionID : connectionIDs) {//for each relevant connection ID
                if (connectionEntityMap.containsKey(connectionID)) {//check if an entry from this connection ID has been made into connectionEntityMap
                    connectionEntityMap.get(connectionID).add(entity.adaptToEntityState());//add entity state to existing entry
                } else {
                    Set<ExistingEntityState> entities = new HashSet<>();
                    entities.add(entity.adaptToEntityState());
                    connectionEntityMap.put(connectionID, entities);//add a new entry
                }
            }
        }

        for (Integer connectionID : connectionEntityMap.keySet()) {//send out game states to each relevant client
            ConnectionServer.sendUDP(new StateReturn(connectionEntityMap.get(connectionID)), connectionID);
        }
        entityDestinationsReturnBuffer.clear();//clear the buffer
    }

    public static Set<NewEntityState> adaptCollectionToNewEntityStates(Collection<? extends Entity> entities) {
        return entities.stream().
                map(entity -> entity.adaptToNewAnimatedEntityState(true)).
                collect(Collectors.toSet());
    }

    private List<Integer> getAllConnectionIDs() {
        return AppServer.currentGame.getPlayers().stream().
                map(Player::getConnectionID).
                collect(Collectors.toList());
    }

    //overloaded method to add entity to buffer, entity will be broadcast to all client
    public synchronized void putEntity(Entity entity) {
        if (RegistryHandler.entityRegistryServer.getItemID(entity) == null) return;
        entityDestinationsReturnBuffer.put(entity, getAllConnectionIDs());
    }

    //overloaded method to add entity to buffer, entity will be broadcast to clients with connection ID's input
    public synchronized void putEntity(Entity entity, List<Integer> connectionIDs) {
        if (RegistryHandler.entityRegistryServer.getItemID(entity) == null) return;
        entityDestinationsReturnBuffer.put(entity, connectionIDs);
    }

    //overloaded method to add entity to buffer, entity will be broadcast to clients with connection ID's in var args
    public synchronized void putEntity(Entity entity, Integer... connectionIDs) {
        if (RegistryHandler.entityRegistryServer.getItemID(entity) == null) return;
        entityDestinationsReturnBuffer.put(entity, Arrays.asList(connectionIDs));
    }

    //removes entity from buffer
    public synchronized void removeEntity(Entity entity) {
        this.entityDestinationsReturnBuffer.remove(entity);
    }

}
