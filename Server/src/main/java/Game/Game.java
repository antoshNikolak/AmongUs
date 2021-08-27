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
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Game {
    private final StateManager stateManager = new StateManager();
    private final Set<Entity> entityReturnBuffer = ConcurrentHashMap.newKeySet();
    protected final List<Client> clients = new ArrayList<>(); //maybe make this a player list


    public void startGame() {
        init();
        startGameLoop();
//        startSendStateLoop();
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



//    private void startSendStateLoop() {
//        new GameLoop(5) {
//            @Override
//            public void handle() {
//                sendGameState();
//            }
//        }.start();
//    }

    private void sendGameState() {
        Set<EntityState> entityReturnStates = getEntityReturnStates();
        ConnectionServer.sendUDPToAllPlayers(new StateReturn(entityReturnStates));
        entityReturnBuffer.clear();
    }

    private Set<EntityState> getEntityReturnStates(){
        return entityReturnBuffer.stream().
                map(entity -> entity.adaptToEntityState()).
                collect(Collectors.toSet());
    }

    public State getCurrentState(){
        return stateManager.getCurrentState();
    }

    public Set<Entity> getEntityReturnBuffer() {
        return entityReturnBuffer;
    }

    public List<Client> getClients() {
        return clients;
    }
}
