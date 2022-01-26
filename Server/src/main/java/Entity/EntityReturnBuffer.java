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
    //    private final Map<Entity, List<Integer>> entityDestinationsReturnBuffer = new HashMap<>();


    public void sendGameState() {
        Map<Integer, Set<ExistingEntityState>> connectionEntityMap = new HashMap<>();
        for (Entity entity : entityDestinationsReturnBuffer.keySet()) {
            List<Integer> connectionIDs = entityDestinationsReturnBuffer.get(entity);
            for (Integer connectionID : connectionIDs) {
                if (connectionEntityMap.containsKey(connectionID)) {
                    connectionEntityMap.get(connectionID).add(entity.adaptToEntityState());
                } else {
                    Set<ExistingEntityState> entities = new HashSet<>();
                    entities.add(entity.adaptToEntityState());
                    connectionEntityMap.put(connectionID, entities);
                }
            }
        }
        //why do i get a warning when synchronizing on a local var, even though its stored in a global list?
        for (Integer connectionID : connectionEntityMap.keySet()) {
            ConnectionServer.sendUDP(new StateReturn(connectionEntityMap.get(connectionID)), connectionID);
        }
        entityDestinationsReturnBuffer.clear();
    }

    public static Set<NewEntityState> adaptCollectionToNewEntityStates(Collection<? extends Entity> entities) {
        return entities.stream().
                map(entity -> entity.adaptToNewAnimatedEntityState(true)).
                collect(Collectors.toSet());
    }

    public static Set<NewLineState> adaptCollectionToNewLineStates(Collection<? extends Entity> entities) {
        return entities.stream().
                map(entity -> entity.adaptToNewLineState()).
                collect(Collectors.toSet());
    }


    private List<Integer> getAllConnectionIDs() {
        return AppServer.currentGame.getPlayers().stream().
                map(Player::getConnectionID).
                collect(Collectors.toList());
    }

    public synchronized void putEntity(Entity entity) {
        if (RegistryHandler.entityRegistryServer.getItemID(entity) == null) return;
        entityDestinationsReturnBuffer.put(entity, getAllConnectionIDs());
    }

    public synchronized void putEntity(Entity entity, List<Integer> connectionIDs) {
        if (RegistryHandler.entityRegistryServer.getItemID(entity) == null) return;
        entityDestinationsReturnBuffer.put(entity, connectionIDs);
    }

    public synchronized void putEntity(Entity entity, Integer... connectionIDs) {
        if (RegistryHandler.entityRegistryServer.getItemID(entity) == null) return;
        entityDestinationsReturnBuffer.put(entity, Arrays.asList(connectionIDs));
    }

    public synchronized void removeEntity(Entity entity) {
        this.entityDestinationsReturnBuffer.remove(entity);
    }

}
