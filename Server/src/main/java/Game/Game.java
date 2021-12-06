package Game;

import Client.Client;
import StartUpServer.AppServer;
import State.LobbyState;
import State.StateManager;

import java.util.ArrayList;
import java.util.List;

import Entity.*;
import Entity.Player;

public class Game {
    private final StateManager stateManager = new StateManager();
    private final EntityReturnBuffer entityReturnBuffer = new EntityReturnBuffer();
    private final List<Player> players = new ArrayList<>();
//    private boolean running = true;
    private GameLoop gameLoop;

    private final Object lock = new Object();

    public void startGame() {
        init();
        startGameLoop();
    }

    public  void stopGame() {//todo doc stopping game and syncing
        synchronized (lock) {
            removeClientPlayers();
            this.gameLoop.setRunning(false);
//            AppServer.currentGame = null;
            System.out.println("set current game to null");
        }
    }

    public void removeClientPlayers(){
        for (Client client: AppServer.getClients()){
            if (client.getPlayer() != null && players.contains(client.getPlayer())){
               client.setPlayer(null);
            }
        }
//        AppServer.getClients().stream().
//                map(Client::getPlayer).
//                filter(players::contains).
//                forEach(player -> player = null);

    }

    public void init() {
        stateManager.pushState(new LobbyState());
    }

    private void startGameLoop() {
        this.gameLoop = new GameLoop(30) {
            @Override
            public  void handle() {//todo document syncing
                synchronized (lock) {
                    if (this.isRunning()) {
                        stateManager.update();
                        sendGameState();
                    }
                }

//                    if (!this.isRunning()){
//                        AppServer.currentGame = null;
//                    }
//                }
//                else {
//                    System.out.println("setting game to null");
////                    AppServer.currentGame = null;
//                }
            }
        };
        gameLoop.start();
    }
    //TODO dont set game to null, but use is running var all around MAYBE, idk if thats what i want

    private void sendGameState() {
        this.entityReturnBuffer.sendGameState();
    }

    public synchronized StateManager getStateManager() {
        return stateManager;
    }

    public EntityReturnBuffer getEntityReturnBuffer() {
        return entityReturnBuffer;
    }

    public List<Player> getPlayers() {
        return players;
//        return AppServer.getClients().stream().
//                map(Client::getPlayer)
//                .collect(Collectors.toList());
    }
}
