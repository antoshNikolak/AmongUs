package Game;

import Client.Client;
import ConnectionServer.ConnectionServer;
import Entity.Entity;
import Packet.Position.EntityState;
import Packet.Position.StateReturn;
import State.LobbyState;
import State.State;
import State.StateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import Entity.*;
import State.*;
import Entity.Player;
import Entity.EntityDestination;

public class Game {
    private final StateManager stateManager = new StateManager();
    private final EntityReturnBuffer entityReturnBuffer = new EntityReturnBuffer();
    //    private final Set<Entity> entityReturnBuffer = ConcurrentHashMap.newKeySet();
//    private final Map<Entity, List<Integer>> entityDestinationsReturnBuffer = new ConcurrentHashMap<>();
    protected final List<Client> clients = new ArrayList<>();


    public void startGame() {
        init();
        startGameLoop();
    }

    public void init() {
        stateManager.pushState(new LobbyState());
    }

    private void startGameLoop() {
        new GameLoop(30) {
            @Override
            public void handle() {
                stateManager.update();
                sendGameState();
            }
        }.start();
    }

    private void sendGameState() {
        this.entityReturnBuffer.sendGameState();

    }


    public List<Player> getPlayers() {
        return clients.stream().
                map(Client::getPlayer)
                .collect(Collectors.toList());
    }

    public synchronized StateManager getStateManager() {
        return stateManager;
    }

//        public Set<Entity> getEntityReturnBuffer () {
//            return entityReturnBuffer;
//        }

    public List<Client> getClients() {
        return clients;
    }

    public EntityReturnBuffer getEntityReturnBuffer() {
        return entityReturnBuffer;
    }

    //    public Map<Entity, List<Integer>> getEntityDestinationsReturnBuffer() {
//        return entityDestinationsReturnBuffer;
//    }
}
